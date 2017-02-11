package com.hxqc.qr.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hxqc.mall.activity.recharge.RechargePayBaseActivity;
import com.hxqc.mall.core.model.recharge.RechargeRequest;
import com.hxqc.mall.qr.util.QRActivitySwitch;
import com.hxqc.mall.qr.model.OffLineWorkOrderQRModel;
import com.hxqc.qr.offlinepay.HomeQRPayChargeActivity;
import com.hxqc.qr.offlinepay.HomeQRPayConfirmActivity;
import com.hxqc.qr.offlinepay.HomeQRPayFinishActivity;
import com.hxqc.util.DebugLog;

/**
 * Author:  wh
 * Date:  2016/11/14
 * FIXME
 * Todo
 */

public class QRActivitySwitchApp extends QRActivitySwitch {
    public static String Scan_pay_tag = "Scan_pay_tag";

    /**
     * 扫码支付确认界面
     */
    public static void toQRPayConfirmPage(OffLineWorkOrderQRModel model, Context ctx) {
        if (model != null)
            DebugLog.i("scan_code", model.toString());
        Intent intent = new Intent(ctx, HomeQRPayConfirmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Scan_pay_tag, model);
        ctx.startActivity(intent);
    }

    /**
     * 扫码支付选择支付方式界面
     */
    public static void toQRRechargePayList(Context context, RechargeRequest rechargeRequest) {
        Intent intent = new Intent(context, HomeQRPayChargeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(RechargePayBaseActivity.DATA_TAG, rechargeRequest);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 扫码支付完成界面
     */
    public static void toQRRechargePayFinish(Context context) {
        Intent intent = new Intent(context, HomeQRPayFinishActivity.class);
        context.startActivity(intent);
    }

}
