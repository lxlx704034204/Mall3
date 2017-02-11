package com.hxqc.mall.thirdshop.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.thirdshop.model.SiteBean;
import com.hxqc.util.JSONUtils;

import java.util.LinkedList;


/**
 * 说明:SharedPreferences工具
 * <p/>
 * author: 吕飞
 * since: 2015-03-11
 * Copyright:恒信汽车电子商务有限公司
 */
public class SharedPreferencesHelper extends BaseSharedPreferencesHelper {
    protected static final String SEARCH_KEYWORD = "searchKeyWord";
    protected static final String LATITUDE = "latitude";
    protected static final String LONGITUDE = "longitude";
    protected static final String CITY = "city";
    protected static final String PROVINCE = "province";
    public static final String TAB_CHANGE_4S = "tab_change_4s";

    public SharedPreferencesHelper(Context context) {
        super(context);
    }


    /**
     * 设置特价车默认站点信息
     **/
    public void prepareSite(String siteString) {
        if (getHistoryCityForSpecialCar().size() <= 0) {
            shared.edit().putString("historyCityList_for_special_car_def", siteString).apply();
        }
    }


    /**
     * 获取武汉站点默认的全部信息
     **/
    public SiteBean getDefaultSiteData() {
        String siteData = shared.getString("historyCityList_for_special_car_def", "{\"siteGroupName\":\"武汉\",\"siteGroupID\":\"1639797187704660\",\"siteAreaName\":\"武汉市\",\"siteProvinceName\":\"湖北省\"}");
        return JSONUtils.fromJson(siteData, SiteBean.class);
    }


    /**
     * 获取当前城市
     */
    public String getCity() {
        return shared.getString(CITY, "武汉市");
    }


    /**
     * 设置当前城市
     *
     * @param city
     */
    public void setCity(String city) {
        shared.edit().putString(CITY, city).apply();
    }


    /**
     * 获取当前省名称
     */
    public String getProvince() {
        return shared.getString(PROVINCE, "湖北省");
    }


    /**
     * 设置当前省名称
     *
     * @param province
     */
    public void setProvince(String province) {
        shared.edit().putString(PROVINCE, province).apply();
    }


    /**
     * 获取当前经度坐标
     *
     * @return
     */
    public String getLatitude() {
        return shared.getString(LATITUDE, "");
    }


    /**
     * 保存当前位置经度坐标
     *
     * @param latitude
     */
    public void setLatitude(String latitude) {
        shared.edit().putString(SharedPreferencesHelper.LATITUDE, latitude).apply();
    }


    /**
     * 获取当前维度坐标
     *
     * @return
     */
    public String getLongitude() {
        return shared.getString(LONGITUDE, "");
    }


    /**
     * 保存当前位置维度坐标
     *
     * @param longitude
     */
    public void setLongitude(String longitude) {
        shared.edit().putString(LONGITUDE, longitude).apply();
    }


    /**
     * @return 返回最近查询城市的list, 第一个为最新, 最多6个
     */
    public LinkedList<String> getHistoryCity() {
        String historyCityList = shared.getString("historyCityList", null);
        if (TextUtils.isEmpty(historyCityList)) {
            return new LinkedList<>();
        }
        return JSONUtils.fromJson(historyCityList, new TypeToken<LinkedList<String>>() {
        });
    }


    /**
     * 添加最近查询城市
     *
     * @param city
     */
    public void setHistoryCity(String city) {
        if (TextUtils.isEmpty(city)) {
            return;
        }
        LinkedList<String> historyCityList = getHistoryCity();
        historyCityList.remove(city);
        historyCityList.addFirst(city);
        while (historyCityList.size() > 6) {
            historyCityList.removeLast();
        }
        String s = JSONUtils.toJson(historyCityList, new TypeToken<LinkedList<String>>() {
        }.getType());
        shared.edit().putString("historyCityList", s).apply();
    }


    /**
     * 是否弹出更换地址弹出框
     *
     * @return
     */
    public boolean getPositionTranslate() {
        return shared.getBoolean("PositionTranslate", false);
    }


    public void setPositionTranslate(boolean isDefault) {
        shared.edit().putBoolean("PositionTranslate", isDefault).apply();
    }


    /**
     * 是否弹出更换站点弹出框
     *
     * @return
     */
    public boolean getPositionTranslateForSiteGroup() {
        return shared.getBoolean("PositionTranslateForSiteGroup", false);
    }


    public void setPositionTranslateForSiteGroup(boolean isDefault) {
        shared.edit().putBoolean("PositionTranslateForSiteGroup", isDefault).apply();
    }


    /**
     * 是否重新下载数据
     *
     * @return
     */
    public boolean getLoadPosition() {
        return shared.getBoolean("LoadPosition", false);
    }


    public void setLoadPosition(boolean isDefault) {
        shared.edit().putBoolean("LoadPosition", isDefault).apply();
    }


    /**
     * 设置当前城市所在省份
     *
     * @param province
     */
    public void setHistoryProvinceForSpecialCar(String province) {
        shared.edit().putString("historyProvince_for_special_car", province).apply();
    }


    public String getHistoryProvinceForSpecialCar(Context context) {
        return shared.getString("historyProvince_for_special_car", new BaseSharedPreferencesHelper(context).getProvince());
    }


    /**
     * 设置当前站点城市的名称
     *
     * @param city
     */
    public void setCityForSpecialCar(String city) {
        shared.edit().putString("historyCity_for_special_car", city).apply();
    }


    public String getCityForSpecialCar(Context context) {
        return shared.getString("historyCity_for_special_car", new BaseSharedPreferencesHelper(context).getCity());

    }


    public LinkedList<String> getHistoryCityForSpecialCar() {
        String historyCityList = shared.getString("historyCityList_for_special_car", null);
        if (TextUtils.isEmpty(historyCityList)) {
            return new LinkedList<>();
        }
        return JSONUtils.fromJson(historyCityList, new TypeToken<LinkedList<String>>() {
        });
    }


    /**
     * 设置历史站点城市
     *
     * @param city
     */
    public void setHistoryCityForSpecialCar(String city) {
        if (TextUtils.isEmpty(city)) {
            return;
        }
        LinkedList<String> historyCityList = getHistoryCityForSpecialCar();
        historyCityList.remove(city);
        historyCityList.addFirst(city);
        while (historyCityList.size() > 6) {
            historyCityList.removeLast();
        }
        String s = JSONUtils.toJson(historyCityList, new TypeToken<LinkedList<String>>() {
        }.getType());
        shared.edit().putString("historyCityList_for_special_car", s).apply();
    }


    public boolean getLoadPositionForSpecialCar() {
        return shared.getBoolean("LoadPosition_for_special_car", false);
    }


    public void setLoadPositionForSpecialCar(boolean isDefault) {
        shared.edit().putBoolean("LoadPosition_for_special_car", isDefault).apply();
    }

    /**
     * 获取首页改变状态
     */
    public boolean get4STabChange() {
        return shared.getBoolean(TAB_CHANGE_4S, false);
    }

    /**
     * 设置首页改变状态
     *
     * @param isChanged
     */
    public void set4STabChange(boolean isChanged) {
        shared.edit().putBoolean(TAB_CHANGE_4S, isChanged).apply();
    }
}
