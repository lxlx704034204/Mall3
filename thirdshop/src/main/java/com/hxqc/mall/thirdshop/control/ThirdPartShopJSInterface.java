package com.hxqc.mall.thirdshop.control;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.util.DebugLog;

/**
 * Author: wanghao
 * Date: 2015-12-18
 * FIXME
 * Todo
 */
public class ThirdPartShopJSInterface {

    private Context context;
    private T_NewImageViewControl control;

    public ThirdPartShopJSInterface(Context context, T_NewImageViewControl control) {
        this.context = context;
        this.control = control;
    }

    @JavascriptInterface
    public void showResponseView(String success) {
        DebugLog.i("test_html_js", success + "");
        if (control!=null){
            control.setPosition(Integer.parseInt(success));
            ActivitySwitcherThirdPartShop.toViewNewsImage(context);
        }
    }

}
