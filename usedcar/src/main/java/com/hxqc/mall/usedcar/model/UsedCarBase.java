package com.hxqc.mall.usedcar.model;

import android.text.TextUtils;


/**
 * 说明:二手车基类
 *
 * @author: 吕飞
 * @since: 2015-08-18
 * Copyright:恒信汽车电子商务有限公司
 */
public class UsedCarBase {

    public static final String PLATFORM = "1";
    public static final String PERSONAL = "2";
    public String first_on_card; //上牌时间
    public String car_mileage; //里程
    public String estimate_price; //价格
    public String path; //原图地址
    public String publish_time; //车辆发布时间
    public String publish_from; //发布来源 1代表认证 2代表个人
    public String car_source_no; //车源号
    public String car_name; //车辆名称
    public String small_path; //缩略图地址
    public String car_on_sale; //3代表已售

    public boolean isPersonal() {
        return publish_from.equals(PERSONAL);
    }

    public boolean isPlatform() {
        //return publish_from.equals(PLATFORM);
        return !isPersonal();
    }

    public String getItemPrice() {
        if (TextUtils.isEmpty(estimate_price)) {
            return "";
        }
        return "￥" + estimate_price + "万";
    }

    public String getDateAndRange() {
        String[] str = first_on_card.split("-");
        String year = str[0];
        return year + "年/" + car_mileage + "万公里";
    }

}
