package com.hxqc.mall.thirdshop.maintenance.model.maintenance;

/**
 * @Author : 钟学东
 * @Since : 2016-12-29
 * FIXME
 * Todo
 */

public class MaintenanceChildGoodsN {

    public String name;  //配件名称 string
    public float price; //价格 number
    public int count; //配件个数 number
    public String goodsID; //配件ID string
    public String thumb; //缩略图URL string
    public int choose;  //是否选中即是否推荐 boolean
    public float amount; //（prepareN2和orderCreatedN2中返回)（ 计算规则 （price-dicountG）*count） number
    public String typeName; //类型名称 string
    public float discountG; // 配件单价优惠金额（目前没有，返回0） number

}
