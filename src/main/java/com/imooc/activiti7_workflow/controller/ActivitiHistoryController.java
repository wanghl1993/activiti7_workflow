package com.imooc.activiti7_workflow.controller;

import com.imooc.activiti7_workflow.SecurityUtil;
import com.imooc.activiti7_workflow.pojo.UserInfoBean;
import com.imooc.activiti7_workflow.util.AjaxResponse;
import com.imooc.activiti7_workflow.util.GlobalConfig;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/activitiHistory")
public class ActivitiHistoryController {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    // 用户历史任务
    @GetMapping(value = "/getInstanceByUserName")
    public AjaxResponse getInstanceByUserName(@AuthenticationPrincipal UserInfoBean userInfoBean) {
        try {
            List<HistoricTaskInstance> historicTaskInstances =
                    historyService.createHistoricTaskInstanceQuery()
                            .orderByHistoricTaskInstanceEndTime().desc()
                            .taskAssignee(userInfoBean.getUsername())
                            .list();

            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                    historicTaskInstances
            );
        } catch (Exception e) {
            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.ERROR.getCode(),
                    "获取用户历史任务失败",
                    e.toString()
            );
        }
    }

    // 根据流程实例ID查询任务
    @GetMapping(value = "/getInstancesByPiID")
    public AjaxResponse getInstancesByPiID(@RequestParam("piID") String piID) {
        try {
            List<HistoricTaskInstance> historicTaskInstances =
                    historyService.createHistoricTaskInstanceQuery()
                            .orderByHistoricTaskInstanceEndTime().desc()
                            .processInstanceId(piID)
                            .list();

            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                    historicTaskInstances
            );
        } catch (Exception e) {
            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.ERROR.getCode(),
                    "获取历史任务失败",
                    e.toString()
            );
        }
    }

    // 高亮显示流程历史
    // 8246a8a3-11ba-11eb-a312-5a00e3d80daf
    @GetMapping(value = "/getHighLine")
    public AjaxResponse getHighLine(@RequestParam("instanceID") String instanceID,
                                    @AuthenticationPrincipal UserInfoBean userInfoBean) {
        try {

            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(instanceID).singleResult();

            // 读取BPMN
            BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
            Process process = bpmnModel.getProcesses().get(0);
            // 获取所有流程FlowElement的信息
            Collection<FlowElement> flowElements = process.getFlowElements();
            HashMap<String, String> map = new HashMap<String, String>();
            for (FlowElement flowElement : flowElements) {
                // 判断是否是线条
                if (flowElement instanceof SequenceFlow) {
                    SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                    String ref = sequenceFlow.getSourceRef();
                    String targetRef = sequenceFlow.getTargetRef();
                    map.put(ref + targetRef, sequenceFlow.getId());
                }
            }

            // 获取全部历史流程节点
            List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(instanceID)
                    .list();

            // 各历史节点两两组合成key
            Set<String> keyList = new HashSet<>();
            for (HistoricActivityInstance i : list) {
                for (HistoricActivityInstance j : list) {
                    if(i != j){
                        keyList.add(i.getActivityId() + j.getActivityId());
                    }
                }
            }

            // 高亮连线ID
            Set<String> highLine = new HashSet<>();
            keyList.forEach(s -> highLine.add(map.get(s)));

            // 获取已经完成的节点
            List<HistoricActivityInstance> listFinished = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(instanceID)
                    .finished()
                    .list();

            // 已经完成的节点高亮
            Set<String> highPoint = new HashSet<>();
            listFinished.forEach(s -> highPoint.add(s.getActivityId()));

            // 获取待办节点
            List<HistoricActivityInstance> listUnFinished = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(instanceID)
                    .unfinished()
                    .list();

            // 待办高亮节点
            Set<String> waitingToDo = new HashSet<>();
            listUnFinished.forEach(s -> waitingToDo.add(s.getActivityId()));

            // 当前用户完成的任务
            String AssigneeName = null;
            if(GlobalConfig.Test){
                AssigneeName = "bajie";
            } else {
                AssigneeName = userInfoBean.getUsername();
            }
            List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
                    .taskAssignee(AssigneeName)
                    .processInstanceId(instanceID)
                    .finished()
                    .list();

            // 待办高亮节点
            Set<String> iDo = new HashSet<>();
            taskInstanceList.forEach(s -> iDo.add(s.getTaskDefinitionKey()));

            HashMap<String, Object> reMap = new HashMap<>();
            reMap.put("highPoint",highPoint);
            reMap.put("highLine",highLine);
            reMap.put("waitingToDo",waitingToDo);
            reMap.put("iDo",iDo);


            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                    reMap
            );
        } catch (Exception e) {
            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.ERROR.getCode(),
                    "高亮历史任务失败",
                    e.toString()
            );
        }
    }
}
