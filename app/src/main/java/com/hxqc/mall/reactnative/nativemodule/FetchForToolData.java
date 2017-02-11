package com.hxqc.mall.reactnative.nativemodule;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.hxqc.mall.activity.MainActivity;
import com.hxqc.mall.activity.PositionActivity;
import com.hxqc.mall.reactnative.HomeDataHelper;
import com.hxqc.util.DebugLog;

import java.util.Calendar;

/**
 * Author:  wh
 * Date:  2016/4/20
 * FIXME
 * Todo  获取天气 今日限行等信息
 */
public class FetchForToolData extends ReactContextBaseJavaModule implements HomeDataHelper.OnHomeToolsMessageListener,
        MainActivity.GetHomeNameListener, LifecycleEventListener {

    //    int i = 0;
    ReactContext rc;

    final public static String weather_icon_tag = "weather_icon";
    final public static String traffic_tag = "traffic_message";
    final public static String weather_tag = "weather_message";
    final public static String city_tag = "city_name";
    final public static String current_month = "current_month";
    final public static String current_day = "current_day";
    final public static int request_code = 123;

    HomeDataHelper dataHelper;

    public FetchForToolData(ReactApplicationContext reactContext) {
        super(reactContext);
        rc = reactContext;
        reactContext.addLifecycleEventListener(this);
        dataHelper = HomeDataHelper.getInstance(rc.getApplicationContext());
        dataHelper.setOnHomeToolsMessageListener(this);
    }

    @Override
    public String getName() {
        return "ToolsModule";
    }

    //打开城市列表
    @ReactMethod
    public void openCityList() {
        DebugLog.i("home_data", "getThings");
        MainActivity currentActivity = (MainActivity) getCurrentActivity();
        if (currentActivity != null) {
            currentActivity.setGetHomeName(this);
            DebugLog.i("home_data", "getThings currentActivity");
            Intent intent = new Intent(rc, PositionActivity.class);
            currentActivity.startActivityForResult(intent, request_code, null);
        }
    }

    //初始化首页
    @ReactMethod
    public void firstInitData() {
        DebugLog.i("home_data_life", "FetchForToolData firstInitData");
        if (dataHelper != null)
            dataHelper.startFetchData("", true);
    }

    private void sendEvent(String eventName, @Nullable WritableMap params) {
        if (params!=null){
            params.putString(FetchForToolData.current_month, getCurrrentMonth());
            params.putString(FetchForToolData.current_day, getCurrrentDay());
            rc.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
        }
    }

    @Override
    public void getName(String name) {
        DebugLog.i("home_data", "FetchForToolData getName" + name);
        if (!TextUtils.isEmpty(name)) {
            if (name.contains("市")) {
                name = name.replace("市", "");
                WritableMap params = Arguments.createMap();
                params.putString(FetchForToolData.traffic_tag, "");
                params.putString(FetchForToolData.weather_tag, "获取中...");
                params.putString(FetchForToolData.city_tag, name);
                sendEvent("homeToolsShow", params);

                startRequestData(name);
            }
        }
    }

    public String getCurrrentMonth() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        DebugLog.i("home_data", "FetchForToolData1 getCurrrentMonth" + month);
        if (month<10){
            return "0"+month;
        }
        return ""+month;
    }

    public String getCurrrentDay() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        DebugLog.i("home_data", "FetchForToolData1 getCurrrentDay" + day);
        if (day<10){
            return "0"+day;
        }
        return ""+day;
    }

    public void startRequestData(String cityName) {
        if (dataHelper != null)
            dataHelper.startFetchData(cityName, false);
    }

    @Override
    public void onCityNameGet(WritableMap params) {
        sendEvent("homeCityName", params);
    }

    @Override
    public void onTrafficRuleGet(WritableMap params) {
        sendEvent("homeTrafficShow", params);
    }

    @Override
    public void onHostResume() {
        DebugLog.w("home_data_life", "FetchForToolData onHostResume");
//        firstInitData();
    }

    @Override
    public void onHostPause() {
        DebugLog.w("home_data_life", "FetchForToolData onHostPause");
    }

    @Override
    public void onHostDestroy() {
        DebugLog.w("home_data_life", "FetchForToolData onHostDestroy");
    }
}
