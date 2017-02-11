package com.hxqc.mall.core.model;

/**
 * Author: HuJunJie
 * Date: 2015-04-17
 * FIXME
 * Todo 保险内容
 */
public class Insurance {
    public String name; //项目名称
    public String note;//备注
    public String price;//保险金额

    public String getPrice() {
        return String.format("¥%s", price);
    }
}
