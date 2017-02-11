package com.hxqc.mall.activity.me;

import android.os.Bundle;
import android.webkit.WebSettings;

import com.hxqc.mall.activity.WebActivity;

/**
 * Function:
 *
 * @author yby
 * @since 2015年10月17日
 */
@Deprecated
public class HelpCenterActivity extends WebActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolBar.setNavigationIcon(getResources().getDrawable(com.hxqc.mall.core.R.drawable.ic_back));
        mWebView.getSettings().setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setBuiltInZoomControls(true);                      //support zoom
        settings.setLoadWithOverviewMode(true);
    }
}
