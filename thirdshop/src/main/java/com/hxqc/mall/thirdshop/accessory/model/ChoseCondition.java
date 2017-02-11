package com.hxqc.mall.thirdshop.accessory.model;

/**
 * 已选条件
 * Created by huangyi on 16/2/25.
 */
public class ChoseCondition {

    public String conditionName; //已选条件类目
    public String conditionValue; //已选条件值
    public String color; //16进制颜色 多个用英文逗号隔开

    public ChoseCondition(String conditionName, String conditionValue, String color) {
        this.conditionName = conditionName;
        this.conditionValue = conditionValue;
        this.color = color;
    }
}
