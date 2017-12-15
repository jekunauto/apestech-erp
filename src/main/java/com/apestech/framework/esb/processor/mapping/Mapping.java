package com.apestech.framework.esb.processor.mapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apestech.framework.esb.api.Request;
import com.apestech.framework.esb.processor.converter.AbstractConverter;
import com.apestech.framework.esb.processor.mapping.config.Config;
import com.apestech.framework.esb.processor.mapping.config.Field;
import com.apestech.framework.esb.processor.mapping.config.JKConfig;
import com.apestech.framework.util.CacheUtil;
import com.apestech.framework.util.Tools;
import com.ql.util.express.DefaultContext;
import org.springframework.util.Assert;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 功能：Mapping类
 *
 * @author xul
 * @create 2017-12-02 15:58
 */
public class Mapping<T extends Request, R> extends AbstractConverter<T, R> {

    private String configKey = null;
    private boolean clone = false;

    public Mapping() {
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public void setClone(boolean clone) {
        this.clone = clone;
    }

    @Override
    public R convert(T source) {
        Assert.notNull(configKey, this.getClass().getName() + ": configKey must not be null.");
        Config config = JKConfig.getConfig(configKey);
        Object data = source.getData();

        if (data instanceof String) {
            if (((String) data).startsWith("[")) {
                data = JSONArray.parseArray((String) data);
            } else if (((String) data).startsWith("{")) {
                JSONObject jsonObject = JSONObject.parseObject((String) data);
                Map row = new LinkedCaseInsensitiveMap();
                row.putAll(jsonObject);
                data = row;
            } else {
                throw new RuntimeException("Mapping输入数据格式不正确。");
            }
        }

        if (data instanceof Map) {
            return (R) mapping(config, (Map) data);
        } else if (data instanceof List) {
            List rows = new ArrayList();
            for (Object o : ((List) data)) {
                rows.add(mapping(config, (Map) o));
            }
            return (R) rows;
        } else if (data instanceof Object[]) {
            List rows = new ArrayList();
            for (Object o : ((Object[]) data)) {
                rows.add(mapping(config, (Map) o));
            }
            return (R) rows.toArray();
        } else {
            throw new RuntimeException("source element Expected. Got - " + source.getClass());
        }
    }

    protected Map clone(Map source) {
        if (clone) {
            Map desc = new LinkedCaseInsensitiveMap();
            desc.putAll(source);
            return desc;
        }
        return null;
    }

    private Object mapping(Config config, Map source) {
        Object o;
        Map target = clone(source);
        if (target == null) {
            target = new LinkedCaseInsensitiveMap();
        }
        List fields = config.getFields();
        for (Iterator it = fields.iterator(); it.hasNext(); ) {
            o = it.next();
            if (o instanceof Config) {
                mappingConfig((Config) o, source, target);
            } else if (o instanceof Field) {
                mappingField((Field) o, source, target);
            }
        }
        if (Tools.isNull(config.getClassName())) {
            return target;
        }
        try {
            return Tools.toBean(Class.forName(config.getClassName()), target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object mapping(Config config, List source) {
        List target = new ArrayList();
        for (Object o : source) {
            if (o instanceof Map) {
                target.add(mapping(config, (Map) o));
            } else if (o instanceof List) {
                target.add(mapping(config, (List) o));
            }
        }
        return target;
    }

    private void mappingConfig(Config config, Map source, Map target) {
        Object key = getKey(config);
        Object value = source.get(config.getId());
        isEmpty(config, value);
        if (value instanceof Map) {
            target.put(key, mapping(config, (Map) value));
        } else if (value instanceof List) {
            target.put(key, mapping(config, (List) value));
        } else if (value instanceof Object[]) {
            List result = new ArrayList();
            for (Object data : (Object[]) value) {
                result.add(mapping(config, (Map) data));
            }
            target.put(key, result.toArray());
        }
    }

    private void mappingField(Field field, Map source, Map target) {
        Object value = source.get(field.getAttrname());
        value = (!Tools.isNull(value)) ? value : field.getDefaultValue();
        isEmpty(field, value);
        if (value != null) {
            value = convert(field, value);
            Object key = getKey(field);
            check(field.getCheck(), key, value, Tools.isNull(field.getName()) ? field.getAttrname() : field.getName());
            target.put(key, value);
        }
    }

    private Object convert(Field field, Object value) {
        if (field.getFieldtype() != null) {
            try {
                value = field.conver(value);
            } catch (Exception e) {
                throw new RuntimeException(field.getAttrname() + "节点值[" + value + "]转换类型[" + field.getFieldtype() + "]报错！");
            }
        }
        if (field.getLength() != null) {
            if (String.valueOf(value).length() > Integer.valueOf(field.getLength())) {
                throw new RuntimeException(field.getAttrname() + "节点值[" + value + "]长度不允许超过[" + field.getLength() + "]！");
            }
        }
        return value;
    }

    private void isEmpty(Field field, Object value) {
        if (Tools.isNull(value)) {
            String allowNull = field.getAllownull() == null ? "Y" : field.getAllownull();
            if (allowNull.toUpperCase().equals("N")) {
                String alert = String.format("%s不允许为空！", Tools.isNull(field.getName()) ? field.getAttrname() : field.getName());
                throw new RuntimeException(alert);
            }
        }
    }

    private void isEmpty(Config o, Object value) {
        boolean isnull = false;
        if (value == null) {
            isnull = true;
        } else if (value instanceof List) {
            if (((List) value).isEmpty()) {
                isnull = true;
            }
        }
        if (isnull) {
            String allowNull = o.getAllownull() == null ? "Y" : o.getAllownull();
            if (allowNull.toUpperCase().equals("N")) {
                String name = o.getName();
                String alert = String.format("%s不允许为空！", Tools.isNull(name) ? o.getId() : name);
                throw new RuntimeException(alert);
            }
        }
    }

    private void check(String condition, Object key, Object value, String description) {
        if (Tools.isNull(condition)) {
            return;
        }
        DefaultContext context = new DefaultContext();
        context.put(key, value);
        Object o;
        try {
            o = CacheUtil.getEngine().execute(condition, context);
        } catch (Exception e) {
            throw new RuntimeException(String.format("表达式：%s 在上下文：%s 中执行错误！", condition, context));
        }

        if (!(o instanceof Boolean)) {
            throw new RuntimeException("表达式：" + condition + " 定义错误！");
        }

        if (o.equals(false)) {
            throw new RuntimeException(String.format("%s 的输入值: %s 有误，不符合检查条件：%s ！", description, value, condition));
        }

    }

    private Object getKey(Object o) {
        if (o instanceof Field) {
            return isNull(((Field) o).getTarget(), ((Field) o).getAttrname());
        } else if (o instanceof Config) {
            return isNull(((Config) o).getTarget(), ((Config) o).getId());
        }
        return null;
    }

    private Object isNull(Object value, Object defaultValue) {
        if (value instanceof String) {
            return !Tools.isNull(value.toString()) ? value : defaultValue;
        } else {
            return (value != null) ? value : defaultValue;
        }
    }

}
