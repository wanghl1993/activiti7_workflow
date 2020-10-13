package com.imooc.activiti7_workflow.controller;

import com.imooc.activiti7_workflow.util.AjaxResponse;
import com.imooc.activiti7_workflow.util.GlobalConfig;
import com.sun.org.apache.xml.internal.utils.Hashtree2Node;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipInputStream;

@RestController
@RequestMapping("/processDefinition")
public class ProcessDefinitionController {

    @Autowired
    private RepositoryService repositoryService;

    // 添加流程定义通过上传bpmn
    @PostMapping(value = "/uploadStreamAndDeployment")
    public AjaxResponse uploadStreamAndDeployment(@RequestParam("processFile") MultipartFile multipartFile,
                                                  @RequestParam("processName") String processName){


        List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();

        try {
            // 获取上传文件名
            String fileName = multipartFile.getOriginalFilename();
            // 获取文件扩展名
            String extension = FilenameUtils.getExtension(fileName);
            // 获取文件字节流对象
            InputStream fileInputStream = multipartFile.getInputStream();

            Deployment deployment = null;
            if(extension.equals("zip")){
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = repositoryService.createDeployment() // 初始化部署
                        .addZipInputStream(zip)
                        .name(processName)
                        .deploy();

            } else {
                deployment = repositoryService.createDeployment()
                        .addInputStream(fileName, fileInputStream)
                        .name(processName)
                        .deploy();
            }

            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                    deployment.getId() + ";" + fileName
            );
        } catch (Exception e){
            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.ERROR.getCode(),
                    "部署流程失败",
                    e.toString()
            );
        }
    }


    // 添加流程定义在线提交BPMN的XML




    // 获取流程定义列表
    @GetMapping(value = "/getDefinitions")
    public AjaxResponse getDefinitions(){


        List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();

        try {
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                    .list();

            for (ProcessDefinition pd : list) {
                HashMap<String, Object> hashMap = new HashMap<>();

                hashMap.put("Name", pd.getName());
                hashMap.put("Key", pd.getKey());
                hashMap.put("ResourceName", pd.getResourceName());
                hashMap.put("DeploymentId", pd.getDeploymentId());
                hashMap.put("Version", pd.getVersion());

                System.out.println("Name:" + pd.getName());
                System.out.println("Key:" + pd.getKey());
                System.out.println("ResourceName:" + pd.getResourceName());
                System.out.println("DeploymentId:" + pd.getDeploymentId());
                System.out.println("Version:" + pd.getVersion());
                System.out.println("--- segmentation ---");

                listMap.add(hashMap);
            }

            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.SUCCESS.getCode(),
                    GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                    listMap     //list.toString()
            );
        } catch (Exception e){
            return AjaxResponse.AjaxData(
                    GlobalConfig.ResponseCode.ERROR.getCode(),
                    "获取流程定义失败",
                    e.toString()
            );
        }
    }


    // 获取流程定义XML

    // 获取流程部署列表
    // 删除流程定义

}
