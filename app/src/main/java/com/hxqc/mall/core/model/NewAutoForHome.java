package com.hxqc.mall.core.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.hxqc.mall.core.util.OtherUtil;

/**
 * 说明:新车（首页）
 *
 * author: 吕飞
 * since: 2015-06-17
 * Copyright:恒信汽车电子商务有限公司
 */
public class NewAutoForHome {
    @Expose
    public String itemID = "";//商品ID
    @Expose
    public String itemName = "";//车型名称
    @Expose
    public String itemPrice = "";//价格
    @Expose
    public String itemThumb = "";//对应车型的缩略图

    public String getItemPrice() {
        if (TextUtils.isEmpty(itemPrice)) {
            return "";
        }
        return "¥" + OtherUtil.amountFormat(itemPrice);
    }

    public NewAutoForHome() {
    }
}
