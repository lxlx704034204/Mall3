package com.hxqc.mall.thirdshop.maintenance.api;

import android.content.Context;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年10月08日
 */
public class EmergencyRescueApi extends BaseApiClient {
    private final static String TAG = EmergencyRescueApi.class.getSimpleName();
    private Context mContext;


    @Override
    protected String completeUrl(String control) {
        return ApiUtil.getAroundURL(control);
    }


    /**
     * 发送当前位置
     *
     * @param phone   电话
     * @param pro     省
     * @param city    市
     * @param area    区
     * @param info    街道
     * @param address 地址
     * @param lng     经度
     * @param lat     维度
     * @param handler
     */
    public void sendPos(String phone, String pro, String city, String area, String info, String address, double lng, double lat, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/HelpCenter/getAppHelpInfo");
        RequestParams requestParams = new RequestParams();
        requestParams.put("phone", phone);
        requestParams.put("pro", pro);
        requestParams.put("city", city);
        requestParams.put("area", area);
        requestParams.put("info", info);
        requestParams.put("address", address);
        requestParams.put("lng", lng);
        requestParams.put("lat", lat);
        gPostUrl(url, requestParams, handler);
    }
}
