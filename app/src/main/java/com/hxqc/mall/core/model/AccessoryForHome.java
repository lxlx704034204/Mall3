package com.hxqc.mall.core.model;

import com.google.gson.annotations.Expose;

/**
 * 说明:用品（首页）
 *
 * author: 吕飞
 * since: 2015-06-17
 * Copyright:恒信汽车电子商务有限公司
 */
@Deprecated
public class AccessoryForHome {
    @Expose
    public  String itemPic;//对应图片
    @Expose
    public  String subtitle;//副标题
    @Expose
    public  String title;//主标题
    @Expose
    public String url;// 链接地址

    public AccessoryForHome(String itemPic, String subtitle, String title) {
        this.itemPic = itemPic;
        this.subtitle = subtitle;
        this.title = title;
    }
}
