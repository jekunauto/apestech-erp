package com.apestech.framework.mq;

import com.apestech.framework.mq.domain.MQueue;

public interface Publisher {

    void send(MQueue message);
}
