package com.hxqc.mall.core.model.order;

import com.hxqc.mall.core.util.OtherUtil;

import java.util.ArrayList;

/**
 * liaoguilong
 * Created by CPR113 on 2016/3/1.
 * 用品bean
 */
public class AccessoryBean extends BaseOrderStatus.AccessoryOrderStatus{
   public String orderID;//订单ID string
    public String  shopName;//    店铺名称 string
    public String  shopID;//    店铺ID string
    public ArrayList<AccessoryItem> itemList;//商品列表[]
    public String   orderCreatTime;//  订单创建时间 y-m-n h-m string
    public String   orderAmount; //订单总计 number
    public String   amountPayable;// 应付金额 number
    public String   actualPayment;//实付金额 number
    public String   paymentID;//'YIJIPAY'易极付|'ALIPAY'支付宝|'YEEPAY'易宝|'WEIXIN'微信|'offline'线下 string
    public String   paymentIDText;//'YIJIPAY'易极付|'ALIPAY'支付宝|'YEEPAY'易宝|'WEIXIN'微信|'offline'线下 string

    public String getActualPayment() {
//        if(Float.valueOf(orderAmount)<=99)
        return String.format("支付金额:%s", OtherUtil.amountFormat(actualPayment, true));
//        else
//        return String.format("支付订金:%s", OtherUtil.amountFormat(actualPayment, true));
    }

    /**
     * 商品列
     */
    public static class AccessoryItem{
        public String name;//  商品名称 string
        public String smallPhoto;//   缩略图 string
        public String productNum;//    商品数量 number
        public String price;//  用品价格 number
        public String refundStatus;//退款状态 number
        public String refundStatusText;//退款状态 string
        public String   refundCount;//退款数量 number
        public String  refundAmount;//退款金额 number
    }
}
