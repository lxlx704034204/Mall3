package com.hxqc.mall.control;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.interfaces.LoadDataCallBack;
import com.hxqc.mall.core.model.AmountConfig;
import com.hxqc.mall.core.model.Error;
import com.hxqc.mall.core.model.recharge.PrepaidHistory;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-03-18
 * FIXME
 * Todo 充值帮助类
 */
public class RechargeHelper {
    private static RechargeHelper instance;
    private UserApiClient client;
    private Context context;

    private RechargeHelper(Context context) {
        this.context = context.getApplicationContext();
    }

    public synchronized static RechargeHelper getIntance(Context context) {
        if (instance == null)
            synchronized (RechargeHelper.class) {
                if (instance == null)
                    instance = new RechargeHelper(context);
            }
        return instance;
    }

    private UserApiClient getClient() {
        if (client == null)
            client = new UserApiClient();
        return client;
    }
    /**
     * 销毁
     */
    public void destory() {
        if (instance != null)
            instance = null;
    }
    /**
     * 生成订单
     *
     * @param phoneNumber
     * @param money
     * @param callBack
     */
    public void createOrder(String phoneNumber, String money, final LoadDataCallBack<String> callBack) {
        client = getClient();
        client.orderCreat(phoneNumber, money, new BaseMallJsonHttpResponseHandler(context) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                com.hxqc.mall.core.model.Error error = JSONUtils.fromJson(responseString, new TypeToken<Error>() {
                });
                if (error != null && !TextUtils.isEmpty(error.message))
                    callBack.onDataNull(error.message);
            }

            @Override
            public void onSuccess(String responseString) {
                try {
                    JSONObject object = new JSONObject(responseString);
                    String orderID = object.getString("orderID");
                    callBack.onDataGot(orderID);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onDataNull("");
                }
            }
        });
    }

    /**
     * 加载记录
     *
     * @param callBack
     */
    public void loadRecord(final LoadDataCallBack<ArrayList<PrepaidHistory>> callBack) {
        client = getClient();
        client.rechargeHistory(new BaseMallJsonHttpResponseHandler(context) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callBack.onDataNull("");
            }

            @Override
            public void onSuccess(String responseString) {
                ArrayList<PrepaidHistory> prepaidHistories
                        = JSONUtils.fromJson(responseString, new TypeToken<ArrayList<PrepaidHistory>>() {
                });
                if (prepaidHistories != null)
                    callBack.onDataGot(prepaidHistories);
                else callBack.onDataNull("");
            }
        });
    }

    /**
     * 获取充值配置
     *
     * @param callBack
     */
    public void loadAmountConfig(final LoadDataCallBack<AmountConfig> callBack) {
        client = getClient();
        client.amountConfig(new BaseMallJsonHttpResponseHandler(context) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callBack.onDataNull("");
            }

            @Override
            public void onSuccess( String responseString) {
                AmountConfig amountConfig = JSONUtils.fromJson(responseString, new TypeToken<AmountConfig>() {
                });
                if (amountConfig != null)
                    callBack.onDataGot(amountConfig);
                else callBack.onDataNull("");
            }
        });
    }

    /**
     * 获取可兑换的积分
     *
     * @param number
     * @param callBack
     */
    public void canGetScore(String number, final LoadDataCallBack<Integer> callBack) {
        client = getClient();
        client.score(number, new BaseMallJsonHttpResponseHandler(context) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                callBack.onDataNull("");
            }

            @Override
            public void onSuccess(String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    int score = jsonObject.getInt("score");
                    callBack.onDataGot(score);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onDataNull("");
                }
            }
        });
    }
}
