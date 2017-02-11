package com.hxqc.mall.payment.model;

import com.hxqc.mall.paymethodlibrary.util.PayConstant;

/**
 * Author: wanghao
 * Date: 2015-04-01
 * FIXME
 * 付款方式
 */
public class PaymentMethod {

    public String paymentID;
    public String thumb;
    public String title;
    public boolean selected;

    public boolean isValid(Float amount, String balance) {
        Float b;
        try {
            b = Float.parseFloat(balance);
        } catch (Exception e) {
            b = 0f;
        }
        if (paymentID.equals(PayConstant.BALANCE)) {
            return b >= amount;
        } else {
            return amount != 0f;
        }
    }
}
