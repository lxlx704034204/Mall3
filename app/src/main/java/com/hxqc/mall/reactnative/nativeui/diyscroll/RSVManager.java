package com.hxqc.mall.reactnative.nativeui.diyscroll;

import android.view.View;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import javax.annotation.Nullable;

/**
 * Author: wanghao
 * Date: 2016-03-02
 * FIXME
 * Todo
 */
public class RSVManager extends ViewGroupManager<StretchScrollView> {

    private static final String REACT_CLASS = "RCTStretchScrollView";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected StretchScrollView createViewInstance(ThemedReactContext reactContext) {
        return new StretchScrollView(reactContext);
    }

    @ReactProp(name = "isScrollNever")
    public void setScrollModeRP(StretchScrollView view,@Nullable boolean isScrollNever) {
        //  Log.e("TAG", "setUrl");
        if (isScrollNever) {
            view.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
    }

    @ReactProp(name = "showsVerticalScrollIndicator")
    public void setShowsVerticalScrollIndicator(StretchScrollView view, boolean value) {
        view.setVerticalScrollBarEnabled(value);
    }
}
