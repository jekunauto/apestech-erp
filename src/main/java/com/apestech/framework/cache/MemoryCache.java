package com.apestech.framework.cache;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能：内存缓存类
 *
 * @author xul
 * @create 2017-12-02 16:52
 */
public class MemoryCache<K, V> implements Cache<K, V> {

    private volatile static MemoryCache instance = null;
    private static Map cache = new HashMap();

    private MemoryCache() {
    }

    public static MemoryCache getInstance() {
        if (instance == null) {
            synchronized (MemoryCache.class) {
                if (instance == null) {
                    instance = new MemoryCache();
                }
            }
        }
        return instance;
    }

    @Override
    public V get(K key) {
        return (V) cache.get(key);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
    }

    @Override
    public V get(String cache, K key) {
        Map m = (Map) instance.get(cache);
        if (m == null) {
            m = new ConcurrentHashMap();
        }
        return (V) m.get(key);
    }

    @Override
    public void put(String cache, K key, V value) {
        Map m = (Map) instance.get(cache);
        if (m == null) {
            m = new ConcurrentHashMap();
        }
        m.put(key, value);
    }

    @Override
    public void remove(String cache, K key) {
        Map m = (Map) instance.get(cache);
        if (m == null) {
            return;
        }
        m.remove(key);
    }
}


