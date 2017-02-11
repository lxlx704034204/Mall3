package com.hxqc.mall.thirdshop.maintenance.model.maintenance;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-03-21
 * FIXME
 * Todo 检查套餐返回的套餐
 */
public class ReturnPackage {
    public String packageID;
    public String name;
    public float amount;
    public float discount;
    public ArrayList<ReturnItem> items;


    public class ReturnItem{
           public  String itemID;
    }
}
