package com.apestech.scm.service;

import com.apestech.framework.cache.HazelcastCache;
import com.apestech.framework.esb.api.SimpleRequest;
import com.apestech.framework.util.LockUtil;
import com.apestech.framework.util.SpringManager;
import com.apestech.oap.annotation.IgnoreSignType;
import com.apestech.oap.annotation.NeedInSessionType;
import com.apestech.oap.annotation.ServiceMethod;
import com.apestech.oap.annotation.ServiceMethodBean;
import com.apestech.oap.response.OapResponse;
import com.apestech.scm.model.Person;
import com.apestech.scm.repository.PersonRepository;
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
public class PersonService {
    private static Logger log = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    private HazelcastCache cache;

    @Transactional()
    @ServiceMethod(method = "user.logon")
    public OapResponse<Person> logon(LogonRequest request) {
        PersonRepository repository = SpringManager.getBean("personRepository");
        Person user = new Person("userName");
        user = repository.save(user);
        return new OapResponse<Person>().setBody(user);
    }

    @Autowired
    private LockUtil lockUtil;
    /**
     * 功能：事务传递性测试(service => service)
     */
    @Transactional()
    @ServiceMethod(method = "user.save", ignoreSign = IgnoreSignType.YES) //, ignoreSign = IgnoreSignType.YES
    public OapResponse<?> save(SimpleRequest request) {
        Lock lock = lockUtil.getDistributedLock("test");
        lock.lock();// 得到锁
        try {
            PersonRepository repository = SpringManager.getBean("personRepository");
            Person user = new Person("userName");
            repository.save(user);
            AppSecretService service = SpringManager.getBean("appSecretService");
            cache.put("test", "hello xul!");
            System.out.println(cache.get("test"));

            return service.save(request);
        } finally {
            lock.unlock();// 释放锁
        }
    }

    public OapResponse send(SimpleRequest request) {

        List<Map> rows = new ArrayList();
        for (int i = 0; i < 1000000; i++) {
            Map row = new HashedMap();
            row.put("body", i);
            rows.add(row);
        }
        return new OapResponse().setBody(rows);
    }

}
