package com.apestech.framework.esb.processor;

import com.apestech.framework.esb.api.Request;

/**
 * 功能：处理器接口
 *
 * @author xul
 * @create 2017-12-01 15:29
 */
public interface Processor<T extends Request, R> {

    R process(T data);
}
