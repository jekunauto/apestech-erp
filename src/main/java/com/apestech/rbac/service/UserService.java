package com.apestech.rbac.service;

import com.apestech.framework.cache.HazelcastCache;
import com.apestech.framework.esb.api.SimpleRequest;
import com.apestech.framework.util.LockUtil;
import com.apestech.framework.util.MD5Util;
import com.apestech.rbac.domain.Role;
import com.apestech.rbac.domain.User;
import com.apestech.rbac.repository.UserRepository;
import com.apestech.rop.session.SimpleSession;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    private CacheManager cacheManager;

    /**
     * 功能：通过Name查找用户
     *
     * @param request
     * @return
     */
    public Map login(SimpleRequest request) {
        User user = userRepository.findByName(request.get("name"));
        Assert.notNull(user, "用户：" + request.get("name") + " 在系统中不存在。");
        String password = MD5Util.encrypt(request.get("password"));
        Assert.isTrue(user.getPassword().equalsIgnoreCase(password), "请输入合法密码。");
        SimpleSession session = new SimpleSession();
        session.setAttribute("userName", request.get("name"));
        session.setAttribute("ip", request.getRopRequestContext().getIp());
        String sessionId = lockUtil.getSessionId();
        request.getRopRequestContext().addSession(sessionId, session);
        Map result = new HashedMap();
        result.put("sessionId", sessionId);

        Collection<String> caches = cacheManager.getCacheNames();
        for (String cache : caches) {
            result.put(cache, cacheManager.getCache(cache));
        }
        return result;
    }


    public Object logout(SimpleRequest request) {
        request.getRopRequestContext().removeSession();

        return null;
    }
}
