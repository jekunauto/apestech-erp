package com.apestech.framework.util;

import com.apestech.oap.ServiceRouter;

import java.util.Map;

/**
 * 功能：Rop相关工具类
 *
 * @author xul
 * @create 2018-01-11 15:30
 */
public class RopUtil {

    public static ServiceRouter getServiceRouter() {
        Object routers = SpringManager.getBean(ServiceRouter.class);
        return (ServiceRouter) ((Map) routers).get("router");
    }

}
