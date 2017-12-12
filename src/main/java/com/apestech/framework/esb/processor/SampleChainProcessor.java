package com.apestech.framework.esb.processor;

import com.apestech.framework.esb.api.Message;

/**
 * 功能：基础处理器类
 *
 * @author xul
 * @create 2017-12-04 11:00
 */
public class SampleChainProcessor<T extends Message> extends AbstractChainProcessor<T> {

    private String id; //编号
    private String version; //版本
    private String description; //描述
    private String concurrency; //是不允许并发
    private String log;  //是否保存业务日志
    private String transaction = ""; //注入数据库事务

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(String concurrency) {
        this.concurrency = concurrency;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

//    public DataSourceType getDataSourceType() throws Exception{
//        return SpringTransaction.getDataSourceType(transaction);
//    }


    @Override
    protected void doProcess(T data) {

    }
}
