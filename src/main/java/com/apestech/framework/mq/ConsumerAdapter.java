package com.apestech.framework.mq;

import com.apestech.framework.esb.api.SimpleRequest;
import com.apestech.framework.esb.api.Request;
import com.apestech.framework.esb.processor.Processor;
import com.apestech.oap.RopRequestContext;
import com.apestech.oap.impl.SimpleRopRequestContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 功能：消费者适配器
 *
 * @author xul
 * @create 2017-12-07 18:50
 */

@Component("consumerAdapter")
public class ConsumerAdapter {

    public boolean consume(Processor processor, MQEvent ropEvent, boolean isTransaction) {
        RopRequestContext context = new SimpleRopRequestContext(ropEvent.getRopContext());
        SimpleRequest message = new SimpleRequest();
        message.setRopRequestContext(context);
        message.setBody(ropEvent.getMessage().getBody());
        if (isTransaction) {
            return process(processor, message);
        } else {
            return invoke(processor, message);
        }
    }

    @Transactional()
    public boolean process(Processor processor, Request request) {
        return invoke(processor, request);
    }

    private boolean invoke(Processor processor, Request request) {
        processor.process(request);
        return true;
    }


}
