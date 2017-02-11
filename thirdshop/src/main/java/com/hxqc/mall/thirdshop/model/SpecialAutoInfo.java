package com.hxqc.mall.thirdshop.model;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年05月06日
 * <p>
 * 特价车车辆详情 {
 * itemID:
 * 车辆ID string
 * itemName:
 * 车辆简称 string
 * itemPic:
 * 车辆图片 string
 * itemThumb:
 * 车辆缩略图 string
 * itemPrice:
 * 价格 (格式：12345.67) number
 * itemOrigPrice:
 * 厂家指导价 (格式：12345.67) number
 * subscription:
 * 订金 单位 元 number
 * appearance:
 * 车身颜色 { }
 * interior:
 * 内饰颜色 { }
 * itemFall:
 * 降幅 number
 * extID:
 * 车型ID string
 * introduce:
 * 图文介绍(HTML) string
 * isStarted:
 * 特卖是否开始 boolean
 * isEnded:
 * 特卖是否结束 boolean
 * serverTime:
 * 服务器时间 时间戳 string
 * startTime:
 * 开始时间 时间戳 string
 * endTime:
 * 结束时间 时间戳 string
 * realityPhoto:
 * 实拍图片[]
 * autoDetailInfo:
 * 车辆详情 string
 * salesArea:
 * 销售范围 10全国 20本省 number
 * }
 */
public class SpecialAutoInfo {

    public String subscription; // 订金 单位 元 number

    public ColorInfo appearance; // 车身颜色

    public ColorInfo interior; // 内饰颜色

    public boolean isStarted; // 特卖是否开始 boolean

    public boolean isEnded; // 特卖是否结束 boolean

    public String startTime; // 开始时间 时间戳 string

    public String endTime; // 结束时间 时间戳 string

    public ArrayList< String > realityPhoto; // 实拍图片[String]

    public String autoDetailInfo; // 车辆详情 string

    public String salesArea; // 销售范围 10全国 20本省 number             /** '10'=>'售本省','20'=>'售全国', '30'=>'售本市' **/

    public long itemCategory; // 车辆类别 (10.汽车，20.电动车) number

    public String autoStatus; // 车辆状态 30已销售 string

    public String brand; // 特价车品牌

    public String series; // 特价车车系



    public String itemID;//车辆ID string

    public String itemName;//车辆简称 string

    public String itemPic;//车辆图片 string

    public String itemThumb;//车辆缩略图 string

    public String itemPrice;//价格 (格式：12345.67) number

    public String itemOrigPrice;//厂家指导价 (格式：12345.67) number

    public String itemTotalPrice;//参考总价 (格式：12345.67) number

    public String itemFall;//降幅 number

    public String extID;//车型ID string

    public String introduce;//图文介绍(HTML) string

    public String isInPromotion;//是否为促销商品（0.否;1.是） number

    public String promotionEnd;//活动结束时间 string

    public String serverTime;//服务器时间 string

    public String decrease;


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
        return !TextUtils.isEmpty(itemThumb) ? itemThumb : "empty";
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


    public String getItemFall() {
        return !TextUtils.isEmpty(itemFall) ? itemFall : "";
    }


    public String getExtID() {
        return !TextUtils.isEmpty(extID) ? extID : "";
    }


    public String getIntroduce() {
        return !TextUtils.isEmpty(introduce) ? introduce : "";
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
