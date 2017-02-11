package com.hxqc.mall.core.model.auto;

import java.util.ArrayList;

/**
 * 说明:颜色信息
 * <p/>
 * author: 吕飞
 * since: 2015-03-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class ColorInformation implements Cloneable {
    /**
     * 颜色描述
     */
    public String colorDescription;//颜色描述
    /**
     * 商品ID
     */
    public String itemID;//商品ID
    public String seriesID;//车系ID
    /**
     * 内饰颜色
     */
    public ArrayList< ColorInformation > interior;//
    public String color;//颜色

    public String[] getColors() {
        if (!color.contains(",")) {
            return new String[]{color};
        }
        return color.split(",");
    }

    public String getColor() {
        return color;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Object clones() {
        try {
            return clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return this;
    }
}
