package com.hxqc.mall.thirdshop.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hxqc.util.DisplayTools;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.control.T_NewImageViewControl;
import com.hxqc.mall.thirdshop.control.ThirdPartShopJSInterface;
import com.hxqc.mall.thirdshop.model.AutoDetailThirdShop;
import com.hxqc.mall.thirdshop.model.promotion.AttachmentImageNewsModel;
import com.hxqc.mall.thirdshop.model.promotion.SalesItem;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2015-12-03
 * FIXME  html拼接view
 * Todo
 */
public class InfoIntroduceView extends RelativeLayout {

    public WebView mIntroduceView;
    ImageView loadingRoundView;
    public ArrayList< AttachmentImageNewsModel > attachments;
    public String contentHtml;
    public T_NewImageViewControl control;
    Animation animation;
    public int img_width = 0;

    public InfoIntroduceView(Context context) {
        super(context);
        initView();
    }

    public InfoIntroduceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        img_width = (((Activity) getContext()).getWindowManager().getDefaultDisplay().getWidth() / (int) DisplayTools.getScreenDensity(getContext())) - 16 * 2;
        DebugLog.i("test_html_wh", img_width + "");

        control = T_NewImageViewControl.getInstance();
        LayoutInflater.from(getContext()).inflate(R.layout.t_info_introduce_webview, this);
        mIntroduceView = (WebView) findViewById(R.id.t_introduce_view);
        mIntroduceView.addJavascriptInterface(new ThirdPartShopJSInterface(getContext(), control), "Android");
        mIntroduceView.setOverScrollMode(OVER_SCROLL_NEVER);
        loadingRoundView = (ImageView) findViewById(R.id.iv_t_detail_round);
    }

    /**
     * 设置促销详情 html
     */
    public void setSalesDetailInfo(String html, ArrayList< SalesItem > Models, String tableHead, String tableFooter, ArrayList< AttachmentImageNewsModel > attachments) {
        this.contentHtml = html;
        this.attachments = attachments;

        clearATag();
        salesPIntroduceConfig(Models, tableHead, tableFooter);
    }

    /**
     * 设置新闻资讯详情 html
     */
    public void setNewsDetailInfo(String content, ArrayList< AttachmentImageNewsModel > attachments) {
        this.contentHtml = content;
        this.attachments = attachments;

        clearATag();
        newsDetailIntroduceConfig();
    }

    /**
     * 重写webview
     */
    public void reWriteWebview() {
        mIntroduceView.setWebViewClient(new InfoWebViewClient());
    }

    /**
     * 设置新闻资讯详情的页面拼接
     */
    private void newsDetailIntroduceConfig() {
        control.setAttachments(attachments);
        reWriteWebview();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mIntroduceView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            mIntroduceView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        }
        mIntroduceView.getSettings().setLoadWithOverviewMode(true);
        mIntroduceView.getSettings().setJavaScriptEnabled(true);
        mIntroduceView.getSettings().setUseWideViewPort(true);

        exchangeImgTags();

        String value = "<head><meta charset=\"UTF-8\">" +
                "        <meta http-equiv=\"x-rim-auto-match\" content=\"none\">" +
                "        <meta name=\"viewport\" content=\"initial-scale=1.0, maximum-scale=1.0,minimum-scale=0.7, user-scalable=no\">" +
                "        <meta name=\"renderer\" content=\"webkit\">" +
                "        <title>kk</title>\n" +
                "        <style>\n" +
                "         body {margin: 0;padding: 0 0;font-family: Helvetica, Arial, sans-serif;background-color:#fff}\n" +
                "         div,ul,li,p{padding:0; margin:0;}\n" +
                "         .itemImg{margin:10 0;}\n" +
                "         .itemImageContent{width:" + img_width + "}\n" +
                "</style>\n" +

                "<script>\n" +
                "          function redirectItem(p){\n" +
                "              var androidData = p;\n" +
                "              Android.showResponseView(androidData);\n" +
                "          }\n" +
                "</script>" +

                "</head>\n" +
                "<body>";
        value += contentHtml;
