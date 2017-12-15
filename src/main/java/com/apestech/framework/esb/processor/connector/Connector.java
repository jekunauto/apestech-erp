package com.apestech.framework.esb.processor.connector;

import com.apestech.framework.esb.api.Request;
import com.apestech.framework.esb.processor.Processor;

/**
 * 功能：连接器接口
 *
 * @author xul
 * @create 2017-12-02 14:35
 */
public interface Connector<T extends Request, R> extends Processor<T, R> {

    void connect();

    void disconnect();
}
