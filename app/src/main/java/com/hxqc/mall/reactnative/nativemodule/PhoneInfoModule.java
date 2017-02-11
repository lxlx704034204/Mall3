package com.hxqc.mall.reactnative.nativemodule;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.hxqc.util.DebugLog;
import com.hxqc.util.ScreenUtil;

import java.lang.reflect.Field;

/**
 * Author:  wh
 * Date:  2016/4/28
 * FIXME
 * Todo
 */
public class PhoneInfoModule extends ReactContextBaseJavaModule {

    Context context;

    public PhoneInfoModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
    }

    @Override
    public String getName() {
        return "RNPhoneInfoModule";
    }

    /**
     * 获取状态栏高度
     */
    @ReactMethod
    public void getPhoneHeadHeight(Callback successCallback) {
        DebugLog.w("RN_phone_info", "getStatusHeight: " + getBarHeight());

        int i = ScreenUtil.px2dip(context, getBarHeight());

        DebugLog.w("RN_phone_info", "getStatusHeight: " + i);

        successCallback.invoke(i);
    }

    /**
     * 获取菜单软键盘高度
     */
    @ReactMethod
    public void getPhoneMenuKeyboardHeight(Callback successCallback) {

        int height = 0;
        Resources resources = context.getResources();
        int rid = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid > 0) {
            DebugLog.w("RN_phone_info", resources.getBoolean(rid) + ""); //获取导航栏是否显示true or false
            if (resources.getBoolean(rid)) {
                int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    DebugLog.w("RN_phone_info", resources.getDimensionPixelSize(resourceId) + ""); //获取高度
                    height = resources.getDimensionPixelSize(resourceId);
                    height = ScreenUtil.px2dip(context, height);
                }
            }
        }


        DebugLog.w("RN_phone_info", " brand: "+Build.BRAND+" 型号: "+ Build.DEVICE+" MODEL:  "+Build.MODEL+" PRODUCT : "+Build.PRODUCT+" BOARD: "+Build.BOARD);
        DebugLog.w("RN_phone_info", height + "");

        if ("Meizu".equals(Build.BRAND)){
            height = 0;
        }

        if ("Sony".equals(Build.BRAND)){
            height = 0;
        }

        height = 0;

        successCallback.invoke(height);

    }

    public int getBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 38;//默认为38，貌似大部分是这样的
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }
}
