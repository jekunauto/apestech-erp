package com.apestech.framework.esb.processor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apestech.framework.esb.api.Message;
import com.apestech.framework.mq.Channel;
import com.apestech.framework.mq.domain.MQueue;
import com.apestech.framework.util.SpringManager;

import java.util.List;
import java.util.Map;

/**
 * 功能：消息生产者处理器类
 *
 * @author xul
 * @create 2017-12-07 17:30
 */
public class ProducerProcessor<T extends Message> extends AbstractChainProcessor<T> {

    private String topic;
    private String description;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    protected void doProcess(T data) {
        String body;
        Object record = data.getData();
        if (record instanceof Map) {
            body = JSONObject.toJSONString(record);
        } else if (record instanceof String) {
            body = (String) record;
        } else if (record instanceof List) {
            body = JSONArray.toJSONString(record);
        } else {
            body = String.valueOf(record);
        }
        Channel channel = SpringManager.getBean("messageChannel");
        MQueue message = new MQueue();
        message.setTopic(topic);
        message.setDescription(description);
        message.setBody(body);
        channel.publish(message);
    }

}
