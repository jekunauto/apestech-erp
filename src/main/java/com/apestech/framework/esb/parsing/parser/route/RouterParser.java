package com.apestech.framework.esb.parsing.parser.route;

import com.apestech.framework.esb.parsing.definition.ComponentDefinition;
import com.apestech.framework.esb.parsing.parser.Parser;
import com.apestech.framework.esb.processor.router.FilterProcessor;
import com.apestech.framework.esb.processor.router.RouterProcessor;

/**
 * 功能：router组件解析器
 *
 * @author xul
 * @create 2017-12-04 10:38
 */
public class RouterParser<T> implements Parser {

    private ComponentDefinition componentDefinition;

    public RouterParser(ComponentDefinition componentDefinition) {
        this.componentDefinition = componentDefinition;
    }

    @Override
    public T parse() {
        RouterProcessor processor = new RouterProcessor();
        for (ComponentDefinition componentDefinition : componentDefinition.getNestedComponents()) {
            if (componentDefinition.getName().equals("if")) {
                processor.addFilter((FilterProcessor) componentDefinition.getParser().parse());
            } else if (componentDefinition.getName().equals("other")) {
                processor.setOther((FilterProcessor) componentDefinition.getParser().parse());
            }
        }
        return (T) processor;
    }
}
