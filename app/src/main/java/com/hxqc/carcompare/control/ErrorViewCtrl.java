package com.hxqc.carcompare.control;

import android.app.Activity;
import android.view.View;

import com.hxqc.mall.core.views.ErrorView;

import hxqc.mall.R;

/**
 * Created by zhaofan on 2016/11/2.
 */
public class ErrorViewCtrl {
    public static final String TAG_CAR_EMPTY = "car_empty";

    public static void CarEmpty(final Activity mActivity, View.OnClickListener listener) {
        ErrorView.builder(mActivity).setTag(TAG_CAR_EMPTY)
                .showCustom("添加车型后才能进行车型对比", "去添加", R.drawable.comparecar_empty, false, listener);
    }

    public static void removeCarEmpty() {
        ErrorView.remove(TAG_CAR_EMPTY);
    }


    public static void DiscussEmpty(final Activity mActivity) {
        ErrorView.builder(mActivity).showCustom("暂无评论", "", R.drawable.ic_no_date, false, null);
    }

}
