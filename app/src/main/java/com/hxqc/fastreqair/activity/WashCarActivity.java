package com.hxqc.fastreqair.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.fastreqair.api.CarWashApiClient;
import com.hxqc.fastreqair.model.CaptchaPayModel;
import com.hxqc.fastreqair.util.CarWashActivitySwitcher;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.qr.util.QRCodeUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.util.JSONUtils;

import hxqc.mall.R;

/**
 * @Author : 钟学东
 * @Since : 2016-05-25
 * FIXME
 * Todo 洗车付款码页面
 */
public class WashCarActivity extends BackActivity {

    private ImageView mQRcodeView;
    private TextView mCaptchaView;
    private RelativeLayout mRefreshView;
    private TextView mTimeView;
    private ImageView mStoreView;
    private ImageView mReChargeView;

    private CaptchaPayModel captchaPayModel;
    private CarWashApiClient apiClient;

    private static final int HintMessage = 1;
    private static int DelayedSearchTime ;//延迟提示时间
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == HintMessage){
                getDate();
            }
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wash_car);
        apiClient = new CarWashApiClient();
        initView();
        getDate();
        initEvent();
    }


    private void initEvent() {
        mStoreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarWashActivitySwitcher.toCarWashShopList(WashCarActivity.this);
            }
        });

        mReChargeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcher.toRecharge(WashCarActivity.this);
            }
        });

        mRefreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });
    }

    private void hintMessage(){
        Message message = new Message();
        message.what = HintMessage;
        handler.sendMessageDelayed(message,DelayedSearchTime);
    }

    private void getDate() {
        apiClient.CaptchaPay(new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                captchaPayModel = JSONUtils.fromJson(response,new TypeToken<CaptchaPayModel>(){});
                if(captchaPayModel != null) {
                    initDate(captchaPayModel);
                    DelayedSearchTime = captchaPayModel.validityTime * 1000;
                }
                hintMessage();
            }
        });
    }

    private void initDate(CaptchaPayModel captchaPayModel) {
        mCaptchaView.setText(String.format("%s   %s", captchaPayModel.captcha.substring(0, 4), captchaPayModel.captcha.substring(4, 8)));
//        mTimeView.setText(String.format("每%s分钟自动更新",captchaPayModel.validityTime/60));
        mTimeView.setText("每分钟自动更新");
        mQRcodeView.setImageBitmap(QRCodeUtil.createQRImage(captchaPayModel.captcha,mQRcodeView.getWidth(),mQRcodeView.getHeight()));
    }

    private void initView() {
        mQRcodeView = (ImageView) findViewById(R.id.qr_code);
        mCaptchaView = (TextView) findViewById(R.id.captcha);
        mRefreshView = (RelativeLayout) findViewById(R.id.refresh);
        mTimeView = (TextView) findViewById(R.id.validityTime);
        mStoreView = (ImageView) findViewById(R.id.store);
        mReChargeView = (ImageView) findViewById(R.id.recharge);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(HintMessage);
    }
}
