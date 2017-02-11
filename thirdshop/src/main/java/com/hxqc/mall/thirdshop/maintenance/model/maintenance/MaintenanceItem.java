package com.hxqc.mall.thirdshop.maintenance.model.maintenance;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-12-28
 * FIXME
 * Todo
 */

public class MaintenanceItem {

    public String itemID; //项目ID
    public String name; //项目名称
    public String amount; //项目总计
    public String workCost; //工时费
    public float discount; //优惠价(项目自身优惠价) number (float)
    public String descriptionUrl; //项目介绍URL string
    public String summary; //保养项目介绍摘要 string
    public int revisability; //是否可以修改 默认 1可修改 number

    public ArrayList<ArrayList<MaintenanceChildGoodsN>> goods;

}
