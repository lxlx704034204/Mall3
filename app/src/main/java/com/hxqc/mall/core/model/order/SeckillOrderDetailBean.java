package com.hxqc.mall.core.model.order;


import com.hxqc.mall.thirdshop.model.ShopInfo;

/**
 * liaoguilong
 *  特价车bean
 * Created by CPR113 on 2016/5/6.
 */
public class SeckillOrderDetailBean extends BaseOrderStatus.SeckillOrderStatus{
  public   String orderID ;//订单ID
    public  String subscription;//订金 number;
    public  boolean  insurance;//是否办理保险 boolean
    public  String   method;//购买方式 string
    public  String decorate;//店内装饰 string
    public  String  itemName;//车辆简称 string
    public   String itemPic;//车辆图片 string
    public  String itemThumb;//车辆缩略图 string
    public  String  itemPrice;//价格 (格式：12345.67) number
    public  String  appearance;//车身颜色 string
    public  String interior;//内饰颜色 string
    public  String phoneNumber;//联系人手机号 string
    public  String  username;//联系人姓名 string
    public ShopInfo shopInfo;//店铺信息 { }
    public  String  orderCreateTime;//订单创建时间 Y-m-d string
    public  String orderPaid;//已付款金额 number
    public  String  orderUnpaid;//未付款金额 number
    public  String   orderAmount;//总计 number
    public  String  paymentID;//付款方式 string
    public  String  paymentIDText;//付款方式 string
    public  String  captcha;//验证码
    public  String  refund;//能否退款
    public  String  alipayRefund;//支付宝能否退款

    public String isInsurance() {
        if(insurance)
        return "是";
        else
         return "否";
    }

 // 能否退款
  public boolean getRefund() {
    return refund.equals("1")?true:false;
  }

  //支付宝能否退款
  public boolean getAlipayRefund() {
      return alipayRefund !=null && paymentIDText.equals("支付宝") && alipayRefund.equals("1") ?true:false;
  }
}
