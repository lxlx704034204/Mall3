package com.hxqc.mall.thirdshop.maintenance.views;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;

import com.hxqc.util.DisplayTools;
import com.hxqc.mall.thirdshop.maintenance.inter.TPSMaintenancePJSInterface;
import com.hxqc.mall.thirdshop.maintenance.model.promotion.PromotionInfo_m;
import com.hxqc.mall.thirdshop.views.InfoIntroduceView;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;

/**
 * Author: wanghao
 * Date: 2016-03-23
 * FIXME
 * Todo
 */
public class MaintenanceHTMLView extends InfoIntroduceView {

    PromotionInfo_m model;
    Context ctx;
    private int screenWidth;

    public MaintenanceHTMLView(Context context) {
        super(context);
        this.ctx = context;
        initSet();
    }

    public MaintenanceHTMLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        initSet();
    }

    public void setHTMLDatas(PromotionInfo_m datas) {
        if (datas != null) {

            this.model = datas;

            if (datas.attachments != null) {
                attachments = datas.attachments;
                control.setAttachments(attachments);
            }
//            contentHtml = datas.content;
            contentHtml = Html.fromHtml(datas.content).toString();
            DebugLog.i("mphtml","setHTMLDatas:  "+ contentHtml);

            mIntroduceView.addJavascriptInterface(new TPSMaintenancePJSInterface(model.shopInfo.shopID, getContext(), control), "Android");
            clearATag();

            if (attachments != null)
                exchangeImgTags();

            createHTMLView();
        }
    }

    void initSet() {

        screenWidth = ((Activity) getContext()).getWindowManager().getDefaultDisplay().getWidth() / (int) DisplayTools.getScreenDensity(getContext());

        reWriteWebview();

        mIntroduceView.getSettings().setLoadWithOverviewMode(true);
        mIntroduceView.getSettings().setJavaScriptEnabled(true);
        mIntroduceView.getSettings().setUseWideViewPort(true);
    }

    /**
     * 拼接html代码
     */
    private void createHTMLView() {
        contentHtml = contentHtml.replace("[data]",tableTag());

//        for (int i=0;i<model.maintenancePackages.size();i++){
//
//            model.maintenancePackages.get(i).suitable = "[]";
//        }
        String jsonData = JSONUtils.toJson(model.maintenancePackages);
        DebugLog.i("mphtml","createHTMLView   1:  "+ jsonData);
        String valueHTML = readFileFromAssets("promotion/promotion_m.html");
        contentHtml = contentHtml.replace("<img", "<img width=\"" + (screenWidth-32) + "\"");

        DebugLog.i("mphtml","createHTMLView   2:  "+ screenWidth);

        valueHTML = valueHTML.replace("[jsonData]", jsonData);
        valueHTML = valueHTML.replace("[content]", contentHtml);
        valueHTML += "</body></html>";
        mIntroduceView.loadDataWithBaseURL("", valueHTML, "text/html", "UTF-8", "");


    }

    private String tableTag() {
        return "<section class=\"activity\" id=\"activity\"></section>";
    }

    private String readFileFromAssets(String assetName) {
        String res = "";
        try {
            //得到资源中的asset数据流
            InputStream in = ctx.getResources().getAssets().open(assetName);
            int length = in.available();
            byte[] buffer = new byte[length];

            in.read(buffer);
            in.close();
            res = EncodingUtils.getString(buffer, "UTF-8");
            return res;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 替换图片标签
     */
    @Override
    public void exchangeImgTags() {
        for (int i = 0; i < attachments.size(); i++) {
            StringBuffer sb = new StringBuffer();
//            String newStr = "<div class='itemImg'><img onclick=\"redirectItem('"+i+"')\" src=\""+attachments.get(i).url+"\" /></div>";
            sb.append("<div class='itemImg'><img class='itemImageContent' onclick=\"redirectItem('");
            sb.append(i);
            sb.append("','" + TPSMaintenancePJSInterface.IMAGE_ITEM + "')\" src=\"");
            sb.append(attachments.get(i).url);
            sb.append("\" /></div>");
            String format = String.format("[IMG_%d]", i + 1);
            DebugLog.i("mphtml", format);
            contentHtml = contentHtml.replace(format, sb.toString());
        }
    }
}
