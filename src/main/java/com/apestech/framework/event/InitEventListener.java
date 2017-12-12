package com.apestech.framework.event;

import com.apestech.oap.*;
import com.apestech.oap.event.AfterStartedRopEvent;
import com.apestech.oap.event.RopEventListener;
import com.apestech.oap.event.RopEventMulticaster;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 功能：事件监听器注入
 *
 * @author xul
 * @create 2017-12-06 14:30
 */
public class InitEventListener implements RopEventListener<AfterStartedRopEvent> {
    protected final Logger logger = Logger.getLogger(getClass());

    @Override
    public void onRopEvent(AfterStartedRopEvent ropEvent) {
        RopContext ropContext = ropEvent.getRopContext();
        addListener(ropContext, ropContext.getApplicationContext());
    }

    /**
     * 扫描Spring容器中的Bean，查找有标注{@link Listener}注解的Listener，将它们注册到
     * {@link RopEventMulticaster}中缓存起来。
     *
     * @throws org.springframework.beans.BeansException
     *
     */
    private void addListener(RopContext ropContext, final ApplicationContext context) throws BeansException {
        if (logger.isDebugEnabled()) {
            logger.debug("对Spring上下文中的Bean进行扫描，查找Listener: " + context);
        }
        int i = 0;
        String[] beanNames = context.getBeanNamesForType(RopEventListener.class);
        for (final String beanName : beanNames) {
            Class<?> handlerType = context.getType(beanName);
            if (AnnotationUtils.findAnnotation(handlerType, Listener.class) != null) {
                RopEventListener listener = (RopEventListener) context.getBean(beanName);
                RopEventMulticaster multicaster = ropContext.getEventMulticaster();
                multicaster.addRopListener(listener);
                i ++;
            }
        }
        if (context.getParent() != null) {
            addListener(ropContext, context.getParent());
        }
        if (logger.isInfoEnabled()) {
            logger.info("共注册了" + i + "个Listener");
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
