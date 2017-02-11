package com.hxqc.mall.thirdshop.model.promotion;

/**
 * Author: wanghao
 * Date: 2015-12-01
 * FIXME  促销详情价格列表 item
 * Todo
 */
public class SalesDetailPriceModel {
    public String carTitle = "";
    public String knockdownPrice = "";
    public String preferentialPrice = "";
    public String indicativePrice = "";
    public String nakedCarPrice = "";
    public String gift = "无";

    public String getKnockdownPrice() {
        return "¥" + knockdownPrice + "万";
    }

    public String getPreferentialPrice() {
        return "¥" + preferentialPrice + "万";
    }

    public String getIndicativePrice() {
        return "¥" + indicativePrice + "万";
    }

    public String getNakedCarPrice() {
        return "¥" + nakedCarPrice + "万";
    }
}
