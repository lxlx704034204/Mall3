package com.hxqc.mall.thirdshop.accessory.model;

/**
 * 条件
 * Created by huangyi on 16/2/25.
 */
public class Condition {

    public String conditionName; //条件名字
    public String conditionKey; //条件Key
    public String conditionValue; //条件值
    public String color; //16进制颜色 多个用英文逗号隔开

    public Condition(String conditionName, String conditionKey, String conditionValue, String color) {
        this.conditionName = conditionName;
        this.conditionKey = conditionKey;
        this.conditionValue = conditionValue;
        this.color = color;
    }
}
