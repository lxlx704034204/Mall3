package com.hxqc.mall.config.router;

import android.content.Context;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.bzinga.DES3;
import com.hxqc.mall.core.db.CoreDatabase;
import com.hxqc.mall.core.db.DbHelper;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created 胡俊杰
 * 2016/9/26.
 * Todo: 路由的增删改查
 */

public class RouteManager {

	public static RouteModule getRouteModule(String module) {
		return DbHelper.queryEntity(RouteModule.class, RouteModule_Table.module.eq(module));
	}

	public void checkRouter(final Context context) {
		RouteApiClient apiClient = new RouteApiClient();
		apiClient.checkUpdate(context, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				RouteResponse routeResponse = null;
				try {
					if (!ApiUtil.isDebug) {
						responseString = DES3.decode(responseString);
					}
					DebugLog.i("Tag", responseString);
					routeResponse = JSONUtils.fromJson(responseString, RouteResponse.class);
					if (routeResponse == null) return;
					new RouteSP(context).saveRouterLastTime(routeResponse.lastTime);
					updateRouteValue(routeResponse.data);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 更新路由表
	 *
	 * @param routeModules
	 */
	private void updateRouteValue(ArrayList<RouteModule> routeModules) {
		DbHelper.saveTransaction(CoreDatabase.class, routeModules);
	}


}
