package com.apestech.framework.esb.parsing.dom4j;

/**
 * 功能：dom4测试
 *
 * @author xul
 * @create 2017-12-01 14:37
 */

import com.apestech.framework.esb.parsing.definition.CompositeComponentDefinition;
import com.apestech.framework.esb.parsing.definition.ComponentDefinition;
import com.apestech.framework.esb.parsing.parser.NormalParser;
import com.apestech.framework.esb.parsing.parser.Parser;
import com.apestech.framework.esb.parsing.parser.ActionParser;
import com.apestech.framework.esb.parsing.parser.ChainParser;
import com.apestech.framework.esb.parsing.parser.route.RouterParser;
import com.apestech.framework.esb.processor.SampleChainProcessor;
import com.apestech.framework.esb.processor.SubChainProcessor;
import com.apestech.framework.esb.processor.router.FilterProcessor;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Dom4JforXML {
    private static Map<String, ComponentDefinition> chains = new HashMap();
    private static Map<String, ComponentDefinition> consumers = new HashMap();

    public static void main(String[] args) throws Exception {
        //创建SAXReader对象
        SAXReader reader = new SAXReader();
        //读取文件 转换成Document
        Document document = reader.read(new File("D:\\work\\apestech\\server\\apestech\\oap-sample\\src\\test\\java\\com\\apestech\\framework\\esb\\parsing\\dom4j\\test_esb.xml"));
        //获取根节点元素对象
        Element root = document.getRootElement();
        //遍历
        Iterator<Element> iterator = root.elementIterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            listNodes(element, null);
        }
        System.out.print("");
    }

    //遍历当前节点下的所有节点
    public static void listNodes(Element node, ComponentDefinition root) {
        System.out.println("当前节点的名称：" + node.getName());
        ComponentDefinition component = createComponentDefinition(node.getName());
        if (root != null && !component.getName().equals("consumer")) root.setNestedComponent(component);

        //首先获取当前节点的所有属性节点
        List<Attribute> list = node.attributes();
        //遍历属性节点
        for (Attribute attribute : list) {
            System.out.println("属性" + attribute.getName() + ":" + attribute.getValue());
            component.setAttribute(attribute.getName(), attribute.getValue());
        }

        //检查组件属性合法性


        if (component.getName().equals("chain")) chains.put(String.valueOf(component.getAttribute("id")), component);
        if (component.getName().equals("consumer")) consumers.put(String.valueOf(component.getAttribute("topic")), component);

        createParser(component);

//        //如果当前节点内容不为空，则输出
//        if (!(node.getTextTrim().equals(""))) {
//            System.out.println(node.getName() + "：" + node.getText());
//        }

        Iterator<Element> iterator = node.elementIterator();
        while (iterator.hasNext()) {
            Element e = iterator.next();
            listNodes(e, component);
        }
    }

    private static ComponentDefinition createComponentDefinition(String name) {
        ComponentDefinition component = new CompositeComponentDefinition();
        component.setName(name);
        return component;
    }

    private static void createParser(ComponentDefinition componentDefinition){
        Parser parser = null;
        String name = componentDefinition.getName();
        if(name.equals("chain")) {
            parser = new ChainParser(componentDefinition, SampleChainProcessor.class);
        } else if(name.equals("subchain")) {
            parser = new NormalParser(componentDefinition, SubChainProcessor.class);

        } else if(name.equals("producer")) {

        } else if(name.equals("consumer")) {

        } else if(name.equals("router")) {
            parser = new RouterParser(componentDefinition);
        } else if(name.equals("if")) {
            parser = new ChainParser(componentDefinition, FilterProcessor.class);
        } else if(name.equals("other")) {
            parser = new ChainParser(componentDefinition, FilterProcessor.class);
        } else if(name.equals("action")) {
            parser = new ActionParser(componentDefinition);
        } else if(name.equals("processor")){
            //....
        }
        //Assert.notNull(parser, "解析器没有设置！");
        componentDefinition.setParser(parser);
    }
}