package com.hxqc.mall.auto.controler;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.auto.api.AutoTypeClient;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.AutoModel;
import com.hxqc.mall.auto.model.AutoModelGroup;
import com.hxqc.mall.auto.model.Brand;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.auto.model.SeriesGroup;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import org.apache.http.conn.HttpHostConnectException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 17
 * FIXME
 * Todo 车辆品牌车系车型控制器
 */
public class AutoTypeControl {


    private static final String TAG = AutoInfoContants.LOG_J;
    private static AutoTypeControl ourInstance;

    private AutoTypeClient mAutoTypeClient;

    private ArrayList<BrandGroup> brandGroups;

    private ArrayList<SeriesGroup> seriesGroups;

    private ArrayList<AutoModel> autoModels;

    private ArrayList<AutoModelGroup> autoModelN;

    public static AutoTypeControl getInstance() {
        if (ourInstance == null) {
            synchronized (AutoInfoControl.class) {
                if (ourInstance == null) {
                    ourInstance = new AutoTypeControl();
                }
            }
        }
        return ourInstance;
    }

    private AutoTypeControl() {
        this.mAutoTypeClient = new AutoTypeClient();
    }

    public void killInstance() {
        if (mAutoTypeClient != null) {
            mAutoTypeClient = null;
        }

        if (ourInstance != null) {
            ourInstance = null;
        }
    }

    public ArrayList<BrandGroup> getBrandGroups() {
        return brandGroups;
    }

    public ArrayList<SeriesGroup> getSeriesGroups() {
        return seriesGroups;
    }

    public ArrayList<AutoModel> getAutoModels() {
        return autoModels;
    }

    public ArrayList<AutoModelGroup> getAutoModelN() {
        return autoModelN;
    }

    /**
     * 请求品牌
     *
     * @param context
     * @param shopID
     * @param callBack
     */
    public void requestBrand(Context context, String shopID, @NonNull final CallBackControl.CallBack<ArrayList<BrandGroup>> callBack) {
        mAutoTypeClient.autoBrand(shopID, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                ArrayList<BrandGroup> tMyBrandGroups = JSONUtils.fromJson(response, new TypeToken<ArrayList<BrandGroup>>() {
                });
                brandGroups = tMyBrandGroups;
                callBack.onSuccess(tMyBrandGroups);
            }

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
     * 请求车系
     *
     * @param context
     * @param shopID
     * @param brand
     * @param brandID
     * @param callBack
     */
    public void requestSeries(Context context, String shopID, String brand, String brandID, @NonNull final CallBackControl.CallBack<ArrayList<SeriesGroup>> callBack) {
        DebugLog.i(TAG, mAutoTypeClient.toString());
        mAutoTypeClient.autoSeries(shopID, brand, brandID, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                ArrayList<SeriesGroup> tMySeriesGroups = JSONUtils.fromJson(response, new TypeToken<ArrayList<SeriesGroup>>() {
                });
                seriesGroups = tMySeriesGroups;
                callBack.onSuccess(tMySeriesGroups);
            }

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
     * 请求车型
     *
     * @param context
     * @param shopID
     * @param brand
     * @param brandID
     * @param series
     * @param seriesID
     * @param callBack
     */
    public void requestAutoModel(Context context, String shopID, String brand, String brandID, String series, String seriesID, @NonNull final CallBackControl.CallBack<ArrayList<AutoModel>> callBack) {
        mAutoTypeClient.autoModel(shopID, brand, brandID, series, seriesID, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                ArrayList<AutoModel> tMyAutoModels = JSONUtils.fromJson(response, new TypeToken<ArrayList<AutoModel>>() {
                });
                autoModels = tMyAutoModels;
                callBack.onSuccess(tMyAutoModels);
            }

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
     * 请求车型
     *
     * @param context
     * @param shopID
     * @param brand
     * @param brandID
     * @param series
     * @param seriesID
     * @param callBack
     */
    public void requestAutoModelN(Context context, String shopID, String brand, String brandID, String series, String seriesID, @NonNull final CallBackControl.CallBack<ArrayList<AutoModelGroup>> callBack) {
        mAutoTypeClient.autoModelN(shopID, brand, brandID, series, seriesID, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                ArrayList<AutoModelGroup> tMyAutoModels = JSONUtils.fromJson(response, new TypeToken<ArrayList<AutoModelGroup>>() {
                });
                autoModelN = tMyAutoModels;
                callBack.onSuccess(tMyAutoModels);
            }

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
     * 获取品牌名
     *
     * @param shopBrandGroups
     * @return
     */
    public String getBrand(ArrayList<BrandGroup> shopBrandGroups) {
        String brands = "";
        for (BrandGroup brandGroup : shopBrandGroups) {
            for (Brand brand : brandGroup.group) {
                brands = brands + "  " + brand.brandName;
            }
        }
        return brands;
    }

    /**
     * @param context
     * @param shopBrandGroups
     * @return
     */
    /*public boolean query(Context context, ArrayList<BrandGroup> shopBrandGroups) {
        for (int i = 0; i < shopBrandGroups.size(); i++) {
            for (int j = 0; j < shopBrandGroups.get(i).group.size(); j++) {
                boolean brandID = AutoDao.getInstance().queryByBrandID(context, shopBrandGroups.get(i).group.get(j).brandID);
                return brandID;
            }
        }
        return false;
    }*/

    /**
     * 得到所有品牌
     *
     * @param brandGroups
     * @return
     */
    public ArrayList<Brand> getBrands(ArrayList<BrandGroup> brandGroups) {
        ArrayList<Brand> brands = new ArrayList<Brand>();
        for (int i = 0; i < brandGroups.size(); i++) {
            for (int j = 0; j < brandGroups.get(i).group.size(); j++) {
                brands.add(brandGroups.get(i).group.get(j));
            }
        }
        return brands;
    }
}
