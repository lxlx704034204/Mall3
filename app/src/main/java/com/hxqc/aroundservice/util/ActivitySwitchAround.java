package com.hxqc.aroundservice.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.hxqc.aroundservice.activity.AroundServiceAMapActivity;
import com.hxqc.aroundservice.activity.AroundServiceListOnMapActivity;
import com.hxqc.aroundservice.activity.CancelOrderDetailActivity;
import com.hxqc.aroundservice.activity.CarWashShopDetailActivity;
import com.hxqc.aroundservice.activity.CircumPayFinishActivity;
import com.hxqc.aroundservice.activity.DriversLicenseChangeActivity;
import com.hxqc.aroundservice.activity.IllegalConfiscateActivity;
import com.hxqc.aroundservice.activity.IllegalDetailActivity;
import com.hxqc.aroundservice.activity.IllegalProcessingActivity;
import com.hxqc.aroundservice.activity.IllegalProcessingSuccessActivity;
import com.hxqc.aroundservice.activity.IllegalQueryResultActivity;
import com.hxqc.aroundservice.activity.MyIllegalOrderActivity;
import com.hxqc.aroundservice.activity.OrderDetailActivity;
import com.hxqc.aroundservice.activity.PositionActivity;
import com.hxqc.aroundservice.activity.VehicleInspectionActivity;
import com.hxqc.aroundservice.config.OrderDetailContants;
import com.hxqc.aroundservice.model.CityList;
import com.hxqc.aroundservice.model.IllegalOrderDetail;
import com.hxqc.aroundservice.model.IllegalQueryRequestData;
import com.hxqc.aroundservice.model.IllegalQueryResult;
import com.hxqc.aroundservice.model.IllegalQueryResultInfo;
import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.util.ActivitySwitchBase;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2016-04-07
 * FIXME
 * Todo
 */
public class ActivitySwitchAround extends ActivitySwitchBase {

    private static ImageModel imImageModel;

