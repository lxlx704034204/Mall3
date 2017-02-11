package com.hxqc.mall.reactnative.nativemodule;

import android.content.Context;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.bzinga.DES3;
import com.hxqc.util.DebugLog;

import java.net.URLEncoder;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Author: wanghao
 * Date: 2016-03-08
 * FIXME
 * Todo
 */
public class SecurityJSJavaModule extends ReactContextBaseJavaModule {

    String prefixUrlHost = "";
    Context ctx;

    public SecurityJSJavaModule(ReactApplicationContext context) {
        super(context);
        ctx = context;
    }


    @Override
    public String getName() {
        return "SecurityModule";
    }

    @Nullable
    @Override
    public Map< String, Object > getConstants() {
        return super.getConstants();
    }

    //二手车竞拍 分环境
    @ReactMethod
    public void getUsedcarAuction(Callback successCallback, Callback errorCallback){
        prefixUrlHost = ApiUtil.getUsedCarAuctionHostURL();
        try {
            DebugLog.i("security", "getUsedcarAuction host: "+prefixUrlHost);
            successCallback.invoke(prefixUrlHost);
        } catch (Exception e) {
            e.printStackTrace();
            errorCallback.invoke(e.getMessage());
        }
    }

    //获取请求参数首页 加密并回调给js请求数据-----home
    @ReactMethod
    public void getEncodeThings(String response, Callback successCallback, Callback errorCallback) {
        prefixUrlHost = ApiUtil.getMallUrl();
        try {
            DebugLog.i("security", "host: "+prefixUrlHost+" getEncodeThings normal: " + response);

            if (!ApiUtil.isDebug){
                response = "p="+URLEncoder.encode(DES3.encode(response),"UTF-8");
            }

            DebugLog.d("security", "host: "+prefixUrlHost+" getEncodeThings encode: " + response);

            successCallback.invoke(prefixUrlHost, response);
        } catch (Exception e) {
            e.printStackTrace();
            errorCallback.invoke(e.getMessage());
        }
    }

    //获取请求参数首页 加密并回调给js请求数据-----周边
    @ReactMethod
    public void getEncodeThingsService(String response, Callback successCallback, Callback errorCallback) {
        prefixUrlHost = ApiUtil.getAroundURL();
        try {
            DebugLog.i("security", "host: "+prefixUrlHost+" getEncodeThings normal: " + response);

            if (!ApiUtil.isDebug){
                response = "p="+URLEncoder.encode(DES3.encode(response),"UTF-8");
            }

            DebugLog.d("security", "host: "+prefixUrlHost+" getEncodeThings encode: " + response);

            successCallback.invoke(prefixUrlHost, response);
        } catch (Exception e) {
            e.printStackTrace();
            errorCallback.invoke(e.getMessage());
        }
    }

    //获取请求参数首页 加密并回调给js请求数据-----会员中心
    @ReactMethod
    public void getEncodeThingsAccount(String response, Callback successCallback, Callback errorCallback) {
        prefixUrlHost = ApiUtil.getAccountURL();
        try {
            DebugLog.i("security", "host: "+prefixUrlHost+" getEncodeThings normal: " + response);

            if (!ApiUtil.isDebug){
                response = "p="+URLEncoder.encode(DES3.encode(response),"UTF-8");
            }

            DebugLog.d("security", "host: "+prefixUrlHost+" getEncodeThings encode: " + response);

            successCallback.invoke(prefixUrlHost, response);
        } catch (Exception e) {
            e.printStackTrace();
            errorCallback.invoke(e.getMessage());
        }
    }

    //获取加密数据   解密并回调给js
    @ReactMethod
    public void getDecodeThings(String request, Callback successCallback, Callback errorCallback) {

        try {
            DebugLog.i("security", "getDecodeThings normal: " + request);
            String response = DES3.decode(request);
            DebugLog.d("security", "getDecodeThings encode: " + response);
            successCallback.invoke(response);
        } catch (Exception e) {
            e.printStackTrace();
            errorCallback.invoke(e.getMessage());
        }

    }


}
