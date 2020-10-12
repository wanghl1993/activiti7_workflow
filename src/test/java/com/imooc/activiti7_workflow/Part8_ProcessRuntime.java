package com.imooc.activiti7_workflow;

import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Part8_ProcessRuntime {

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    // 获取流程实例
    @Test
    public void getProcessInstance(){
        securityUtil.logInAs("bajie");
        Page<ProcessInstance> processInstancePage =
                processRuntime.processInstances(Pageable.of(0, 100));
        System.out.println("流程实例数量:" + processInstancePage.getTotalItems());

        List<ProcessInstance> list = processInstancePage.getContent();
        for (ProcessInstance pi : list) {
            System.out.println("--- segmentation ---");
            System.out.println("Id: " + pi.getId());
            System.out.println("Name: " + pi.getName());
            System.out.println("StartDate: " + pi.getStartDate());
            System.out.println("Status: " + pi.getStatus());
            System.out.println("ProcessDefinitionId: " + pi.getProcessDefinitionId());
            System.out.println("ProcessDefinitionKey: " + pi.getProcessDefinitionKey());
        }
    }

    // 启动流程实例
    @Test
    public void startProcessInstance(){
        securityUtil.logInAs("bajie");
        ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                .start()
                .withProcessDefinitionKey("myProcess_ProcessRuntime")
                .withName("第一个流程实例名称")
//                .withVariable("","")
                .withBusinessKey("自定义bKey")
                .build()
        );
    }

    // 删除流程实例
    @Test
    public void delProcessInstance(){
        // 不用,taskId: 2018a77c-0c6c-11eb-bd1f-5a00e3d80daf taskId
        // 用,第一个流程实例Id 1fdfe437-0c6c-11eb-bd1f-5a00e3d80daf
        securityUtil.logInAs("bajie");
        ProcessInstance processInstance = processRuntime.delete(ProcessPayloadBuilder
                .delete()
                .withProcessInstanceId("1fdfe437-0c6c-11eb-bd1f-5a00e3d80daf")
                .build()
        );
    }

    // 挂起流程实例(找上面存在的流程实例ID)
    @Test
    public void suspendProcessInstance(){
        // 0b99b907-0c51-11eb-a4f3-5a00e3d80daf
        securityUtil.logInAs("bajie");
        ProcessInstance processInstance = processRuntime.suspend(ProcessPayloadBuilder
                .suspend()
                .withProcessInstanceId("0b99b907-0c51-11eb-a4f3-5a00e3d80daf")
                .build()
        );
    }

    // 激活流程实例
    @Test
    public void activitiProcessInstance(){
        securityUtil.logInAs("bajie");
        ProcessInstance processInstance = processRuntime.resume(ProcessPayloadBuilder
                .resume()
                .withProcessInstanceId("1fdfe437-0c6c-11eb-bd1f-5a00e3d80daf")
                .build()
        );
    }

    // 流程实例参数
    @Test
    public void getVariables(){
        securityUtil.logInAs("bajie");
        List<VariableInstance> list = processRuntime.variables(ProcessPayloadBuilder
                .variables()
                .withProcessInstanceId("f0a89ac1-0c5f-11eb-a55c-5a00e3d80daf")
                .build()
        );
        for (VariableInstance vi : list) {
            System.out.println("--- segmentation ---");
            System.out.println("Name: " + vi.getName());
            System.out.println("Value: " + vi.getValue());
            System.out.println("TaskId: " + vi.getTaskId());
            System.out.println("ProcessInstanceId: " + vi.getProcessInstanceId());
        }
    }
}
