package com.apestech.framework.esb.parsing.parser.processor;

import com.apestech.framework.esb.parsing.definition.ComponentDefinition;
import com.apestech.framework.esb.parsing.parser.Parser;
import com.apestech.framework.esb.processor.ChainProcessor;

/**
 * 功能：processor组件解析器
 *
 * @author xul
 * @create 2017-12-04 10:23
 */
public class ProcessorParser implements Parser {

    private ComponentDefinition componentDefinition;

    public ProcessorParser(ComponentDefinition componentDefinition) {
        this.componentDefinition = componentDefinition;
    }

    @Override
    public ChainProcessor parse() {
        return null;
    }
}
