package com.apestech.framework.cache;

public interface Cache<K, V> {
    V get(K key);

    void put(K key, V value);

    void remove(K key);

    V get(String cache, K key);

    void put(String cache, K key, V value);

    void remove(String cache, K key);
}
