package com.apestech.framework.esb.api;

import com.alibaba.fastjson.JSONObject;
import com.apestech.framework.esb.processor.connector.http.HttpClientConnector;
import com.apestech.framework.esb.processor.connector.Transporter;
import com.apestech.framework.util.Tools;
import com.apestech.rbac.domain.Role;
import com.apestech.rbac.domain.User;
import org.apache.commons.collections.map.HashedMap;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.*;

/**
 * 功能：
 *
 * @author xul
 * @create 2017-12-04 9:51
 */
public class ApiTest {

    public static void main(String[] args) throws Exception {

//        HttpClientConnector hcc = new HttpClientConnector("http://localhost:8080/router");
//        Map row = new HashMap<>();
//        row.put("appKey","00001");
//        row.put("body","123456");
//        row.put("method","user.save");
//        row.put("messageFormat","json");
//        row.put("version","1.0");
//        SimpleRequest rq = new SimpleRequest();
//        rq.setData(row);
//        Transporter transporter = new Transporter(hcc);
//        transporter.process(rq);
//        System.out.println(rq.getData());

//        Map row = new HashMap<>();
//        row.put("appKey","00001");
//        row.put("body","123456");
//        row.put("method","user.save");
//        row.put("messageFormat","json");
//        row.put("version","1.0");
//        System.out.println((JSONObject) JSONObject.toJSON(row));

        Map row = new HashMap<>();
        //{"name":"CZY0001","password":"1234","roles":[{"id":"roles01"}]}
        row.put("id","00123");
        row.put("name","CZY0001");
        row.put("password","1234");
        List roles = new ArrayList();
        Map role = new HashedMap();
        role.put("name", "roleName");
        List assignedUsers = new ArrayList();
        Map row1 = new HashMap<>();
        row1.put("name","CZY0001");
        row1.put("password","1234");
        assignedUsers.add(row1);
        role.put("assignedUsers", assignedUsers);
        roles.add(role);
        row.put("roles",roles);
        User user = Tools.map(row, User.class);
        System.out.println(user);

        Map desc = Tools.map(user, Map.class);
        System.out.println(desc);
    }
}
