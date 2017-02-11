package com.hxqc.mall.thirdshop.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.thirdshop.model.AreaCategory;
import com.hxqc.mall.thirdshop.model.AreaCategoryCity;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * Function: 站点历史数据管理Helper
 *
 * @author 袁秉勇
 * @since 2016年06月27日
 */
public class AreaSiteUtil {
    private final static String TAG = AreaSiteUtil.class.getSimpleName();
    private Context mContext;

    public static String initGroupID;

    public static String initGroupName;

    public static String initProvinceName;

    private static AreaSiteUtil instance;

    public SharedPreferencesHelper sharedPreferencesHelper;

    private ArrayList< AreaCategory > areaCategories;


    public AreaSiteUtil(Context context) {
        sharedPreferencesHelper = new SharedPreferencesHelper(context);
        initGroupName = sharedPreferencesHelper.getDefaultSiteData().siteGroupName;
        initGroupID = sharedPreferencesHelper.getDefaultSiteData().siteGroupID;
        initProvinceName = sharedPreferencesHelper.getDefaultSiteData().siteProvinceName;
        DebugLog.e("==========TAG============", sharedPreferencesHelper.getDefaultSiteData().toString());
        areaCategories = JSONUtils.fromJson(sharedPreferencesHelper.getSpecialCarHistoryData(), new TypeToken< ArrayList< AreaCategory > >() {
        });
    }


    public static AreaSiteUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (AreaSiteUtil.class) {
                if (instance == null) {
                    instance = new AreaSiteUtil(context);
                }
            }
        }
        return instance;
    }


    /**
     * 判断city是否在站点列表中
     *
     * @param city
     * @return
     */
    public boolean adjustCity(String city) {
        if (areaCategories == null || areaCategories.size() <= 0) return false;
        for (int i = 0; i < areaCategories.size(); i++) {
            ArrayList< AreaCategoryCity > areaCategoryCities = areaCategories.get(i).areaGroup;
            if (areaCategories == null || areaCategoryCities.size() <= 0) break;
            for (int j = 0; j < areaCategoryCities.size(); j++) {
                if (areaCategoryCities.get(j).city.equals(city)) return true;
            }
        }
        return false;
    }


    /**
     * 根据传入城市获得当前城市对应站点的名称
     *
     * @param city
     * @return
     */
    public String getCityGroup(String city) {
        if (TextUtils.isEmpty(city) || areaCategories == null || areaCategories.size() <= 0) {
            return initGroupName;
        }
        for (int i = 0; i < areaCategories.size(); i++) {
            ArrayList< AreaCategoryCity > areaCategoryCities = areaCategories.get(i).areaGroup;
            if (areaCategoryCities == null || areaCategoryCities.size() <= 0) break;
            for (int j = 0; j < areaCategoryCities.size(); j++) {
                if (areaCategoryCities.get(j).city.equals(city)) return areaCategories.get(i).areaName;
            }
        }
        return initGroupName;
    }


    public String getPinYin(String siteName) {
        if (TextUtils.isEmpty(siteName) || areaCategories == null || areaCategories.size() <= 0) {
            return "wuhan";
        }

        for (int i = 0; i < areaCategories.size(); i++) {
            if (siteName.equals(areaCategories.get(i).areaName)) return areaCategories.get(i).pinyin;
        }
        return "wuhan";
    }


    /**
     * 获取当前选择站点城市时对应的省名称
     *
     * @param city
     * @return
     */
    public String getCityProvince(String city) {
        if (areaCategories == null || areaCategories.size() <= 0) return initProvinceName;
        for (int i = 0; i < areaCategories.size(); i++) {
            ArrayList< AreaCategoryCity > areaCategoryCities = areaCategories.get(i).areaGroup;
            if (areaCategoryCities == null || areaCategoryCities.size() <= 0) break;
            for (int j = 0; j < areaCategoryCities.size(); j++) {
                if (areaCategoryCities.get(j).city.equals(city)) return areaCategoryCities.get(j).province;
            }
        }
        return initProvinceName;
    }


    /**
     * 根据传入的城市名称，返回对应站点的ID值，如果不存在站点则返回默认站点ID
     *
     * @param cityName
     * @return
     */
    public String getCityGroupID(String cityName) {
        if (areaCategories == null || areaCategories.size() <= 0) {
            areaCategories = JSONUtils.fromJson(sharedPreferencesHelper.getSpecialCarHistoryData(), new TypeToken< ArrayList< AreaCategory > >() {
            });
            if (areaCategories == null || areaCategories.size() <= 0) return initGroupID;
        }
        for (int i = 0; i < areaCategories.size(); i++) {
            if (areaCategories.get(i).areaName.equals(cityName)) {
                return areaCategories.get(i).siteID;
            }
        }
        return initGroupID;
    }


    /**
     * 获取站点id，如果存在历史站点就返回历史站点对应的ID，否则就返回“武汉”站点对应的ID值
     *
     * @return
     */
    public String getSiteID() {
//        if (TextUtils.isEmpty(sharedPreferencesHelper.getHistoryCityForSpecialCar().getFirst())) {
        if (areaCategories == null || areaCategories.size() <= 0) {
            return initGroupID;
        }

        String condition = initGroupName;

        if (!sharedPreferencesHelper.getHistoryCityForSpecialCar().isEmpty() && !TextUtils.isEmpty(sharedPreferencesHelper.getHistoryCityForSpecialCar().getFirst())) {
            condition = sharedPreferencesHelper.getHistoryCityForSpecialCar().getFirst();
        }

        for (int i = 0; i < areaCategories.size(); i++) {
            if (areaCategories.get(i).areaName.indexOf(condition) != -1) {
                return areaCategories.get(i).siteID;
            }
        }
//        }
//        else {
//            if (areaCategories == null || areaCategories.size() <= 0) {
//                return initGroupID;
//            }
//            for (int i = 0; i < areaCategories.size(); i++) {
//                if (areaCategories.get(i).areaName.equals(sharedPreferencesHelper.getHistoryCityForSpecialCar().getFirst())) {
//                    return areaCategories.get(i).siteID;
//                }
//            }
//        }
        return initGroupID;
    }


    /**
     * 获取历史选择站点名称
     *
     * @return
     */
    public String getSiteName() {
        if (!sharedPreferencesHelper.getHistoryCityForSpecialCar().isEmpty()) {
            return sharedPreferencesHelper.getHistoryCityForSpecialCar().getFirst();
        }
        return initGroupName;
    }


    /**
     * 获取默认的“武汉”站的名称
     *
     * @return
     */
    public String getWHSiteName() {
        if (areaCategories == null || areaCategories.size() <= 0) return initGroupName;
        for (int i = 0; i < areaCategories.size(); i++) {
            if (areaCategories.get(i).areaName.contains("武汉")) return areaCategories.get(i).areaName;
        }
        return initGroupName;
    }


    public void destroy() {
        if (instance != null) instance = null;
    }
}
