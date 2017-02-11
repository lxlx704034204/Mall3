package com.hxqc.mall.thirdshop.activity.auto.control;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.thirdshop.model.BrandGroup;
import com.hxqc.mall.thirdshop.model.CarType;
import com.hxqc.mall.thirdshop.model.ShopSearchAuto;
import com.hxqc.mall.thirdshop.model.ShopSearchShop;
import com.hxqc.mall.thirdshop.model.TCarSeriesModel;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;
import org.apache.http.conn.HttpHostConnectException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Function: 4S店中特卖车辆的筛选控制器
 *
 * @author 袁秉勇
 * @since 2016年05月18日
 */
public class FilterControllerForSpecialCar extends BaseFilterController {
    public static BaseFilterController getInstance() {
        if (ourInstance == null) {
            synchronized (FilterControllerForSpecialCar.class) {
                if (ourInstance == null) {
                    ourInstance = new FilterControllerForSpecialCar();
                }
            }
        }
        return ourInstance;
    }


    public void requestThirdShopBrand(Context context, @NonNull final TBrandHandler tBrandHandler) {
//        if (tBrandsGroups != null) {
//            tBrandHandler.onSucceed(tBrandsGroups);
//            return;
//        }

        ThirdPartShopClient.requestSpecialBrand(mFilterMap.get("siteID"), new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                tBrandsGroups = JSONUtils.fromJson(response, new TypeToken< ArrayList< BrandGroup > >() {
                });
                tBrandHandler.onSucceed(tBrandsGroups);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    tBrandHandler.onFailed(true);
                } else {
                    tBrandHandler.onFailed(false);
                }
            }
        });
    }


    /**
     * 请求车系
     */
    public void requestThirdShopSeries(Context context, @NonNull String brandName, @NonNull final TSeriesHandler tSeriesHandler, boolean showLoading) {
        ThirdPartShopClient.requestSpecialSeries(brandName, mFilterMap.get("siteID"), new LoadingAnimResponseHandler(context, showLoading, false) {
            @Override
            public void onSuccess(String response) {
                ArrayList< TCarSeriesModel > series = JSONUtils.fromJson(response, new TypeToken< ArrayList< TCarSeriesModel > >() {
                });
                tSeriesHandler.onSucceed(series);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    tSeriesHandler.onFailed(true);
                } else {
                    tSeriesHandler.onFailed(false);
                }
            }


            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }


    /**
     * 请求车型
     */
    public void requestThirdShopType(@NonNull String brand, @NonNull String series, Context context, final TTypeHandler tTypeHandler, boolean showLoading) {
        ThirdPartShopClient.requestSpecialModel(brand, series, mFilterMap.get("siteID"), new LoadingAnimResponseHandler(context, showLoading, false) {
            @Override
            public void onSuccess(String response) {
                ArrayList< CarType > carTypes = JSONUtils.fromJson(response, new TypeToken< ArrayList< CarType > >() {
                });
                tTypeHandler.onSucceed(carTypes);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    tTypeHandler.onFailed(true);
                } else {
                    tTypeHandler.onFailed(false);
                }
            }
        });
    }


    /**
     * 获取商铺数据
     */
    public void getShopData(Context context, int page, int count, HashMap< String, String > searchConditionMap, final TShopHandler tShopHandler) {
        ThirdPartShopClient.requestSpecialFilterShop(page, count, searchConditionMap, new LoadingAnimResponseHandler(context, true) {
            @Override
            public void onSuccess(String response) {
                ArrayList< ShopSearchShop > shopSearchShops = JSONUtils.fromJson(response, new TypeToken< ArrayList< ShopSearchShop > >() {
                });
                tShopHandler.onGetShopSucceed(shopSearchShops);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                tShopHandler.onGetShopFailed();
            }
        });
    }


    /**
     * 获取车辆数据
     */
    public void getCarData(Context context, int page, int count, HashMap< String, String > searchConditionMap, final TCarHandler tCarHandler) {
        ThirdPartShopClient.requestSpecialFilterCar(page, count, searchConditionMap, new LoadingAnimResponseHandler(context, true) {
            @Override
            public void onSuccess(String response) {
                ArrayList< ShopSearchAuto > shopSearchAutos = JSONUtils.fromJson(response, new TypeToken< ArrayList< ShopSearchAuto > >() {
                });
                tCarHandler.onGetCarSucceed(shopSearchAutos);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                tCarHandler.onGetCarFailed();
            }
        });
    }
}
