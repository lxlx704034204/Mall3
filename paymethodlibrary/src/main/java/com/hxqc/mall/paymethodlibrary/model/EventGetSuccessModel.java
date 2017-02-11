package com.hxqc.mall.paymethodlibrary.model;

import com.hxqc.mall.paymethodlibrary.util.PayCallBackTag;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.mall.paymethodlibrary.util.PayMethodConstant;

/**
 * Author: wanghao
 * Date: 2015-04-08
 * FIXME
 * 接收支付后回调信息
 */
public class EventGetSuccessModel {
    //原返回码
    private int backCode;

    //信息
    private String message_;

    //付款方式，支付类型
    private int pay_method;

    //支付状态
    private int pay_status;

    public int getBackCode() {
        return backCode;
    }

    public int getPay_status() {
        //易宝
        if (pay_method == PayMethodConstant.YEEPAY_TYPE) {
            if (backCode == PayConstant.YEEPAY_SUCCESS) {
                this.pay_status = PayCallBackTag.PAY_SUCCESS;
            } else {
                this.pay_status = PayCallBackTag.PAY_FAIL;
            }

        }//支付宝
        else if (pay_method == PayMethodConstant.ALIPAY_TYPE) {
            if (backCode == PayConstant.ALIPAY_SUCCESS) {
                this.pay_status = PayCallBackTag.PAY_SUCCESS;
            } else if (backCode == PayConstant.ALIPAY_CONFIRMING) {
                this.pay_status = PayCallBackTag.PAY_PROGRESSING;
            } else if (backCode == PayConstant.ALIPAY_CANCEL) {
                this.pay_status = PayCallBackTag.PAY_CANCEL;
            } else {
                this.pay_status = PayCallBackTag.PAY_FAIL;
            }
        }//易极付
        else if (pay_method == PayMethodConstant.MICROPAY_TYPE) {

            if (backCode == PayConstant.YJF_PAY_SUCCESS) {
                this.pay_status = PayCallBackTag.PAY_SUCCESS;
            } else if (backCode == PayConstant.YJF_TRADE_PROCESS) {
                this.pay_status = PayCallBackTag.PAY_PROGRESSING;
            } else if (backCode == PayConstant.YJF_PAY_CANCEL) {
                this.pay_status = PayCallBackTag.PAY_CANCEL;
            } else if (backCode == PayConstant.YJF_PAY_DATA_EXCEPTION) {
                this.pay_status = PayCallBackTag.PAY_EXCEPTION;
            } else {
                this.pay_status = PayCallBackTag.PAY_FAIL;
            }
        }//微信支付
        else if (pay_method == PayMethodConstant.WECHAT_PAY) {
            if (backCode == PayConstant.WECHAT_SUCCESS){
                this.pay_status = PayCallBackTag.PAY_SUCCESS;
            }else if (backCode == PayConstant.WECHAT_FAIL){
                this.pay_status = PayCallBackTag.PAY_FAIL;
            }else if (backCode == PayConstant.WECHAT_CANCEL){
                this.pay_status = PayCallBackTag.PAY_CANCEL;
            }else {
                this.pay_status = PayCallBackTag.PAY_FAIL;
            }
        }

        return pay_status;
    }

    public EventGetSuccessModel(int backCode, String message_, int pay_method) {
        this.backCode = backCode;
        this.message_ = message_;
        this.pay_method = pay_method;
    }

    @Override
    public String toString() {
        return "EventGetSuccessModel{" +
                "backCode=" + backCode +
                ", message_='" + message_ + '\'' +
                ", pay_method=" + pay_method +
                ", pay_status=" + pay_status +
                '}';
    }
}
