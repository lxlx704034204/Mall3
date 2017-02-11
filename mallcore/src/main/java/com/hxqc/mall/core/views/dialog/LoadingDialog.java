package com.hxqc.mall.core.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hxqc.mall.core.R;

/**
 * Author: wanghao
 * Date: 2015-04-09
 * FIXME
 * Todo
 */
public class LoadingDialog extends Dialog {

    public static final int DFLOADING = 10;//默认loading
    public static final int QRLOADING = 20;//扫码loading

    /**
     * 生成默认 loading
     */
    private void initDefaultLoading(Context context) {
        setContentView(R.layout.dialog_loading);
        setCancelable(true);
        RelativeLayout pView = (RelativeLayout) findViewById(R.id.hx_default_loading);
        pView.setVisibility(View.VISIBLE);
        ImageView imageView = (ImageView) findViewById(R.id.iv_round);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate_anim);
        LinearInterpolator interpolator = new LinearInterpolator();
        animation.setInterpolator(interpolator);
        imageView.startAnimation(animation);
    }

    /**
     * 扫码 loading
     */
    private void initQRLoading(Context context) {
        setContentView(R.layout.dialog_loading);
        setCancelable(true);
        RelativeLayout pView = (RelativeLayout) findViewById(R.id.hx_qr_loading);
        pView.setVisibility(View.VISIBLE);
        ImageView imageView = (ImageView) findViewById(R.id.qr_round);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate_anim);
        LinearInterpolator interpolator = new LinearInterpolator();
        animation.setInterpolator(interpolator);
        imageView.startAnimation(animation);
    }

    public LoadingDialog(Context context) {
        super(context, R.style.loading_dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initDefaultLoading(context);
    }


    public LoadingDialog(Context context, int animType) {
        super(context, R.style.loading_dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (animType == QRLOADING) {
            initQRLoading(context);
        } else {
            initDefaultLoading(context);
        }
    }

}
