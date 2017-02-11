package com.hxqc.tinker.api;

import android.util.Log;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.hxqc.tinker.util.TinkerAutoFixStartUtil;
import com.hxqc.util.DebugLog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import hxqc.mall.BuildConfig;

/**
 * Author:  wh
 * Date:  2016/12/29
 * FIXME
 * Todo
 */

public class TinkerApiClient extends BaseApiClient {

    /**
     * 获取 app更新补丁
     * @param handler    获取补丁回调
     * TODO   打包时注释log
     */
    public  void fetchForAppPatch(AsyncHttpResponseHandler handler){
        client.addHeader("accept", "*/*");
        RequestParams requestParams = new RequestParams();
        requestParams.put("version", BuildConfig.VERSION_NAME);
//        requestParams.put("appName", "恒信汽车");
        requestParams.put("appKey", ApiUtil.ConfigAppKey);
        requestParams.put("os", "Android");
        DebugLog.i(TinkerAutoFixStartUtil.TFS_Tag, " appVersion: " + BuildConfig.VERSION_NAME + " appKey: "+ ApiUtil.ConfigAppKey);
        Log.i(TinkerAutoFixStartUtil.TFS_Tag, " appVersion: " + BuildConfig.VERSION_NAME);
        gGetUrl(completeUrl("/apis/release/patch"), requestParams, handler);
    }

    @Override
    protected String completeUrl(String control) {
        return ApiUtil.getTinkerURLHost() + control;
    }
}
