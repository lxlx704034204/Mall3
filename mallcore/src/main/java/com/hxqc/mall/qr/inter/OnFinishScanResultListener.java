package com.hxqc.mall.qr.inter;

/**
 * Author:  wh
 * Date:  2016/11/16
 * FIXME
 * Todo  获取 扫码请求结果
 */

public interface OnFinishScanResultListener {
    void onSendScanResultSuccess(String responseJsonStr);
    void onSendScanResultFail();
}
