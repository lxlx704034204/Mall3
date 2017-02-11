package com.hxqc.mall.thirdshop.maintenance.model.maintenance;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-12-28
 * FIXME
 * Todo 保养第五版model
 */

public class MaintenanceItemGroup {

    public String name; //项目名称
    public String itemGroupID; //项目类型ID string
    public String amount; //项目总计
    public float discount; //优惠价(项目自身优惠价) number (float)
    public String mutualExclusionGroup; //互斥组ID，用于判断是否有项目互斥 相同ID表示互斥 string
    public String bothGroup;  //同时选中组ID,多同组选中以逗号分隔，逻辑类似互斥组ID string
    public int choose; //用于4s点首页  是否选中 boolean
    public int deleteable ; //是否可以删除 默认1为可以删除 ，0为不可删除

    public ArrayList<MaintenanceItem> items;

    public boolean isCheck = false; //用于判断是否勾选

    @Override
    public String toString() {
        return "MaintenanceItemGroup{" +
                "name='" + name + '\'' +
                ", itemGroupID='" + itemGroupID + '\'' +
                ", amount='" + amount + '\'' +
                ", discount=" + discount +
                ", mutualExclusionGroup='" + mutualExclusionGroup + '\'' +
                ", bothGroup='" + bothGroup + '\'' +
                ", choose=" + choose +
                ", deleteable=" + deleteable +
                ", items=" + items +
                ", isCheck=" + isCheck +
                '}';
    }
}
