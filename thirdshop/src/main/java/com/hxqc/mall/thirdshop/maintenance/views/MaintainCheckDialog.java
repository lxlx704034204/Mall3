package com.hxqc.mall.thirdshop.maintenance.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;

/**
 * @Author : 钟学东
 * @Since : 2016-09-13
 * FIXME
 * Todo 保养检测的Dialog
 */
public class MaintainCheckDialog extends Dialog {

    private TextView mTitleView;
    private WebView mWebView;
    private WebSettings settings;
    private TextView mConfirmView;
    private RelativeLayout mRlConfirmView;

    private DisplayMetrics metric;
    private String url;

    public MaintainCheckDialog(Context context, int themeResId,String url) {
        super(context, themeResId);
        metric = new DisplayMetrics();
        this.url = url;
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metric);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_maintain_check_dialog);
        mTitleView  = (TextView) findViewById(R.id.title);
        mWebView = (WebView) findViewById(R.id.web_view);
        mConfirmView = (TextView) findViewById(R.id.confirm);
        mRlConfirmView = (RelativeLayout) findViewById(R.id.rl_confirm);


        settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        mWebView.getSettings().setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setBuiltInZoomControls(true);                      //support zoom
        settings.setLoadWithOverviewMode(true);

        ViewGroup.LayoutParams layoutParams = mWebView.getLayoutParams();
        layoutParams.height = metric.heightPixels/2 ;
        layoutParams.width = metric.widthPixels;
        mWebView.setLayoutParams(layoutParams);

        mTitleView.setText("保养检测说明");
        mConfirmView.setText("我知道了");
        mWebView.loadUrl(url);

        mRlConfirmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }



}
