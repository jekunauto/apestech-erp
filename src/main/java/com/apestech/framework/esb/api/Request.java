package com.apestech.framework.esb.api;

import com.apestech.oap.AbstractRopRequest;

import javax.validation.constraints.NotNull;


public class Request extends AbstractRopRequest implements Message {

    @NotNull
    private String body;

    private Object data;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public Object getData() {
        return (data == null) ? this.body : data;
    }

    @Override
    public Message setData(Object o) {
        this.data = o;
        return this;
    }

}