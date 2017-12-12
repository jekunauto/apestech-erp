package com.apestech.framework.esb.parsing.definition;

import com.apestech.framework.esb.parsing.parser.Parser;

import java.util.Map;

/**
 * 功能：组件定义接口
 *
 * @author xul
 * @create 2017-12-01 15:29
 */
public interface ComponentDefinition {

    /*
     * 设置组件元数据
     */
    void setAttribute(String key, String value);

    String getAttribute(String key);

    /*
     * 设置子组件
     */
    void setNestedComponent(ComponentDefinition component);

    ComponentDefinition[] getNestedComponents();

    /*
     * 设置组件名
     */
    void setName(String name);

    String getName();

    void setParser(Parser parser);

    Parser getParser();

    Map<String, String> getAttributes();

}
