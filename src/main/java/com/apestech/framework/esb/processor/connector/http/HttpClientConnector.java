package com.apestech.framework.esb.processor.connector.http;

import com.apestech.framework.esb.api.Message;
import com.apestech.framework.esb.processor.connector.Connector;
import com.apestech.framework.util.Tools;
import com.apestech.oap.Constants;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 功能：Http连接器类：HttpClient实现
 *
 * @author xul
 * @create 2017-12-02 15:13
 */
public class HttpClientConnector<T extends Message> implements Connector<T> {

    protected String url = null;
    private String enc = Constants.UTF8;
    private final int BUFFER_SIZE = 4096;
    private Integer timeout = 60 * 1000;
    protected HttpClient client;
    private PostMethod postMethod;
    private Map<String, String> headers = null;

    public HttpClientConnector() {
    }

    public HttpClientConnector(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public void connect() {
        client = new HttpClient();
        client.setTimeout(timeout);
        url = Tools.replace(url);
        postMethod = new UTF8PostMethod(url);
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                postMethod.addRequestHeader(header.getKey(), header.getValue());
            }
        }
    }

    @Override
    public void disconnect() {
        if (postMethod != null) {
            postMethod.releaseConnection();
        }
    }

    @Override
    public void process(T data) {
        Object body = data.getData();
        Assert.notNull(body, this.getClass().getName() + ": body must not be null.");
        if (body instanceof Map) {
            NameValuePair[] nameValuePairs = MapToNameValuePair((Map) body);
            postMethod.setRequestBody(nameValuePairs);
        } else if (body instanceof String) {
            postMethod.setRequestBody((String) body);
        } else {
            throw new RuntimeException("Batch element Expected. Got - " + body.getClass());
        }
        Object result = null;
        try {
            int statusCode = client.executeMethod(postMethod);
            if (statusCode != HttpStatus.SC_OK) {
                throw new RuntimeException("Method failed: " + postMethod.getStatusLine());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while write from HTTP POST! " + e.getMessage());
        }
        try {
            InputStream input = postMethod.getResponseBodyAsStream();
            result = InputStreamToString(input);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading response from HTTP POST! " + e.getMessage());
        }
        data.setData(result);
    }

    private String InputStreamToString(InputStream input) {
        byte[] data = InputStreamToByteArray(input);
        try {
            return new String(data, enc);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] InputStreamToByteArray(InputStream in) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            int count = -1;
            byte[] data = new byte[BUFFER_SIZE];
            while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
                outStream.write(data, 0, count);
            }
            return outStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private NameValuePair[] MapToNameValuePair(Map data) {
        NameValuePair[] result = new NameValuePair[data.size()];
        List<NameValuePair> values = new ArrayList();
        for (Object key : data.keySet()) {
            NameValuePair value = new NameValuePair();
            value.setName(String.valueOf(key));
            value.setValue(String.valueOf(data.get(key)));
            values.add(value);
        }
        return values.toArray(result);
    }

    //Inner class for UTF-8 support
    class UTF8PostMethod extends PostMethod {

        public UTF8PostMethod(String url) {
            super(url);
        }

        @Override
        public String getRequestCharSet() {
            //return super.getRequestCharSet();
            return enc;
        }
    }
}
