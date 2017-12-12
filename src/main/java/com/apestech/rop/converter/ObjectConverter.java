package com.apestech.rop.converter;

import com.apestech.oap.request.OapConverter;


/**
 * 功能：数据类型转换
 *
 * @author xul
 * @create 2017-11-17 9:54
 */
public class ObjectConverter implements OapConverter<String,Object> {
    @Override
    public String unconvert(Object target) {
        return null;
    }

    @Override
    public Class<String> getSourceClass() {
        return null;
    }

    @Override
    public Class<Object> getTargetClass() {
        return null;
    }

    @Override
    public Object convert(String s) {
        return null;
    }
}
