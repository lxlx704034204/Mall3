package com.hxqc.mall.thirdshop.maintenance.control;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.auto.api.AutoTypeClient;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.interfaces.LoadDataCallBack;
import com.hxqc.mall.thirdshop.maintenance.model.CarSeriesModel;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.Model;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Author:李烽
 * Date:2016-03-17
 * FIXME
 * Todo 选择车辆
 */
public class SelectAutoHelper {
    private static SelectAutoHelper instance;
    private AutoTypeClient client;
    private Context context;

    private SelectAutoHelper(Context context) {
        client = new AutoTypeClient();
        this.context = context.getApplicationContext();
    }

    public synchronized static SelectAutoHelper getInstance(Context context) {
        if (instance == null)
            synchronized (SelectAutoHelper.class) {
                if (instance == null)
                    instance = new SelectAutoHelper(context);
            }
        return instance;
    }

    /**
     * 销毁
     */
    public void destory() {
        if (instance != null)
            instance = null;
    }

    /**
     * 获取品牌组
     *
     * @param shopID
     * @param callBack
     */
    public void getBrandData(String shopID, final LoadDataCallBack<ArrayList<BrandGroup>> callBack) {
        client.autoBrand(shopID, new BaseMallJsonHttpResponseHandler(context) {
            @Override
            public void onSuccess(String response) {
                ArrayList<BrandGroup> brandGroups = JSONUtils.fromJson(response, new TypeToken<ArrayList<BrandGroup>>() {
                });
                if (brandGroups != null)
                    callBack.onDataGot(brandGroups);
                else callBack.onDataNull("");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callBack.onDataNull("");
            }
        });
    }

    /**
     * 获取车系
     *
     * @param brand
     * @param callBack
     */
    public void getSeriesData(String shopID, String brand, String brandID, final LoadDataCallBack<ArrayList<CarSeriesModel>> callBack) {
        client.autoSeries(shopID, brand, brandID, new BaseMallJsonHttpResponseHandler(context) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callBack.onDataNull("");
            }

            @Override
            public void onSuccess(String responseString) {
                ArrayList<CarSeriesModel> brands = JSONUtils.fromJson(responseString,
                        new TypeToken<ArrayList<CarSeriesModel>>() {
                        });
                if (brands != null)
                    callBack.onDataGot(brands);
                else callBack.onDataNull("");
            }
        });
    }

    /**
     * 获取车型
     *
     * @param brand
     * @param series
     * @param callBack
     */
    public void getModelData(String shopID, String brand, String brandID, String series, String seriesID, final LoadDataCallBack<ArrayList<Model>> callBack) {
        client.autoModelN(shopID, brand, brandID, series, seriesID, new BaseMallJsonHttpResponseHandler(context) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callBack.onDataNull("");
            }

            @Override
            public void onSuccess(String responseString) {
                ArrayList<Model> models = JSONUtils.fromJson(responseString, new TypeToken<ArrayList<Model>>() {
                });
                if (models != null)
                    callBack.onDataGot(models);
                else callBack.onDataNull("");
            }
        });
    }

}
