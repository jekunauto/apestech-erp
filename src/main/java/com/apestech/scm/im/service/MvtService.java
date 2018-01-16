package com.apestech.scm.im.service;

import com.apestech.framework.esb.api.SimpleRequest;
import com.apestech.scm.im.repository.MvtRepository;
import com.apestech.scm.im.domain.Mvt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 功能：移动类型服务
 *
 * @author xul
 * @create 2018-01-15 16:34
 */
@Service("mvtService")
public class MvtService {

    @Autowired
    MvtRepository mvtRepository;

    public Mvt save(SimpleRequest request) {
        Mvt mvt = request.getO(Mvt.class);
        mvtRepository.save(mvt);
        return mvt;
    }
}
