package com.hxqc.mall.usedcar.utils;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.model.Keyword;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.usedcar.model.AreaModel;
import com.hxqc.mall.usedcar.model.BrandGroup;
import com.hxqc.mall.usedcar.model.Choose;
import com.hxqc.mall.usedcar.model.ReportReason;
import com.hxqc.mall.usedcar.model.SellCarChoose;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * 说明:
 *
 * @author: 吕飞
 * @since: 2015-08-05
 * Copyright:恒信汽车电子商务有限公司
 */
public class UsedCarSPHelper extends BaseSharedPreferencesHelper {
    public static final String CITY = "city";
    public static final String CITY_LIST = "city_list";
    public static final String USED_CAR_KEYWORD = "used_car_keyword";
    public static final String KEYWORD = "keyword";
    public static final String BRAND_LIST = "brand_list";
    public static final String VALUATION_BRAND_LIST = "valuation_brand_list";
    public static final String INTENT_BRAND_LIST = "intent_brand_list";
    public static final String CHOOSE = "choose";
    public static final String REASON = "report_reason";
    public static final int HISTORY_MAX = 15;
    public static final String HISTORY = "history";
    public static final String HOT_KEYWORDS = "hot_keywords";
    public static final String SELL_CAR_CHOOSE = "sell_car_choose";
    public static final String PRC = "PRC";
    public UsedCarSPHelper(Context context) {
        super(context);
    }

    /**
     * 保存卖车筛选
     */
    public void saveSellCarChoose(SellCarChoose sellCarChoose) {
        shared.edit().putString(SELL_CAR_CHOOSE, JSONUtils.toJson(sellCarChoose)).apply();
    }

    /**
     * 卖车筛选
     *
     * @return
     */
    public SellCarChoose getSellCarChoose() {
        String sellCarChoose = shared.getString(SELL_CAR_CHOOSE, "{}");
        return JSONUtils.fromJson(sellCarChoose, SellCarChoose.class);
    }

    /**
     * 移除卖车筛选
     */
    public void removeSellCarChoose() {
        shared.edit().remove(SELL_CAR_CHOOSE).apply();
    }

    /**
     * 保存热门搜索
     */
    public void saveHotKeywords(String hotKeywords) {
        shared.edit().putString(HOT_KEYWORDS, hotKeywords).apply();
    }

    /**
     * 热门搜索
     *
     * @return
     */
    public ArrayList<Keyword> getHotKeywords() {
        String mHistories = shared.getString(HOT_KEYWORDS, "[]");
        return JSONUtils.fromJson(mHistories, new TypeToken<ArrayList<Keyword>>() {
        });
    }

    /**
     * 移除热门搜索
     */
    public void removeHotKeywords() {
        shared.edit().remove(HOT_KEYWORDS).apply();
    }

    /**
     * 保存关键字
     */
    public void saveKeyword(String keyword) {
        shared.edit().putString(KEYWORD, keyword).apply();
    }

    /**
     * 获取关键字
     *
     * @return
     */
    public String getKeyword() {
        return shared.getString(KEYWORD, "");
    }

    /**
     * 搜索历史记录
     *
     * @return
     */
    public String getSearchHistory() {
        return shared.getString(USED_CAR_KEYWORD, "[]");
    }

    /**
     * 保存搜索历史关键字
     */
    public void saveSearchHistory(String keyword) {
        shared.edit().putString(USED_CAR_KEYWORD, keyword).apply();
    }


    /**
     * 清除搜索历史关键字
     */
    public void clearSearchHistoryKeyWord() {
        shared.edit().remove(USED_CAR_KEYWORD).apply();
    }

    /**
     * 保存城市
     *
     * @param city
     */
    public void saveCity(String city) {
        shared.edit().putString(CITY, city).apply();
    }

    /**
     * 获取城市
     */
    public String getCity() {
        return shared.getString(CITY, "");
    }

    /**
     * 保存城市列表
     *
     * @param cityList
     */
    public void saveCityList(String cityList) {
        shared.edit().putString(CITY_LIST, cityList).apply();
    }

