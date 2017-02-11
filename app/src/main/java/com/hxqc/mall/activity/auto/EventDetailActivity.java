package com.hxqc.mall.activity.auto;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;

import com.hxqc.mall.activity.WebActivity;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.util.DebugLog;

/**
 * 活动详情
 */
public class EventDetailActivity extends WebActivity implements OnRefreshHandler {

//    ProgressWebView mEventDetail;
//
//    PtrFrameLayout mPtrFrameLayoutView;
//
//    UltraPullRefreshHeaderHelper mPtrHelper;

    @SuppressLint("AddJavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_event_detail);
//        getSupportActionBar().setTitle("活动");
//        String url = getIntent().getBundleExtra("data").getString("event_url");


//        mPtrFrameLayoutView = (PtrFrameLayout)findViewById(R.id.event_active_detail_refresh);
//        mPtrHelper = new UltraPullRefreshHeaderHelper(EventDetailActivity.this, mPtrFrameLayoutView);
//        mPtrHelper.setOnRefreshHandler(this);
//
//
//        mEventDetail = (ProgressWebView) findViewById(R.id.wv_event_detail);
//        mEventDetail.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }

        mWebView.addJavascriptInterface(new EventInter(), "Android");

//        if (!TextUtils.isEmpty(url)){
//            mEventDetail.loadUrl(url);
//            mPtrHelper.setOnRefreshHandler(this);
//        }
    }

    /**
     * 刷新
     */
//    android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//
//            if (msg.what == 1)
//                mEventDetail.reload();
//
//            return false;
//        }
//    });

//    @Override
//    public boolean hasMore() {
//        return false;
//    }
//
//    @Override
//    public void onRefresh() {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                mPtrHelper.refreshComplete(mPtrFrameLayoutView);
//                Message message = Message.obtain();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        }, 2000);
//    }
//
//    @Override
//    public void onLoadMore() {
//
//    }

    /**
     * share    -------------------------------------------------------------------------------------------------
     * @param menu  menu
     * @return if share
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_event_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
//        if (item.getItemId() == com.hxqc.hxqcmall.comment.R.id.action_settings) {
//            Toast.makeText(EventDetailActivity.this, " - 分享 - ", Toast.LENGTH_SHORT).show();
//            MyShareUtil.shareUrl(EventDetailActivity.this, "I have successfully share my message through my app");
//        }
        return false;
    }

    class EventInter {
        @JavascriptInterface
        public void showResponseView(String success) {
            DebugLog.i("home", "web++++: " + success);
        }
    }

}
