package com.hxqc.mall.thirdshop.maintenance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.thirdshop.R;

/**
 * @Author : 钟学东
 * @Since : 2016-03-03
 * FIXME
 * Todo 商品详情的activity
 */
public class MerchandiseDetailsActivity extends BackActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchandise_detail);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        getSupportActionBar().setTitle(title);

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.requestFocus();
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                MerchandiseDetailsActivity.this.setProgress(newProgress * 100);
            }
        });
        mWebView.loadUrl("https://www.baidu.com/");
    }

    @Override
    public boolean onSupportNavigateUp() {
        if(mWebView.canGoBack() ){
            mWebView.goBack();
            return true;
        }
        return super.onSupportNavigateUp();
    }

    public boolean onKeyDown(int keyCoder, KeyEvent event){
        if(mWebView.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK ){
            mWebView.goBack();   //goBack()表示返回webView的上一页面

            return true;
        }
        return super.onKeyDown(keyCoder, event);
    }
}
