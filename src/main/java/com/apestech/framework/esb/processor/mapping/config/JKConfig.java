package com.apestech.framework.esb.processor.mapping.config;

import org.apache.commons.beanutils.PropertyUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JKConfig extends DefaultHandler {

    private static Map configs = new HashMap();
    private List fields = null;
    private Config nested = null;

//    public static void main(String[] args) throws Exception {
//        JKConfig jkConfig = new JKConfig();
//        //jkConfig.testConvertor();
//        //jkConfig.testChild();
//        //jkConfig.testJa();
//        jkConfig.init();
//        Config config = JKConfig.getConfig("RKD.Bill_RKD");
//        System.out.println(JKConfig.configs);
//
//    }

    public void readConfigFile(String fileName) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser parser = null;
        try {
            parser = factory.newSAXParser();
            parser.parse(Resources.getResourceAsStream(fileName), this);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }

    public void init() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser parser = null;
        try {
            parser = factory.newSAXParser();
            refreshFile(parser, this.getClass().getResource("/").getPath());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }

    public void refreshFile(SAXParser parser, String strPath) throws Exception {
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
                refreshFile(parser, files[i].getAbsolutePath());
            } else {
                fileName = "";
                is = null;
                String[] paths = files[i].getAbsolutePath().split("classes");
                if (paths.length == 1) {
                    fileName = paths[0];
                    if (fileName.toLowerCase().endsWith("_columns.xml")) {
                        is = new FileInputStream(fileName);
                    }
                } else if (paths.length == 2) {
                    fileName = paths[1];
                    if (fileName.toLowerCase().endsWith("_columns.xml")) {
                        is = Resources.getResourceAsStream(fileName);
                    }
                }
                if (is != null) {
                    parser.parse(is, this);
                }
            }
        }
    }

    @Override
    public void startElement(String namespaceUri, String ename, String qname, Attributes attributes) throws SAXException {
        if (ename.equalsIgnoreCase("field")) {
            Field field = new Field();
            for (int i = 0; i < attributes.getLength(); i++) {
                try {
                    PropertyUtils.setSimpleProperty(field, attributes.getLocalName(i), attributes.getValue(i));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            fields.add(field);
        } else if (ename.toLowerCase().endsWith("nested")) {
            Config config = new Config();
            fields.add(config);

            fields = new ArrayList();
            config.setFields(fields);
            for (int i = 0; i < attributes.getLength(); i++) {
                try {
                    //BeanUtils.setProperty(config, attributes.getLocalName(i), attributes.getValue(i));
                    PropertyUtils.setSimpleProperty(config, attributes.getLocalName(i), attributes.getValue(i));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            config.setParent(nested);
            nested = config;
        } else if (ename.toLowerCase().endsWith("config")) {
            Config config = new Config();
            fields = new ArrayList();
            config.setFields(fields);
            for (int i = 0; i < attributes.getLength(); i++) {
                try {
                    PropertyUtils.setSimpleProperty(config, attributes.getLocalName(i), attributes.getValue(i));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            configs.put(attributes.getValue("id"), config);
            nested = config;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("field")) {
            Field field = (Field) fields.get(fields.size() - 1);
        }

        if (qName.toLowerCase().endsWith("nested")) {
            if (nested != null) {
                nested = nested.getParent();
                fields = nested.getFields();
            }
        }
    }

    public static Config getConfig(String key) {
        Config config = (Config) configs.get(key);
        if (config == null) {
            throw new RuntimeException("Maping id： " + key + " 没有配置！");
        }
        return config;
    }

}
