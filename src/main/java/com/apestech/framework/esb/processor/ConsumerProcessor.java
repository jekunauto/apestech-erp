package com.apestech.framework.esb.processor;

import com.apestech.framework.esb.api.Message;

/**
 * 功能：消息消费者处理器类
 *
 * @author xul
 * @create 2017-12-07 18:06
 */
public class ConsumerProcessor<T extends Message> extends AbstractChainProcessor<T> {

    @Override
    protected void doProcess(T data) {

    }
}
