package com.hxqc.mall.thirdshop.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.BaseInfo;
import com.hxqc.mall.thirdshop.model.SpecialAutoInfo;
import com.hxqc.mall.thirdshop.model.SpecialCarDetailBean;
import com.hxqc.mall.thirdshop.views.TimeCounterView;
import com.hxqc.util.DisplayTools;
import com.hxqc.util.JSONUtils;

/**
 * Function: 特价车中的车辆详情Activity
 *
 * @author 袁秉勇
 * @since 2016年05月05日
 */
public class SpecialCarDetailActivity extends BackActivity implements TimeCounterView.CallBack {
    private final static String TAG = SpecialCarDetailActivity.class.getSimpleName();
    private Context mContext;

    public static final String SALEAREA = "saleArea";
    public static final String ISSTARTED = "isStarted";
    public static final String IMGURL = "imgUrl";
    public static final String CARNAME = "carName";
    public static final String INTRODUCE = "introduce";
    public static final String ITEMID = "seckillID";

    private ImageView mSaleAreaView, mCarImageView;
    private TimeCounterView mTimeCountView;
    private TextView mCarNameView;

    private String saleArea;
    private String imgUrl;
    private String carName;
    private String introduce;
    private String itemID;

    private ImageView loadingRoundView;
    private WebView mIntroduceView;

    private ThirdPartShopClient ThirdPartShopClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        final Bundle bundle = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
        saleArea = bundle.getString(SALEAREA, "");
        imgUrl = bundle.getString(IMGURL, "");
        carName = bundle.getString(CARNAME, "");
        introduce = bundle.getString(INTRODUCE, "");
        itemID = bundle.getString(ITEMID, "");

        setContentView(R.layout.activity_special_car_detail);

        /** 头部图片相关 **/
        mSaleAreaView = (ImageView) findViewById(R.id.sale_area);
        mCarImageView = (ImageView) findViewById(R.id.banner);
        mTimeCountView = (TimeCounterView) findViewById(R.id.time_counter);
        mTimeCountView.setCallBack(this);
        mCarNameView = (TextView) findViewById(R.id.auto_descriptions);

        mIntroduceView = (WebView) findViewById(R.id.web_view);

        loadingRoundView = (ImageView) findViewById(R.id.iv_item_detail_round);

        ImageUtil.setImage(mContext, mCarImageView, imgUrl, R.drawable.sliderimage_pic_normal_slider);
        if ("10".equals(saleArea)) {
            mSaleAreaView.setImageResource(R.drawable.spcial_car_province);
        } else if ("20".equals(saleArea)) {
            mSaleAreaView.setImageResource(R.drawable.spcial_car_country);
        } else {
            mSaleAreaView.setImageResource(R.drawable.icon_range);
        }

        mCarNameView.setText(carName);

        BaseInfo baseInfo = new BaseInfo();
        baseInfo.introduce = introduce;
        setAutoDetail(baseInfo);

        if (SpecialCarDetailMainActivity.TIMER > 0 && !mTimeCountView.isRun()) {
            mTimeCountView.post(new Runnable() {
                @Override
                public void run() {
                    mTimeCountView.setDate(bundle.getBoolean(ISSTARTED, true), SpecialCarDetailMainActivity.TIMER);
                    mTimeCountView.run();
                }
            });
        } else if (SpecialCarDetailMainActivity.TIMER == 0 && !getIntent().getBooleanExtra(ISSTARTED, true)) {
            mTimeCountView.showStartStyle();
        } else {
            mTimeCountView.showEndStyle();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimeCountView.isRun()) mTimeCountView.stopCount();
    }


    private void getData() {
        if (ThirdPartShopClient == null) ThirdPartShopClient = new ThirdPartShopClient();
        ThirdPartShopClient.getSeckillDetail(itemID, new LoadingAnimResponseHandler(mContext) {
            @Override
            public void onSuccess(String response) {
                SpecialCarDetailBean specialCarDetailBean = JSONUtils.fromJson(response, SpecialCarDetailBean.class);
                if (specialCarDetailBean != null) {
                    if (specialCarDetailBean.autoInfo != null) updateTimer(specialCarDetailBean.autoInfo);
                }
            }
        });
    }


    /** 更新计时器 **/
    private void updateTimer(SpecialAutoInfo autoInfo) {
        if (!autoInfo.isEnded) {
            if (autoInfo.isStarted && !mTimeCountView.isRun()) {
                mTimeCountView.setDate(autoInfo.isStarted, (Long.valueOf(autoInfo.endTime) - Long.valueOf(autoInfo.serverTime)));
                mTimeCountView.run();
            } else if (!autoInfo.isStarted && !mTimeCountView.isRun()) {
                mTimeCountView.setDate(autoInfo.isStarted, (Long.valueOf(autoInfo.startTime) - Long.valueOf(autoInfo.serverTime)));
                mTimeCountView.run();
            }
        } else {
            mTimeCountView.showEndStyle();
        }
    }


    public void setAutoDetail(BaseInfo baseInfo) {
        introduceConfig(baseInfo);
    }


    /**
     * 重写webview
     */
    public void reWriteWebview() {
        mIntroduceView.setWebViewClient(new AutoWebViewClient());
    }


    /**
     * 车辆详情设置
     */
    public void introduceConfig(BaseInfo baseInfo) {

        reWriteWebview();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mIntroduceView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            mIntroduceView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        }
        mIntroduceView.getSettings().setLoadWithOverviewMode(true);
        mIntroduceView.getSettings().setJavaScriptEnabled(true);
        mIntroduceView.getSettings().setUseWideViewPort(true);
        mIntroduceView.getSettings().setDefaultTextEncodingName("UTF-8");

        String value = "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "\t<meta name=\"viewport\" content=\"initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui\">\n" +
                "<style type='text/css'>" +
                "body {margin:0;padding:0} " +
                "p {margin:0;padding:0} " +
                " </style>" +
                "  </head>\n" +
                "  <body>\n";
        value += Html.fromHtml(baseInfo.getIntroduce()) + "</body></html>";
        value = value.replace("<img", "<img width=\"" + ((this).getWindowManager().getDefaultDisplay().getWidth() / (int) DisplayTools.getScreenDensity(this)) + "\"");
//        mIntroduceView.loadData(value, "text/html", "UTF-8");
        mIntroduceView.loadDataWithBaseURL(null, value, "text/html", "utf-8", null);
    }


Animation animation;


    /**
     * 开始loading动画
     */
    public void startLoadingAnim() {
        if (loadingRoundView == null) return;
        loadingRoundView.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        LinearInterpolator interpolator = new LinearInterpolator();
        animation.setInterpolator(interpolator);
        loadingRoundView.startAnimation(animation);
    }


    /**
     * 停止 并 隐藏loading动画
     */
    public void cancelLoadingAnim() {

        if (animation != null) animation.cancel();

        if (loadingRoundView != null) {
            loadingRoundView.clearAnimation();
            loadingRoundView.setVisibility(View.GONE);
        }

        animation = null;
    }


    @Override
    public void callBack() {
        getData();
    }


private class AutoWebViewClient extends WebViewClient {
    @Override
    public void onPageFinished(WebView view, String url) {
//            DebugLog.i("Test_auto", "---->" + "onPageFinished");
        cancelLoadingAnim();
        super.onPageFinished(view, url);
    }


    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            DebugLog.i("Test_auto", "---->" + "onPageStarted");
        startLoadingAnim();
        super.onPageStarted(view, url, favicon);
    }


}


    /** 图集 **/
    public void toPictures(View view) {
//        ActivitySwitcherThirdPartShop.toAtlas(mContext, itemID);
    }
}