    /**
     * 跳转到周边地图
     *
     * @param otype   是否显示的 项目
     * @param context 上下文
     */
    public static void toAroundMap(int otype, Context context) {
        Intent intent = new Intent(context, AroundServiceAMapActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(AroundServiceAMapActivity.AROUND_MAP_TAG, otype);
        context.startActivity(intent);
    }

    /**
     * 跳转违章查询页面
     *
     * @param context
     */
    public static void toIllegalQueryActivity(Context context) {
        context.startActivity(new Intent(context, IllegalConfiscateActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * 跳转到单个违章详情页面
     *
     * @param context
     * @param illegalQueryResultInfo
     */
    public static void toIllegalDetailActivity(Context context, IllegalQueryResultInfo illegalQueryResultInfo, String plateNumber) {
        Intent intent = new Intent(context, IllegalDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("illegalQueryResultInfo", illegalQueryResultInfo);
        bundle.putString("plateNumber", plateNumber);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 跳转到单个违章详情页面
     *
     * @param context
     * @param illegalQueryResultInfo
     */
    public static void toIllegalDetailActivity(Context context, IllegalQueryResultInfo illegalQueryResultInfo, String plateNumber,boolean isHistory) {
        Intent intent = new Intent(context, IllegalDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("illegalQueryResultInfo", illegalQueryResultInfo);
        bundle.putString("plateNumber", plateNumber);
        bundle.putBoolean("isHistory",isHistory);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 车辆年审
     *
     * @param context
     */
    public static void toVehicleInspectionActivity(Context context) {
        context.startActivity(new Intent(context, VehicleInspectionActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * 驾驶证换证
     *
     * @param context
     */
    public static void toDriversLicenseChangeActivity(Context context) {
        context.startActivity(new Intent(context, DriversLicenseChangeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * 跳转违章查询结果页面
     *
     * @param context
     * @param illegalQueryResult
     */
    public static void toIllegalQueryResultActivity(Context context, int flagActivity, IllegalQueryResult illegalQueryResult) {
        Intent intent = new Intent(context, IllegalQueryResultActivity.class);
//        intent.putExtra("provinceAndCity", provinceAndCity);
        Bundle bundle = new Bundle();
        bundle.putInt("flagActivity", flagActivity);
        bundle.putParcelable("illegalQueryResult", illegalQueryResult);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 跳转违章查询结果页面
     *
     * @param context
     * @param flagActivity
     * @param illegalQueryResult
     * @param illegalQueryRequestData
     */
    public static void toIllegalQueryResultActivity(Context context, int flagActivity, IllegalQueryResult illegalQueryResult, IllegalQueryRequestData illegalQueryRequestData) {
        Intent intent = new Intent(context, IllegalQueryResultActivity.class);
//        intent.putExtra("provinceAndCity", provinceAndCity);
        Bundle bundle = new Bundle();
        bundle.putInt("flagActivity", flagActivity);
        bundle.putParcelable("illegalQueryResult", illegalQueryResult);
        bundle.putParcelable("illegalQueryRequestData", illegalQueryRequestData);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 详情跳转违章查询结果页面
     *
     * @param context
     * @param flagActivity
     * @param illegalOrderDetail
     */
    public static void toIllegalQueryResultActivity(Context context, int flagActivity, IllegalOrderDetail illegalOrderDetail) {
        Intent intent = new Intent(context, IllegalQueryResultActivity.class);
//        intent.putExtra("illegalOrderDetail", illegalOrderDetail);
        Bundle bundle = new Bundle();
        bundle.putInt("flagActivity", flagActivity);
        bundle.putParcelable("illegalOrderDetail", illegalOrderDetail);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 定位城市选择
     **/
    public static void toPositionActivity(Context context, ArrayList<CityList> cityLists, int requestCode, String position) {
        Intent intent = new Intent(context, PositionActivity.class);
        intent.putExtra("position", position);
        intent.putParcelableArrayListExtra(PositionActivity.DATA, cityLists);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    /**
     * 违章处理
     *
     * @param context
     */
    public static void toQueryProcessingActivity(Context context, String plateNumber, String choseWZID) {
        Intent intent = new Intent(context, IllegalProcessingActivity.class);
//        intent.putExtra("plateNumber", plateNumber);
//        intent.putExtra("choseWZID", choseWZID);
        Bundle bundle = new Bundle();
        bundle.putString("plateNumber", plateNumber);
        bundle.putString("choseWZID", choseWZID);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 我的违章订单
     *
     * @param context
     */
    public static void toMyIllegalOrderActivity(Context context) {
        context.startActivity(new Intent(context, MyIllegalOrderActivity.class));
    }

    /**
     * 违章订单提交成功
     *
     * @param context
     */
    public static void toIllegalProcessingSuccessActivity(Context context) {
        context.startActivity(new Intent(context, IllegalProcessingSuccessActivity.class));
    }


    /**
     * 订单详情
     *
     * @param context
     * @param orderID
     * @param flagFragment
     */
    public static void toOrderDetailActivity(Context context, String orderID, String flagFragment) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
//        intent.putExtra("orderID", orderID);
//        intent.putExtra("flagFragment", flagFragment);
        Bundle bundle = new Bundle();
        bundle.putString("orderID", orderID);
        bundle.putString("flagFragment", flagFragment);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 周边支付完成页面
     *
     * @param context
     */
    public static void toPayFinishActivity(Context context) {
        context.startActivity(new Intent(context, CircumPayFinishActivity.class));
    }

    /**
     * 取消订单
     *
     * @param fragment
     * @param orderID
     * @param flagFragment
     */
    public static void toCancelOrderDetailActivity(Fragment fragment, String orderID, String flagFragment) {
        Intent intent = new Intent(fragment.getActivity(), CancelOrderDetailActivity.class);
//        intent.putExtra("orderID", orderID);
//        intent.putExtra("flagFragment", flagFragment);
        Bundle bundle = new Bundle();
        bundle.putString("orderID", orderID);
        bundle.putString("flagFragment", flagFragment);
        intent.putExtra(KEY_DATA, bundle);
        fragment.startActivityForResult(intent, OrderDetailContants.REQUEST_CANCEL);
    }

    /**
     * @param activity
     */
    public static void toOrderDetailActivity(Activity activity) {
        Intent intent = new Intent();
        activity.setResult(OrderDetailContants.CANDEL_SRCCESS, intent);
    }

    /**
     * 洗车店详情
     */
    public static void toWashCarDetail(Context context, String shopID) {
        Intent intent = new Intent(context, CarWashShopDetailActivity.class);
        intent.putExtra("wash_car_shop_id", shopID);
        context.startActivity(intent);
    }

    /**
     * 跳转大图
     *
     * @param context
     * @param v
     * @param filePath
     */
    public static void toViewLargePic(Context context, View v, String filePath) {
        if (imImageModel == null) {
            imImageModel = new ImageModel(filePath, "");
        } else {
            imImageModel.largeImage = filePath;
        }
        int location[] = new int[2];
        v.getLocationOnScreen(location);
        Bundle bundle = new Bundle();
        bundle.putInt("locationX", location[0]);
        bundle.putInt("locationY", location[1]);
        bundle.putInt("width", v.getWidth());
        bundle.putInt("height", v.getHeight());
        ActivitySwitchBase.toViewLagerPic(0, imImageModel, context, bundle);
    }

    /**
     * 跳转大图
     *
     * @param context
     * @param v
     * @param filePath
     */
    public static void toActivityLargePic(Context context, View v, String filePath) {
        if (imImageModel == null) {
            imImageModel = new ImageModel(filePath, "");
        } else {
            imImageModel.largeImage = filePath;
        }
        int location[] = new int[2];
        v.getLocationOnScreen(location);
        Bundle bundle = new Bundle();
        bundle.putInt("locationX", location[0]);
        bundle.putInt("locationY", location[1]);
        bundle.putInt("width", v.getWidth());
        bundle.putInt("height", v.getHeight());
        ActivitySwitchBase.toActivityLagerPic(0, imImageModel, context, bundle);
    }

    /**
     * 清除内存
     */
    public static void killDirty() {
        if (imImageModel != null) {
            imImageModel = null;
        }
    }


    /**
     * 打开周围服务地图列表页面
     **/
    public static void toAroundServiceMapList(Context context, int type) {
        Intent intent = new Intent(context, AroundServiceListOnMapActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(AroundServiceListOnMapActivity.TYPE, type);
        intent.putExtra(KEY_DATA, bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
