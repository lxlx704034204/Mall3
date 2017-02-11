package com.hxqc.mall.core.model.bill;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-03-03
 * FIXME
 * Todo 积分账单
 */
public class ScoreBill extends Bill {
    public long balance = -1;//剩余积分
    public long obtain = -1;//获得总计
    public long expendamount = -1;//消费总计
    public ArrayList<ScoreBillList> billList;//账单列表
    public String end_month;
    public long billCount;
    public String curPage;
//    public ScoreBill match() {
//        ScoreBill bill = new ScoreBill();
//        bill.balance = this.balance;
//        bill.obtain = this.obtain;
//        bill.expendamount = this.expendamount;
//        bill.end_month = this.end_month;
//        bill.billCount = this.billCount;
//        ArrayList<ScoreBillList> scoreBillLists = new ArrayList<>();
//        for (int i = 0; i < this.billList.size(); i++) {
//            scoreBillLists.add(this.billList.get(i).match());
//        }
//        bill.billList = this.billList;
//        return bill;
//    }

//    public void addList(ArrayList<ScoreBillList> list) {
//        for (int i = 0; i < list.size(); i++) {
//            billList.add(list.get(i).match());
//        }
//    }

}
