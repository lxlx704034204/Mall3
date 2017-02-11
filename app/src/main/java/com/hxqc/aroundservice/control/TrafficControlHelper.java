package com.hxqc.aroundservice.control;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.google.gson.reflect.TypeToken;
import com.hxqc.aroundservice.api.AroundServiceApiClient;
import com.hxqc.aroundservice.model.TrafficCity;
import com.hxqc.aroundservice.model.TrafficControlRule;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.controler.AMapLocationControl;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

/**
 * @Author : 钟学东
 * @Since : 2016-04-21
 * FIXME
 * Todo 获取城市限行列表帮助类
 */
public class TrafficControlHelper implements AMapLocationControl.onCoreLocationListener, AMapLocationControl.OnGetLocationFailCallBack {

    private static AroundServiceApiClient aroundServiceApiClient;
    private static TrafficControlHelper instance;

    private TrafficCity trafficCity; // 限行城市列表

    private TrafficControlRule controlRule; //限行规则

    private AMapLocationControl aMapLocationControl; //定位control

//    private Context context;

    private LocationHandle locationHandle;

    public void setLocationHandle(LocationHandle locationHandle) {
        this.locationHandle = locationHandle;
    }

    private TrafficControlHelper() {
        if (aroundServiceApiClient == null)
            aroundServiceApiClient = new AroundServiceApiClient();
    }

    public static TrafficControlHelper getInstance() {
        if (instance == null)
            synchronized (TrafficControlHelper.class) {
                if (instance == null) {
                    instance = new TrafficControlHelper();
                }
            }
        return instance;
    }

    public void onDestory() {

        if (aMapLocationControl != null) {
            aMapLocationControl.unregistListener();
        }

        if (aroundServiceApiClient != null) {
            aroundServiceApiClient = null;
        }

        if (instance != null)
            instance = null;
    }

    public void startLocation(Context context) {
        DebugLog.i("home_data", "startLocation");
        if (aMapLocationControl == null) {
            aMapLocationControl = AMapLocationControl.getInstance().setUpLocation(context.getApplicationContext());
        }
        aMapLocationControl.setCoreLocationListener(this);
        aMapLocationControl.setOnGetLocationFailCallBack(this);
        aMapLocationControl.startLocation();
    }

    /**
     * 获取城市列表
     */
    public void getCity(Context context, final getCityHandle getCityHandle) {
        if (aroundServiceApiClient != null)
            aroundServiceApiClient.weizhangXianxingCity(new BaseMallJsonHttpResponseHandler(context) {
                @Override
                public void onSuccess(String response) {
                    trafficCity = JSONUtils.fromJson(response, new TypeToken<TrafficCity>() {
                    });
                    if (getCityHandle != null)
                        getCityHandle.onSuccess(trafficCity);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (getCityHandle != null)
                        getCityHandle.onFailure();
                }
            });
    }

    /**
     * 获取限行规则
     *
     * @param cityId
     * @param getRuleHandle
     */
    public void getRule(Context context, String cityId, final getRuleHandle getRuleHandle) {
        if (aroundServiceApiClient != null)
            aroundServiceApiClient.weizhangXianxingQuery(cityId, new LoadingAnimResponseHandler(context, false) {
                @Override
                public void onSuccess(String response) {
                    controlRule = JSONUtils.fromJson(response, new TypeToken<TrafficControlRule>() {
                    });
                    if (getRuleHandle != null)
                        getRuleHandle.onSuccess(controlRule);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (getRuleHandle != null)
                        getRuleHandle.onFailure();
                }
            });
    }

    /**
     * 获取限行规则
     *
     * @param cityId
     * @param getRuleHandle
     */
    public void getRule(Context context, String cityId, final getRuleHandle getRuleHandle, boolean isShow) {
        if (aroundServiceApiClient != null)
            aroundServiceApiClient.weizhangXianxingQuery(cityId, new LoadingAnimResponseHandler(context, false) {
                @Override
                public void onSuccess(String response) {
                    controlRule = JSONUtils.fromJson(response, new TypeToken<TrafficControlRule>() {
                    });
                    if (getRuleHandle != null)
                        getRuleHandle.onSuccess(controlRule);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (getRuleHandle != null)
                        getRuleHandle.onFailure();
                }
            });
    }


    @Override
    public void onLocationChange(AMapLocation aMapLocation) {
        DebugLog.i("home_data", "onLocationChange");
        if (locationHandle != null)
            locationHandle.onLocationChange(aMapLocation);
    }

    @Override
    public void onLocationFail(AMapLocation aMapLocation) {
        DebugLog.i("home_data", "onLocationFail");
        if (locationHandle != null)
            locationHandle.onLocationFail(aMapLocation);
    }


    /***************
     * 获取城市列表接口
     *****************/
    public interface getCityHandle {
        void onSuccess(TrafficCity trafficCity);

        void onFailure();
    }

    /***************
     * 获取限行规则接口
     *****************/
    public interface getRuleHandle {
        void onSuccess(TrafficControlRule controlRule);

        void onFailure();
    }

    /***************
     * 定位接口
     *****************/
    public interface LocationHandle {
        void onLocationChange(AMapLocation aMapLocation);

        void onLocationFail(AMapLocation aMapLocation);
    }

    public TrafficCity getTrafficCity() {
        return trafficCity;
    }

    public TrafficControlRule getControlRule() {
        return controlRule;
    }
}
