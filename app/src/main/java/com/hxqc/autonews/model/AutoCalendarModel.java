package com.hxqc.autonews.model;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.hxqc.autonews.api.AutoInformationApiClient;
import com.hxqc.autonews.model.pojos.AutoCalendar;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.interfaces.LoadDataCallBack;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Author:李烽
 * Date:2016-11-22
 * FIXME
 * Todo 新车日历的数据
 */

public class AutoCalendarModel {
    public AutoCalendarModel(AutoInformationApiClient client, Context mContext) {
        this.client = client;

        this.mContext = mContext;
    }

    private AutoInformationApiClient client;
    private Context mContext;

    public void autoCalendarYears(final LoadDataCallBack<ArrayList<String>> callBack) {
        client.getAutoCalendarYears(new LoadingAnimResponseHandler(mContext, true) {
            @Override
            public void onSuccess(String response) {
                ArrayList<String> years = JSONUtils.fromJson(response, new TypeToken<ArrayList<String>>() {
                });
                callBack.onDataGot(years);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                callBack.onDataNull(responseString);
            }
        });
    }

    public void autoCalendar(String year, final LoadDataCallBack<ArrayList<AutoCalendar>> callBack) {

        client.getAutoCalendar(year, new LoadingAnimResponseHandler(mContext, true) {
            @Override
            public void onSuccess(String response) {
                ArrayList<AutoCalendar> autoCalendars = JSONUtils.fromJson(response, new TypeToken<ArrayList<AutoCalendar>>() {
                });
                callBack.onDataGot(autoCalendars);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                callBack.onDataNull(responseString);
            }
        });
    }
}
