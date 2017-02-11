package com.hxqc.fastreqair.model;

import com.hxqc.mall.core.model.order.BaseOrderStatus;

/**
 * liaoguilong
 * Created by CPR113 on 2016/5/17.
 * 洗车订单详情bean
 */
public class CarWashOrderDetailsBean extends BaseOrderStatus.CarWashOrderStatus {
    public String orderID;//订单ID string
    public String  actualPayment;//实付金额 number
    public String  shopName;//店铺名称 string
    public String  shopID;//店铺ID string
    public String shopPhoto;//店铺图片 string
    public String  hasComment;//是否有评论 boolean
    public String  paymentID;//支付方式 string
    public String   captcha;//验证码 string
    public CarWashShopPoint shopPoint;//店铺地点 { }
    public String   tradeID;//交易单号 string
    public String   orderCreatTime;//订单创建时间 string
    public String  orderAmount;//订单金额 number


    public boolean getHasComment() {
        return hasComment.equals("1")?true:false;
    }

    /**
     * 店铺地址
     */
    public static class  CarWashShopPoint {
        public String id;//店铺ID string
        public String shopName;//店铺名称 string
        public String address;//地址 string
        public String tel;//电话 string
        public float latitude;//纬度 number
        public float longitude;//经度 number
    }

}
