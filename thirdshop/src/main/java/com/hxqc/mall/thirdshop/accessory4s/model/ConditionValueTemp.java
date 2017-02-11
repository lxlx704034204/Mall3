package com.hxqc.mall.thirdshop.accessory4s.model;

/**
 * Created by huangyi on 16/8/8.
 */
public class ConditionValueTemp {

    public String value; //单个条件值
    public Status status;

    public ConditionValueTemp(String value, Status status) {
        this.value = value;
        this.status = status;
    }

    public enum Status {
        UNUSABLE, //不可用的
        NORMAL, //正常的
        CHECKED //选中的
    }
}
