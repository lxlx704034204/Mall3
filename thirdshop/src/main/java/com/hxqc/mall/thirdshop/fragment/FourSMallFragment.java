package com.hxqc.mall.thirdshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.FourSNews;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.utils.AreaSiteUtil;
import com.hxqc.mall.thirdshop.views.FourSMallLayout;
import com.hxqc.mall.usedcar.utils.OtherUtil;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

/**
 * 说明:4s店首页推荐活动
 *
 * @author: 吕飞
 * @since: 2016-11-21
 * Copyright:恒信汽车电子商务有限公司
 */

public class FourSMallFragment extends FunctionFragment implements BaseSliderView.OnSliderClickListener, FourSMallLayout.OnNestScrollListener, FourSRecommendFragment.RefreshRecommendListener {
    SliderLayout mSliderView;
    ThirdPartShopClient mThirdPartShopClient;
    ImageView mBannerImageView;
    FourSSpecialFragment mFourSSpecialFragment;
    FourSMallLayout mFourSMallLayoutView;
    boolean mIsWuhan;
    FourSRecommendFragment mFourSRecommendFragment;
    LinearLayout mTopView;
    AreaSiteUtil mAreaSiteUtil;
    String mCurrentCityID = "";
//    ArrayList<FourSNews> listData = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_4s_mall, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAreaSiteUtil = AreaSiteUtil.getInstance(getActivity());
        mThirdPartShopClient = new ThirdPartShopClient();
        mTopView = (LinearLayout) view.findViewById(R.id.top);
        mBannerImageView = (ImageView) view.findViewById(R.id.banner_image);
        mSliderView = (SliderLayout) view.findViewById(R.id.slider);
        mFourSMallLayoutView = (FourSMallLayout) view.findViewById(R.id.root);
        mFourSSpecialFragment = new FourSSpecialFragment();
        mFourSRecommendFragment = new FourSRecommendFragment();
        mFourSMallLayoutView.setOnNestScrollListener(this);
        mFourSRecommendFragment.setRefreshRecommendListener(this);
        getData();
    }

    @Override
    public String fragmentDescription() {
        return "4s店首页推荐活动";
    }

    @Override
    public void onNestedPreScroll() {
        mFourSRecommendFragment.getPtrFrameLayoutView().requestDisallowInterceptTouchEvent(false);
    }

    @Override
    public void onNestedScroll() {
        mFourSRecommendFragment.getPtrFrameLayoutView().requestDisallowInterceptTouchEvent(true);
    }

    public void getData() {
        String cityGroup = mAreaSiteUtil.getSiteName();
        mIsWuhan = cityGroup.contains("武汉");
        String cityGroupID = mAreaSiteUtil.getSiteID();
        if (!mCurrentCityID.equals(cityGroupID)) {
            OtherUtil.setVisible(mTopView, mIsWuhan);
            if (mIsWuhan) {
                getRecommendData(cityGroupID);
                switchFragment(mFourSRecommendFragment, mFourSSpecialFragment);
            } else {
                switchFragment(mFourSSpecialFragment, mFourSRecommendFragment);
            }
        }
    }

    private void getRecommendData(final String cityGroupID) {
        mThirdPartShopClient.getIndexInfoList(cityGroupID, new LoadingAnimResponseHandler(getActivity()) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                mFourSRecommendFragment.showFailData();
            }

            @Override
            public void onSuccess(String response) {
                mCurrentCityID = cityGroupID;
                ArrayList<FourSNews> fourSNewses = JSONUtils.fromJson(response, new TypeToken<ArrayList<FourSNews>>() {
                });
                if (fourSNewses != null && fourSNewses.size() > 0) {
                    mBannerImageView.setVisibility(View.GONE);
                    mSliderView.setVisibility(View.VISIBLE);
                    sliderAD(fourSNewses);
                    if (fourSNewses.size() > 5) {
                        fourSNewses.subList(6, fourSNewses.size());
                        mFourSRecommendFragment.setData(fourSNewses);
                    } else {
                        mFourSRecommendFragment.showNoData();
                    }
                } else {
                    mSliderView.setVisibility(View.GONE);
                    mBannerImageView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void switchFragment(FunctionFragment functionFragmentShow, FunctionFragment functionFragmentHide) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        if (functionFragmentShow.isAdded()) {
            fragmentTransaction.show(functionFragmentShow);
        } else {
            fragmentTransaction.add(R.id.fragment_container, functionFragmentShow);
        }
        if (functionFragmentHide.isAdded()) {
            fragmentTransaction.hide(functionFragmentHide);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void sliderAD(ArrayList<FourSNews> fourSNewses) {
        mSliderView.removeAllSliders();
        if (fourSNewses.size() == 1) {
            final FourSNews item = fourSNewses.get(0);
            mSliderView.setSliderOnlyOneView(item.thumbSmall);
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
            for (int i = 0; i < fourSNewses.size(); i++) {
                FourSNews item = fourSNewses.get(i);
                DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
                textSliderView.empty(R.drawable.sliderimage_pic_normal_slider).error(R.drawable.sliderimage_pic_normal_slider);
                textSliderView.description(i + "").image(item.thumb).
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
        FourSNews item = baseSliderView.getBundle().getParcelable("extra");
        sliderClickOperate(item);
    }

    private void sliderClickOperate(FourSNews item) {
        switch (item.newsKind) {
            case 10:
                ActivitySwitchBase.toH5Activity(getContext(), "资讯详情", item.defaultNews.url);
                break;
            case 20:
                ActivitySwitcherThirdPartShop.toCarDetail(item.newAuto.itemID, item.newAuto.shopID, "", getContext());
                break;
            case 30:
                ActivitySwitcherThirdPartShop.toSpecialCarDetail(getContext(), item.newAuto.itemID);
                break;
            case 40:
            case 50:
            case 60:
                ActivitySwitcherThirdPartShop.toSalesItemDetail(item.promotion.promotionID, getContext());
                break;
        }
    }

    @Override
    public void refreshRecommend() {
        String cityGroupID = mAreaSiteUtil.getSiteID();
        getRecommendData(cityGroupID);
    }
}
