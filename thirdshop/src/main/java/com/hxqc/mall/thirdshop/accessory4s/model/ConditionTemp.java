package com.hxqc.mall.thirdshop.accessory4s.model;

import java.util.ArrayList;

/**
 * Created by huangyi on 16/8/8.
 */
public class ConditionTemp {

    public String name; //条件名字
    public ArrayList<ConditionValueTemp> values; //多个条件值

    public ConditionTemp(String name, ArrayList<ConditionValueTemp> values) {
        this.name = name;
        this.values = values;
    }
}
