package com.hxqc.pay.model;

/**
 * Author: wanghao
 * Date: 2015-04-08
 * FIXME
 *  支付后 返回的 data
 */
public class PayPartBackDataModel {

    // pay online__===::{"error_code":0,"message":"支付成功","amount":"1.00","fullname":"啊啊","username":"13339997910"}--
    public int error_code;
    public String message;
    public String amount;
    public String fullname;
    public String username;
    public String bank_title;
    public String bank_id;

    //0  未支付   1 已支付
    public int pay_status_flag;

    @Override
    public String toString() {
        return "PayPartBackDataModel{" +
                "error_code=" + error_code +
                ", message='" + message + '\'' +
                ", amount='" + amount + '\'' +
                ", fullname='" + fullname + '\'' +
                ", username='" + username + '\'' +
                ", bank_title='" + bank_title + '\'' +
                ", bank_id='" + bank_id + '\'' +
                ", pay_status_flag=" + pay_status_flag +
                '}';
    }

    public PayPartBackDataModel(int pay_status_flag) {
        this.pay_status_flag = pay_status_flag;
    }
}
