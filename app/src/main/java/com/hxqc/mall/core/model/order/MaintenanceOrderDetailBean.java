package com.hxqc.mall.core.model.order;

import com.hxqc.mall.core.model.InvoiceModel;

import java.util.ArrayList;

/**
 * liaoguilong
 * Created by CPR113 on 2016/3/7.
 * 保养订单详情
 */
public class MaintenanceOrderDetailBean extends BaseOrderStatus.MaintenanceOrderStatus{

    public float workCostAmount;//工时总额 84.00 number
    public String workOrderID;//工单号 string
    public String erpCode;//店铺erp
    public float goodsAmount;//配件总额 number
    public float amount;//总计(工时总额+配件总额) 400.00 number
    public float discount;//折扣价 380.00 number
    public float orderAmount;//订单总计 number
    public MaintenaneScore score;//使用了积分抵扣 例 100分抵扣10元 { }
    public MaintenaneCoupon coupon;//使用优惠卷[]
    public String appointmentDate;//预约时间 string
    public String maintainDate;//维修时间 string
    public MaintenaneServiceAdviser serviceAdviser;//服务顾问 { }
    public MaintenaneMechanic mechanic;//维修技师 { }
    public MaintenaneShopPoint shopPoint;//店铺地点 { }
    public String userFullname;//联系人姓名 string
    public String userPhoneNumber;//联系人手机号 string
    public String paymentType;//支付方式 string
    public String orderPaid;//已付款金额 string
    public String prepayID;//交易单号 string
    public String paidTime;//付款时间 y-m-n h-m string
    public String orderCreateTime;//订单创建时间 y-m-n h-m string
    public String orderID;//订单ID string
    public ArrayList<MaintenanceItem> maintenanceItems;//保养项目[]
    public String hasComment;// 是否有评论 String
    public String shopPhoto;// 店铺图片
    public String shopType;//  店铺类型 10 4S店 20快修店
    public String captcha;//
    public String model;//车辆信息
    public String drivingDistance;// 累计行驶里程
    public String registerTime;// 首次上牌时间

    public InvoiceModel orderInvoice; //发票信息

    public boolean getHasComment() {
        return hasComment.equals("1");
    }

    /**
     * 积分
     */
    public static class MaintenaneScore {
        public int count;//可抵扣分数 100 number
        public float unitPrice;//1分对应的金额 0.1 number
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
        public float latitude;//纬度 number
        public float longitude;//经度 number
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
