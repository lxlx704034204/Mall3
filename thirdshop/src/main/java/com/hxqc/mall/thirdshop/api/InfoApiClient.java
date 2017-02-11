package com.hxqc.mall.thirdshop.api;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 资讯信息 API接口
 * Created by zhaofan on 2016/11/1.
 */
public class InfoApiClient extends BaseApiClient {

    @Override
    protected String completeUrl(String control) {
        return ApiUtil.getAutoInfoURL(control);
    }


    /**
     * 获取筛选品牌
     *
     * @param handler
     */
    public void requestFilterBrand(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Filter/brand");
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取筛选车系
     *
     * @param handler
     */
    public void requestFilterSeries(String brandName, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Filter/series");
        RequestParams requestParams = new RequestParams();
        requestParams.put("brand", brandName);
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 获取筛选车型
     *
     * @param handler
     */
    public void requestFilterModel(String brandName, String serieName, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Filter/model");
        RequestParams requestParams = new RequestParams();
        requestParams.put("brand", brandName);
        requestParams.put("serie", serieName);
        gGetUrl(url, requestParams, handler);
    }




    /**
     * 车型车系评分列表
     */
    public void getAutoGrade(String extID, String brand, String series, int page, int count, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/AutoGrade");
        RequestParams requestParams = new RequestParams();
        requestParams.put("extID", extID);
        requestParams.put("brand", brand);
        requestParams.put("series", series);
        requestParams.put("page", page);
        requestParams.put("count", count);
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 车型车系 资讯列表
     */
    public void getAutoNews(String extID, String brand, String series, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Index/autoInfoRelevant");
        RequestParams requestParams = new RequestParams();
    //    requestParams.put("extID", extID);
        requestParams.put("brand", brand);
        requestParams.put("series", series);
        gGetUrl(url, requestParams, handler);
    }


}
