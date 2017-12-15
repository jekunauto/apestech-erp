package com.apestech.framework.esb.processor.router;

import com.apestech.framework.esb.api.Request;
import com.apestech.framework.esb.processor.AbstractChainProcessor;
import com.apestech.framework.util.CacheUtil;
import com.apestech.framework.util.Tools;
import com.ql.util.express.DefaultContext;

import java.util.Map;

/**
 * 功能：Filter处理器类
 *
 * @author xul
 * @create 2017-12-05 8:55
 */
public class FilterProcessor<T extends Request, R> extends AbstractChainProcessor<T, R> {
    private String condition;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public boolean execute(Map row) {
        Object o;
        try {
            condition = Tools.replace(condition);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            DefaultContext context = new DefaultContext(row);
            o = CacheUtil.getEngine().execute(condition, context); //条件表达式
        } catch (Exception e) {
            throw new RuntimeException(String.format("表达式：%s执行错误，上下文件环境：%s", condition, row.toString()));
        }
        if (!(o instanceof Boolean)) {
            throw new RuntimeException(String.format("表达式：%s" + " 定义错误！", condition));
        }
        if (o.equals(true)) {
            return true;
        }
        return false;
    }

    @Override
    protected R doProcess(T data) {
        return null;
    }

}
