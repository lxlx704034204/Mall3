package com.hxqc.mall.thirdshop.model;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Function:第三方店铺车辆基本信息model
 *
 * @author 袁秉勇
 * @since 2015年12月08日
 */
public class BaseInfo {
    public String itemID;//车辆ID string

    public String itemName;//车辆简称 string

    public String itemPic;//车辆图片 string

    public String itemThumb;//车辆缩略图 string

    public String itemPrice;//价格 (格式：12345.67) number

    public String itemOrigPrice;//厂家指导价 (格式：12345.67) number

    public String itemTotalPrice;//参考总价 (格式：12345.67) number

    public ArrayList<ColorInfo> appearance;// 车身颜色

    public String itemFall;//降幅 number

    public String extID;//车型ID string

    public String introduce;//图文介绍(HTML) string

    public String isInPromotion;//是否为促销商品（0.否;1.是） number

    public String promotionEnd;//活动结束时间 string

    public String serverTime;//服务器时间 string

    public String decrease;

    public String brand;

    public String series;


    public String getItemID() {
        return !TextUtils.isEmpty(itemID) ? itemID : "";
    }

    public String getItemName() {
        return !TextUtils.isEmpty(itemName) ? itemName : "";
    }

    public String getItemPic() {
        return !TextUtils.isEmpty(itemPic) ? itemPic : "";
    }

    public String getItemThumb() {
        return !TextUtils.isEmpty(itemThumb) ? itemThumb : "";
    }

    public String getItemPrice() {
        return !TextUtils.isEmpty(itemPrice) ? itemPrice : "0";
    }

    public String getItemOrigPrice() {
        return !TextUtils.isEmpty(itemOrigPrice) ? itemOrigPrice : "0";
    }

    public String getItemTotalPrice() {
        return !TextUtils.isEmpty(itemTotalPrice) ? itemTotalPrice : "0";
    }

    public ArrayList<ColorInfo> getAppearance() {
        return appearance;
    }

    public String getItemFall() {
        return !TextUtils.isEmpty(itemFall) ? itemFall : "";
    }

    public String getExtID() {
        return !TextUtils.isEmpty(extID) ? extID : "";
    }

    public String getIntroduce() {
        return !TextUtils.isEmpty(introduce) ? introduce : "";
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getIsInPromotion() {
        return !TextUtils.isEmpty(isInPromotion) ? isInPromotion : "";
    }

    public String getPromotionEnd() {
        return !TextUtils.isEmpty(promotionEnd) ? promotionEnd : "";
    }

    public String getServerTime() {
        return !TextUtils.isEmpty(serverTime) ? serverTime : "";
    }
}
