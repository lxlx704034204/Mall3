package com.hxqc.mall.core.model.order;

import java.util.ArrayList;

/**
 * liaoguilong
 * Created by CPR113 on 2016/3/1.
 * 保养bean
 */
public class MaintenanceBean  extends BaseOrderStatus.MaintenanceOrderStatus{
    public String orderID;//订单ID
    public String orderCreatTime;//订单创建时间
    public String actualPayment;//订单总计
    public String paymentID;// 'YIJIPAY'易极付|'ALIPAY'支付宝|'YEEPAY'易宝|'WEIXIN'微信|'offline'线下
    public String paymentIDText;// 'YIJIPAY'易极付|'ALIPAY'支付宝|'YEEPAY'易宝|'WEIXIN'微信|'offline'线下
    public String amountPayable;//应付金额
    public String shopName;//店铺名称
    public String shopID; //店铺ID
    public int  hasComment;// 是否有评论 boolean
    public String  shopPhoto;// 店铺图片
    public String  shopType;//  店铺类型 10 4S店 20快修店

    public ArrayList<MaintenanceProject> maintenanceItem;//保养项目



    public boolean getHasComment() {
        return hasComment==1 ?true:false;
    }

    /**
     * 保养项目
     */
    public static class MaintenanceProject{
         public String name; //项目名称
         public String itemId;//项目ID
         public String amount;//项目总计
         public String discount;//折扣价
         public ArrayList<MaintenanceParts>  goods;//所需配件

        /**
         * 保养配件
         */
        public static class MaintenanceParts
        {
            public String   name;//配件名称
            public String  price;//价格
            public String  workCost;//工时费
            public String   goodsID;//配件ID
            public String  thumb;//缩略图URL
        }
    }
}