//        value = value.replace("<img", "<img width=\"" + (((Activity) getContext()).getWindowManager().getDefaultDisplay().getWidth() / (int) DisplayTools.getScreenDensity(getContext())) + "\"");
        value += "</body></html>";
        DebugLog.i("test_s_info", value);
        mIntroduceView.loadDataWithBaseURL("", value, "text/html", "UTF-8", "");
    }

    /**
     * 设置促销详情的页面拼接
     */
    private void salesPIntroduceConfig(ArrayList< SalesItem > priceModels, String tableHead, String tableFooter) {
        control.setAttachments(attachments);
        DebugLog.i("test_s_info", contentHtml);
        reWriteWebview();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mIntroduceView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            mIntroduceView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        }
        mIntroduceView.getSettings().setLoadWithOverviewMode(true);
        mIntroduceView.getSettings().setJavaScriptEnabled(true);
        mIntroduceView.getSettings().setUseWideViewPort(true);

        exchangeImgTags();

        String value = "<head><meta charset=\"UTF-8\">" +
                "        <meta http-equiv=\"x-rim-auto-match\" content=\"none\">" +
                "        <meta name=\"viewport\" content=\"initial-scale=1.0, maximum-scale=1.0,minimum-scale=0.7, user-scalable=no\">" +
                "        <meta name=\"renderer\" content=\"webkit\">" +
                "        <title>kk</title>\n" +

                "        <script>\n" +
                "          function redirectItem(p){\n" +
                "              var androidData = p;\n" +
                "              Android.showResponseView(androidData);\n" +
                "          }\n" +
                "        </script>" +

                "        <style>\n" +
                "         body {margin: 0;padding: 0 0;font-family: Helvetica, Arial, sans-serif;}\n" +
                "         div,ul,li,p{padding:0; margin:0;}\n" +
                "         .nativeTitle{color:#000;}" +
                "         .nativeTFont{color:#747171;}\n" +
                "         .dealAmount{color:#e02c36;}\n" +
                "         .itemImg{margin:10 0;}\n" +

                "         .itemImageContent{width:" + img_width + "}\n" +

                "         .tableHead{border:solid 1px;border-color: #d5d5d5;color:#000}\n" +
                "         .tableEnd{border:solid 1px;border-color: #d5d5d5;color:#747171;font-size:small;}\n" +
                "         .dropImg{width:8px;height:16px;vertical-align:middle;}\n" +
                "         .tableWrapper{ text-align: center;margin:16 0;}\n" +
                "         .priceTable{ border-collapse:collapse; " +
                "                            border-style: solid;border-width:1px;border-color: #d5d5d5;" +
                "                            width: 100%;}\n" +
                "         .priceTh{background-color:f5f5f5;" +
//                "                       border-bottom-width:1px; border-style: solid; border-color: #d5d5d5;" +
                "                       padding-top:10px;padding-bottom:10px;}\n" +
                "         .priceTd{ width:50%;padding:8px 10px;}\n" +
                "         .contentEnd{ border:solid 1px; border-color: #d5d5d5;border-top-width:0px;padding: 0 0 10 0;}\n" +

                "         @media screen and (max-width: 1201px){\n" +
                "                            .nativeTitle{ font-size: 16;}\n" +
                "                            .nativeTFont{font-size:14;}\n" +
                "                            .dealAmount{font-size:14;}\n" +
                "         }   \n" +
                "         @media screen and (max-width: 1081px){\n" +
                "                            .nativeTitle{ font-size: 16;}\n" +
                "                            .nativeTFont{font-size:14;}\n" +
                "                            .dealAmount{font-size:14;}\n" +
                "         }   \n" +
                "         @media screen and (max-width: 721px){\n" +
                "                            .nativeTitle{ font-size: 16;}\n" +
                "                            .nativeTFont{font-size:14;}\n" +
                "                            .dealAmount{font-size:14;}\n" +
                "         }   \n" +
                "         @media screen and (max-width: 601px){\n" +
                "                            .nativeTitle{ font-size: 16;}\n" +
                "                            .nativeTFont{font-size:14;}\n" +
                "                            .dealAmount{font-size:14;}\n" +
                "         }   \n" +
                "         @media screen and (max-width: 541px){\n" +
                "                            .nativeTitle{ font-size: 14;}\n" +
                "                            .nativeTFont{font-size:12;}\n" +
                "                            .dealAmount{font-size:12;}\n" +
                "         }   \n" +
                "         @media screen and (max-width: 481px){\n" +
                "                            .nativeTitle{ font-size: 14;}\n" +
                "                            .nativeTFont{font-size:12;}\n" +
                "                            .dealAmount{font-size:12;}\n" +
                "         }   \n" +
                "         @media screen and (max-width: 321px){\n" +
                "                            .nativeTitle{ font-size: 12;}\n" +
                "                            .nativeTFont{font-size:10;}\n" +
                "                            .dealAmount{font-size:10;}\n" +
                "         }   \n" +


                "</style>\n" +
                "</head>\n" +
                "<body>";
        value += contentHtml;
