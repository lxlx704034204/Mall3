package com.hxqc.mall.usedcar.model;


import android.text.TextUtils;

/**
 * @Author : 钟学东
 * @Since : 2015-08-21
 * FIXME
 * Todo
 */
public class SellCarInfo extends UsedCarBase {
    public String check_status;
    public String product_status;
    public String reason;

    public String getProductStatus() {
        if (check_status.equals("1")) {
            return "车辆状态：待审核";
        } else if (check_status.equals("3")) {
            return "车辆状态：未通过审核";
        } else if (product_status.equals("0")) {
            return "车辆状态：待上架";
        } else if (product_status.equals("1") || product_status.equals("2")) {
            return "车辆状态：下架";
        } else if (product_status.equals("3")) {
            return "车辆状态：上架";
        } else if (product_status.equals("4")) {
            return "车辆状态：已售";
        } else return "";
    }

    public String getReasonText() {
        if (TextUtils.isEmpty(reason)) {
            return "";
        } else {
            return "查看原因：" + reason;
        }
    }

    public String getCarNoText() {
        return "车源编号：" + car_source_no;
    }

    public String getOnCardTimeText() {
        return "上牌时间：" + first_on_card;
    }

    public String getUploadTimeText() {
        if (TextUtils.isEmpty(publish_time)) {
            return "";
        } else {
            return "上传时间：" + publish_time.substring(0, 10);
        }
    }

    public String getRangeText() {
        return "里程：" + car_mileage+"万公里";
    }
}
