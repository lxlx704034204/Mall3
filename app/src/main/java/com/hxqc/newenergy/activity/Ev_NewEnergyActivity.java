package com.hxqc.newenergy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.db.areacacheutil_new.AreaCacheUtil;
import com.hxqc.mall.core.model.HomeSlideADModel;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.newenergy.api.NewEnergyApiClient;
import com.hxqc.newenergy.bean.EVNewenergyAutoSample;
import com.hxqc.newenergy.bean.EVRankingSchema;
import com.hxqc.newenergy.bean.PromotionAuto;
import com.hxqc.newenergy.bean.RecommendBean;
import com.hxqc.newenergy.util.ActivitySwitcherEV;
import com.hxqc.newenergy.util.EVSharePreferencesHelper;
import com.hxqc.newenergy.view.DealsView;
import com.hxqc.newenergy.view.EvHomeModuleView;
import com.hxqc.newenergy.view.MyScrollview;
import com.hxqc.newenergy.view.NewEnergyRanklingView;
import com.hxqc.newenergy.view.RecommendView;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;
import java.util.LinkedList;

import cz.msebera.android.httpclient.Header;
import hxqc.mall.R;

/**
 * 说明： 新能源模块首页Activity
 * author: 何玉
 * since: 2016年3月9日
 * Copyright:恒信汽车电子商务有限公司
 */
public class Ev_NewEnergyActivity extends NoBackActivity implements OnClickListener, OnRefreshHandler, BaseSliderView.OnSliderClickListener {

