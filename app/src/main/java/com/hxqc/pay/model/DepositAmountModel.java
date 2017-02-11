package com.hxqc.pay.model;

import com.google.gson.annotations.Expose;

/**
 * Author: wanghao
 * Date: 2015-04-02
 * FIXME
 * 订金金额
 */
public class DepositAmountModel {
    @Expose
    public String amount;

    @Override
    public String toString() {
        return "DepositAmountModel{" +
                "amount='" + amount + '\'' +
                '}';
    }
}
