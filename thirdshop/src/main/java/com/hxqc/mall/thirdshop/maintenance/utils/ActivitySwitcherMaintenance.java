package com.hxqc.mall.thirdshop.maintenance.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hxqc.mall.activity.coupon.MyCouponActivity;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.controler.AutoSPControl;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.core.model.Coupon;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.thirdshop.control.ShopDetailsController;
import com.hxqc.mall.thirdshop.maintenance.activity.ChoicePayMaintenanceActivity;
import com.hxqc.mall.thirdshop.maintenance.activity.FilterMaintenanceShopListActivity;

import com.hxqc.mall.thirdshop.maintenance.activity.FilterMaintenanceShopListActivity2;
import com.hxqc.mall.thirdshop.maintenance.activity.FinishPayMaintenanceActivity;
import com.hxqc.mall.thirdshop.maintenance.activity.FourSAndQuickConfirmOrderActivity;
import com.hxqc.mall.thirdshop.maintenance.activity.FourSShopMaintenanceActivity;
import com.hxqc.mall.thirdshop.maintenance.activity.IntroduceShopActivity;
import com.hxqc.mall.thirdshop.maintenance.activity.MaintenanceRecordActivity;
import com.hxqc.mall.thirdshop.maintenance.activity.MaintenanceShopListActivity;
import com.hxqc.mall.thirdshop.maintenance.activity.MaintenanceShopListOnMapActivity;
import com.hxqc.mall.thirdshop.maintenance.activity.PackageListActivity;
import com.hxqc.mall.thirdshop.maintenance.activity.PositionActivity;
import com.hxqc.mall.thirdshop.maintenance.activity.ReserveMaintainActivity;
import com.hxqc.mall.thirdshop.maintenance.activity.ReserveMaintainAndHeadActivity;
import com.hxqc.mall.thirdshop.maintenance.activity.ReserveSuccessActivity;
import com.hxqc.mall.thirdshop.maintenance.activity.ServiceAdviserActivity;
import com.hxqc.mall.thirdshop.maintenance.activity.ServiceMechanicActivity;
import com.hxqc.mall.thirdshop.maintenance.activity.ServiceTypeActivity;
import com.hxqc.mall.thirdshop.maintenance.activity.ShopFinishPayActivity;
import com.hxqc.mall.thirdshop.maintenance.activity.ShopQuoteActivity;
import com.hxqc.mall.thirdshop.maintenance.adapter.OtherMaintenancePackageListAdapter;
import com.hxqc.mall.thirdshop.maintenance.config.MaintainContants;
import com.hxqc.mall.thirdshop.maintenance.model.MaintenanceShop;
import com.hxqc.mall.thirdshop.maintenance.model.Mechanic;
import com.hxqc.mall.thirdshop.maintenance.model.NewMaintenanceShop;
import com.hxqc.mall.thirdshop.maintenance.model.ServiceAdviser;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ReservationSuccessInfo;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.ServiceType;
import com.hxqc.mall.thirdshop.maintenance.model.order.CouponCombination;
import com.hxqc.mall.thirdshop.maintenance.model.order.CreateOrder;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Function:
 *
 * @since 2016年02月18日
 */
public class ActivitySwitcherMaintenance extends ActivitySwitchBase {
    final public static String MAP_OPERATOR = "map_operator";
    final public static String SHOP_AMAP = "shop_amap";
    private final static String TAG = ActivitySwitcherMaintenance.class.getSimpleName();
    private Context mContext;


    /**
     * 定位选择
     **/
    public static void toPositionActivity(Context context, int requestCode, String position) {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(context, PositionActivity.class);
        intent.setComponent(componentName);
        intent.putExtra("position", position);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }


    /**
     * 到常规保养界面
     **/
    public static void toNormalMaintenance(Context context) {
        Intent intent = new Intent(context, FilterMaintenanceShopListActivity2.class);
        intent.putExtra(FilterMaintenanceShopListActivity2.ACTIVITY_TYPE, FilterMaintenanceShopListActivity2.NORMAL_MAINTENANCE);
        context.startActivity(intent);
    }


