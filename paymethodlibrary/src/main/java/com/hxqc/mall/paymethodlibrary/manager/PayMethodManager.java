package com.hxqc.mall.paymethodlibrary.manager;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.hxqc.mall.paymethodlibrary.alipay.PayResult;
import com.hxqc.mall.paymethodlibrary.model.EventGetSuccessModel;
import com.hxqc.mall.paymethodlibrary.model.PayOnlineResponse;
import com.hxqc.mall.paymethodlibrary.util.AlipayResultUtil;
import com.hxqc.mall.paymethodlibrary.util.MoneyUtil;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.mall.paymethodlibrary.util.PayKeyConstants;
import com.hxqc.mall.paymethodlibrary.util.PayMethodConstant;
import com.hxqc.mall.paymethodlibrary.util.UIHelper;
import com.hxqc.mall.paymethodlibrary.wechat.MD5;
import com.hxqc.util.DebugLog;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Author: wanghao
 * Date: 2015-09-22
 * FIXME   支付方式管理
 * Todo
 */
public class PayMethodManager {


//    private String pay_id;

    private String money;

    private PayOnlineResponse data;

    Activity mA;

    IWXAPI msgApi;
    //    Map<String,String> resultunifiedorder;
    StringBuffer sb;
    PayReq req;

    public PayMethodManager(Activity c, String money, PayOnlineResponse data) {
//        this.pay_id = pay_method;
        this.data = data;
        this.mA = c;
        this.money = money;

        if (data != null && PayConstant.WEIXIN.equals(data.paymentID)) {
            msgApi = WXAPIFactory.createWXAPI(mA, null);
            req = new PayReq();
            sb = new StringBuffer();
        }
    }

    public void paySwitch() throws Exception {

        if (data == null) {
            return;
        }

        if (TextUtils.isEmpty(data.paymentID))
            return;


        switch (data.paymentID) {
            case PayConstant.YEEPAY:
                UIHelper.toYeePay(data.url, mA);
                break;

            case PayConstant.ALIPAY:

                if (TextUtils.isEmpty(data.url)){
                    EventBus.getDefault().post(new EventGetSuccessModel(PayConstant.ALIPAY_FAIL, "支付失败", PayMethodConstant.ALIPAY_TYPE));

                    PayCallBackManager
                            .getInstance()
                            .onPayCallBack(new EventGetSuccessModel(PayConstant.ALIPAY_FAIL, "支付失败", PayMethodConstant.ALIPAY_TYPE));

                    break;
                }

//                UIHelper.toAlipay(data.url, money, mA);
                try{
                    DebugLog.i("test_alipay","paySwitch 触发支付宝: " + data.url);
                    alipayPay(data.url);
                }catch (Exception e){
                    DebugLog.i("test_alipay",e.getMessage());
                }

                break;

            case PayConstant.YIJIPAY:
                UIHelper.toMicroPay(mA, data.url);
                break;

            case PayConstant.WEIXIN:
                DebugLog.i("pay_test",data.url);
                if (TextUtils.isEmpty(data.url)){
                    EventBus.getDefault().post(new EventGetSuccessModel(PayConstant.WECHAT_FAIL, "", PayMethodConstant.WECHAT_PAY));

                    PayCallBackManager
                            .getInstance()
                            .onPayCallBack(new EventGetSuccessModel(PayConstant.WECHAT_FAIL, "", PayMethodConstant.WECHAT_PAY));

                    break;
                }

                msgApi.registerApp(PayKeyConstants.getWxAppId());
//                createPid();
                genPayReq();
                UIHelper.toWeChatPay(mA, req);
                break;

            case PayConstant.BALANCE:
                balancePay();
                break;

        }
    }

