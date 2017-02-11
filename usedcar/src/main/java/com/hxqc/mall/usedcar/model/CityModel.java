package com.hxqc.mall.usedcar.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * 说明:
 *
 * @author: 吕飞
 * @since: 2016-08-01
 * Copyright:恒信汽车电子商务有限公司
 */
public class CityModel {
    @Expose
    public String c_code;
    @Expose
    public String c_name;
    @Expose
    public String c_id;
    @Expose
    public ArrayList<RegionModel> region;
}
