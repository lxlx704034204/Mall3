package com.hxqc.mall.thirdshop.maintenance.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.util.DisplayTools;
import com.hxqc.mall.thirdshop.R;

/**
 * Function: 保养列表中店铺的信息介绍页面
 *
 * @author 袁秉勇
 * @since 2016年05月23日
 */
public class IntroduceShopActivity extends BackActivity {
    private final static String TAG = IntroduceShopActivity.class.getSimpleName();
    private Context mContext;
    public static final String INTRODUCE = "introduce";

    private WebView mWebView;
    private String introduce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        introduce = TextUtils.isEmpty(getIntent().getStringExtra(INTRODUCE)) ? "" : getIntent().getStringExtra(INTRODUCE);

        setContentView(R.layout.activity_introduce_shop_info);

        mWebView = (WebView) findViewById(R.id.web_view);

        introduceConfig(introduce);
    }


    /**
     * 车辆详情设置
     */
    public void introduceConfig(String introduce) {

        reWriteWebview();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        }
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
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
        value += Html.fromHtml(introduce) + "</body></html>";
        value = value.replace("<img", "<img width=\"" + ((this).getWindowManager().getDefaultDisplay().getWidth() / (int) DisplayTools.getScreenDensity(this)) + "\"");
        mWebView.loadData(value, "text/html", "utf-8");
    }


    /**
     * 重写webview
     */
    public void reWriteWebview() {
        mWebView.setWebViewClient(new WebViewClient());
    }
}
