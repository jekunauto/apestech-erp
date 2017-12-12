package com.apestech.framework.mq;

import com.apestech.framework.esb.parsing.definition.ComponentDefinition;
import com.apestech.framework.esb.processor.Processor;
import com.apestech.framework.util.SpringManager;

/**
 * 功能：消费者
 *
 * @author xul
 * @create 2017-12-07 17:07
 */
public class MessageConsumer implements Consumer {
    private ComponentDefinition componentDefinition;
    private String topic;
    private boolean isBackup = true;
    private boolean isTransaction = false;
    private int period; //异常消息处理间隔时间（单位：分钟）

    public ComponentDefinition getComponentDefinition() {
        return componentDefinition;
    }

    public void setComponentDefinition(ComponentDefinition componentDefinition) {
        this.componentDefinition = componentDefinition;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public boolean isBackup() {
        return isBackup;
    }

    public void setIsBackup(boolean backup) {
        isBackup = backup;
    }

    public boolean isTransaction() {
        return isTransaction;
    }

    public void setIsTransaction(boolean transaction) {
        isTransaction = transaction;
    }

    @Override
    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    @Override
    public boolean consume(MQEvent ropEvent) {
        ConsumerAdapter adapter = SpringManager.getBean("consumerAdapter");
        Processor processor = (Processor) componentDefinition.getParser().parse();
        return adapter.consume(processor, ropEvent, isTransaction);
    }
}
