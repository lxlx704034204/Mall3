package com.hxqc.autonews.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import com.hxqc.mall.activity.WebActivity;
import com.hxqc.mall.core.db.carcomparedb.ChooseCarModel_Table;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hxqc.autonews.model.AutoInfoDetailModel;
import com.hxqc.autonews.model.pojos.AutoImage;
import com.hxqc.autonews.model.pojos.AutoInfoData;
import com.hxqc.autonews.model.pojos.AutoInfoDetail;
import com.hxqc.autonews.model.pojos.AutoInformation;
import com.hxqc.autonews.presenter.Presenter;
import com.hxqc.autonews.util.ActivitySwitchAutoInformation;
import com.hxqc.autonews.util.AutoDetailHelper;
import com.hxqc.autonews.util.NewAutoDetailHelper;
import com.hxqc.autonews.util.ToAutoInfoDetailUtil;
import com.hxqc.autonews.view.RequestDataWithCacheHandler;
import com.hxqc.autonews.widget.EvaluationBar;
import com.hxqc.autonews.widget.WriteEvaluationPopWindow;
import com.hxqc.carcompare.db.DAO;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.BuildConfig;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.db.carcomparedb.ChooseCarModel;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.views.ProgressWebView;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.utils.AreaSiteUtil;
import com.hxqc.socialshare.manager.ShareController;
import com.hxqc.socialshare.pojo.ShareContent;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;
import java.util.List;

import hxqc.mall.R;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static com.hxqc.mall.core.views.ProgressWebView.*;

/**
 * Author:李烽
 * Date:2016-09-01
 * FIXME
 * Todo 汽车资讯详情 （图文类型的汽车资讯）
 */
public class AutoInfoDetailActivity extends BackActivity implements RequestDataWithCacheHandler<AutoInfoDetail>, OnRefreshHandler, View.OnClickListener, EvaluationBar.OnMessageClickListener, EvaluationBar.OnShareClickListener {

    public static final String INFO_ID = "infoID";
    public static final String FROM = "from";
    public static final int AUTO_CALENDAR = 0001;
    public static final int AUTO_INFO = 0002;
    private ShareContent shareContent;
    private ShareController shareController;
    private ProgressWebView mWebView;
    private PtrFrameLayout mPtrFrameLayoutView;
    private UltraPullRefreshHeaderHelper mPtrHelper;
    private WebSettings settings;

    private Presenter mPresenter;
    private String infoID = "";
    private ArrayList<AutoImage> imgs = new ArrayList<>();

    private String htmlString = "";
    private AutoInfoDetail data;

    private Context mContext;
    private RequestFailView mRequestFailView;
    private EvaluationBar evaluationBar;
    private ImageButton fabAutoMatch;
    private int from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_auto_info_detail);
        fabAutoMatch = (ImageButton) findViewById(R.id.fab_auto_match);
        fabAutoMatch.setOnClickListener(this);
        evaluationBar = (EvaluationBar) findViewById(R.id.evaluation_bar);
        evaluationBar.setOnClickListener(this);
        evaluationBar.setOnMessageClickListener(this);
        evaluationBar.setOnShareClickListener(this);
        mRequestFailView = (RequestFailView) findViewById(R.id.request_fail_view);
        mRequestFailView.setFailButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        initWebView();
        mPresenter = new Presenter();
        if (shareController == null) {
            shareController = new ShareController(this);
        }
        infoID = getInfoID();
        from = getFrom();
        loadData();
    }

    private String getInfoID() {
        if (getIntent() != null) {
            if (getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA) != null) {
                infoID = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(INFO_ID);
            }
        }
        return infoID;
    }

    public int getFrom() {
        if (getIntent() != null)
            if (getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA) != null) {
                from = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getInt(FROM, AUTO_INFO);
            }
        return from;
    }

    private void loadData() {
        mPresenter.getAutoInfoDetail(infoID, this, new AutoInfoDetailModel(this));
    }

    private void initWebView() {
        mWebView = (ProgressWebView) findViewById(R.id.web_view);

        mPtrFrameLayoutView = (PtrFrameLayout) findViewById(R.id.refresh_frame);
        mPtrHelper = new UltraPullRefreshHeaderHelper(this, mPtrFrameLayoutView);
        mPtrHelper.setOnRefreshHandler(this);
        settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

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
                mWebView.loadUrl(url);

                return true;
            }
        });
        mWebView.setWebChromeClient(mWebView.new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {

                }
            }
        });
        //模拟数据
