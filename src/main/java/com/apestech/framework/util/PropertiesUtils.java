package com.apestech.framework.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能：properties文件工具
 *
 * @author xul
 * @create 2017-12-04 9:10
 */
@Component("propertiesUtils")
public class PropertiesUtils {

    @Autowired
    private Environment env;

    public String getProperty(String key) {
        return env.getProperty(key);
    }

    public String replace(String value) {
        String variable = getVariable(value);
        while (variable != null) {
            String key = getProperty(variable);
            if (key == null) {
                throw new RuntimeException(String.format("ESB文件配置错误，参数’%s‘没有在properties文件中配置！", variable));
            }
            value = replace(value, key);
            variable = getVariable(value);
        }
        return value;
    }

    private String getVariable(String value) {
        String regex = "(\\{\\{)(.+?)(}})";
        return getVariable(regex, value);
    }

    private String getVariable(String regex, String value) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            return matcher.group(2).trim();
        }
        return null;
    }

    private String replace(String target, String replacement) {
        String regex = "(\\{\\{)(.+?)(}})";
        return replace(regex, target, replacement);
    }

    private String replace(String regex, String target, String replacement) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(target);
        return matcher.replaceFirst(replacement);
    }

}
