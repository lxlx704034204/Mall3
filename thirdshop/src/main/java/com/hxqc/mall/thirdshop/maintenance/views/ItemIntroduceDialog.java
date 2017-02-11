package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.GoodsIntroduce;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-03-30
 * FIXME
 * Todo 项目介绍dialog
 */
public class ItemIntroduceDialog extends AlertDialog {

    private SliderLayout mSliderView;
    private WebView mWebView;
    private WebSettings settings;
    private GoodsIntroduce goodsIntroduce;
    private Context context;
    private TextView mTitleView;
    private String title;

    private String CSS = "<html><head><meta charset='utf-8'/><style type='text/css'>p,p span{font-size: 2.0rem!important;display: block!important;height: auto !important;line-height: 70px!important;}ul {font-size: 5.0rem !important;list-style: none;}</style></head><body>";

    public ItemIntroduceDialog(Context context, int themeResId, GoodsIntroduce goodsIntroduce,String title) {
        super(context, themeResId);
        this.context = context;
        this.goodsIntroduce = goodsIntroduce;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_item_introduce_dialog);
        initView();
        initDate();
    }

    private void initDate() {
        if(goodsIntroduce.thumb.size()>0){
            sliderAD(goodsIntroduce.thumb);
        }else {
            mSliderView.setSliderOnlyOneView("");
            mSliderView.setEnabled(false);
            mSliderView.setFocusableInTouchMode(false);
            mSliderView.setFilterTouchesWhenObscured(false);
            mSliderView.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        }

        mWebView.loadDataWithBaseURL("", CSS +goodsIntroduce.content +"</body></html>", "text/html", "UTF-8", "");
//        mWebView.loadUrl(goodsIntroduce.content);
        mTitleView.setText(title);
    }

    private void initView() {
        mSliderView = (SliderLayout) findViewById(R.id.slider_home);
        mWebView = (WebView) findViewById(R.id.webView);
        mTitleView = (TextView) findViewById(R.id.title);

        settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        mWebView.getSettings().setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setBuiltInZoomControls(true);                      //support zoom
        settings.setLoadWithOverviewMode(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("tel:")) {
                    Intent dialIntent = new Intent();
                    dialIntent.setAction(Intent.ACTION_DIAL);
                    dialIntent.setData(Uri.parse(url));
                    context.startActivity(dialIntent);
                } else {
                    mWebView.loadUrl(url);
                }
                return true;
            }
        });
    }

    private void sliderAD(ArrayList<String> thumb) {
        if (thumb.size() == 1) {
            mSliderView.setSliderOnlyOneView(thumb.get(0));
            mSliderView.setEnabled(false);
            mSliderView.setFocusableInTouchMode(false);
            mSliderView.setFilterTouchesWhenObscured(false);
            mSliderView.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        } else {
            for (int i = 0; i < thumb.size(); i++) {
                DefaultSliderView textSliderView = new DefaultSliderView(context);
                textSliderView.empty(R.drawable.sliderimage_pic_normal_slider).error(R.drawable.sliderimage_pic_normal_slider);

                textSliderView.description(i + "").image(thumb.get(i)).
                        setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderView.addSlider(textSliderView);
            }
            mSliderView.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mSliderView.setCustomAnimation(new DescriptionAnimation());
            mSliderView.setDuration(5000);
        }
    }
}
