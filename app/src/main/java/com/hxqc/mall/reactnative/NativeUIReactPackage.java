package com.hxqc.mall.reactnative;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.hxqc.mall.reactnative.nativeui.diyscroll.RSVManager;
import com.hxqc.mall.reactnative.nativeui.slider.SVADManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Author: wanghao
 * Date: 2016-03-02
 * FIXME
 * Todo
 */
public class NativeUIReactPackage implements ReactPackage {

    @Override
    public List<NativeModule > createNativeModules(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
    @Override
    public List<Class<? extends JavaScriptModule >> createJSModules() {
        return Collections.emptyList();
    }
    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Arrays.<ViewManager>asList(
                new RSVManager(),
                new SVADManager()
        );
    }

}
