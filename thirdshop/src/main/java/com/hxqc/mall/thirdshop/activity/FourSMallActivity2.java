package com.hxqc.mall.thirdshop.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.config.router.RouteOpenActivityUtil;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.model.HomeSlideADModel;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.FourSNews;
import com.hxqc.mall.thirdshop.model.FourSSeries;
import com.hxqc.mall.thirdshop.model.FourSShop;
import com.hxqc.mall.thirdshop.model.SingleSeckillItem;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.views.FourSHomeItem;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * 说明:4s店网上商城
 *
 * @author: 吕飞
 * @since: 2016-05-05
 * Copyright:恒信汽车电子商务有限公司
 */
@Deprecated
public class FourSMallActivity2 extends BaseSiteChooseActivity implements View.OnClickListener, BaseSliderView.OnSliderClickListener {
    SliderLayout mSliderView;
    FourSHomeItem mNewsView;
    FourSHomeItem mNewCarView;
    FourSHomeItem mShopView;
    FourSHomeItem mSeckillView;
    ThirdPartShopClient mThirdPartShopClient;
    ImageView mBannerImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4s_mall);
        mThirdPartShopClient = new ThirdPartShopClient();
        initView();
        initLocationData();
        getData();
    }


    @Override
    void onResultCallBack(Bundle bundle) {
        getData();
    }


    private void getData() {
        mThirdPartShopClient.getBanner(cityGroupID, new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                ArrayList<HomeSlideADModel> homeSlideADModels = JSONUtils.fromJson(response, new TypeToken<ArrayList<HomeSlideADModel>>() {
                });
                if (homeSlideADModels != null && homeSlideADModels.size() > 0) {
                    if (homeSlideADModels.size() > 8) {
                        homeSlideADModels.subList(0, 8);
                    }
                    mBannerImageView.setVisibility(View.GONE);
                    mSliderView.setVisibility(View.VISIBLE);
                    sliderAD(homeSlideADModels);
                } else {
                    mSliderView.setVisibility(View.GONE);
                    mBannerImageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                mSliderView.setVisibility(View.GONE);
                mBannerImageView.setVisibility(View.GONE);
            }
        });
        mThirdPartShopClient.getIndexNews(cityGroupID, new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                ArrayList<FourSNews> fourSNewses = JSONUtils.fromJson(response, new TypeToken<ArrayList<FourSNews>>() {
                });
                if (fourSNewses != null && fourSNewses.size() > 0) {
                    mNewsView.setVisibility(View.VISIBLE);
                    mNewsView.setNewsView(fourSNewses, cityGroupID);
                } else {
                    mNewsView.setVisibility(View.GONE);
                }
            }
        });
        mThirdPartShopClient.getIndexSeriesList(cityGroupID, new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                ArrayList<FourSSeries> fourSSeries = JSONUtils.fromJson(response, new TypeToken<ArrayList<FourSSeries>>() {
                });
                if (fourSSeries != null && fourSSeries.size() > 0) {
                    mNewCarView.setVisibility(View.VISIBLE);
                    mNewCarView.setNewCarView(fourSSeries, cityGroupID);
                } else {
                    mNewCarView.setVisibility(View.GONE);
                }
            }
        });
        mThirdPartShopClient.getIndexShopList(3, cityGroupID, new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                ArrayList<FourSShop> fourSShops = JSONUtils.fromJson(response, new TypeToken<ArrayList<FourSShop>>() {
                });
                if (fourSShops != null && fourSShops.size() > 0) {
                    mShopView.setVisibility(View.VISIBLE);
                    mShopView.setShopView(fourSShops, cityGroupID);
                } else {
                    mShopView.setVisibility(View.GONE);
                }
            }
        });
        mThirdPartShopClient.getIndexSeckillList(cityGroupID, new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                ArrayList<SingleSeckillItem> singleSeckillItems = JSONUtils.fromJson(response, new TypeToken<ArrayList<SingleSeckillItem>>() {
                });
                if (singleSeckillItems != null && singleSeckillItems.size() > 0) {
                    mSeckillView.setVisibility(View.VISIBLE);
                    mSeckillView.setSeckillView(singleSeckillItems, cityGroupID);
                } else {
                    mSeckillView.setVisibility(View.GONE);
                }
            }
        });
    }


    private void sliderAD(ArrayList<HomeSlideADModel> dataes) {
        mSliderView.removeAllSliders();
        if (dataes.size() == 1) {
            final HomeSlideADModel item = dataes.get(0);
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
            for (int i = 0; i < dataes.size(); i++) {
                HomeSlideADModel item = dataes.get(i);
                DefaultSliderView textSliderView = new DefaultSliderView(this);
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


    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setTitle("网上4S店商城");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBannerImageView = (ImageView) findViewById(R.id.banner_image);
        mChangeCityView = (TextView) findViewById(R.id.change_city);
        mSliderView = (SliderLayout) findViewById(R.id.slider);
        mNewsView = (FourSHomeItem) findViewById(R.id.item_news);
        mNewCarView = (FourSHomeItem) findViewById(R.id.item_new_car);
        mShopView = (FourSHomeItem) findViewById(R.id.item_shop);
        mSeckillView = (FourSHomeItem) findViewById(R.id.item_seckill);
        mChangeCityView.setOnClickListener(this);
    }


    private void sliderClickOperate(HomeSlideADModel item) {
        RouteOpenActivityUtil.linkToActivity(this, item.routerUrl);
//        if (item != null && item.isPromotion()) {
//            ActivitySwitchBase.toH5Activity(this, "活动", item.url);
//        } else {
//            assert item != null;
//            if (item.isSeckill()) {
//                // TODO: 2016/5/5 特卖
//            } else {
//                // TODO: 2016/5/5 详情
//            }
//        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.change_city) {
            ActivitySwitcherThirdPartShop.toSpecialCarChoosePositon(this, 1, ((TextView) v).getText().toString());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSeckillView.destory();
    }

//    public void showLocationIcon(boolean flag) {
//        if (mChangeCityView == null) return;
//        if (flag) {
//            if ((mChangeCityView.getCompoundDrawables())[0] == null) mChangeCityView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.t_icon_button_location), null, null, null);
//        } else {
//            if ((mChangeCityView.getCompoundDrawables())[0] != null) {
//                mChangeCityView.setCompoundDrawables(null, null, null, null);
//            }
//        }
//    }
}
