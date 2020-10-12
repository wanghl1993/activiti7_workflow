package com.imooc.activiti7_workflow;

import java.io.Serializable;

public class UEL_POJO implements Serializable { // 必须序列化

    private String zhixingren;  // 必须小写
    private String pay;

    public String getZhixingren() {
        return zhixingren;
    }

    public void setZhixingren(String zhixingren) {
        this.zhixingren = zhixingren;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }
}
