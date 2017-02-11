package com.hxqc.mall.core.api;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.hxqc.mall.core.views.dialog.LoadingDialog;
import com.hxqc.mall.core.views.dialog.SubmitDialog;

/**
 * 说明:带loading框的ResponseHandler
 * <p/>
 * author: 吕飞
 * since: 2015-03-25
 * Copyright:恒信汽车电子商务有限公司
 */
public abstract class DialogResponseHandler extends BaseMallJsonHttpResponseHandler {
    //提示框
    public Dialog loadingDialog;
    boolean isShowAnim = true;
    private boolean cancelOutside = true;

    private final int StringDialog = 1;//字符串loading
    private final int AnimDialog = 2;//转圈loading
    private final int QRDialog = 3;//扫码loading
    private int currentType = 0;

    String str = "";

    public void setCancelOutside(boolean cancelOutside) {
        this.cancelOutside = cancelOutside;
    }

    public DialogResponseHandler(Context context) {
        super(context);
        currentType = AnimDialog;
    }

    public DialogResponseHandler(Context context, String string) {
        super(context);
        this.str = string;
        currentType = StringDialog;
    }

    public DialogResponseHandler(Context context, boolean showAnim) {
        super(context);
        this.isShowAnim = showAnim;
        currentType = AnimDialog;
    }

    public DialogResponseHandler(Context context, boolean showAnim,int dialogType) {
        super(context);
        this.isShowAnim = showAnim;
        currentType = dialogType;
    }

    private void showAnimDialog() {
        if (isShowAnim) {
            if (mContext instanceof Activity) {
                if (!((Activity) mContext).isFinishing()) {
                    loadingDialog = new LoadingDialog(mContext);
                    loadingDialog.setCancelable(cancelOutside);
                    if (!loadingDialog.isShowing())
                        loadingDialog.show();
                }
            }
        }
    }

    private void showQRDialog() {
        if (isShowAnim) {
            if (mContext instanceof Activity) {
                if (!((Activity) mContext).isFinishing()) {
                    loadingDialog = new LoadingDialog(mContext,LoadingDialog.QRLOADING);
                    loadingDialog.setCancelable(cancelOutside);
                    if (!loadingDialog.isShowing())
                        loadingDialog.show();
                }
            }
        }
    }

    public DialogResponseHandler(Context context, boolean showAnim, boolean showToast) {
        super(context);
        this.isShowAnim = showAnim;
        this.showToast = showToast;
        currentType = AnimDialog;
    }

    private void showStringDialog(String text) {
        if (mContext instanceof Activity) {
            if (!((Activity) mContext).isFinishing()) {
                loadingDialog = new SubmitDialog(mContext, text);
                loadingDialog.setCancelable(cancelOutside);
                loadingDialog.show();
            }
        }
    }

    private void dissmissDialog() {
        boolean b = true;

        if (mContext instanceof Activity) {
            b = !((Activity) mContext).isFinishing();
        }
        if (b && loadingDialog != null && isShowAnim && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (currentType == AnimDialog) {
            showAnimDialog();
        } else if (currentType == StringDialog) {
            showStringDialog(str);
        } else if (currentType == QRDialog){
            showQRDialog();
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        dissmissDialog();
    }
}
