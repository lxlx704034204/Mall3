package com.hxqc.mall.core.model.recharge;

import java.io.Serializable;

/**
 * Author:李烽
 * Date:2016-03-10
 * FIXME
 * Todo 充值的时候姓名，号码，金额，积分
 */
public class RechargeRequest implements Serializable {
    public String name;
    public String phone_number;
    public String charge_number;//充值金额
    public String score;
    public String orderID;//订单号
    public String workOrderID;//工单号
}
