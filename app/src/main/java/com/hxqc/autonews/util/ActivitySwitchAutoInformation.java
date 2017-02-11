package com.hxqc.autonews.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hxqc.autonews.activities.AllCommentActivity;
import com.hxqc.autonews.activities.AutoGalleryActivity;
import com.hxqc.autonews.activities.AutoInfoDetailActivity;
import com.hxqc.autonews.activities.CommentDetailActivity;
import com.hxqc.autonews.activities.MyCommentActivity;
import com.hxqc.autonews.activities.NewAutoCalendarActivity;
import com.hxqc.autonews.activities.PublicCommentActivity;
import com.hxqc.autonews.activities.SendCommentActivity;
import com.hxqc.autonews.activities.UserCommentActivity;
import com.hxqc.autonews.model.UserGradeComment;
import com.hxqc.autonews.model.pojos.AutoImage;
import com.hxqc.carcompare.ui.addcar.CarChooseListActivity;
import com.hxqc.carcompare.ui.addcar.CarCompareListActivity;
import com.hxqc.carcompare.ui.discuss.DiscussDetailActivity;
import com.hxqc.carcompare.ui.discuss.UserDiscussActivity;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.thirdshop.activity.ShopBrandActivity;
import com.hxqc.mall.thirdshop.constant.FilterResultKey;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-09-05
 * FIXME
 * Todo 汽车资讯模块的activity之间跳转
 */
