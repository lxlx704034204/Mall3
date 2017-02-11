package com.hxqc.mall.thirdshop.accessory.control;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.thirdshop.accessory.api.AccessoryApiClient;
import com.hxqc.mall.thirdshop.accessory.model.AccessoryBigCategory;
import com.hxqc.mall.thirdshop.accessory.model.AccessoryInfo;
import com.hxqc.mall.thirdshop.accessory.model.AccessoryShop;
import com.hxqc.mall.thirdshop.accessory.model.Brand;
import com.hxqc.mall.thirdshop.accessory.model.BrandGroup;
import com.hxqc.mall.thirdshop.accessory.model.Series;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;
import org.apache.http.conn.HttpHostConnectException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Function: 筛选控制器
 *
 * @author 袁秉勇
 * @since 2016年02月16日
 */
public class FilterController {
    private final static String TAG = FilterController.class.getSimpleName();
    private static FilterController ourInstance;
    public HashMap< String, String > mFilterMap = new HashMap<>();
    private Context mContext;
    private Brand brand;
    private Series series;
    private AccessoryBigCategory accessoryBigCategory;
    private AccessoryApiClient accessoryApiClient;


    private FilterController() {
        this.accessoryApiClient = new AccessoryApiClient();
    }

    public static FilterController getInstance() {
        if (ourInstance == null) {
            synchronized (FilterController.class) {
                if (ourInstance == null) {
                    ourInstance = new FilterController();
                }
            }
        }
        return ourInstance;
    }

    public AccessoryBigCategory getAccessoryBigCategory() {
        return accessoryBigCategory;
    }

    public void setAccessoryBigCategory(AccessoryBigCategory accessoryBigCategory) {
        this.accessoryBigCategory = accessoryBigCategory;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    /*** 请求品牌 ***/
    public void requestAccessoryBrand(Context context, @NonNull final BrandHandler brandHandler) {
        accessoryApiClient.requestFilterBrand(new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                ArrayList< BrandGroup > tBrandsGroups = JSONUtils.fromJson(response, new TypeToken< ArrayList< BrandGroup > >() {
                });
                brandHandler.onSucceed(tBrandsGroups);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    brandHandler.onFailed(true);
                } else {
                    brandHandler.onFailed(false);
                }
            }
        });
    }

    /*** 请求车系 ***/
    public void requestAccessorySeries(Context context, @NonNull String brandID, @NonNull final SeriesHandler seriesHandler, boolean showLoading) {
        accessoryApiClient.requestFilterSeries(brandID, new LoadingAnimResponseHandler(context, showLoading, false) {
            @Override
            public void onSuccess(String response) {
                ArrayList<Series> series = JSONUtils.fromJson(response, new TypeToken< ArrayList< Series > >(){});
                    seriesHandler.onSucceed(series);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    seriesHandler.onFailed(true);
                } else {
                    seriesHandler.onFailed(false);
                }
            }


            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    /*** 获取用品分类数据 ***/
    public void getAccessoryCategoryData(Context context, String shopID, final AccessoryCategoryHandler accessoryCategoryHandler) {
        accessoryApiClient.requestAccessoryCategory(shopID, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                ArrayList< AccessoryBigCategory > accessoryBigCategory = JSONUtils.fromJson(response, new TypeToken< ArrayList< AccessoryBigCategory > >() {
                });
                accessoryCategoryHandler.onSucceed(accessoryBigCategory);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    accessoryCategoryHandler.onFailed(true);
                } else {
                    accessoryCategoryHandler.onFailed(false);
                }
            }
        });
    }

    /*** 获取用品数据 ***/
    public void getAccessoryPriceData(Context context, int page, int count, HashMap< String, String > searchConditionMap, final AccessoryInfoHandler accessoryInfoHandler) {
        accessoryApiClient.getProductSeriesList(page, count, searchConditionMap, new LoadingAnimResponseHandler(context, true) {
            @Override
            public void onSuccess(String response) {
                ArrayList< AccessoryInfo > accessoryInfos = JSONUtils.fromJson(response, new TypeToken< ArrayList< AccessoryInfo > >() {
                });
                accessoryInfoHandler.onGetInfoSucceed(accessoryInfos);
//                accessoryInfoHandler.onGetInfoSucceed(null);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                accessoryInfoHandler.onGetInfoFailed();
//                accessoryInfoHandler.onGetInfoSucceed(null);
            }
        });
    }

    /*** 获取用品商铺数据 ***/
    public void getAccessoryShopData(Context context, int page, int count, HashMap< String, String > searchConditionMap, final AccessoryShopHandler accessoryShopHandler) {
        accessoryApiClient.getShopListByProductSeriesID(page, count, searchConditionMap, new LoadingAnimResponseHandler(context, true) {
            @Override
            public void onSuccess(String response) {
                ArrayList< AccessoryShop > accessoryInfos = JSONUtils.fromJson(response, new TypeToken< ArrayList< AccessoryShop > >() {
                });
                accessoryShopHandler.onGetShopSucceed(accessoryInfos);
//                accessoryShopHandler.onGetShopSucceed(null);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                accessoryShopHandler.onGetShopFailed();
//                accessoryShopHandler.onGetShopSucceed(null);
            }
        });
    }

    /**** 添加筛选选择条件 ****/
    public void putValue(String key, String value) {
        mFilterMap.put(key, value);
    }

    public void destroy() {
        ourInstance = null;
    }


    /******* 请求品牌数据回调接口 ******/
    public interface BrandHandler {
        void onSucceed(ArrayList< BrandGroup > brandGroups);

        void onFailed(boolean offLine);
    }


    /******* 请求车系数据回调接口 ******/
    public interface SeriesHandler {
        void onSucceed(ArrayList< Series > series);

        void onFailed(boolean offLine);
    }


    /****** 请求用品分类数据回到接口 ******/
    public interface AccessoryCategoryHandler {
        void onSucceed(ArrayList< AccessoryBigCategory > accessoryBigCategories);

        void onFailed(boolean offLine);
    }


    public interface AccessoryInfoHandler {
        void onGetInfoSucceed(ArrayList<AccessoryInfo> accessoryInfos);

        void onGetInfoFailed();
    }


    public interface AccessoryShopHandler {
        void onGetShopSucceed(ArrayList< AccessoryShop > AccessoryInfos);

        void onGetShopFailed();
    }
}
