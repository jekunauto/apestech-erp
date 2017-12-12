package com.apestech.scm.service;

import com.apestech.framework.esb.api.Request;
import com.apestech.framework.util.SpringManager;
import com.apestech.oap.annotation.NeedInSessionType;
import com.apestech.oap.annotation.ServiceMethod;
import com.apestech.oap.annotation.ServiceMethodBean;
import com.apestech.oap.response.OapResponse;
import com.apestech.scm.model.User;
import com.apestech.scm.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@ServiceMethodBean(version = "1.0", needInSession = NeedInSessionType.NO)
public class AppSecretService {

    @Transactional()
    @ServiceMethod(method = "appSecret.save")
    public OapResponse<String> save(Request request) {
        UserRepository repository = SpringManager.getBean("userRepository");
        for (int i = 0; i <= 10; i++) {
            User user = new User("userName" + String.valueOf(i));
            repository.save(user);
//            if (i == 7) throw new RuntimeException("事务测试！");
        }
        return new OapResponse<String>().setBody("OK");
    }
}
