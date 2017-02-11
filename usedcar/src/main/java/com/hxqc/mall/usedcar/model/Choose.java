package com.hxqc.mall.usedcar.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * 说明:筛选条件
 *
 * @author: 吕飞
 * @since: 2015-08-22
 * Copyright:恒信汽车电子商务有限公司
 */
public class Choose {

    @Expose
    public ArrayList<IdAndValue> price; //价格

    @Expose
    public ArrayList<IdAndValue> age_limit; //年龄

    @Expose
    public ArrayList<IdAndValue> level; //级别

    @Expose
    public ArrayList<IdAndValue> publish_from; //来源

    @Expose
    public ArrayList<IdAndValue> displacement; //排量

    @Expose
    public ArrayList<IdAndValue> gearbox; //变速箱

    @Expose
    public ArrayList<IdAndValue> mileage; //里程

    @Expose
    public ArrayList<BrandGroup> brand; //品牌

}
