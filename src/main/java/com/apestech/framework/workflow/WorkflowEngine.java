package com.apestech.framework.workflow;

import java.util.Map;

public interface WorkflowEngine {


    /**
     * 功能：流程启动
     *
     * @param processId 工作流ID
     * @param operator  登陆人帐号
     * @param role      岗位
     * @param args      输入参数
     * @return 工作流实例编号
     */
    String startInstanceById(String processId, String orderId, String operator, String role, Map args);

    /**
     * 功能：执行
     * @param taskId 任务ID
     * @param operator 登陆人帐号
     * @param args 输入参数
     *
     */
     void executeTask(String taskId, String operator, Map args);

    /**
     * 功能：后续一级任务节点集合
     * @param processId 工作流ID
     * @param orderId 业务单号
     * @param operator 登陆人帐号
     * @return
     */
//    List<TaskModel> getNextTaskModels(String processId, String orderId, String operator);
//
//    /**
//     * 功能：查询待办
//     * @param operator
//     * @return
//     */
//    List<Task> getTasks(String operator);
//
//    /**
//     * 功能：查询历史任务
//     * @param operator
//     * @return
//     */
//    List<Task> getHistoryTasks(String operator);


}
