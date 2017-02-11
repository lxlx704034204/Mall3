package com.hxqc.mall.paymethodlibrary.wechat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hxqc.mall.paymethodlibrary.util.PayKeyConstants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Author: wanghao
 * Date: 2015-09-18
 * FIXME
 * Todo
 */
public class AppRegister extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            final IWXAPI api = WXAPIFactory.createWXAPI(context, null);
            api.registerApp(PayKeyConstants.getWxAppId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
