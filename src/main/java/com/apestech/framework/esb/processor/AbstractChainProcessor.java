package com.apestech.framework.esb.processor;

import com.apestech.framework.esb.api.Request;

/**
 * 功能：处理器抽象类
 *
 * @author xul
 * @create 2017-12-02 13:46
 */
public abstract class AbstractChainProcessor<T extends Request, R> implements ChainProcessor<T, R> {

    protected ChainProcessor processor = null;

    public AbstractChainProcessor() {
        super();
    }

    public ChainProcessor getProcessor() {
        return processor;
    }

    public ChainProcessor setProcessor(ChainProcessor processor) {
        if (this.processor == null) {
            this.processor = processor;
        } else {
            this.processor.setProcessor(processor);
        }
        return this;
    }

    @Override
    public R process(T data) {
        R result = doProcess(data);
        if (processor != null) {
            if (result != null) {
                data.setData(result);
            }
            result = (R) processor.process(data);
        }
        return result;
    }

    protected abstract R doProcess(T data);
}
