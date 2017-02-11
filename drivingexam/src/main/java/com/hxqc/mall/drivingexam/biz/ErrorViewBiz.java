package com.hxqc.mall.drivingexam.biz;

import android.app.Activity;
import android.os.Handler;
import android.view.View;

import com.hxqc.mall.core.views.ErrorView;
import com.hxqc.mall.drivingexam.R;

/**
 * Created by zhaofan on 2016/8/9.
 */
public class ErrorViewBiz {

    public static void requestFail(final Activity mActivity, int hight, final View.OnClickListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ErrorView.builder(mActivity).showCustom("网络连接失败",
                        "重新加载", R.drawable.no_line, true, listener);
            }
        }, 20);
    }


    public static void showWrongSubjectEmpty(final Activity mActivity) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ErrorView.builder(mActivity)
                        .showCustom("您还没有错题", "返回", R.drawable.file_search, false, null);
            }
        }, 20);
    }


}
