package com.hxqc.newenergy.api;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.hxqc.mall.config.application.SampleApplicationContext;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.newenergy.util.EVSharePreferencesHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;

/**
 * Author:胡俊杰
 * e-mail:hujj@corp.hxqc.com
 * company:恒信汽车电子商务有限公司.
 * Date: 2016/3/9
 * FIXME
 * Todo
 */
public class NewEnergyApiClient extends BaseApiClient {


    @Override
    protected String completeUrl(String control) {
//        return HOST + "/Ecar/" + API_VERSION + control;
        return ApiUtil.getEcarURL(control);
    }


    /**
     * 获取爆款推荐数据
     */
    public void getRecommendData(String are, String Lat, String Lot, int count, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Home/hotRecommend");
        RequestParams requestParams = new RequestParams();
//        requestParams.put("deviceType ", "Android");
        requestParams.put("area", are);
        requestParams.put("latitude", Lat);
        requestParams.put("longitude", Lot);
        requestParams.put("count", count);
        gGetUrl(url, requestParams, handler);


    }

    /**
     * 获取爆款推荐列表数据
     */
    public void getRecommendListData(String are, String Lat, String Lot, int count, int page, String recommendType, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Home/hotRecommendList");
        RequestParams requestParams = new RequestParams();
        requestParams.put("area", are);
        requestParams.put("latitude", Lat);
        requestParams.put("longitude", Lot);
        requestParams.put("count", count);
        requestParams.put("page", page);
        requestParams.put("recommendType", recommendType);
        gGetUrl(url, requestParams, handler);


    }


    /**
     * 获取特卖列表
     *
     * @param itemCategory
     * @param handler
     */
    public void getSpecialOffer(String area, int itemCategory, int Count, int page, AsyncHttpResponseHandler handler) {
        String url = ApiUtil.getMallUrl("/Seckill");
        RequestParams requestParams = new RequestParams();
        requestParams.put("itemCategory", itemCategory);
        requestParams.put("area", area);
        requestParams.put("page", page);
        requestParams.put("count", Count);
        switch (itemCategory) {
            case AutoItem.CATEGORY_AUTOMOBILE:
                break;
            case AutoItem.CATEGORY_NEW_ENERGY:
                requestParams.put("area", EVSharePreferencesHelper.getLastHistoryCity(SampleApplicationContext.application));
                break;
        }
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取特卖列表
     *
     * @param itemCategory
     * @param handler
     */
    public void getDealsInfo(String area, int itemCategory, int Count, int page, AsyncHttpResponseHandler handler) {
        String url = ApiUtil.getEcarURL("/Home/Seckill");
        RequestParams requestParams = new RequestParams();
        requestParams.put("itemCategory", itemCategory);
        requestParams.put("area", area);
        requestParams.put("page", page);
        requestParams.put("count", Count);
        switch (itemCategory) {
            case AutoItem.CATEGORY_AUTOMOBILE:
                break;
            case AutoItem.CATEGORY_NEW_ENERGY:
                requestParams.put("area", EVSharePreferencesHelper.getLastHistoryCity(SampleApplicationContext.application));
                break;
        }
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取最新资讯数据数据
     */
    public void getNewsList(String are, String Lat, String Lot, int count, int page, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/News/newsList");
        RequestParams requestParams = new RequestParams();
        requestParams.put("deviceType ", "Android");
        requestParams.put("area", are);
        requestParams.put("latitude", Lat);
        requestParams.put("longitude", Lot);
        requestParams.put("count", count);
        requestParams.put("page", page);
        gGetUrl(url, requestParams, handler);

    }


    /**
     * 获取最新排行榜数据
     */
    public void getRankingData(String are, int limit, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Home/Ranking");
        RequestParams requestParams = new RequestParams();
        requestParams.put("area", are);
        requestParams.put("deviceType ", "Android");
        requestParams.put("limit", limit);
        gGetUrl(url, requestParams, handler);

    }

    /**
     * 获取补贴详情数据
     */
    public void getSubsidyDetailData(String are, String autoID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Subsidy/detail");
        RequestParams requestParams = new RequestParams();
        requestParams.put("area", are);
        requestParams.put("autoID", autoID);
        gGetUrl(url, requestParams, handler);

    }


    /**
     * 获取最新补贴数据
     */
    public void getnewFilter(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Subsidy/filter");
        RequestParams requestParams = new RequestParams();

        requestParams.put("deviceType ", "Android");
        gGetUrl(url, requestParams, handler);

    }


    /**
     * 获取最新百科数据
     */
    public void getWikiData(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Wiki");
        RequestParams requestParams = new RequestParams();

        requestParams.put("deviceType ", "Android");
        gGetUrl(url, requestParams, handler);

    }


    /**
     * 首页上半部分 活动推荐
     */
    public void getBannerData(String aree, String Lat, String Lot, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("area", aree);
        requestParams.put("latitude", Lat);
        requestParams.put("longitude", Lot);
        gGetUrl(completeUrl("/Home/banner"), requestParams, handler);
    }


    /**
     * 车型及补贴资料数据
     */
    public void getModeAndSubsidyData(int page, int count, HashMap< String, String > filterCondition, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Subsidy");
        RequestParams requestParams = new RequestParams(filterCondition);
        requestParams.put("page", page);
        requestParams.put("count", count);
        gGetUrl(url, requestParams, handler);
    }


    /** 获取补贴资料筛选条件 */
    public void getFilterData(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Subsidy/filter");
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);
    }


    /**
     * 获取地区数据
     */
    public void getAreaData(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Area");
        RequestParams requestParams = new RequestParams();
        gGetUrl(url, requestParams, handler);
    }

    /**
     * 获取新能源百科
     * @return
     */
    public String getWiki() {
        return completeUrl("/Html/wikiIndex");

    }
}
