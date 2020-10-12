package com.imooc.activiti7_workflow;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class Part7_Gateway {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Test
    public void initProcessInstance(){
        // myProcess_Parallel
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(
                        "myProcess_Inclusive",  //bpmn 里面的Id
                        "bKeyInclusive");
        System.out.println("流程实例ID:" + processInstance.getProcessDefinitionId());
    }

    @Test
    public void completeTask(){
        // 流程实例ID  daedf197-0c61-11eb-aedb-5a00e3d80daf
        Map<String, Object> variables = new HashMap<String, Object>();

        taskService.complete("22a52a5e-0c62-11eb-a7c4-5a00e3d80daf");   //taskId
        taskService.complete("22a52a60-0c62-11eb-a7c4-5a00e3d80daf");   //taskId
        System.out.println("完成任务");
    }
}
