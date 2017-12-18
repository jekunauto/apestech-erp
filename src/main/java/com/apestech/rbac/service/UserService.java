package com.apestech.rbac.service;

import com.apestech.framework.cache.HazelcastCache;
import com.apestech.framework.esb.api.SimpleRequest;
import com.apestech.framework.util.LockUtil;
import com.apestech.framework.util.MD5Util;
import com.apestech.oap.session.Session;
import com.apestech.rbac.domain.Post;
import com.apestech.rbac.domain.Role;
import com.apestech.rbac.domain.User;
import com.apestech.rbac.repository.UserRepository;
import com.apestech.rop.session.SimpleSession;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.EntryObject;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import com.hazelcast.query.SqlPredicate;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 功能：用户服务
 *
 * @author xul
 * @create 2017-12-13 16:57
 */
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private LockUtil lockUtil;

    /**
     * 功能：通过ID查找用户
     *
     * @param request
     * @return
     */
    public User findUserById(SimpleRequest request) {
        User user = userRepository.findOne(request.get("id"));
        Assert.notNull(user, "用户：" + request.get("id") + " 在系统中不存在。");
        return user;
    }

    public User save(SimpleRequest request) {
        User user = request.getO(User.class);
        user.setPassword(MD5Util.encrypt(request.get("password")));
        userRepository.save(user);
        return user;
    }

    @Qualifier("hazelcastInstance")
    @Autowired
    private HazelcastInstance hazelcastInstance;

    //http://localhost:8060/router?appKey=00001&body={"name":"CZY0001","password":"1234","roles":[{"id":"00123"}]}&method=user.login&version=1.0

    /**
     * 功能：通过Name查找用户
     *
     * @param request
     * @return
     */
    public Map login(SimpleRequest request) {
        User user = userRepository.findByUserId(request.get("userId"));
        Assert.notNull(user, "用户：" + request.get("userId") + " 在系统中不存在。");
        String password = MD5Util.encrypt(request.get("password"));
        Assert.isTrue(user.getPassword().equalsIgnoreCase(password), "请输入合法密码。");

        String sessionId;
        IMap<String, SimpleSession> map = hazelcastInstance.getMap("sessionCache");
        EntryObject e = new PredicateBuilder().getEntryObject();
        Predicate predicate = e.get("userId").equal(request.get("userId"));
        Collection<SimpleSession> sessions = map.values(predicate);
        if (sessions.size() == 0) {
            sessionId = lockUtil.getSessionId();
            SimpleSession session = new SimpleSession();
            session.setUserId(request.get("userId"));
            session.setUser(user);
            session.setIp(request.getRopRequestContext().getIp());
            session.setSessionId(sessionId);
            request.getRopRequestContext().addSession(sessionId, session);
        } else if (sessions.size() == 1) {
            sessionId = ((SimpleSession) sessions.toArray()[0]).getSessionId();
        } else {
            throw new RuntimeException("登陆策略错误。");
        }
        Map result = new HashedMap();
        result.put("sessionId", sessionId);
        result.put("user", user);
        return result;
    }

    public void bindPost(SimpleRequest request) {
        SimpleSession session = (SimpleSession) request.getRopRequestContext().getSession();
        Post post = null;
        for (Post o : session.getUser().getPosts()) {
            if(o.getId().equals(request.get("postId"))){
                post = o;
                break;
            }
        }
        Assert.notNull(post, "岗位ID：" + request.get("postId") + " 输入错误。");
        session.setPost(post);
    }

    public void logout(SimpleRequest request) {
        request.getRopRequestContext().removeSession();
    }

}
