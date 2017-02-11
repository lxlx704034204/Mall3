package com.hxqc.mall.auto.api;

import android.support.annotation.NonNull;

import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.hxqc.util.DebugLog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 14
 * FIXME
 * Todo 车辆信息联网操作
 */
public class AutoInfoClient extends BaseApiClient {


    @Override
    protected String completeUrl(String control) {
//        return HOST + "/Account/" + API_VERSION + control;
        return ApiUtil.getAccountURL(control);
    }


    /**
     * 请求车辆信息
     *
     * @param handler
     */
    public void requestAutoInfo(AsyncHttpResponseHandler handler) {
        gGetUrl(completeUrl("/MyAuto"), handler);
    }

    /**
     * 修改车辆信息
     *
     * @param handler
     */
    public void editAutoInfo(MyAuto myAuto, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("myAutoID", myAuto.myAutoID);
//        requestParams.put("plateNumber", myAuto.plateNumber);
        requestParams.put("drivingDistance", myAuto.drivingDistance);
//        requestParams.put("ownPhone", myAuto.ownPhone);
//        requestParams.put("ownName", myAuto.ownName);
//        requestParams.put("isDefault", myAuto.isDefault);
//        requestParams.put("registerTime", myAuto.registerTime);
        requestParams.put("brandID", myAuto.brandID);
        requestParams.put("brandName", myAuto.brandName);
        requestParams.put("seriesID", myAuto.seriesID);
        requestParams.put("modelID", myAuto.autoModelID);
        requestParams.put("brand", myAuto.brand);
        requestParams.put("series", myAuto.series);
        requestParams.put("model", myAuto.autoModel);
        requestParams.put("plateNumber", myAuto.plateNumber);
//        gPutUrl(completeUrl("/MyAuto"), requestParams, handler);
        gPostUrl(completeUrl("/MyAuto"), requestParams, handler);
    }

    /**
     * 添加车辆
     *
     * @param handler
     */
    public void addAutoInfo(MyAuto auto, AsyncHttpResponseHandler handler) {
        addAutoInfo(auto.myAutoID, auto.drivingDistance, auto.brand, auto.brandName, auto.brandID
                , auto.series, auto.seriesID, auto.autoModel, auto.autoModelID
                , auto.plateNumber, handler);
    }

    /**
     * 增加车辆信息
     *
     * @param drivingDistance（可选）
     * @param brand
     * @param brandID
     * @param series
     * @param seriesID
     * @param model
     * @param modelID
     * @param plateNumber
     * @param handler
     */
    public void addAutoInfo(String myAutoID, String drivingDistance, String brand, String brandName, String brandID, String series
            , String seriesID, String model, String modelID, String plateNumber
            , AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
//		requestParams.put("VIN", VIN);
        requestParams.put("drivingDistance", drivingDistance);
        requestParams.put("brand", brand);
        requestParams.put("brandName", brandName);
        requestParams.put("brandID", brandID);
        requestParams.put("series", series);
        requestParams.put("seriesID", seriesID);
        requestParams.put("model", model);
        requestParams.put("modelID", modelID);
        requestParams.put("plateNumber", plateNumber);
        requestParams.put("myAutoID", myAutoID);
//		requestParams.put("ownPhone", ownPhone);
//		requestParams.put("ownName", ownName);
//		requestParams.put("registerTime", registerTime);
        gPostUrl(completeUrl("/MyAuto"), requestParams, handler);
    }

    public static int CONNECTION_TIMEOUT = 2 * 60 * 1000;
    public static int SOCKET_TIMEOUT = 2 * 60 * 1000;
    private AsyncHttpClient asyncHttpClient;

    /**
     * 删除车辆
     *
     * @param myAutoID
     * @param handler
     */
    public void deleteAutoInfo(String myAutoID, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("myAutoID", myAutoID);
//        RequestParams newParams = getDESRequestParams(completeUrl("/MyAuto"), requestParams);
//        asyncHttpClient = new AsyncHttpClient(true, 0, 0);
//        asyncHttpClient.setUserAgent(UserAgent);
//        asyncHttpClient.setConnectTimeout(CONNECTION_TIMEOUT);
//        asyncHttpClient.setTimeout(SOCKET_TIMEOUT);
//        asyncHttpClient.delete(null, completeUrl("/MyAuto"), null, newParams, handler);
        gDeleteUrl(completeUrl("/MyAuto"), requestParams, handler);
    }

    /**
     * 检查车辆
     *
     * @param VIN
     * @param plateNumber
     * @param handler
     */
    @Deprecated
    public void checkAutoInfo(String VIN, String plateNumber, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("plateNumber", plateNumber);
        requestParams.put("VIN", VIN);
        gGetUrl(completeUrl("/MyAuto/checkAuto"), requestParams, handler);
    }

    /**
     * 保养记录
     *
     * @return handler
     */
    public void getMaintenanceRecord(@NonNull String myAutoID, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
//        requestParams.put("plateNumber", plateNumber);
        requestParams.put("myAutoID", myAutoID);
        gGetUrl(completeUrl("/MyAuto/maintenancRecord"), requestParams, handler);
    }


    /**
     * 优惠卷规则
     *
     * @return
     */
    public String couponRules() {
        DebugLog.i(TAG, completeUrl("/Html/couponRules"));
        return completeUrl("/Html/couponRules");
    }

}
