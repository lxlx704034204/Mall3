package com.hxqc.mall.thirdshop.maintenance.utils;

import android.content.Context;

import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年02月18日
 */
public class SharedPreferencesHelper extends BaseSharedPreferencesHelper {
    private final static String TAG = SharedPreferencesHelper.class.getSimpleName();
    private Context mContext;


    public SharedPreferencesHelper(Context context) {
        super(context);
    }
}
