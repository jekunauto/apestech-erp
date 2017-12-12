package com.apestech.framework.esb.processor.router;

import com.alibaba.fastjson.JSONObject;
import com.apestech.framework.esb.api.Message;
import com.apestech.framework.esb.processor.AbstractChainProcessor;
import com.apestech.framework.esb.processor.ChainProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 功能：Router处理器类
 *
 * @author xul
 * @create 2017-12-04 18:46
 */
public class RouterProcessor<T extends Message> extends AbstractChainProcessor<T> {

    private List<FilterProcessor> filters = new ArrayList();
    private FilterProcessor other = null;

    public void setOther(FilterProcessor filter) {
        other = filter;
    }

    public void addFilter(FilterProcessor filter) {
        filters.add(filter);
    }

    @Override
    protected void doProcess(T data) {
        if ((filters.isEmpty()) && (other == null)) {
            return;
        }
        Map row = null;
        Object record = data.getData();
        if (record instanceof Map) {
            row = (Map) record;
        } else if (record instanceof String) {
            if (((String) record).startsWith("{")) {
                row = JSONObject.parseObject((String) record);
            }
        }
        if (row == null) {
            throw new RuntimeException("record element Expected. Got - " + record.getClass());
        }

        boolean isExecute = false;
        for (FilterProcessor filter : filters) {
            if (filter.execute(row)) {
                ChainProcessor processor = filter.getProcessor();
                if (processor == null) {
                    continue;
                }
                processor.process(data);
                isExecute = true;
                break;
            }
        }
        if ((!isExecute) && (other != null)) {
            if (other.execute(row)) {
                ChainProcessor processor = other.getProcessor();
                if (processor != null) {
                    processor.process(data);
                }
                isExecute = true;
            }
        }
        if (!isExecute) {
            throw new RuntimeException("路由中表达式不正确！");
        }
    }

}
