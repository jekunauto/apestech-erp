package com.apestech.framework.esb.parsing.definition;

import com.apestech.framework.esb.parsing.parser.Parser;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 功能：复合组件定义类
 *
 * @author xul
 * @create 2017-12-01 17:26
 */
public class CompositeComponentDefinition implements ComponentDefinition {

    private final List<ComponentDefinition> nestedComponents = new LinkedList();
    private final Map<String, String> attributes = new HashMap<>();
    private Parser parser;
    private String name;

    @Override
    public void setAttribute(String key, String value) {
        Assert.notNull(key, "key must not be null");
        this.attributes.put(key, value);
    }

    @Override
    public String getAttribute(String key) {
        return this.attributes.get(key);
    }

    @Override
    public void setNestedComponent(ComponentDefinition component) {
        Assert.notNull(component, "ComponentDefinition must not be null");
        this.nestedComponents.add(component);
    }

    @Override
    public ComponentDefinition[] getNestedComponents() {
        return this.nestedComponents.toArray(new ComponentDefinition[this.nestedComponents.size()]);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setParser(Parser parser) {
        this.parser = parser;
    }

    @Override
    public Parser getParser() {
        return this.parser;
    }

    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("name: ").append(this.name);
        return sb.toString();
    }
}
