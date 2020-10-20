package com.imooc.activiti7_workflow.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class TkListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("执行人: " + delegateTask.getAssignee());
    }

}
