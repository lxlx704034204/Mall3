package com.hxqc.mall.usedcar.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * 说明:品牌分组
 *
 * @author: 吕飞
 * @since: 2015-07-31
 * Copyright:恒信汽车电子商务有限公司
 */
public class BrandGroup {
    @Expose
    public ArrayList<Brand> group;
    @Expose
    public String groupTag;
}
