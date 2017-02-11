package com.hxqc.autonews.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hxqc.autonews.model.pojos.AutoInfoHomeData;
import com.hxqc.autonews.model.pojos.AutoInformation;
import com.hxqc.mall.application.Application;
import com.hxqc.mall.application.SampleApplicationLike;
import com.hxqc.mall.config.application.SampleApplicationContext;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2017-02-09
 * FIXME
 * Todo 列表点击已读记录（通过保存已读的资讯id来管理获取的资讯分已读和未读）
 */

public class ListReadRecolder {
    private static final String LIST_RECORDER = "auto_info_list_read_recorder";
    private static final String AUTO_INFO_ID = "auto_info_id";
    public static SharedPreferences sharedPreferences;

    private static SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null)
            sharedPreferences = SampleApplicationContext.context.getSharedPreferences(LIST_RECORDER, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    //记录点击已读的资讯的id
    public static void saveReadInfoId(String infoID) {
        String alSaveIDs = getSharedPreferences().getString(AUTO_INFO_ID, "[]");
        ArrayList<String> infos = JSONUtils.fromJson(alSaveIDs, new TypeToken<ArrayList<String>>() {
        });
        if (infos == null)
            infos = new ArrayList<>();
        if (!infos.contains(infoID)) infos.add(infoID);
        getSharedPreferences().edit().putString(AUTO_INFO_ID, JSONUtils.toJson(infos)).apply();
    }

    //判断是否是已读
    public static boolean itemIsRead(String infoID) {
        String alSaveIDs = getSharedPreferences().getString(AUTO_INFO_ID, "[]");
        ArrayList<String> infos = JSONUtils.fromJson(alSaveIDs, new TypeToken<ArrayList<String>>() {
        });
        return infos.contains(infoID);
    }

    public static void checkRead(ArrayList<AutoInformation> sourceData) {
        for (AutoInformation information : sourceData) {
            if (itemIsRead(information.infoID))
                information.isRead = 1;//修改状态
        }
    }

    public static void markRead(AutoInfoHomeData homeDatas) {
        checkRead(homeDatas.infoList);
    }
}
