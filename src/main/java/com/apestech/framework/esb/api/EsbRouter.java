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

    public OapResponse process(Request request) {
        String method = request.getRopRequestContext().getMethod();
        String version = request.getRopRequestContext().getVersion();
        Processor<Request> processor = getProcessor(method, version);
        processor.process(request);
        return new OapResponse().setBody(request.getData());
    }

    @Transactional()
    public OapResponse invoke(Request request) {
        return process(request);
    }

}
