package com.hxqc.mall.thirdshop.maintenance.model.maintenance;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-02-22
 * FIXME
 * Todo 保养套餐
 */
public class MaintenancePackage {
    public String packageID;
    public String img;
    public String name;
    public float amount;
    public float discount;
    public String itemsNames;
//    public String suitable;
    public ArrayList<MaintenanceItem> items;

}
