package com.hxqc.mall.usedcar.model;

import com.google.gson.annotations.Expose;

/**
 * 说明:已id和value传过来的值，多用于筛选条件
 *
 * @author: 吕飞
 * @since: 2015-08-03
 * Copyright:恒信汽车电子商务有限公司
 */
public class IdAndValue {
    @Expose
    public String id;
    @Expose
    public String value;
    @Expose
    public String text;         // text字段只用在车辆排序接口，其他位置均未用

    public IdAndValue(String id, String value, String text) {
        this.id = id;
        this.value = value;
        this.text = text;
    }
}
