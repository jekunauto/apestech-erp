package com.apestech.scm.service;

import com.apestech.SampleRequest;
import com.apestech.framework.util.SpringManager;
import com.apestech.oap.annotation.IgnoreSignType;
import com.apestech.oap.annotation.NeedInSessionType;
import com.apestech.oap.annotation.ServiceMethod;
import com.apestech.oap.annotation.ServiceMethodBean;
import com.apestech.oap.response.OapResponse;
import com.apestech.scm.model.Order;
import com.apestech.scm.repository.OrderRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 功能：订单服务
 *
 * @author xul
 * @create 2017-11-17 15:04
 */
@ServiceMethodBean(version = "1.0", needInSession = NeedInSessionType.NO)
public class OrderService {


    /**
     * 功能：订单保存
     */
    @Transactional()
    @ServiceMethod(method = "order.save", ignoreSign = IgnoreSignType.YES)
    public OapResponse save(SampleRequest request) {
        OrderRepository repository = SpringManager.getBean("orderRepository");
        Order order = new Order();
        order = repository.save(order);
        return new OapResponse().setBody(order);
    }

    /**
     * 功能：订单更改
     */
    @Transactional()
    @ServiceMethod(method = "order.update", ignoreSign = IgnoreSignType.YES)
    public OapResponse update(SampleRequest request) {
        OrderRepository repository = SpringManager.getBean("orderRepository");
        Order order = repository.findOne(469);
        order.setUserName("orderName");
        repository.save(order);
        return new OapResponse().setBody(order);
    }

    /**
     * 功能：订单删除
     */
    @Transactional()
    @ServiceMethod(method = "order.delete", ignoreSign = IgnoreSignType.YES)
    public OapResponse delete(SampleRequest request) {
        OrderRepository repository = SpringManager.getBean("orderRepository");
        repository.delete(468);
        return new OapResponse().setBody("OK");
    }

    /**
     * 功能：删除所有订单
     */
    @Transactional()
    @ServiceMethod(method = "order.deleteAll", ignoreSign = IgnoreSignType.YES)
    public OapResponse deleteAll(SampleRequest request){
        OrderRepository repository = SpringManager.getBean("orderRepository");
        repository.deleteAll();
        return new OapResponse().setBody("OK");
    }


}   