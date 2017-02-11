package com.hxqc.mall.usedcar.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.activity.AutoValuationActivity;
import com.hxqc.mall.usedcar.activity.BuyCarActivity;
import com.hxqc.mall.usedcar.activity.ExchangeActivity;
import com.hxqc.mall.usedcar.activity.ExchangeEntranceActivity;
import com.hxqc.mall.usedcar.activity.ImageActivity;
import com.hxqc.mall.usedcar.activity.InstalmentActivity;
import com.hxqc.mall.usedcar.activity.NewSellCarActivity;
import com.hxqc.mall.usedcar.activity.PersonalCarDetailActivity;
import com.hxqc.mall.usedcar.activity.PlatformCarDetailActivity;
import com.hxqc.mall.usedcar.activity.QAActivity;
import com.hxqc.mall.usedcar.activity.QAStandardActivity;
import com.hxqc.mall.usedcar.activity.ReadmeActivity;
import com.hxqc.mall.usedcar.activity.ReportActivity;
import com.hxqc.mall.usedcar.activity.SellCarActivity;
import com.hxqc.mall.usedcar.activity.SellerInfoActivity;
import com.hxqc.mall.usedcar.activity.ValuationResultActivity;
import com.hxqc.mall.usedcar.model.Image;
import com.hxqc.mall.usedcar.model.Instalment;
import com.hxqc.mall.usedcar.model.QA;
import com.hxqc.mall.usedcar.model.SellCarDetailModel;
import com.hxqc.mall.usedcar.model.Table;
import com.hxqc.mall.usedcar.model.ValuationArgument;

import java.util.ArrayList;

/**
 * Author : 钟学东
 * Date : 2015-08-04
 * FIXME
 * Todo 界面跳转
 */
public class UsedCarActivitySwitcher extends ActivitySwitchBase{
    public static final String VALUATION_ARGUMENT = "valuation_argument";
    public static final String CURRENT_CITY = "current_city";
    public static final String FROM = "from";
    public static final String SELECTED_TIP_MAP = "selected_tip_map";
    public static final String MOBILE = "mobile";
    public static final String SELLER_PHOTO = "seller_photo";
    public static final String SELLER_NAME = "seller_name";
    public static final String README = "readme";
    public static final String TABLE = "table";
    public static final String QA = "qa";

    /**
     * 从首页跳二手车
     * @param context
     */
    public static void toBuyCarFromHome(Context context) {
        if (TextUtils.isEmpty(new UsedCarSPHelper(context).getCity())) {
            new UsedCarSPHelper(context).saveCity("武汉市");
        }
        toBuyCar(context, "");
    }

    /**
     * 买车
     * @param context
     * @param keyword 搜索关键字
     */
    public static void toBuyCar(Context context, String keyword) {
        new UsedCarSPHelper(context).saveKeyword(keyword);
        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword);
        Intent intent = new Intent(context, BuyCarActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 个人车辆详情页
     * @param context
     * @param car_source_no 车源号
     */
    public static void toPersonalCarDetail(Context context, String car_source_no) {
        Bundle bundle = new Bundle();
        bundle.putString(PersonalCarDetailActivity.CAR_SOURCE_NO, car_source_no);
        context.startActivity(new Intent(context, PersonalCarDetailActivity.class).putExtra(KEY_DATA, bundle));
    }

    /**
     * 车主自述
     * @param readme 车源号
     */
    public static void toReadme(Activity activity, String readme) {
        Intent intent=new Intent(activity, ReadmeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(README, readme);
        intent.putExtra(KEY_DATA, bundle);
        activity.startActivityForResult(intent, SellCarActivity.README);
    }

    /**
     * 质保标准
     */
    public static void toQAStandard(Context context, ArrayList<Table> tables, ArrayList<QA> QAs) {
        Intent intent=new Intent(context, QAStandardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(TABLE,tables);
        bundle.putParcelableArrayList(QA, QAs);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 平台车辆详情页
     * @param context
     * @param car_source_no 车源号
     */
    public static void toPlatformCarDetail(Context context, String car_source_no) {
        Bundle bundle = new Bundle();
        bundle.putString(PlatformCarDetailActivity.CAR_SOURCE_NO, car_source_no);
        context.startActivity(new Intent(context, PlatformCarDetailActivity.class).putExtra(KEY_DATA, bundle));
    }

    /**
     * 分期购车
     * @param activity
     * @param requestCode 请求码
     * @param instalment 分期数据
     * @param instalment_id 已选分期id
     * @param url H5
     */
    public static void toInstalmentForResult(Activity activity, int requestCode, ArrayList<Instalment> instalment, String instalment_id, String url) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(InstalmentActivity.INSTALMENT, instalment);
        bundle.putString(InstalmentActivity.INSTALMENT_ID, instalment_id);
        bundle.putString(InstalmentActivity.URL, url);
        activity.startActivityForResult(new Intent(activity, InstalmentActivity.class).putExtra(KEY_DATA, bundle), requestCode);
    }

    /**
     * 恒信质保
     * @param activity
     * @param requestCode 请求码
     * @param qa 质保数据
     * @param table 质保 查看详情数据
     * @param url H5
     */
    public static void toQAForResult(Activity activity, int requestCode, ArrayList<QA> qa, ArrayList<Table> table, String qa_id, String url) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(QAActivity.QA, qa);
        bundle.putParcelableArrayList(QAActivity.TABLE, table);
        bundle.putString(QAActivity.QA_ID, qa_id);
        bundle.putString(QAActivity.URL, url);
        activity.startActivityForResult(new Intent(activity, QAActivity.class).putExtra(KEY_DATA, bundle), requestCode);
    }

    /**
     * 车辆详情页图集
     * @param context
     * @param image 图集数据
     */
    public static void toImage(Context context, ArrayList<Image> image) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ImageActivity.IMAGE, image);
        context.startActivity(new Intent(context, ImageActivity.class).putExtra(KEY_DATA, bundle));
    }

