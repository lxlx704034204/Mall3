/**
 * 2014年7月22日
 * ZmUtil.java
 * TODO
 * @version 1.0
 * author 胡俊杰
 */
package com.hxqc.socialshare.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * author 胡俊杰
 */
public class AppUtil {
    /**
     * activity MetaData读取
     *
     * @throws NameNotFoundException
     */
    public static String getActivityMetaData(Context context, String key) throws NameNotFoundException {
        ActivityInfo info;
        info = context.getPackageManager().getActivityInfo(((Activity) context).getComponentName(),
                PackageManager.GET_META_DATA);

        return info.metaData.getString(key);

    }

    /**
     * appliction MetaData读取
     *
     * @throws NameNotFoundException
     */
    public static String getApplicationMetaDataString(Context context, String key) throws NameNotFoundException {
        ApplicationInfo info;
        info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        return info.metaData.getString(key);

    }
    /**
     * appliction MetaData读取
     *
     * @throws NameNotFoundException
     */
    public static int getApplicationMetaDataInt(Context context, String key) throws NameNotFoundException {
        ApplicationInfo info;
        info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        return info.metaData.getInt(key);

    }
}
