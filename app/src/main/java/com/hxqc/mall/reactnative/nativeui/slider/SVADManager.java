package com.hxqc.mall.reactnative.nativeui.slider;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.hxqc.util.DebugLog;


/**
 * Author: wanghao
 * Date: 2016-03-26
 * FIXME  原生轮播  交互rn
 * Todo
 */
public class SVADManager extends ViewGroupManager<SliderLayoutForJS> {
    final private String TAG = "SliderLayoutForJS";

    @Override
    public String getName() {
        return "RCTSliderADView";
    }

    @Override
    protected SliderLayoutForJS createViewInstance(ThemedReactContext reactContext) {
        return new SliderLayoutForJS(reactContext);
    }

    @ReactProp(name = "adData")
    public void setADData(SliderLayoutForJS view, String json) {
          DebugLog.e(TAG, "SVADManager: "+json);
          view.setData(json);
    }
}