    /**
     * 我的卖车信息
     * @param context
     */
    public static void toSellCarInfo(Context context) {
        Intent intent = new Intent();
        intent.setAction(context.getResources().getString(R.string.action_sell_car_info));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 从首页跳二手车（卖车）
     *
     * @param context
     */
    public static void toSellCarFromHome(Context context) {
        if (TextUtils.isEmpty(new UsedCarSPHelper(context).getCity())) {
            new UsedCarSPHelper(context).saveCity("武汉市");
        }
        Intent intent = new Intent(context, NewSellCarActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 选城市
     *
     * @param context
     */
    public static void toChooseCity(Context context, String currentCity) {
        Intent intent = new Intent();
        intent.setAction(context.getResources().getString(R.string.action_choose_city));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_CITY, currentCity);
        intent.putExtra(KEY_DATA,bundle);
        context.startActivity(intent);
    }

    /**
     * 选城市
     */
    public static void toChooseCityForResult(Fragment fragment, String currentCity) {
        Intent intent = new Intent();
        intent.setAction(fragment.getActivity().getResources().getString(R.string.action_choose_city));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_CITY, currentCity);
        intent.putExtra(KEY_DATA, bundle);
        fragment.startActivityForResult(intent, 1);
    }

    /**
     * 选城市
     */
    public static void toChooseCityForResult(Activity activity, String currentCity) {
        Intent intent = new Intent();
        intent.setAction(activity.getResources().getString(R.string.action_choose_city));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putString(CURRENT_CITY, currentCity);
        intent.putExtra(KEY_DATA, bundle);
        activity.startActivityForResult(intent, 1);
    }

    /**
     * 我要卖车
     *
     * @param context
     */
    public static void toSellCar(Context context) {
        Intent intent = new Intent(context, SellCarActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 我要卖车
     *
     * @param context
     */
    public static void toSellCar(Context context, ValuationArgument valuationArgument) {
        Intent intent = new Intent(context, SellCarActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(VALUATION_ARGUMENT, valuationArgument);
        intent.putExtra(KEY_DATA, bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 我要搜索
     *
     * @param context
     */
    public static void toSearch(Context context) {
        Intent intent = new Intent();
        intent.setAction(context.getResources().getString(R.string.action_search));
        context.startActivity(intent);
    }

    /**
     * 平台帮卖
     *
     * @param context
     */
    public static void toPlatformSell(Context context) {
        Intent intent = new Intent();
        intent.setAction(context.getResources().getString(R.string.action_platform_sell));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 举报
     *
     * @param context
     */
    public static void toReport(Context context, String carNo) {
        Intent mIntent = new Intent(context, ReportActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("carNo", carNo);
        mIntent.putExtra(KEY_DATA, bundle);
        context.startActivity(mIntent);
    }
    /**
     * 举报
     *
     * @param context
     */
    public static void toSellCarFromSellCarDetail(Context context, SellCarDetailModel sellCarDetailModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("sellCarDetailModel", sellCarDetailModel);
        Intent intent = new Intent(context, SellCarActivity.class);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }
    /**
     * 卖家信息
     *
     * @param context
     * @param mobile
     * @param sellerPhoto
     * @param sellerName
     */
    public static void toSellInfo(Context context, String mobile, String sellerName, String sellerPhoto) {
        Intent intent = new Intent(context, SellerInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(MOBILE, mobile);
        bundle.putString(SELLER_PHOTO, sellerPhoto);
        bundle.putString(SELLER_NAME, sellerName);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    /**
     * 跳转到卖车信息详情页面
     *
     * @param context
     * @param phoneNumber
     * @param carSourceNo
     */
    public static void toSellCarDetail(Context context, String phoneNumber, String carSourceNo) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("phoneNumber", phoneNumber);
        bundle.putString("carSourceNo", carSourceNo);
        intent.putExtra(KEY_DATA, bundle);
        intent.setAction(context.getResources().getString(R.string.action_sell_car_detail));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 估价结果
     *
     * @param context
     */
    public static void toValuationResult(Context context, ValuationArgument valuationArgument) {
        Intent intent = new Intent(context, ValuationResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(VALUATION_ARGUMENT, valuationArgument);
        intent.putExtra(KEY_DATA, bundle);
        context.startActivity(intent);
    }

    public static void toValuation(Context context) {
        Intent intent = new Intent(context, AutoValuationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static void toExchange(Context context) {
        Intent intent = new Intent(context, ExchangeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public static void toExchangeEntrance(Context context) {
        Intent intent = new Intent(context, ExchangeEntranceActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    /**
     * 从估价到卖车
     *
     * @param context
     */
    public static void toNewSellCar(Context context, ValuationArgument valuationArgument) {
        Intent intent = new Intent(context, NewSellCarActivity.class);
        Bundle bundle = new Bundle();
//        bundle.putInt(FROM, from);
        bundle.putParcelable(VALUATION_ARGUMENT, valuationArgument);
        intent.putExtra(KEY_DATA, bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 卖车
     *
     * @param context
     */
    public static void toNewSellCar(Context context) {
        Intent intent = new Intent(context, NewSellCarActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
