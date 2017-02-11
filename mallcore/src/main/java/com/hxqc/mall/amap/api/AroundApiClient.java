package com.hxqc.mall.amap.api;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Author: wanghao
 * Date: 2016-04-08
 * FIXME
 * Todo
 */
public class AroundApiClient extends BaseApiClient {
//
//    /**
//     * 查询周边 加油站信息  请求外部 不加密get方式
//     * @param lon 经度 116.403119
//     * @param lat 纬度 39.916042
//     * @param r 半径 3000
//     * @param page 查询页数
//     * @param handler 回调
//     */
//    public void getGasStationInfo(String lon, String lat, String r, String page, AsyncHttpResponseHandler handler) {
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("lon", lon);
//        requestParams.put("lat", lat);
//        requestParams.put("r", "1000");
//        requestParams.put("page", page);
//        String gasSearchKey = "e65d5652da974ef7a3702d753598b6b4";
//        requestParams.put("key", gasSearchKey);
//        /*
//      查询油价 加油站信息  url
//     */
//        String gasStationSearchURL = "http://apis.haoservice.com/oil/local";
//        doGetUrl(gasStationSearchURL, requestParams, handler);
//    }

    @Override
    protected String completeUrl(String control) {
        //http://10.0.15.201:8089/Service/V1/Service/oil
        return ApiUtil.getAroundURL(control);
//        return HOST + "/Service/" + API_VERSION + control;
    }

    /**
     * 查询周边 加油站信息  请求内部  不加密get方式
     * @param lon 经度 116.403119
     * @param lat 纬度 39.916042
     * @param r 半径 3000
     * @param page 查询页数
     * @param handler 回调
     */
    public void getGasStationInfoInner(String lon, String lat, String r, String page, AsyncHttpResponseHandler handler){
        String url = completeUrl("/Service/oil");
        RequestParams requestParams = new RequestParams();
        requestParams.put("lon", lon);
        requestParams.put("lat", lat);
        requestParams.put("r", "1000");
        requestParams.put("page", page);
        gGetUrl(url,requestParams,handler);
    }


    /**
     * 查询周边 加油站信息  请求内部  不加密get方式
     * @param lon 经度 116.403119
     * @param lat 纬度 39.916042
     * @param r 半径 3000
     * @param page 查询页数
     * @param handler 回调
     */
    public void getGasStationInfoInner(String lat, String lon, int r, int page, AsyncHttpResponseHandler handler){
        String url = completeUrl("/Service/oil");
//        String url = "http://app-interface.t.dasqctest.com//Service/V2/Service/oil";
        RequestParams requestParams = new RequestParams();
        requestParams.put("lat", lat);
        requestParams.put("lon", lon);
        requestParams.put("r", r);
        requestParams.put("page", page);
        gGetUrl(url,requestParams,handler);
    }

    /**
     * 查询内部已有的 停车场数据
     * @param latitude 纬度
     * @param longitude 经度
     * @param handler 回调
     */
    public void getParkInfoInner(String latitude,String longitude,AsyncHttpResponseHandler handler){
        String url = completeUrl("/Service/park");
        RequestParams requestParams = new RequestParams();
        requestParams.put("latitude", latitude);
        requestParams.put("longitude", longitude);
        gGetUrl(url,requestParams,handler);
    }

    /**
     * 查询充电站接口
     * @param province 省
     * @param city 市
     * @param region 区
     * @param latitude 纬度
     * @param longitude 经度
     * @param handler 回调
     */
    public void getChargeStationInfoInner(String province,String city,String region,String latitude,String longitude,AsyncHttpResponseHandler handler){
        String url = completeUrl("/Service/station");
        RequestParams requestParams = new RequestParams();
        requestParams.put("province", province);
        requestParams.put("city", city);
        requestParams.put("region", region);
        requestParams.put("type", "公共站");
//        requestParams.put("opState", opState);
        requestParams.put("latitude", latitude);
        requestParams.put("longitude", longitude);
        gGetUrl(url,requestParams,handler);
    }

    /**
     * 查询天气信息
     * @param cityname  城市名称
     * @param handler  回调
     */
    public void getWeatherInfoInner(String cityname,AsyncHttpResponseHandler handler){
        String url = completeUrl("/Service/weather");
//        url = "http://10.0.15.203:8089/Account/V2/Weather";
        RequestParams requestParams = new RequestParams();
        requestParams.put("cityname", cityname);
        gGetUrl(url,requestParams,handler);
    }
}
