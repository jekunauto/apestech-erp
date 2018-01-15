package com.apestech.framework.util;

import com.apestech.oap.ServiceRouter;
import com.apestech.oap.event.OapEvent;
import com.apestech.oap.event.RopEventMulticaster;

import java.util.Map;

/**
 * 功能：事件工具类
 *
 * @author xul
 * @create 2018-01-11 15:28
 */
public class EventUtil {

    /**
     * 功能：发送事件
     * @param event
     */
    public static void multicastEvent(OapEvent event){
        ServiceRouter serviceRouter = RopUtil.getServiceRouter();
        RopEventMulticaster multicaster = serviceRouter.getRopContext().getEventMulticaster();
        multicaster.multicastEvent(event);
    }

}
