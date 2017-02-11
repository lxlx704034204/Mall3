package com.hxqc.mall.amap.control;

/**
 * Author:  wh
 * Date:  2016/4/26
 * FIXME
 * Todo  分类
 */
public interface MapMarkerType {

    //反地理位置编码成功
    int GeoCodeSuccess = 110;

    //关键字   停车场
    String keyword_park = "停车场";
    //关键字   充电站
    String keyword_charger = "充电站";

    int Nothing = 0;
    int GasStationType = 1;
    int ParkType = 2;
    int TrafficType = 3;
    int ChargerStationType = 4;

}
