package com.imooc.activiti7_workflow.util;


public class GlobalConfig {

    public static final Boolean Test = true;    //不需要调用登录接口

    // 公司电脑windows
    public static final String BPMN_PathMapping = "file:E:\\gitee\\activiti7_workflow2\\src\\main\\resources\\resources\\bpmn";

    // windows发布路径
//    public static final String BPMN_PathMapping = "file:F:\\gitee\\activiti7_workflow\\src\\main\\resources\\resources\\bpmn\\";

    // linux发布路径
    //public static final String BPMN_PathMapping = "/root/Activiti";

    public enum ResponseCode {
        SUCCESS(0,"成功"),
        ERROR(1,"错误");

        private final int code;
        private final String desc;

        ResponseCode(int code,String desc){
            this.code = code;
            this.desc = desc;
        }

        public int getCode(){return code;}
        public String getDesc(){return desc;}
    }

}
