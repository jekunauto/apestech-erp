package com.apestech.framework.esb.processor.connector.http;

import com.apestech.framework.esb.processor.connector.Transporter;

import java.util.Map;

/**
 * 功能：Http传输器类
 *
 * @author xul
 * @create 2017-12-05 20:04
 */
public class HttpTransporter extends Transporter {

    private String url;

    Map<String, String> headers = null;

    public HttpTransporter(){
        super();
        setConnector(new HttpClientConnector());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        ((HttpClientConnector) this.getConnector()).setUrl(url);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
        ((HttpClientConnector) this.getConnector()).setHeaders(headers);
    }

}
