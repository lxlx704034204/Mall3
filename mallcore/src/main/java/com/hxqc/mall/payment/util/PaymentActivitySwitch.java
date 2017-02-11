package com.hxqc.mall.payment.util;

import android.content.Context;
import android.os.Bundle;

import com.hxqc.mall.core.util.ActivitySwitchBase;

/**
 * Author:胡俊杰
 * Date: 2016/6/21
 * FIXME
 * Todo
 */
public class PaymentActivitySwitch extends ActivitySwitchBase {

    /**
     * 周边支付页面
     *
     * @param context
     * @param money
     * @param orderID
     * @param typeChoose OrderDetailContants中选择ILLEGAL_AND_COMMISSION或CAR_WASH传入
     */
    public static void toAroundPaymentActivity(Context context, String money, String orderID, int typeChoose) {

        Bundle bundle = new Bundle();
        bundle.putString("money", money);
        bundle.putString("orderID", orderID);
        bundle.putInt("typeChoose", typeChoose);
        toWhere(context,"com.hxqc.aroundservice.activity.PeripheralServicesPayActivity",bundle);
    }

    /**
     * 周边支付页面
     *
     * @param context
     * @param money
     * @param orderID
     * @param typeChoose OrderDetailContants中选择ILLEGAL_AND_COMMISSION或CAR_WASH传入
     */
    public static void toAroundPaymentActivity(Context context, String money, String orderID, int typeChoose,String exemption) {

        Bundle bundle = new Bundle();
        bundle.putString("money", money);
        bundle.putString("orderID", orderID);
        bundle.putInt("typeChoose", typeChoose);
        bundle.putString("exemption",exemption);
        toWhere(context,"com.hxqc.aroundservice.activity.PeripheralServicesPayActivity",bundle);
    }


    /**
     * 洗车付款完成页面
     */
    public static void toWashCarPayFinish(Context context ,String orderID){
        Bundle bundle = new Bundle();
        bundle.putString("orderID",orderID);
        toWhere(context,"com.hxqc.fastreqair.activity.WashCarPayFinishActivity",bundle);
    }
}
