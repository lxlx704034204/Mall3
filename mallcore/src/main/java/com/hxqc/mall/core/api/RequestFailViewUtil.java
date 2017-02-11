package com.hxqc.mall.core.api;

import android.content.Context;
import android.view.Gravity;
import android.view.View;


/**
 * Author: wanghao
 * Date: 2015-05-20
 * FIXME
 * Todo 请求失败view
 */
public class RequestFailViewUtil {
    com.hxqc.mall.core.api.RequestFailView mFailView;

    public View getFailView(Context context) {
        if (mFailView == null) {
            mFailView = new com.hxqc.mall.core.api.RequestFailView(context);
            mFailView.setVisibility(View.GONE);
            mFailView.setGravity(Gravity.CENTER);
        }
        return mFailView;
    }

    public View getEmptyView(Context context, String text) {
        return getFailView(context);
    }
}
