package com.hxqc.mall.usedcar.utils;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

/**
 * 说明:
 *
 * @author: 吕飞
 * @since: 2016-05-11
 * Copyright:恒信汽车电子商务有限公司
 */
public class MyBarDataSet extends BarDataSet {
    public MyBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
    }
    public void setYVals(List yVals) {
        mYVals = yVals;
        notifyDataSetChanged();
    }
}
