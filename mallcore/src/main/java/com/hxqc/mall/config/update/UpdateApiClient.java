package com.hxqc.mall.config.update;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created 胡俊杰
 * 2016/9/23.
 * Todo:
 */

public class UpdateApiClient extends BaseApiClient {
	@Override
	protected String completeUrl(String control) {
		return ApiUtil.getAPPConfigHostURL(control);
	}

	/**
	 * 版本更新
	 */
	public  void checkUpdate(AsyncHttpResponseHandler handler) {
		String url = completeUrl("/apis/release/version");
		RequestParams requestParams = new RequestParams();
		requestParams.put("os", "Android");
		requestParams.put("appName", "恒信汽车");
		gGetUrl(url, requestParams, handler);
	}
}
