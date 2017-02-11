package com.hxqc.aroundservice.control;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.hxqc.aroundservice.api.AroundServiceApiClient;
import com.hxqc.aroundservice.model.AnnualInspection;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import org.apache.http.conn.HttpHostConnectException;

import cz.msebera.android.httpclient.Header;

/**
 * Author:胡仲俊
 * Date: 2016 - 05 - 10
 * Des: 年检控制器
 * FIXME
 * Todo
 */
public class AnnualVehicleControl {

    private static final String TAG = AutoInfoContants.LOG_J;
    private static AnnualVehicleControl imInstance;
    private AroundServiceApiClient imIllegalQueryClient;

    private AnnualVehicleControl() {
        imIllegalQueryClient = new AroundServiceApiClient();
    }

    public static AnnualVehicleControl getInstance() {
        if (imInstance == null) {
            synchronized (AnnualVehicleControl.class) {
                if (imInstance == null) {
                    imInstance = new AnnualVehicleControl();
                }
            }
        }
        return imInstance;
    }

    /**
     * 年检订单详情
     *
     * @param context
     * @param orderID
     * @param callBack
     */
    public void getAnnualnspectionDetail(Context context, String orderID, @NonNull final CallBackControl.CallBack<AnnualInspection> callBack) {
        imIllegalQueryClient.getAnnualnspectionDetail(orderID, new LoadingAnimResponseHandler(context, true, false, "") {
            @Override
            public void onSuccess(String response) {
                AnnualInspection imAnnualInspection = JSONUtils.fromJson(response, new TypeToken<AnnualInspection>() {
                });
                callBack.onSuccess(imAnnualInspection);
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
     * 年检订单修改
     *
     * @param context
     * @param orderID
     * @param drivingLicenseFile1
     * @param drivingLicenseFile2
     * @param callBack
     */
    public void editAnnualnspectionDetail(Context context, String orderID, String drivingLicenseFile1, String drivingLicenseFile2, @NonNull final CallBackControl.CallBack<String> callBack) {
       /* try {
            if (!TextUtils.isEmpty(drivingLicenseFile1)) {
                drivingLicenseFile1 = imIllegalQueryClient.getFilePath(context, drivingLicenseFile1);
            }
            if (!TextUtils.isEmpty(drivingLicenseFile2)) {
                drivingLicenseFile2 = imIllegalQueryClient.getFilePath(context, drivingLicenseFile2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        DebugLog.i(TAG, "drivingLicenseFile1: " + drivingLicenseFile1);
        DebugLog.i(TAG, "drivingLicenseFile2: " + drivingLicenseFile2);
        imIllegalQueryClient.postAnnualnspectionEdit(orderID, drivingLicenseFile1, drivingLicenseFile2, new LoadingAnimResponseHandler(context, true, false, "") {
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
     * 取消年检订单
     *
     * @param context
     * @param orderID
     * @param reason
     * @param callBack
     */
    public void cancelAnnualnspection(Context context, String orderID, String reason, @NonNull final CallBackControl.CallBack<String> callBack) {
        imIllegalQueryClient.deleteAnnualnspectionCancel(orderID, reason, new LoadingAnimResponseHandler(context, true, false, "") {
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
     * 车辆年检订单提交
     *
     * @param context
     * @param plateNumber
     * @param registerDate
     * @param name
     * @param phone
     * @param province
     * @param city
     * @param district
     * @param address
     * @param path1
     * @param path2
     * @param callBack
     */
    public void commitAnnualVehicle(Context context, String plateNumber, String registerDate, String name, String phone,
                                    String province, String city, String district, String address, String path1, String path2, @NonNull final CallBackControl.CallBack<String> callBack) {
        imIllegalQueryClient.postAnnualnspectionSubmit(context, plateNumber, registerDate, name, phone, province, city, district, address, path1, path2, new LoadingAnimResponseHandler(context, true, false) {

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
     * 消除
     */
    public void killInstance() {
        if (imIllegalQueryClient != null) {
            imIllegalQueryClient = null;
        }
        if (imInstance != null) {
            imInstance = null;
        }
    }
}
