package com.apestech;

import com.apestech.oap.AbstractRopRequest;

import javax.validation.constraints.NotNull;


public class SampleRequest extends AbstractRopRequest {

    @NotNull
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}