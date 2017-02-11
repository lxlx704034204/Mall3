package com.hxqc.mall.core.model.bill;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-03-03
 * FIXME
 * Todo 余额账单列表
 */
public class BalanceBillList {
    public String month;//月份周期 yyyy-MM
    public String expendMonth;//    月份消费
    public String subtotal;//    月份总计
    public String prepaidMonth;//    月份充值
    public ArrayList<BalanceBillMatter> matter;//账单内容概括
}
