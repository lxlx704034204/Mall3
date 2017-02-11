package com.hxqc.aroundservice.util;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.util.JSONUtils;

import java.util.LinkedList;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年04月13日
 */
public class AroundServiceSharePreferencesHelper extends BaseSharedPreferencesHelper {
    private final static String TAG = AroundServiceSharePreferencesHelper.class.getSimpleName();
    private Context mContext;


    public AroundServiceSharePreferencesHelper(Context context) {
        super(context);
    }



    /**
     * @return 返回最近查询城市的list, 第一个为最新, 最多6个
     */
    public LinkedList<String> getHistoryCity() {
        String historyCityList = shared.getString("aroundServices_historyCityList", null);
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
        shared.edit().putString("aroundServices_historyCityList", s).apply();
    }
}
