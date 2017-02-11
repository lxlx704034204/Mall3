package com.hxqc.mall.thirdshop.maintenance.views;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
 * @Since : 2016-02-23
 * FIXME
 * Todo  推荐保养广告dialog
 */
public class AdvertisementDialog extends Dialog {

    private SliderLayout mSliderView;
    private WebView mWebView;
    private WebSettings settings;
    private GoodsIntroduce goodsIntroduce;
    private Context context;


    public AdvertisementDialog(Context context , GoodsIntroduce goodsIntroduce) {
        super(context);
        this.context = context;
        this.goodsIntroduce = goodsIntroduce;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_advertisement_dialog);
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

//        loadHTMLData(goodsIntroduce.content);
        mWebView.loadDataWithBaseURL("", goodsIntroduce.content, "text/html", "UTF-8", "");
//        mWebView.loadUrl(goodsIntroduce.content);
    }

    void loadHTMLData(String content){
//        String htmlStr =   "<head><meta charset=\"UTF-8\">" +
//                "        <meta http-equiv=\"x-rim-auto-match\" content=\"none\">" +
//                "        <meta name=\"viewport\" content=\"initial-scale=1.0, maximum-scale=1.0,minimum-scale=0.7, user-scalable=no\">" +
//                "        <meta name=\"renderer\" content=\"webkit\">" +
//                "        <title>详情</title>\n" +
//                "</head>\n" +
//                "<body>";
//
//        htmlStr += content;
//        htmlStr += "</body></html>";
        mWebView.loadDataWithBaseURL("", content, "text/html", "UTF-8", "");
    }

    private void sliderAD(ArrayList<String> thumb) {
        if(thumb.size() == 1){
            mSliderView.setSliderOnlyOneView(thumb.get(0));
            mSliderView.setEnabled(false);
            mSliderView.setFocusableInTouchMode(false);
            mSliderView.setFilterTouchesWhenObscured(false);
            mSliderView.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        }else {
            for(int i=0 ; i< thumb.size();i++){
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


    private void initView() {
        mSliderView = (SliderLayout)findViewById(R.id.slider_home);
        mWebView = (WebView) findViewById(R.id.webView);

        settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        mWebView.getSettings().setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setBuiltInZoomControls(false);                      //support zoom
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
}
