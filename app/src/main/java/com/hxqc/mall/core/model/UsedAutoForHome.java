package com.hxqc.mall.core.model;

import com.google.gson.annotations.Expose;

/**
 * 说明:首页二手车
 *
 * author: 吕飞
 * since: 2015-06-17
 * Copyright:恒信汽车电子商务有限公司
 */
@Deprecated
public class UsedAutoForHome extends NewAutoForHome {
    @Expose
    public String firstShowTime = "";//上牌时间
    @Expose
    public String itemMileage = "";//里程数

    public String getFirstShowTime() {
        return "上牌：" + firstShowTime.substring(0,4)+"年"+firstShowTime.substring(5,7)+"月";
    }

    public String getItemMileage() {
        return "里程：" + itemMileage + "km";
    }

}
