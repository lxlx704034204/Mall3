package com.hxqc.mall.usedcar.model;

import com.google.gson.annotations.Expose;

/**
 * 说明:城市
 *
 * @author: 吕飞
 * @since: 2015-08-06
 * Copyright:恒信汽车电子商务有限公司
 */
public class City {
    @Expose
    public String city_code;
    @Expose
    public String city_name;

    public City(String city_name) {
        this.city_name = city_name;
    }
}
