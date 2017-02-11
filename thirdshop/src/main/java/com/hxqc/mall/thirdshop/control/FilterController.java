package com.hxqc.mall.thirdshop.control;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.auto.model.Series;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.model.auto.Brand;
import com.hxqc.mall.thirdshop.model.CarType;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;
import org.apache.http.conn.HttpHostConnectException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author:李烽
 * Date:2016-05-17
 * FIXME
 * Todo
 */
@Deprecated
public class FilterController extends TFilterController {

    public HashMap<String, String> mFilterMap;

    private Brand brand;
    private Series series;

    {
        mFilterMap = new HashMap<>();
    }

    private FilterController() {
        super();
    }

    private static FilterController instance;

    public static FilterController getInstance() {
        if (instance == null) {
            synchronized (TFilterController.class) {
                if (instance == null) {
                    instance = new FilterController();
                }
            }
        }
        return instance;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }


    public void setSeries(Series series) {
        this.series = series;
    }


    public interface BrandHandler {
        void onSucceed(ArrayList<BrandGroup> brandGroups);

        void onFailed(boolean offLine);
    }

    public interface SeriesHandler {
        void onSucceed(ArrayList<Series> series);

        void onFailed(boolean offLine);
    }

    public interface ModelHandler {
        void onSucceed(ArrayList<CarType> carModels);

        void onFailed(boolean offLine);
    }

    public FilterController setAreaID(String areaID) {
        instance.areaID = areaID;
        return instance;
    }

    public void requestThirdShopBrand(Context context, @NonNull final BrandHandler brandHandler) {
        ThirdPartShopClient.filterAutoBrand(instance.areaID, new LoadingAnimResponseHandler(context) {
            @Override
            public void onSuccess(String response) {
                ArrayList<BrandGroup> brands = JSONUtils.fromJson(response, new TypeToken<ArrayList<BrandGroup>>() {
                });
                brandHandler.onSucceed(brands);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBytes, Throwable throwable) {
                super.onFailure(statusCode, headers, responseBytes, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    brandHandler.onFailed(true);
                } else {
                    brandHandler.onFailed(false);
                }
            }
        });
    }


    public void requestThirdShopSeries(Context context, @NonNull String brandName, @NonNull final SeriesHandler seriesHandler, boolean showLoading) {
        ThirdPartShopClient.filterAutoBrand(instance.areaID, new LoadingAnimResponseHandler(context) {
            @Override
            public void onSuccess(String response) {
                ArrayList<Series> brands = JSONUtils.fromJson(response, new TypeToken<ArrayList<Series>>() {
                });
                seriesHandler.onSucceed(brands);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBytes, Throwable throwable) {
                super.onFailure(statusCode, headers, responseBytes, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    seriesHandler.onFailed(true);
                } else {
                    seriesHandler.onFailed(false);
                }
            }
        });
    }

    public void requestThirdShopType(@NonNull String brand, @NonNull String series, Context context, final ModelHandler modelHandler, boolean showLoading) {
        ThirdPartShopClient.filterAutoBrand(instance.areaID, new LoadingAnimResponseHandler(context) {
            @Override
            public void onSuccess(String response) {
                ArrayList<CarType> brands = JSONUtils.fromJson(response, new TypeToken<ArrayList<CarType>>() {
                });
                modelHandler.onSucceed(brands);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBytes, Throwable throwable) {
                super.onFailure(statusCode, headers, responseBytes, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    modelHandler.onFailed(true);
                } else {
                    modelHandler.onFailed(false);
                }
            }
        });
    }
}
