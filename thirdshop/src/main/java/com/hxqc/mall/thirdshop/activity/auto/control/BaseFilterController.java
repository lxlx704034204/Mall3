package com.hxqc.mall.thirdshop.activity.auto.control;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.AutoSeriesGroup;
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
 * Function: 4S店筛选数据控制器
 *
 * @author 袁秉勇
 * @since 2016年05月18日
 */
public class BaseFilterController {
    protected static BaseFilterController ourInstance;
    protected ThirdPartShopClient ThirdPartShopClient;

    public HashMap< String, String > mFilterMap = new HashMap<>();

    protected Brand brand;
    protected Series series;
    protected CarType carType;


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


    public static BaseFilterController getInstance() {
        if (ourInstance == null) {
            synchronized (BaseFilterController.class) {
                if (ourInstance == null) {
                    ourInstance = new BaseFilterController();
                }
            }
        }
        return ourInstance;
    }


    protected BaseFilterController() {
        this.ThirdPartShopClient = new ThirdPartShopClient();
    }


    public interface TBrandHandler {
        void onSucceed(ArrayList< BrandGroup > brandGroups);

        void onFailed(boolean offLine);
    }

    public interface TSeriesHandler {
        void onSucceed(ArrayList< TCarSeriesModel > series);

        void onFailed(boolean offLine);
    }

    public interface TTypeHandler {
        void onSucceed(ArrayList< CarType > carTypes);

        void onFailed(boolean offLine);
    }

    public interface TAreaHandler {
        void onSucceed(ArrayList< AutoSeriesGroup > seriesGroups);

        void onFailed();
    }

    public interface TShopHandler {
        void onGetShopSucceed(ArrayList< ShopSearchShop > shopSearchShops);

        void onGetShopFailed();
    }

    public interface TCarHandler {
        void onGetCarSucceed(ArrayList< ShopSearchAuto > shopSearchAutos);

        void onGetCarFailed();
    }

    ArrayList< BrandGroup > tBrandsGroups;

    ArrayList< BrandGroup > sBrandsGroups;


    /**
     * 请求品牌
     */
    public void requestThirdShopBrand(Context context, @NonNull final TBrandHandler tBrandHandler) {
//        if (tBrandsGroups != null && tBrandHandler != null) {
//            tBrandHandler.onSucceed(tBrandsGroups);
//            return;
//        }

        ThirdPartShopClient.requestFilterBrand(new LoadingAnimResponseHandler(context, true, false) {
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
     * 请求4s品牌
     */
    public void request4SShopBrand(Context context, String siteID, @NonNull final TBrandHandler tBrandHandler) {
        if (sBrandsGroups != null && tBrandHandler != null) {
            tBrandHandler.onSucceed(sBrandsGroups);
            return;
        }

        ThirdPartShopClient.requestSpecialBrand(siteID, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                sBrandsGroups = JSONUtils.fromJson(response, new TypeToken< ArrayList< BrandGroup > >() {
                });
                tBrandHandler.onSucceed(sBrandsGroups);
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
        ThirdPartShopClient.requestFilterSeries(brandName, new LoadingAnimResponseHandler(context, showLoading, false) {
            @Override
            public void onSuccess(String response) {
                ArrayList< TCarSeriesModel > tCarSeriesModels = JSONUtils.fromJson(response, new TypeToken< ArrayList< TCarSeriesModel > >() {
                });
                tSeriesHandler.onSucceed(tCarSeriesModels);
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
        });
    }


//    /**
//     * 请求4S车系
//     */
//    public void request4SShopSeries(Context context, String siteID, @NonNull final String brandName, @NonNull final TSeriesHandler tSeriesHandler, boolean showLoading) {
//        ThirdPartShopClient.requestSpecialSeries(brandName, siteID, new LoadingAnimResponseHandler(context, showLoading, false) {
//            @Override
//            public void onSuccess(String response) {
//                TCarSeriesModel tCarSeriesModel = new TCarSeriesModel(brandName);
//                tCarSeriesModel.series = JSONUtils.fromJson(response, new TypeToken< ArrayList< Series > >() {
//                });
//                if (tCarSeriesModel.series != null && tCarSeriesModel.series.size() > 0) {
//                    tSeriesHandler.onSucceed(tCarSeriesModel.series);
//                } else {
//                    tSeriesHandler.onFailed(false);
//                }
//            }
//
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//                if (throwable instanceof HttpHostConnectException) {
//                    tSeriesHandler.onFailed(true);
//                } else {
//                    tSeriesHandler.onFailed(false);
//                }
//            }
//        });
//    }


    /**
     * 请求车型
     */
    public void requestThirdShopType(@NonNull String brand, @NonNull String series, Context context, final TTypeHandler tTypeHandler, boolean showLoading) {
        ThirdPartShopClient.requestFilterModel(brand, series, new LoadingAnimResponseHandler(context, showLoading, false) {
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
        ThirdPartShopClient.searchShop(page, count, 2, searchConditionMap, new LoadingAnimResponseHandler(context, true) {
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
        ThirdPartShopClient.searchCar(page, count, 2, searchConditionMap, new LoadingAnimResponseHandler(context, true) {
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
