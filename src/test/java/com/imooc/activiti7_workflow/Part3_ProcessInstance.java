package com.imooc.activiti7_workflow;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Part3_ProcessInstance {

    @Autowired
    private RuntimeService runtimeService;

    // 初始化流程实例  act_ru_identitylink : act_ru_execution == 1 : n(一对多)
    @Test
    public void initProcessInstance(){
        // 1.获取页面表单填报的内容,请假时间,请假事由, String fromData
        // 2.fromData 写入业务表,返回业务表主键ID == businessKey
        // 3.把业务数据与Activiti7流程数据关联
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(
                        "myProcess_UEL_V2",  //bpmn 里面的Id
                        "bKeyUEL_V2");
        System.out.println("流程实例ID:" + processInstance.getProcessDefinitionId());

    }

    // 获取流程实例
    @Test
    public void getProcessInstances(){
        List<ProcessInstance> processInstances =
                runtimeService.createProcessInstanceQuery().list();

        for (ProcessInstance pi : processInstances) {
            System.out.println("--- segmentation ---");
            System.out.println("ProcessInstanceId:" + pi.getProcessInstanceId());
            // myProcess_Part1:3:19729977-0bbc-11eb-a7ce-005056c00001
            // bpmn的key : 流程版本号 : UUID
            System.out.println("ProcessDefinitionId:" + pi.getProcessDefinitionId());
            System.out.println("isEnded:" + pi.isEnded());
            System.out.println("isSuspended:" + pi.isSuspended());
        }
    }

    // 暂停与激活流程实例
    @Test
    public void activitiProcessInstance(){
//        runtimeService.suspendProcessInstanceById("69f35977-0bd4-11eb-9fe8-005056c00001");
//        System.out.println("挂起流程实例");

        runtimeService.activateProcessInstanceById("69f35977-0bd4-11eb-9fe8-005056c00001");
        System.out.println("激活流程实例");
    }

    // 删除流程实例
    @Test
    public void delProcessInstance(){
        runtimeService.deleteProcessInstance("69f35977-0bd4-11eb-9fe8-005056c00001","删着玩");
        System.out.println("删除流程实例");
    }

}
