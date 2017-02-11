package com.hxqc.mall.thirdshop.model;

import com.hxqc.util.DateUtil;

import java.util.Calendar;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2015年11月30日
 */
public class ThirdOrderModel {
    public String orderID;//订单id
    public Promotion promotion;//店铺促销信息
    public String shopTitle;//店铺简称
    public String shopID;//店铺ID
    public int orderStatusCode;//订单状态码
    public String orderStatus;//订单状态
    public String orderCreateTime;//下单时间
    public String fullname;//付款人姓名
    public String mobile;//付款人手机号
    public float amount;//付款金额
    public String subscription;//应付订金（无定金返回0）
    public String shopTel;//店铺电话
    public String paymentStatus;//付款状态
    public String paymentStatusCode;//付款状态码
    public String captcha;//验证码
    public String refund;//是否可以退款 boolean
    public String alipayRefund;//支付宝是否可以退款 boolean

    public String paymentID;//'YIJIPAY'易极付|'ALIPAY'支付宝|'YEEPAY'易宝|'WEIXIN'微信|'offline'线下|'BALANCE'余额|'DISCOUNT' 优惠抵扣 string
    public String paymentIDText;//'YIJIPAY'易极付|'ALIPAY'支付宝|'YEEPAY'易宝|'WEIXIN'微信|'offline'线下|'BALANCE'余额|'DISCOUNT' 优惠抵扣 string

    public String refundStatus;//退款状态 10-待退款 20-退款中 30-退款完成 40-退款关闭 订单状态为10的时候判断 number
    public String refundStatusText;//退款状态 10-待退款 20-退款中 30-退款完成 40-退款关闭 string

    public boolean getRefund() {
        if(refund.equals("1"))
        return true;

        return  false;
    }
    public boolean getAlipayRefund() {
        return alipayRefund !=null && paymentIDText.equals("支付宝") && alipayRefund.equals("1") ?true:false;
    }


//    /**
//     * 满足3个月退款时间以内
//     * @param orderCreateTime
//     * @return
//     */
//    public boolean isInsideRefundTime(String orderCreateTime){
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MONTH, -3);
//        if(DateUtil.str2Date(orderCreateTime).after(calendar.getTime()))
//            return true;
//        return false;
//    }


    /**
     * -40: 已关闭
     * -30：客服取消
     * -20：用户取消
     * -10：系统取消
     *  0：  待付款
     *  10： 已付款
     *  20:  已完成
     *  30:  退款中
     */
}
