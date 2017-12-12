package com.apestech.framework.esb.processor.mapping.config;

import java.util.ArrayList;
import java.util.List;

public class Config implements java.io.Serializable {

    private String id = null;
    private String target = null;
    private List fields = new ArrayList();
    private Config parent = null;
    private String description = null;
    private String name = null;
    private String allownull = null;
    private String className = null; //Map to Bean 使用

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
        

    public List getFields() {
        return fields;
    }

    public void setFields(List fields) {
        this.fields = fields;
    }

    public void addField(Field field) {
        this.fields.add(field);
    }

    public Config getParent() {
        return parent;
    }

    public void setParent(Config parent) {
        this.parent = parent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAllownull() {
        return allownull;
    }

    public void setAllownull(String allownull) {
        this.allownull = allownull;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
