package com.hxqc.mall.control;

import android.content.Context;

import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.interfaces.LoadDataCallBack;
import com.hxqc.mall.core.model.Error;
import com.hxqc.mall.core.model.bill.BalanceBill;
import com.hxqc.mall.core.model.bill.ScoreBill;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

/**
 * Author:李烽
 * Date:2016-03-23
 * FIXME
 * Todo 账单列表管理
 */
public class BillDataManager {
    private static BillDataManager instance;
    private UserApiClient apiClient;
    private Context context;

    private BillDataManager(Context context) {
        this.context = context.getApplicationContext();
    }

    private UserApiClient getApiClient() {
        if (apiClient == null)
            apiClient = new UserApiClient();
        return apiClient;
    }

    public synchronized static BillDataManager getInstance(Context context) {
        if (instance == null)
            synchronized (BillDataManager.class) {
                if (instance == null)
                    instance = new BillDataManager(context);
            }

        return instance;
    }

    public void getBalaceBillList(String page, String lm, boolean showAnim, final LoadDataCallBack<BalanceBill> callBack) {
        apiClient = getApiClient();
        apiClient.getBalanceBill(page, lm, new LoadingAnimResponseHandler(context, showAnim) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                com.hxqc.mall.core.model.Error error = JSONUtils.fromJson(responseString, Error.class);
                if (error != null)
                    callBack.onDataNull(error.message);
                else callBack.onDataNull("");
            }

            @Override
            public void onSuccess(String responseString) {
                BalanceBill balanceBill = JSONUtils.fromJson(responseString, BalanceBill.class);
                if (balanceBill != null)
                    callBack.onDataGot(balanceBill);
                else callBack.onDataNull("");
            }
        });
    }

    public void getScoreBillList(String myAutoID,String page, String lm, boolean showAnim,
                                 final LoadDataCallBack<ScoreBill> callBack) {
        apiClient = getApiClient();
        apiClient.getScoreBill(myAutoID,page, lm, new LoadingAnimResponseHandler(context,showAnim) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                com.hxqc.mall.core.model.Error error = JSONUtils.fromJson(responseString, Error.class);
                if (error != null)
                    callBack.onDataNull(error.message);
                else callBack.onDataNull("");
            }

            @Override
            public void onSuccess(String responseString) {
                ScoreBill scoreBill = JSONUtils.fromJson(responseString, ScoreBill.class);
                if (scoreBill != null)
                    callBack.onDataGot(scoreBill);
                else callBack.onDataNull("");
            }
        });
    }

    /**
     * 销毁
     */
    public void destory() {
        if (instance != null)
            instance = null;
    }
}
