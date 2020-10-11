package com.imooc.activiti7_workflow;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

@SpringBootTest
public class Part1_Deployment {

    @Autowired
    private RepositoryService repositoryService;

    @Test
    public void initDeploymentBPMN(){
        String filename = "BPMN/Part1_Deployment.bpmn";
        //String pngname = "BPMN/Part1_Deployment.png";
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(filename)
                //.addClasspathResource(pngname)  //图片
                .name("流程部署测试BPMN_V2")
                .deploy();

        System.out.println(deployment.getName());
    }

    @Test
    public void initDeploymentZIP(){

        InputStream fileInputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("BPMN/Part1_Deployment.zip");

        ZipInputStream zip = new ZipInputStream(fileInputStream);
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zip)
                .name("流程部署测试zip")
                .deploy();

        System.out.println(deployment.getName());
    }


    @Test
    public void getDeployments(){
        List<Deployment> list = repositoryService.createDeploymentQuery().list();

        for(Deployment dep : list){
            System.out.println("Id:"+dep.getId());
            System.out.println("Name:"+dep.getName());
            System.out.println("Deployment"+dep.getDeploymentTime());
            System.out.println("Key:"+dep.getKey());
            System.out.println("--- segmentation ---");
        }
    }
}
