package com.hxqc.mall.api;

import android.content.Context;

import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.newenergy.util.EVSharePreferencesHelper;
import com.hxqc.mall.config.application.SampleApplicationContext;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Map;

/**
 * Author: HuJunJie
 * Date: 2015-03-24
 * FIXME
 * Todo
 */
public class NewAutoClient extends ApiClient {

    /**
     * 获取品牌列表
     *
     * @param handler brandType:
     *                品牌类型：10.汽车品牌（全部），20.新能源品牌，30.全部
     */
    public void homeBrandList(AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams("brandType", 30);
        gGetUrl(completeUrl("/Home/brandList"), requestParams, handler);
    }

    /**
     * 获取品牌列表
     *
     * @param handler
     */
    public void autoBrandList(int itemCategory, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams(AutoItem.ItemCategory, itemCategory);
        switch (itemCategory) {
            case AutoItem.CATEGORY_AUTOMOBILE:
                break;
            case AutoItem.CATEGORY_NEW_ENERGY:
                requestParams.put("area", EVSharePreferencesHelper.getLastHistoryCity(SampleApplicationContext.application));
                break;
        }
        gGetUrl(completeUrl("/Auto/brandList"), requestParams, handler);
    }


    /**
     * 获取车系
     *
     * @param handler
     */
    public void autoSeries(String brandID, String brandInitial, int itemCategory, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams("brandID", brandID);
        requestParams.put("brandInitial", brandInitial);
        requestParams.put("itemCategory", itemCategory);
        switch (itemCategory) {
            case AutoItem.CATEGORY_AUTOMOBILE:
                break;
            case AutoItem.CATEGORY_NEW_ENERGY:
                requestParams.put("area", EVSharePreferencesHelper.getLastHistoryCity(SampleApplicationContext.application));
                break;
        }
        gGetUrl(completeUrl("/Auto/series"), requestParams, handler);
    }

    /**
     * 筛选条件
     *
     * @param handler
     */
    public void filterFactor(String itemCategory, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams("itemCategory", itemCategory);
        gGetUrl(completeUrl("/Auto/filter"), requestParams, handler);
    }

    /**
     * 热门搜索
     *
     * @param handler
     */
    public void searchHotKetWord(AsyncHttpResponseHandler handler) {
        gGetUrl(completeUrl("/Search/hotKeywords"), handler);
    }

    /**
     * 搜索提示
     *
     * @param keyword
     * @param handler
     */
    public void searchHint(String keyword, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams("keyword", keyword);
        gGetUrl(completeUrl("/Search/hint"), params, handler);
    }

    /**
     * 搜索提示V2
     * liaoguilong
     *
     * @param keyword
     * @param handler
     */
    public void searchHintV2(String keyword, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams("keyword", keyword);
        gGetUrl(completeUrl("/Search/hintV2"), params, handler);
    }


    public void filterExamine(String itemCategory, Map<String, String> searchMap, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams(searchMap);
        params.put("itemCategory", itemCategory);
        gGetUrl(completeUrl("/Auto/filterExamine"), params, handler);
    }

    /**
     * 搜索列表
     *
     * @param searchMap
     * @param page
     * @param handler
     */
    public void search(Context context, Map<String, String> searchMap,
                       int page, int pageItemCount, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams(searchMap);
        params.put("page", page);
//        params.put("token", sharePreHelp.getToken());
        params.put("count", pageItemCount);
        gGetUrl(completeUrl("/Search"), params, handler);
    }

    /**
     * 获取特卖列表
     */
    public void getSpecialOffer(int itemCategory, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Seckill");
        RequestParams requestParams = new RequestParams();
        requestParams.put("itemCategory", itemCategory);
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
     * 汽车详情
     *
     * @param itemID
     * @param itemType
     * @param handler
     */
    public void autoItemDetail(Context context, String itemID, String itemType, int itemCategory, AsyncHttpResponseHandler handler) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("itemID", itemID);
        requestParams.put("itemType", itemType);
//        switch (itemCategory) {
//            case AutoItem.CATEGORY_AUTOMOBILE:
//                break;
//            case AutoItem.CATEGORY_NEW_ENERGY:
//
//                break;
//        }
        requestParams.put("area", EVSharePreferencesHelper.getLastHistoryCity(SampleApplicationContext.application));
        gGetUrl(completeUrl("/Auto/itemDetail"), requestParams, handler);
    }

    /**
     * 参数
     *
     * @param itemID
     * @param handler
     */
    public void itemParameter(boolean isExtID, String itemID, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        if (isExtID) {
            params.put("extID", itemID);
        } else
            params.put("itemID", itemID);
        gGetUrl(completeUrl("/Auto/itemParameter"), params, handler);
    }

    /**
     * 图集
     */
    public void autoItemAtlas(String itemID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Auto/pictures");
        RequestParams params = new RequestParams();
        params.put("itemID", itemID);
        gGetUrl(url, params, handler);
    }


    /**
     * 首页上半部分 活动推荐
     */
    public void getEventAD(AsyncHttpResponseHandler handler) {
        gGetUrl(completeUrl("/Home/slideshow"), handler);
    }

    /**
     * 到货通知
     *
     * @param itemID
     * @param cellphone
     * @param email
     * @param handler
     */
    public void arrival(String itemID, String cellphone, String email, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Subscription/arrival");
        RequestParams params = new RequestParams();
        params.put("itemID", itemID);
        params.put("cellphone", cellphone);
        params.put("email", email);
        gPostUrl(url, params, handler);
    }

    /**
     * 降价通知
     *
     * @param itemID
     * @param cellphone
     * @param email
     * @param handler
     */
    public void priceCut(String itemID, String cellphone, String email, String price, AsyncHttpResponseHandler
            handler) {
        String url = completeUrl("/Subscription/priceCut");
        RequestParams params = new RequestParams();
        params.put("itemID", itemID);
        params.put("price", price);
        params.put("cellphone", cellphone);
        params.put("email", email);
        gPostUrl(url, params, handler);
    }

    /**
     * 欲购买
     *
     * @param itemID
     * @param handler
     */
    public void readyPurchase(Context context, String itemID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Order/readyPurchase");
        RequestParams params = new RequestParams();
        params.put("itemID", itemID);
//        params.put("token", sharePreHelp.getToken());
        gGetUrl(url, params, handler);
    }

    /**
     * 用品列表
     *
     * @param extID
     * @param handler
     */
    public void packageList(String extID, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Auto/package");
        RequestParams params = new RequestParams();
        params.put("extID", extID);
        gGetUrl(url, params, handler);
    }

    /**
     * 金融公司
     */
    public void financeList(AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Finance");
        gGetUrl(url, handler);
    }


}
