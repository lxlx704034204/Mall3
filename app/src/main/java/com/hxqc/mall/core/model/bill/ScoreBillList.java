package com.hxqc.mall.core.model.bill;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-03-03
 * FIXME
 * Todo
 */
public class ScoreBillList {
    public String month;//月份周期 yyyy-MM
    public String expendMonth;//    月份使用
    public String subtotal;//    月份总计
    public String obtainMonth;//    月份获得
    public ArrayList<ScoreBillMatter> matter;//账单内容概括


//    public ScoreBillList match() {
//        ScoreBillList list = new ScoreBillList();
//        list.month = this.month;
//        list.subtotal = this.subtotal;
//        ArrayList<ScoreBillMatter> scoreBillMatters = new ArrayList<>();
//        for (int i = 0; i < this.matter.size(); i++) {
//            list.matter.add(this.matter.get(i).match());
//        }
//        list.matter = scoreBillMatters;
//        return list;
//    }
}
