package com.apestech.framework.mq;

import com.apestech.framework.mq.domain.MQueue;
import com.apestech.oap.RopContext;
import com.apestech.oap.event.OapEvent;

/**
 * 功能：Queue事件
 *
 * @author xul
 * @create 2017-12-07 15:06
 */
public class MQEvent extends OapEvent {
    private MQueue message;

    public MQEvent(Object source, RopContext ropContext, MQueue message) {
        super(source, ropContext);
        this.message = message;
    }

    public MQueue getMessage() {
        return message;
    }
}
