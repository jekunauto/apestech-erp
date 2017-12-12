package com.apestech.framework.mq;

public interface Consumer {

    String getTopic();

    boolean isBackup();

    int getPeriod();

    boolean consume(MQEvent ropEvent);
}
