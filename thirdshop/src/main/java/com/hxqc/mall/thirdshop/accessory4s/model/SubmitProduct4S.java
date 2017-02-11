package com.hxqc.mall.thirdshop.accessory4s.model;

import com.google.gson.annotations.Expose;

/**
 * 说明:用于购物车提交的商品
 *
 * @author: 吕飞
 * @since: 2016-02-23
 * Copyright:恒信汽车电子商务有限公司
 */
public class SubmitProduct4S {
    /**
     * itemNum : integer, 数量
     * isPackage : string, 是否是套餐，是：1，否：0
     * itemID : string, 商品ID或套餐ID
     * idList : string, 套餐中商品id列表，多个用英文逗号隔开，如果不是套餐，传英文引号
     */
    @Expose
    public int itemNum;
    @Expose
    public String isPackage;
    @Expose
    public String itemID;
    @Expose
    public String idList;
}
