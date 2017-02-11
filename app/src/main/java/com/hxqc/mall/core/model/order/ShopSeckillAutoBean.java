package com.hxqc.mall.core.model.order;

import com.hxqc.mall.core.util.OtherUtil;

/**
 * 4s店特价车
 * Created by CPR113 on 2016/5/9.
 */
public class ShopSeckillAutoBean extends BaseOrderStatus.SeckillOrderStatus {
    public String shopTitle;//店铺简称 string
    public String shopID;//店铺ID string
    public String shopTel;//店铺电话 string
    public String orderID;//订单ID string
    public String itemName;//产品名称 string
    public String itemColor;//车身颜色：16进制颜色 string
    public String item;//车身颜色：16进制颜色 string
    public String itemThumb;//特价车图片 string
    public String itemInterior;//内饰颜色：16进制颜色 string
    public String subscription;//订金 number
    public String orderAmount;//总计 number
    public String captcha;//验证码
    public String refund;//是否可以退款
    public String paymentID;//'YIJIPAY'易极付|'ALIPAY'支付宝|'YEEPAY'易宝|'WEIXIN'微信|'offline'线下|'BALANCE'余额|'DISCOUNT' 优惠抵扣 string
    public String paymentIDText;//'YIJIPAY'易极付|'ALIPAY'支付宝|'YEEPAY'易宝|'WEIXIN'微信|'offline'线下|'BALANCE'余额|'DISCOUNT' 优惠抵扣 string

    public String getOrderPrice() {
//        if (orderStatusCode.equals(ORDER_WFK)) //10未付款时
//            return String.format("订金:%s", OtherUtil.amountFormat(subscription, true));
//        else
        return String.format("支付订金:%s", OtherUtil.amountFormat(subscription, true));
    }

    public boolean getRefund() {
        return refund.equals("1") ? true : false;
    }


    @Override
    public String toString() {
        return "ShopSeckillAutoBean{" +
                "shopTitle='" + shopTitle + '\'' +
                ", shopID='" + shopID + '\'' +
                ", shopTel='" + shopTel + '\'' +
                ", orderID='" + orderID + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemColor='" + itemColor + '\'' +
                ", item='" + item + '\'' +
                ", itemThumb='" + itemThumb + '\'' +
                ", itemInterior='" + itemInterior + '\'' +
                ", subscription='" + subscription + '\'' +
                ", orderAmount='" + orderAmount + '\'' +
                ", captcha='" + captcha + '\'' +
                ", refund='" + refund + '\'' +
                ", paymentID='" + paymentID + '\'' +
                ", paymentIDText='" + paymentIDText + '\'' +
                '}';
    }
}
