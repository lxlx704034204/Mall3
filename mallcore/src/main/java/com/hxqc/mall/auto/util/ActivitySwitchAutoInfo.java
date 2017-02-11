package com.hxqc.mall.auto.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hxqc.mall.activity.coupon.MyCouponActivity;
import com.hxqc.mall.auto.activity.AutoDetailActivity;
import com.hxqc.mall.auto.activity.AutoInfoActivityV3;
import com.hxqc.mall.auto.activity.CompleteDialogActivity;
import com.hxqc.mall.auto.activity.MaintainAutoInfoActivity;
import com.hxqc.mall.auto.activity.MaintainEditAutoActivity;
import com.hxqc.mall.auto.activity.RepairRecordActivity;
import com.hxqc.mall.auto.activity.automodel.ChooseAutoModelActivity;
import com.hxqc.mall.auto.activity.automodel.ChooseBrandActivity;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.controler.AutoSPControl;
import com.hxqc.mall.auto.model.Brand;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.core.util.ActivitySwitchBase;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 23
 * Des: 车辆信息页面跳转控制
 * FIXME
 * Todo
 */
public class ActivitySwitchAutoInfo extends ActivitySwitchBase {


    /**
     * 为完善车辆弹窗
     *
     * @param context
     * @param myAuto
     * @param flagAcivity
     */
    public static void toDialogActivity(Context context, MyAuto myAuto, int flagAcivity) {
        Intent intent = new Intent(context, CompleteDialogActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("myAuto", myAuto);
        bundle.putInt("flagActivity", flagAcivity);
        intent.putExtra(KEY_DATA, bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 车辆信息页面
     *
     * @param activity
     * @param myAuto
     */
    public static void toBackAutoData(Activity activity, MyAuto myAuto, boolean isAdd) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("myAuto", myAuto);
        intent.putExtra(KEY_DATA, bundle);
        if (isAdd) {
            activity.setResult(AutoInfoContants.ADD_AUTO, intent);
        } else {
            activity.setResult(AutoInfoContants.EDIT_AUTO, intent);
        }
    }

    /**
     * 车辆信息页面
     *
     * @param context
     */
    public static void toBackAutoData(Context context) {
        Intent intent = new Intent(context, AutoInfoActivityV3.class);
        Bundle bundle = new Bundle();
        intent.putExtra(KEY_DATA, bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 会员中心车辆列表
     *
     * @param activity
     * @param myAuto
     */
    /*public static void toCenterAutoInfo(Activity activity, MyAuto myAuto) {
        Intent intent = new Intent(activity, AutoInfoActivityV3.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("myAuto", myAuto);
        intent.putExtra(KEY_DATA, bundle);
        activity.setResult(AutoInfoContants.CHOOSE_SUCCESS, intent);
    }*/

    /**
     * 跳转保养车辆信息页面
     *
     * @param activity
     * @param shopID
     * @param myAutos
     * @param flagActivity
     */
    public static void toMaintainAutoInfo(Activity activity, String shopID, ArrayList<MyAuto> myAutos, int flagActivity, ArrayList<Brand> brands) {
        Intent intent = new Intent(activity, MaintainAutoInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("shopID", shopID);
        if (myAutos != null) {
            bundle.putParcelableArrayList("myAutos", myAutos);
        }
        if (brands != null) {
            bundle.putParcelableArrayList("brands", brands);
        }
        bundle.putInt("flagActivity", flagActivity);
        intent.putExtra(KEY_DATA, bundle);
        activity.startActivityForResult(intent, AutoInfoContants.GET_AUTO_DATA);
    }

    /**
     * 品牌车型车系跳转添加车辆
     *
     * @param context
     * @param myAuto
     * @param flagActivity
     */
    public static void toAddAuto(Context context, MyAuto myAuto, int flagActivity) {
        Intent intent = new Intent(context, MaintainEditAutoActivity.class);
        Bundle bundle = new Bundle();
        if (myAuto != null) {
            bundle.putParcelable("myAuto", myAuto);
        }
        bundle.putInt("flagActivity", flagActivity);
        intent.putExtra(KEY_DATA, bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 添加修改车辆信息页面
     *
     * @param activity
     * @param myAuto
     * @param shopID
     * @param flagActivity
     * @param flagPage
     * @param cls
     */
    public static void toAddOrEditAutoInfo(Activity activity, MyAuto myAuto, String shopID, int flagActivity, int flagPage, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        Bundle bundle = new Bundle();
        if (myAuto != null) {
            bundle.putParcelable("myAuto", myAuto);
        }
        bundle.putString("shopID", shopID);
        bundle.putInt("flagActivity", flagActivity);
        bundle.putInt("flagPage", flagPage);
        intent.putExtra(KEY_DATA, bundle);
        activity.startActivityForResult(intent, AutoInfoContants.REQUEST_ADD_OR_EDIT);

    }

    /**
     * 首页跳转保养流程
     */
    public static void toFlowMaintain(Context context) {
//        Intent intent = new Intent(c, AddOrEditAutoInfoActivity.class);
        /*Intent intent = new Intent(context, MaintainEditAutoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("shopID", "");
        bundle.putInt("flagActivity", AutoInfoContants.HOME_PAGE);
        intent.putExtra(KEY_DATA, bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);*/
        AutoInfoControl.getInstance().isHaveAuto(context);
    }

    /**
     * @param context
     */
    public static void toMaintainEditAutoInfo(Context context) {
        Intent intent = new Intent(context, MaintainEditAutoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("shopID", "");
        bundle.putInt("flagActivity", AutoInfoContants.HOME_PAGE);
        intent.putExtra(KEY_DATA, bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param flagActivity
     */
    /*public static void toMaintainEditAutoInfo(Context context, int flagActivity) {
        Intent intent = new Intent(context, MaintainEditAutoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("shopID", "");
        bundle.putInt("flagActivity", flagActivity);
        intent.putExtra(KEY_DATA, bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }*/

    /**
     * 保养记录页面
     *
     * @param context
     * @param myAuto
     */
    public static void toRepairRecord(Context context, MyAuto myAuto) {
        Intent intent = new Intent(context, RepairRecordActivity.class);
        Bundle bundle = new Bundle();
        if (myAuto != null) {
            bundle.putParcelable("myAuto", myAuto);
        }
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 车辆详情页面
     *
     * @param context
     * @param myAuto
     */
    public static void toAutoDetailActivity(Context context, MyAuto myAuto) {
        Intent intent = new Intent(context, AutoDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("myAuto", myAuto);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 查看明细
     *
     * @param context
     * @param isBalance 是否是现金余额账单
     * @param myAutoID
     */
    public static void toMyBillList(Context context, boolean isBalance, String myAutoID) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.mall.activity.me.bill.MyBillListActivity");
        Bundle bundle = new Bundle();
        bundle.putBoolean("com.hxqc.mall.activity.me.MyBillListActivity_data_type", isBalance);
        bundle.putString("myAutoID", myAutoID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
//        context.startActivity(new Intent(context, MyBillListActivity.class).putExtra(MyBillListActivity.TYPE, isBalance));
    }

    /**
     * to违章查缴
     *
     * @param context
     * @param myAuto
     */
    public static void toIllegalConfiscateActivity(Context context, MyAuto myAuto) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.aroundservice.activity.IllegalConfiscateActivity");
        Bundle bundle = new Bundle();
        bundle.putParcelable("myAuto", myAuto);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * to车辆年检
     *
     * @param context
     * @param myAuto
     */
    @Deprecated
    public static void toVehicleInspectionActivity(Context context, MyAuto myAuto) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.aroundservice.activity.VehicleInspectionActivity");
        Bundle bundle = new Bundle();
        bundle.putParcelable("myAuto", myAuto);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param myAuto
     */
    public static void toChooseBrandActivity(Context context, MyAuto myAuto) {
        Intent intent = new Intent(context, ChooseBrandActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("myAuto", myAuto);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param myAuto
     * @param flagActivity
     * @param isHome
     */
    public static void toChooseBrandActivity(Context context, MyAuto myAuto, int flagActivity, boolean isHome) {
        Intent intent = new Intent(context, ChooseBrandActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("myAuto", myAuto);
        bundle.putInt("flagActivity", flagActivity);
        intent.putExtra(KEY_DATA, bundle);
        if (isHome) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param myAuto
     * @param shopID
     * @param flagActivity
     */
    public static void toChooseAutoModelActivity(Context context, MyAuto myAuto, String shopID, int flagActivity) {
        Intent intent = new Intent(context, ChooseAutoModelActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("myAuto", myAuto);
        bundle.putInt("flagActivity", flagActivity);
        bundle.putString("shopID", shopID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * to我的优惠券列表
     *
     * @param context
     * @param myAutoID
     */
    public static void toMyCoupon(Context context, String myAutoID) {
        Intent intent = new Intent(context, MyCouponActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(MyCouponActivity.MY_AUTO_ID, myAutoID);
        intent.putExtra(KEY_DATA, bundle);
//        intent.setClassName(context, "com.hxqc.mall.activity.coupon.MyCouponActivity");
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param myAuto
     */
    public static void toShopQuoteActivity(Context context, MyAuto myAuto) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("myAuto", myAuto);
        ActivitySwitchBase.toWhere(context, "com.hxqc.mall.thirdshop.maintenance.activity.ShopQuoteActivity", bundle);
    }

    /**
     * @param context
     * @param shopID
     */
    public static void toShopDetails(Context context, String shopID) {
        Bundle bundle = new Bundle();
        bundle.putString("shopID", shopID);
        bundle.putInt("from", 0);
        ActivitySwitchBase.toWhere(context, "com.hxqc.mall.thirdshop.activity.shop.ShopHomeActivity", bundle);
    }

    /**
     * @param context
     * @param myAuto
     */
    public static void to4SShopMaintain(Context context, MyAuto myAuto) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("myAuto", myAuto);
        ActivitySwitchBase.toWhere(context, "com.hxqc.mall.thirdshop.activity.shop.MaintenanceHomeActivity_1", bundle);

    }


    /**
     * 跳新版修车预约界面 2017年1月13日
     *
     * @param context
     * @param myAuto
     */
    public static void toAppiontmentMaintenance(Context context, MyAuto myAuto) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.mall.thirdshop.maintenance.activity.FilterMaintenanceShopListActivity");
        Bundle bundle = new Bundle();
        bundle.putParcelable("myAuto", myAuto);
        intent.putExtra(KEY_DATA, bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(FilterMaintenanceShopListActivity.ACTIVITY_TYPE, FilterMaintenanceShopListActivity.APPOINTMENT_MAINTENANCE);
        context.startActivity(intent);
    }

    /**
     * 4S店铺->修车预约
     *
     * @param context
     * @param shopType
     * @param shopID
     * @param myAuto
     */
    public static void toReserveMaintainAndHeadActivity(Context context, String shopType, String shopID, MyAuto myAuto) {
        AutoSPControl.saveAppointmentInfo(context, shopID, "", shopType);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("shopID", shopID);
        bundle.putString("shopType", shopType);
        if (myAuto != null) {
            bundle.putParcelable("myAuto", myAuto);
        }
        intent.setClassName(context, "com.hxqc.mall.thirdshop.maintenance.activity.ReserveMaintainAndHeadActivity");
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 跳转在线预约页面
     *
     * @param context
     * @param shopID
     * @param shopTitle
     */
    public static void toReserveMaintainAndHeadActivity(Context context, MyAuto myAuto, String shopID, String shopTitle) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.mall.thirdshop.maintenance.activity.ReserveMaintainAndHeadActivity");
        Bundle bundle = new Bundle();
        if (myAuto != null) {
            bundle.putParcelable("myAuto", myAuto);
        }
        bundle.putString("shopID", shopID);
        bundle.putString("shopTitle", shopTitle);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }
}
