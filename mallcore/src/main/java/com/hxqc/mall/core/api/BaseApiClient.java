package com.hxqc.mall.core.api;


import android.content.Context;

import com.hxqc.mall.config.application.SampleApplicationContext;
import com.hxqc.mall.core.bzinga.DES3;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.util.DebugLog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import cz.msebera.android.httpclient.conn.ConnectionPoolTimeoutException;


/**
 * Created by 胡俊杰 on 2015/1/13.
 */
public abstract class BaseApiClient {

	public static final String UserAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0 " + "HXMall Android";
	protected static final String TAG = "ApiClient";
	public static int CONNECTION_TIMEOUT = 5 * 1000;
	public static int SOCKET_TIMEOUT = 5 * 1000;
	protected AsyncHttpClient client;

	{
		// The following exceptions will be whitelisted, i.e.: When an exception
		// of this type is raised, the request will be retried.
		AsyncHttpClient.allowRetryExceptionClass(IOException.class);
		AsyncHttpClient.allowRetryExceptionClass(SocketTimeoutException.class);
		AsyncHttpClient.allowRetryExceptionClass(ConnectTimeoutException.class);

		// The following exceptions will be blacklisted, i.e.: When an exception
		// of this type is raised, the request will not be retried and it will
		// fail immediately.
		AsyncHttpClient.blockRetryExceptionClass(UnknownHostException.class);
		AsyncHttpClient.blockRetryExceptionClass(ConnectionPoolTimeoutException.class);
	}

	protected BaseApiClient() {


		client = new AsyncHttpClient(true, 0, 0);
		client.setConnectTimeout(CONNECTION_TIMEOUT);
		client.setTimeout(SOCKET_TIMEOUT);
		client.setUserAgent(UserAgent);

	}


	protected abstract String completeUrl(String control);


	/**
	 * 获取加密Params
	 */
	public RequestParams getDESRequestParams(String url, RequestParams requestParams) {
		RequestParams requestParam = paramsAddDeviceType(requestParams);
		String tUrl = AsyncHttpClient.getUrlWithQueryString(false, url, requestParam);
		DebugLog.v(TAG, "--未加密--  " + tUrl);
		tUrl = tUrl.substring(tUrl.indexOf("?") + 1);

		if (ApiUtil.isDebug) {
			try {
				tUrl = DES3.encode(tUrl);
				RequestParams newParams1 = new RequestParams();
				newParams1.put("p", tUrl);
				DebugLog.v(TAG, "--  加密--  " + AsyncHttpClient.getUrlWithQueryString(false, url, newParams1));
			} catch (Exception e) {
				e.printStackTrace();
			}

			return requestParam;
		}
		tUrl = tUrl.substring(tUrl.indexOf("?") + 1);
		try {
			tUrl = DES3.encode(tUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestParams newParams = new RequestParams();
		newParams.put("p", tUrl);
		return newParams;

	}


	protected String getRequestUrl(String url, RequestParams requestParams) {
		RequestParams newParams = getDESRequestParams(url, paramsAddDeviceType(requestParams));
		return AsyncHttpClient.getUrlWithQueryString(true, url, newParams);
	}

	/**
	 * get请求
	 */
	protected void gGetUrl(String url, AsyncHttpResponseHandler handler) {
		gGetUrl(url, null, handler);
	}


	/**
	 * get请求
	 *
	 * @param url
	 * @param requestParams 未加密Param
	 * @param handler       TODO   打包时注释
	 */
	protected void gGetUrl(String url, RequestParams requestParams, AsyncHttpResponseHandler handler) {
		RequestParams newParams = getDESRequestParams(url, paramsAddDeviceType(requestParams));
		client.get(url, newParams, handler);
		DebugLog.d(TAG, AsyncHttpClient.getUrlWithQueryString(true, url, newParams));
		DebugLog.i(TAG, "--------------------------------------------------------------------------");
		//测试广播内请求log
//		Log.d("TFS_Tag", AsyncHttpClient.getUrlWithQueryString(true, url, newParams));
	}


	protected RequestParams paramsAddDeviceType(RequestParams requestParams) {
		if (requestParams == null) {
			requestParams = new RequestParams();
		}
		requestParams.put("deviceType", "Android");
		requestParams.put("token", UserInfoHelper.getInstance().getToken(SampleApplicationContext.application));
		return requestParams;
	}


	/**
	 * post请求
	 *
	 * @param url
	 * @param handler
	 */
	protected void gPostUrl(String url, AsyncHttpResponseHandler handler) {
		gPostUrl(url, null, handler);
	}


	/**
	 * post请求
	 *
	 * @param url
	 * @param requestParams 未加密Param
	 * @param handler
	 */
	protected void gPostUrl(String url, RequestParams requestParams, AsyncHttpResponseHandler handler) {

		RequestParams newParams = getDESRequestParams(url, requestParams);
		client.post(url, newParams, handler);
		DebugLog.d(TAG, AsyncHttpClient.getUrlWithQueryString(true, url, newParams));
	}

	/**
	 * @param url
	 * @param handler
	 */
	protected void gPutUrl(String url, AsyncHttpResponseHandler handler) {
		gPutUrl(url, null, handler);
	}

	/**
	 * @param url
	 * @param requestParams
	 * @param handler
	 */
	protected void gPutUrl(String url, RequestParams requestParams, AsyncHttpResponseHandler handler) {
		RequestParams newParams = getDESRequestParams(url, requestParams);
		client.put(url, newParams, handler);
		DebugLog.d(TAG, AsyncHttpClient.getUrlWithQueryString(true, url, newParams));
	}

	/**
	 * delete 请求
	 *
	 * @param url
	 * @param requestParams 加密过Params
	 * @param handler
	 */
	protected void gDeleteUrl(String url, RequestParams requestParams, AsyncHttpResponseHandler handler) {
		RequestParams newParams = getDESRequestParams(url, requestParams);
		client.delete(null, url, null, newParams, handler);
		DebugLog.d(TAG, AsyncHttpClient.getUrlWithQueryString(true, url, newParams));
	}


	/**
	 * @param context
	 * @param url
	 * @param handler
	 */
	protected void gDeleteUrl(Context context, String url, AsyncHttpResponseHandler handler) {
		gDeleteUrl(context, url, null, handler);
	}


	/**
	 * @param context
	 * @param url
	 * @param requestParams
	 * @param handler
	 */
	protected void gDeleteUrl(Context context, String url, RequestParams requestParams, AsyncHttpResponseHandler handler) {
		RequestParams newParams = getDESRequestParams(url, requestParams);
		client.delete(context, url, null, newParams, handler);
		DebugLog.d(TAG, AsyncHttpClient.getUrlWithQueryString(true, url, newParams));
	}
}
