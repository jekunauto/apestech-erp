package com.apestech.framework.util;


import com.apestech.framework.cache.MemoryCache;
import com.apestech.framework.express.ExpressEngine;
import com.apestech.framework.express.IEngine;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 功能：缓存工具方法类
 *
 * @author xul
 * @create 2017-12-02 16:54
 */
public class CacheUtil {

    public static IEngine getEngine() {
        IEngine runner = (IEngine) MemoryCache.getInstance().get("ExpressEngine");
        if (runner == null) {
            runner = new ExpressEngine();
            MemoryCache.getInstance().put("ExpressEngine", runner);
        }
        return runner;
    }

    private static Lock getLock(String key) {
        Lock lock = (Lock) MemoryCache.getInstance().get("Lock_" + key);
        if (lock == null) {
            lock = new ReentrantLock();
            MemoryCache.getInstance().put("Lock_" + key, lock);
        }
        return lock;
    }

    /*
     * 判断该队列是否可以执行（单位：分钟）
     */
    public static boolean isRunTopic(String topic) {
        String key = "TopicPeriod_" + topic;
        Map<String, Map> topics = getTopics();
        Map row = topics.get(key);
        if (row == null) {
            return false;
        }
        Date date = new Date(((Date) row.get("date")).getTime() + ((Long) row.get("period")).intValue() * 60 * 1000);
        Date now = new Date();
        if (now.getTime() < date.getTime()) {
            return false;
        }
        row.put("date", now);
        return true;
    }

    public static Map<String, Map> getTopics() {
        String key = "TopicThreadExecutor";
        Lock lock = getLock(key);
        lock.lock();// 得到锁
        try {
            Map topics = (Map) MemoryCache.getInstance().get(key);
            if (topics == null) {
                topics = new HashMap();
                MemoryCache.getInstance().put(key, topics);
            }
            return topics;
        } finally {
            lock.unlock();// 释放锁
        }
    }

    public static Map getTopic(String topic) {
        String key = "TopicPeriod_" + topic;
        Map<String, Map> topics = getTopics();
        Map row = topics.get(key);
        return row;
    }

    /*
     * 设置每个消息队列异常消息的处理时间间隔（单位：分钟）
     */
    public static void setTopicPeriod(String topic, int period) {
        String key = "TopicPeriod_" + topic;
        Map<String, Map> topics = getTopics();
        Map row = topics.get(key);
        if (row == null) {
            row = new HashMap();
            topics.put(key, row);
        }
        row.put("key", key);
        row.put("period", Long.valueOf(period));
        row.put("date", new Date());
        row.put("isRunning", false);
        row.put("topicThreadExecutor", Executors.newSingleThreadExecutor());
    }

}
