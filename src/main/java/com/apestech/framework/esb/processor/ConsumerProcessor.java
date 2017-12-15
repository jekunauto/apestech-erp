package com.apestech.framework.esb.processor;

import com.apestech.framework.esb.api.Request;

/**
 * 功能：消息消费者处理器类
 *
 * @author xul
 * @create 2017-12-07 18:06
 */
public class ConsumerProcessor<T extends Request, R> extends AbstractChainProcessor<T, R> {

    @Override
    protected R doProcess(T data) {
        return null;
    }
}
