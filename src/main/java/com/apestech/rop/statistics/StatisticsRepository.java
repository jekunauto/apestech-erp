package com.apestech.rop.statistics;

import com.apestech.framework.mq.domain.MQueue;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatisticsRepository extends MongoRepository<Statistics, String> {

}
