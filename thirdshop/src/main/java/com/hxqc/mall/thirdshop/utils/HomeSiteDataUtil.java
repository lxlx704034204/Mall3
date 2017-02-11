package com.hxqc.mall.thirdshop.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.DialogResponseHandler;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.AreaCategory;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Function: 在首页获取分站数据，并筛选出当前定位城市所在的分站，若无相应的分站，提示切换到武汉站
 *
 * @author 袁秉勇
 * @since 2016年10月26日
 */
public class HomeSiteDataUtil {
    private final static String TAG = HomeSiteDataUtil.class.getSimpleName();
    private static Context mContext;
    private static SharedPreferencesHelper sharedPreferencesHelper;
    private static AreaSiteUtil areaSiteUtil;
    private static String areaGroup;


    /**
     * 4S店站点数据
     */
    public static void getSiteData(Context context) {
        mContext = context;
        sharedPreferencesHelper = new SharedPreferencesHelper(mContext);

        ThirdPartShopClient apiClient = new ThirdPartShopClient();
        apiClient.requestSiteData(new DialogResponseHandler(mContext, false) {
            @Override
            public void onSuccess(String response) {
                ArrayList< AreaCategory > areaCategories = JSONUtils.fromJson(response, new TypeToken< ArrayList< AreaCategory > >() {
                });

                if (areaCategories != null && areaCategories.size() > 0) {
                    sharedPreferencesHelper.setSpecialCarHistoryData(response);
                }
            }


            @Override
            public void onFinish() {
                super.onFinish();

                areaSiteUtil = AreaSiteUtil.getInstance(mContext);
                if (TextUtils.isEmpty(areaSiteUtil.sharedPreferencesHelper.getSpecialCarHistoryData())) return;
                initLocationData();
            }
        });
    }


    /**
     * 初始化当前坐标
     */
    protected static void initLocationData() {
        String historyCity;
        String city = sharedPreferencesHelper.getCity();
        DebugLog.e(TAG, city);
        LinkedList< String > historyCityList = sharedPreferencesHelper.getHistoryCityForSpecialCar();

        if (!historyCityList.isEmpty() && !TextUtils.isEmpty(historyCity = historyCityList.getFirst())) {
            DebugLog.e(TAG, "11111");
            if (!areaSiteUtil.getCityGroup(city).equals(historyCity)) {
                startSettingDialog(city);
            }
            return;
        }

        DebugLog.e(TAG, "22222");
        areaGroup = areaSiteUtil.getWHSiteName();
        sharedPreferencesHelper.setHistoryCityForSpecialCar(areaGroup);

        if (areaSiteUtil.getCityGroup(city).equals(areaSiteUtil.getWHSiteName())) {
            DebugLog.e(TAG, " 33333   " + areaSiteUtil.getCityGroup(city) + "  " + areaSiteUtil.getWHSiteName());
            sharedPreferencesHelper.setCityForSpecialCar(city);
            sharedPreferencesHelper.setHistoryProvinceForSpecialCar(sharedPreferencesHelper.getProvince());
        } else {
            DebugLog.e(TAG, "44444");
            startSettingDialog(city);
        }
    }


    /**
     * @param city 当前城市
     * @return
     */


    private static void startSettingDialog(final String city) {
        boolean showCancel = false;
        if (TextUtils.isEmpty(city)) {
            return;
        }
        sharedPreferencesHelper.setPositionTranslateForSiteGroup(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MaterialDialog);
        builder.setCancelable(false);
        builder.setTitle("提示");
        if (areaSiteUtil.adjustCity(city)) {
            showCancel = true;
            builder.setMessage("您当前城市属于【" + (appendWordToStr(areaSiteUtil.getCityGroup(city)) + "】" + ",是否需要进行数据切换?")); // 您当前城市是【%@】，需要切换吗？
        } else {
            builder.setMessage("您当前城市是【" + city + "】,不在分站列表中，已为您切换到【" + appendWordToStr(areaSiteUtil.getWHSiteName()) + "】"); // 您当前城市是【%@】，需要切换吗？
        }
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (areaSiteUtil.adjustCity(city)) {
                    areaGroup = areaSiteUtil.getCityGroup(city);
                    sharedPreferencesHelper.setCityForSpecialCar(city);
                    sharedPreferencesHelper.setHistoryProvinceForSpecialCar(areaSiteUtil.getCityProvince(city));
                } else {
                    areaGroup = areaSiteUtil.getWHSiteName();
                    sharedPreferencesHelper.setCityForSpecialCar(sharedPreferencesHelper.getDefaultSiteData().siteAreaName);
                    sharedPreferencesHelper.setHistoryProvinceForSpecialCar(areaSiteUtil.getCityProvince("湖北省"));
                }
                sharedPreferencesHelper.setSpecialCarAreaHistoryPinYing(areaSiteUtil.getPinYin(areaGroup));
                sharedPreferencesHelper.setHistoryCityForSpecialCar(areaGroup);
                areaSiteUtil.destroy();
            }
        });
        if (showCancel) {
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    areaSiteUtil.destroy();
                }
            });
        }
        builder.create().show();
    }


    public static String appendWordToStr(String str) {
        if (TextUtils.isEmpty(str)) return "";
        return str.endsWith("站") ? str : str + "站";
    }
}