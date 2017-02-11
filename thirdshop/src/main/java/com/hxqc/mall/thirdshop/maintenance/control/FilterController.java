package com.hxqc.mall.thirdshop.maintenance.control;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.config.application.SampleApplicationContext;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
import com.hxqc.mall.thirdshop.maintenance.model.Brand;
import com.hxqc.mall.thirdshop.maintenance.model.BrandGroup;
import com.hxqc.mall.thirdshop.maintenance.model.CarSeriesModel;
import com.hxqc.mall.thirdshop.maintenance.model.MaintenanceShop;
import com.hxqc.mall.thirdshop.maintenance.model.NewMaintenanceShop;
import com.hxqc.mall.thirdshop.maintenance.model.Series;
import com.hxqc.mall.thirdshop.maintenance.utils.SharedPreferencesHelper;
import com.hxqc.util.JSONUtils;

import org.apache.http.conn.HttpHostConnectException;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * Function: 筛选控制器
 *
 * @author 袁秉勇
 * @since 2016年02月16日
 */
public class FilterController {
    private final static String TAG = FilterController.class.getSimpleName();
    private Context mContext;

    private static FilterController ourInstance;
    public HashMap< String, String > mFilterMap = new HashMap<>();
    private Brand brand;
    private Series series;

    private MaintenanceClient maintenanceClient;


