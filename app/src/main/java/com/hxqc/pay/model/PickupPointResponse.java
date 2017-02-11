package com.hxqc.pay.model;

import android.text.TextUtils;

import com.hxqc.mall.core.model.auto.PickupPointT;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2015-03-27
 * FIXME
 * 获取自提点
 */
public class PickupPointResponse {

    //自提点等级：1.省，2.市
    public String level;

    public int getLevel() {

        if (TextUtils.isEmpty(level)) {
            return 0;
        }
        return Integer.parseInt(level);
    }

    //自提点 列表
    public ArrayList<PickupPointT > pointList;


}
