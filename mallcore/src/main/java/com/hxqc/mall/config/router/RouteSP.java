package com.hxqc.mall.config.router;

import android.content.Context;

import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;

/**
 * Created 胡俊杰
 * 2016/9/23.
 * Todo:
 */

public class RouteSP extends BaseSharedPreferencesHelper {
	public RouteSP(Context context) {
		super(context);
	}

	/**
	 * 保存最新更新时间
	 */
	public String getRouterLastTime() {
		return shared.getString("routeLastTime", "");
	}

	/**
	 * 获取最新更新时间
	 */
	public void saveRouterLastTime(String routeLastTime) {
		shared.edit().putString("routeLastTime", routeLastTime).apply();
	}


}