    private FilterController() {
        this.maintenanceClient = new MaintenanceClient();
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
    public void requestMaintenanceBrand(Context context, @NonNull final BrandHandler brandHandler) {
        maintenanceClient.requestFilterBrand(new LoadingAnimResponseHandler(context, true, false) {
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
    public void requestMaintenanceSeries(Context context, @NonNull String brandName, @NonNull final SeriesHandler seriesHandler, boolean showLoading) {
        maintenanceClient.requestFilterSeries(brandName, new LoadingAnimResponseHandler(context, showLoading, false) {
            @Override
            public void onSuccess(String response) {
//                CarSeriesModel carSeriesModel = JSONUtils.fromJson(response, CarSeriesModel.class);
                ArrayList< CarSeriesModel > carSeriesModel = JSONUtils.fromJson(response, new TypeToken< ArrayList< CarSeriesModel > >() {
                });
                if (carSeriesModel != null && carSeriesModel.get(0) != null) {
                    seriesHandler.onSucceed(carSeriesModel.get(0).series);
                } else {
                    seriesHandler.onFailed(false);
                }
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
        });
    }


//    /*** 获取商铺数据 ***/
//    public void getMaintenanceShopListData(Context context, int page, int count, HashMap< String, String > searchConditionMap, final MaintenanceShopListHandler maintenanceShopListHandler) {
//        if (searchConditionMap.containsKey("area") && searchConditionMap.get("area").equals("全国")) searchConditionMap.remove("area");
//        maintenanceClient.requireMaintenanceData(page, count, searchConditionMap, new LoadingAnimResponseHandler(context, true) {
//            @Override
//            public void onSuccess(String response) {
//                ArrayList< Object > objects = JSONUtils.fromJson(response, new TypeToken< ArrayList< Object > >() {
//                });
//                maintenanceShopListHandler.onSucceed(objects);
//            }
//
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//                if (throwable instanceof HttpHostConnectException) {
//                    maintenanceShopListHandler.onFailed(true);
//                } else {
//                    maintenanceShopListHandler.onFailed(false);
//                }
//            }
//        });
//    }


    /*** 获取商铺数据 ***/
    public void getNewMaintenanceListData(Context context, int page, int count, String brandID, String seriesID, String autoModelID, String myAutoID, String items, int shopType, HashMap< String, String > searchConditionMap, boolean showAnim, final MaintenanceShopListHandler maintenanceShopListHandler) {
        maintenanceClient.requireNewMaintenanceData(page, count, brandID, seriesID, autoModelID, myAutoID, items, shopType, searchConditionMap, new LoadingAnimResponseHandler(context, showAnim) {
            @Override
            public void onSuccess(String response) {
                ArrayList< NewMaintenanceShop > maintenanceShops = JSONUtils.fromJson(response, new TypeToken< ArrayList< NewMaintenanceShop > >() {
                });
                maintenanceShopListHandler.onSucceed(maintenanceShops);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable instanceof HttpHostConnectException) {
                    maintenanceShopListHandler.onFailed(true);
                } else {
                    maintenanceShopListHandler.onFailed(false);
                }
            }


            @Override
            public void onFinish() {
                super.onFinish();
                maintenanceShopListHandler.onFinish();
            }
        });
    }


    /*** 获取商铺数据 ***/
    public void getShopData(Context context, int page, int count, HashMap< String, String > searchConditionMap, final ShopHandler shopHandler) {
        if (searchConditionMap.containsKey("area") && (searchConditionMap.get("area").equals("全国") || searchConditionMap.get("area").equals(new SharedPreferencesHelper(SampleApplicationContext.application).getCity()))) searchConditionMap.remove("area");
        maintenanceClient.requireMaintenanceData(page, count, searchConditionMap, new LoadingAnimResponseHandler(context, true) {
            @Override
            public void onSuccess(String response) {
                ArrayList< MaintenanceShop > maintenanceShops = JSONUtils.fromJson(response, new TypeToken< ArrayList< MaintenanceShop > >() {
                });
                shopHandler.onGetShopSucceed(maintenanceShops);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                shopHandler.onGetShopFailed();
            }
        });
    }


    /*** 获取救援数据 ***/
    public void getRescueData(Context context, int page, int count, HashMap< String, String > searchConditionMap, final RescueHandler rescueHandler) {
        if (searchConditionMap.containsKey("area") && (searchConditionMap.get("area").equals("全国") || searchConditionMap.get("area").equals(new SharedPreferencesHelper(SampleApplicationContext.application).getCity()))) searchConditionMap.remove("area");
        maintenanceClient.requireRescueData(page, count, searchConditionMap, new LoadingAnimResponseHandler(context, true) {
            @Override
            public void onSuccess(String response) {
                ArrayList< MaintenanceShop > maintenanceShops = JSONUtils.fromJson(response, new TypeToken< ArrayList< MaintenanceShop > >() {
                });
                rescueHandler.onGetPriceSucceed(maintenanceShops);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                rescueHandler.onGetPriceFailed();
            }
        });
    }


    /*** 获取预约维修数据 ***/
    public void getAppointmentMaintenanceData(Context context, int page, int count, HashMap< String, String > searchConditionMap, final AppointmentMsgHandler appointmentMsgHandler) {
        if (searchConditionMap.containsKey("area") && (searchConditionMap.get("area").equals("全国") || searchConditionMap.get("area").equals(new SharedPreferencesHelper(SampleApplicationContext.application).getCity()))) searchConditionMap.remove("area");
        maintenanceClient.requireAppointmentData(page, count, searchConditionMap, new LoadingAnimResponseHandler(context, true) {
            @Override
            public void onSuccess(String response) {
                ArrayList< MaintenanceShop > maintenanceShops = JSONUtils.fromJson(response, new TypeToken< ArrayList< MaintenanceShop > >() {
                });
                appointmentMsgHandler.onGetAppointmentMsgSucceed(maintenanceShops);
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                appointmentMsgHandler.onGetAppointmentMsgFailed();
            }


            @Override
            public void onFinish() {
                super.onFinish();
                appointmentMsgHandler.onFinish();
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


    public interface ShopHandler {
        void onGetShopSucceed(ArrayList< MaintenanceShop > maintenanceShops);

        void onGetShopFailed();
    }


    public interface RescueHandler {
        void onGetPriceSucceed(ArrayList< MaintenanceShop > maintenanceShops);

        void onGetPriceFailed();
    }


    public interface AppointmentMsgHandler {
        void onGetAppointmentMsgSucceed(ArrayList< MaintenanceShop > maintenanceShops);

        void onGetAppointmentMsgFailed();

        void onFinish();
    }


    public interface MaintenanceShopListHandler {
        void onSucceed(ArrayList< NewMaintenanceShop > newMaintenanceShops);

        void onFailed(boolean offLine);

        void onFinish();
    }
}
