package com.hxqc.mall.core.model.bill;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-03-03
 * FIXME
 * Todo 余额账单
 */
public class BalanceBill extends Bill{
    public double balance ;//剩余余额
    public double prepaidAmount = -1;//充值总计额
    public double expendamount = -1;//消费总计
    public ArrayList<BalanceBillList> billList;//账单列表
    public String end_month;
    public long billCount;
    public String curPage;
}
