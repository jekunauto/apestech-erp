package com.apestech.framework.mq.store;

import com.apestech.framework.mq.Publisher;
import com.apestech.framework.mq.domain.MQueue;
import com.apestech.framework.mq.domain.MQueueBak;
import com.apestech.framework.util.SpringManager;
import com.apestech.framework.util.Tools;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * 功能：存储服务
 *
 * @author xul
 * @create 2017-12-07 16:34
 */
@Component("mqStoreService")
public class MQStoreService {

    private static final String CONST_COLLECTION_NAME = "MQueue";

    @Autowired
    private Environment env;

    @Autowired
    private MongoTemplate template;

    public MQueue save(MQueue message) {
        QueueRepository repository = SpringManager.getBean("queueRepository");
        return repository.save(message);
    }

    public MQueue findOne(String id) {
        QueueRepository repository = SpringManager.getBean("queueRepository");
        return repository.findOne(id);
    }

    public void delete(MQueue message) {
        QueueRepository repository = SpringManager.getBean("queueRepository");
        repository.delete(message);
    }

    public void backup(MQueue message) {
        QueueBakRepository repository = SpringManager.getBean("queueBakRepository");
        MQueueBak mqb = new MQueueBak();
        Tools.beanConvert(mqb, message);
        mqb.setState(2);
        repository.save(mqb);
    }

    public List<String> getTopics() {
        List<String> topics = template.getCollection(CONST_COLLECTION_NAME).distinct("topic");
        return topics != null ? topics : new ArrayList<String>();
    }

    public void invoke(String topic, Publisher publisher) {
        String maxEnded = getMaxEnded(topic);
        if (maxEnded == null) return;

        DBObject ref = new BasicDBObject();
        ref.put("topic", topic);
        BasicDBObject bo = new BasicDBObject();
        bo.put("$lte", maxEnded);
        ref.put("ended", bo);
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "created"));

        int size = getSize();
        int lastSize = size;
        while (lastSize == size) {
            Query query = new BasicQuery(ref).with(sort).limit(size); //.skip(i * size)
            List<MQueue> messages = template.find(query, MQueue.class);
            lastSize = messages.size();
            for (MQueue message : messages) {
                publisher.send(message);
            }
        }
    }

    private int getSize() {
        String size = env.getProperty("mq.size");
        if (Tools.isNull(size)) {
            size = "1000";
        }
        return Integer.valueOf(size);
    }

    private String getMaxEnded(String topic) {
        DBObject ref = new BasicDBObject();
        ref.put("topic", topic);
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "ended"));
        Query query = new BasicQuery(ref).with(sort).limit(1);
        MQueue result = template.findOne(query, MQueue.class);
        if (result == null) return null;
        return result.getEnded();
    }


}
