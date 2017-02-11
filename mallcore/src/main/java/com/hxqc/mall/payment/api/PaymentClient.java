package com.hxqc.mall.payment.api;

import com.hxqc.mall.core.api.UserApiClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Author:胡仲俊
 * Date: 2016 - 05 - 18
 * FIXME
 * Todo 洗车，违章，年审，换证支付API
 */
public class PaymentClient extends UserApiClient {

    /**
     * 付款方式
     *
     * @param handler
     */
    public void getPayment(String orderID,AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Payment/listPayment");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        gGetUrl(url,requestParams, handler);
    }

    /**
     * 付款
     *
     * @param orderID
     * @param paymentID
     * @param money
     * @param handler
     */
    public void postPay(String orderID, String paymentID, String money, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Payment/pay");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        requestParams.put("paymentID", paymentID);
        requestParams.put("money", money);
        gPostUrl(url, requestParams, handler);
    }

    /**
     * 余额支付
     *
     * @param orderID
     * @param paymentID
     * @param password
     * @param handler
     */
    public void postBlancePay(String orderID, String paymentID, String password, AsyncHttpResponseHandler handler) {
        String url = completeUrl("/Payment/blance");
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderID", orderID);
        requestParams.put("paymentID", paymentID);
        requestParams.put("password", password);
        gPostUrl(url, requestParams, handler);
    }
}
