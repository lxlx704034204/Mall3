package com.hxqc.mall.core.util;

/**
 * Author: wanghao
 * Date: 2015-06-24
 * FIXME
 * 地图工具
 */
public class MapUtils {

    //π值
    final private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;


    //GCJ-02 坐标转换成 BD-09

    public static com.amap.api.maps.model.LatLng bd_encrypt(double gg_lat, double gg_lon) {
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new com.amap.api.maps.model.LatLng(bd_lat, bd_lon);
    }
//BD-09坐标转换成GCJ-02  3D map

    public static com.amap.api.maps.model.LatLng bd_decrypt(double bd_lat, double bd_lon) {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new com.amap.api.maps.model.LatLng(gg_lat, gg_lon);
    }

    //BD-09坐标转换成GCJ-02  2D map

    public static com.amap.api.maps2d.model.LatLng bd_decrypt_2d(double bd_lat, double bd_lon) {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new com.amap.api.maps2d.model.LatLng(gg_lat, gg_lon);
    }


}
