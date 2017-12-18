package com.apestech.framework.esb.api;


import com.apestech.framework.esb.parsing.EsbFileParser;
import com.apestech.framework.esb.parsing.definition.ComponentDefinition;
import com.apestech.framework.esb.processor.mapping.config.JKConfig;
import com.apestech.framework.mq.Scheduler;
import com.apestech.framework.util.Tools;
import com.apestech.oap.*;
import com.apestech.oap.annotation.*;
import com.apestech.oap.event.AfterStartedRopEvent;
import com.apestech.oap.event.RopEventListener;
import com.apestech.oap.response.OapResponse;
import org.apache.log4j.Logger;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EsbEventListener implements RopEventListener<AfterStartedRopEvent> {
    protected final Logger logger = Logger.getLogger(getClass());

    public void onRopEvent(AfterStartedRopEvent ropRopEvent) {
        if (logger.isDebugEnabled()) {
            logger.debug("开始解析Mapping配置文件...");
            logger.debug((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
        }
        new JKConfig().init();
        if (logger.isDebugEnabled()) {
            logger.debug((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
            logger.debug("完成解析Mapping配置文件...");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("开始解析Esb配置文件...");
            logger.debug((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
        }
        new EsbFileParser().init();
        if (logger.isDebugEnabled()) {
            logger.debug((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
            logger.debug("完成解析Esb配置文件...");
        }

        register(ropRopEvent.getRopContext());

        Scheduler scheduler = getBean(ropRopEvent.getRopContext(), "scheduler");
        scheduler.run(ropRopEvent.getRopContext().getApplicationContext());
    }

    private void register(RopContext context) {
        EsbRouter.getRouters().forEach((key, componentDefinition) -> {
            try {
                ServiceMethodDefinition definition = buildServiceMethodDefinition(componentDefinition);
                ServiceMethodHandler serviceMethodHandler = new ServiceMethodHandler();
                serviceMethodHandler.setServiceMethodDefinition(definition);
                List<String> ignoreSignFieldNames = new ArrayList<>();
                ignoreSignFieldNames.add("messageFormat");
                ignoreSignFieldNames.add("sign");
                serviceMethodHandler.setIgnoreSignFieldNames(ignoreSignFieldNames);
                Object handler = getBean(context, "esbRouter");
                serviceMethodHandler.setHandler(handler); // handler
                Method method = getMethod(componentDefinition, handler);
                serviceMethodHandler.setHandlerMethod(method); // handler'method

                Class<?> returnType = method.getReturnType();
                // 验证返回参数必须是OapResponse.class
                if (!ClassUtils.isAssignable(OapResponse.class, returnType)) {
                    throw new RopException(method.getDeclaringClass().getName() + "." + method.getName() + "的返回必须是" + OapResponse.class.getName());
                }

                // handler method's parameter
                Class<?> paramType = method.getParameterTypes()[0];
                if (!ClassUtils.isAssignable(RopRequest.class, paramType)) {
                    throw new RopException(method.getDeclaringClass().getName() + "." + method.getName() + "的入参必须是" + RopRequest.class.getName());
                }
                boolean ropRequestImplType = !(paramType.isAssignableFrom(RopRequest.class) || paramType.isAssignableFrom(AbstractRopRequest.class));
                serviceMethodHandler.setRopRequestImplType(ropRequestImplType);
                serviceMethodHandler.setRequestType((Class<? extends RopRequest>) paramType);

                context.addServiceMethod(definition.getMethod(), definition.getVersion(), serviceMethodHandler);

                if (logger.isDebugEnabled()) {
                    logger.debug("注册服务方法：" + key);
                }
            } catch (Exception e) {
                logger.error("注册服务方法：" + key + " 错误：" + e);
            }
        });
    }

    private Method getMethod(ComponentDefinition value, Object handler) {
        String parameterType = "com.apestech.framework.esb.api.SimpleRequest";
        boolean isTransaction = "true".equals(value.getAttribute("isTransaction").toString().toLowerCase());
        try {
            return handler.getClass().getMethod(isTransaction ? "invoke" : "process", Class.forName(parameterType));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ServiceMethodDefinition buildServiceMethodDefinition(ComponentDefinition componentDefinition) {
        ServiceMethodDefinition definition = Tools.toBean(ServiceMethodDefinition.class, componentDefinition.getAttributes());
        definition.setHttpAction(new HttpAction[]{HttpAction.GET, HttpAction.POST});
        return definition;
    }

    private <T> T getBean(RopContext context, String name) {
        return (T) context.getApplicationContext().getBean(name);
    }

    public int getOrder() {
        return 1;
    }
}

