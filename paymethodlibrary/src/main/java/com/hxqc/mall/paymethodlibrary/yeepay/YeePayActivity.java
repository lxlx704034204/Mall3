package com.hxqc.mall.paymethodlibrary.yeepay;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hxqc.mall.paymethodlibrary.R;
import com.hxqc.mall.paymethodlibrary.activity.PayMethodBaseActivity;
import com.hxqc.mall.paymethodlibrary.inter.WebYeepayInterface;
import com.hxqc.mall.paymethodlibrary.manager.PayCallBackManager;
import com.hxqc.mall.paymethodlibrary.model.EventGetSuccessModel;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.mall.paymethodlibrary.util.PayMethodConstant;

import org.greenrobot.eventbus.EventBus;


public class YeePayActivity extends PayMethodBaseActivity {

    WebView yeePayView;
    PayCallBackManager callBackManager;

    @SuppressLint("AddJavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yee_pay);
        actionBar.setTitle("易宝支付");

        callBackManager = PayCallBackManager.getInstance();

        String url = getIntent().getStringExtra("url");
        yeePayView = (WebView) findViewById(R.id.wv_yeepay);
        yeePayView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });


        WebSettings webSettings = yeePayView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);


        yeePayView.addJavascriptInterface(new WebYeepayInterface(this), "Android");
        yeePayView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new EventGetSuccessModel(PayConstant.YEEPAY_fail,"",PayMethodConstant.YEEPAY_TYPE));

        if (callBackManager!=null){
            callBackManager.onPayCallBack(new EventGetSuccessModel(PayConstant.YEEPAY_fail,"",PayMethodConstant.YEEPAY_TYPE));
        }

        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        EventBus.getDefault().post(new EventGetSuccessModel(PayConstant.YEEPAY_fail,"",PayMethodConstant.YEEPAY_TYPE));

        if (callBackManager!=null){
            callBackManager.onPayCallBack(new EventGetSuccessModel(PayConstant.YEEPAY_fail,"",PayMethodConstant.YEEPAY_TYPE));
        }

        return super.onSupportNavigateUp();
    }
}
