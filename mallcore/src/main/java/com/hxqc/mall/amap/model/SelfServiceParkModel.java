package com.hxqc.mall.amap.model;

/**
 * Author:  wh
 * Date:  2016/4/21
 * FIXME
 * Todo  自己服务器的 停车场信息
 */
public class SelfServiceParkModel {
    public String address;
    public String capacityDesc;
    public int capacityNum;
    public int freeNum;
    public double distance;
    public String feedesc;
    public String latitude;
    public String longitude;
    public String name;
//    public ArrayList<String> photoList;


    @Override
    public String toString() {
        return "SelfServiceParkModel{" +
                "address='" + address + '\'' +
                ", capacityDesc='" + capacityDesc + '\'' +
                ", capacityNum=" + capacityNum +
                ", freeNum=" + freeNum +
                ", distance=" + distance +
                ", feedesc='" + feedesc + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public double tipLatitude() {
        double d = 0;
        try {
            d = Double.parseDouble(latitude);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public double tipLongitude() {
        double d = 0;
        try {
            d = Double.parseDouble(longitude);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }
}
