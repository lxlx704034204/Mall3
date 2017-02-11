package com.hxqc.mall.core.model;

import java.util.ArrayList;

/**
 * 说明:品牌组
 *
 * author: 吕飞
 * since: 2015-03-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class BrandGroup {

    public ArrayList< Brand > group;
    public String groupTag;

    public BrandGroup() {
        this.group = new ArrayList<>();
    }

    public BrandGroup(String groupTag, ArrayList< Brand > group) {
        this.group = group;
        this.groupTag = groupTag;
    }
}
