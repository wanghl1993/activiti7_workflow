package com.imooc.activiti7_workflow;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Part4_Task {

    @Autowired
    private TaskService taskService;

    // 任务查询
    @Test
    public void getTasks(){
        List<Task> list = taskService.createTaskQuery().list();
        for (Task tk : list) {
            System.out.println("--- segmentation ---");
            System.out.println("Id:" + tk.getId());
            System.out.println("Name:" + tk.getName());
            System.out.println("Assignee:" + tk.getAssignee());
        }
    }

    // 查询我的待办任务 taskId
    @Test
    public void getTasksByAssignee(){
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee("wukong")
//                .taskCandidateUser("")    activiti7 会报用户未登录,
                .list();
        for (Task tk : list) {
            System.out.println("--- segmentation ---");
            System.out.println("Id:" + tk.getId());
            System.out.println("Name:" + tk.getName());
            System.out.println("Assignee:" + tk.getAssignee());
        }
    }

    // 执行任务
    @Test
    public void completeTask(){
        taskService.complete("ee388900-0c33-11eb-9819-5a00e3d80daf");   //taskId
        System.out.println("完成任务");
    }

    // 拾取任务 -> 针对用户组(候选人)
    @Test
    public void claimTask(){
//        List<Task> list = taskService.createTaskQuery()
//                .taskCandidateUser("bajie")     //activiti7 缺少安全框架
//                .list();
        Task task = taskService.createTaskQuery().taskId("6aecdd44-0c35-11eb-873b-5a00e3d80daf").singleResult();
        taskService.claim("6aecdd44-0c35-11eb-873b-5a00e3d80daf","bajie");
    }

    // 归还与交办任务 (设置执行人)
    @Test
    public void setTaskAssignee(){
        Task task = taskService.createTaskQuery().taskId("6aecdd44-0c35-11eb-873b-5a00e3d80daf").singleResult();
        taskService.claim("6aecdd44-0c35-11eb-873b-5a00e3d80daf","null");   // 归还候选任务
        taskService.claim("6aecdd44-0c35-11eb-873b-5a00e3d80daf","wukong");   // 交办任务
    }
}
