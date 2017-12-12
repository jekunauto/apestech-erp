package com.apestech.framework.express;

public interface IEngine<T, K, R> {

    public R execute(T express, K context) throws Exception;
}
