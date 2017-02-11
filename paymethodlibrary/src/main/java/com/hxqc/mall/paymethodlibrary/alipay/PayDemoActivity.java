package com.hxqc.mall.paymethodlibrary.alipay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.hxqc.mall.paymethodlibrary.R;
import com.hxqc.mall.paymethodlibrary.model.EventGetSuccessModel;
import com.hxqc.mall.paymethodlibrary.util.AlipayResultUtil;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.mall.paymethodlibrary.util.PayMethodConstant;

import org.greenrobot.eventbus.EventBus;

public class PayDemoActivity extends FragmentActivity {

    private static final int SDK_PAY_FLAG = 1;

//    private static final int SDK_CHECK_FLAG = 2;

    String url = "+";
//    int pay_type;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    Log.i("alipay", "：++++" + resultInfo+"--"+resultStatus);

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000") && AlipayResultUtil.isSuccess(resultInfo)) {
                        Log.i("alipay", "支付成功：++++" + resultInfo);
                        EventBus.getDefault().post(new EventGetSuccessModel(PayConstant.ALIPAY_SUCCESS, resultInfo, PayMethodConstant.ALIPAY_TYPE));
                        PayDemoActivity.this.finish();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayDemoActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post(new EventGetSuccessModel(PayConstant.ALIPAY_CONFIRMING, resultInfo, PayMethodConstant.ALIPAY_TYPE));
                        } else if (TextUtils.equals(resultStatus, "6001")){
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            EventBus.getDefault().post(new EventGetSuccessModel(PayConstant.ALIPAY_CANCEL,resultInfo,PayMethodConstant.ALIPAY_TYPE));
                            Toast.makeText(PayDemoActivity.this, "支付取消",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            EventBus.getDefault().post(new EventGetSuccessModel(PayConstant.ALIPAY_FAIL,resultInfo,PayMethodConstant.ALIPAY_TYPE));
                            Toast.makeText(PayDemoActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new EventGetSuccessModel(PayConstant.ALIPAY_CANCEL,"",PayMethodConstant.ALIPAY_TYPE));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_main);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
//        pay_type = intent.getIntExtra(PayConstant.PAY_STATUS_FLAG, PayConstant.PAY_LIST);
        TextView p_price = (TextView) findViewById(R.id.product_price);
        p_price.setText(intent.getStringExtra("money_") + " 元");

    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(View v) {
        // 订单

        final String payInfo = url;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayDemoActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo,true);
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

}
