package com.hxqc.mall.payment.control;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hxqc.mall.payment.api.PaymentClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Author:胡仲俊
 * Date: 2016 - 05 - 18
 * FIXME
 * Todo 洗车，违章，年审，换证支付控制
 */
public class PaymentControl {

    private static final String TAG = "IllegalQueryControl";
    private static PaymentControl ourInstance;
    private PaymentClient imPaymentClient;

    private PaymentControl() {
        imPaymentClient = new PaymentClient();
    }

    public static PaymentControl getInstance() {
        if (ourInstance == null) {
            synchronized (PaymentControl.class) {
                if (ourInstance == null) {
                    ourInstance = new PaymentControl();
                }
            }
        }
        return ourInstance;
    }

    /**
     * 付款方式
     *
     * @param context
     * @param handler
     */
    public void getPayment(Context context,String orderID, @NonNull final AsyncHttpResponseHandler handler) {
        imPaymentClient.getPayment(orderID,handler);
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
        imPaymentClient.postPay(orderID, paymentID, money, handler);
    }

    /**
     * 余额付款
     *
     * @param orderID
     * @param paymentID
     * @param password
     * @param handler
     */
    public void postBlancePay(String orderID, String paymentID, String password, AsyncHttpResponseHandler handler) {
        imPaymentClient.postBlancePay(orderID, paymentID, password, handler);
    }



    /**
     * 清除
     */
    public void killInstance() {
        if (imPaymentClient != null) {
            imPaymentClient = null;
        }

        if (ourInstance != null) {
            ourInstance = null;
        }
    }
}
