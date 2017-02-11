package com.hxqc.mall.core.api;

import android.content.Context;


/**
 * Author: wanghao
 * Date: 2015-04-09
 * FIXME
 * Todo
 */
public abstract class LoadingAnimResponseHandler extends DialogResponseHandler {
    public LoadingAnimResponseHandler(Context context) {
        super(context);
    }

    public LoadingAnimResponseHandler(Context context, boolean showAnim) {
        super(context, showAnim);
    }

    public LoadingAnimResponseHandler(Context context, boolean showAnim,int dialogType) {
        super(context, showAnim,dialogType);
    }

    public LoadingAnimResponseHandler(Context context, boolean showAnim, boolean showToast, String s) {
        super(context, showAnim, showToast);
    }

    public LoadingAnimResponseHandler(Context context, boolean showAnim, boolean cancelOutside) {
        super(context, showAnim);
        setCancelOutside(cancelOutside);
    }

    public LoadingAnimResponseHandler(Context context, boolean showAnim, boolean showToast, boolean cancelOutside) {
        super(context, showAnim,showToast);
        setCancelOutside(cancelOutside);
    }
}
