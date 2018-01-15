package com.apestech.framework.esb.parsing.parser;

import com.apestech.framework.esb.parsing.definition.ComponentDefinition;
import com.apestech.framework.esb.processor.ChainProcessor;
import com.apestech.framework.util.Tools;

/**
 * 功能：chain组件解析器
 *
 * @author xul
 * @create 2017-12-04 10:18
 */
public class ChainParser implements Parser<ChainProcessor> {

    private ComponentDefinition componentDefinition;
    private Class<ChainProcessor> processorClazz;

    public ChainParser(ComponentDefinition componentDefinition, Class processorClazz) {
        this.componentDefinition = componentDefinition;
        this.processorClazz = processorClazz;
    }

    @Override
    public ChainProcessor parse() {
        ChainProcessor processor = Tools.map(componentDefinition.getAttributes(), processorClazz);
        for (ComponentDefinition componentDefinition : componentDefinition.getNestedComponents()) {
            processor.setProcessor((ChainProcessor) componentDefinition.getParser().parse());
        }
        return processor;
    }
}
