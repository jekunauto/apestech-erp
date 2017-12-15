package com.apestech.framework.esb.parsing;

import com.apestech.framework.esb.api.EsbRouter;
import com.apestech.framework.esb.parsing.definition.ComponentDefinition;
import com.apestech.framework.esb.parsing.definition.CompositeComponentDefinition;
import com.apestech.framework.esb.parsing.parser.ActionParser;
import com.apestech.framework.esb.parsing.parser.ChainParser;
import com.apestech.framework.esb.parsing.parser.NormalParser;
import com.apestech.framework.esb.parsing.parser.Parser;
import com.apestech.framework.esb.parsing.parser.route.RouterParser;
import com.apestech.framework.esb.processor.*;
import com.apestech.framework.esb.processor.connector.http.HttpTransporter;
import com.apestech.framework.esb.processor.mapping.Mapping;
import com.apestech.framework.esb.processor.mapping.config.Resources;
import com.apestech.framework.esb.processor.router.FilterProcessor;
import com.apestech.framework.mq.MessageChannel;
import com.apestech.framework.mq.MessageConsumer;
import com.apestech.framework.util.Tools;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;

/**
 * 功能：Esb文件解析器
 *
 * @author xul
 * @create 2017-12-05 14:05
 */
public class EsbFileParser {
    protected final Logger logger = Logger.getLogger(getClass());

    public void init() {
        try {
            SAXReader reader = new SAXReader();
            refreshFile(reader, this.getClass().getResource("/com/apestech").getPath());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    public void refreshFile(SAXReader reader, String strPath) throws Exception {
        strPath = URLDecoder.decode(strPath, "utf-8");//解决路径有空格问题
        File dir = new File(strPath);
        File[] files = dir.listFiles();
        String fileName = null;
        InputStream is = null;
        if (files == null) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                refreshFile(reader, files[i].getAbsolutePath());
            } else {
                fileName = "";
                is = null;
                String[] paths = files[i].getAbsolutePath().split("classes");
                if (paths.length == 1) {
                    fileName = paths[0];
                    if (fileName.toLowerCase().endsWith("_esb.xml")) {
                        is = new FileInputStream(fileName);
                    }
                } else if (paths.length == 2) {
                    fileName = paths[1];
                    if (fileName.toLowerCase().endsWith("_esb.xml")) {
                        is = Resources.getResourceAsStream(fileName);
                        if (logger.isDebugEnabled()) {
                            logger.debug(fileName);
                        }
                    }
                }
                if (is != null) {
                    Document document = reader.read(is);
                    Element root = document.getRootElement();
                    Iterator<Element> iterator = root.elementIterator();
                    while (iterator.hasNext()) {
                        Element element = iterator.next();
                        try {
                            listNodes(element, null);
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                        }
                    }
                }
            }
        }
    }

    //遍历当前节点下的所有节点
    public void listNodes(Element node, ComponentDefinition root) {
        ComponentDefinition component = createComponentDefinition(node.getName());
        if (root != null && !component.getName().equals("consumer")) root.setNestedComponent(component);

        //首先获取当前节点的所有属性节点
        List<Attribute> list = node.attributes();
        //遍历属性节点
        for (Attribute attribute : list) {
            component.setAttribute(attribute.getName(), attribute.getValue());
        }

        if (component.getName().equals("chain")) {
            EsbRouter.setRouter(component.getAttribute("method").toString(), component.getAttribute("version").toString(), component);
        } else if (component.getName().equals("consumer")) {
            subscribe(component);
        }
        createParser(component);
        Iterator<Element> iterator = node.elementIterator();
        while (iterator.hasNext()) {
            Element e = iterator.next();
            listNodes(e, component);
        }
    }

    private void subscribe(ComponentDefinition component) {
        MessageConsumer consumer = Tools.toBean(MessageConsumer.class, component.getAttributes());
        consumer.setComponentDefinition(component);
        MessageChannel.addConsumer(consumer);
    }

    private ComponentDefinition createComponentDefinition(String name) {
        ComponentDefinition component = new CompositeComponentDefinition();
        component.setName(name);
        return component;
    }

    private void createParser(ComponentDefinition componentDefinition) {
        Parser parser = null;
        String name = componentDefinition.getName();
        if (name.equals("chain")) {
            parser = new ChainParser(componentDefinition, SampleChainProcessor.class);
        } else if (name.equals("subchain")) {
            parser = new NormalParser(componentDefinition, SubChainProcessor.class);
        } else if (name.equals("producer")) {
            parser = new NormalParser(componentDefinition, ProducerProcessor.class);
        } else if (name.equals("consumer")) {
            parser = new ChainParser(componentDefinition, ConsumerProcessor.class);
        } else if (name.equals("router")) {
            parser = new RouterParser(componentDefinition);
        } else if (name.equals("if")) {
            parser = new ChainParser(componentDefinition, FilterProcessor.class);
        } else if (name.equals("other")) {
            componentDefinition.setAttribute("condition", "1==1");
            parser = new ChainParser(componentDefinition, FilterProcessor.class);
        } else if (name.equals("action")) {
            parser = new ActionParser(componentDefinition);
        } else if (name.equals("processor")) {
            String componentName = componentDefinition.getAttribute("component");
            componentDefinition.setName(componentName);
            if (componentName.equals("mapping")) {
                parser = new NormalParser(componentDefinition, Mapping.class);
            } else if (componentName.equals("http")) {
                parser = new NormalParser(componentDefinition, HttpTransporter.class);
            } else if (componentName.equals("splitter")) {
                parser = new ChainParser(componentDefinition, SplitterProcessor.class);
            }
            //...
        }
        Assert.notNull(parser, "解析器没有设置！");
        componentDefinition.setParser(parser);
    }
}
