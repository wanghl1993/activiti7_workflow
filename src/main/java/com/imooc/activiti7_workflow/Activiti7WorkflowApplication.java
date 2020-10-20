package com.imooc.activiti7_workflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Activiti7WorkflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(Activiti7WorkflowApplication.class, args);
    }

}


//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//@SpringBootApplication(scanBasePackages = {"com.imooc.activiti7_workflow"})
//public class Activiti7WorkflowApplication extends SpringBootServletInitializer {
//
//
//
//    public static void main(String[] args) {
//        SpringApplication.run(Activiti7WorkflowApplication.class, args);
//    }
//
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(Activiti7WorkflowApplication.class);
//    }
//}
