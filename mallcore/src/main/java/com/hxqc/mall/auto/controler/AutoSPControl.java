package com.hxqc.mall.auto.controler;

import android.content.Context;
import android.text.TextUtils;

import com.hxqc.mall.auto.util.SPUtils;
import com.hxqc.mall.core.controler.UserInfoHelper;

import java.util.Map;

/**
 * Author:胡仲俊
 * Date: 2017 - 01 - 16
 * Des: 车辆SP控制器
 * FIXME
 * Todo
 */

public class AutoSPControl {

    private static final String DIALOG_AUTO_INFO_COMPLETE = "Dialog";
    private static final String APPOINTMENT_INFO = "appointment_info";

    /**
     * 设置弹窗次数
     *
     * @param count
     */
    public static void saveDialogCount(int count) {
        UserInfoHelper.getInstance().getSharedPreferences().getSharedPreferences().edit().putInt(DIALOG_AUTO_INFO_COMPLETE, count).apply();
    }

    /**
     * 获取弹窗次数
     *
     * @return
     */
    public static int getDialogCount() {
        return UserInfoHelper.getInstance().getSharedPreferences().getSharedPreferences().getInt(DIALOG_AUTO_INFO_COMPLETE, 0);
    }

    /**
     * @param context
     * @param shopID
     * @param shopTitle
     * @param shopType
     */
    public static void saveAppointmentInfo(Context context, String shopID, String shopTitle, String shopType) {
        SPUtils.putFileName(APPOINTMENT_INFO);
        SPUtils.put(context, "shopID", shopID);
        if (!TextUtils.isEmpty(shopTitle)) {
            SPUtils.put(context, "shopTitle", shopTitle);
        }
        SPUtils.put(context, "shopType", shopType);
    }

    /**
     * @param context
     * @return
     */
    public static Map<String, ?> getAppointmentInfo(Context context) {
        return SPUtils.getAll(context);
    }

    /**
     * @param context
     * @param flagActivity
     */
    public static void saveAppointmentFlag(Context context, int flagActivity) {
        SPUtils.putFileName(APPOINTMENT_INFO);
        SPUtils.put(context, "flagActivity", flagActivity);
    }

    /**
     * @param context
     * @return
     */
    public static int getAppointmentFlag(Context context) {
        return (int) SPUtils.get(context, "flagActivity", -1);
    }

}
