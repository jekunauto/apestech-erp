package com.apestech.framework.mq;

import com.apestech.framework.event.Listener;
import com.apestech.framework.mq.domain.MQueue;
import com.apestech.framework.mq.store.MQStoreService;
import com.apestech.framework.util.CacheUtil;
import com.apestech.framework.util.DateUtil;
import com.apestech.framework.util.LockUtil;
import com.apestech.framework.util.SpringManager;
import com.apestech.oap.ServiceRouter;
import com.apestech.oap.event.RopEventMulticaster;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;

/**
 * 功能：Channel实现类
 *
 * @author xul
 * @create 2017-12-07 14:14
 */
@Listener("messageChannel")
public class MessageChannel implements Channel {
    protected final Logger logger = Logger.getLogger(getClass());

    private static Map<String, Consumer> consumers = new HashMap();

    @Autowired
    MQStoreService storeService;

    @Autowired
    private LockUtil lockUtil;

    public static void addConsumer(Consumer consumer) {
        String topic = consumer.getTopic();
        if (consumers.containsKey(topic)) {
            return;
        }
        consumers.put(topic, consumer);
        CacheUtil.setTopicPeriod(topic, consumer.getPeriod());
    }

    public static void removeConsumer(Consumer consumer) {
        consumers.remove(consumer.getTopic());
    }

    @Override
    public void invoke() {
        storeService.getTopics().forEach(this::accept);
    }

    private void accept(String topic) {
        if (!CacheUtil.isRunTopic(topic)) return;
        final Map row = CacheUtil.getTopic(topic);
        if (row == null) return;
        ExecutorService topicThreadExecutor = (ExecutorService) row.get("topicThreadExecutor");
        topicThreadExecutor.execute(new Runnable() {
            public void run() {
                try {
                    invoke(topic);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private void invoke(String topic) {
                storeService.invoke(topic, new Publisher() {

                    @Override
                    public void send(MQueue message) {
                        publish(message);
                    }
                });
            }
        });
    }

    @Override
    public void publish(MQueue message) {
        Lock lock = lockUtil.getLocalLock(message.getTopic());
        lock.lock();// 得到锁
        try {
            if (message.getState() == 0) {
                message = storeService.save(message);
            }
            ServiceRouter serviceRouter = getServiceRouter();
            RopEventMulticaster multicaster = serviceRouter.getRopContext().getEventMulticaster();
            MQEvent event = new MQEvent(this, serviceRouter.getRopContext(), message);
            multicaster.multicastEvent(event);
        } finally {
            lock.unlock();// 释放锁
        }
    }

    private ServiceRouter getServiceRouter() {
        Object routers = SpringManager.getBean(ServiceRouter.class);
        return (ServiceRouter) ((Map) routers).get("router");
    }

    /**
     * 响应事件：消息消费
     *
     * @param ropEvent
     */
    @Override
    public void onRopEvent(MQEvent ropEvent) {
        MQueue message = ropEvent.getMessage();
        String topic = message.getTopic();
        boolean isConsumed = false;
        boolean isBackup = true;
        Lock lock = lockUtil.getDistributedLock(topic); //后期如果部署多台机器，需要使用分布式锁
        lock.lock();// 得到锁
        try {
            if (isConsumed(message)) return; //防止消息重复消费
            if (consumers.containsKey(topic)) {
                Consumer consumer = consumers.get(topic);
                isBackup = consumer.isBackup();
                try {
                    isConsumed = consumer.consume(ropEvent);
                } catch (Exception e) {
                    message.setError(e.getMessage());
                }
            } else {
                message.setError("该消息没有消费者！");
            }
            message.setState(1);
            message.setEnded(DateUtil.formatTime());
            if (isConsumed) {
                storeService.delete(message);
                if (isBackup) {
                    storeService.backup(message);
                }
            } else {
                storeService.save(message);
            }
        } finally {
            lock.unlock();// 释放锁
        }
    }

    private boolean isConsumed(MQueue message) {
        MQueue oMessage = storeService.findOne(message.getId());
        if (oMessage == null) {
            return true; //消息已经消费
        }

        if (message.getEnded().equals(oMessage.getEnded())) {
            return false; //消息没有消费
        }
        return true;
    }

    /**
     * 执行的顺序号
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }

}
