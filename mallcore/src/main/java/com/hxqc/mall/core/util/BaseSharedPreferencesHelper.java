package com.hxqc.mall.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.util.JSONUtils;

import java.util.LinkedList;

/**
 * Created by CPR011 on 2015/1/13.
 */
public class BaseSharedPreferencesHelper {
    public static final String SHARED_PREFERENCES_NAME = "com.hxqc.hxqcmall";

    protected static final String LOGIN_STATUS = "LoginStatus";


    protected static final String DELIVERY_ADDRESS = "DeliveryAddress";
    protected static final String HISTORY = "History";
    protected static final String ORDER_CHANGE = "OrderChange";
    protected static final String ORDER_CLEAR = "OrderClear";
    protected static final String COMMENT_CHANGE = "CommentChange";
    protected static final String TAB_CHANGE = "TabChange";
    protected static final int MAX_HISTORY_NUM = 10;
    protected SharedPreferences shared;

    protected static final String DOWNLOAD_ID = "downloadID";

    protected static final String SEARCH_KEYWORD = "searchKeyWord";
    protected static final String LATITUDE = "latitude";
    protected static final String LONGITUDE = "longitude";
    protected static final String CITY = "city";
    protected static final String PROVINCE = "province";
    protected static final String DISTRICT = "district";


    public SharedPreferences getSharedPreferences() {
        return shared;
    }


    public BaseSharedPreferencesHelper(Context context) {
        shared = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }


    /**
     * 获取评论改变状态
     */
    public boolean getCommentChange() {
        return shared.getBoolean(COMMENT_CHANGE, false);
    }


    /**
     * 设置评论改变状态
     *
     * @param isChanged
     */
    public void setCommentChange(boolean isChanged) {
        shared.edit().putBoolean(COMMENT_CHANGE, isChanged).apply();
    }


    /**
     * 获取订单改变状态
     */
    public boolean getOrderChange() {
        return shared.getBoolean(ORDER_CHANGE, false);
    }


    /**
     * 设置订单改变状态
     *
     * @param isChanged
     */
    public void setOrderChange(boolean isChanged) {
        shared.edit().putBoolean(ORDER_CHANGE, isChanged).apply();
    }


    public boolean getClearOrder() {
        return shared.getBoolean(ORDER_CLEAR, false);
    }

    /**
     * 清理订单列表
     * @param isClear
     */
    public void setClearOrder(boolean isClear) {
        shared.edit().putBoolean(ORDER_CLEAR, isClear).apply();
    }

    /**
     * 第三方订单状态改变，onResume中重新加载数据
     */
    public void setThirdOrderChange(boolean isChanged) {
        shared.edit().putBoolean(ORDER_CHANGE, isChanged).apply();
    }


    /**
     * 获取第三方发店铺订单数据状态
     */
    public boolean getThirdOrderChanged() {
        return shared.getBoolean(ORDER_CHANGE, false);
    }


    /**
     * 获取首页改变状态
     */
    public boolean getTabChange() {
        return shared.getBoolean(TAB_CHANGE, false);
    }


    /**
     * 设置首页改变状态
     *
     * @param isChanged
     */
    public void setTabChange(boolean isChanged) {
        shared.edit().putBoolean(TAB_CHANGE, isChanged).apply();
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
     *  设置当前定位所在区
     * @param district
     */
    public void setDistrict(String district) {
        shared.edit().putString(DISTRICT, district).apply();
    }


    public String getDistrict() {
        return shared.getString(DISTRICT, "汉阳区");
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
     * 保存当前位置经度坐标(百度坐标）
     *
     * @param latitude
     */
    public void setLatitudeBD(String latitude) {
        shared.edit().putString(BaseSharedPreferencesHelper.LATITUDE, latitude).apply();
    }


    /**
     * 获取当前经度坐标 (百度坐标）
     *
     * @return 30.5537770000, 114.2106580000
     */
    public String getLatitudeBD() {
        return shared.getString(LATITUDE, "30.5537770000");
    }


    /**
     * 保存当前位置经度坐标
     *
     * @param latitude
     */
    public void setLatitude(String latitude) {
        shared.edit().putString(BaseSharedPreferencesHelper.LATITUDE, latitude).apply();
    }


    /**
     * 获取当前经度坐标
     *
     * @return 30.5478282963, 114.2043620603
     */
    public String getLatitude() {
        return shared.getString(LATITUDE, "30.5478282963");
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
     * 获取当前维度坐标
     *
     * @return
     */
    public String getLongitude() {
        return shared.getString(LONGITUDE, "114.2043620603");
    }


    /**
     * 保存当前位置维度坐标(百度坐标）
     *
     * @param longitude
     */
    public void setLongitudeBD(String longitude) {
        shared.edit().putString(LONGITUDE, longitude).apply();
    }


    /**
     * 获取当前维度坐标(百度坐标）
     *
     * @return
     */
    public String getLongitudeBD() {
        return shared.getString(LONGITUDE, "114.2106580000");
    }


    /**
     * @return 返回最近查询城市的list, 第一个为最新, 最多6个
     */
    public LinkedList< String > getHistoryCity() {
        String historyCityList = shared.getString("historyCityList", null);
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
        LinkedList< String > historyCityList = getHistoryCity();
        historyCityList.remove(city);
        historyCityList.addFirst(city);
        while (historyCityList.size() > 6) {
            historyCityList.removeLast();
        }
        String s = JSONUtils.toJson(historyCityList, new TypeToken< LinkedList< String > >() {
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
     * 是否重新下载数据
     *
     * @return
     */
    public boolean getLoadPosition() {
        return shared.getBoolean("LoadPosition", false);
    }


    /** 设置下载标识符 **/
    public void setLoadPosition(boolean isDefault) {
        shared.edit().putBoolean("LoadPosition", isDefault).apply();
    }


    /** 设置是否存在特价车地区分站数据标识符 **/
    public void setSpecialCarAreaHistoryFlag(boolean flag) {
        shared.edit().putBoolean("SpecialCarHasHistoryDataFlag", flag).apply();
    }


    /** 获取是否存在特价车地区分站数据标识符 **/
    public boolean getSpecialCarAreaHistoryFlag() {
        return shared.getBoolean("SpecialCarHasHistoryDataFlag", false);
    }


    /** 设置特价车历史分站数据 **/
    public void setSpecialCarHistoryData(String data) {
        shared.edit().putString("SpecialCarHasHistoryData", data).apply();
    }


    /** 获取特价车历史分站数据 **/
    public String getSpecialCarHistoryData() {
        return shared.getString("SpecialCarHasHistoryData", "");
    }


    public void setSpecialCarAreaHistoryPinYing(String pinYing) {
        shared.edit().putString("SpecialCarHasHistoryDataPY", pinYing).apply();
    }


    public String getSpecialCarAreaHistoryPinYing() {
        return shared.getString("SpecialCarHasHistoryDataPY", "wuhan");
    }
}
