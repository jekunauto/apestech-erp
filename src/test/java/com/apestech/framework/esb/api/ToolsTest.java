package com.apestech.framework.esb.api;

import com.apestech.framework.util.Tools;
import com.apestech.oap.ServiceMethodDefinition;
import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * 功能：
 *
 * @author xul
 * @create 2017-12-05 15:52
 */
public class ToolsTest {

    public static void main(String[] args) throws Exception {
        Map<String, Object> row = new HashedMap();
        row.put("method", "scm.lsd.save");
        row.put("version", "1.0");
        row.put("needInSession", "true");
        row.put("ignoreSign", "false");
        row.put("obsoleted", "true");
        ServiceMethodDefinition definition = Tools.toBean(ServiceMethodDefinition.class, row);
        System.out.println(definition);
    }

}
