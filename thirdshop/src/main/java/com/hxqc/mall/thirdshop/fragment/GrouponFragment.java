package com.hxqc.mall.thirdshop.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.hxqc.mall.core.BuildConfig;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.bzinga.DES3;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.mall.thirdshop.utils.AreaSiteUtil;

import java.util.Timer;
import java.util.TimerTask;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 说明:团购会
 *
 * @author: 吕飞
 * @since: 2016-11-21
 * Copyright:恒信汽车电子商务有限公司
 */

public class GrouponFragment extends FunctionFragment implements OnRefreshHandler {
    public Toolbar mToolBar;
    public WebView mWebView;
    public WebSettings settings;
    public PtrFrameLayout mPtrFrameLayoutView;
    public UltraPullRefreshHeaderHelper mPtrHelper;
    LinearLayout parent;
    AreaSiteUtil mAreaSiteUtil;
    BaseSharedPreferencesHelper mBaseSharedPreferencesHelper;
    String mCurrentCityID = "";
    Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == 1)
                mWebView.reload();

            return false;
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_web, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parent = (LinearLayout) view.findViewById(R.id.ll_parent);
        mWebView = (WebView) view.findViewById(R.id.web_view);
        mAreaSiteUtil = new AreaSiteUtil(getActivity());
        mBaseSharedPreferencesHelper = new BaseSharedPreferencesHelper(getActivity());
        mPtrFrameLayoutView = (PtrFrameLayout) view.findViewById(R.id.refresh_frame);
        mPtrHelper = new UltraPullRefreshHeaderHelper(getActivity(), mPtrFrameLayoutView);
        mPtrHelper.setOnRefreshHandler(this);
        settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
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
        getData();
    }

    public void getData() {
        String url = "";
        String site = mBaseSharedPreferencesHelper.getSpecialCarAreaHistoryPinYing();
        String cityGroupID = mAreaSiteUtil.getSiteID();
        if (!mCurrentCityID.equals(cityGroupID)) {
            mCurrentCityID = cityGroupID;
            if (ApiUtil.isDebug) {
                url = ApiUtil.getShopURL("/Groupon") + "?site=" + site;
            } else {
                try {
                    url = DES3.encode("site=" + site);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                url = ApiUtil.getShopURL("/Groupon?p=") + url;
            }
            if (!TextUtils.isEmpty(url)) {
                if (url.startsWith("/")) {
                    mWebView.loadUrl(ApiUtil.AccountHostURL + url);
                } else {
                    mWebView.loadUrl(url);
                }
            }
        }
    }

    @Override
    public String fragmentDescription() {
        return "H5页面";
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
}
