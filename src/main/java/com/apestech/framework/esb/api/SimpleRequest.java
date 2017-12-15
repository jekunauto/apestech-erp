package com.apestech.framework.esb.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apestech.framework.util.Tools;
import com.apestech.oap.AbstractRopRequest;
import org.springframework.util.Assert;
import org.springframework.util.LinkedCaseInsensitiveMap;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SimpleRequest extends AbstractRopRequest implements Request {

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
    public Request setData(Object o) {
        this.data = o;
        return this;
    }

    @Override
    public Map getMap() {
        return getJO();
    }

    @Override
    public List getList() {
        return getJA();
    }

    @Override
    public String getS() {
        return JSONObject.toJSONString(getData());
    }

    @Override
    public JSONObject getJO() {
        Object o = getJ();
        Assert.isTrue(o instanceof JSONObject, "调用方法：getJO() 错误，数据类型错误。");
        return (JSONObject) o;
    }

    @Override
    public JSONArray getJA() {
        Object o = getJ();
        Assert.isTrue(o instanceof JSONObject, "调用方法：getJA() 错误，数据类型错误。");
        return (JSONArray) o;
    }

    private Object getJ() {
        Object data = getData();
        if (data instanceof String) {
            if (((String) data).startsWith("[")) {
                return JSONArray.parseArray((String) data);
            } else if (((String) data).startsWith("{")) {
                return JSONObject.parseObject((String) data);
            } else {
                throw new RuntimeException("Mapping输入数据格式不正确。");
            }
        } else {
            Object o = JSONObject.toJSON(getData());
            Assert.isTrue(o instanceof JSON, "调用方法：getJ() 错误，数据类型错误。");
            return o;
        }
    }

    @Override
    public <T> T get(String key) {
        Map m = getMap();
        Assert.isTrue(m.containsKey(key), "调用方法：get(String key) 错误，key值: " + key + "不存在。");
        return (T) m.get(key);
    }

    @Override
    public <T> T getO(Class<T> type) {
        Object o = getJ();
        if (o instanceof Map) {
            return Tools.map(o, type);
        } else if (o instanceof List) {
            List rows = new ArrayList();
            for (Map row : (List<Map>) o) {
                rows.add(Tools.map(row, type));
            }
            return (T) rows;
        } else {
            throw new RuntimeException("调用方法：getO() 错误，数据类型错误。");
        }
    }

}