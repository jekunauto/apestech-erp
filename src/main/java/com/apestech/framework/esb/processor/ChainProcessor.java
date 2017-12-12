package com.apestech.framework.esb.processor;

import com.apestech.framework.esb.api.Message;

/**
 * 功能：链处理器接口
 *
 * @author xul
 * @create 2017-12-01 15:29
 */
public interface ChainProcessor<T extends Message> extends Processor<T> {

    ChainProcessor setProcessor(ChainProcessor processor);

    ChainProcessor getProcessor();
}
