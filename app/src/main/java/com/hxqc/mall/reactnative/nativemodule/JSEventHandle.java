package com.hxqc.mall.reactnative.nativemodule;


import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.hxqc.mall.activity.MainActivity;
import com.hxqc.mall.reactnative.manager.HomeJumpManager;
import com.hxqc.util.DebugLog;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Author: wanghao
 * Date: 2016-03-02
 * FIXME   首页跳转处理
 * Todo
 */
public class JSEventHandle extends ReactContextBaseJavaModule {

    //广告栏
    private static final String AD_SLIDER = "AD_SLIDER";
    //在用按钮
    private static final String USE_MODULE = "USE_MODULE";
    //其他未实现
    private static final String OTHER_S = "OTHER_S";
    //--------------------------some    tag-------------------------

    private MainActivity currentActivity;

    public JSEventHandle(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "JSSwitchEventHandle";
    }

    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(AD_SLIDER, AD_SLIDER);
        constants.put(USE_MODULE, USE_MODULE);
        constants.put(OTHER_S, OTHER_S);
        return constants;
    }

    @ReactMethod
    public void turn(String title, String type, String jsonStr) {
        currentActivity = (MainActivity) getCurrentActivity();
        DebugLog.d("JSEventHandle", title + "<==>json:" + jsonStr);
        HomeJumpManager.getInstance().jump(currentActivity,title,type,jsonStr);
    }

    @ReactMethod
    public void onlyRNTurn(String title, String type, String jsonStr) {
        currentActivity = (MainActivity) getCurrentActivity();
        DebugLog.d("JSEventHandle", title + "<==>json:" + jsonStr);
        HomeJumpManager.getInstance().onlyRNJump(currentActivity,title,type,jsonStr);
    }


}
