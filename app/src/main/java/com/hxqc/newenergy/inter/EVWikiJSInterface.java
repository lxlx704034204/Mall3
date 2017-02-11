package com.hxqc.newenergy.inter;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.hxqc.util.DebugLog;

/**
 * Author: wanghao
 * Date: 2016-03-25
 * FIXME
 * Todo
 */
public class EVWikiJSInterface {

    Context context;

    WebJSResponseListener listener;

    public void setListener(WebJSResponseListener listener) {
        this.listener = listener;
    }

    public interface WebJSResponseListener{
        void getSwitchResponse(String response);
    }

    public EVWikiJSInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void showResponseView(String success) {
        DebugLog.i("ev_wiki", "web++++: " + success);

        if (listener!=null){
            listener.getSwitchResponse(success);
        }
    }

}