//        value = value.replace("<img", "<img width=\"" + (((Activity) getContext()).getWindowManager().getDefaultDisplay().getWidth() / (int) DisplayTools.getScreenDensity(getContext())) + "\"");
        value = value.replace("[data]", createHtmlTable(priceModels, tableHead, tableFooter));
        value += "</body></html>";
        DebugLog.i("test_s_info", value);
        mIntroduceView.loadDataWithBaseURL("", value, "text/html", "UTF-8", "");
    }


    /**
     * 替换图片拼接
     */
    public void exchangeImgTags() {

        for (int i = 0; i < attachments.size(); i++) {
            StringBuffer sb = new StringBuffer();
//            String newStr = "<div class='itemImg'><img onclick=\"redirectItem('"+i+"')\" src=\""+attachments.get(i).url+"\" /></div>";
            sb.append("<div class='itemImg'><img class='itemImageContent' onclick=\"redirectItem('");
            sb.append(i);
            sb.append("')\" src=\"");
            sb.append(attachments.get(i).url);
            sb.append("\" /></div>");
            String format = String.format("[IMG_%d]", i + 1);
            DebugLog.i("test_img", format);
            contentHtml = contentHtml.replace(format, sb.toString());
        }
        DebugLog.i("test_s_info", contentHtml);
    }

    /**
     * 清除a标记
     */
    public void clearATag() {
        contentHtml = contentHtml.replaceAll("<a [^>]+>", "");
        contentHtml = contentHtml.replaceAll("</[ ]*a>", "");
    }

    /**
     * 拼接 促销详情的表单
     */
    private String createHtmlTable(ArrayList< SalesItem > priceModels, String tableHead, String tableFooter) {

        if (priceModels == null) {
            return "";
        } else {
            if (priceModels.size() <= 0) {
                return "";
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append("<div class=\"tableWrapper\"><table class='priceTable'><tr class='tableHead'><th class='priceTh' colspan='2'>");
                sb.append(tableHead);
                sb.append("</th></tr>");

                for (int i = 0; i < priceModels.size(); i++) {

                    SalesItem item = priceModels.get(i);
                    AutoDetailThirdShop autoInfoShop = item.baseInfo;
                    sb.append("<tr class='contentHead'><td class='priceTd' colspan='2'><span class=\"nativeTitle\">");
                    sb.append(autoInfoShop.itemName);
                    sb.append("</span></td></tr><tr><td class='priceTd'><span class='nativeTFont'>店铺参考价:</span><span class=\"dealAmount\">");
                    sb.append(autoInfoShop.getStringItemPrice());
                    sb.append("</span></td><td class='priceTd'><span class='nativeTFont'>优惠金额:</span><span class='nativeTFont'>");
                    sb.append(autoInfoShop.getItemFall());
                    sb.append("</span><img class='dropImg' src='");
                    sb.append("file:///android_asset/img_search_drop.png");
                    sb.append("' /></td></tr><tr class='contentEnd'><td class='priceTd' colspan='1'><span class='nativeTFont'>厂商指导价:</span><span class='nativeTFont' style='text-decoration:line-through'>");
                    sb.append(autoInfoShop.getStringItemOrigPrice());
                    sb.append("</span></td><td class='priceTd' ></td></tr>");

                }

                sb.append("<tr class='tableEnd'><td class='priceTd' colspan='2' style=\"text-align: center;\"><span>");
                sb.append(tableFooter);
                sb.append("</span></td></tr></table></div>");

                return sb.toString();
            }
        }
    }

    /**
     * 开始loading动画
     */
    public void startLoadingAnim() {
        if (loadingRoundView == null) return;
        loadingRoundView.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
        LinearInterpolator interpolator = new LinearInterpolator();
        animation.setInterpolator(interpolator);
        loadingRoundView.startAnimation(animation);
    }

    /**
     * 停止 并 隐藏loading动画
     */
    public void cancelLoadingAnim() {
        if (animation != null)
            animation.cancel();

        if (loadingRoundView != null) {
            loadingRoundView.clearAnimation();
            loadingRoundView.setVisibility(GONE);
        }
        animation = null;
    }

    public class InfoWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            cancelLoadingAnim();
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            startLoadingAnim();
            super.onPageStarted(view, url, favicon);
        }
    }

}
