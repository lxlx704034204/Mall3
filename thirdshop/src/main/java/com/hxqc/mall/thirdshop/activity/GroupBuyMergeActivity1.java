package com.hxqc.mall.thirdshop.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.BuildConfig;
import com.hxqc.mall.core.api.ApiUtil;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.utils.AreaSiteUtil;
import com.hxqc.mall.thirdshop.utils.SharedPreferencesHelper;
import com.hxqc.util.DebugLog;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import in.srain.cube.views.ptr.PtrFrameLayout;

import static com.hxqc.mall.thirdshop.utils.HomeSiteDataUtil.appendWordToStr;

@Deprecated
public class GroupBuyMergeActivity1 extends NoBackActivity implements OnRefreshHandler {
    public final static String SHOWTOHOME = "toHome";
    public WebView mWebView;
    public WebSettings settings;
    public PtrFrameLayout mPtrFrameLayoutView;
    public UltraPullRefreshHeaderHelper mPtrHelper;
    protected LinearLayout parent;
    protected RequestFailView mRequestFailView;

    private TextView mChangeCityView;
    private ImageView mToHomeView;
    private boolean showToHome = false;

    android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == 1) mWebView.reload();

            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** getIntent().getBundleExtra()不为空，那么getIntent.getExtras()一定也不会空 **/
        if (getIntent().getExtras() != null) {
            DebugLog.e("TEst", "getExtras is not null");
        } else {
            DebugLog.e("TEst", "getExtras is null");
        }

        if (getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA) != null) {
            DebugLog.e("TEst", "getBundleExtras is not null");
        } else {
            DebugLog.e("TEst", "getBundleExtras is null");
        }

        showToHome = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA) != null ? getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getBoolean(SHOWTOHOME, false) : getIntent().getExtras() != null ? getIntent().getExtras().getBoolean(SHOWTOHOME, false) : false;

        setContentView(R.layout.activity_groupbuy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mChangeCityView = (TextView) findViewById(R.id.change_city);
        mChangeCityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherThirdPartShop.toSpecialCarChoosePositon(GroupBuyMergeActivity1.this, 1, ((TextView) v).getText().toString());
            }
        });

        mToHomeView = (ImageView) findViewById(R.id.to_home);
        mToHomeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitchBase.toMain(GroupBuyMergeActivity1.this, 0);
            }
        });

        if (showToHome) {
            mToHomeView.setVisibility(View.VISIBLE);
            mChangeCityView.setVisibility(View.GONE);
        }

        parent = (LinearLayout) findViewById(R.id.ll_parent);
        mWebView = (WebView) findViewById(R.id.web_view);

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
                    mWebView.loadUrl(url);
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

        initSharedPreferences();
        initLocationData();

        loadUrl();
    }


    boolean needClearHistory;
    SharedPreferencesHelper sharedPreferencesHelper;
    AreaSiteUtil areaSiteUtil;
    String cityGroupID;
    String areaGroup;


    protected void initSharedPreferences() {
        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        areaSiteUtil = AreaSiteUtil.getInstance(this);

        if (sharedPreferencesHelper.getHistoryCityForSpecialCar().size() > 0 && !TextUtils.isEmpty(sharedPreferencesHelper.getHistoryCityForSpecialCar().getFirst())) {
            cityGroupID = areaSiteUtil.getCityGroupID(sharedPreferencesHelper.getHistoryCityForSpecialCar().getFirst());
        } else {
            cityGroupID = sharedPreferencesHelper.getDefaultSiteData().siteGroupID;
        }
    }


    protected void initLocationData() {
        String historyCity;
        String city = sharedPreferencesHelper.getCity();
        LinkedList< String > historyCityList = sharedPreferencesHelper.getHistoryCityForSpecialCar();
        if (!historyCityList.isEmpty() && !TextUtils.isEmpty(historyCity = historyCityList.getFirst())) {
            mChangeCityView.setText(historyCity);
            cityGroupID = areaSiteUtil.getCityGroupID(historyCity);
            if (!areaSiteUtil.getCityGroup(city).equals(historyCity)) {
                startSettingDialog(city);
            }
            return;
        }

        mChangeCityView.setText(areaGroup = areaSiteUtil.getWHSiteName());
        sharedPreferencesHelper.setHistoryCityForSpecialCar(areaGroup);

        if (areaSiteUtil.getCityGroup(city).endsWith(areaSiteUtil.getWHSiteName())) {
            sharedPreferencesHelper.setCityForSpecialCar(city);
            sharedPreferencesHelper.setHistoryProvinceForSpecialCar(sharedPreferencesHelper.getProvince());
        } else {
            startSettingDialog(city);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 1) {
            String position;
            if (!TextUtils.isEmpty(position = data.getStringExtra("position"))) {
                if (data.getBooleanExtra("clickFromPositionCity", false)) {
                    position = areaSiteUtil.getCityGroup(position);
                }
                if (position.equals(mChangeCityView.getText().toString())) {
                    return;
                } else {
                    mChangeCityView.setText(position);
//                    mWebView.clearHistory(); // 清除历史回退栈
                    needClearHistory = true;
                }

                areaGroup = position;
                cityGroupID = areaSiteUtil.getCityGroupID(areaGroup);
                loadUrl();
            }
        }
    }


    /**
     * @param city 当前城市
     * @return
     */
    private void startSettingDialog(final String city) {
        if (TextUtils.isEmpty(city) || sharedPreferencesHelper.getPositionTranslateForSiteGroup()) {
            return;
        } else {
            sharedPreferencesHelper.setPositionTranslateForSiteGroup(true);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MaterialDialog);
        builder.setCancelable(false);
        builder.setTitle("提示");
        if (areaSiteUtil.adjustCity(city)) {
            builder.setMessage("您当前城市属于【" + (appendWordToStr(areaSiteUtil.getCityGroup(city))) + "】" + ",是否需要进行数据切换?"); // 您当前城市是【%@】，需要切换吗？
        } else {
            builder.setMessage("您当前城市是【" + city + "】,不在分站列表中,已为您切换到【" + (appendWordToStr(areaSiteUtil.getWHSiteName())) + "】"); // 您当前城市是【%@】，需要切换吗？
        }
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (areaSiteUtil.adjustCity(city)) {
                    areaGroup = areaSiteUtil.getCityGroup(city);
                    sharedPreferencesHelper.setCityForSpecialCar(city);
                    sharedPreferencesHelper.setHistoryProvinceForSpecialCar(areaSiteUtil.getCityProvince(city));

//                    mWebView.clearHistory(); // 清除历史回退栈
                    needClearHistory = true;
                } else {
                    areaGroup = areaSiteUtil.getWHSiteName();
                    sharedPreferencesHelper.setCityForSpecialCar(sharedPreferencesHelper.getDefaultSiteData().siteAreaName);
                    sharedPreferencesHelper.setHistoryProvinceForSpecialCar(areaSiteUtil.getCityProvince("湖北省"));
                }
                sharedPreferencesHelper.setSpecialCarAreaHistoryPinYing(areaSiteUtil.getPinYin(areaGroup));
                cityGroupID = areaSiteUtil.getCityGroupID(areaGroup);
//                baseFilterController.mFilterMap.put("siteID", cityGroupID);
                mChangeCityView.setText(areaGroup);
                sharedPreferencesHelper.setHistoryCityForSpecialCar(areaGroup);
//                getData(true);
                loadUrl();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).create().show();
    }


    public void loadUrl() {
        String site = new BaseSharedPreferencesHelper(GroupBuyMergeActivity1.this).getSpecialCarAreaHistoryPinYing();
        String url = new ThirdPartShopClient().getGrouponURL(site);

        this.setTitle("团购汇");

        if (!TextUtils.isEmpty(url)) {
            DebugLog.e(getClass().getName(), url);
            if (url.startsWith("/")) {
                mWebView.loadUrl(ApiUtil.AccountHostURL + url);
            } else {
                mWebView.loadUrl(url);
            }
        }
    }


    private void showFailView() {
        mRequestFailView.setVisibility(View.VISIBLE);
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

}
