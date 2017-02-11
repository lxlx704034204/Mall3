package com.hxqc.mall.config.push;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.config.router.RouteOpenActivityUtil;
import com.hxqc.util.JSONUtils;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

/**
 * Author:胡俊杰
 * Date: 2016/6/30
 * FIXME
 * Todo 自定义推送消息
 */
public class CustomMessageHandler extends UmengMessageHandler {
    @Override
    public void dealWithCustomMessage(final Context context, final UMessage msg) {
        new Handler(context.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
//                    Toast.makeText(context, "推送测试" + msg.custom, Toast.LENGTH_LONG).show();
                if (TextUtils.isEmpty(msg.custom)) return;
                PushCustomMessage customMessage = JSONUtils.fromJson(msg.custom,
                        new TypeToken<PushCustomMessage>() {
                        });
                if (customMessage == null) return;
                if (customMessage.notificationType == PushCustomMessage.NotificationType_OrderPay) {
                    RouteOpenActivityUtil.payMessage(context, customMessage);
                }
            }
        });
    }


}

