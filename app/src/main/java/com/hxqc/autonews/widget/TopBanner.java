package com.hxqc.autonews.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.hxqc.autonews.model.pojos.AutoInformation;
import com.hxqc.autonews.util.ActivitySwitchAutoInformation;
import com.hxqc.autonews.util.ToAutoInfoDetailUtil;
import com.hxqc.mall.auto.util.ScreenUtil;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-09-29
 * FIXME
 * Todo 顶部广告条
 */

public class TopBanner extends SliderLayout implements BaseSliderView.OnSliderClickListener {
    private static final long SLIDER_DURATION = 5000;
    private final LinearLayout indicatorGroup;
    private final TextView title;
    private Context mContext;
    private ArrayList<RadioButton> radioButtons;
    private SparseBooleanArray indicData;//记录小白（indicator）的的状态


    public TopBanner(Context context) {
        this(context, null);
    }

    public TopBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private FixScrollView scrollView = null;

    private ArrayList<AutoInformation> mBanner;

    public TopBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBanner = new ArrayList<>();
        mContext = context;
        radioButtons = new ArrayList<>();
        indicData = new SparseBooleanArray();
        View bottomContainer = LayoutInflater.from(context).inflate(R.layout.layout_slider_bottom, null);
        title = (TextView) bottomContainer.findViewById(R.id.title);
        indicatorGroup = (LinearLayout) bottomContainer.findViewById(R.id.indicator_group);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        bottomContainer.setLayoutParams(lp);
        addView(bottomContainer);
    }

    /**
     * 绑定数据，动态添加指示角标
     * @param banner
     */
    public void bindData(final ArrayList<AutoInformation> banner) {
        mBanner.clear();
        mBanner.addAll(banner);
        removeAllSliders();
        radioButtons.clear();
        indicatorGroup.removeAllViews();
        if (mBanner == null) {
            setSliderOnlyOneView(null);
            setEnabled(false);
            setFocusableInTouchMode(false);
            setFilterTouchesWhenObscured(false);
            setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        } else {
            if (mBanner.size() > 1) {
                for (int i = 0; i < (mBanner.size() > 5 ? 5 : mBanner.size()); i++) {
                    AutoInformation autoInformation = mBanner.get(i);
                    ArrayList<String> thumbImages = autoInformation.thumbImage;
                    if (thumbImages != null && thumbImages.size() > 0) {
                        DefaultSliderView sliderView = new DefaultSliderView(mContext);
                        sliderView.empty(R.drawable.sliderimage_pic_normal_slider)
                                .error(R.drawable.sliderimage_pic_normal_slider);
                        sliderView.description(autoInformation.title).image(thumbImages.get(0))
                                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                                .setOnSliderClickListener(this);
                        sliderView.bundle(new Bundle());
                        sliderView.getBundle().putParcelable("extra", autoInformation);
                        addSlider(sliderView);
                        addIndicator(i);
                    }
                }
                setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
                setCustomAnimation(new DescriptionAnimation());
                setDuration(SLIDER_DURATION);
                if (getRealAdapter().getCount() > 0) {
                    int currentPosition = getCurrentPosition();
                    setChoosed(currentPosition);
                }
                startAutoCycle();
                ViewPagerEx.OnPageChangeListener listener = new ViewPagerEx.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        int i = indicData.indexOfValue(true);
                        indicData.append(i, false);
                        indicData.append(position, true);
                        setChoosed(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                };
                addOnPageChangeListener(listener);
            } else if (mBanner.size() == 1) {
                ArrayList<String> thumbImage = mBanner.get(0).thumbImage;
                if (thumbImage != null && thumbImage.size() > 0) {
                    setSliderOnlyOneView(thumbImage.get(0));
                    setEnabled(false);
                    setFocusableInTouchMode(false);
                    setFilterTouchesWhenObscured(false);
                    setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
                    addIndicator(0);
                    sliderOnlyOneView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //点击唯一的一个按钮
                            AutoInformation information = mBanner.get(0);
                            ToAutoInfoDetailUtil
                                    .onToNextPage(information.infoID, information.getType(), mContext);
                        }
                    });
                }
                if (indicData.indexOfValue(true) == -1) {
                    indicData.put(0, true);
                }
                setChoosed(indicData.indexOfValue(true));
            }
        }
    }

    /**
     * 去图集界面
     */
    private void toAutoImagesDetail(String infoID, String title) {
        ActivitySwitchAutoInformation.toAutoGallery(mContext, infoID);
    }

    /**
     * 去图文详情界面
     */
    private void toAutoInfoDetail(String infoID) {
        ActivitySwitchAutoInformation.toAutoInfoDetail(mContext, infoID);
    }

    private void setChoosed(int currentPosition) {
        allFalse();
        if (radioButtons.size() > currentPosition)
            radioButtons.get(currentPosition).setChecked(true);
        title.setText(mBanner.get(currentPosition).title);
    }

    private void allFalse() {
        for (int i = 0; i < radioButtons.size(); i++) {
            if (radioButtons.get(i).isChecked())
                radioButtons.get(i).setChecked(false);
        }
    }

    private void addIndicator(int i) {
        int count = mBanner.size() > 5 ? 5 : mBanner.size();//最多为5
        if (indicatorGroup.getChildCount() < count) {
            RadioButton rb = new RadioButton(mContext);
            rb.setEnabled(false);
            rb.setBackgroundResource(R.drawable.bg_indicator);
            rb.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
            int size = ScreenUtil.dip2px(mContext, 10);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size
                    , size);
            int margin = ScreenUtil.dip2px(mContext, 5);
            lp.leftMargin = margin;
            rb.setLayoutParams(lp);
            indicatorGroup.addView(rb);
            if (radioButtons.size() < count) {
                radioButtons.add(rb);
            }
        }
        if (indicData.size() < count) {
            indicData.put(i, false);
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //点击banner跳转到指定的页面
        AutoInformation extra = slider.getBundle().getParcelable("extra");
        String infoID = "";
        String infoTitle = "";
        if (extra != null) {
            infoID = extra.infoID + "";
            infoTitle = extra.title;
        }
        ToAutoInfoDetailUtil
                .onToNextPage(infoID, extra.getType(), mContext);
//        if (extra.getType() == AutoInformation.Type.Images)
//            toAutoImagesDetail(infoID, infoTitle);
//        else toAutoInfoDetail(infoID);
    }
}
