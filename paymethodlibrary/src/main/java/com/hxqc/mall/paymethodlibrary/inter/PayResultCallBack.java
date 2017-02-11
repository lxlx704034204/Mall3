package com.hxqc.mall.paymethodlibrary.inter;

/**
 * Author: wanghao
 * Date: 2016-03-21
 * FIXME
 * Todo
 */


import com.hxqc.mall.paymethodlibrary.model.EventGetSuccessModel;

/**
 *  if (event.getPay_status() == PayCallBackTag.PAY_SUCCESS) {
 ToastHelper.showOrangeToast(SalesDepositPayActivity.this, "支付成功");
 ActivitySwitcherThirdPartShop.finishPay(SalesDepositPayActivity.this);
 finish();
 } else if (event.getPay_status() == PayCallBackTag.PAY_FAIL) {
 ToastHelper.showOrangeToast(SalesDepositPayActivity.this, "支付失败");
 } else if (event.getPay_status() == PayCallBackTag.PAY_CANCEL) {
 ToastHelper.showOrangeToast(SalesDepositPayActivity.this, "交易取消");
 } else if (event.getPay_status() == PayCallBackTag.PAY_EXCEPTION) {
 ToastHelper.showOrangeToast(SalesDepositPayActivity.this, "数据异常");
 } else if (event.getPay_status() == PayCallBackTag.PAY_PROGRESSING) {
 ToastHelper.showYellowToast(SalesDepositPayActivity.this, "支付结果确认中");
 }
 */
public interface PayResultCallBack {
    void paySuccess(EventGetSuccessModel model);
    void payFail(EventGetSuccessModel model);
    void payCancel(EventGetSuccessModel model);
    void payException(EventGetSuccessModel model);
    void payProgressing(EventGetSuccessModel model);
}
