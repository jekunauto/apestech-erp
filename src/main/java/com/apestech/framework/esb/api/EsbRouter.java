package com.apestech.framework.esb.api;

import com.apestech.framework.esb.parsing.definition.ComponentDefinition;
import com.apestech.framework.esb.processor.Processor;
import com.apestech.oap.response.OapResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能：Esb路由入口
 *
 * @author xul
 * @create 2017-12-04 16:56
 */
@Component("esbRouter")
public class EsbRouter {
    private static final Map<String, ComponentDefinition> routers = new HashMap();

    public Processor getProcessor(String method, String version) {
        ComponentDefinition componentDefinition = getComponentDefinition(method, version);
        Processor processor = (Processor) componentDefinition.getParser().parse();
        return processor;
    }

    private ComponentDefinition getComponentDefinition(String method, String version) {
        return routers.get(methodWithVersion(method, version));
    }

    private static String methodWithVersion(String method, String version) {
        return method + "#" + version;
    }

    private static void setRouter(String key, ComponentDefinition componentDefinition){
        routers.put(key, componentDefinition);
    }

    public static void setRouter(String method, String version, ComponentDefinition componentDefinition){
        setRouter(methodWithVersion(method, version), componentDefinition);
    }

    public static Map<String, ComponentDefinition> getRouters() {
        return routers;
    }

    public OapResponse process(SimpleRequest request) {
        String method = request.getRopRequestContext().getMethod();
        String version = request.getRopRequestContext().getVersion();
        Processor<SimpleRequest, Object> processor = getProcessor(method, version);
        Object result = processor.process(request);
        return new OapResponse().setBody(result);
    }

    @Transactional()
    public OapResponse invoke(SimpleRequest request) {
        return process(request);
    }

}
