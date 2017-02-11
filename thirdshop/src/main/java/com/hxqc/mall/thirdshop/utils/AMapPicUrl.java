package com.hxqc.mall.thirdshop.utils;

import android.content.Context;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.util.ScreenUtil;

/**
 * Author:李烽
 * Date:2015-12-01
 * FIXME
 * Todo 获取地图图片
 */
public class AMapPicUrl {
    private static final String URL = "http://restapi.amap.com/v3/staticmap?";
    private static final String AMAP_KEY = "d4604c34c9e49240ae222d6bff7f22a9";
    private Context mContext;

    public AMapPicUrl(Context context) {
        mContext = context;
    }


    /**
     * 获取图片地址链接
     *
     * @param latLng  点的经纬度
     * @param zoom    地图级别：地图缩放级别:[1,17]
     * @param height  图片宽度
     * @param width   图片宽度*图片高度。最大值为1024*1024
     * @param scale   1:返回普通图；
     *                2:调用高清图，图片高度和宽度都增加一倍，zoom也增加一倍（当zoom为最大值时，zoom不再改变）。
     * @param markers 自定义markersStyle： -1，url，0。
     *                -1表示为自定义图片，url为图片的网址。自定义图片只支持png格式。
     * @param labels  [0-9]、[A-Z]、[单个中文字] 当size为small时，图片不展现标注名。
     * @param paths   pathsStyle：weight, color, transparency, fillcolor, fillTransparency。
     * @param traffic 底图是否展现实时路况。 可选值： 0，不展现；1，展现。
     * @param sig     数字签名认证用户必填
     * @return 图片的url
     */
    public String getLocationPicUrl(
            LatLng latLng, int zoom, int height,
            int width, boolean scale,
            Markers markers, String labels,
            String paths, boolean traffic,
            String sig) {
        int scaleValue = scale ? 2 : 1;
        int trafficValue = traffic ? 1 : 0;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(URL).append("key=").append(AMAP_KEY).append("&");
        stringBuffer.append("zoom=").append(zoom).append("&");
        stringBuffer.append("location=").append(latLng.longitude).append(",")
                .append(latLng.latitude).append("&");
        if (null != markers)
            stringBuffer.append("markers=").append(markers.getSize())
                    .append(",").append(markers.getColor())
                    .append(",").append(markers.getLabel())
                    .append(":").append(latLng.longitude).append(",").append(latLng.latitude)
                    .append("&");
        stringBuffer.append("size=").append(width).append("*").append(height).append("&");
        stringBuffer.append("scale=").append(scaleValue).append("&");
        stringBuffer.append("traffic=").append(trafficValue);
        Log.d("AMapPicUrl", stringBuffer.toString());
        return stringBuffer.toString();
    }

    /**
     * 获取图片地址
     *
     * @param latLng 经纬度
     * @return 图片的url
     */
    public String getLocationPicUrl(LatLng latLng) {
        Markers markers = new Markers("0xFF0000", "A", Markers.SIZE_LARGE);
        int screenWidth = ScreenUtil.getScreenWidth(mContext);
        int dimensionPixelSize = mContext.getResources().getDimensionPixelSize(R.dimen.item_padding_16);
        int width = screenWidth - 2 * dimensionPixelSize;
        int height = width * 9 / 16;
//        width = width / 3 * 2;
//        height = height / 3 * 2;
        return getLocationPicUrl(latLng, 14, height, width, false, markers, null, null, false, null);
    }

}

/**
 * 自定义markersStyle： -1，url，0。
 * -1表示为自定义图片，url为图片的网址。自定义图片只支持png格式。
 */
class Markers {
    private String color;
    private String label;

    public Markers(String color, String label, String size) {
        this.color = color;
        this.label = label;
        this.size = size;
    }

    private String size;

    public static final String SIZE_SMALL = "small";
    public static final String SIZE_MID = "mid";
    public static final String SIZE_LARGE = "large";


    public String getColor() {
        return color;
    }

    public String getLabel() {
        return label;
    }

    public String getSize() {
        return size;
    }
}
