package com.hxqc.mall.amap.model;

import android.text.TextUtils;

/**
 * Author: wanghao
 * Date: 2016-04-08
 * FIXME
 * Todo 加油站明细
 */
public class GasStationModel {
    public double distance;//距离
    public String name;//加油站名称
    public String area;//邮政编码
    public String areaname;//地区名称
    public String address;//具体地址
    public String brandname;//加油站品牌
    public String type;//加油站类别
    public String discount;//折扣类型
    public String exhaust;//地区编号
    public String position;//经纬度
    public double lat;
    public double lon;
    public GasPrice price;//价格明细
    public String gastprice;//汽油价格
    public String fwlsmc;//支持支付方式 类型

    //纬度 39.916042
    public double getLatitude(){
        if (!TextUtils.isEmpty(this.position)){
            String s = this.position.split(",")[1];
            return Double.parseDouble(s);
        }else {
            return 0;
        }
    }

    //经度 116.403119
    public double getLongitude(){
        if (!TextUtils.isEmpty(this.position)){
            String s = this.position.split(",")[0];
            return Double.parseDouble(s);
        }else {
            return 0;
        }
    }

}
