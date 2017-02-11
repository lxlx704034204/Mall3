package com.hxqc.mall.thirdshop.accessory4s.control;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.auto.inter.CallBack;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.thirdshop.accessory4s.api.Accessory4SApiClient;
import com.hxqc.mall.thirdshop.accessory4s.model.InstallmentBuyingModel;
import com.hxqc.mall.thirdshop.accessory4s.model.InstallmentBuyingSeries;
import com.hxqc.mall.thirdshop.accessory4s.model.SeriesGroup;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.HttpHostConnectException;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 11 - 22
 * FIXME
 * Todo 分期购车接口控制器
 */

public class InstallmentBuyingControl {

    private Accessory4SApiClient mAccessory4SApiClient;

    private static InstallmentBuyingControl ourInstance;

    public static InstallmentBuyingControl getInstance() {
        if (ourInstance == null) {
            synchronized (InstallmentBuyingControl.class) {
                if (ourInstance == null) {
                    ourInstance = new InstallmentBuyingControl();
                }
            }
        }
        return ourInstance;
    }

    private InstallmentBuyingControl() {
        this.mAccessory4SApiClient = new Accessory4SApiClient();
    }

    public void killInstance() {
        if (mAccessory4SApiClient != null) {
            mAccessory4SApiClient = null;
        }
        if (ourInstance != null) {
            ourInstance = null;
        }
    }

    /**
     * 分期 筛选条件 品牌列表
     *
     * @param context
     * @param siteID
     * @param callBack
     */
    public void getFilterAutoBrandInstallment(Context context, String siteID, @NonNull final CallBack<ArrayList<BrandGroup>> callBack) {
        mAccessory4SApiClient.filterAutoBrandInstallment(siteID, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                ArrayList<BrandGroup> mBrandGroups = JSONUtils.fromJson(response, new TypeToken<ArrayList<BrandGroup>>() {
                });
                callBack.onSuccess(mBrandGroups);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    callBack.onFailed(true);
                } else {
                    callBack.onFailed(false);
                }
            }
        });
    }

    /**
     * 分期 筛选条件 车系列表
     *
     * @param context
     * @param siteID
     * @param callBack
     */
    public void getFilterAutoSeriesInstallment(Context context, String brand, String siteID, @NonNull final CallBack<ArrayList<SeriesGroup>> callBack) {
        mAccessory4SApiClient.filterAutoSeriesInstallment(brand,siteID, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                ArrayList<SeriesGroup> mSeriesGroups = JSONUtils.fromJson(response, new TypeToken<ArrayList<SeriesGroup>>() {
                });
                callBack.onSuccess(mSeriesGroups);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    callBack.onFailed(true);
                } else {
                    callBack.onFailed(false);
                }
            }
        });
    }

    /**
     * 分期 车系列表
     *
     * @param context
     * @param shopID
     * @param brand
     * @param serie
     * @param callBack
     */
    public void getSearchAutoSericeInstallmentList(Context context, String shopID, String brand, String serie, @NonNull final CallBack<ArrayList<InstallmentBuyingSeries>> callBack) {
        mAccessory4SApiClient.searchAutoSericeInstallmentList(shopID, brand, serie, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                ArrayList<InstallmentBuyingSeries> mInstallmentBuyingSeries = JSONUtils.fromJson(response, new TypeToken<ArrayList<InstallmentBuyingSeries>>() {
                });
                callBack.onSuccess(mInstallmentBuyingSeries);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    callBack.onFailed(true);
                } else {
                    callBack.onFailed(false);
                }
            }
        });
    }

    /**
     * 店铺 车系分期列表
     *
     * @param context
     * @param shopID
     * @param callBack
     */
    public void getAutoSericesInstallment(Context context, String shopID, @NonNull final CallBack<ArrayList<InstallmentBuyingSeries>> callBack) {
        mAccessory4SApiClient.autoSericesInstallment(shopID, new LoadingAnimResponseHandler(context, true, false) {

            @Override
            public void onSuccess(String response) {
                ArrayList<InstallmentBuyingSeries> mInstallmentBuyingSeries = JSONUtils.fromJson(response, new TypeToken<ArrayList<InstallmentBuyingSeries>>() {
                });
                callBack.onSuccess(mInstallmentBuyingSeries);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    callBack.onFailed(true);
                } else {
                    callBack.onFailed(false);
                }
            }
        });
    }

    /**
     * 店铺 车型分期列表
     *
     * @param context
     * @param shopID
     * @param brand
     * @param serie
     * @param callBack
     */
    public void getAutoModelInstallment(Context context, String shopID, String brand, String serie, @NonNull final CallBack<InstallmentBuyingModel> callBack) {
        mAccessory4SApiClient.autoModelInstallment(shopID, brand, serie, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                InstallmentBuyingModel mInstallmentBuyingModel = JSONUtils.fromJson(response, new TypeToken<InstallmentBuyingModel>() {
                });
                callBack.onSuccess(mInstallmentBuyingModel);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    callBack.onFailed(true);
                } else {
                    callBack.onFailed(false);
                }
            }
        });
    }

}
