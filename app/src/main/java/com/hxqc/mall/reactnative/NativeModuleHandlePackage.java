package com.hxqc.mall.reactnative;


import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.hxqc.mall.reactnative.nativemodule.ClickForScanCode;
import com.hxqc.mall.reactnative.nativemodule.FetchForToolData;
import com.hxqc.mall.reactnative.nativemodule.JSEventHandle;
import com.hxqc.mall.reactnative.nativemodule.PhoneInfoModule;
import com.hxqc.mall.reactnative.nativemodule.SecurityJSJavaModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: wanghao
 * Date: 2016-03-02
 * FIXME
 * Todo
 */
public class NativeModuleHandlePackage implements ReactPackage  {


    @Override
    public List< NativeModule > createNativeModules(ReactApplicationContext reactContext) {

        List<NativeModule> modules = new ArrayList<>();
        modules.add(new JSEventHandle(reactContext));
        modules.add(new SecurityJSJavaModule(reactContext));
        //天气 限行 首页 版本  数据获取  module
        modules.add(new FetchForToolData(reactContext));
        //手机屏幕等 数据获取
        modules.add(new PhoneInfoModule(reactContext));
        //扫码点击
        modules.add(new ClickForScanCode(reactContext));

        return modules;
    }

    @Override
    public List< Class< ? extends JavaScriptModule > > createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List< ViewManager > createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }



}