    /**
     * 微信支付---------------------------------------------------------------------------------------------------------------------------------
     */

//    private void createPid(){
//        Log.i("pay_t","createPid");
//        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
//        getPrepayId.execute();
//    }
//    /**
//     * 生成微信支付请求参数对象
//     * {"paymentID":"WEIXIN","url":"prepayID=wx2015100911071104524314c90292987835&package=Sign=WXPay"}
//     * @return 微信支付对象
//     */
//    private PayReq weChatRequest() {
//        return null;
//    }
//
//    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String,String>> {
//
//        private ProgressDialog dialog;
//
//
//        @Override
//        protected void onPreExecute() {
//            Log.i("wx","onPreExecute");
//            dialog = ProgressDialog.show(mA, mA.getString(R.string.app_tip), mA.getString(R.string.getting_prepayid));
//        }
//
//        @Override
//        protected void onPostExecute(Map<String,String> result) {
//            Log.i("pay_t","onPostExecute");
//            Log.i("wx", "onPostExecute");
//            if (dialog != null) {
//                dialog.dismiss();
//            }
//
//            sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
//
//            Log.i("wx", "result:" + result + "--prepay_id: " + result.get("prepay_id"));
//            resultunifiedorder=result;
//            genPayReq();
//            UIHelper.toWeChatPay(mA,req);
//
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//        }
//
//        @Override
//        protected Map<String,String>  doInBackground(Void... params) {
//
//            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
//            String entity = genProductArgs();
//            Log.i("wx","doInBackground"+entity);
//
//            byte[] buf = Util.httpPost(url, entity);
//
//            String content = new String(buf);
//
//            Map<String,String> xml=decodeXml(content);
//
//            return xml;
//        }
//    }
//
//    public Map<String,String> decodeXml(String content) {
//
//        try {
//            Map<String, String> xml = new HashMap<String, String>();
//            XmlPullParser parser = Xml.newPullParser();
//            parser.setInput(new StringReader(content));
//            int event = parser.getEventType();
//            while (event != XmlPullParser.END_DOCUMENT) {
//
//                String nodeName=parser.getName();
//                switch (event) {
//                    case XmlPullParser.START_DOCUMENT:
//
//                        break;
//                    case XmlPullParser.START_TAG:
//
//                        if("xml".equals(nodeName)==false){
//                            //实例化student对象
//                            xml.put(nodeName,parser.nextText());
//                        }
//                        break;
//                    case XmlPullParser.END_TAG:
//                        break;
//                }
//                event = parser.next();
//            }
//
//            return xml;
//        } catch (Exception e) {
//        }
//        return null;
//
//    }


    /**
     * 微信生成随机数参数
     *
     * @return
     */
    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    /**
     * 微信生成时间戳参数
     *
     * @return
     */
    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }


//    private String genOutTradNo() {
//        Random random = new Random();
//        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
//    }


//    private String genProductArgs() {
//        StringBuffer xml = new StringBuffer();
//
//        try {
//            String	nonceStr = genNonceStr();
//
//
//            xml.append("</xml>");
//            List<NameValuePair> packageParams = new LinkedList<>();
//            packageParams.add(new BasicNameValuePair("appid", Constants.WX_APP_ID));
//            packageParams.add(new BasicNameValuePair("body", "weixin"));
//            packageParams.add(new BasicNameValuePair("mch_id", Constants.WX_MCH_ID));
//            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
//            packageParams.add(new BasicNameValuePair("notify_url", "http://121.40.35.3/test"));
//            packageParams.add(new BasicNameValuePair("out_trade_no",genOutTradNo()));
//            packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));
//            packageParams.add(new BasicNameValuePair("total_fee", "1"));
//            packageParams.add(new BasicNameValuePair("trade_type", "APP"));
//
//
//            String sign = genPackageSign(packageParams);
//            packageParams.add(new BasicNameValuePair("sign", sign));
//
//
//            String xmlstring =toXml(packageParams);
//
//            return xmlstring;
//
//        } catch (Exception e) {
//            return null;
//        }
//
//
//    }

    /**
     * 微信生成请求参数
     */
    private void genPayReq() throws Exception {
        Map<String, String> payData = MoneyUtil.getWeChatPayData(data.url);
        req.appId = PayKeyConstants.getWxAppId();
        req.partnerId = PayKeyConstants.getWxMchId();
        assert payData != null;
//        req.prepayId = resultunifiedorder.get("prepay_id");
        req.prepayId = payData.get("prepayID");
        req.packageValue = payData.get("package");
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());


        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        /**
         *
         * 服务器传值 签名需要
         */
        sb.append("prepay_id\n").append(payData.get("prepayID")).append("\n\n");

        req.sign = genAppSign(signParams);

        sb.append("sign\n").append(req.sign).append("\n\n");

    }

