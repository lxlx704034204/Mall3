package com.hxqc.mall.core.model.order;

import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.thirdshop.accessory.model.ShoppingCartItem;

import java.util.ArrayList;

/**
 * liaoguilong
 * Created by CPR113 on 2016/6/6.
 * 用品订单详情bean
 */
public class AccessoryOrderDetailBean extends BaseOrderStatus.AccessoryOrderStatus{
    public String captcha;//验证码 string
    public String  orderCreateTime;//下单时间，yyyy-mm-dd hh:mm:ss string
    public String contactPhone;//联系人手机 string
    public String contactName;//联系人姓名 string
    public String  orderID;//订单号 string
    public ArrayList<RefundInfo>  refund;//退款信息[]
    public String shopTitle;//店铺简称 string
    public String  orderAmount;//订单金额 string
    public String  transactionNumber;//交易单号 string
    public String  payment;//支付方式 string
    public String  shopPhoto;//店铺图片url string
    public String  shopID;//店铺ID string
    public String  paymentMethodText;//支付方式 string
    public PickupPointT shopLocation;//店铺地址信息 { }
    public ArrayList<ShoppingCartItem> itemList;


    public class RefundInfo{
        public String  refundStatus;//退款状态 10-待退款 20-退款中 30-退款完成 40-退款关闭 订单状态为10的时候判断 number
        public String  refundStatusText;//退款状态 10-待退款 20-退款中 30-退款完成 40-退款关闭 string
        public String  refundAmount;//退款金额 number
        public String  refundTime;//退款时间 y-m-d h:m:s string
        public String  refundNumber;//退款单号 string
        public String  refundType;//退款方式 支付宝 微信 线下 string
        public String   refundDescription;//退款原因描述 string
    }
}
