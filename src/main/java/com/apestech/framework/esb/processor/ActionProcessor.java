package com.apestech.framework.esb.processor;

import com.apestech.framework.esb.api.Message;
import com.apestech.oap.response.OapResponse;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * 功能：action处理器类
 *
 * @author xul
 * @create 2017-12-04 13:41
 */
public class ActionProcessor<T extends Message> extends AbstractChainProcessor<T> {
    private Object target;
    private Method method;

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    protected void doProcess(T data) {
        Object result;
        Assert.isTrue(!(method.getParameterTypes().length > 1), target.getClass() + "." + getMethod().getName() + ": 方法无效。");
        if (method.getParameterTypes().length == 1) {
            result = ReflectionUtils.invokeMethod(method, target, data);
        } else {
            result = ReflectionUtils.invokeMethod(method, target);
        }
        if (result instanceof OapResponse) {
            OapResponse response = ((OapResponse) result);
            String code = response.getHeader().getCode();
            code = code != null ? code : "success";
            if (!code.equals("success")) {
                throw new RuntimeException(response.getBody().toString());
            }
            result = response.getBody();
        }
        data.setData(result);
    }

}
