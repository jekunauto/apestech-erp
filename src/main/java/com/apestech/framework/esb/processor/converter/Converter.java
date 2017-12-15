package com.apestech.framework.esb.processor.converter;

import com.apestech.framework.esb.api.Request;

/**
 * 功能：转换器接口
 *
 * @author xul
 * @create 2017-12-02 14:35
 */
public interface Converter<T extends Request, R> {

    R convert(T record);

}
