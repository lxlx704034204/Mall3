package com.hxqc.mall.qr.api;

import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.BaseApiClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Author:  wh
 * Date:  2016/11/14
 * FIXME
 * Todo
 */

public class HomeQRApiClient extends BaseApiClient{
    @Override
    protected String completeUrl(String control) {
        return ApiUtil.getAccountURL(control);
    }

    /**
     * 获取自提点
     */
    public void getOfflinePayInfoByQR(String scanData,AsyncHttpResponseHandler handler) {
        String url = completeUrl("/QR");
        RequestParams requestParams = new RequestParams();
        requestParams.put("QRValue", scanData);
        gGetUrl(url, requestParams, handler);
    }
}