	//    long times[] ;
	protected AreaCacheUtil areaCacheUtil;                  //城市缓存工具类
	BackActivity m;
	EvHomeModuleView mDealsModuleView;//特卖
	LinearLayout mDealse_layout;
	private TextView mChangeCityView;//选择city
    private NewEnergyApiClient mNewEnergy_ApiClient;        //网络请求工具
    private EVSharePreferencesHelper EVSharePreferencesHelper; //
    private SliderLayout mSliderView;                       //banner 广告栏
    private String choosedCity;                             //城市
    private LinearLayout mRecommendedLayout;                //推荐列表容器
    private NewEnergyRanklingView mNewEnergyRanklingView;    //排行榜列表
    private TextView mTitle;
    private MyScrollview mNewenergy_ScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ev_newwenergy_layout);
        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) findViewById(R.id.newener_title);
        mToolBar.setTitle("");
        ((TextView) findViewById(R.id.newener_title)).setText(getTitle());
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        EVSharePreferencesHelper = new EVSharePreferencesHelper(Ev_NewEnergyActivity.this);
        mNewEnergy_ApiClient = new NewEnergyApiClient();

        //---------------------
        mRecommendedLayout = (LinearLayout) findViewById(R.id.recommended_layout);
        mDealsModuleView = (EvHomeModuleView) findViewById(R.id.deals_module);
        mDealsModuleView.setModuleTitle("新能源车特卖");
        mDealsModuleView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherEV.toTemaiActivity(Ev_NewEnergyActivity.this);
            }
        });
        mSliderView = (SliderLayout) findViewById(R.id.slider_home);

        mNewEnergyRanklingView = (NewEnergyRanklingView) findViewById(R.id.NewEnergyRanklingView);
        mNewEnergyRanklingView.setModuleTitle("新能源车排行");
        mChangeCityView = (TextView) findViewById(R.id.change_city);
        mSliderView = (SliderLayout) findViewById(R.id.slider_home);
        mChangeCityView.setOnClickListener(this);


        getAD();
        /** 获取历史定位城市数据并上传city */
        initLocationData();
        getRecommendData();
        getSpecialOffer();
        getRankingData();
    }


    private void initLocationData() {
        areaCacheUtil = AreaCacheUtil.getInstance(this);

        if (EVSharePreferencesHelper == null) {
            EVSharePreferencesHelper = new EVSharePreferencesHelper(this);
        }

        String city = EVSharePreferencesHelper.getCity(); // 当前定位城市
        LinkedList< String > historyCityList = EVSharePreferencesHelper.getHistoryCity(); // 历史选择城市列表
        LinkedList< String > evHistoryList = EVSharePreferencesHelper.getNewEnergyHistoryCity();

        if (historyCityList.isEmpty() || TextUtils.isEmpty(historyCityList.getFirst())) {
            commonFunction(evHistoryList, city, "");
        } else {
            String historyCity = historyCityList.getFirst();
            if (areaCacheUtil.checkExistCity(AreaCacheUtil.NEWENERGY, historyCity)) {
                mChangeCityView.setText(historyCity);
                choosedCity = historyCity;
                if (!TextUtils.isEmpty(city) && !city.equals(historyCity)) {
                    startSettingDialog(city);
                }
            } else {
                commonFunction(evHistoryList, city, historyCity);
            }

            if (choosedCity.equals(EVSharePreferencesHelper.getCity()) && areaCacheUtil.checkExistCity(AreaCacheUtil.NEWENERGY, choosedCity)) {
                showLocationIcon(true);
            } else {
                showLocationIcon(false);
            }
        }
    }

    /** 定位代码块中相同的代码 **/
    private void commonFunction(LinkedList< String > evHistoryList, String city, String historyCity) {
        if (evHistoryList.isEmpty() || TextUtils.isEmpty(evHistoryList.getFirst())) {
            if (!TextUtils.isEmpty(city) && areaCacheUtil.checkExistCity(AreaCacheUtil.NEWENERGY, city)) {
                mChangeCityView.setText(city);
                choosedCity = city;
                if (!TextUtils.isEmpty(historyCity) && !TextUtils.isEmpty(city) && !city.equals(historyCity)) {
                    startSettingDialog(city);
                }
            } else {
                mChangeCityView.setText("武汉市"); // 针对第一次使用App，进入新能源时，历史城市为空的情况
                choosedCity = "武汉市";
                EVSharePreferencesHelper.setHistoryCity(choosedCity);
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MaterialDialog);
                builder.setCancelable(false);
                builder.setTitle("提示")
                        .setMessage("当前城市暂未开通该功能，已自动为您获取 【武汉市】 数据")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            }
        } else {
            mChangeCityView.setText(evHistoryList.getFirst());
            choosedCity = evHistoryList.getFirst();
        }
    }

    /**
     * @param city
     *         当前城市
     * @return
     */
    private void startSettingDialog(final String city) {
        if (TextUtils.isEmpty(city) || EVSharePreferencesHelper.getPositionTranslate()) {
            return;
        } else {
            EVSharePreferencesHelper.setPositionTranslate(true);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MaterialDialog);
        builder.setCancelable(false);
        builder.setTitle("提示").
                setMessage("您当前城市是【" + city + "】,需要切换吗？") // 您当前城市是【%@】，需要切换吗？
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showLocationIcon(true);
                        mChangeCityView.setText(city);
                        choosedCity = city;
                        EVSharePreferencesHelper.setHistoryCity(city);
                        getRecommendData();
                        getSpecialOffer();
                        getRankingData();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            String position;
            if (!TextUtils.isEmpty(position = data.getStringExtra("position"))) {
                if (position.equals(mChangeCityView.getText().toString())) {
                    return;
                }

                areaCacheUtil = AreaCacheUtil.getInstance(this);
                if (!areaCacheUtil.checkExistCity(AreaCacheUtil.NEWENERGY, position))
                    choosedCity = position = "武汉市";

                mChangeCityView.setText(position);

                if (position.equals(EVSharePreferencesHelper.getCity())) {
                    showLocationIcon(true);
                } else {
                    showLocationIcon(false);
                }

                getRecommendData();
                getSpecialOffer();
                getRankingData();
            }
        }
    }

    public void showLocationIcon(boolean flag) {
        if (mChangeCityView == null) return;
        if (flag) {
            if ((mChangeCityView.getCompoundDrawables())[0] == null)
                mChangeCityView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(com.hxqc.mall.thirdshop.R.drawable.t_icon_button_location), null, null, null);
        } else {
            if (mChangeCityView.getCompoundDrawables().length > 0) {
                mChangeCityView.setCompoundDrawables(null, null, null, null);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (areaCacheUtil != null) areaCacheUtil.close();
        if (mNewEnergyRanklingView != null) mNewEnergyRanklingView.removeAllViews();
        mSliderView.destroy();

        stopTimerView();
    }


    /**
     * 关闭特卖时间计算
     */
    private void stopTimerView() {
        int childCount = mDealsModuleView.getContentView().getChildCount();
        if (childCount != 0) {
            View view = null;
            for (int i = 0; i < childCount; i++) {
                view = mDealsModuleView.getContentView().getChildAt(i);
                if (view instanceof DealsView) {
                    ((DealsView) view).stopTimer();
                }
            }
        }
    }

    private synchronized void getAD() {
        mNewEnergy_ApiClient.getBannerData(choosedCity, EVSharePreferencesHelper.getLatitudeBD(),
                EVSharePreferencesHelper.getLongitudeBD(), new LoadingAnimResponseHandler(this, true) {
                    @Override
                    public void onSuccess(String response) {
                        ArrayList< HomeSlideADModel > homeSlideADModels = JSONUtils.fromJson(response,
                                new TypeToken< ArrayList< HomeSlideADModel > >() {
                                });

                        if (homeSlideADModels.size() > 8) {
                            homeSlideADModels.subList(0, 8);
                        }

                        if (homeSlideADModels.size() > 0) {
                            sliderAD(homeSlideADModels);
                        } else {
                            mSliderView.setSliderOnlyOneView("");
                            mSliderView.setEnabled(false);
                            mSliderView.setFocusableInTouchMode(false);
                            mSliderView.setFilterTouchesWhenObscured(false);
                            mSliderView.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        sliderAD(null);
                    }

                }
        );
    }

    private void sliderAD(ArrayList< HomeSlideADModel > datas) {
        if(datas==null){

            mSliderView.setSliderOnlyOneView(null);
            mSliderView.setEnabled(false);
            mSliderView.setFocusableInTouchMode(false);
            mSliderView.setFilterTouchesWhenObscured(false);
            mSliderView.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
            return;
        }
        if (datas.size() == 1) {
            final HomeSlideADModel item = datas.get(0);
            mSliderView.setSliderOnlyOneView(item.slide);
            mSliderView.setEnabled(false);
            mSliderView.setFocusableInTouchMode(false);
            mSliderView.setFilterTouchesWhenObscured(false);
            mSliderView.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
            mSliderView.sliderOnlyOneView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sliderClickOperate(item);
                }
            });
        } else {
            for (int i = 0; i < datas.size(); i++) {
                HomeSlideADModel item = datas.get(i);
                DefaultSliderView textSliderView = new DefaultSliderView(this);
                // initialize a SliderLayout
                textSliderView.empty(R.drawable.sliderimage_pic_normal_slider).error(R.drawable.sliderimage_pic_normal_slider);
                textSliderView.description(i + "").image(item.slide).
                        setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(this);
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putParcelable("extra", item);
                mSliderView.addSlider(textSliderView);
            }
            mSliderView.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mSliderView.setCustomAnimation(new DescriptionAnimation());
            mSliderView.setDuration(5000);
        }
    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {
        // TODO: 2015/8/17
        HomeSlideADModel item = baseSliderView.getBundle().getParcelable("extra");
        sliderClickOperate(item);

    }

    void sliderClickOperate(HomeSlideADModel item) {
        if (item != null && item.isPromotion()) {
            ActivitySwitcher.toEventDetail(this, item.url);
        } else {
            assert item != null;
            if (item.isSeckill()) {
                ActivitySwitcher.toAutoItemDetail(this, AutoItem.AUTO_PROMOTION, item.getID(), item.getProductName());
            } else {
                ActivitySwitcher.toAutoItemDetail(this, AutoItem.AUTO_COMMON, item.getID(), item.getProductName());
            }
        }
    }

    /**
     * 推荐车辆
     */
    public void getRecommendData() {
        mNewEnergy_ApiClient.getRecommendData(choosedCity, EVSharePreferencesHelper.getLatitudeBD(), EVSharePreferencesHelper.getLongitudeBD(), 5, new LoadingAnimResponseHandler(this, true) {
            @Override
            public void onSuccess(String response) {
                ArrayList< RecommendBean > mRecommend_list = JSONUtils.fromJson(response, new TypeToken< ArrayList< RecommendBean > >() {
                });
                if (mRecommend_list != null && mRecommend_list.size() != 0) {
                    for (final RecommendBean recommendBean : mRecommend_list) {
                        EvHomeModuleView mEvHomeModuleView = new EvHomeModuleView(Ev_NewEnergyActivity.this);
                        mEvHomeModuleView.setModuleTitle(recommendBean.title);
                        mEvHomeModuleView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivitySwitcherEV.toEVRecommendMore(Ev_NewEnergyActivity.this, recommendBean.recommendType);
                            }
                        });
                        for (EVNewenergyAutoSample sample : recommendBean.recommend) {
                            if (sample == null) continue;
                            RecommendView recommendView = new RecommendView(Ev_NewEnergyActivity.this);
                            recommendView.setData(sample);
                            mEvHomeModuleView.getContentView().addView(recommendView);

                        }

                        mRecommendedLayout.addView(mEvHomeModuleView);
                    }
                } else {
                    mRecommendedLayout.setVisibility(View.GONE);
                }
            }
        });
    }


    /**
     * 获取特卖列表
     */
    public void getSpecialOffer() {

        mNewEnergy_ApiClient.getDealsInfo(EVSharePreferencesHelper.getCity(), 20, 2, 1, new LoadingAnimResponseHandler(this, true) {
            @Override
            public void onSuccess(String response) {
                ArrayList< PromotionAuto > objectArrayList = JSONUtils.fromJson(response, new TypeToken< ArrayList< PromotionAuto > >() {
                });

                for (int i = 0; i < objectArrayList.size(); i++) {
                    DealsView dealsView = new DealsView(Ev_NewEnergyActivity.this);
                    dealsView.setData(objectArrayList.get(i));
                    mDealsModuleView.getContentView().addView(dealsView);
                }
            }
        });

    }

    public void getRankingData() {

        mNewEnergy_ApiClient.getRankingData(choosedCity, 1, new LoadingAnimResponseHandler(this, true) {
            @Override
            public void onSuccess(String response) {
                EVRankingSchema objectArrayList = JSONUtils.fromJson(response, EVRankingSchema.class);
                mNewEnergyRanklingView.setData(objectArrayList);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.change_city:
                ActivitySwitcherEV.toPositionActivity(this, 1, ((TextView) v).getText().toString());
                break;
        }

    }

    @Override
    public boolean hasMore() {
        return false;
    }


    @Override
    public void onRefresh() {

    }


    @Override
    public void onLoadMore() {

    }


}
