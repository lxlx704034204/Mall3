package com.hxqc.mall.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.hxqc.mall.core.model.auto.Accessory;
import com.hxqc.mall.core.model.auto.AccessoryPhoto;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author: wanghao
 * Date: 2015-10-08
 * FIXME
 * Todo 套餐用品详情
 */
public class PackageInfoDialog extends Dialog implements BaseSliderView.OnSliderClickListener {

    ArrayList< AccessoryPhoto > ms;
    SliderLayout mSliderView;
    ViewGroup mRootView;
    protected View mDialogView;
    Accessory a;

    TextView mItemNameView;
    TextView mItemDetailView;


    public PackageInfoDialog(Context context) {
        super(context, R.style.item_dialog);
        initView();
        setCanceledOnTouchOutside(true);
    }

    public PackageInfoDialog(Context context, Accessory accessory) {
        super(context, R.style.item_dialog);
        this.a = accessory;
        this.ms = accessory.photo;
        DebugLog.i("test_a_desc", a.desc);
        initView();
        setCanceledOnTouchOutside(true);
    }

    public PackageInfoDialog(Context context, String content) {
        super(context, R.style.item_dialog);
        initView();
        setCanceledOnTouchOutside(true);
    }


//    public void setBodyView(View contentView) {
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        lp.addRule(RelativeLayout.BELOW, R.id.content);
//        ViewGroup view = (ViewGroup) findViewById(com.hxqc.mall.core.R.id.content_layout);
//        view.addView(contentView, lp);
//    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_item_detail);
        mRootView = (ViewGroup) findViewById(R.id.root);
        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mSliderView = (SliderLayout) findViewById(R.id.item_slider);
        mItemNameView = (TextView) findViewById(R.id.tv_item_info_name);
        mItemDetailView = (TextView) findViewById(R.id.tv_item_detail);
        mItemDetailView.setMovementMethod(ScrollingMovementMethod.getInstance());
        mItemNameView.setText(a.title);
//        String desc = "<html><head></head><body>"+a.desc+"</body></html>";
        mItemDetailView.setText(Html.fromHtml(a.desc));
        if (ms != null)
            if (ms.size() > 0)
                sliderAD();
    }

    private void sliderAD() {
        if (ms.size() == 1) {

            AccessoryPhoto item = ms.get(0);
            mSliderView.setSliderOnlyOneView(item.large);
            mSliderView.setEnabled(false);
            mSliderView.setFocusableInTouchMode(false);
            mSliderView.setFilterTouchesWhenObscured(false);
            mSliderView.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);

        } else {

            for (int i = 0; i < ms.size(); i++) {
                AccessoryPhoto item = ms.get(i);
                DefaultSliderView textSliderView = new DefaultSliderView(getContext());
                textSliderView.description(i + "").image(item.large).
                        setScaleType(BaseSliderView.ScaleType.Fit).setOnSliderClickListener(this);

                mSliderView.addSlider(textSliderView);
            }
            mSliderView.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        }

    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {

    }

}
