package com.hxqc.mall.core.model.order;

import android.text.TextUtils;

import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemGroup;
import com.hxqc.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * liaoguilong
 * Created by CPR113 on 2016/3/7.
 * 4S店保养订单详情
 */
public class Maintenance4SShopOrderDetailBean extends BaseOrderStatus.Maintenance4SShopOrderStatus{

    public float workCostAmount;//工时总额 84.00 number
    public String workOrderID;//工单号 string
    public float goodsAmount;//配件总额 number
    public float amount;//总计(工时总额+配件总额) 400.00 number
    public float discount;//折扣价 380.00 number
    public float orderAmount;//订单总计 number
    public MaintenaneScore score;//使用了积分抵扣 例 100分抵扣10元 { }
    public MaintenaneCoupon coupon;//使用优惠卷{}
    public String appointmentDate;//预约时间 string
    public String maintainDate;//维修时间 string
    public MaintenaneServiceAdviser serviceAdviser;//服务顾问 { }
    public MaintenaneMechanic mechanic;//维修技师 { }
    public MaintenaneShopPoint shopPoint;//店铺地点 { }
    public String userFullname;//联系人姓名 string
    public String userPhoneNumber;//联系人手机号 string
    public String paymentID;//支付方式 string
    public String paymentIDText;//支付方式 string'YIJIPAY'易极付|'ALIPAY'支付宝|'YEEPAY'易宝|'WEIXIN'微信|'offline'线下|'BALANCE'余额|'DISCOUNT' 优惠抵扣 string
    public String orderPaid;//已付款金额 string
    public String prepayID;//交易单号 string
    public String paidTime;//付款时间 y-m-n h-m string
    public String orderCreateTime;//订单创建时间 y-m-n h-m string
    public String orderID;//订单ID string
    public ArrayList<MaintenanceItemGroup> maintenanceItems;//保养项目[]
    public String hasComment;// 是否有评论 String
    public String shopPhoto;// 店铺图片
    public String shopType;//  店铺类型 10 4S店 20快修店
    public String captcha;//
    public String model;//车辆信息
    public float drivingDistance;// 累计行驶里程
    public String registerTime;// 首次上牌时间
    public String plateNumber;//车牌号

    public String alipayRefund;//支付宝是否可以退款 (默认不能退款)

  // public InvoiceModel orderInvoice; //发票信息

    public boolean getHasComment() {
        return hasComment.equals("1")?true:false;
    }

    //支付宝能否退款
    public boolean getAlipayRefund() {
        return alipayRefund !=null && paymentIDText.equals("支付宝") && alipayRefund.equals("1") ?true:false;
    }



    /**
     * 是否可以退款
     * @return
     */
    public  boolean isRefund(){
        if((orderStatus.equals(ORDER_YFK)   ||
                (orderStatus.equals(ORDER_DSL) && !paymentID.equals(PayConstant.INSHOP)))
                && !refundStatus.equals(REFUND_DTK)
                && !refundStatus.equals(REFUND_TKZ)
                && !refundStatus.equals(REFUND_TKWC)
                && !refundStatus.equals(REFUND_TKGB)
                && !paymentID.equals(PayConstant.DISCOUNT)//优惠抵扣不能退款
                && TextUtils.isEmpty(workOrderID))
            return true;

        return false;
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
     * 是否可以取消订单
     * @return
     */
    public boolean isCancel(){
        if(orderStatus.equals(ORDER_WFK) ||
                (orderStatus.equals(ORDER_DQR)  && paymentID.equals(PayConstant.INSHOP)) ||
                (orderStatus.equals(ORDER_DSL) && paymentID.equals(PayConstant.INSHOP)) ||
                paymentID.equals(PayConstant.DISCOUNT)) //优惠抵扣，可以取消订单

            return true;
        return false;
    }

    /**
     * 积分
     */
    public static class MaintenaneScore {
        public float count;//可抵扣分数 100 number
        public float unitPrice;//1分对应的金额 0.1 number
        public float useCount;//使用数量 number
        public float usePrice;//积分优惠总金额 number
    }

    /**
     * 优惠卷
     */
    public static class MaintenaneCoupon {
        public String preferentialDescription;//优惠说明 "满100元减10元"
        public float price;//优惠金额
    }

    /**
     * 服务顾问
     */
    public static class MaintenaneServiceAdviser {
        public String name;//姓名 string
        public String avatar;//头像 string
        public String serviceAdviserID;//服务顾问ID string
    }

    /**
     * 维修技师
     */
    public static class MaintenaneMechanic {
        public String name;//姓名 string
        public String avatar;//头像 string
        public String mechanicID;//维修技师ID string

    }

    /**
     * 店铺地址
     */
    public static class MaintenaneShopPoint {
        public String id;//店铺ID string
        public String shopName;//店铺名称 string
        public String address;//地址 string
        public String tel;//电话 string
        public String erpShopCode;//erpShopCode 用于查看工单详情 4s店保养订单详情中返回 string
        public String latitude;//纬度 number
        public String longitude;//经度 number
    }

    /**
     * 保养项目
     */
    public static class MaintenanceItem {
        public String name;//项目名称 string
        public String itemId;//项目ID string
        public float amount;//项目总计 number
        public float discount;//折扣价 number
        public boolean maintainStatus;//该项目是否维修过 boolean
        public float goodsAmount;//配件总额
        public float workCost;//工时费 number
        public ArrayList<MaintenanceItemGoods> goods;//所需配件[]

        /**
         * 配件
         */
        public static class MaintenanceItemGoods {
            public String name;//配件名称 string
            public float price;//价格 number
            public String goodsID;//配件ID string
            public int count;
            public String thumb;//缩略图URL string
        }


    }
}