    /**
     * 到维修预约界面
     **/
    public static void toAppointmentMaintenance(Context context) {
//        Intent intent = new Intent(context, FilterMaintenanceShopListActivity2.class);
//        intent.putExtra(FilterMaintenanceShopListActivity2.ACTIVITY_TYPE, FilterMaintenanceShopListActivity2.APPOINTMENT_MAINTENANCE);
//        context.startActivity(intent);
        AutoInfoControl.getInstance().toActivityInter(context, AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_HOME, null);
    }


    /**
     * 店铺详情
     *
     * @param context
     * @param shopID  店铺ID
     */
    public static void toShopDetails(Context context, int type, String shopID) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.mall.thirdshop.activity.ShopDetailsActivity");
        intent.putExtra("ShopID", shopID);
        if (type == 0) {
            intent.putExtra("DEFAULT", "2");
        } else if (type == 1) {
            intent.putExtra("DEFAULT", "3");
        }
        context.startActivity(intent);
    }


    /**
     * 车辆保养记录
     *
     * @param context
     * @param autoID
     */
    public static void toMaintenanceRecord(Context context, String autoID, String VIN) {
        Intent intent = new Intent(context, MaintenanceRecordActivity.class).putExtra(MaintenanceRecordActivity.AUTO_ID, autoID).putExtra(MaintenanceRecordActivity.VIN_CODE, VIN);
        context.startActivity(intent);
    }




    /**
     * 选择支付方式
     *
     * @param context
     * @param createOrder
     * @param shopID      有则传 无则""
     * @param flag        1 正常流程 2 订单列表 3 首页订单
     */
    public static void toPayChoice(Context context, CreateOrder createOrder, String shopID, String flag) {
        Intent intent = new Intent(context, ChoicePayMaintenanceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("createOrder", createOrder);
        bundle.putString("shopID", shopID);
        bundle.putString("flag", flag);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 支付完成
     *
     * @param context
     */
    public static void toPayFinish(Context context) {
        Intent intent = new Intent(context, FinishPayMaintenanceActivity.class);
//        intent.putExtra("shopID", shopID);
//        intent.putExtra("flag", flag);
//        intent.putExtra("orderID", orderID);
        context.startActivity(intent);
    }


    /**
     * 在线预约测试
     *
     * @param context
     */
    public static void toTestReserveMaintain(Context context) {
        Intent intent = new Intent(context, ReserveMaintainActivity.class);
        context.startActivity(intent);
    }


    /**
     * 在线预约
     *
     * @param context
     * @param maintenanceShop
     */
    public static void toReserveMaintain(Context context, MaintenanceShop maintenanceShop) {
        Intent intent = new Intent(context, ReserveMaintainActivity.class);
//        intent.putExtra(ReserveMaintainActivity.MAINTEANCESHOP, maintenanceShop);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ReserveMaintainActivity.MAINTEANCESHOP, maintenanceShop);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 修车预约
     *
     * @param context
     * @param shopID
     */
    @Deprecated
    public static void toReserveMaintainAndHeadActivity(Context context, String shopType, String shopID) {
//        Intent intent = new Intent(context, InstallmentBuyingActivity.class);
        Intent intent = new Intent(context, ReserveMaintainAndHeadActivity.class);
//        intent.putExtra(ShopDetailsController.SHOPID_KEY, shopID);
        Bundle bundle = new Bundle();
        bundle.putString(ShopDetailsController.SHOPID_KEY, shopID);
        bundle.putString(ShopDetailsController.SHOP_TYPE, shopType);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 修车预约
     *
     * @param context
     * @param shopID
     */
    public static void toReserveMaintainAndHeadActivity(Context context, String shopType, String shopID, MyAuto myAuto) {
//        myAuto = new MyAuto();
//        myAuto.brand = "奥迪";
//        myAuto.brandID = "101510064";
//        myAuto.brandName = "一汽-大众奥迪";
//        myAuto.brandThumb = "http://s.t.hxqctest.com/newcar/product/images/aa/34/aa34c75bbed726d4b3ecd73bedfbf24f.png";
//        myAuto.series = "奥迪Q5";
//        myAuto.seriesID = "101710167";
//        myAuto.autoModel = "奥迪Q5 2016款 40 TFSI 舒适型";
//        myAuto.autoModelID = "101616897";

        AutoSPControl.saveAppointmentInfo(context, shopID, "", shopType);
        AutoSPControl.saveAppointmentFlag(context, AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_LIST);
        Intent intent = new Intent(context, ReserveMaintainAndHeadActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ShopDetailsController.SHOPID_KEY, shopID);
        bundle.putString(ShopDetailsController.SHOP_TYPE, shopType);
        bundle.putParcelable("myAuto", myAuto);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 服务类型to在线预约
     * @param activity
     * @param serviceTypeList
     */
    @Deprecated
    public static void toReserveMaintain(Activity activity, HashMap< Integer, String > serviceTypeList) {
        Intent intent = new Intent();
        intent.putExtra("serviceTypeBackData", serviceTypeList);
        activity.setResult(MaintainContants.SERVICE_TYPE_DATA_BACK, intent);
    }


    /**
     * 服务类型to在线预约
     *
     * @param activity
     * @param serviceType
     */
    public static void toReserveMaintain(Activity activity, ServiceType serviceType, ArrayList< ServiceType > serviceTypeGroup) {
        Intent intent = new Intent();
//        intent.putExtra("serviceTypeBackData", serviceType);
        Bundle bundle = new Bundle();
        bundle.putParcelable("serviceTypeBackData", serviceType);
        bundle.putParcelableArrayList("serviceTypeGroupBackData", serviceTypeGroup);
        intent.putExtra(KEY_DATA, bundle);
        activity.setResult(MaintainContants.SERVICE_TYPE_DATA_BACK, intent);
    }


    /**
     * 服务顾问to在线预约
     *
     * @param activity
     * @param serviceAdviser
     * @param position
     */
    public static void toReserveMaintain(Activity activity, ServiceAdviser serviceAdviser, int position) {
        Intent intent = new Intent();
//        intent.putExtra("serviceAdviser", serviceAdviser);
//        intent.putExtra("position", position);
        Bundle bundle = new Bundle();
        bundle.putParcelable("serviceAdviser", serviceAdviser);
        bundle.putInt("position", position);
        intent.putExtra(KEY_DATA, bundle);
        activity.setResult(MaintainContants.SERVCE_ADVISER_OR_MECHANIC_BACK_DATA, intent);
    }


    /**
     * 服务技师to在线预约
     *
     * @param activity
     * @param mechanic
     * @param position
     */
    public static void toReserveMaintain(Activity activity, Mechanic mechanic, int position) {
        Intent intent = new Intent();
//        intent.putExtra("mechanic", mechanic);
//        intent.putExtra("position", position);
        Bundle bundle = new Bundle();
        bundle.putParcelable("mechanic", mechanic);
        bundle.putInt("position", position);
        intent.putExtra(KEY_DATA, bundle);
        activity.setResult(MaintainContants.SERVCE_ADVISER_OR_MECHANIC_BACK_DATA, intent);
    }


    /**
     * 服务类型
     *
     * @param activity
     * @param requestCode
     * @param shopID
     * @param serviceTypes
     */
    public static void toServiceType(Activity activity, int requestCode, String shopID, ArrayList< ServiceType > serviceTypes, ServiceType serviceType) {
//        Intent intent = new Intent(activity.getApplicationContext(),ServiceTypeTestActivity.class);
        Intent intent = new Intent(activity.getApplicationContext(), ServiceTypeActivity.class);
//        intent.putExtra("shopID", shopID);
//        intent.putParcelableArrayListExtra("serviceTypes", serviceTypes);
        Bundle bundle = new Bundle();
        bundle.putString("shopID", shopID);
        bundle.putParcelableArrayList("serviceTypes", serviceTypes);
        bundle.putParcelable("serviceType", serviceType);
        intent.putExtra(KEY_DATA, bundle);
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 预约服务顾问
     *
     * @param activity
     * @param requestCode
     * @param shopID
     * @param serviceAdvisers
     * @param position
     */
    public static void toServiceAdviser(Activity activity, int requestCode, String shopID, ArrayList< ServiceAdviser > serviceAdvisers, int position) {
        Intent intent = new Intent(activity.getApplicationContext(), ServiceAdviserActivity.class);
//        intent.putExtra("serviceAdvisers", serviceAdvisers);
//        intent.putExtra("position", position);
//        intent.putExtra("shopID", shopID);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("serviceAdvisers", serviceAdvisers);
        bundle.putInt("position", position);
        bundle.putString("shopID", shopID);
        intent.putExtra(KEY_DATA, bundle);
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 预约服务技师
     *
     * @param activity
     * @param requestCode
     * @param shopID
     * @param mechanics
     * @param position
     */
    public static void toServiceMechanic(Activity activity, int requestCode, String shopID, ArrayList< Mechanic > mechanics, int position) {
        Intent intent = new Intent(activity.getApplicationContext(), ServiceMechanicActivity.class);
//        intent.putExtra("mechanics", mechanics);
//        intent.putExtra("position", position);
//        intent.putExtra("shopID", shopID);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("mechanics", mechanics);
        bundle.putInt("position", position);
        bundle.putString("shopID", shopID);
        intent.putExtra(KEY_DATA, bundle);
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 预约成功
     *
     * @param activity
     */
    @Deprecated
    public static void toReserveSuccess(Activity activity) {
        Intent intent = new Intent(activity.getApplicationContext(), ReserveSuccessActivity.class);
        activity.startActivity(intent);
    }


    /**
     * 预约成功
     *
     * @param activity
     * @param reservationSuccessInfo
     */
    public static void toReserveSuccess(Activity activity, ReservationSuccessInfo reservationSuccessInfo) {
        Intent intent = new Intent(activity.getApplicationContext(), ReserveSuccessActivity.class);
//        intent.putExtra("reservationSuccessInfo", reservationSuccessInfo);
        Bundle bundle = new Bundle();
        bundle.putParcelable("reservationSuccessInfo", reservationSuccessInfo);
        intent.putExtra(KEY_DATA, bundle);
        activity.startActivity(intent);
    }


    /**
     * 优惠套餐列表
     *
     * @param context
     */
    public static void toPackageList(Context context, String shopID, OtherMaintenancePackageListAdapter.Type type, MyAuto auto) {
        context.startActivity(new Intent(context, PackageListActivity.class).putExtra(PackageListActivity.SHOP_ID, shopID).putExtra(PackageListActivity.AUTO, auto).putExtra(PackageListActivity.TYPE, type));
    }




    /**
     * 服务顾问
     *
     * @param activity
     * @param shopID
     * @param requestCode
     */
    public static void toServiceAdviserForResult(Activity activity, String shopID, int requestCode) {
        Intent intent = new Intent(activity, ServiceAdviserActivity.class);
//        intent.putExtra("shopID", shopID);
        Bundle bundle = new Bundle();
        bundle.putString("shopID", shopID);
        intent.putExtra(KEY_DATA, bundle);
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 维修技师
     *
     * @param activity
     * @param shopID
     * @param requestCode
     */
    public static void toServiceMechanicForResult(Activity activity, String shopID, int requestCode) {
        Intent intent = new Intent(activity, ServiceMechanicActivity.class);
//        intent.putExtra("shopID", shopID);
        Bundle bundle = new Bundle();
        bundle.putString("shopID", shopID);
        intent.putExtra(KEY_DATA, bundle);
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 我的优惠券
     *
     * @param activity
     * @param couponCombination
     * @param requestCode
     */
    public static void toMyCouponForResult(Activity activity, ArrayList< CouponCombination > couponCombination, int requestCode) {
        Intent intent = new Intent(activity, MyCouponActivity.class);
        intent.putExtra("couponCombinations", couponCombination);
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 跳转到救援导航界面
     **/
    public static void toAMapNvai(Context context, int ot, PickupPointT shopLocation) {
        //因代码调整暂时注释
        Intent intent = new Intent();
        intent.setClassName(context, "com.hxqc.mall.thirdshop.activity.ThirdShopAMapActivity");
        intent.putExtra(SHOP_AMAP, shopLocation);
        intent.putExtra(MAP_OPERATOR, ot);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 跳转到店铺报价的主页
     *
     * @param context
     */
    public static void toShopQuote(Context context) {
        Intent intent = new Intent(context, ShopQuoteActivity.class);
        context.startActivity(intent);
    }


    /**
     * 保养4s店流程
     *
     * @param context
     * @param newMaintenanceShop
     */
    public static void toFourSShopMaintanence(Context context, @Nullable NewMaintenanceShop newMaintenanceShop) {
        Intent intent = new Intent(context, FourSShopMaintenanceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("newMaintenanceShop", newMaintenanceShop);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }







    /**
     * 4s 快修店流程的 确认订单页面
     *
     * @param context
     */
    public static void toFourSAndQuickConfirmOrder(Context context, @Nullable NewMaintenanceShop newMaintenanceShop) {
        Intent intent = new Intent(context, FourSAndQuickConfirmOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("newMaintenanceShop", newMaintenanceShop);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 跳转到新的保养列表页面
     **/
    public static void toMaintenanceList(Context context, String brandID, String seriesID, String autoModelID, String myAutoID, String items, int type) {
        Intent intent = new Intent(context, MaintenanceShopListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(MaintenanceShopListActivity.BRANDID, brandID);
        bundle.putString(MaintenanceShopListActivity.AUTOMODELID, autoModelID);
        bundle.putString(MaintenanceShopListActivity.SERIESID, seriesID);
        bundle.putString(MaintenanceShopListActivity.MYAUTOID, myAutoID);
        bundle.putInt(MaintenanceShopListActivity.TYPE, type); // 0 为4S店，1为快修店
        bundle.putString(MaintenanceShopListActivity.ITEMS, items);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 跳转到地图选店页面
     **/
    public static void toMapShopList(Context context, String brandID, String seriesID, String autoModelID, String myAutoID, String items, int type, HashMap< String, String > hashMap) {
        Intent intent = new Intent(context, MaintenanceShopListOnMapActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(MaintenanceShopListOnMapActivity.BRANDID, brandID);
        bundle.putString(MaintenanceShopListOnMapActivity.AUTOMODELID, autoModelID);
        bundle.putString(MaintenanceShopListOnMapActivity.SERIESID, seriesID);
        bundle.putString(MaintenanceShopListOnMapActivity.MYAUTOID, myAutoID);
        bundle.putInt(MaintenanceShopListOnMapActivity.TYPE, type); // 0 为4S店，1为快修店
        bundle.putString(MaintenanceShopListOnMapActivity.ITEMS, items);
        bundle.putSerializable(MaintenanceShopListOnMapActivity.HASHMAP, hashMap);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 到店支付完成页面
     *
     * @param context flag 1 优惠支付  2 到店支付
     */
    public static void toShopFinishPay(Context context, String orderID, String flag) {
        Intent intent = new Intent(context, ShopFinishPayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("orderID", orderID);
        bundle.putString("flag", flag);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 跳保养列表中店铺对应的店铺信息页
     **/
    public static void toShopIntroduceInfo(Context context, String introduce) {
        Intent intent = new Intent(context, IntroduceShopActivity.class);
        intent.putExtra(IntroduceShopActivity.INTRODUCE, introduce);
        context.startActivity(intent);
    }


    /**
     * 我的优惠券列表
     *
     * @param context
     */
    public static void toMyCoupon(Context context, String myAutoID) {
        Intent intent = new Intent(context, MyCouponActivity.class);
        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList(MyCouponActivity.COUPONS, coupons);
        bundle.putString(MyCouponActivity.MY_AUTO_ID, myAutoID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }


    /**
     * 可用优惠券列表
     *
     * @param context
     * @param coupons 优惠券组合
     */
    public static void toAvailableCouponList(Activity context, ArrayList< Coupon > coupons, int requestCode) {
        Intent intent = new Intent(context, MyCouponActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(MyCouponActivity.COUPONS, coupons);
//        bundle.putString(MyCouponActivity.MY_AUTO_ID, myAutoID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivityForResult(intent, requestCode);
    }
}
