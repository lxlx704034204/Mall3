package com.hxqc.mall.core.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.model.Error;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.HttpHostConnectException;


/**
 * 说明:
 * <p/>
 * author: 吕飞
 * since: 2015-03-23
 * Copyright:恒信汽车电子商务有限公司
 */
public abstract class BaseMallJsonHttpResponseHandler extends TextHttpResponseHandler {
    protected static final String TAG = "Response";
    static final boolean DEBUG = Log.isLoggable(TAG, Log.DEBUG);
    public Error mError;
    protected Context mContext;
    BaseResponseHandleDialogManager dialogManager;
    Boolean showToast = true;


    public BaseMallJsonHttpResponseHandler(Context context) {
        this.mContext = context;
    }
    abstract public void onSuccess(String response);

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        if (!TextUtils.isEmpty(responseString)) {
            DebugLog.i(TAG, JSONUtils.format(responseString));
        }
        onSuccess(responseString);
    }


    public void onOtherFailure(int statusCode, Header[] headers, String responseString, Error error) {
        if (mError == null) {
            ToastHelper.showRedToast(mContext, mContext.getResources().getString(R.string.app_sever_error));
        } else {
            dialogManager = BaseResponseHandleDialogManager.getInstance();
            if (mError.code == 401) {
                //跳转到登陆界面
                dialogManager.initDialog(mContext, "提示", "请求失败，请重新登录！", mError.code);
                dialogManager.showDialog();
            } else if (mError.code == 402) {
                dialogManager.initDialog(mContext, "提示", "您的密码已修改，请重新登录！", mError.code);
                dialogManager.showDialog();
            } else if (mError.code == 404) {
             //   提示:该用户未注册，请先注册
                dialogManager.initDialog(mContext, "提示", "您的账号尚未注册，请先注册", mError.code);
                dialogManager.showDialog();
            } else {
                if(showToast) {
                    if (!TextUtils.isEmpty(mError.message)) {
                        if (!mError.message.contains("token")) {
                            ToastHelper.showRedToast(mContext, mError.message);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        if (DEBUG) {
            DebugLog.i(TAG, responseString);
        }
        if (throwable instanceof HttpHostConnectException)
            ToastHelper.showRedToast(mContext, R.string.app_net_error);
        else if (TextUtils.isEmpty(responseString)) {
            ToastHelper.showRedToast(mContext, R.string.app_sever_error);
        } else {
            mError = JSONUtils.fromJson(responseString, Error.class);
            if (mError == null) {
                ToastHelper.showRedToast(mContext, R.string.app_net_error);
            } else {
                onOtherFailure(statusCode, headers, responseString, mError);
            }
        }
    }

}
