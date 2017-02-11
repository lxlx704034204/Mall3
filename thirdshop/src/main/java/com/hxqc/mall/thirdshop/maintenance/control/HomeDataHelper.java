package com.hxqc.mall.thirdshop.maintenance.control;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.interfaces.LoadDataCallBack;
import com.hxqc.mall.core.model.Error;
import com.hxqc.mall.thirdshop.maintenance.adapter.OtherMaintenancePackageListAdapter;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
import com.hxqc.mall.thirdshop.maintenance.model.Mechanic;
import com.hxqc.mall.thirdshop.maintenance.model.ServiceAdviser;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceHome;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenancePackage;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-03-14
 * FIXME
 * Todo 维修首页数据帮助类
 */
public class HomeDataHelper {
    private static HomeDataHelper instance;
    private static MaintenanceClient client;
//    private static MaintenanceBaseFragment.OnMaintenanceListener onMaintenanceListener;
    private Context context;

    private HomeDataHelper(Context context) {

        this.context = context.getApplicationContext();
    }

    public static synchronized HomeDataHelper getInstance(Context context) {
        if (instance == null)
            synchronized (HomeDataHelper.class) {
                if (instance == null)
                    instance = new HomeDataHelper(context);
            }
        return instance;
    }

    private static MaintenanceClient getClient() {
        if (client == null)
            client = new MaintenanceClient();
        return client;
    }

//    private static MaintenanceBaseFragment.OnMaintenanceListener getOnMaintenanceListener() {
//        if (onMaintenanceListener == null)
//            onMaintenanceListener = new MaintenanceHomeActivity();
//        return onMaintenanceListener;
//    }

    /**
     * 销毁
     */
    public void destory() {
        if (instance != null)
            instance = null;
    }

    /**
     * 获得维修首页数据
     *
     * @param autoModel
     * @param drivingDistance
     * @param plateNumber
     * @param callBack
     */
    public void getHomeData(String shopID,String autoModel, String autoModelID, String drivingDistance, String plateNumber,
                            final LoadDataCallBack<MaintenanceHome> callBack) {
        client = getClient();
//        onMaintenanceListener = getOnMaintenanceListener();
        client.home(autoModel, autoModelID, drivingDistance, plateNumber, /*"shop1580552177935965"*/
               shopID, new BaseMallJsonHttpResponseHandler(context) {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        callBack.onDataNull("resultCode is " + statusCode);
                    }

                    @Override
                    public void onSuccess(String response) {
                        MaintenanceHome home = JSONUtils.fromJson(response, new TypeToken<MaintenanceHome>() {
                        });
                        if (home == null)
                            callBack.onDataNull("");
                        else
                            callBack.onDataGot(home);
                    }
                });


    }

    /**
     * 获取匹配的车辆
     *
     * @param callBack
     */
    @Deprecated
    public void getMatchAuto(String shopID,final LoadDataCallBack<MyAuto> callBack) {
        client = getClient();
//        onMaintenanceListener = getOnMaintenanceListener();
        client.home("", "", "", "",shopID, new BaseMallJsonHttpResponseHandler(context) {
            @Override
            public void onSuccess(String response) {
                MaintenanceHome home = JSONUtils.fromJson(response, new TypeToken<MaintenanceHome>() {
                });
                if (home == null)
                    callBack.onDataNull("home is null");
                else {
                    if (home.myAuto == null)
                        callBack.onDataNull("myAuto is null");
                    else
                        callBack.onDataGot(home.myAuto);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callBack.onDataNull("resultCode is " + statusCode);
            }
        });
    }

    /**
     * 服务顾问
     *
     * @param callBack
     */
    public void getServiceAdvisers(String shopID,final LoadDataCallBack<ArrayList<ServiceAdviser>> callBack) {
        client = getClient();
//        onMaintenanceListener = getOnMaintenanceListener();
        client.serviceAdvisers(shopID, new BaseMallJsonHttpResponseHandler(context) {
            @Override
            public void onSuccess(String response) {
                ArrayList<ServiceAdviser> serviceAdvisers = JSONUtils.fromJson(response, new TypeToken<ArrayList<ServiceAdviser>>() {
                });
                if (serviceAdvisers == null || serviceAdvisers.size() == 0)
                    callBack.onDataNull("");
                else
                    callBack.onDataGot(serviceAdvisers);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callBack.onDataNull("");
            }
        });
    }

    /**
     * 技师
     *
     * @param callBack
     */
    public void getMechanic(String shopID,final LoadDataCallBack<ArrayList<Mechanic>> callBack) {
        client = getClient();
//        onMaintenanceListener = getOnMaintenanceListener();
        client.mechanics(shopID, new BaseMallJsonHttpResponseHandler(context) {
            @Override
            public void onSuccess(String response) {
                ArrayList<Mechanic> mechanics = JSONUtils.fromJson(response, new TypeToken<ArrayList<Mechanic>>() {
                });
                if (mechanics == null || mechanics.size() == 0)
                    callBack.onDataNull("");
                else
                    callBack.onDataGot(mechanics);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callBack.onDataNull("");
            }
        });
    }


    public void getPackageList(String shopID, String autoModelID,
                               OtherMaintenancePackageListAdapter.Type type,
                               final LoadDataCallBack<ArrayList<MaintenancePackage>> callBack) {
        client = getClient();
        String typeStr = type == OtherMaintenancePackageListAdapter.Type.BASE ? "20" : "10";
        client.packageList(shopID, autoModelID, typeStr,
                new BaseMallJsonHttpResponseHandler(context) {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        com.hxqc.mall.core.model.Error error = JSONUtils.fromJson(responseString, Error.class);
                        if (error != null)
                            callBack.onDataNull(error.message);
                        else callBack.onDataNull("");
                    }

                    @Override
                    public void onSuccess(String responseString) {
                        ArrayList<MaintenancePackage> packages = JSONUtils.fromJson(responseString,
                                new TypeToken<ArrayList<MaintenancePackage>>() {
                                });
                        if (packages != null)
                            callBack.onDataGot(packages);
                        else callBack.onDataNull("数据为空");
                    }
                });
    }
}