//        mWebView.loadUrl("file:///android_asset/test.html");
//        mWebView.addJavascriptInterface(new ToDetail(), "ToDetail");
//        mWebView.addJavascriptInterface(new ToAutoGallery(), "ToAutoGallery");
//        mWebView.addJavascriptInterface(new ToAutoInfoDetail(), "ToAutoInfoDetail");
//        mWebView.addJavascriptInterface(new ToCompare(), "ToCompare");

        mWebView.addJavascriptInterface(new JavaScriptMonth(), "JavaScriptMonth");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.evaluation_bar:
                UserInfoHelper.getInstance().loginAction(mContext, new UserInfoHelper.OnLoginListener() {
                    @Override
                    public void onLoginSuccess() {
                        showEvaluationEditTextWindow();
                    }
                });
                break;
            case R.id.fab_auto_match:
                //车型对比
//                if (data.data != null && !data.data.isEmpty()) {
//                    AutoInfoData autoInfoData = data.data.get(0);
//                    ActivitySwitchAutoInformation.toCarCompare(mContext, autoInfoData.extID,
//                            autoInfoData.brandName, autoInfoData.modelName);
//                } else {
                ActivitySwitchAutoInformation.toCarCompare(mContext, null, null, null);
//                }
                break;
        }
    }

    private WriteEvaluationPopWindow popWindow;

    private void showEvaluationEditTextWindow() {
        if (popWindow == null) {
            popWindow = new WriteEvaluationPopWindow(this, infoID);
        }
        popWindow.showAtLocation(findViewById(R.id.root_layout),
                Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onMessageClick() {
        //去评论页面
//        if (data.commentCount > 0)
        ActivitySwitchAutoInformation.toMessageCommentList(this, infoID, (int) data.commentCount);
    }

    @Override
    public void onShareClick() {
        share();
    }

    private List<Boolean> compareAutos;

    final class JavaScriptMonth {
        @JavascriptInterface
        public void toDetail(final int index) {
            //  ToastHelper.showRedToast(AutoInfoDetailActivity.this, str);
            DebugLog.i(getClass().getSimpleName(), "runOnAndroidJavaScript:ToDetail" + index);
            AutoInfoData autoInfoData = data.data.get(index);
            String siteID = AreaSiteUtil.getInstance(mContext).getSiteID();
            if (autoInfoData != null) {
                String dataType = autoInfoData.dataType;
                if (dataType.equals("10")) {
                    //// TODO: 2016/10/12   autoInfoData.autoModelID 待验证 by zhaofan
                    ActivitySwitcherThirdPartShop
                            .toNewCarModelDetail(mContext, siteID, autoInfoData.extID, autoInfoData.brandName, autoInfoData.modelName);
                } else {
                    ActivitySwitcherThirdPartShop
                            .toNewCarModelList2(mContext, siteID, autoInfoData.brandName, autoInfoData.seriesName);
                }
            }
        }

        @JavascriptInterface
        public void toAutoGalley(final int index) {
            DebugLog.d(getClass().getSimpleName(), "runOnAndroidJavaScript:ToAutoGallery:index:" + index);
            ActivitySwitchAutoInformation.toAutoImages(AutoInfoDetailActivity.this, imgs, index);
        }

        @JavascriptInterface
        public void toAutoInfoDetail(final int index) {
            AutoInformation autoInformation = data.relevant.get(index);
            ToAutoInfoDetailUtil.onToNextPage(autoInformation.infoID, autoInformation.getType(),
                    AutoInfoDetailActivity.this);
        }

        @JavascriptInterface
        public void toCompare(final int index) {
            //添加//删除
//            Toast.makeText(mContext, "index:" + index, Toast.LENGTH_SHORT).show();
            ArrayList<AutoInfoData> data = AutoInfoDetailActivity.this.data.data;
            AutoInfoData autoInfoData = data.get(index);
            Boolean aBoolean = compareAutos.get(index);
            if (autoInfoData != null) {
                if (aBoolean) {
                    DAO.deleteCarDb(autoInfoData.extID);
                    changeUI(index);
                } else
                    DAO.addCompareCarDb(autoInfoData.brandName, autoInfoData.seriesName, autoInfoData.extID, new DAO.onSuccessCallBack() {
                        @Override
                        public void onSaveSuccess() {
                            changeUI(index);
                        }
                    });
            }
        }
    }

    private void changeUI(final int index) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript:toCompare(" + index + ")");
                compareAutos.set(index, !compareAutos.get(index));//记住当前的车型
            }
        });
    }

