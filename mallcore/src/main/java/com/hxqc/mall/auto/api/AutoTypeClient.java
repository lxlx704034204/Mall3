package com.hxqc.mall.auto.api;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Author: 胡仲俊
 * Date: 2015-03-17
 * FIXME
 * Todo 车辆品牌车系车型
 */
public class AutoTypeClient extends BaseApiClient {


    public AutoTypeClient() {
        super();
    }

    @Override
    protected String completeUrl(String control) {
//        return HOST + "/maintain/" + API_VERSION + control;
        return ApiUtil.getMaintainURL(control);
    }

    /**
     * 获取品牌
     *
     * @param shopID
     * @param handler
     */
    public void autoBrand(String shopID, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
//        requestParams.put("deviceType", "Android");
        requestParams.put("shopID", shopID);
        gGetUrl(completeUrl("/Auto/brands"), requestParams, handler);
    }


    /**
     * 获取车系
     *
     * @param brandID
     * @param handler
     */
    @Deprecated
    public void autoSeries(String brandID, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
//        requestParams.put("deviceType", "Android");
        requestParams.put("brandID", brandID);
        gGetUrl(completeUrl("/Auto/seriess"), requestParams, handler);
    }

    /**
     * 获取车系
     *
     * @param shopID
     * @param brand
     * @param handler
     */
    @Deprecated
    public void autoSeries(String shopID, String brand, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
//        requestParams.put("deviceType", "Android");
        requestParams.put("shopID", shopID);
        requestParams.put("brand", brand);
        gGetUrl(completeUrl("/Auto/seriess"), requestParams, handler);
    }

    /**
     * 获取车系
     *
     * @param shopID
     * @param brand
     * @param brandID
     * @param handler
     */
    public void autoSeries(String shopID, String brand, String brandID, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
//        requestParams.put("deviceType", "Android");
        requestParams.put("shopID", shopID);
        requestParams.put("brand", brand);
        requestParams.put("brandID", brandID);
        gGetUrl(completeUrl("/Auto/seriess"), requestParams, handler);
    }

    /**
     * 获取车型
     *
     * @param brandID
     * @param serieID
     */
    @Deprecated
    public void autoModel(String brandID, String serieID, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
//        requestParams.put("deviceType", "Android");
        requestParams.put("brandID", brandID);
        requestParams.put("seriesID", serieID);
        gGetUrl(completeUrl("/Auto/model"), requestParams, handler);
    }

    /**
     * 获取车型
     *
     * @param shopID
     * @param brand
     * @param series
     */
    @Deprecated
    public void autoModel(String shopID, String brand, String series, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
//        requestParams.put("deviceType", "Android");
        requestParams.put("shopID", shopID);
        requestParams.put("brand", brand);
        requestParams.put("series", series);
        gGetUrl(completeUrl("/Auto/model"), requestParams, handler);
    }

    /**
     * 获取车型
     *
     * @param shopID
     * @param brand
     * @param brandID
     * @param series
     * @param seriesID
     */
    @Deprecated
    public void autoModel(String shopID, String brand, String brandID, String series, String seriesID, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
//        requestParams.put("deviceType", "Android");
        requestParams.put("shopID", shopID);
        requestParams.put("brand", brand);
        requestParams.put("brandID", brandID);
        requestParams.put("series", series);
        requestParams.put("seriesID", seriesID);
        gGetUrl(completeUrl("/Auto/model"), requestParams, handler);
    }

    /**
     * 获取车型
     *
     * @param shopID
     * @param brand
     * @param brandID
     * @param series
     * @param seriesID
     */
    public void autoModelN(String shopID, String brand, String brandID, String series, String seriesID, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
//        requestParams.put("deviceType", "Android");
        requestParams.put("shopID", shopID);
        requestParams.put("brand", brand);
        requestParams.put("brandID", brandID);
        requestParams.put("series", series);
        requestParams.put("seriesID", seriesID);
        gGetUrl(completeUrl("/Auto/modelN"), requestParams, handler);
    }

}
