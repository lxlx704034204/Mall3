package com.hxqc.mall.core.util;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
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

    public SharedPreferencesHelper(Context context) {
        super(context);
    }

    /**
     * 历史记录
     *
     * @return
     */
    public String getSearchHistory() {
        return shared.getString(SEARCH_KEYWORD, "[]");
    }

    /**
     * 保存搜索历史关键字
     */
    public void saveSearchHistory(String keyword) {
        shared.edit().putString(SEARCH_KEYWORD, keyword).apply();
    }

    /**
     * 清除搜索历史关键字
     */
    public void clearSearchHistoryKeyWord() {
        shared.edit().remove(SEARCH_KEYWORD).apply();
    }

    /**
     * 获取当前城市
     */
    public String getCity() {
        return shared.getString(CITY, "");
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
        return shared.getString(PROVINCE, "");
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
        String historyCityList = shared.getString("app_historyCityList", null);
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
        shared.edit().putString("app_historyCityList", s).apply();
    }

    /**
     * 是否弹出更换地址弹出框
     * @return
     */
    public boolean getPositionTranslate() {
        return shared.getBoolean("PositionTranslate", false);
    }

    public void setPositionTranslate(boolean isDefault) {
        shared.edit().putBoolean("PositionTranslate", isDefault).apply();
    }

    /**
     * 是否重新下载数据
     * @return
     */
    public boolean getLoadPosition() {
        return shared.getBoolean("LoadPosition", false);
    }

    public void setLoadPosition(boolean isDefault) {
        shared.edit().putBoolean("LoadPosition", isDefault).apply();
    }
}
