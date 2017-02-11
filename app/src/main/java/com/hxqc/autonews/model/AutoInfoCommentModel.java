package com.hxqc.autonews.model;

import android.content.Context;

import com.hxqc.autonews.api.AutoInformationApiClient;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.interfaces.LoadDataCallBack;
import com.hxqc.mall.core.model.Error;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

/**
 * Author:李烽
 * Date:2016-10-25
 * FIXME
 * Todo 汽车资讯的评价数据
 */

public class AutoInfoCommentModel {
    private AutoInformationApiClient client;
    private Context mContext;

    public AutoInfoCommentModel(Context context) {
        mContext = context;
        this.client = new AutoInformationApiClient();
    }

    public void sendComment(String infoID, String content, final LoadDataCallBack<Error> callBack) {
        client.sendAutoInfoComment(infoID, content, new LoadingAnimResponseHandler(mContext, true) {
            @Override
            public void onSuccess(String response) {
                Error error = JSONUtils.fromJson(response, Error.class);
                callBack.onDataGot(error);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                callBack.onDataNull(responseString);
            }
        });
    }
}
