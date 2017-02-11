package com.hxqc.mall.thirdshop.activity;

import android.os.Bundle;

import com.hxqc.mall.activity.WebActivity;

/**
 * Function:
 * QA详情
 *
 * @author 袁秉勇
 * @since 2015年12月02日
 */
@Deprecated
public class ClauseDetailActivity extends WebActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebView.loadUrl("file:///android_asset/QA.html");
    }
}
