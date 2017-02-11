package com.hxqc.mall.reactnative;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.google.gson.reflect.TypeToken;
import com.hxqc.aroundservice.api.AroundServiceApiClient;
import com.hxqc.aroundservice.model.TrafficCity;
import com.hxqc.aroundservice.model.TrafficControlRule;
import com.hxqc.mall.amap.api.AroundApiClient;
import com.hxqc.mall.amap.model.SelfServiceWeatherModel;
import com.hxqc.mall.config.application.SampleApplicationContext;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.controler.AMapLocationControl;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.MapUtils;
import com.hxqc.mall.reactnative.nativemodule.FetchForToolData;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

/**
 * Author:  wh
 * Date:  2016/4/21
 * FIXME  首页需要的所有数据
 * Todo
 */
public class HomeDataHelper implements AMapLocationControl.onCoreLocationListener, AMapLocationControl.OnGetLocationFailCallBack {

    final private int MAX_REQUEST_TIMES = 2;

    int cityListRequestTimes = 0;
    int cityRuleRequestTimes = 0;
    int weatherRequestTimes = 0;

    Context context;

    private AMapLocationControl aMapLocationControl; //定位control

    String currentTempCity = "";
    String currentTempCityID = "";

    TrafficCity trafficCities;
    TrafficControlRule currentControlRule;
    private AroundApiClient aroundApiClient;
    private AroundServiceApiClient aroundServiceApiClient;
    SelfServiceWeatherModel weatherModel;

    boolean isFirstFetchData = true;

    //首页js所需参数回调
    OnHomeToolsMessageListener onHomeToolsMessageListener;


    public void setOnHomeToolsMessageListener(OnHomeToolsMessageListener onHomeToolsMessageListener) {
        this.onHomeToolsMessageListener = onHomeToolsMessageListener;
    }


    public interface OnHomeToolsMessageListener {
        void onCityNameGet(WritableMap params);

        void onTrafficRuleGet(WritableMap params);
    }


    private HomeDataHelper(Context context) {
        this.context = context;
        aroundApiClient = new AroundApiClient();
        aroundServiceApiClient = new AroundServiceApiClient();
        aMapLocationControl = AMapLocationControl.getInstance().setUpLocation(context.getApplicationContext());
        aMapLocationControl.setCoreLocationListener(this);
        aMapLocationControl.setOnGetLocationFailCallBack(this);
    }


    /**
     * 开始定位
     */
    private void startLocation() {
        DebugLog.i("home_data", "startLocation");
        if (aMapLocationControl == null) {
            aMapLocationControl = AMapLocationControl.getInstance().setUpLocation(context.getApplicationContext());
            DebugLog.i("home_data", "startLocation inner");
        }
        aMapLocationControl.setCoreLocationListener(this);
        aMapLocationControl.setOnGetLocationFailCallBack(this);
        aMapLocationControl.startLocation();
    }


    public void startFetchData(String cityName, boolean isFirst) {
        DebugLog.i("home_data", "startFetchData");

        cityListRequestTimes = 0;
        cityRuleRequestTimes = 0;
        weatherRequestTimes = 0;

        if (isFirst) {
            DebugLog.i("home_data", "startFetchData1");
            isFirstFetchData = true;
            startLocation();
        } else {
            DebugLog.i("home_data", "startFetchData2");
            if (!TextUtils.isEmpty(cityName)) {
                currentTempCity = cityName;
                backCityName();
//                requestDataForChangeCity();
            }
        }
    }


    private static HomeDataHelper instance;


    public static HomeDataHelper getInstance(Context context) {
        if (instance == null) synchronized (HomeDataHelper.class) {
            if (instance == null) {
                instance = new HomeDataHelper(context);
            }
        }
        return instance;
    }


    @Override
    public void onLocationChange(AMapLocation aMapLocation) {
        DebugLog.i("home_data", "HomeDataHelper onLocationChange");
        if (isFirstFetchData) {
            DebugLog.i("home_data", "HomeDataHelper onLocationChange inner");
            isFirstFetchData = false;
            String cityName = aMapLocation.getCity();
            if (cityName.contains("市")) {
                cityName = cityName.replace("市", "");
            }
            currentTempCity = cityName;
            backCityName();
//            requestForRlueCityList();

            double latitude = aMapLocation.getLatitude();//经度
            double longitude = aMapLocation.getLongitude();//维度
            String province = aMapLocation.getProvince();
            String city = aMapLocation.getCity();
            String district = aMapLocation.getDistrict();
            BaseSharedPreferencesHelper sharedPreferencesHelper = new BaseSharedPreferencesHelper(SampleApplicationContext.application);
            sharedPreferencesHelper.setLatitude(latitude + "");
            sharedPreferencesHelper.setLongitude(longitude + "");
            LatLng latLng = MapUtils.bd_encrypt(latitude, longitude);
            sharedPreferencesHelper.setLatitudeBD(latLng.latitude + "");
            sharedPreferencesHelper.setLongitudeBD(latLng.longitude + "");
            DebugLog.e("APi", latLng.toString());
            sharedPreferencesHelper.setProvince(province);
            sharedPreferencesHelper.setCity(city);
            sharedPreferencesHelper.setDistrict(district);
        }
    }


