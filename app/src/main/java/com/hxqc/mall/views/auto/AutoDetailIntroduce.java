package com.hxqc.mall.views.auto;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hxqc.mall.core.model.auto.AutoDetail;
import com.hxqc.util.DisplayTools;

import hxqc.mall.R;

/**
 * Author:胡俊杰
 * Date: 2015/10/26
 * FIXME
 * Todo
 */
public class AutoDetailIntroduce extends RelativeLayout {
    AutoDetailTabView mTabView;
    WebView mIntroduceView;
    ImageView loadingRoundView;
    View mContentLayout;

    public AutoDetailIntroduce(Context context) {
        super(context);
    }

    public AutoDetailIntroduce(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_auto_introduce, this);
        mTabView = (AutoDetailTabView) findViewById(R.id.item_introduce_tab);
        mIntroduceView = (WebView) findViewById(R.id.item_introduce_view);
        loadingRoundView = (ImageView) findViewById(R.id.iv_item_detail_round);
        mContentLayout = findViewById(R.id.content_layout);
        mTabView.setContentView(mContentLayout);
    }

    public void setAutoDetail(AutoDetail autoDetail) {
        introduceConfig(autoDetail);
    }

    /**
     * 重写webview
     */
    public void reWriteWebview() {
        mIntroduceView.setWebViewClient(new AutoWebViewClient());
    }

    /**
     * 车辆详情设置
     */
    public void introduceConfig(AutoDetail autoDetail) {

        reWriteWebview();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mIntroduceView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            mIntroduceView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        }
        mIntroduceView.getSettings().setLoadWithOverviewMode(true);
        mIntroduceView.getSettings().setJavaScriptEnabled(true);
        mIntroduceView.getSettings().setUseWideViewPort(true);
        String value = "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "\t<meta name=\"viewport\" content=\"initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui\">\n" +
                "<style type='text/css'>" +
                "body {margin:0;padding:0} " +
                "p {margin:0;padding:0} " +
                " </style>" +
                "  </head>\n" +
                "  <body>\n";
        value += Html.fromHtml(autoDetail.getIntroduce()).toString() + "</body></html>";
        value = value.replace("<img", "<img width=\"" + (((Activity) getContext()).getWindowManager().getDefaultDisplay().getWidth() / (int) DisplayTools.getScreenDensity(getContext())) + "\"");
        mIntroduceView.loadData(value, "text/html", "utf-8");
    }

    Animation animation;

    /**
     * 开始loading动画
     */
    public void startLoadingAnim() {
        if (loadingRoundView == null) return;
        loadingRoundView.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(getContext(), com.hxqc.mall.core.R.anim.rotate_anim);
        LinearInterpolator interpolator = new LinearInterpolator();
        animation.setInterpolator(interpolator);
        loadingRoundView.startAnimation(animation);
    }

    /**
     * 停止 并 隐藏loading动画
     */
    public void cancelLoadingAnim() {

        if (animation != null)
            animation.cancel();

        if (loadingRoundView != null) {
            loadingRoundView.clearAnimation();
            loadingRoundView.setVisibility(GONE);
        }

        animation = null;

    }

    private class AutoWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
//            DebugLog.i("Test_auto", "---->" + "onPageFinished");
            cancelLoadingAnim();
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            DebugLog.i("Test_auto", "---->" + "onPageStarted");
            startLoadingAnim();
            super.onPageStarted(view, url, favicon);
        }
    }

}
