package com.apestech.framework.esb.processor;

import com.apestech.framework.esb.api.Message;

import java.util.*;

/**
 * 功能：Splitter处理器类
 *
 * @author xul
 * @create 2017-12-06 9:16
 */
public class SplitterProcessor<T extends Message> extends AbstractChainProcessor<T> {

    @Override
    public void process(T data) {
        List rows;
        List result;
        Object record = data.getData();
        if (record instanceof Map) {
            rows = new ArrayList();
            rows.add(record);
            result = exec(data, rows);
            if (result.size() > 0) {
                data.setData(result.get(0));
            }
        } else if (record instanceof List) {
            rows = (List) record;
            result = exec(data, rows);
            data.setData(result);
        } else if (record instanceof Object[]) {
            rows = Arrays.asList(record);
            result = exec(data, rows);
            data.setData(result.toArray());
        } else {
            throw new RuntimeException("record element Expected. Got - " + record.getClass());
        }
    }

    private List exec(T data, List rows) {
        List result = new ArrayList();
        for (Object row : rows) {
            Object o = null;
            try {
                data.setData(row);
                super.process(data);
                o = data.getData();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            result.add(o);
        }
        return result;
    }

    @Override
    protected void doProcess(T data) {
    }
}
