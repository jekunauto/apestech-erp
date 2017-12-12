package com.apestech.framework.esb.parsing.parser;

import com.apestech.framework.util.SpringManager;
import com.apestech.framework.esb.parsing.definition.ComponentDefinition;
import com.apestech.framework.esb.processor.ActionProcessor;
import com.apestech.framework.esb.processor.ChainProcessor;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能：action组件解析器
 *
 * @author xul
 * @create 2017-12-04 10:21
 */
public class ActionParser implements Parser {

    private ComponentDefinition componentDefinition;

    public ActionParser(ComponentDefinition componentDefinition) {
        this.componentDefinition = componentDefinition;
    }

    @Override
    public ChainProcessor parse() {
        final String name = componentDefinition.getAttribute("component");
        Assert.notNull(name, this.getClass().getName() + ": 组件名参数不允许为空。");
        Object target;
        try {
            target = SpringManager.getBean(name);
            Assert.notNull(target,  this.getClass().getName() + ": " + name + " 在spring容器中没有定义。");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String methodName = componentDefinition.getAttribute("method");
        Assert.notNull(methodName, this.getClass().getName() + ": 方法名参数不允许为空。");
        if (target instanceof ChainProcessor) {
            ChainProcessor processor = (ChainProcessor) target;
            return processor;
        } else {
            final List<ChainProcessor> processors = new ArrayList();
            ReflectionUtils.doWithMethods(target.getClass(), new ReflectionUtils.MethodCallback() {
                public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                    ReflectionUtils.makeAccessible(method);
                    if (method.getParameterTypes().length > 1) {
                        throw new RuntimeException(method.getDeclaringClass().getName() + "." + method.getName() + "的入参只能一个或无入参。");
                    } else if (method.getParameterTypes().length == 1) {
                        Class<?> paramType = method.getParameterTypes()[0];
                        String parameterType = componentDefinition.getAttribute("parameterType");
                        if (parameterType == null) {
                            setProcessor(method);
                        } else {
                            Class<?> clazz;
                            try {
                                clazz = Class.forName(parameterType);
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            if (paramType == clazz) {
                                setProcessor(method);
                            }
                        }
                    }
                }

                private void setProcessor(Method method) {
                    ActionProcessor actionProcessor = new ActionProcessor();
                    actionProcessor.setTarget(target);
                    actionProcessor.setMethod(method);
                    processors.add(actionProcessor);
                }
            }, new ReflectionUtils.MethodFilter() {
                public boolean matches(Method method) {
                    return method.getName().equals(methodName);
                }
            });
            Assert.isTrue(!processors.isEmpty(), target.getClass() + "." + methodName + ": 方法无效。");
            return processors.get(0);
        }
    }
}
