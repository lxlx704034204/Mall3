package com.hxqc.mall.core.model.order;

import java.util.ArrayList;

/**
 * liaoguilong
 * Created by CPR113 on 2016/3/1.
 * 新能源Bean
 */
public class NewEnergBean {
    public String  orderID;//订单ID string
    public ArrayList<Package> packages;//套餐数组[]
    public int canComment;//是否可以评价。 0.不可以，1.可以 number
    public int subscription;//订金 number
    public String paymentStatus;//付款状态 string 0.未付款 1.分部付款 2.已付款
    public String  itemName;//产品名称 string
    public int itemPrice;//产品单价 number
    public String itemCount;//产品数量 string
    public String itemThumb;//产品缩略图 string
    public String  expressType;//配送方式 string 收货方式(0.未选择，1.自提点提车，2.送车上门)
    public String  paymentType;//支付方式 string 0-全款 1-线下 2-分期
    public int  orderType;//订单类型 number 0. 一般订单 1. 特卖订单
    public int  orderPaid;//已付款金额 number
    public int  orderUnpaid;//未付款金额 number
    public int  orderAmount;//总计 number
    public int orderExpressFee;//运费 number
    public int  orderStatus;//订单状态 number 发货中，已签收等。
    public String serverTime;//服务器时间 string
    public String   expiredTime;//订单过期时间 string
    public int isLicense;//上否上牌 0-没有 1-上牌 number
    public String  itemColor;//车身颜色：16进制颜色 string
    public String itemInterior;//内饰颜色：16进制颜色 string 若有两个及两个以上颜色以 , 分隔
    /**
     * 套餐
     */
    public static class Package{
        public String packageID;//套餐ID string
        public String title;//套餐名称 string
        public int amount;//套餐价格 number
    }
}
