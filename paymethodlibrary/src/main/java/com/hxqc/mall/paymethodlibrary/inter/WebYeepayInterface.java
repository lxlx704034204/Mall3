package com.hxqc.mall.paymethodlibrary.inter;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.hxqc.mall.paymethodlibrary.manager.PayCallBackManager;
import com.hxqc.mall.paymethodlibrary.model.EventGetSuccessModel;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.mall.paymethodlibrary.util.PayMethodConstant;
import com.hxqc.mall.paymethodlibrary.yeepay.YeePayActivity;

import org.greenrobot.eventbus.EventBus;


/**
 * Author: wanghao
 * Date: 2015-04-02
 * FIXME
 * 易宝js回调接口
 */
public class WebYeepayInterface {

    Context context;

    public WebYeepayInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void showResponseView(String success) {
        Log.i("test_pay", "web++++: " + success);

        EventBus.getDefault().post(new EventGetSuccessModel(PayConstant.YEEPAY_SUCCESS, success, PayMethodConstant.YEEPAY_TYPE));

        PayCallBackManager.getInstance().onPayCallBack(new EventGetSuccessModel(PayConstant.YEEPAY_SUCCESS, success, PayMethodConstant.YEEPAY_TYPE));

        ((YeePayActivity) context).finish();
    }

}
