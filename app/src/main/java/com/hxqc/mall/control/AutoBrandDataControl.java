package com.hxqc.mall.control;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.auto.AutoListActivity;
import com.hxqc.mall.api.NewAutoClient;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.model.AutoSeriesGroup;
import com.hxqc.mall.core.model.Brand;
import com.hxqc.mall.core.model.BrandGroup;
import com.hxqc.mall.core.model.Series;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Author: HuJunJie
 * Date: 2015-03-12
 * FIXME
 * Todo  品牌数据内容
 */
public class AutoBrandDataControl {
    private static AutoBrandDataControl ourInstance = null;
    protected ArrayList< AutoSeriesGroup > mSeriesGroups;//车系列表
    NewAutoClient client;
    SparseArray< ArrayList< BrandGroup > > mBrandGroup = new SparseArray<>();
    Brand tBrand = null;//当前品牌
    int itemCategory = 10;//类型

    private AutoBrandDataControl() {
        client = new NewAutoClient();
    }

    public static AutoBrandDataControl getInstance() {
        if (ourInstance == null) {
            synchronized (AutoBrandDataControl.class) {
                if (ourInstance == null) {
                    ourInstance = new AutoBrandDataControl();
                }
            }
        }
        return ourInstance;
    }

    public ArrayList< BrandGroup > getBrandGroups(int itemCategory) {
        return mBrandGroup.get(itemCategory);
    }

    public void getBrandGroups(Context context, final int itemCategory, final BrandHandler brandHandler) {
        if (getBrandGroups(itemCategory) != null) {
            //存在品牌列表
            brandHandler.onSucceed(getBrandGroups(itemCategory));
            return;
        }
        client.autoBrandList(itemCategory, new LoadingAnimResponseHandler(context) {
            @Override
            public void onSuccess(String response) {

                ArrayList< BrandGroup > tBrandsGroups = JSONUtils.fromJson(response,
                        new TypeToken< ArrayList< BrandGroup > >() {
                        });

                mBrandGroup.put(itemCategory, tBrandsGroups);
                brandHandler.onSucceed(tBrandsGroups);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                brandHandler.onFailed();
            }


        });

    }

    public List< AutoSeriesGroup > getSeriesGroup() {
        return mSeriesGroups;
    }

    public void getSeriesGroup(Context context, Brand brand, int itemCategory, final SeriesHandler handler) {
        this.itemCategory = itemCategory;
        if (tBrand != null && this.tBrand.equals(brand) && mSeriesGroups != null) {
            handler.onSucceed(mSeriesGroups);
            return;
        }
        this.tBrand = brand;
        client.autoSeries(brand.brandID, brand.brandInitial, itemCategory,
                new LoadingAnimResponseHandler(context) {
                    @Override
                    public void onSuccess(String response) {
                        mSeriesGroups = JSONUtils.fromJson(response,
                                new TypeToken< ArrayList< AutoSeriesGroup > >() {
                                });
                        handler.onSucceed(mSeriesGroups);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                                          Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        handler.onFailed();
                    }

                });
    }

    /**
     * 清除车系和品牌
     */
    public void clearTempValue() {
        tBrand = null;
        mSeriesGroups = null;
    }

    public void toAutoList(Context context, int groupPosition, int childPosition) {
        Series autoSeries = AutoBrandDataControl.getInstance().getSeriesGroup().get(groupPosition).group.get(childPosition);
        Map< String, String > map = new HashMap<>();
        map.put(AutoListActivity.SERIES, autoSeries.getSeriesName());
        map.put(AutoItem.ItemCategory, String.valueOf(itemCategory));
        ActivitySwitcher.toAutoList(context, map);
    }

    public interface BrandHandler {
        void onSucceed(List< BrandGroup > brandGroups);

        void onFailed();
    }

    public interface SeriesHandler {
        void onSucceed(List< AutoSeriesGroup > seriesGroups);

        void onFailed();
    }
}
