package com.apestech.framework.cache;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * 功能：Hazelcast缓存类
 *
 * @author xul
 * @create 2017-12-11 14:50
 */
@Service
public class HazelcastCache<K, V> implements Cache<K, V> {
    private static final String CONST_DEFAULT_CACHENAME = "default";

    @Autowired
    private CacheManager cacheManager;

    @Override
    public V get(K key) {
        org.springframework.cache.Cache.ValueWrapper vw = getCache().get(key);
        if(vw == null) return null;
        return (V) vw.get();
    }

    @Override
    public void put(K key, V value) {
        getCache().put(key, value);
    }

    @Override
    public void remove(K key) {
        getCache().evict(key);
    }

    private org.springframework.cache.Cache getCache(){
        return cacheManager.getCache(CONST_DEFAULT_CACHENAME);
    }
}
