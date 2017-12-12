package com.apestech.framework.mq;

import com.apestech.framework.mq.domain.MQueue;
import com.apestech.oap.event.RopEventListener;

public interface Channel extends RopEventListener<MQEvent> {

    void publish(MQueue message);

    void invoke();
}
