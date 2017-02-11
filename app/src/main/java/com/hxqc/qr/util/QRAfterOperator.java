package com.hxqc.qr.util;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.qr.model.ScanResultModel;
import com.hxqc.util.JSONUtils;

/**
 * Author:  wh
 * Date:  2016/11/16
 * FIXME
 * Todo 扫码后操作
 */

public class QRAfterOperator {
    private Context mC;
    private ScanResultModel model;

    public QRAfterOperator(String responseJson, Context context) {
        this.mC = context;
        model = JSONUtils.fromJson(responseJson, new TypeToken<ScanResultModel>() {
        });
    }

    public void doOperator() {
        if (model == null)
            return;

        if (!TextUtils.isEmpty(model.valueType)) {

            //线下工单
            if (model.valueType.equals("10")) {
                QRActivitySwitchApp.toQRPayConfirmPage(model.offlineWorkOrder, mC);
            }
        }
    }

}
