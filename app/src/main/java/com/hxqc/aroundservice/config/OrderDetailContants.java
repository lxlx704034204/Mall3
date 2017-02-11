package com.hxqc.aroundservice.config;

import com.hxqc.mall.core.api.ApiUtil;

/**
 * Author:胡仲俊
 * Date: 2016 - 05 - 04
 * FIXME
 * Todo  周边服务flag
 */
public class OrderDetailContants {

    public static final String FRAGMENT_ILLEGAL_ORDER_DETAIL = "fragment_illegal_order_detail";//违章订单详情flag
    public static final String FRAGMENT_LICENSE_ORDER_DETAIL = "fragment_license_order_detail";//驾驶证更换订单详情flag
    public static final String FRAGMENT_VEHICLES_ORDER_DETAIL = "fragment_vehicles_order_detail";//车辆年检订单详情flag

    public static final int REQUEST_CANCEL = 4;//取消请求
    public static final int CANDEL_SRCCESS = 5;//取消请求

    public static final int ILLEGAL_AND_COMMISSION = 6;//违章代办

    public static final int CAR_WASH = 7;//洗车
    public static final int ILLEGAL_NEW_LIST = 8;
    public static final int ILLEGAL_HISTORY_LIST = 9;
    public static final int ILLEGAL_DETAIL_LIST = 10;
    public static final int PAY_PROMPT = 11;

    public static final int DRIVER = 12;//驾驶证
    public static final int DRIVING = 13;//行驶证
    public static final int ID = 14;//身份正

    public static final int FLAG_ACTIVITY_LICENSE = 15;//驾驶证换证标识
    public static final int FLAG_ACTIVITY_VEHICLES = 16;//车辆年检标识

    public static final String SERVICE_EXPLAIN = ApiUtil.getAroundURL("/Servicedeclar/index");

}
