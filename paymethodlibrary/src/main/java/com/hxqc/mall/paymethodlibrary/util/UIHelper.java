package com.hxqc.mall.paymethodlibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hxqc.mall.paymethodlibrary.alipay.PayDemoActivity;
import com.hxqc.mall.paymethodlibrary.yeepay.YeePayActivity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Author: wanghao
 * Date: 2015-04-02
 * FIXME
 * 数据传输跳转
 */
public class UIHelper {
    /**
     * 易宝支付
     */
    public static void toYeePay(String url, Context context) {
        Intent intent = new Intent(context, YeePayActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);

    }

    /**
     * 支付宝
     */
    public static void toAlipay( String url, String money, Context context) {
        Intent intent = new Intent(context, PayDemoActivity.class);
        intent.putExtra("money_", money);
        intent.putExtra("url", url);
        context.startActivity(intent);

    }


    /**
     * 微信支付
     */
    public static void toWeChatPay(Context context, PayReq req) throws Exception{
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
        msgApi.registerApp(PayKeyConstants.getWxAppId());
        msgApi.sendReq(req);
    }

    /**
     * 易极付
     */
    public static void toMicroPay(Activity activity, String url) throws Exception{
//        Map< String, String > yijiPayData = MoneyUtil.getPayData(url);
//
//        String server_url = PayKeyConstants.getYjfServerUrl();
//        String partner_id = PayKeyConstants.getYjfPartnerId();
//        String securityId = PayKeyConstants.getYjfSecurityId();
//
//        SdkClient.mContext = activity;
//        ResLoader.init(activity);
//        SdkClient.SERVER_URL = server_url;
//        SdkClient.SECURITY_KEY = securityId;
//        SdkClient.PARTNER_ID_VAL = partner_id;
//
//        if (yijiPayData != null) {
//
//            String memberID = yijiPayData.get(MoneyUtil.MEMBER_ID);
//            String tradeNo = yijiPayData.get(MoneyUtil.TRADE_NO);
//
//            if (TextUtils.isEmpty(memberID) || TextUtils.isEmpty(tradeNo)) {
//                try {
//                    Toast.makeText(activity, "获取参数失败", Toast.LENGTH_SHORT).show();
//                    Log.i("yijipay", "获取参数失败");
//                } catch (Exception e) {
//                    Log.e("yijipay", "获取参数失败");
//                }
//            } else {
//                /**
//                 * TODO  易极付 请求服务器 获取交易号  然后打开支付页面
//                 */
//                YiJiPayPlugin.startPay(activity,
//                        PayConstant.YJF_PAY_REQUEST_CODE, partner_id, partner_id, tradeNo,
//                        /*outBizNo,*/ securityId, memberID);
//            }
//
//        } else {
//            try {
//                Toast.makeText(activity, "创建订单失败", Toast.LENGTH_SHORT).show();
//                Log.i("yijipay", "创建订单失败");
//            } catch (Exception e) {
//                Log.e("yijipay", "创建订单失败");
//            }
//        }

    }

    /**
     * 测试易极付
     */
    public static void testYJF(Activity activity) {
//        String server_url = "https://openapi.yijifu.net/gateway";
//        String partner_id = "20141226020000099880";
//        String securityId = "d588dd8f67237dfb81656a6ba3757d08";
//
//        SdkClient.mContext = activity;
//        ResLoader.init(activity);
//        SdkClient.YJF_SERVER_URL = server_url;
//        SdkClient.YJF_SECURITY_KEY = securityId;
//        SdkClient.YJF_PARTNER_ID_VAL = partner_id;
//
//        String memberID = "13339997910";
//        String tradeNo = "20150911000080847195";
//        /**
//         * TODO  易极付 请求服务器 获取交易号  然后打开支付页面
//         */
//        YiJiPayPlugin.startPay(activity,
//                PayConstant.YJF_PAY_REQUEST_CODE, partner_id, partner_id, tradeNo,
//                        /*outBizNo,*/ securityId, memberID);

    }
}
