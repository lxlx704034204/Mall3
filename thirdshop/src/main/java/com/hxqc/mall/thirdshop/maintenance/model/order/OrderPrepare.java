package com.hxqc.mall.thirdshop.maintenance.model.order;

import com.hxqc.mall.thirdshop.maintenance.model.BaseMoney;
import com.hxqc.mall.thirdshop.maintenance.model.Mechanic;
import com.hxqc.mall.thirdshop.maintenance.model.ServiceAdviser;
import com.hxqc.mall.thirdshop.maintenance.model.ShopPoint;
import com.hxqc.mall.thirdshop.maintenance.model.coupon.Coupon;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-02-24
 * FIXME
 * Todo 订单准备
 */
public class OrderPrepare  extends BaseMoney{

    public Mechanic mechanic;
    public String preferentialWay;
    public String recommendApppintmentDate;

    public Score score;
    public ServiceAdviser serviceAdviser;

    public ShopPoint shopPoint;
    public ArrayList<String> apppintmentDate;
    public ArrayList<Coupon> coupon;
    public ArrayList<CouponCombination> couponCombination;


//    public String preferentialWay; //以4位二进制数形式返回
//    // 首位表示是否可用积分，第二位为多张优惠卷，第三位为单张优惠卷，第四位是否可用优惠。
//    // 例如 可用积分和多张优惠卷 1101, 不可用积分可用单张优惠卷0011，均不可用0000
//    public Score score;
//    public ArrayList<Coupon> coupon;
//    public ArrayList<CouponCombination> couponCombination;
//    public ArrayList<String> apppintmentDate;//预约时间
//    public ServiceAdviser serviceAdviser;
//    public Mechanic mechanic;
//    public ShopPoint shopPoint;
//    public String recommendApppintmentDate;



}
