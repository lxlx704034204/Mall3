package com.hxqc.mall.usedcar.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * 说明:城市组
 *
 * @author: 吕飞
 * @since: 2015-08-06
 * Copyright:恒信汽车电子商务有限公司
 */
public class CityGroup {
    @Expose
    public String groupTag;
    @Expose
    public ArrayList<City> group;
}
