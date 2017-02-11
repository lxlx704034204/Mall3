package com.hxqc.mall.config.push;

import android.content.Context;

import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.controler.UserInfoSP;
import com.hxqc.util.DebugLog;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

/**
 * Author:胡俊杰
 * Date: 2015/9/2
 * FIXME
 * Todo
 */
public class PushMessageUtil {

	static PushMessageUtil umPushUtil;

	public PushMessageUtil() {
	}

	public static PushMessageUtil getInstance() {
		if (umPushUtil == null) {
			synchronized (PushMessageUtil.class) {
				if (umPushUtil == null) {
					umPushUtil = new PushMessageUtil();
				}
			}
		}
		return umPushUtil;
	}

	public void preparePushMessage(final Context context, boolean debug,String channel) {
		PushAgent mPushAgent = PushAgent.getInstance(context);
		mPushAgent.setDebugMode(debug);
		mPushAgent.setMessageChannel(channel);
		mPushAgent.setNotificationClickHandler(new CustomNotificationClickHandler());
		//自定义消息回调
		mPushAgent.setMessageHandler(new CustomMessageHandler());
		//注册推送服务，每次调用register方法都会回调该接口
		mPushAgent.register(new IUmengRegisterCallback() {

			@Override
			public void onSuccess(String deviceToken) {
				//注册成功会返回device token
				DebugLog.i("Tag", "DeviceToken  " + deviceToken);
				new UserInfoSP(context).saveUMengDeviceToken(deviceToken);
				UserInfoHelper.getInstance().bindDevice(context);
			}

			@Override
			public void onFailure(String s, String s1) {
				DebugLog.e("Tag","register  onFailure");
			}
		});

	}
}


