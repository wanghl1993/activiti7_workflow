package com.imooc.activiti7_workflow.controller;

import com.imooc.activiti7_workflow.SecurityUtil;
import com.imooc.activiti7_workflow.pojo.UserInfoBean;
import com.imooc.activiti7_workflow.util.AjaxResponse;
import com.imooc.activiti7_workflow.util.GlobalConfig;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private RepositoryService repositoryService;

    // 获取我的待办任务
    @GetMapping(value = "/getTasks")
    public AjaxResponse getTasks() {
        try {
            if (GlobalConfig.Test) {  //登录存在securtity框架里
                securityUtil.logInAs("bajie");
            }
            // 先获取用户名, 再把用户名当做当前任务来操作shiro
            Page<Task> taskPage = taskRuntime.tasks(Pageable.of(0, 100));
            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();

            for (Task tk : taskPage.getContent()) {
                HashMap<String, Object> hashMap = new HashMap<>();

                hashMap.put("id", tk.getId());
                hashMap.put("name", tk.getName());
                hashMap.put("status", tk.getStatus());
                hashMap.put("createdDate", tk.getCreatedDate());
                hashMap.put("assignee", tk.getAssignee());
                if (tk.getAssignee() == null) {
                    hashMap.put("assignee", "待拾取任务");
                } else {
                    hashMap.put("assignee", tk.getAssignee());
                }

                ProcessInstance processInstance =
                        processRuntime.processInstance(tk.getProcessInstanceId());
                hashMap.put("instanceName", processInstance.getName());

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
                    "获取我的待办任务失败",
                    e.toString()
            );
        }
    }

    // 完成任务
    @GetMapping(value = "/completeTask")
    public AjaxResponse completeTask(@RequestParam("taskID") String taskID) {
        try {
            if (GlobalConfig.Test) {  //登录存在securtity框架里
                securityUtil.logInAs("bajie");
            }

            Task task = taskRuntime.task(taskID);

            if (task.getAssignee() == null) {
                taskRuntime.claim(TaskPayloadBuilder.claim()
                        .withTaskId(taskID)
                        .build()
                );
            }
            taskRuntime.complete(TaskPayloadBuilder.complete()
                            .withTaskId(task.getId())
//                    .withVariable("num","2")
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
                    "完成任务 :" + "失败",
                    e.toString()
            );
        }
    }


    // 渲染动态表单
    @GetMapping(value = "/formDataShow")
    public AjaxResponse formDataShow(@RequestParam("taskID") String taskID) {
        try {
            if (GlobalConfig.Test) {  //登录存在securtity框架里
                securityUtil.logInAs("bajie");
            }

            Task task = taskRuntime.task(taskID);

            UserTask userTask = (UserTask) repositoryService.getBpmnModel(task.getProcessDefinitionId())
                    .getFlowElement(task.getFormKey());

            if (userTask == null) {
                return AjaxResponse.AjaxData(
                        GlobalConfig.ResponseCode.SUCCESS.getCode(),
                        GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                        "无表单"
                );
            }

            List<FormProperty> formProperties = userTask.getFormProperties();
            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
            for (FormProperty fp : formProperties) {
                String[] splitFP = fp.getId().split("-_!");
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", splitFP[0]);
                hashMap.put("controlType", splitFP[1]);
                hashMap.put("controlLable", splitFP[2]);
                hashMap.put("controlDefvalue", splitFP[3]);
                hashMap.put("controlParam", splitFP[4]);

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
                    "任务表单渲染失败",
                    e.toString()
            );
        }
    }


    // 保存动态表单
}
