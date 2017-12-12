package com.apestech.framework.esb.api;

import com.apestech.framework.esb.processor.connector.http.HttpClientConnector;
import com.apestech.framework.esb.processor.connector.Transporter;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能：
 *
 * @author xul
 * @create 2017-12-04 9:51
 */
public class ApiTest {

    public static void main(String[] args) throws Exception {

        HttpClientConnector hcc = new HttpClientConnector("http://localhost:8080/router");
        Map row = new HashMap<>();
        row.put("appKey","00001");
        row.put("body","123456");
        row.put("method","user.save");
        row.put("messageFormat","json");
        row.put("version","1.0");
        Request rq = new Request();
        rq.setData(row);
        Transporter transporter = new Transporter(hcc);
        transporter.process(rq);
        System.out.println(rq.getData());
    }
}
