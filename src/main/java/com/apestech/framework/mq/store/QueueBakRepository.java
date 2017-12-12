package com.apestech.framework.mq.store;


import com.apestech.framework.mq.domain.MQueueBak;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QueueBakRepository extends MongoRepository<MQueueBak, String> {
}
