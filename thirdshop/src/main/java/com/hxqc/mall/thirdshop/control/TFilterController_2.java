package com.hxqc.mall.thirdshop.control;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.Brand;
import com.hxqc.mall.thirdshop.model.BrandGroup;
import com.hxqc.mall.thirdshop.model.CarType;
import com.hxqc.mall.thirdshop.model.Series;
import com.hxqc.mall.thirdshop.model.ShopSearchAuto;
import com.hxqc.mall.thirdshop.model.ShopSearchShop;
import com.hxqc.mall.thirdshop.model.TCarSeriesModel;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;
import org.apache.http.conn.HttpHostConnectException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年05月17日
 */
public class TFilterController_2 {
    public static final String TFilerTypeCar = "car";
    public static final String TFilerTypeShop = "shop";

    public static final String BrandKey = "brand";
    public static final String SerieKey = "serie";
    public static final String ModelKey = "model";
    public static final String AreaInitialKey = "areaInitial";
    public static final String AreaKey = "area";
    public static final String TypeKey = "type";

    private static TFilterController_2 ourInstance;
    private ThirdPartShopClient ThirdPartShopClient;

    public HashMap< String, String > mFilterMap;

    private Brand brand;
    private Series series;
    private CarType carType;


    {
        mFilterMap = new HashMap<>();
//        setTfilerType(TFilerTypeShop);
    }


    public Brand getBrand() {
        return brand;
    }


    public CarType getCarType() {
        return carType;
    }


    public Series getSeries() {
        return series;
    }


    public void setBrand(Brand brand) {
        this.brand = brand;
    }


    public void setCarType(CarType carType) {
        this.carType = carType;
    }


    public void setSeries(Series series) {
        this.series = series;
    }


    public static TFilterController_2 getInstance() {
        if (ourInstance == null) {
            synchronized (TFilterController_2.class) {
                if (ourInstance == null) {
                    ourInstance = new TFilterController_2();
                }
            }
        }
        return ourInstance;
    }


    private TFilterController_2() {
        this.ThirdPartShopClient = new ThirdPartShopClient();
    }


//    public interface TBrandHandler {
//        void onSucceed(ArrayList< BrandGroup > brandGroups);
//
//        void onFailed(boolean offLine);
//    }
//
//    public interface TSeriesHandler {
//        void onSucceed(ArrayList< Series > series);
//
//        void onFailed(boolean offLine);
//    }
//
//    public interface TTypeHandler {
//        void onSucceed(ArrayList< CarType > carTypes);
//
//        void onFailed(boolean offLine);
//    }
//
//    public interface TAreaHandler {
//        void onSucceed(ArrayList< AutoSeriesGroup > seriesGroups);
//
//        void onFailed();
//    }
//
    public interface TShopHandler {
        void onGetShopSucceed(ArrayList< ShopSearchShop > shopSearchShops);

        void onGetShopFailed();
    }

    public interface TCarHandler {
        void onGetCarSucceed(ArrayList< ShopSearchAuto > shopSearchAutos);

        void onGetCarFailed();
    }

    ArrayList< BrandGroup > tBrandsGroups;


    /**
     * 请求品牌
     */
    public void requestThirdShopBrand(Context context, String siteID, @NonNull final TFilterController.TBrandHandler tBrandHandler) {

        if (tBrandsGroups != null && tBrandHandler != null) {
            tBrandHandler.onSucceed(tBrandsGroups);
            return;
        }


        ThirdPartShopClient.requestSpecialBrand(siteID, new LoadingAnimResponseHandler(context, true, false) {
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
    public void requestThirdShopSeries(Context context, @NonNull String brandName, String siteID, @NonNull final TFilterController.TSeriesHandler tSeriesHandler, boolean showLoading) {
        ThirdPartShopClient.requestSpecialSeries(brandName, siteID, new LoadingAnimResponseHandler(context, showLoading, false) {
            @Override
            public void onSuccess(String response) {
                TCarSeriesModel tCarSeriesModel = JSONUtils.fromJson(response, TCarSeriesModel.class);
                tSeriesHandler.onSucceed(tCarSeriesModel.series);
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
    public void requestThirdShopType(@NonNull String brand, @NonNull String series, String siteID, Context context, final TFilterController.TTypeHandler tTypeHandler, boolean showLoading) {
        ThirdPartShopClient.requestSpecialModel(brand, series, siteID, new LoadingAnimResponseHandler(context, showLoading, false) {
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


    /**
     * 请求地区
     */
    public void requestThirdShopArea() {

    }


    /**
     * 设置筛选类型
     *
     * @param type TFilerTypeCar 车
     *             TFilerTypeShop 店铺
     */
    public void setTfilerType(String type) {
        mFilterMap.put(TypeKey, type);
    }


    /**
     * 添加筛选选择条件
     *
     * @param key
     * @param value
     */
    public void putValue(String key, String value) {
        mFilterMap.put(key, value);
    }


    public void destroy() {
        ourInstance = null;
    }
}