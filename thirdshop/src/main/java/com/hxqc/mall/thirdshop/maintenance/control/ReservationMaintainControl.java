package com.hxqc.mall.thirdshop.maintenance.control;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.maintenance.api.ReservationMaintainClient;
import com.hxqc.mall.thirdshop.maintenance.model.Mechanic;
import com.hxqc.mall.thirdshop.maintenance.model.ServiceAdviser;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ReservationMaintainInfo;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ServiceType;
import com.hxqc.mall.thirdshop.model.ThirdPartShop;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import org.apache.http.conn.HttpHostConnectException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 21
 * FIXME
 * Todo 在线预约控制器
 */
public class ReservationMaintainControl {

    private ReservationMaintainClient mReservationMaintainClient;

    private static ReservationMaintainControl ourInstance;

    public static ReservationMaintainControl getInstance() {
        if (ourInstance == null) {
            synchronized (ReservationMaintainControl.class) {
                if (ourInstance == null) {
                    ourInstance = new ReservationMaintainControl();
                }
            }
        }
        return ourInstance;
    }

    private ReservationMaintainControl() {
        this.mReservationMaintainClient = new ReservationMaintainClient();
    }

    public void killInstance() {
        if (mReservationMaintainClient != null) {
            mReservationMaintainClient = null;
        }
        if (ourInstance != null) {
            ourInstance = null;
        }
    }

    /**
     * 在线预约维修请求
     *
     * @param context
     * @param shopID
     * @param callBack
     */
    public void requestReservationMaintain(Context context, String shopID, @NonNull final CallBackControl.CallBack<ReservationMaintainInfo> callBack) {
        mReservationMaintainClient.requestReservationMaintain(shopID, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                ReservationMaintainInfo tMyReservationMaintainInfo = JSONUtils.fromJson(response, new TypeToken<ReservationMaintainInfo>() {
                });
                callBack.onSuccess(tMyReservationMaintainInfo);
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
     * 在线预约维修提交
     *
     * @param context
     * @param plateNumber
     * @param autoModel
     * @param drivingDistance
     * @param name
     * @param phone
     * @param shopID
     * @param apppintmentDate
     * @param serviceType
     * @param serviceAdviserID
     * @param mechanicID
     * @param VIN
     * @param callBack
     */
    public void postReservationMaintain(Context context, String plateNumber, String autoModel, String drivingDistance, String name, String phone, String shopID, String shopType, String apppintmentDate, String serviceType, String serviceAdviserID, String mechanicID, String VIN, @NonNull final CallBackControl.CallBack<String> callBack) {
        mReservationMaintainClient.postReservationMaintain(plateNumber, autoModel, drivingDistance, name, phone, shopID, shopType, apppintmentDate, serviceType, serviceAdviserID, mechanicID, VIN, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                DebugLog.i(TAG, response);
                callBack.onSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                DebugLog.i(TAG, statusCode + "----" + responseString + "-----" + throwable.toString());
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
     * 在线预约维修提交
     *
     * @param context
     * @param plateNumber
     * @param autoModel
     * @param drivingDistance
     * @param name
     * @param phone
     * @param shopID
     * @param apppintmentDate
     * @param serviceType
     * @param serviceAdviserID
     * @param mechanicID
     * @param VIN
     * @param callBack
     */
    public void postReservationMaintain(Context context, String plateNumber, String autoModel, String drivingDistance, String name, String phone, String shopID, String shopType, String apppintmentDate, String serviceType, String serviceAdviserID, String mechanicID, String VIN, String remark, @NonNull final CallBackControl.CallBack<String> callBack) {
        mReservationMaintainClient.postReservationMaintain(plateNumber, autoModel, drivingDistance, name, phone, shopID, shopType, apppintmentDate, serviceType, serviceAdviserID, mechanicID, VIN, remark, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                DebugLog.i(TAG, response);
                callBack.onSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                DebugLog.i(TAG, statusCode + "----" + responseString + "-----" + throwable.toString());
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
     * 服务类型请求
     *
     * @param context
     * @param shopID
     */
    public void requestServiceType(Context context, String shopID, @NonNull final CallBackControl.CallBack<ArrayList<ServiceType>> callBack) {
        mReservationMaintainClient.requestServiceType(shopID, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                ArrayList<ServiceType> tMyServiceType = JSONUtils.fromJson(response, new TypeToken<ArrayList<ServiceType>>() {
                });
                callBack.onSuccess(tMyServiceType);
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
     * 服务类型介绍请求
     *
     * @param context
     * @param shopID
     */
    public void requestServiceTypeIntroduce(Context context, String shopID, String itemID, @NonNull final CallBackControl.CallBack<String> callBack) {
        mReservationMaintainClient.requestServiceType(shopID, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                callBack.onSuccess(response);
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
     * 三方店铺
     *
     * @param context
     * @param shopID
     * @param callBack
     */
    public void requestThirdpartshop(Context context, String shopID, @NonNull final CallBackControl.CallBack<ThirdPartShop> callBack) {
        new ThirdPartShopClient().shopInfo(shopID, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                ThirdPartShop thirdPartShop = JSONUtils.fromJson(response, new TypeToken<ThirdPartShop>() {
                });
                callBack.onSuccess(thirdPartShop);
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
     * 服务顾问
     *
     * @param context
     * @param shopID
     * @param callBack
     */
    public void requestServiceAdviser(Context context, String shopID, @NonNull final CallBackControl.CallBack<ArrayList<ServiceAdviser>> callBack) {
        mReservationMaintainClient.requestServiceAdviser(shopID, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                ArrayList<ServiceAdviser> serviceAdvisers = JSONUtils.fromJson(response, new TypeToken<ArrayList<ServiceAdviser>>() {
                });
                callBack.onSuccess(serviceAdvisers);
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
     * 技师
     *
     * @param context
     * @param shopID
     * @param callBack
     */
    public void requestMechanic(Context context, String shopID, @NonNull final CallBackControl.CallBack<ArrayList<Mechanic>> callBack) {
        mReservationMaintainClient.requestMechanic(shopID, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                ArrayList<Mechanic> mechanics = JSONUtils.fromJson(response, new TypeToken<ArrayList<Mechanic>>() {
                });
                callBack.onSuccess(mechanics);
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
