package com.hxqc.autonews.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hxqc.autonews.model.pojos.AutoInfoDetail;
import com.hxqc.autonews.model.pojos.AutoInformation;
import com.hxqc.mall.core.util.ACache;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-10-14
 * FIXME
 * Todo 缓存资讯列表的非第一页数据缓存工具
 */

public class CacheAutoInfoUtil {
    public static final String CACHE_AUTOINFO = "auto_info_cache_tatol";
    public static final String TAG = "CacheAutoInfoUtil";
    private static final String CACHE_AUTOINFO_DETAIL = "cache_auto_info_detail";

    /**
     * 保存资讯列表的非第一页数据
     *
     * @param page
     * @param aCache
     * @param guiCode
     * @param informations
     */
    public static void saveListData(int page, ACache aCache, String guiCode, ArrayList<AutoInformation> informations) {
        if (informations == null) {
            DebugLog.d(TAG, "data is null to cache");
            return;
        }
        String newDataJson = JSONUtils.toJson(informations);
        String oldDataJson = aCache.getAsString(getCacheFileName(page, guiCode));
        if (!TextUtils.isEmpty(newDataJson) && !newDataJson.equals(oldDataJson)) {
            //不相同就存储
            saveDataPage(aCache, page, guiCode, informations);
            DebugLog.d(TAG, "saveData:" + newDataJson);
        }
    }

    /**
     * 保存资讯列表的非第一页数据
     *
     * @param aCache
     * @param page
     * @param guiCode
     * @param informations
     */
    private static void saveDataPage(ACache aCache, int page, String guiCode, ArrayList<AutoInformation> informations) {
        String json = JSONUtils.toJson(informations);
        aCache.put(getCacheFileName(page, guiCode), json);
    }

    /**
     * 判断是否包含当前组数据
     *
     * @param autoInformation
     * @return
     */
    private static boolean isInclude(ArrayList<AutoInformation> autoInformations, AutoInformation autoInformation) {
        if (autoInformation != null) {
            return false;
        }
        if (autoInformations == null && autoInformations.isEmpty())
            return false;
        for (AutoInformation information : autoInformations) {
            if (autoInformation.equals(information))
                return true;
        }
        return false;
    }


    /**
     * 获取加载更多中某一页
     *
     * @param aCache
     * @param page
     * @param guiCode
     * @return
     */
    public static ArrayList<AutoInformation> getListDataPage(ACache aCache, int page, String guiCode) {
        String asString = aCache.getAsString(getCacheFileName(page, guiCode));
        DebugLog.d("AutoInformationModel", asString);
        if (TextUtils.isEmpty(asString))
            return null;
        else {
            ArrayList<AutoInformation> informations = JSONUtils.fromJson(asString, new TypeToken<ArrayList<AutoInformation>>() {
            });
            return informations;
        }
    }

    private static String getCacheFileName(int page, String guiCode) {
        return CACHE_AUTOINFO + guiCode + page;
    }

    /**
     * 两组数据是否一致
     *
     * @param list1
     * @param list2
     * @return
     */
    public static boolean equals(ArrayList<AutoInformation> list1, ArrayList<AutoInformation> list2) {
        if (list1 == null || list2 == null)
            return false;
        if (list1.size() == list2.size()) {
            for (int i = 0; i < list1.size(); i++) {
                if (!list1.get(i).equals(list2.get(i))) {
                    return false;
                }
            }
            return true;
        } else return false;
//        String s = JSONUtils.toJson(list1);
//        String s1 = JSONUtils.toJson(list2);
//        if (s == null || s1 == null)
//            return false;
//        return s.equals(s1);
    }


/************************************资讯详情的缓存*********************************************/


    /**
     * 保存资讯详情
     *
     * @param aCache
     * @param infoID
     */
    public static void saveAutoInfoDetail(ACache aCache, String infoID, AutoInfoDetail detail) {
        String jsonString = JSONUtils.toJson(detail);
        aCache.put(getAutoInfoDetailCacheName(infoID), jsonString);
    }

    /**
     * 获取保存的详情
     *
     * @param infoID
     * @param aCache
     * @return
     */
    public static AutoInfoDetail getCacheAutoDetial(String infoID, ACache aCache) {
        String asString = aCache.getAsString(getAutoInfoDetailCacheName(infoID));
        DebugLog.d("AutoInfoDetailModel", asString);
        if (!TextUtils.isEmpty(asString)) {
            AutoInfoDetail detail = JSONUtils.fromJson(asString, AutoInfoDetail.class);
            return detail;
        } else
            return null;
    }


    public static boolean equals(AutoInfoDetail detail, AutoInfoDetail detail1) {
        if (detail == null || detail1 == null) {
            return false;
        }
        String s = JSONUtils.toJson(detail);
        String s1 = JSONUtils.toJson(detail1);
        if (s == null || s1 == null) {
            return false;
        }
        return (s.equals(s1));

    }

    @NonNull
    private static String getAutoInfoDetailCacheName(String infoID) {
        return CACHE_AUTOINFO_DETAIL + infoID;
    }

}
