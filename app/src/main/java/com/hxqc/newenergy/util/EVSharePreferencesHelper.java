package com.hxqc.newenergy.util;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.db.areacacheutil_new.AreaCacheUtil;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.util.JSONUtils;

import java.util.LinkedList;


/**
 * 新能源SP
 */
public class EVSharePreferencesHelper extends BaseSharedPreferencesHelper {
    public final static String NEWENERGY_CITY = "chooseCity";


    public EVSharePreferencesHelper(Context context) {
        super(context);
    }


    /** 获取历史选择城市中的最近一个城市 **/
    public static String getLastHistoryCity(Context context) {
        LinkedList< String > cities = new EVSharePreferencesHelper(context).getHistoryCity();
        if (cities.size() > 0 && !TextUtils.isEmpty(cities.getFirst())) {
            AreaCacheUtil areaCacheUtil = AreaCacheUtil.getInstance(context);
            if (areaCacheUtil.checkExistCity(AreaCacheUtil.NEWENERGY, cities.getFirst())) {
                if (areaCacheUtil != null) areaCacheUtil.close();
                return cities.getFirst();
            }

        }
        return "武汉市";
    }

    /**
     * @return 返回最近查询城市的list, 第一个为最新, 最多6个
     */
    public LinkedList<String> getNewEnergyHistoryCity() {
        String historyCityList = shared.getString("newEnergy_historyCityList", null);
        if (TextUtils.isEmpty(historyCityList)) {
            return new LinkedList<>();
        }
        return JSONUtils.fromJson(historyCityList, new TypeToken< LinkedList< String > >() {
        });
    }

    /**
     * 添加最近查询城市
     *
     * @param city
     */
    public void setNewenergyHistoryCity(String city) {
        if (TextUtils.isEmpty(city)) {
            return;
        }
        LinkedList<String> historyCityList = getNewEnergyHistoryCity();
        historyCityList.remove(city);
        historyCityList.addFirst(city);
        while (historyCityList.size() > 6) {
            historyCityList.removeLast();
        }
        String s = JSONUtils.toJson(historyCityList, new TypeToken<LinkedList<String>>() {
        }.getType());
        shared.edit().putString("newEnergy_historyCityList", s).apply();
    }
}