public class ActivitySwitchAutoInformation extends ActivitySwitchBase {
    /**
     * 汽车资讯图集
     *
     * @param context
     * @param infoID
     */
    public static void toAutoGallery(Context context, String infoID) {
        Intent intent = new Intent(context, AutoGalleryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(AutoGalleryActivity.INFO_ID, infoID);
        intent.putExtra(ActivitySwitchBase.KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 带数据过去汽车资讯图集
     *
     * @param context
     * @param images
     */
    public static void toAutoImages(Context context, ArrayList<AutoImage> images, int index) {
        Intent intent = new Intent(context, AutoGalleryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AutoGalleryActivity.IMAGES, images);
        bundle.putInt(AutoGalleryActivity.POSITION, index);
        intent.putExtra(ActivitySwitchBase.KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 汽车资讯图文
     *
     * @param context
     * @param infoID
     */
    public static void toAutoInfoDetail(Context context, String infoID) {
        Intent intent = new Intent(context, AutoInfoDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString("infoID", infoID);
        intent.putExtra(ActivitySwitchBase.KEY_DATA, bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 从新车日历进入详情
     *
     * @param context
     * @param infoID
     */
    public static void toAutoCalendarInfoDetail(Context context, String infoID) {

        Intent intent = new Intent(context, AutoInfoDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(AutoInfoDetailActivity.INFO_ID, infoID);
        bundle.putInt(AutoInfoDetailActivity.FROM, AutoInfoDetailActivity.AUTO_CALENDAR);
        intent.putExtra(ActivitySwitchBase.KEY_DATA, bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
//    /**
//     * 去汽车资讯列表
//     *
//     * @param context
//     * @param index
//     */
//    public static void toAutoInfoList(Context context, int index) {
//        Intent intent = new Intent();
//        intent.setClassName(context, "com.hxqc.mall.activity.MainActivity");
//        intent.putExtra(TAB, 1);
//        intent.putExtra(MainActivity.INFO_INDEX, index);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        new BaseSharedPreferencesHelper(context).setTabChange(true);
//        context.startActivity(intent);
//    }

    /**
     * 发表 口碑评价
     *
     * @param context
     * @param extID   车型ID
     * @param brand   品牌
     * @param series  车系
     * @param image   车辆封面图
     */
    public static void toSendPublicComment(final Context context, final String extID, final String brand, final String series, final String image) {
        UserInfoHelper.getInstance().loginAction(context, new UserInfoHelper.OnLoginListener() {
            @Override
            public void onLoginSuccess() {
                Bundle bundle = new Bundle();
                bundle.putString(FilterResultKey.EXT_ID, extID);
                bundle.putString(FilterResultKey.BRAND_KEY, brand);
                bundle.putString(FilterResultKey.SERIES_KEY, series);
                bundle.putString(FilterResultKey.IMG_KEY, image);
                context.startActivity(new Intent(context, SendCommentActivity.class).putExtra(ActivitySwitchBase.KEY_DATA, bundle));
            }
        });
    }

    /**
     * 口碑评价 列表
     *
     * @param context
     * @param extID   车型ID
     * @param brand   品牌
     * @param series  车系
     */
    public static void toPublicCommentList(Context context, String extID, String brand, String series) {
        Bundle bundle = new Bundle();
        bundle.putString(PublicCommentActivity.EXT_ID, extID);
        bundle.putString(PublicCommentActivity.BRAND, brand);
        bundle.putString(PublicCommentActivity.SERIES, series);
        context.startActivity(new Intent(context, PublicCommentActivity.class).putExtra(ActivitySwitchBase.KEY_DATA, bundle));
    }

    public static void toPublicCommentList2(Context context, String extID, String brand, String series) {
        Bundle bundle = new Bundle();
        bundle.putString("extID", extID);
        bundle.putString("brand", brand);
        bundle.putString("series", series);
        context.startActivity(new Intent(context, UserDiscussActivity.class).putExtras(bundle));
    }

    /**
     * 口碑评价 单条详情
     *
     * @param context
     * @param comment
     */
    public static void toPublicCommentDetail(Context context, UserGradeComment comment) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(UserCommentActivity.COMMENT, comment);
        context.startActivity(new Intent(context, UserCommentActivity.class).putExtra(ActivitySwitchBase.KEY_DATA, bundle));
    }

    public static void toPublicCommentDetail2(Context context) {
        context.startActivity(new Intent(context, DiscussDetailActivity.class));
    }

    /**
     * 资讯评价 列表
     *
     * @param context
     * @param infoID  资讯ID
     */
    public static void toMessageCommentList(Context context, String infoID, int count) {
        Bundle bundle = new Bundle();
        bundle.putString(AllCommentActivity.INFO_ID, infoID);
        bundle.putInt(AllCommentActivity.COUNT, count);
        context.startActivity(new Intent(context, AllCommentActivity.class).putExtra(ActivitySwitchBase.KEY_DATA, bundle));
    }

    /**
     * 资讯评价 单条详情
     *
     * @param context
     * @param infoID     资讯ID
     * @param pCommentID 评论ID
     */
    public static void toMessageCommentDetail(Context context, String infoID, String pCommentID) {
        Bundle bundle = new Bundle();
        bundle.putString(CommentDetailActivity.INFO_ID, infoID);
        bundle.putString(CommentDetailActivity.P_COMMENT_ID, pCommentID);
        context.startActivity(new Intent(context, CommentDetailActivity.class).putExtra(ActivitySwitchBase.KEY_DATA, bundle));
    }

    /**
     * 我的评价
     *
     * @param context
     */
    public static void toMyComment(final Context context) {
        UserInfoHelper.getInstance().loginAction(context, new UserInfoHelper.OnLoginListener() {
            @Override
            public void onLoginSuccess() {
                context.startActivity(new Intent(context, MyCommentActivity.class));
            }
        });
    }


    /**
     * 车型对比
     *
     * @param context
     * @param extID
     * @param brand
     * @param modelName
     */
    public static void toCarCompare(Context context, String extID, String brand, String modelName) {
        Bundle bundle = new Bundle();
        bundle.putString(FilterResultKey.EXT_ID, extID);
        bundle.putString(FilterResultKey.BRAND_KEY, brand);
        bundle.putString(FilterResultKey.MODEL_KEY, modelName);
        context.startActivity(new Intent(context, CarCompareListActivity.class).putExtra(ActivitySwitchBase.KEY_DATA, bundle));
    }

    public static void toCarChooseList(Context context) {
        context.startActivity(new Intent(context, CarChooseListActivity.class));
    }

    /**
     * 筛选列表
     *
     * @param context
     */
    public static void toFilterAll(Context context) {
        Intent intent = new Intent(context, ShopBrandActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(ShopBrandActivity.FROM_4S_HOME, true);
        bundle.putBoolean(ShopBrandActivity.FROM_CAR_COMPARE, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent.putExtra(ActivitySwitchBase.KEY_DATA, bundle));
    }

    /**
     * 去新车日历界面
     *
     * @param context
     */
    public static void toNewAutoCalendar(Context context) {
        Intent intent = new Intent(context, NewAutoCalendarActivity.class);
        context.startActivity(intent);
    }

}
