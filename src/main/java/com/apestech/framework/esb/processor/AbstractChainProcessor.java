package com.apestech.framework.esb.processor;

import com.apestech.framework.esb.api.Message;

/**
 * 功能：处理器抽象类
 *
 * @author xul
 * @create 2017-12-02 13:46
 */
public abstract class AbstractChainProcessor<T extends Message> implements ChainProcessor<T> {

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
    public void process(T data) {
        doProcess(data);
        if (processor != null) {
            processor.process(data);
        }
    }

    protected abstract void doProcess(T data);
}
