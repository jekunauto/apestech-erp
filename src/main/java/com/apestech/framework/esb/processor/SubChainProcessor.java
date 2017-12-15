package com.apestech.framework.esb.processor;

import com.apestech.framework.esb.api.EsbRouter;
import com.apestech.framework.esb.api.Request;
import com.apestech.framework.util.SpringManager;
import org.springframework.util.Assert;

/**
 * 功能：子链处理器类
 *
 * @author xul
 * @create 2017-12-04 16:46
 */
public class SubChainProcessor<T extends Request, R> extends AbstractChainProcessor<T, R> {

    private String method; //编号
    private String version; //版本

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    protected R doProcess(T data) {
        try {
            Processor processor = ((EsbRouter) SpringManager.getBean("esbRouter")).getProcessor(method, version);
            Assert.notNull(processor, this.getClass().getName() + ": "  + " 链："  + method + " V" + version + "定义。");
            return (R) processor.process(data);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }


}
