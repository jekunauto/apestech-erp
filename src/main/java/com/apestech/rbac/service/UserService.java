package com.apestech.rbac.service;

import com.apestech.framework.jpa.dynamic.CriteriaFactory;
import com.apestech.framework.jpa.dynamic.Criteria;
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
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
        User user = userRepository.findOne(Integer.valueOf(request.get("id")));
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
            if (o.getId().equals(request.get("postId"))) {
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


    /**
     * 功能：查询动态参数测试
     *
     * @param request
     */
    public List<User> findAll(SimpleRequest request) {
        List<Map> filters = request.get("condition");
        Criteria<User> criteria = CriteriaFactory.toCriteria(filters);
        return userRepository.findAll(criteria);
    }

    @Autowired
    EntityManager em;

    public List findUser(SimpleRequest request) {
        String sql = "SELECT user0_.id       AS id1_15_," +
                "       user0_.name     AS name2_15_," +
                "       user0_.password AS password3_15_," +
                "       user0_.userid   AS userid4_15_" +
                "  FROM aut_users user0_";
        Query query = em.createNativeQuery(sql);
        // Query 接口是 spring-data-jpa 的接口，而 SQLQuery 接口是 hibenate 的接口，这里的做法就是先转成 hibenate 的查询接口对象，然后设置结果转换器
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

}
