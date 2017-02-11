package com.hxqc.newenergy.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hxqc.mall.activity.WebActivity;
import com.hxqc.mall.activity.auto.BrandActivity;
import com.hxqc.mall.activity.auto.FilterAutoActivity;
import com.hxqc.mall.activity.auto.SpecialOfferActivity;
import com.hxqc.mall.core.model.Brand;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.newenergy.activity.EV_ModelsandsubsidiesDatailActivity;
import com.hxqc.newenergy.activity.Ev_ModelAndSubsidyActivity;
import com.hxqc.newenergy.activity.Ev_NewEnergyCarTemaiActivity;
import com.hxqc.newenergy.activity.Ev_NewsWebActivity;
import com.hxqc.newenergy.activity.Ev_RecommendMoreActivity;
import com.hxqc.newenergy.activity.PositionActivity;

import hxqc.mall.R;


/**
 * 说明:新能源页面七啊黄
 * author: 胡俊杰
 * since: 2016年3月9日
 * Copyright:恒信汽车电子商务有限公司
 */
public class ActivitySwitcherEV extends ActivitySwitchBase {

    /**
     * 新能源车辆品牌
     *
     * @param context
     * @param itemCategory
     */
    public static void toEVBrand(Context context, String itemCategory) {
        Bundle bundle = new Bundle();
        bundle.putString("itemCategory", itemCategory);
        Intent intent = new Intent(context, FilterAutoActivity.class);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    public static void toEVRecommendMore(Context context, String recommendType) {

        context. startActivity(new Intent(context,
                Ev_RecommendMoreActivity.class).putExtra("recommendType", recommendType));
    }
    /**
     * 新能源车辆特卖列表
     * @param context
     */
    public static void toWiki(Context context) {
        Intent intent = new Intent(context, SpecialOfferActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("itemCategory","20");
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }
    /**
     * 百科知识
     *
     * @param context http://10.0.15.201:8089/Ecar/V1/Html/wikiIndex
     */
    public static void toWikiActivity(Context context) {
        Intent intent = new Intent(context, Ev_NewsWebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, context.getString(R.string.title_activity_me_wiki));
        bundle.putString(WebActivity.URL, "http://10.0.15.201:8089/Ecar/V1/Html/wikiIndex");
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 百科单品页面
     */
    public static void toSingleWikiPage(Context context,String url,String title){
        Intent intent = new Intent(context, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, title);
        bundle.putString(WebActivity.URL, url);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    /** 首页定位页面 **/
    public static void toPositionActivity(Context context, int requestCode, String position) {
        Intent intent = new Intent(context, PositionActivity.class);
        intent.putExtra("position", position);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }


    /**
     * 跳转到特卖列表
     * @param context
     */
    public static void toTemaiActivity(Context  context){
       context.startActivity(new Intent(context, Ev_NewEnergyCarTemaiActivity.class));
    }
    /**
     * 车型及补贴资料界面
     * @param context
     */
    public static void toModelAndSubsidyActivity(Context  context){
       context.startActivity(new Intent(context, Ev_ModelAndSubsidyActivity.class));
    }

    public static void toBrand(Context context, Brand brand) {
        Intent intent = new Intent(context, BrandActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("itemCategory","20");
        bundle.putParcelable("brand",brand);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /** 跳转车辆补贴详情 **/
    public static void toModelAndSubsidyDetail(Context context, String mAutoId, String area) {
        Intent intent = new Intent(context, EV_ModelsandsubsidiesDatailActivity.class);
        intent.putExtra("autoID", mAutoId);
        intent.putExtra("area", area);
        context.startActivity(intent);
    }

}
