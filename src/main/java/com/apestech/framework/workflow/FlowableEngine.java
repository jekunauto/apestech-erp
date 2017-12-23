package com.apestech.framework.workflow;

import com.apestech.framework.esb.api.SimpleRequest;
import org.flowable.engine.*;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能：flowable实现
 *
 * @author xul
 * @create 2017-12-21 9:04
 */
@Service
public class FlowableEngine implements WorkflowEngine {

    @Autowired
    private ProcessEngine engine;

    /**
     * 功能：流程启动
     *
     * @param processId 工作流ID
     * @param orderId
     * @param operator  登陆人帐号
     * @param role      岗位
     * @param args      输入参数    @return 工作流实例编号
     */
    @Override
    public String startInstanceById(String processId, String orderId, String operator, String role, Map args) {
        return null;
    }

    /**
     * 功能：执行
     *
     * @param taskId   任务ID
     * @param operator 登陆人帐号
     * @param args     输入参数
     */
    @Override
    public void executeTask(String taskId, String operator, Map args) {

    }

    public void test(SimpleRequest request){
        // 创建流程引擎
//        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 得到流程存储服务组件
        RepositoryService repositoryService = engine.getRepositoryService();
        // 得到运行时服务组件
        RuntimeService runtimeService = engine.getRuntimeService();
        // 获取流程任务组件
        TaskService taskService = engine.getTaskService();
        // 部署流程文件
//        repositoryService.createDeployment()
//                .addClasspathResource("processes/one-task-process.test.bpmn20.xml").deploy();
        // 启动流程
        runtimeService.startProcessInstanceByKey("one-test1");
        // 查询第一个任务
        Task task = taskService.createTaskQuery().singleResult();

        while (task != null){
            System.out.println("当前任务名称：" + task.getName());
            if(task.getTaskDefinitionKey().equals("sid-A1ED29FA-2534-45A1-851C-4477D2BAB62A")) {
                Map row = new HashMap();
                row.put("ts", 4);
                taskService.complete(task.getId(), row);
            } else{
                taskService.complete(task.getId());
            }
            task = taskService.createTaskQuery().singleResult();
        }
    }
}
