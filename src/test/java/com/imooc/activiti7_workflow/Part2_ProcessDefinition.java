package com.imooc.activiti7_workflow;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Part2_ProcessDefinition {

    @Autowired
    private RepositoryService repositoryService;

    // 查询流程定义
    @Test
    public void getDefinitions(){
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .list();

        for (ProcessDefinition pd : list) {
            System.out.println("Name:" + pd.getName());
            System.out.println("Key:" + pd.getKey());
            System.out.println("ResourceName:" + pd.getResourceName());
            System.out.println("DeploymentId:" + pd.getDeploymentId());
            System.out.println("Version:" + pd.getVersion());
            System.out.println("--- segmentation ---");
        }
    }

    // 删除流程定义
    @Test
    public void delDefinition(){
        // bb6a83f8-0bb8-11eb-94c1-005056c00001
        String pdID = "bb6a83f8-0bb8-11eb-94c1-005056c00001";
        repositoryService.deleteDeployment(pdID, true); //false保留历史(留痕) true删除历史(一笔勾销)
        System.out.println("删除流程定义成功");
    }

}
