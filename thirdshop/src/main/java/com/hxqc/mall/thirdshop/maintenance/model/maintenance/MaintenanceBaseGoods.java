package com.hxqc.mall.thirdshop.maintenance.model.maintenance;

import java.io.Serializable;

/**
 * @Author : 钟学东
 * @Since : 2016-05-21
 * FIXME
 * Todo
 */
public class MaintenanceBaseGoods implements Serializable {
    public String name;  //配件名称 string
    public float price; //价格 number
    public int count; //配件个数 number
    public String goodsID; //配件ID string
    public String thumb; //缩略图URL string
}
