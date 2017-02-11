package com.hxqc.mall.qr.inter;

/**
 * Author:  wh
 * Date:  2016/11/16
 * FIXME
 * Todo  扫码请求 成功  失败回调
 */

public interface OnQRApiRequestListener {
    void onQRRequestSuccess();
    void onQRRequestFail(String  eMsg);
}
