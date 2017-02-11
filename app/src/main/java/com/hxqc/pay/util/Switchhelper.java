package com.hxqc.pay.util;

import android.content.Context;
import android.content.Intent;

import com.hxqc.mall.core.model.OrderPayRequest;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.pay.activity.DepositActivity;
import com.hxqc.pay.activity.PayMainActivity;
import com.hxqc.pay.activity.PaySpareMoneyActivity;
import com.hxqc.pay.activity.PickCarHelpActivity;
import com.hxqc.pay.activity.PickupPlaceActivity;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2015-04-03
 * FIXME
 */
public class Switchhelper extends ActivitySwitchBase{


//    /**
//     * 上传分期资料   如果code值为 10 或 26 或 27，就只可查看资料二不能修改
//     */
//    public static void toUploadInfo(String orderStatus, String order_id,Activity context){
//        DebugLog.i("test_upload", "toUploadInfo: " + order_id);
//        Intent intent = new Intent(context, UploadInfoActivity.class);
//        intent.putExtra(ConstantValue.PAY_MAIN_ORDER_ID, order_id);
//        intent.putExtra(ConstantValue.ORDER_STATUS, orderStatus);
//        context.startActivity(intent);
//    }
//
//    public static void toUploadInfo(String order_id,Activity context,String flag){
//        DebugLog.i("test_upload", "toUploadInfo: " + order_id);
//        Intent intent = new Intent(context, UploadInfoActivity.class);
//        intent.putExtra(ConstantValue.PAY_MAIN_ORDER_ID, order_id);
//        intent.putExtra(ConstantValue.SWITCH_FROM_STEP_4, flag);
//        context.startActivity(intent);
//    }

    /**
     * 普通支付
     */
    public static void toPayNormal(int pay_type, OrderPayRequest request, Context context) {
        request.isSeckill = "0";
        Intent intent = new Intent(context, PayMainActivity.class);
        intent.putExtra(ConstantValue.ORDER_PAY_REQUEST, request);
        intent.putExtra(PayConstant.PAY_STATUS_FLAG, pay_type);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    /**
     * 特卖支付
     */
    public static void toPaySeckill(int pay_type, String order_id, Context context) {
        Intent intent = new Intent(context, DepositActivity.class);
        intent.putExtra(ConstantValue.PAY_MAIN_ORDER_ID, order_id);
        intent.putExtra(PayConstant.PAY_STATUS_FLAG, pay_type);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 特卖跳转到 签订合同
     */
    public static void toContinueSale(int flag, String order_id, Context context) {
        Intent intent = new Intent(context, PayMainActivity.class);
        intent.putExtra(ConstantValue.PAY_MAIN_ORDER_ID, order_id);
        intent.putExtra(PayConstant.PAY_STATUS_FLAG, flag);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 自提点界面
     */
    public static void toPickupPoint(String orderID,String itemID,String province,String pid, String cid, Context context) {
        Intent intent = new Intent(context, PickupPlaceActivity.class);
        intent.putExtra("orderID",orderID);
        intent.putExtra("itemID",itemID);
        intent.putExtra("pid", pid);
        intent.putExtra("cid", cid);
        intent.putExtra("province", province);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    /**
     * 自提点界面
     */
    public static void toPickupPoint(ArrayList<PickupPointT > pickupPoints, Context context) {
        Intent intent = new Intent(context, PickupPlaceActivity.class);
//        intent.putExtra(KEY_DATA, JSONUtils.toJson(pickupPoints));
        intent.putParcelableArrayListExtra(KEY_DATA,pickupPoints);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     *完成界面 继续支付
     */
    public static void toContinuePay(String order_id,Context context){
        Intent intent = new Intent(context, PaySpareMoneyActivity.class);
        intent.putExtra("order_id",order_id);
        intent.putExtra(PayConstant.PAY_STATUS_FLAG, PayConstant.PAY_ONLY_ONLINE_PAID);
        context.startActivity(intent);
    }

    /**
     * 如何提车
     */
    public static void pickCarHelp(Context context){
        Intent intent = new Intent(context, PickCarHelpActivity.class);
        context.startActivity(intent);
    }


}
