package com.hxqc.mall.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.hxqc.mall.core.BuildConfig;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.util.DebugLog;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 说明:h5页面
 * <p>
 * author: 吕飞
 * since: 2015-09-02
 * Copyright:恒信汽车电子商务有限公司
 */
public class WebActivity extends BackActivity implements OnRefreshHandler {
    public final static String TITLE = "title";
    public final static String URL = "url";
    public final static String TOHOMEFLAG = "toHome";
    public Toolbar mToolBar;
    public WebView mWebView;
    public String mWeb;
    public WebSettings settings;
    public PtrFrameLayout mPtrFrameLayoutView;
    public UltraPullRefreshHeaderHelper mPtrHelper;
    protected boolean needClearHistory;
    protected LinearLayout parent;
    android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == 1) mWebView.reload();

            return false;
        }
    });
    private RequestFailView mRequestFailView;
    private boolean showToHomeIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        parent = (LinearLayout) findViewById(R.id.ll_parent);
        mWebView = (WebView) findViewById(R.id.web_view);

        /** 如果需要使用ToolBar，注意修改theme并重写{@link #initActionBar()} **/
        if (null != (mToolBar = initToolBar())) {
            parent.addView(mToolBar, 0);
            setSupportActionBar(mToolBar);
            mToolBar.setNavigationIcon(R.drawable.ic_back);
            mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        mPtrFrameLayoutView = (PtrFrameLayout) findViewById(R.id.refresh_frame);
        mPtrHelper = new UltraPullRefreshHeaderHelper(this, mPtrFrameLayoutView);
        mPtrHelper.setOnRefreshHandler(this);
        settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        mRequestFailView = (RequestFailView) findViewById(R.id.fail_view);

        mWebView.getSettings().setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
//        settings.setBuiltInZoomControls(true);                      //support zoom
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
//					mWebView.loadUrl(url);
                    HashMap< String, String > map = new HashMap<>();
                    map.put("token", UserInfoHelper.getInstance().getToken(WebActivity.this));
                    view.loadUrl(url, map);
                }
                return true;
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, final String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                //DebugLog.d("MyTAG","errorCode="+errorCode+",description="+description+",failingUrl="+failingUrl);
                showFailView();
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                DebugLog.e(getClass().getName(),"加载完成时 url："+ url);
                mWebView.setVisibility(View.VISIBLE);
            }


            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);
                if (needClearHistory) {
                    mWebView.clearHistory();
                    needClearHistory = false;
                }
            }

        });
        loadUrl();
    }


    /**
     * 如果继承webActivity的页面需要使用ToolBar 请重写该方法
     **/
    protected Toolbar initToolBar() {
        return null;
    }


    private void showFailView() {
        mRequestFailView.setVisibility(View.VISIBLE);
        mWebView.setVisibility(View.GONE);
        mRequestFailView.setEmptyDescription("网络连接异常");
        mRequestFailView.setFailButtonClick("重试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.setVisibility(View.INVISIBLE);
                mRequestFailView.setVisibility(View.GONE);
                onRefresh();
            }
        });
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
    }


    public void loadUrl() {
        if (getIntent().getExtras() != null || getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA) != null) {
            String title = getIntent().getExtras().getString(WebActivity.TITLE) != null ? getIntent().getExtras().getString(WebActivity.TITLE) : getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(WebActivity.TITLE);
            if (!TextUtils.isEmpty(title)) {
                DebugLog.e(getClass().getName(), title);
                this.setTitle(title);
            }

            String url = getIntent().getExtras().getString(WebActivity.URL) != null ? getIntent().getExtras().getString(WebActivity.URL) : getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(WebActivity.URL);
            if (!TextUtils.isEmpty(url)) {
                DebugLog.e(getClass().getName(), url);
                if (url.startsWith("/")) {
                    url = ApiUtil.AccountHostURL + url;
                }
                HashMap< String, String > map = new HashMap<>();
                map.put("token", UserInfoHelper.getInstance().getToken(this));
                mWebView.loadUrl(url, map);
            }

            // 是否显示返回主页按钮
            showToHomeIcon = getIntent().getExtras() != null ? getIntent().getExtras().getBoolean(TOHOMEFLAG) : getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getBoolean(TOHOMEFLAG);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean hasMore() {
        return false;
    }


    @Override
    public void onRefresh() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mPtrHelper.refreshComplete(mPtrFrameLayoutView);
                Message message = Message.obtain();
                message.what = 1;
                handler.sendMessage(message);
            }
        }, 2000);
    }


    @Override
    public void onLoadMore() {

    }


    //控制返回键
    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (showToHomeIcon) {
            getMenuInflater().inflate(R.menu.menu_tohome, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_to_home) {
            ActivitySwitchBase.toMain(this, 0);
        }
        return super.onOptionsItemSelected(item);
    }
}
