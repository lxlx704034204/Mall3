package com.hxqc.mall.core.controler;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.hxqc.util.DebugLog;

/**
 * Author: wanghao
 * Date: 2015-12-10
 * FIXME   高德地图定位
 * Todo
 */
public class AMapLocationControl implements AMapLocationListener {


    protected static AMapLocationControl mInstance;

    AMapLocation tempAMapLocation;

    onCoreLocationListener coreLocationListener;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private OnGetLocationFailCallBack onGetLocationFailCallBack;

    protected AMapLocationControl() {
    }

    public static AMapLocationControl getInstance() {
        if (mInstance == null) {
            synchronized (AMapLocationControl.class) {
                if (mInstance == null) {
                    mInstance = new AMapLocationControl();
                }
            }
        }
        return mInstance;
    }

    public void onDestroy() {
        unregistRequestLocationProxy();
        mInstance = null;
    }

    public AMapLocationControl setUpLocation(Context context) {
        locationClient = new AMapLocationClient(context);
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        // 设置定位监听
        locationClient.setLocationListener(this);

        locationOption.setOnceLocation(false);
        return this;
    }

    public void setCoreLocationListener(onCoreLocationListener coreLocationListener) {
        this.coreLocationListener = coreLocationListener;
    }

    public void setOnGetLocationFailCallBack(OnGetLocationFailCallBack onGetLocationFailCallBack) {
        this.onGetLocationFailCallBack = onGetLocationFailCallBack;
    }

    /**
     * 注册定位代理 获取定位信息
     */
    public void requestLocation() {
        startLocation();
    }

    public void stopLocation() {
        // 停止定位
        if (locationClient != null && locationClient.isStarted())
            locationClient.stopLocation();
    }

    public void startLocation() {
//        DebugLog.i("home_data", "startLocation inner qqq1");
        if (tempAMapLocation != null && coreLocationListener != null) {
            DebugLog.i("home_data", "startLocation inner qqq1 inner");
            coreLocationListener.onLocationChange(tempAMapLocation);
            return;
        }
//        DebugLog.i("home_data", "startLocation inner qqq2");
        if (locationClient != null && !locationClient.isStarted()) {
            initOption();
            // 设置定位参数
            locationClient.setLocationOption(locationOption);
            // 启动定位
            locationClient.startLocation();
        }
    }

    // 根据控件的选择，重新设置定位参数
    private void initOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(true);
        // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
        locationOption.setInterval(2000);
    }

    /**
     * 解绑定位代理
     * 移除监听
     * 删除绑定
     */
    private void unregistRequestLocationProxy() {
        stopLocation();
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
//        DebugLog.i("home_data", "startLocation inner onLocationChanged");
//        DebugLog.w("onLocationChanged", "触发: -->" + aMapLocation.toString());
        if (aMapLocation.getErrorCode() == 0) {
            if (coreLocationListener != null) {
                if (!(TextUtils.isEmpty(aMapLocation.getCity()))) {
                    stopLocation();
                    tempAMapLocation = aMapLocation;
                    coreLocationListener.onLocationChange(aMapLocation);
                }
            }else {
                stopLocation();
            }
            DebugLog.d("onLocationChanged", "onLocationChanged: -->" + aMapLocation.toString());
        } else {
            if (onGetLocationFailCallBack != null) {
                onGetLocationFailCallBack.onLocationFail(aMapLocation);
            }
        }

    }


    public interface onCoreLocationListener {
        void onLocationChange(AMapLocation aMapLocation);
    }

    public interface OnGetLocationFailCallBack {
        void onLocationFail(AMapLocation aMapLocation);
    }

    /**
     * 注销监听
     */
    public void unregistListener() {

        stopLocation();

        if (coreLocationListener != null) {
            coreLocationListener = null;
        }

        if (onGetLocationFailCallBack != null) {
            onGetLocationFailCallBack = null;
        }
    }
}
