package com.hxqc.mall.reactnative.api;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.hxqc.util.DebugLog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import hxqc.mall.BuildConfig;

/**
 * Created 胡俊杰
 * 2016/10/24.
 * Todo:
 */

public class RNPatchApiClient extends BaseApiClient {

	/**
	 * 2.与网络上的md5作比较
	 */
	public void verdifyMD5WithServer(String fileMD5, AsyncHttpResponseHandler handler) {
		/**
		 * 有更新 下载补丁包
		 *
		 * 无更新  不作为
		 */
		client.addHeader("accept", "*/*");

		RequestParams requestParams = new RequestParams();
		requestParams.put("rnVersion", fileMD5);
//        requestParams.put("appVersion", BuildConfig.RNVersionName);
		requestParams.put("appVersion", BuildConfig.RNVersionName);
//		requestParams.put("appName", "恒信汽车");
		requestParams.put("appKey", ApiUtil.ConfigAppKey);
		requestParams.put("platform", "Android");

		DebugLog.i(TAG, "rnVersion: " + fileMD5 + " appVersion: " + BuildConfig.RNVersionName);
		DebugLog.i(TAG, "bsdiffURL: " + completeUrl("/apis/RNVersion")
				+ "?rnVersion=" + fileMD5 + "&appVersion=" + BuildConfig.RNVersionName
				+ "&platform=Android&appKey="+ApiUtil.ConfigAppKey);
//		requestParams = getDESRequestParams(RNConfigUtil.getMd5VerdifyURL(),requestParams);
		//请求增量更新 参数
		gGetUrl(completeUrl("/apis/RNVersion"), requestParams, handler);
	}

	@Override
	protected String completeUrl(String control) {
		return ApiUtil.RNBsdiffURL + control;
	}
}
