package com.imooc.activiti7_workflow.util;


public class GlobalConfig {

    public static final Boolean Test = true;    //不需要调用登录接口

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
