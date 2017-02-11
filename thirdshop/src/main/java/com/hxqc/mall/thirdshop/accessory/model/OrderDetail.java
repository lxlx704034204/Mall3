package com.hxqc.mall.thirdshop.accessory.model;

import com.hxqc.mall.core.util.OtherUtil;

import java.util.ArrayList;

/**
 * 说明:用品订单详情
 *
 * @author: 吕飞
 * @since: 2016-02-26
 * Copyright:恒信汽车电子商务有限公司
 */
public class OrderDetail {
    public final static String WAIT = "0";//待付款
    public final static String PAID = "10";//已付款
    public final static String CANCELED = "50";//已取消
    public final static String ACCEPTED = "70";//已受理
    public final static String UNREFUND = "10";//待退款
    public final static String REFUNDING = "20";//退款中
    public final static String REFUNDED = "30";//退款完成
    public final static String CLOSED = "40";//退款关闭
    public String orderCreateTime;
    public String contactPhone;
    public String orderID;
    public String shopTitle;
    public String orderPaymentTime;
    public String orderStatusCode;
    public String contactName;
    public String orderAmount;
    public ArrayList<ShoppingCartItem> itemList;
    public ShopInfo shopInfo;
    public String refundStatus;
    /**
     * orderCreateTime : string,下单时间
     * contactPhone : string,联系人手机
     * orderID : string,订单号
     * subscription : string,订金金额
     * orderPaymentTime : string,付款时间
     * orderStatusCode : integer,用品订单状态码：10表示未付款，20表示支付完成，30表示已完成，40表示已关闭，50表示已取消
     * contactName : string,联系人姓名
     * onShopPaymentAmount : string,到店支付金额
     * productData : {"shopID":"string,4s店ID","shopName":"string,4s店全称","itemList":[{"isPackage":"integer,是否是套餐，是：1，否：0","packageInfo":{"packageNum":"integer,套餐数量","packageName":"string,套餐名字","packagePrice":"string,套餐价格","productList":[{"price":"string,用品价格","smallPhoto":"string,缩略图url","productNum":"integer,商品数量","name":"string,用品名称","productID":"string,用品ID"}],"packageID":"string,套餐ID"},"productInfo":{"price":"string,用品价格","smallPhoto":"string,缩略图url","productNum":"integer,商品数量","name":"string,用品名称","productID":"string,用品ID"}}]}
     */
    String[] orderStatus = {"待付款", "已付款", "", "", "", "订单已取消", "", "订单已受理"};
    String[] refundStatusText = {"", "待退款", "退款中", "退款完成", "退款关闭"};

    public String getOrderStatus() {
        try {
            return orderStatus[Integer.parseInt(orderStatusCode) / 10];
        } catch (Exception e) {
            return "";
        }
    }

    public String getRefundStatus() {
        try {
            return refundStatusText[Integer.parseInt(refundStatus) / 10];
        } catch (Exception e) {
            return "";
        }
    }

    public String getSubscription() {
            return "支付金额：" + OtherUtil.amountFormat(orderAmount, true);
    }
}
