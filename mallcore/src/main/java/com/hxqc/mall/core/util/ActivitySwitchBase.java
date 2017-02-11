package com.hxqc.mall.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.hxqc.mall.activity.WebActivity;
import com.hxqc.mall.auto.activity.AutoInfoActivityV3;
import com.hxqc.mall.auto.activity.MaintainAutoInfoActivity;
import com.hxqc.mall.auto.activity.automodel.ChooseBrandActivity;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.model.Brand;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2015-03-23
 * FIXME
 * 页面跳转
 */
public class ActivitySwitchBase {
    public static final String KEY_DATA = "data";
    public static final String TAB = "tab";
    public static final int NO_MENU = 0;
    public static final int CANCEL = 1;
    public static final int REFUND = 2;
    public static final String TO_WHERE = "to_where";
    /**
     * 开启高德导航
     */
    final public static String MAP_OPERATOR = "map_operator";
    final public static String SHOP_AMAP = "shop_amap";
    /**
     * 注册code
     */
    final public static int ENTRANCE_SHOP = 50; //商城
    final public static int ENTRANCE_THIRDSHOP = 90; //4s店，第三方店铺
    final public static int ENTRANCE_NEWENERGY = 130;//新能源
    final public static int ENTRANCE_ACCESSORYMAINTENANE = 170;//用品保养
    final public static int ENTRANCE_SECOUNDHANDCAR = 210; //二手车
    //大图   bundle
    public static String imagesBundle = "imagesData";
    public static String viewLargePosition = "viewLargePosition";
    public static String viewLargePics = "viewLargePics";

    /**
     * 任意界面
     *
     * @param context
     */
    public static void toWhere(Context context, String toWhereClassName) {
        toWhere(context, toWhereClassName, null);
    }

