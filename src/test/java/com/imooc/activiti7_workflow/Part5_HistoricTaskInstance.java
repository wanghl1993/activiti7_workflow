package com.imooc.activiti7_workflow;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Part5_HistoricTaskInstance {

    @Autowired
    private HistoryService historyService;

    // 根据用户名称查询历史记录
    @Test
    public void HistoricTaskInstanceByUser(){
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime().asc() //正序
                .taskAssignee("bajie")
                .list();

        for (HistoricTaskInstance hi : list) {
            System.out.println("--- segmentation ---");
            System.out.println("Id:" + hi.getId());
            System.out.println("ProcessInstanceId:" + hi.getProcessInstanceId());
            System.out.println("Name:" + hi.getName());
        }
    }

    // 根据流程实例ID查询历史
    @Test
    public void HistoricTaskInstanceByPiID(){
        // 6ae5ff70-0c35-11eb-873b-5a00e3d80daf
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime().asc() //正序
                .processInstanceId("6ae5ff70-0c35-11eb-873b-5a00e3d80daf")
                .list();

        for (HistoricTaskInstance hi : list) {
            System.out.println("--- segmentation ---");
            System.out.println("Id:" + hi.getId());
            System.out.println("ProcessInstanceId:" + hi.getProcessInstanceId());
            System.out.println("Name:" + hi.getName());
        }
    }
}
