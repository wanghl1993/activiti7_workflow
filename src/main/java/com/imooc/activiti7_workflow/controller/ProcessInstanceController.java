package com.imooc.activiti7_workflow.controller;

import com.imooc.activiti7_workflow.SecurityUtil;
import com.imooc.activiti7_workflow.pojo.UserInfoBean;
import com.imooc.activiti7_workflow.util.AjaxResponse;
import com.imooc.activiti7_workflow.util.GlobalConfig;
import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/processInstance")
public class ProcessInstanceController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private ProcessRuntime processRuntime;

    // 查询流程实例
    @GetMapping(value = "/getInstances")
    public AjaxResponse getInstances(@AuthenticationPrincipal UserInfoBean userInfoBean) {
        try {
            if(GlobalConfig.Test){  //登录存在securtity框架里
                securityUtil.logInAs("bajie");
            }
//            else {
//                securityUtil.logInAs(userInfoBean.getUsername());
//            }

            Page<ProcessInstance> processInstancePage =
                    processRuntime.processInstances(Pageable.of(0, 100));

            List<ProcessInstance> list = processInstancePage.getContent();
            list.sort((y,x) -> x.getStartDate().toString().compareTo(y.getStartDate().toString()));

            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();

            for (ProcessInstance pi : list) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id:", pi.getId());
                hashMap.put("name:", pi.getName());
                hashMap.put("status:", pi.getStatus());
                hashMap.put("processDefinitionId:", pi.getProcessDefinitionId());
                hashMap.put("processDefinitionKey:", pi.getProcessDefinitionKey());
                hashMap.put("startDate:", pi.getStartDate());
                hashMap.put("processDefinitionVersion:", pi.getProcessDefinitionVersion());

                // 自己写SQL最好
                // 因为pi里没有历史高亮需要的deploymentID和资源Name,所以需要再次查询 activiti6去查
                ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                        .processDefinitionId(pi.getProcessDefinitionId())
                        .singleResult();

                hashMap.put("deploymentId",pd.getDeploymentId());
                hashMap.put("resourceName",pd.getResourceName());

                listMap.add(hashMap);
            }


            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                    listMap
            );
        } catch (Exception e) {
            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.ERROR.getCode(),
                    "获取流程实例失败",
                    e.toString()
            );
        }
    }

    // 启动流程实例
    @GetMapping(value = "/startProcess")
    public AjaxResponse startProcess(@RequestParam("processDefinitionKey") String processDefinitioKey,
                                     @RequestParam("instanceName") String instanceName) {
        try {

            if(GlobalConfig.Test){
                securityUtil.logInAs("bajie");
            } else {
                securityUtil.logInAs(SecurityContextHolder.getContext().getAuthentication().getName());
            }

            ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                    .start()
                    .withProcessDefinitionKey(processDefinitioKey)
                    .withName(instanceName)
                    .withBusinessKey("自定义Business")
//                    .withVariable("参数name","参数值")
                    .build()
            );

            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                    null
            );
        } catch (Exception e) {
            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.ERROR.getCode(),
                    "启动流程实例失败",
                    e.toString()
            );
        }
    }

    // 挂起流程实例
    @GetMapping(value = "/suspendInstance")
    public AjaxResponse suspendInstance(@RequestParam("instanceID") String instanceID) {
        try {
            if(GlobalConfig.Test){
                securityUtil.logInAs("bajie");
            }

            ProcessInstance processInstance = processRuntime.suspend(ProcessPayloadBuilder
                    .suspend()
                    .withProcessInstanceId(instanceID)
                    .build()
            );

            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                    processInstance.getName()
            );
        } catch (Exception e) {
            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.ERROR.getCode(),
                    "暂停流程实例失败",
                    e.toString()
            );
        }
    }


    // 激活/重启流程实例
    @GetMapping(value = "/resumeInstance")
    public AjaxResponse resumeInstance(@RequestParam("instanceID") String instanceID) {
        try {
            if(GlobalConfig.Test){
                securityUtil.logInAs("bajie");
            }

            ProcessInstance processInstance = processRuntime.resume(ProcessPayloadBuilder
                    .resume()
                    .withProcessInstanceId(instanceID)
                    .build()
            );

            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                    processInstance.getName()
            );
        } catch (Exception e) {
            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.ERROR.getCode(),
                    "激活流程实例失败",
                    e.toString()
            );
        }
    }

    // 删除流程实例
    @GetMapping(value = "/deleteInstance")
    public AjaxResponse deleteInstance(@RequestParam("instanceID") String instanceID) {
        try {
            if(GlobalConfig.Test){
                securityUtil.logInAs("bajie");
            }

            ProcessInstance processInstance = processRuntime.delete(ProcessPayloadBuilder
                    .delete()
                    .withProcessInstanceId(instanceID)
                    .build()
            );

            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                    processInstance.getName()
            );
        } catch (Exception e) {
            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.ERROR.getCode(),
                    "删除流程实例失败",
                    e.toString()
            );
        }
    }

    // 查询流程参数
    @GetMapping(value = "/variables")
    public AjaxResponse variables(@RequestParam("instanceID") String instanceID) {
        try {
            if(GlobalConfig.Test){
                securityUtil.logInAs("bajie");
            }

            List<VariableInstance> variableInstances = processRuntime.variables(ProcessPayloadBuilder
                    .variables()
                    .withProcessInstanceId(instanceID)
                    .build()
            );

            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                    variableInstances
            );
        } catch (Exception e) {
            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.ERROR.getCode(),
                    "查询流程实例参数失败",
                    e.toString()
            );
        }
    }

}
