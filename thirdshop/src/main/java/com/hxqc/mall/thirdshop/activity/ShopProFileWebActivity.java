package com.hxqc.mall.thirdshop.activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hxqc.mall.activity.WebActivity;
import com.hxqc.mall.core.BuildConfig;
import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.views.CallBar;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * liaoguilong
 * 2015-12-29 15:22:29
 * 店铺简介 H5
 */
public class ShopProFileWebActivity extends WebActivity {

    private CallBar mCallBar;
    public final static String TEL = "tel";
    public final  static String TITLE = "title";
    public final static String URL = "url";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_activity_shop_profile_web);

        View view=findViewById(R.id.shopprofile_include);
        mCallBar= (CallBar) findViewById(R.id.shopprofile_call_bar);
        mWebView = (WebView) view.findViewById(R.id.web_view);

        mPtrFrameLayoutView = (PtrFrameLayout)view.findViewById(R.id.refresh_frame);
        mPtrHelper = new UltraPullRefreshHeaderHelper(this, mPtrFrameLayoutView);
        mPtrHelper.setOnRefreshHandler(this);
        settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        mWebView.getSettings().setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setBuiltInZoomControls(true);                      //support zoom
        settings.setLoadWithOverviewMode(true);
        settings.setUserAgentString(settings.getUserAgentString() + " HXMall/" + BuildConfig.VERSION_NAME);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("tel:")) {
                    Intent dialIntent = new Intent();
                    dialIntent.setAction(Intent.ACTION_DIAL);
                    dialIntent.setData(Uri.parse(url));
                    startActivity(dialIntent);
                } else {
                    mWebView.loadUrl(url);
                }
                return true;
            }
        });

        if (getIntent().getExtras() != null || getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA) != null) {
            String title = getIntent().getExtras().getString(ShopProFileWebActivity.TITLE) != null ?
                    getIntent().getExtras().getString(ShopProFileWebActivity.TITLE) :
                    getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ShopProFileWebActivity.TITLE);
            if (!TextUtils.isEmpty(title)) {
                Log.e(getClass().getName(), title);
                this.setTitle(title);
            }

            String url = getIntent().getExtras().getString(ShopProFileWebActivity.URL) != null ?
                    getIntent().getExtras().getString(ShopProFileWebActivity.URL) :
                    getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ShopProFileWebActivity.URL);
            if (!TextUtils.isEmpty(url)) {
                Log.e(getClass().getName(), url);
                if (url.startsWith("/")) {
                    mWebView.loadUrl(ApiUtil.AccountHostURL + url);
                } else {
                    mWebView.loadUrl(url);
                }
            }
            String shopTel = getIntent().getExtras().getString(ShopProFileWebActivity.TEL) != null ?
                    getIntent().getExtras().getString(ShopProFileWebActivity.TEL) :
                    getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(ShopProFileWebActivity.TEL);
            if (!TextUtils.isEmpty(shopTel)) {
                Log.e(getClass().getName(), shopTel);
                mCallBar.setNumber(shopTel);
            }
        }
    }


}
