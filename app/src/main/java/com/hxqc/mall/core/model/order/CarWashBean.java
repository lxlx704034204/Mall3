package com.hxqc.mall.core.model.order;

/**
 * liaoguilong
 * Created by CPR113 on 2016/5/16.
 * 洗车
 */
public class CarWashBean extends BaseOrderStatus.CarWashOrderStatus{
    public String orderID;//订单ID string
    public String  actualPayment;//实付金额 number
    public String shopName;//店铺名称 string
    public String   shopID;//店铺ID string
    public String  shopPhoto;//店铺图片 string
    public String  hasComment;//是否有评论 boolean
    public String paymentID;//'YIJIPAY'易极付|'ALIPAY'支付宝|'YEEPAY'易宝|'WEIXIN'微信|'offline'线下|'BALANCE'余额|'DISCOUNT' 优惠抵扣 string
    public String paymentIDText;//'YIJIPAY'易极付|'ALIPAY'支付宝|'YEEPAY'易宝|'WEIXIN'微信|'offline'线下|'BALANCE'余额|'DISCOUNT' 优惠抵扣 string

    public boolean getHasComment() {
        return hasComment.equals("1")?true:false;
    }
}
