package com.hxqc.mall.paymethodlibrary.inter;

import com.hxqc.mall.paymethodlibrary.model.EventGetSuccessModel;

/**
 * Author: wanghao
 * Date: 2016-03-21
 * FIXME
 * Todo     统一 支付回调管理类方法
 */
public interface OnPayFinishCallBackListening {
    void onPayCallBack(EventGetSuccessModel model);
}