//    final class ToDetail {
//
//        @JavascriptInterface
//        public void runOnAndroidJavaScript(final int index) {
////            ToastHelper.showRedToast(AutoInfoDetailActivity.this, str);
//            DebugLog.i(getClass().getSimpleName(), "runOnAndroidJavaScript:ToDetail" + index);
//            AutoInfoData autoInfoData = data.data.get(index);
//            String siteID = AreaSiteUtil.getInstance(mContext).getSiteID();
//            if (autoInfoData != null) {
//                String dataType = autoInfoData.dataType;
//                if (dataType.equals("10")) {
//                    //// TODO: 2016/10/12   autoInfoData.autoModelID 待验证 by zhaofan
//                    ActivitySwitcherThirdPartShop
//                            .toNewCarModelDetail(mContext, siteID, autoInfoData.extID, autoInfoData.brandName, autoInfoData.modelName);
//                } else {
//                    ActivitySwitcherThirdPartShop
//                            .toNewCarModelList2(mContext, siteID, autoInfoData.brandName, autoInfoData.seriesName);
//                }
//            }
//        }
//    }

//    final class ToAutoGallery {
//
//        @JavascriptInterface
//        public void runOnAndroidJavaScript(final int index) {
//            DebugLog.d(getClass().getSimpleName(), "runOnAndroidJavaScript:ToAutoGallery:index:" + index);
//            ActivitySwitchAutoInformation.toAutoImages(AutoInfoDetailActivity.this, imgs, index);
//        }
//    }


//    final class ToAutoInfoDetail {
//        @JavascriptInterface
//        public void runOnAndroidJavaScript(final int index) {
//            AutoInformation autoInformation = data.relevant.get(index);
//            ToAutoInfoDetailUtil.onToNextPage(autoInformation.infoID, autoInformation.getType(),
//                    AutoInfoDetailActivity.this);
//        }
//    }

//    final class ToCompare {
//        @JavascriptInterface
//        public void runOnAndroidJavaScript(final int index) {
//            //添加//删除
////            Toast.makeText(mContext, "index:" + index, Toast.LENGTH_SHORT).show();
//            ArrayList<AutoInfoData> data = AutoInfoDetailActivity.this.data.data;
//            AutoInfoData autoInfoData = data.get(index);
//            if (autoInfoData != null) {
//                DAO.addCompareCarDb(autoInfoData.brandName, autoInfoData.seriesName, autoInfoData.extID, new DAO.onSuccessCallBack() {
//                    @Override
//                    public void onSaveSuccess() {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mWebView.loadUrl("javascript:toCompare(" + index + ")");
//                            }
//                        });
//                    }
//                });
//            }
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_auto_info_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_to_share) {
            share();
        }
        return false;
    }

    private void share() {
        if (shareContent != null) {
            shareController.showSharePopupWindow(shareContent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (shareController != null) {
            shareController.onActivityResult(this, requestCode, resultCode, data);
        }
    }

    private void initImgs(AutoInfoDetail data) {
        ArrayList<String> bodyImg = data.bodyImg;
        for (String s : bodyImg) {
            AutoImage autoImage = new AutoImage();
            autoImage.largeURL = s;
            autoImage.description = "";
            imgs.add(autoImage);
        }
    }

    private void refrashData(AutoInfoDetail detail) {
        mRequestFailView.setVisibility(View.GONE);
        mPtrFrameLayoutView.setVisibility(View.VISIBLE);
        this.data = detail;
        initImgs(this.data);
//        htmlString = AutoDetailHelper.completeHtml(data);
        htmlString = NewAutoDetailHelper.completeHtml(this.data, from);
//        DebugLog.d(getClass().getSimpleName(), htmlString);
        mWebView.loadDataWithBaseURL(null, htmlString, "text/html", "UTF-8", null);
        if (this.data.share != null) {
            shareContent = this.data.share;
        }
        evaluationBar.setCount((int) detail.commentCount);
        mPtrHelper.refreshComplete(mPtrFrameLayoutView);
        initCompareAutos();
    }

    private void initCompareAutos() {
        //记录添加对比的车辆
        compareAutos = new ArrayList<>();
        ArrayList<AutoInfoData> data = this.data.data;
        for (int i = 0; i < data.size(); i++) {
            AutoInfoData autoInfoData = data.get(i);
            List<ChooseCarModel> chooseCarModels =
                    DAO.queryCarCompareList(ChooseCarModel_Table.extId.eq(autoInfoData.extID));
            if (chooseCarModels.isEmpty())
                compareAutos.add(false);
            else {
                compareAutos.add(true);
                mWebView.loadUrl("javascript:toCompare(" + i + ")");
            }
        }
    }


    @Override
    public void onDataNull(String message) {
        mRequestFailView.setVisibility(View.VISIBLE);
        mPtrFrameLayoutView.setVisibility(View.GONE);
        mPtrHelper.refreshComplete(mPtrFrameLayoutView);
    }

    @Override
    public void onDataResponse(AutoInfoDetail detail) {
        refrashData(detail);
    }

    @Override
    public void onCacheDataBack(AutoInfoDetail detail) {
        refrashData(detail);
    }

    @Override
    public void onCacheDataNull() {

    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (popWindow != null && popWindow.isShowing())
            popWindow.dismiss();
        else
            finish();
    }
}
