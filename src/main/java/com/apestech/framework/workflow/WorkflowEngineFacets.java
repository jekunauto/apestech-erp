package com.apestech.framework.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 功能：工作流引擎Facets
 *
 * @author xul
 * @create 2017-11-29 14:11
 */
@Service
public class WorkflowEngineFacets {

    @Qualifier("flowableEngine")
    @Autowired
    private WorkflowEngine engine;

    //流程管理：流程部署（）

    /**
     * 功能：流程部署
     */
    public void deploy() {
        engine.executeTask(null,null, null);
    }

    public void redeploy(){

    }
}
