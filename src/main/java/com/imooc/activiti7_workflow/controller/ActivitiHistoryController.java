package com.imooc.activiti7_workflow.controller;

import com.imooc.activiti7_workflow.SecurityUtil;
import com.imooc.activiti7_workflow.pojo.UserInfoBean;
import com.imooc.activiti7_workflow.util.AjaxResponse;
import com.imooc.activiti7_workflow.util.GlobalConfig;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/activitiHistory")
public class ActivitiHistoryController {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private HistoryService historyService;

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
}
