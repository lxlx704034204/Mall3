package com.hxqc.mall.amap.model;

/**
 * Author:  wh
 * Date:  2016/4/21
 * FIXME
 * Todo 天气model
 */
public class SelfServiceWeatherModel {

    public String city;
    public String date;
    public String time;
    public String longitude;
    public String latitude;
    public String weather;
    public String temp;
    public String l_tmp;
    public String h_tmp;
    public String WD;
    public String WS;

    @Override
    public String toString() {
        return "SelfServiceWeatherModel{" +
                "city='" + city + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", weather='" + weather + '\'' +
                ", temp='" + temp + '\'' +
                ", l_tmp='" + l_tmp + '\'' +
                ", h_tmp='" + h_tmp + '\'' +
                ", WD='" + WD + '\'' +
                ", WS='" + WS + '\'' +
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
