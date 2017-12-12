package com.apestech.framework.mq.store;

import com.apestech.framework.mq.domain.MQueue;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QueueRepository extends MongoRepository<MQueue, String> {

}
