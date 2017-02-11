package com.hxqc.mall.auto.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.hxqc.mall.auto.dao.PACDao;
import com.hxqc.mall.auto.model.PAC;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 13
 * FIXME
 * Todo 省份与城市的工具
 */
public class PACUtil {

    private static final String TAG = "PACUtil";
    private String PAC_PATH = "file:///android_asset/provinceandcity.txt";
    private static String fileName = "provinceandcity.txt"; //文件名字
    private String res = "";
    private static List<PAC> pacs;

    public static void getFromAssets(Context context) {
        pacs = new ArrayList<PAC>();
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null) {
                String[] split = line.split(",");
                PAC pac = new PAC(split[0], split[1], split[2]);
                PACDao.getInstance().add(context,pac);
                pacs.add(pac);
                DebugLog.i(TAG, pac.toString());
            }
            toJson(context, pacs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void toJson(Context context, List<PAC> pacs) {
        String json = JSONUtils.toJson(pacs);
        DebugLog.i(TAG,"json:"+json);
        savePACDataLocal(context, json);
    }

    public static final String SHARED_PREFERENCES_NAME = "com.hxqc.hxqcmall.pac";
    protected static final String PAC_INFO = "com.hxqc.hxqcmall.pac_json";
    private static SharedPreferences sharedPreferences;

    public static SharedPreferences getSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_NAME,
                    Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    /**
     * 保存车辆信息
     *
     * @param pacJson
     */
    public static void savePACDataLocal(Context context, String pacJson) {
        getSharedPreferences(context).edit().putString(PAC_INFO, pacJson).apply();
    }


}
