package com.hxqc.mall.thirdshop.maintenance.model.order;

import com.hxqc.mall.core.model.Coupon;
import com.hxqc.mall.thirdshop.maintenance.model.ApppintmentDateNew;
import com.hxqc.mall.thirdshop.maintenance.model.BaseMoney;
import com.hxqc.mall.thirdshop.maintenance.model.Mechanic;
import com.hxqc.mall.thirdshop.maintenance.model.ServiceAdviser;
import com.hxqc.mall.thirdshop.maintenance.model.ShopIntroduce;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemGroup;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemN;

import java.util.ArrayList;

/**
 * Author:钟学东
 * Date:2016-04-22
 * FIXME
 * Todo 报价流程  订单准备
 */
public class OrderPrepareN extends BaseMoney{

    public ArrayList<ApppintmentDateNew> apppintmentDateNew;
    public String recommendApppintmentDate;
    public ArrayList<String> apppintmentDate;

    public float shouldPay;  //应该支付金额 折扣价-积分优惠-优惠卷优惠 number
    public Mechanic mechanic;
    public ServiceAdviser serviceAdviser;

    public ShopIntroduce shopIntroduce;

    public ArrayList<MaintenanceItemGroup> items;

    public Score score;

    public ArrayList<Coupon> coupon;
}
