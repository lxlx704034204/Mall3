package com.hxqc.mall.config.push;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.config.router.RouteOpenActivityUtil;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;
import com.umeng.message.UTrack;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

/**
 * Author:胡俊杰
 * Date: 2016/6/30
 * FIXME
 * Todo
 */
public class CustomNotificationClickHandler extends UmengNotificationClickHandler {

	@Override
	public void handleMessage(Context context, UMessage uMessage) {
		super.handleMessage(context, uMessage);

//		if (TextUtils.isEmpty(uMessage.custom)) return;
//		PushCustomMessage customMessage = JSONUtils.fromJson(uMessage.custom,
//				new TypeToken<PushCustomMessage>() {
//				});
//		if (customMessage == null) return;
//		switch (customMessage.notificationType) {
//			case PushCustomMessage.NotificationType_Promotion://活动
//				String url = customMessage.url;
//				RouteOpenActivityUtil.linkToActivity(context, url);
//				break;
//			case PushCustomMessage.NotificationType_OrderDetail://订单详情
//				String orderID = customMessage.orderID;
//				ActivitySwitchBase.toOrderDetail(context, orderID);
//				break;
//			case PushCustomMessage.NotificationType_OrderPay:
//				RouteOpenActivityUtil.payMessage(context, customMessage);
//				break;
//			default:
//				break;
//		}
	}

	@Override
	public void dealWithCustomAction(Context context, UMessage uMessage) {
		super.dealWithCustomAction(context, uMessage);
		//点击消息
		DebugLog.i("Tag", "推送点击消息  uMessage.custom  " + uMessage.custom);
		UTrack.getInstance(context).trackMsgClick(uMessage);
		if (TextUtils.isEmpty(uMessage.custom)) return;
		PushCustomMessage customMessage = JSONUtils.fromJson(uMessage.custom,
				new TypeToken<PushCustomMessage>() {
				});
		if (customMessage == null) return;
		switch (customMessage.notificationType) {
			case PushCustomMessage.NotificationType_Promotion://活动
				String url = customMessage.url;
				RouteOpenActivityUtil.linkToActivity(context, url);
				break;
			case PushCustomMessage.NotificationType_OrderDetail://订单详情
				String orderID = customMessage.orderID;
				ActivitySwitchBase.toOrderDetail(context, orderID);
				break;
			case PushCustomMessage.NotificationType_OrderPay:
				RouteOpenActivityUtil.payMessage(context, customMessage);
				break;
			default:
				break;
		}
	}


	@Override
	public void dismissNotification(Context context, UMessage uMessage) {
		super.dismissNotification(context, uMessage);
		DebugLog.i("Tag", "push   dismissNotification");
		//忽略消息
		UTrack.getInstance(context).trackMsgDismissed(uMessage);
	}
}
