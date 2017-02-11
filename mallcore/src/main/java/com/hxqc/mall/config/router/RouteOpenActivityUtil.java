package com.hxqc.mall.config.router;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.hxqc.mall.config.push.PushCustomMessage;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.order.OrderType;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.payment.util.PaymentActivitySwitch;
import com.hxqc.util.DebugLog;

import java.util.Set;

/**
 * Author:胡俊杰
 * Date: 2016/6/13
 * FIXME
 * Todo
 */
public class RouteOpenActivityUtil {

	public static void linkToActivity(Context context, Intent intent) {
		String action = intent.getAction();
		DebugLog.e("MainActivity", "------------- action ------------   "+action);
		if (Intent.ACTION_VIEW.equals(action)) {
			parserSingInUri(context, intent.getData());
		}
	}

	/**
	 * 路由跳转
	 * @param context
	 * @param url   scheme为hxmall时路由跳转
	 *              http或者https时打开h5页面
	 */
	public static void linkToActivity(Context context, String url) {
		// TODO: 2016/6/30
		if (TextUtils.isEmpty(url)) return;
		try {
			parserSingInUri(context, Uri.parse(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 付款消息
	 */
	public static void payMessage(Context context, PushCustomMessage pushCustomMessage) {
		if (pushCustomMessage == null) {
			return;
		}
		if (pushCustomMessage.errorCode != 0) {
			//未付款
			switch (OrderType.getOrderType(pushCustomMessage.orderType)) {
				case wash_car:
					PaymentActivitySwitch.toAroundPaymentActivity
							(context, String.valueOf(pushCustomMessage.money), pushCustomMessage.orderID, 7,"-1");
					break;
			}
		} else {
			//付款成功
			switch (OrderType.getOrderType(pushCustomMessage.orderType)) {
				case wash_car:
					PaymentActivitySwitch.toWashCarPayFinish
							(context, pushCustomMessage.orderID);
					break;
			}
		}

	}


	/**
	 * 判断打开方式
	 *
	 * @param context
	 * @param uri
	 */
	private static void parserSingInUri(final Context context, final Uri uri) {
		DebugLog.e("MainActivity", "------------- uri ------------   "+uri);
		if (uri != null) {
			String scheme = uri.getScheme().trim().toLowerCase();
			DebugLog.e("MainActivity", "------------- scheme ------------   "+scheme);
			if (scheme.equals("hxmall")) {
				boolean singIn = uri.getBooleanQueryParameter("toSingIn", false);
				if (singIn) {
					UserInfoHelper.getInstance().loginAction(context, new UserInfoHelper.OnLoginListener() {
						@Override
						public void onLoginSuccess() {
							routerToActivity(context, uri);
						}
					});
				} else {
					routerToActivity(context, uri);
				}
			} else if (scheme.equals("http") || scheme.equals("https")) {
				ActivitySwitchBase.toH5Activity(context, "活动详情", uri.toString());
			}

		}
	}

	/**
	 * 解析参数跳转
	 *
	 * @param context
	 * @param uri
	 */
	private static void routerToActivity(Context context, Uri uri) {
		String module = uri.getHost() + uri.getPath();
		RouteModule routeModule =  RouteManager.getRouteModule(module);
		DebugLog.e("MainActivity", "------------- routeModule ------------   "+routeModule.toString());
		String className = routeModule.getClassName();
		Bundle bundle = routeModule.getParamsBundle();
		if (TextUtils.isEmpty(className)) {
			return;
		}
		Set<String> pNames = uri.getQueryParameterNames();
		if (pNames != null && pNames.size() > 0) {
			for (String s : pNames) {
				bundle.putString(s, uri.getQueryParameter(s));
			}
		}
		ActivitySwitchBase.toWhere(context, className, bundle);

	}

}
