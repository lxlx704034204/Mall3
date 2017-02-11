package com.hxqc.aroundservice.control;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author:胡仲俊
 * Date: 2016 - 05 - 25
 * FIXME
 * Todo 违章本地缓存
 */
public class IllegalSharedPreferencesHelper {

    public static final String SHARED_PREFERENCES_NAME = "com.hxqc.hxqcmall.illegal";
    protected SharedPreferences shared;
    protected static final String CITY_CODE = "cityCode";
    protected static final String CITY_NAME = "cityName";
    protected static final String PROVINCE_NAME = "provinceName";
    protected static final String HPZL = "hpzl";
    protected static final String ENGINENO = "engineno";
    protected static final String CLASSNO = "classno";
    protected static final String HPHM = "hphm";

    public SharedPreferences getSharedPreferences() {
        return shared;
    }

    public IllegalSharedPreferencesHelper(Context context) {
        shared = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 获取城市
     *
     * @return
     */
    public String getCityCode() {
        return shared.getString(CITY_CODE, "");
    }

    /**
     * 设置城市
     *
     * @param cityCode
     */
    public void setCityCode(String cityCode) {
        shared.edit().putString(CITY_CODE, cityCode).apply();
    }

    /**
     * 设置城市
     *
     * @param cityName
     */
    public void setCityName(String cityName) {
        shared.edit().putString(CITY_NAME, cityName).apply();
    }

    /**
     * @return
     */
    public String getCityName() {
        return shared.getString(CITY_NAME, "");
    }

    /**
     * 设置城市
     *
     * @param cityName
     */
    public void setProvinceName(String cityName) {
        shared.edit().putString(PROVINCE_NAME, cityName).apply();
    }

    /**
     * @return
     */
    public String getProvinceName() {
        return shared.getString(PROVINCE_NAME, "");
    }


    /**
     * 获取号牌种类编号
     *
     * @return
     */
    public String getHPZL() {
        return shared.getString(HPZL, "");
    }


    /**
     * 设置号牌种类编号
     *
     * @param hpzl
     */
    public void setHPZL(String hpzl) {
        shared.edit().putString(HPZL, hpzl).apply();
    }

    /**
     * 获取发动机号
     *
     * @return
     */
    public String getEngineno() {
        return shared.getString(ENGINENO, "");
    }


    /**
     * 设置发动机号
     *
     * @param engineno
     */
    public void setEngineno(String engineno) {
        shared.edit().putString(ENGINENO, engineno).apply();
    }

    /**
     * 获取车架号
     *
     * @return
     */
    public String getClassno() {
        return shared.getString(CLASSNO, "");
    }


    /**
     * 设置车架号
     *
     * @param classno
     */
    public void setClassno(String classno) {
        shared.edit().putString(CLASSNO, classno).apply();
    }

    /**
     * 获取车牌号
     *
     * @return
     */
    public String getHPHM() {
        return shared.getString(HPHM, "");
    }


    /**
     * 设置车牌号
     *
     * @param hphm
     */
    public void setHPHM(String hphm) {
        shared.edit().putString(HPHM, hphm).apply();
    }

}
