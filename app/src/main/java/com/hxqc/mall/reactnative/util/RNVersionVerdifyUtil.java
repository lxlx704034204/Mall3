package com.hxqc.mall.reactnative.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.hxqc.util.DebugLog;

/**
 * Author:  wh
 * Date:  2016/4/28
 * FIXME   rn版本 判断
 * Todo
 */
public class RNVersionVerdifyUtil {

    private static final String SHARED_PREFERENCES_NAME = "com.hxqc.mall.rn";

    /**
     * 安装时 首页   rn  本地 版本
     */
    public final static String install_version = "install_164_02_04_18_04";

    //当前本地bundle 版本   只做记录 不用
    private final String localBundleVersionName = "目前不清楚 等下一班 换新的";

    /**
     * 安装时 首页   rn  网络 版本
     */
    public final static String service_version = "548ff1d98dd1621b86d0581d7f9ea90d";

    /**
     * rn 网络版本号 存储在首选项中的 网络  key
     */
    private final static String versionPrfSaveKey = "RN_HOME_VERSION";

    /**
     * rn 本地版本号 存储在首选项中的 本地  key
     */
    private final static String versionLocalPrfSaveKey = "RN_HOME_LOCAL_VERSION";

    private final static String encodeFileSaveName = "RN_BUNDLE_NAME";

    protected SharedPreferences shared;

    public RNVersionVerdifyUtil(Context context) {
        shared = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 获取加密bundle文件名
     */
    public String getBundleNameFromPrf() {
        return shared.getString(encodeFileSaveName, "");
    }

    /**
     * 保存加密bundle文件名到首选项
     */
    public void saveBundleNameToPrf(String newName) {
        shared.edit().putString(encodeFileSaveName, newName).apply();
    }

    /**
     * 获取网络首选项中的版本号
     */
    public String getServiceVersionFromPrf() {
        return shared.getString(versionPrfSaveKey, "");
    }

    /**
     * 保存网络版本号到首选项
     */
    public void saveServiceVersionToPrf(String newVersion) {
        shared.edit().putString(versionPrfSaveKey, newVersion).apply();
    }

    /**
     * 获取本地首选项中的版本号
     */
    public String getLocalVersionFromPrf() {
        return shared.getString(versionLocalPrfSaveKey, "");
    }

    /**
     * 保存本地版本号到首选项
     */
    public void saveLocalVersionToPrf(String newVersion) {
        shared.edit().putString(versionLocalPrfSaveKey, newVersion).apply();
    }

    /**
     * 判断 本地版本号
     * 如果 本地版本号与首选项版本号相同  直接读取本地文件
     */
    public boolean verdifyLocalVersion() {
        boolean isLeast = false;
        DebugLog.i("rn_version", " install_version: "+install_version+" prfversion: "+getLocalVersionFromPrf());
        if (TextUtils.isEmpty(getLocalVersionFromPrf())) {
            isLeast = false;
            saveLocalVersionToPrf(install_version);
        } else {
            if (install_version.equals(getLocalVersionFromPrf())) {
                isLeast = true;
            } else {
                isLeast = false;
                saveLocalVersionToPrf(install_version);
            }
        }
        DebugLog.i("rn_version", "isLeast: "+isLeast+" install_version: "+install_version+" prfversion: "+getLocalVersionFromPrf());
        return isLeast;
    }

}
