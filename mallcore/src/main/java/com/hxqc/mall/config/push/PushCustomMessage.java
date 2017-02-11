package com.hxqc.mall.config.push;

/**
 * Author:胡俊杰
 * Date: 2016/6/21
 * FIXME
 * Todo  友盟自定义消息格式
 */
public class PushCustomMessage {
    public static final int NotificationType_Promotion = 0;//活动
    public static final int NotificationType_OrderDetail = 1;//订单详情
    public static final int NotificationType_OrderPay = 2;
	/**
     *  通知类型  0活动消息  1订单消息  2付款消息
     */
    public int notificationType;
    //错误码  成功返回0
    public int errorCode;
    //返回 ok 支付成功
    public String message;
    //支付金额
    public float money;
    //订单号
    public String orderID;

    public int orderType;

    public String url;

    @Override
    public String toString() {
        return "PushCustomMessage{" +
                "notificationType=" + notificationType +
                ", errorCode=" + errorCode +
                ", message='" + message + '\'' +
                ", money=" + money +
                ", orderID='" + orderID + '\'' +
                ", orderType=" + orderType +
                ", url='" + url + '\'' +
                '}';
    }
}
