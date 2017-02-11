package com.hxqc.mall.thirdshop.accessory.model;

/**
 * 单个商品
 * Created by huangyi on 16/3/2.
 */
public class SingleProduct {

    public String priceRange; //价格区间(格式 ¥600.00~700.00)
    public String fitDescription; //适配描述 如XX车系 XX车型 XX全系通用 XX品牌通用

    public SingleProduct(String priceRange, String fitDescription) {
        this.priceRange = priceRange;
        this.fitDescription = fitDescription;
    }
}
