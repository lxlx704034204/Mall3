package com.hxqc.mall.core.views.vedit.tech;

import android.support.annotation.NonNull;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.core.views.vedit.VMETValidator;
import com.hxqc.util.DebugLog;

import java.util.regex.Pattern;

/**
 * Author:胡仲俊
 * Date: 2016 - 11 - 07
 * FIXME
 * Todo
 */

public class PlateNumberRegexpValidator extends VMETValidator {

    private static final String TAG = AutoInfoContants.LOG_J;
    private Pattern mPattern;
    private String mCity;

    public PlateNumberRegexpValidator(@NonNull String errorMessage, @NonNull String regex) {
        super(errorMessage);
        mPattern = Pattern.compile(regex);
    }

    public PlateNumberRegexpValidator(@NonNull String errorMessage, @NonNull String regex, String city) {
        super(errorMessage);
        mPattern = Pattern.compile(regex);
        this.mCity = city;
    }


    @Override
    public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
        DebugLog.i(TAG,"mCityView"+mCity);
        if (mCity.length() < 2) {
            DebugLog.i(TAG,"isValid - city");
            return false;
        } else if (text.length() < 5) {
            DebugLog.i(TAG,"isValid - num");
            return false;
        } else {
            return mPattern.matcher(text).matches();
        }
    }
}
