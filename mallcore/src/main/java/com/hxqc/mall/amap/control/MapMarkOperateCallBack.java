package com.hxqc.mall.amap.control;

import com.amap.api.navi.model.NaviLatLng;

/**
 * Author:  wh
 * Date:  2016/4/26
 * FIXME
 * Todo  地图mark操作回调
 */
public interface MapMarkOperateCallBack {
    /**
     * 导航点击回调
     * @param end  目的地坐标
     */
    void onNavigationClick(NaviLatLng end);

    /**
     * 移动镜头
     * @param scaleGrade  缩放比例
     * @param isReloc 是否是回到自己位置
     */
    void moveCamera(int scaleGrade,boolean isReloc);
}
