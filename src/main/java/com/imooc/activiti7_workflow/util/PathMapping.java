package com.imooc.activiti7_workflow.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PathMapping implements WebMvcConfigurer {
    // 原来 resources/resources/xxxbpmn
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/resources/"); // 默认映射
        registry.addResourceHandler("/bpmn/**")
                .addResourceLocations(GlobalConfig.BPMN_PathMapping);
    }

}