    @Override
    public void onLocationFail(AMapLocation aMapLocation) {
//        DebugLog.i("home_data", "onLocationFail");
    }


    private void requestDataForChangeCity() {
        DebugLog.i("home_data", "requestDataForChangeCity 1");
        if (isCityListExist()) {
            DebugLog.i("home_data", "requestDataForChangeCity 2");
            if (isSearchRuleThisCityAvailable(currentTempCity)) {
                requestForCityRule(currentTempCityID);
            } else {
                backTrafficInfo("null");
            }
        } else {
            DebugLog.i("home_data", "requestDataForChangeCity 3");
            requestForRlueCityList();
        }
    }


    private boolean isCityListExist() {
        return trafficCities != null;
    }


    /**
     * 获取限行 查询城市列表
     */
    private void requestForRlueCityList() {
        DebugLog.i("home_data", "HomeDataHelper requestForRlueCityList ");
        aroundServiceApiClient.weizhangXianxingCity(new BaseMallJsonHttpResponseHandler(context) {
            @Override
            public void onSuccess(String response) {
                trafficCities = JSONUtils.fromJson(response, new TypeToken< TrafficCity >() {
                });
                if (trafficCities != null) {
                    if (isSearchRuleThisCityAvailable(currentTempCity)) {
                        requestForCityRule(currentTempCityID);
                    } else {
                        backTrafficInfo("null");
                    }
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                DebugLog.i("home_data", "HomeDataHelper requestForRlueCityList onFailure");
                if (cityListRequestTimes > MAX_REQUEST_TIMES) {
                } else {
                    cityListRequestTimes++;
                    requestForRlueCityList();
                }
            }
        });
    }


    /**
     * 是否是可以查询的限行城市
     *
     * @param cityName 城市名称
     * @return 是否可查
     */
    public boolean isSearchRuleThisCityAvailable(String cityName) {
        DebugLog.i("home_data", "isSearchRuleThisCityAvailable");
        boolean isAvailable = false;
        if (trafficCities != null && trafficCities.result != null && trafficCities.result.size() > 0) {
            for (int i = 0; i < trafficCities.result.size(); i++) {
                if (cityName.equals(trafficCities.result.get(i).cityname)) {
                    isAvailable = true;
                    currentTempCityID = trafficCities.result.get(i).cityid;
                }
            }
        }
        return isAvailable;
    }


    /**
     * 查询限行规则
     */
    private void requestForCityRule(final String cityID) {
        DebugLog.i("home_data", "requestForCityRule:");
        if (!TextUtils.isEmpty(cityID)) {
            aroundServiceApiClient.weizhangXianxingQuery(cityID, new LoadingAnimResponseHandler(context, false) {
                @Override
                public void onSuccess(String response) {
                    currentControlRule = JSONUtils.fromJson(response, new TypeToken< TrafficControlRule >() {
                    });
                    DebugLog.i("home_data", "HomeDataHelper requestForCityRule onSuccess");
                    if (currentControlRule != null) {
//                        clearUpInfoAndCallback(trafficMessage(), weatherMessage());
                        backTrafficInfo(trafficMessage());
                    }
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    DebugLog.i("home_data", "HomeDataHelper requestForCityRule onFailure");
                    if (cityRuleRequestTimes > MAX_REQUEST_TIMES) {
//                        clearUpInfoAndCallback("null", weatherMessage());
                    } else {
                        cityRuleRequestTimes++;
                        requestForCityRule(cityID);
                    }
                }
            });
        }
    }


    private void backTrafficInfo(String t) {
        WritableMap params = Arguments.createMap();
        params.putString(FetchForToolData.traffic_tag, t);
        if (onHomeToolsMessageListener != null) onHomeToolsMessageListener.onTrafficRuleGet(params);
    }


    private void backCityName() {
        WritableMap params = Arguments.createMap();
        params.putString(FetchForToolData.city_tag, currentTempCity);
        if (onHomeToolsMessageListener != null) onHomeToolsMessageListener.onCityNameGet(params);
    }


    /**
     * 拼接 当前城市限行规则
     */
    private String trafficMessage() {
        DebugLog.i("home_data", "trafficMessage:");
        String str = "今日限行尾号为";
        if (currentControlRule != null && currentControlRule.result != null && currentControlRule.result.data != null && currentControlRule.result.data.size() > 0) {
            str += currentControlRule.result.data.get(0).num;
            str = str.replaceAll(",", "、");
            str = str.replaceAll("，", "、");
        }
        DebugLog.i("home_data", "trafficMessage:" + str.substring(1, str.length()));
        return str;
    }

}
