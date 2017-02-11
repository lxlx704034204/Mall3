package com.hxqc.newenergy.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.newenergy.api.NewEnergyApiClient;
import com.hxqc.newenergy.bean.WikiJSModel;
import com.hxqc.newenergy.inter.EVWikiJSInterface;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hxqc.mall.BuildConfig;
import hxqc.mall.R;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 说明:  新闻界面
 * author: 何玉
 * since: 2016年3月9日
 * Copyright:恒信汽车电子商务有限公司
 */
public class Ev_NewsWebActivity extends ToolBarActivity implements OnRefreshHandler, EVWikiJSInterface.WebJSResponseListener {

    public WebView mWebView;
    public WebSettings settings;
    public PtrFrameLayout mPtrFrameLayoutView;
    public UltraPullRefreshHeaderHelper mPtrHelper;

    public EVWikiJSInterface jsInterface;
    List< WikiJSModel > tempModels;
    WikiJSModel homeModel;
    WikiJSModel currentModel;
    android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == 1) {

                mWebView.reload();
            } else if (msg.what == 2) {
                String response = (String) msg.obj;
                clickChangeURL(response);
            }

            return false;
        }
    });
    FrameLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ev_web);
        isFinish = false;
        toolbarInit();
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPressOperater();
            }
        });

        jsInterface = new EVWikiJSInterface(this);
        jsInterface.setListener(this);

        parent = (FrameLayout) findViewById(R.id.s_parent);

        String url = new NewEnergyApiClient().getWiki();
        initData(url);

        mWebView = (WebView) findViewById(R.id.web_view);

        mPtrFrameLayoutView = (PtrFrameLayout) findViewById(R.id.refresh_frame);
        mPtrHelper = new UltraPullRefreshHeaderHelper(this, mPtrFrameLayoutView);
        mPtrHelper.setOnRefreshHandler(this);
        settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.addJavascriptInterface(jsInterface, "Android");
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setBuiltInZoomControls(true);                      //support zoom
        settings.setLoadWithOverviewMode(true);
        settings.setUserAgentString(settings.getUserAgentString() + " HXMall/" + BuildConfig.VERSION_NAME);

        DebugLog.i("ev_wiki", settings.getUserAgentString());

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("tel:")) {
                    Intent dialIntent = new Intent();
                    dialIntent.setAction(Intent.ACTION_DIAL);
                    dialIntent.setData(Uri.parse(url));
                    startActivity(dialIntent);
                } else {
                    changeTitleAndUrl();
                }
                return true;
            }
        });

        changeTitleAndUrl();
    }

    private void initData(String url) {
        homeModel = new WikiJSModel();
        homeModel.url = url;
        homeModel.title = "百科知识";

        currentModel = homeModel;

        tempModels = new ArrayList<>();
        tempModels.add(homeModel);
    }

    private WikiJSModel backAboveModel() {
        if (tempModels.size() > 1) {
            tempModels.remove(0);
        }
        return tempModels.get(0);
    }

    private void addBelowModel(WikiJSModel model) {
        tempModels.add(0, model);
    }

    @Override
    public void onResume() {
        super.onResume();
        closePopWindowMenu();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
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

    /**
     * 获取返回数据
     *
     * @param response
     *         json
     */
    @Override
    public void getSwitchResponse(String response) {
        DebugLog.i("ev_wiki", "web++++: " + response);

        Message message = Message.obtain();
        message.what = 2;
        message.obj = response;
        handler.sendMessage(message);

    }

    private void clickChangeURL(String response) {
        if (TextUtils.isEmpty(response)) {

        } else {
            WikiJSModel model = JSONUtils.fromJson(response, new TypeToken< WikiJSModel >() {
            });

            addBelowModel(model);
            currentModel = model;
            changeTitleAndUrl();
        }
    }

    private void changeTitleAndUrl() {
        if (currentModel != null && homeModel != null) {
            if ((currentModel.url).equals(homeModel.url)) {
                setTitleBar(currentModel.title, true);
            } else {
                setTitleBar(currentModel.title, false);
            }
            closePopWindowMenu();
            mWebView.loadUrl(currentModel.url);
        }
    }

    //控制返回键
    @Override
    public void onBackPressed() {
        backPressOperater();
    }

    private void backPressOperater() {
        if (tempModels.size() > 1) {
            currentModel = backAboveModel();
            changeTitleAndUrl();
        } else {
            finish();
        }
    }

    /**
     * 设置标题栏
     *
     * @param text
     *         标题
     * @param isClickable
     *         是否可点
     */
    private void setTitleBar(String text, boolean isClickable) {

        /**
         * 切换标题栏
         *  text为空时不做处理
         *  不为空时 切换标题名称
         */
        if (!TextUtils.isEmpty(text))
            mTitle.setText(text);

        mTitle_Button.setEnabled(isClickable);

        DebugLog.i("ev_wiki", text + " : " + isClickable);
    }

}