//    /**
//     生成签名
//     */
//

//    private String genPackageSign(List<NameValuePair> params) {
//        StringBuilder sb = new StringBuilder();
//
//        for (int i = 0; i < params.size(); i++) {
//            sb.append(params.get(i).getName());
//            sb.append('=');
//            sb.append(params.get(i).getValue());
//            sb.append('&');
//        }
//
//        sb.append("key=");
//        sb.append(Constants.WX_API_KEY);
//
//        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
//        return packageSign;
//    }

    /**
     * 微信获取签名
     *
     * @param params
     * @return
     */
    private String genAppSign(List<NameValuePair> params) throws Exception {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(PayKeyConstants.getWxApiKey());

        this.sb.append("sign str\n").append(sb.toString()).append("\n\n");
        return MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
    }

//    private String toXml(List<NameValuePair> params) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("<xml>");
//        for (int i = 0; i < params.size(); i++) {
//            sb.append("<"+params.get(i).getName()+">");
//
//
//            sb.append(params.get(i).getValue());
//            sb.append("</"+params.get(i).getName()+">");
//        }
//        sb.append("</xml>");
//
//        return sb.toString();
//    }
    /**
     * 以上微信支付---------------------------------------------------------------------------------------------------------------------------------------------------
     */

    /**
     * 支付宝支付     请求以及回调-----------------------------------------------------------------------------------------------------------------------------
     */

    private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String,String>) msg.obj);
//                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    DebugLog.i("test_alipay","：++++" + resultInfo + "--" + resultStatus);

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000") && AlipayResultUtil.isSuccess(resultInfo)) {
                        DebugLog.i("test_alipay", "支付成功：++++" + resultInfo);
                        EventBus.getDefault().post(new EventGetSuccessModel(PayConstant.ALIPAY_SUCCESS, resultInfo, PayMethodConstant.ALIPAY_TYPE));

                        PayCallBackManager
                                .getInstance()
                                .onPayCallBack(new EventGetSuccessModel(PayConstant.ALIPAY_SUCCESS, resultInfo, PayMethodConstant.ALIPAY_TYPE));

                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            EventBus.getDefault().post(new EventGetSuccessModel(PayConstant.ALIPAY_CONFIRMING, resultInfo, PayMethodConstant.ALIPAY_TYPE));

                            PayCallBackManager
                                    .getInstance()
                                    .onPayCallBack(new EventGetSuccessModel(PayConstant.ALIPAY_CONFIRMING, resultInfo, PayMethodConstant.ALIPAY_TYPE));

                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            EventBus.getDefault().post(new EventGetSuccessModel(PayConstant.ALIPAY_CANCEL, resultInfo, PayMethodConstant.ALIPAY_TYPE));

                            PayCallBackManager
                                    .getInstance()
                                    .onPayCallBack(new EventGetSuccessModel(PayConstant.ALIPAY_CANCEL, resultInfo, PayMethodConstant.ALIPAY_TYPE));

                        } else {
                            EventBus.getDefault().post(new EventGetSuccessModel(PayConstant.ALIPAY_FAIL, resultInfo, PayMethodConstant.ALIPAY_TYPE));

                            PayCallBackManager
                                    .getInstance()
                                    .onPayCallBack(new EventGetSuccessModel(PayConstant.ALIPAY_FAIL, resultInfo, PayMethodConstant.ALIPAY_TYPE));

                        }
                    }
                    break;
                }
                default:
                    break;
            }
            return false;
        }
    });

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    private void alipayPay(final String payInfo) {
        // 订单
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mA);
                DebugLog.i("test_alipay",Build.MODEL+"  "+ Build.BRAND+" "+Build.VERSION.RELEASE);
                DebugLog.i("test_alipay","payInfo:  "+ payInfo);
                Map<String,String> result = alipay.payV2(payInfo,true);
                // 调用支付接口，获取支付结果
//                String result = alipay.pay(payInfo,false);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 以上支付宝----------------------------------------------------------------------------------------------------------------------------------------------------
     */

    /**
     * 恒信汽车  余额支付   -----------------------------------------------------------------------------------------------------------------------------------
     */
    private void balancePay() {


    }


    /**
     *   以上余额支付    -----------------------------------------------------------------------------------------------------------------------------------------------
     */

}
