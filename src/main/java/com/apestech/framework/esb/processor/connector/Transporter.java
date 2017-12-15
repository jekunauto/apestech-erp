package com.apestech.framework.esb.processor.connector;

import com.apestech.framework.esb.api.Request;
import com.apestech.framework.esb.processor.AbstractChainProcessor;
import org.springframework.util.Assert;

/**
 * 功能：传输器类
 *
 * @author xul
 * @create 2017-12-02 15:17
 */
public class Transporter<T extends Request, R> extends AbstractChainProcessor<T, R> {

    private Connector connector;

    public Transporter() {
    }

    public Transporter(Connector connector) {
        this.connector = connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public Connector getConnector() {
        return connector;
    }

    @Override
    protected R doProcess(T data) {
        Assert.notNull(connector, this.getClass().getName() + ": connector must not be null.");
        try {
            connector.connect();
            return (R) connector.process(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            connector.disconnect();
        }
    }
}
