package com.apestech.scm.service;

import com.apestech.framework.cache.HazelcastCache;
import com.apestech.framework.esb.api.Request;
import com.apestech.framework.util.LockUtil;
import com.apestech.framework.util.SpringManager;
import com.apestech.oap.annotation.IgnoreSignType;
import com.apestech.oap.annotation.NeedInSessionType;
import com.apestech.oap.annotation.ServiceMethod;
import com.apestech.oap.annotation.ServiceMethodBean;
import com.apestech.oap.response.OapResponse;
import com.apestech.scm.model.User;
import com.apestech.scm.repository.UserRepository;
import com.apestech.scm.request.LogonRequest;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * 功能：服务测试
 *
 * @author xul
 * @create 2017-11-17 8:58
 */
@ServiceMethodBean(version = "1.0", needInSession = NeedInSessionType.NO)
public class UserService {
    private static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private HazelcastCache cache;

    @Transactional()
    @ServiceMethod(method = "user.logon")
    public OapResponse<User> logon(LogonRequest request) {
        UserRepository repository = SpringManager.getBean("userRepository");
        User user = new User("userName");
        user = repository.save(user);
        return new OapResponse<User>().setBody(user);
    }

    @Autowired
    private LockUtil lockUtil;
    /**
     * 功能：事务传递性测试(service => service)
     */
    @Transactional()
    @ServiceMethod(method = "user.save", ignoreSign = IgnoreSignType.YES) //, ignoreSign = IgnoreSignType.YES
    public OapResponse<?> save(Request request) {
        Lock lock = lockUtil.getDistributedLock("test"); //后期如果部署多台机器，需要使用分布式锁
        lock.lock();// 得到锁
        try {
            UserRepository repository = SpringManager.getBean("userRepository");
            User user = new User("userName");
            repository.save(user);
            AppSecretService service = SpringManager.getBean("appSecretService");
            cache.put("test", "hello xul!");
            System.out.println(cache.get("test"));

            return service.save(request);
        } finally {
            lock.unlock();// 释放锁
        }
    }

    public OapResponse send(Request request) {

        List<Map> rows = new ArrayList();
        for (int i = 0; i < 1000000; i++) {
            Map row = new HashedMap();
            row.put("body", i);
            rows.add(row);
        }
        return new OapResponse().setBody(rows);
    }

}
