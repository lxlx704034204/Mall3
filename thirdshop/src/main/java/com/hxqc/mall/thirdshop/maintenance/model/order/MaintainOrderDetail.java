package com.hxqc.mall.thirdshop.maintenance.model.order;

import com.hxqc.mall.thirdshop.maintenance.model.Mechanic;
import com.hxqc.mall.thirdshop.maintenance.model.ServiceAdviser;
import com.hxqc.mall.thirdshop.maintenance.model.ShopPoint;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemGroup;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-04-29
 * FIXME
 * Todo
 */
public class MaintainOrderDetail {

    public String orderStatus;//订单状态 string
    public String orderStatusText;//0表示未付款，10表示支付完成，30表示已完成，40表示已关闭，50表示已取消,60表示线下已退款 string
    public float workCostAmount;//工时总额 84.00 number
    public String workOrderID;//工单号 string
    public float goodsAmount;//配件总额 number
    public float amount;//总计(工时总额+配件总额) 400.00 number
    public float discount;//折扣价 380.00 number
    public float orderAmount;//订单总计 number
    public Score score;//使用了积分抵扣 例 100分抵扣10元 { }
    public MaintainCoupon coupon;//使用优惠卷[]
    public String appointmentDate;//预约时间 string
    public String maintainDate;//维修时间 string
    public ServiceAdviser serviceAdviser;//服务顾问 { }
    public Mechanic mechanic;//维修技师 { }
    public ShopPoint shopPoint;//店铺地点 { }
    public String userFullname;//联系人姓名 string
    public String userPhoneNumber;//联系人手机号 string
    public String paymentType;//支付方式 string
    public String orderPaid;//已付款金额 string
    public String prepayID;//交易单号 string
    public String paidTime;//付款时间 y-m-n h-m string
    public String orderCreatTime;//订单创建时间 y-m-n h-m string
    public String orderID;//订单ID string
    public String refundStatus;//退款状态 10-待退款 20-退款中 30-退款完成 40-退款关闭 订单状态为10的时候判断 number
    public String refundStatusText;//退款状态 10-待退款 20-退款中 30-退款完成 40-退款关闭 string
    public ArrayList<MaintenanceItemGroup> maintenanceItems;//保养项目[]
    public int hasComment ;// 是否有评论
    public String shopPhoto;// 店铺图片
    public int shopType;//  店铺类型 10 4S店 20快修店
    public String captcha; //验证码 string


}
