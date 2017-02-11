package com.hxqc.mall.reactnative.nativemodule;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.hxqc.mall.qr.inter.OnFinishScanResultListener;
import com.hxqc.mall.qr.manager.ScanQRManager;
import com.hxqc.qr.util.QRAfterOperator;
import com.hxqc.util.DebugLog;

/**
 * Author:  wh
 * Date:  2016/11/11
 * FIXME
 * Todo     点击扫码图标操作
 */

public class ClickForScanCode extends ReactContextBaseJavaModule {

    private ReactContext rc;

    public ClickForScanCode(ReactApplicationContext reactContext) {
        super(reactContext);
        rc = reactContext;
    }

    @Override
    public String getName() {
        return "TDCodeModule";
    }

    //打开扫码
    @ReactMethod
    public void clickToScan() {
        DebugLog.w("scan_code", "点击打开扫码");
        ScanQRManager.getInstance().startToScan(rc, new OnFinishScanResultListener() {
            @Override
            public void onSendScanResultSuccess(String responseJsonStr) {
                DebugLog.w("scan_code", "扫码请求获取数据成功: "+responseJsonStr);
                QRAfterOperator afterOperator = new QRAfterOperator(responseJsonStr,rc);
                afterOperator.doOperator();
                ScanQRManager.getInstance().onDestroy();
            }

            @Override
            public void onSendScanResultFail() {
                DebugLog.w("scan_code", "扫码请求失败");
            }
        });
    }

}
