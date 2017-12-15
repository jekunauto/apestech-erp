package com.apestech.framework.esb.processor;

import com.apestech.framework.esb.api.Request;

/**
 * 功能：基础处理器类
 *
 * @author xul
 * @create 2017-12-04 11:00
 */
public class SampleChainProcessor<T extends Request, R> extends AbstractChainProcessor<T, R> {

    @Override
    protected R doProcess(T data) {
        return null;
    }
}
