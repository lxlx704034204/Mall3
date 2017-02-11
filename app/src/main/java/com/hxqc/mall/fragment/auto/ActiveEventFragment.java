package com.hxqc.mall.fragment.auto;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hxqc.mall.core.views.ProgressWebView;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.mall.fragment.BackHandledFragment;
import com.hxqc.mall.interfaces.EventActiveInterface;
import com.hxqc.util.DebugLog;

import java.util.Timer;
import java.util.TimerTask;

import hxqc.mall.BuildConfig;
import hxqc.mall.R;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 活动
 * A simple {@link Fragment} subclass.
 */
public class  ActiveEventFragment extends BackHandledFragment implements OnRefreshHandler {


    public ProgressWebView mActiveEvent;
    Toolbar mToolBar;
    Menu menu;
    PtrFrameLayout mPtrFrameLayoutView;
    UltraPullRefreshHeaderHelper mPtrHelper;
    /**
     * 刷新活动列表
     */
    android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == 1)
                mActiveEvent.reload();

            return false;
        }
    });
    private boolean isLoadSuccess = true;

    public ActiveEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_active_event, container, false);
        mToolBar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mToolBar.setTitle(R.string.active_event);
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.inflateMenu(R.menu.menu_event_detail);

        menu = mToolBar.getMenu();
        menu.getItem(0).setVisible(false);
        return rootView;
    }

    @SuppressLint("AddJavascriptInterface")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActiveEvent = (ProgressWebView) view.findViewById(R.id.wv_active_event);

        mActiveEvent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }


            @Override
            public void onPageStarted(WebView view, final String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (url.equals(EventActiveInterface.EVENT_URL))//第一页
                {
                    menu.getItem(0).setVisible(false);
//                    menu.getItem(0).setTitle("刷新");
                    mToolBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_taken));
//                    mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem menuItem) {
//                            if (menuItem.getItemId() == com.hxqc.hxqcmall.comment.R.id.action_settings) {
//                                mActiveEvent.reload();
//                                Toast.makeText(getActivity(),
//                                        " - 刷新 - ",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                            return false;
//                        }
//                    });
                } else//第二页
                {
                    mToolBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
                    mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mActiveEvent.canGoBack())
                                mActiveEvent.goBack();
                        }
                    });
//                    menu.getItem(0).setVisible(true);
//                    menu.getItem(0).setTitle("分享");
//                    mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem menuItem) {
//                            if (menuItem.getItemId() == com.hxqc.hxqcmall.comment.R.id.action_settings) {
//                                Toast.makeText(getActivity(),
//                                        " - 分享 - ",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                            return false;
//                        }
//                    });
                }
//                Toast.makeText(ActiveEventFragment.this.getActivity(), " - 分享 - " + url, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
               super.onReceivedError(view,request,error);
                isLoadSuccess = false;
            }

        });

        WebSettings webSettings = mActiveEvent.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        mActiveEvent.addJavascriptInterface(new EventActiveInterface(getActivity()), "Android");
        DebugLog.i("ApiClient", EventActiveInterface.EVENT_URL);
        mActiveEvent.loadUrl(EventActiveInterface.EVENT_URL);
        webSettings.setUserAgentString(webSettings.getUserAgentString()+" HXMall/"+ BuildConfig.VERSION_NAME);
        DebugLog.i("ApiClient", webSettings.getUserAgentString());
        mActiveEvent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DebugLog.i("active", "-0-" + isLoadSuccess);
                if (!isLoadSuccess) {
                    mActiveEvent.loadUrl(EventActiveInterface.EVENT_URL);
                    isLoadSuccess = true;
                }
                return false;
            }
        });


        mPtrFrameLayoutView = (PtrFrameLayout) view.findViewById(R.id.event_active_refresh);
        mPtrHelper = new UltraPullRefreshHeaderHelper(getActivity(), mPtrFrameLayoutView);
        mPtrHelper.setOnRefreshHandler(this);

        mActiveEvent.loadUrl(EventActiveInterface.EVENT_URL);
    }

    @Override
    public String fragmentDescription() {
        return "活动";
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public void onRefresh() {
//        Toast.makeText(getActivity(),
//                " - 刷新 - ",
//                Toast.LENGTH_SHORT).show();

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
