package com.apestech.framework.util;

import com.apestech.framework.cache.MemoryCache;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 功能：锁
 *
 * @author xul
 * @create 2017-12-11 17:41
 */
@Component("lockUtil")
public class LockUtil {

    @Qualifier("hazelcastInstance")
    @Autowired
    private HazelcastInstance hazelcastInstance;

    public Lock getLocalLock(String key) {
        Lock lock = (Lock) MemoryCache.getInstance().get("Lock_" + key);
        if (lock == null) {
            lock = new ReentrantLock();
            MemoryCache.getInstance().put("Lock_" + key, lock);
        }
        return lock;
    }

    public Lock getDistributedLock(String key){
        key = "Lock_" + key;
        return hazelcastInstance.getLock(key);
    }

}