    /**
     * 任意界面
     *
     * @param context
     * @param toWhereClassName
     * @param bundle
     */
    public static void toWhere(Context context, String toWhereClassName, Bundle bundle) {
        if (!TextUtils.isEmpty(toWhereClassName)) {
            Intent intent = new Intent();
            intent.putExtra(KEY_DATA, bundle);
            intent.setClassName(context, toWhereClassName);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * 打开浏览器
     */
    public static void toWebBrowser(String url, Context context) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    /**
     * 主界面
     *
     * @param context
     */
    public static void toMain(Context context, int tab) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.mall.activity.MainActivity");
        intent.putExtra(TAB, tab);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        new BaseSharedPreferencesHelper(context).setTabChange(true);
        context.startActivity(intent);
    }

    /**
     * 活动详情
     */
    public static void toEventDetail(Context context, String url) {
        Bundle bundle = new Bundle();
        bundle.putString("event_url", url);
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.mall.activity.auto.EventDetailActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 订单详情
     */
    public static void toOrderDetail(Context context, String orderID) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("order_id", orderID);
        intent.putExtra(KEY_DATA, bundle);
//        intent.setAction("OrderDetail");
        intent.setClassName(context, "com.hxqc.mall.activity.order.OrderDetailActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    /**
     * 订单详情（保养）
     *
     * @param context
     * @param orderID
     */
    public static void toMaintainOrderDetail(Context context, String orderID) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("orderID", orderID);
        intent.putExtra(ActivitySwitchBase.KEY_DATA, bundle);
        intent.setClassName(context, "com.hxqc.mall.activity.order.MaintainOrderDetailsActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 大图片
     */
    public static void toViewLagerPic(int p, ArrayList<ImageModel> list, Context context, Bundle b) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.mall.activity.comment.ImageInfoActivity");
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(viewLargePics, list);
        bundle.putInt(viewLargePosition, p);
        if (b != null) {
            bundle.putInt("locationX", b.getInt("locationX"));
            bundle.putInt("locationY", b.getInt("locationY"));
            bundle.putInt("width", b.getInt("width"));
            bundle.putInt("height", b.getInt("height"));
        }
        intent.putExtras(bundle);
        context.startActivity(intent);
        Activity activity = (Activity) context;
        activity.overridePendingTransition(0, 0);
    }

    /**
     * 大图片
     *
     * @param p
     * @param imageModel
     * @param context
     * @param b
     */
    public static void toViewLagerPic(int p, ImageModel imageModel, Context context, Bundle b) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.aroundservice.activity.LargeImageActivity");
        Bundle bundle = new Bundle();
        bundle.putParcelable(viewLargePics, imageModel);
        bundle.putInt(viewLargePosition, p);
        if (b != null) {
            bundle.putInt("locationX", b.getInt("locationX"));
            bundle.putInt("locationY", b.getInt("locationY"));
            bundle.putInt("width", b.getInt("width"));
            bundle.putInt("height", b.getInt("height"));
        }
        intent.putExtras(bundle);
        context.startActivity(intent);
        Activity activity = (Activity) context;
        activity.overridePendingTransition(0, 0);
    }

    /**
     * 大图片
     *
     * @param p
     * @param imageModel
     * @param context
     * @param b
     */
    public static void toActivityLagerPic(int p, ImageModel imageModel, Context context, Bundle b) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.aroundservice.activity.IllegalDetailLargeImageActivity");
        Bundle bundle = new Bundle();
        bundle.putParcelable(viewLargePics, imageModel);
        bundle.putInt(viewLargePosition, p);
        if (b != null) {
            bundle.putInt("locationX", b.getInt("locationX"));
            bundle.putInt("locationY", b.getInt("locationY"));
            bundle.putInt("width", b.getInt("width"));
            bundle.putInt("height", b.getInt("height"));
        }
        intent.putExtras(bundle);
        context.startActivity(intent);
        Activity activity = (Activity) context;
        activity.overridePendingTransition(0, 0);
    }

    /**
     * 跳转会员中心车辆信息页面
     *
     * @param context
     * @param shopID
     */
    public static void toCenterAutoInfo(Context context, String shopID) {
        Intent intent = new Intent(context, AutoInfoActivityV3.class);
        Bundle bundle = new Bundle();
        bundle.putString("shopID", shopID);
        intent.putExtra(KEY_DATA, bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转保养车辆信息页面
     *
     * @param activity
     * @param shopID
     * @param flagActivity
     */
    public static void toMaintainAutoInfo(Activity activity, String shopID, int flagActivity) {
        Intent intent = new Intent(activity, MaintainAutoInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("shopID", shopID);
        bundle.putInt("flagActivity", flagActivity);
        intent.putExtra(KEY_DATA, bundle);
        activity.startActivityForResult(intent, AutoInfoContants.GET_AUTO_DATA);
    }

    /**
     * 跳转保养车辆信息页面
     *
     * @param context
     * @param myAuto
     */
    /*public static void toMaintainAutoInfo(Context context, MyAuto myAuto) {
        Intent intent = new Intent(context, MaintainAutoInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("myAuto", myAuto);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }*/

    /**
     * 跳转保养车辆信息页面
     *
     * @param activity
     * @param shopID
     * @param flagActivity
     * @param brands
     */
    public static void toMaintainAutoInfo(Activity activity, String shopID, int flagActivity, ArrayList<Brand> brands) {
        Intent intent = new Intent(activity, MaintainAutoInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("shopID", shopID);
        bundle.putInt("flagActivity", flagActivity);
        bundle.putParcelableArrayList("brands", brands);
        intent.putExtra(KEY_DATA, bundle);
        activity.startActivityForResult(intent, AutoInfoContants.GET_AUTO_DATA);
    }

    /**
     * 跳转在线预约页面
     *
     * @param context
     * @param shopID
     * @param shopTitle
     */
    public static void toReserveMaintain(Context context, String shopID, String shopTitle) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.mall.thirdshop.maintenance.activity.ReserveMaintainActivity");
        Bundle bundle = new Bundle();
        bundle.putString("shopID", shopID);
        bundle.putString("shopTitle", shopTitle);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 店铺详情
     *
     * @param context
     * @param toWhereClassName
     * @param shopID
     */
    public static void toShopDetails(Context context, String toWhereClassName, String shopID) {
        Intent intent = new Intent();
        if (!TextUtils.isEmpty(toWhereClassName)) {
            intent.putExtra("ShopID", shopID);
            intent.setClassName(context, toWhereClassName);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }
    }

    /**
     * 跳转到H5页面
     *
     * @param context
     * @param title
     * @param URL
     */
    public static void toH5Activity(Context context, String title, String URL) {
        DebugLog.i("ApiClient", URL);
        Intent intent = new Intent(context, WebActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, title);
        bundle.putBoolean(WebActivity.TOHOMEFLAG, true);
        bundle.putString(WebActivity.URL, URL);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    public static void toAMapNvai(Context context, PickupPointT shopLocation) {
        //因代码调整暂时注释
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.mall.thirdshop.activity.ThirdShopAMapActivity");
        intent.putExtra(SHOP_AMAP, shopLocation);
        intent.putExtra(MAP_OPERATOR, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 实名认证
     */
    public static void toRealNameAuthentication(Context context) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.mall.activity.me.password.RealNameAuthenticationActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 到维修预约界面
     **/
    public static void toAppointmentMaintenance(Context context) {

        Bundle bundle = new Bundle();
        bundle.putString("type", "1");
        toWhere(context, "com.hxqc.mall.thirdshop.maintenance.activity.FilterMaintenanceShopListActivity", bundle);
    }

    /**
     * 订单详情（4s保养）
     *
     * @param context
     * @param orderID
     */
    public static void to4SMaintainOrderDetail(Context context, String orderID) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("orderID", orderID);
        intent.putExtra(ActivitySwitchBase.KEY_DATA, bundle);
        intent.setClassName(context, "com.hxqc.mall.activity.order.Maintain4SShopOrderDetailsActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 订单详情（4s用品）
     *
     * @param context
     * @param orderID
     */
    public static void toAccessory4SShopOrderDetail(Context context, String orderID) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("orderID", orderID);
        intent.putExtra(ActivitySwitchBase.KEY_DATA, bundle);
        intent.setClassName(context, "com.hxqc.mall.activity.order.Accessory4SShopOrderDetailActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 订单详情（洗车）
     *
     * @param context
     * @param orderID
     */
    public static void toCarWashOrderDetails(Context context, String orderID) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("orderID", orderID);
        intent.putExtra(ActivitySwitchBase.KEY_DATA, bundle);
        intent.setClassName(context, "com.hxqc.fastreqair.activity.CarWashOrderDetailsActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 订单详情（预约维修）
     *
     * @param context
     * @param orderID
     */
    public static void toRepairOrderDetail(Context context, String orderID) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("orderID", orderID);
        intent.putExtra(ActivitySwitchBase.KEY_DATA, bundle);
        intent.setClassName(context, "com.hxqc.mall.activity.order.RepairOrderDetailsActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static Bundle getBundleExtra(Activity context) {
        if (context.getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA) == null)
            return new Bundle();
        return context.getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
    }

    /**
     * @param context
     * @param myAuto
     * @param flagActivity
     * @param shopID
     * @param isHome
     */
    public static void toChooseBrandActivity(Context context, MyAuto myAuto, int flagActivity, String shopID, boolean isHome) {
        Intent intent = new Intent(context, ChooseBrandActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("myAuto", myAuto);
        bundle.putInt("flagActivity", flagActivity);
        bundle.putString("shopID", shopID);
        intent.putExtra(KEY_DATA, bundle);
        if (isHome) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 返回数据
     *
     * @param context
     * @param myAuto
     * @param flagActivity
     * @param addFlag
     */
    public static void toBackData(Context context, MyAuto myAuto, int flagActivity, boolean addFlag) {
        Intent intent = null;
        if (flagActivity == AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_LIST || flagActivity == AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_HOME) {
            intent = new Intent();
            intent.setClassName(context, "com.hxqc.mall.thirdshop.maintenance.activity.FilterMaintenanceShopListActivity");
        } else if (flagActivity == AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_REPAIR_DETAIL || flagActivity == AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_4S) {
            intent = new Intent();
            intent.setClassName(context, "com.hxqc.mall.thirdshop.maintenance.activity.ReserveMaintainAndHeadActivity");
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("myAuto", myAuto);
        bundle.putInt("flagActivity", flagActivity);
        intent.putExtra(KEY_DATA, bundle);
        if (addFlag) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 返回数据
     *
     * @param activity
     * @param myAuto
     * @param className
     * @param resultCode
     */
    public static void toBackAutoData(Activity activity, MyAuto myAuto, String className, int resultCode) {
        Intent intent = new Intent();
        if (!TextUtils.isEmpty(className)) {
            intent.setClassName(activity, className);
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("myAuto", myAuto);
        intent.putExtra(KEY_DATA, bundle);
        activity.setResult(resultCode, intent);
    }

    /**
     * 返回数据
     *
     * @param activity
     * @param myAuto
     * @param cls
     * @param resultCode
     */
    public static void toBackAutoData(Activity activity, MyAuto myAuto, Class<?> cls, int resultCode) {
        Intent intent = null;
        if (cls == null) {
            intent = new Intent();
        } else {
            intent = new Intent(activity, cls);
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("myAuto", myAuto);
        intent.putExtra(KEY_DATA, bundle);
        activity.setResult(resultCode, intent);
    }
}
