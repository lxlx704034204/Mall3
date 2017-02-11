package com.hxqc.mall.reactnative.nativeui.slider;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderAdapter;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.model.HomeSlideADModel;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author: wanghao
 * Date: 2016-03-26
 * FIXME 轮播  for js view 暂时简单封装
 * Todo
 */
public class SliderLayoutForJS extends RelativeLayout implements BaseSliderView.OnSliderClickListener {

    ArrayList< HomeSlideADModel > homeSlideADModels;
    SliderLayout mSliderView;
    Context c;
    final private String TAG = "SliderLayoutForJS";


    public SliderLayoutForJS(Context context) {
        super(context);
        this.c = context;
        initLayout();
    }

    public SliderLayoutForJS(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.c = context;
        initLayout();
    }

    private void initLayout() {
        LayoutInflater.from(getContext()).inflate(R.layout.js_slider_view, this);
        mSliderView = (SliderLayout) findViewById(R.id.slider_home_js);
        mSliderView.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                DebugLog.e(TAG, "SliderLayoutForJS onPageScrolled position: " + position+" positionOffset: "+positionOffset+" positionOffsetPixels: "+positionOffsetPixels);

                SliderAdapter realAdapter = mSliderView.getRealAdapter();
                if (realAdapter!=null)
                {
                    DebugLog.e(TAG,"onPageScrolled realAdapter.getCount()"+ realAdapter.getCount());
                }
            }

            @Override
            public void onPageSelected(int position) {
                DebugLog.e(TAG, "SliderLayoutForJS onPageSelected position: " + position);
                SliderAdapter realAdapter = mSliderView.getRealAdapter();
                if (realAdapter!=null)
                {
                    DebugLog.e(TAG,"onPageSelected realAdapter.getCount()"+ realAdapter.getCount());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                DebugLog.e(TAG, "SliderLayoutForJS onPageScrollStateChanged state: " + state);
                SliderAdapter realAdapter = mSliderView.getRealAdapter();
                if (realAdapter!=null)
                {
                    DebugLog.e(TAG,"onPageScrollStateChanged realAdapter.getCount()"+ realAdapter.getCount());
                }
            }
        });
    }

    public void onResume() {
        if (mSliderView != null)
            mSliderView.startAutoCycle();
    }


    public void onStop() {
        if (mSliderView != null)
            mSliderView.stopAutoCycle();
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {
        HomeSlideADModel item = slider.getBundle().getParcelable("extra");
        sliderClickOperate(item);
    }

    public void setData(String response) {
        DebugLog.e(TAG, "SliderLayoutForJS: " + response);
        if (TextUtils.isEmpty(response)) {
            defaultSliderView();
        } else {
            ArrayList< HomeSlideADModel > homeSlideADModels = JSONUtils.fromJson(response, new TypeToken<
                    ArrayList< HomeSlideADModel > >() {
            });

            if (homeSlideADModels.size() > 8) {
                homeSlideADModels.subList(0, 8);
            }

            if (homeSlideADModels.size() > 0) {
                sliderAD(homeSlideADModels);
            } else {
                defaultSliderView();
            }
        }
    }

    private void defaultSliderView() {
        mSliderView.setSliderOnlyOneView("");
        mSliderView.setEnabled(false);
        mSliderView.setFocusableInTouchMode(false);
        mSliderView.setFilterTouchesWhenObscured(false);
        mSliderView.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
    }

    private void sliderAD(ArrayList< HomeSlideADModel > dataes) {

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

//            mSliderView.setEnabled(true);
//            mSliderView.setFocusableInTouchMode(true);
//            mSliderView.setFilterTouchesWhenObscured(true);
//            mSliderView.sliderOnlyOneView.setVisibility(GONE);
//            mSliderView.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);

            for (int i = 0; i < dataes.size(); i++) {
                HomeSlideADModel item = dataes.get(i);
                DebugLog.i("test_slider", item.toString());
                DebugLog.e(TAG, "sliderAD: " + item.slide);
                DefaultSliderView textSliderView = new DefaultSliderView(c);
                // initialize a SliderLayout
                textSliderView.empty(R.drawable.sliderimage_pic_normal_slider).error(R.drawable.sliderimage_pic_normal_slider);
                textSliderView.description(i + "").image(item.slide).
                        setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());

                textSliderView.getBundle().putParcelable("extra", item);


                mSliderView.addSlider(textSliderView);
            }

            mSliderView.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mSliderView.setCustomAnimation(new DescriptionAnimation());
            mSliderView.setDuration(5000);
        }
    }

    private void sliderClickOperate(HomeSlideADModel item) {
        if (item != null && item.isPromotion()) {
            ActivitySwitcher.toEventDetail(c, item.url);
        } else {
            assert item != null;
            if (item.isSeckill()) {
                ActivitySwitcher.toAutoItemDetail(c, AutoItem.AUTO_PROMOTION, item.getID(), "");
            } else {
                ActivitySwitcher.toAutoItemDetail(c, AutoItem.AUTO_COMMON, item.getID(), "");
            }
        }
    }

}
