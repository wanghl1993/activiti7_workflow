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
public class Part6_UEL {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    // 启动流程实例带参数,执行执行人
    @Test
    public void initProcessInstanceWithArgs(){
        // 流程变量 ${ZhiXingRen}
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("ZhiXingRen","wukong");
//        variables.put("ZhiXingRen2","aaa");
//        variables.put("ZhiXingRen3","bbb");

        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(
                        "myProcess_UEL_V2",  //bpmn 里面的Id
                        "bKeyUEL_V2",
                        variables);
        System.out.println("流程实例ID:" + processInstance.getProcessDefinitionId());

    }

    // 完成任务带参数,指定流程变量测试
    @Test
    public void completeTaskWithArgs(){
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("pay","101");
        taskService.complete("e11fc7aa-0c52-11eb-aa94-5a00e3d80daf",variables);   //taskId
        System.out.println("完成任务");
    }

    // 启动流程实例带参数,使用实体类
    @Test
    public void initProcessInstanceWithClassArgs(){
        // myProcess_UEL_V3
        UEL_POJO uel_pojo = new UEL_POJO();
        uel_pojo.setZhixingren("bajie");
//        uel_pojo.setPay();

        // 流程变量 ${ZhiXingRen}
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("uelpojo",uel_pojo);

        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(
                        "myProcess_UEL_V3",  //bpmn 里面的Id
                        "bKeyUEL_V3",
                        variables);
        System.out.println("流程实例ID:" + processInstance.getProcessDefinitionId());
    }

    // 任务完成环节带参数,指定多个候选人
    @Test
    public void initProcessInstanceWithCandiDateArgs(){ // 常用
        // taskId 2e4da377-0c57-11eb-8cad-5a00e3d80daf
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("houxuanren","wukong,tangceng");
        taskService.complete("2e4da377-0c57-11eb-8cad-5a00e3d80daf",variables);   //taskId
        System.out.println("完成任务");
    }

    // 直接指定流程变量
    @Test
    public void otherArgs(){
        runtimeService.setVariable(
                "2e4da377-0c57-11eb-8cad-5a00e3d80daf",
                "pay",
                "101");
//        runtimeService.setVariables("");
//        taskService.setVariable();
//        taskService.setVariables();
    }

    // 局部变量
    @Test
    public void otherLocalArgs(){
        runtimeService.setVariableLocal(
                "2e4da377-0c57-11eb-8cad-5a00e3d80daf",
                "pay",
                "101");
//        runtimeService.setVariablesLocal("");
//        taskService.setVariableLocal();
//        taskService.setVariablesLocal();
    }

}
