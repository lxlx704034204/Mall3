package com.hxqc.aroundservice.control;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.hxqc.aroundservice.api.AroundServiceApiClient;
import com.hxqc.aroundservice.model.Licence;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.util.JSONUtils;

import org.apache.http.conn.HttpHostConnectException;

import cz.msebera.android.httpclient.Header;

/**
 * Author:胡仲俊
 * Date: 2016 - 05 - 10
 * Des: 驾驶证换证控制器
 * FIXME
 * Todo
 */
public class ChangeLicenseControl {

    private static final String TAG = AutoInfoContants.LOG_J;
    private static ChangeLicenseControl imInstance;
    private AroundServiceApiClient imIllegalQueryClient;

    private ChangeLicenseControl() {
        imIllegalQueryClient = new AroundServiceApiClient();
    }

    public static ChangeLicenseControl getInstance() {
        if (imInstance == null) {
            synchronized (ChangeLicenseControl.class) {
                if (imInstance == null) {
                    imInstance = new ChangeLicenseControl();
                }
            }
        }
        return imInstance;
    }

    /**
     * 驾驶证换证订单详情
     *
     * @param context
     * @param orderID
     * @param callBack
     */
    public void getLicenceDetail(Context context, String orderID, @NonNull final CallBackControl.CallBack<Licence> callBack) {

        imIllegalQueryClient.getLicenceDetail(orderID, new LoadingAnimResponseHandler(context, true, false) {
            @Override
            public void onSuccess(String response) {
                Licence imLicence = JSONUtils.fromJson(response, new TypeToken<Licence>() {
                });
                callBack.onSuccess(imLicence);
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
     * 驾驶证订单修改
     *
     * @param context
     * @param orderID
     * @param drivingLicenseFile1
     * @param drivingLicenseFile2
     * @param callBack
     */
    public void editLicenceOrder(Context context, String orderID, String drivingLicenseFile1, String drivingLicenseFile2, String IDCardFile1, String IDCardFile2, @NonNull final CallBackControl.CallBack<String> callBack) {
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
        imIllegalQueryClient.postLicenceEdit(orderID, drivingLicenseFile1, drivingLicenseFile2, IDCardFile1, IDCardFile2, new LoadingAnimResponseHandler(context, true, false) {
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
     * 取消驾驶证换证订单
     *
     * @param context
     * @param orderID
     * @param reason
     * @param callBack
     */
    public void cancelLicenceDetail(Context context, String orderID, String reason, @NonNull final CallBackControl.CallBack<String> callBack) {
        imIllegalQueryClient.deleteLicenceCancel(orderID, reason, new LoadingAnimResponseHandler(context, true, false) {
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
     * 驾驶证换证订单提交
     *
     * @param context
     * @param name
     * @param phone
     * @param province
     * @param city
     * @param district
     * @param address
     * @param path1
     * @param path2
     * @param path3
     * @param path4
     * @param callBack
     */
    public void commitLicence(Context context, String name, String phone, String province, String city, String district, String address,
                              String path1, String path2, String path3, String path4, @NonNull final CallBackControl.CallBack<String> callBack) {
        imIllegalQueryClient.postLicenceSubmit(context, name, phone, province, city, district, address, path1, path2, path3, path4, new LoadingAnimResponseHandler(context, true, false) {
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
