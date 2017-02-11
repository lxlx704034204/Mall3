package com.hxqc.mall.qr.manager;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.views.dialog.NoCancelDialog;
import com.hxqc.mall.qr.api.HomeQRApiClient;
import com.hxqc.mall.qr.inter.OnFinishScanResultListener;
import com.hxqc.mall.qr.inter.OnQRApiRequestListener;
import com.hxqc.mall.qr.model.ScanResultModel;
import com.hxqc.mall.qr.util.QRActivitySwitch;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

/**
 * Author:  wh
 * Date:  2016/11/11
 * FIXME
 * Todo   扫码调起 操作类
 */

public class ScanQRManager {

    private static ScanQRManager mInstance;

    private OnFinishScanResultListener onFinishScanResultListener;

    private String responseDataStr = "";
    private boolean isFetchSuccess = false;
    private String tempScanStr = "";

    private ScanQRManager() {
    }

    public static ScanQRManager getInstance() {
        if (mInstance == null) {
            synchronized (ScanQRManager.class) {
                if (mInstance == null) {
                    mInstance = new ScanQRManager();
                }
            }
        }
        return mInstance;
    }

    public void onDestroy() {

        responseDataStr = "";

        if (onFinishScanResultListener != null) {
            onFinishScanResultListener = null;
        }

        if (mInstance != null) {
            mInstance = null;
        }
    }

    //开始扫描
    public void startToScan(Context mC, OnFinishScanResultListener listener) {
        DebugLog.w("scan_code", "startToScan");
        this.onFinishScanResultListener = listener;
        QRActivitySwitch.toQRTransferPage(mC);
    }

    //请求操作
    public void qrFetch(final Activity mA, String scanForRequestStr, final OnQRApiRequestListener listener) {
        tempScanStr = scanForRequestStr;
        //请求
        HomeQRApiClient apiClient = new HomeQRApiClient();
        apiClient.getOfflinePayInfoByQR(scanForRequestStr, new LoadingAnimResponseHandler(mA, true, 3) {
            @Override
            public void onSuccess(String response) {
                responseDataStr = response;
                isFetchSuccess = true;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                responseDataStr = "";
                isFetchSuccess = false;
            }

            @Override
            public void onFinish() {
                super.onFinish();
                qrResponseOperate(mA, listener);
            }
        });
    }

    private void qrResponseOperate(Activity mA, OnQRApiRequestListener listener) {
        if (isFetchSuccess) {
            if (listener != null) {
                ScanResultModel model = JSONUtils.fromJson(responseDataStr, new TypeToken<ScanResultModel>() {
                });
                if (model != null && !TextUtils.isEmpty(model.valueType)) {
                    successResponseOperate(mA, listener, model);
                } else {
                    listener.onQRRequestFail("");
                }
            }
        } else {
            if (listener != null) {
                listener.onQRRequestFail("");
            }
        }
    }

    private void successResponseOperate(Activity mA, OnQRApiRequestListener listener, ScanResultModel model) {
        if (model.valueType.equals("-10")) {
            showDialog(listener, mA, "", tempScanStr);
        } else if (model.valueType.equals("10")) {
            if (TextUtils.isEmpty(model.offlineWorkOrder.amountPayable)) {
                showDialog(listener, mA, "", "获取信息失败,请重试");
            } else {
                if ("0".equals(model.offlineWorkOrder.amountPayable)) {
                    showDialog(listener, mA, "", "该结算单已付款完成");
                } else {
                    successToListener(listener);
                }
            }
        }

        tempScanStr = "";
    }

    private void successToListener(OnQRApiRequestListener listener) {
        listener.onQRRequestSuccess();
        if (onFinishScanResultListener != null) {
            onFinishScanResultListener.onSendScanResultSuccess(responseDataStr);
        }
    }

    private void showDialog(final OnQRApiRequestListener listener, Activity mA, String title, String content) {
        String t = TextUtils.isEmpty(title) ? "提示" : title;
        String c = TextUtils.isEmpty(content) ? "扫码失败" : content;
        new NoCancelDialog(mA, t, c) {
            @Override
            protected void doNext() {
                DebugLog.w("scan_code", "扫码失败    重置扫码");
                if (listener != null)
                    listener.onQRRequestFail("");
            }
        }.show();
    }
}
