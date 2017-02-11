package com.hxqc.mall.amap.model;

/**
 * Author:  wh
 * Date:  2016/4/21
 * FIXME
 * Todo  充电站model
 */
public class SelfServiceChargeStationModel {
    public String staCode;//电站编号
    public String staName;//电站名称
    public String staType;//电站类型
    public String staOpstate;//运营状态
    public String province;
    public String city;
    public String region;
    public String staAddress;//详细地址
    public String lng;
    public String lat;
    public String price;
    public String acNum;//交流终端总数
    public String dcNum;//直流终端总数
    public String acableNum;//可用交流数量
    public String dcableNum;//可用直流数量
    public String clientID;//商户内码
    public String providerUrl;//电站logo

    public double tipLatitude() {
        double d = 0;
        try {
            d = Double.parseDouble(lat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public double tipLongitude() {
        double d = 0;
        try {
            d = Double.parseDouble(lng);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }
}