    /**
     * 获取城市列表
     */
    public String getCityList() {
        return shared.getString(CITY_LIST, "");
    }

    /**
     * 保存品牌列表
     */
    public void saveBrandList(ArrayList<BrandGroup> brandGroups) {
        String str = JSONUtils.toJson(brandGroups);
        shared.edit().putString(BRAND_LIST, str).apply();
    }

    /**
     * 保存估价品牌列表
     */
    public void saveValuationBrandList(ArrayList<BrandGroup> brandGroups) {
        String str = JSONUtils.toJson(brandGroups);
        shared.edit().putString(VALUATION_BRAND_LIST, str).apply();
    }

    /**
     * 保存意向品牌列表
     */
    public void saveIntentBrandList(ArrayList<BrandGroup> brandGroups) {
        String str = JSONUtils.toJson(brandGroups);
        shared.edit().putString(INTENT_BRAND_LIST, str).apply();
    }

    /**
     * 获取品牌列表
     */
    public ArrayList<BrandGroup> getBrandList() {
        String str = shared.getString(BRAND_LIST, "[]");
        return JSONUtils.fromJson(str, new TypeToken<ArrayList<BrandGroup>>() {
        });
    }

    /**
     * 获取估价品牌列表
     */
    public ArrayList<BrandGroup> getValuationBrandList() {
        String str = shared.getString(VALUATION_BRAND_LIST, "[]");
        return JSONUtils.fromJson(str, new TypeToken<ArrayList<BrandGroup>>() {
        });
    }

    /**
     * 获取意向品牌列表
     */
    public ArrayList<BrandGroup> getIntentBrandList() {
        String str = shared.getString(INTENT_BRAND_LIST, "[]");
        return JSONUtils.fromJson(str, new TypeToken<ArrayList<BrandGroup>>() {
        });
    }

    /**
     * 移除品牌列表
     */
    public void removeBrandList() {
        shared.edit().remove(BRAND_LIST).apply();
    }

    /**
     * 移除估价品牌列表
     */
    public void removeValuationBrandList() {
        shared.edit().remove(VALUATION_BRAND_LIST).apply();
    }
    /**
     * 移除意向品牌列表
     */
    public void removeIntentBrandList() {
        shared.edit().remove(INTENT_BRAND_LIST).apply();
    }

    /**
     * 保存筛选条件
     */
    public void saveChoose(Choose choose) {
        String str = JSONUtils.toJson(choose);
        shared.edit().putString(CHOOSE, str).apply();
    }

    /**
     * 获取筛选条件
     */
    public Choose getChoose() {
        String str = shared.getString(CHOOSE, "{}");
        return JSONUtils.fromJson(str, Choose.class);
    }

    /**
     * 移除筛选条件
     */
    public void removeChoose() {
        shared.edit().remove(CHOOSE).apply();
    }
    /**
     * 保存筛选省市
     */
    public void savePCR(ArrayList<AreaModel> areaModels) {
        String str = JSONUtils.toJson(areaModels);
        shared.edit().putString(PRC, str).apply();
    }

    /**
     * 获取筛选省市
     */
    public ArrayList<AreaModel> getPCR() {
        String str = shared.getString(PRC, "[]");
        return JSONUtils.fromJson(str, new TypeToken<ArrayList<AreaModel>>() {
        });
    }

    /**
     * 移除筛选省市
     */
    public void removePCR() {
        shared.edit().remove(PRC).apply();
    }

    /**
     * 保存举报原因
     */
    public void saveReportReason(ReportReason reportReason) {
        String str = JSONUtils.toJson(reportReason);
        shared.edit().putString(REASON, str).apply();
    }

    /**
     * 获取举报原因
     */
    public ReportReason getReportReason() {
        String str = shared.getString(REASON, "{}");
        return JSONUtils.fromJson(str, ReportReason.class);
    }

    /**
     * 移除二手车数据
     */
    public void removeUsedCarData() {
        removeBrandList();
        removeChoose();
        removeHotKeywords();
        removeValuationBrandList();
        removeIntentBrandList();
        removeSellCarChoose();
        removePCR();
    }
}
