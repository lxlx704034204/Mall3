package com.hxqc.carcompare.api;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by zhaofan on 2016/10/28.
 */
public class CarCompareApiClient extends BaseApiClient {
    @Override
    protected String completeUrl(String control) {
        return ApiUtil.getAutoInfoURL(control);
    }


    public void getCompareData(int type, String compareModels, AsyncHttpResponseHandler handler) {
        String url = "";
        if (type == 0) {
            url = "/AutoCompare/parameter";
        } else if (type == 1) {
            url = "/AutoCompare/autoNews";
        } else if (type == 2) {
            url = "/AutoCompare/autoGrade";
        } else if (type == 3) {
            url = "/AutoCompare/userGrade";
        }
        RequestParams params = new RequestParams();
        params.put("deviceType", "Android");
        params.put("compareModels", compareModels);
        gGetUrl(completeUrl(url), params, handler);
    }


}
