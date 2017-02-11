package com.hxqc.mall.interfaces;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.model.ActiveEventJSR;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

/**
 * Author: wanghao
 * Date: 2015-05-14
 * FIXME
 * Todo
 */
public class EventActiveInterface {

    public  static String EVENT_URL= ApiUtil.AccountHostURL  + "/Mall/App/Active/index";
    Context context;

    public EventActiveInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void showResponseView(String success) {
        DebugLog.i("event_", "web++++: " + success);
        ActiveEventJSR activeEventJSR = JSONUtils.fromJson(success, ActiveEventJSR.class);
        if (activeEventJSR != null) {
            ActivitySwitcher.toAutoItemDetail(context, activeEventJSR.type,
                    activeEventJSR.itemID, activeEventJSR.title);
        }
    }

}
