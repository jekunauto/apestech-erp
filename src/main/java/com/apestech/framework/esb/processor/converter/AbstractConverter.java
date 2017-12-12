package com.apestech.framework.esb.processor.converter;

import com.apestech.framework.esb.api.Message;
import com.apestech.framework.esb.processor.AbstractChainProcessor;
import org.springframework.util.Assert;

/**
 * 功能：转换器抽象类
 *
 * @author xul
 * @create 2017-12-02 14:35
 */
public abstract class AbstractConverter<T extends Message> extends AbstractChainProcessor<T> implements Converter<T> {

    public AbstractConverter() {
        super();
    }

    @Override
    public void doProcess(T data) {
        Assert.notNull(data, this.getClass().getName() + ": data parameter must not be null.");
        convert(data);
    }

}
