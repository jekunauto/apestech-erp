package com.apestech.rbac.service;

import com.apestech.framework.esb.api.SimpleRequest;
import com.apestech.framework.util.LockUtil;
import com.apestech.framework.util.MD5Util;
import com.apestech.rbac.domain.Post;
import com.apestech.rbac.domain.User;
import com.apestech.rbac.repository.UserRepository;
import com.apestech.rop.session.SimpleSession;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.EntryObject;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
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
        SimpleSession session;
        if (sessions.size() == 0) {
            sessionId = lockUtil.getSessionId();
            session = new SimpleSession();
            session.setUserId(request.get("userId"));
            session.setUser(user);
            session.setIp(request.getRopRequestContext().getIp());
            session.setSessionId(sessionId);
        } else if (sessions.size() == 1) {
            session = ((SimpleSession) sessions.toArray()[0]);
            sessionId = session.getSessionId();
        } else {
            throw new RuntimeException("登陆策略错误。");
        }
        request.getRopRequestContext().addSession(sessionId, session);
        Map result = new HashedMap();
        result.put("sessionId", sessionId);
        result.put("user", user);
        return result;
    }

    public Post bindPost(SimpleRequest request) {
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
        return post;
    }

    public void logout(SimpleRequest request) {
        request.getRopRequestContext().removeSession();
    }

}
