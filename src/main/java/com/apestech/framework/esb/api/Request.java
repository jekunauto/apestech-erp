package com.apestech.framework.esb.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface Request {

    Object getData();

    Request setData(Object o);

    Map getMap();

    List getList();

    String getS();

    JSONObject getJO();

    JSONArray getJA();

    <T> T get(String key);

    <T> T getO(Class<T> type);

    <T> T getO(String key, Class<T> type);

    List getA(Class type);

    List getA(Class type, boolean isNest);

    List getA(String key, Class type);

    List getA(String key, Class type, boolean isNest);
}
