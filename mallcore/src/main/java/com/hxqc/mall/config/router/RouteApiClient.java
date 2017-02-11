package com.hxqc.mall.config.router;

import android.content.Context;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created 胡俊杰
 * 2016/9/23.
 * Todo:
 */

public class RouteApiClient extends BaseApiClient {
	@Override
	protected String completeUrl(String control) {
		return ApiUtil.getAPPConfigHostURL(control);
	}

	/**
	 * 版本更新
	 */
	public void checkUpdate(Context context,AsyncHttpResponseHandler handler) {
		String url = completeUrl("/apis/router");
		RequestParams requestParams = new RequestParams();
		requestParams.put("lastTime", new RouteSP(context).getRouterLastTime());
		gGetUrl(url, requestParams, handler);
	}
}
