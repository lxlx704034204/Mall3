package com.hxqc.mall.core.api;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.hxqc.mall.core.controler.LogoutAction;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.views.dialog.NormalDialog;
import com.hxqc.mall.launch.util.ActivitySwitchAuthenticate;

/**
 * Author: wanghao
 * Date: 2016-01-14
 * FIXME
 * Todo   api 回调 弹窗管理
 */
public class BaseResponseHandleDialogManager {

    protected static BaseResponseHandleDialogManager mInstance;
    NormalDialog dialog;
//    TextDialog textDialog;
    Context c;

    protected BaseResponseHandleDialogManager() {
    }

    public static BaseResponseHandleDialogManager getInstance() {
        if (mInstance == null) {
            synchronized (BaseResponseHandleDialogManager.class) {
                if (mInstance == null) {
                    mInstance = new BaseResponseHandleDialogManager();
                }
            }
        }
        return mInstance;
    }

    public void onDestroy() {
//        textDialog=null;
        dialog = null;
        mInstance = null;
    }

    public void initDialog(Context ctx, String title, String text, int errorCode) {
        this.c = ctx;

        if (errorCode == 401 || errorCode == 402) {
            new LogoutAction().onLogoutSuccess(ctx);
            switchLoginDialog(title, text);
        }else if(errorCode ==404) {     //注册
            switchRegisterDialog(title, text);
        }
    }
    private void switchRegisterDialog(String title,String text) {
//        if (textDialog == null) {
//            textDialog = new TextDialog(c,title,text);
//            textDialog.setCanceledOnTouchOutside(false);
//            textDialog.setCancelable(false);
//        } else {
//            textDialog.setDialogTitle(title);
//            textDialog.setDialogContent(text);
//        }
            dialog = new NormalDialog(c, title, text) {
                @Override
                protected void doNext() {
                    jumpToRegister();
                    onDestroy();
                }
            };
            if(dialog.mOkView !=null)
                dialog.mOkView.setText("去注册");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.mCancelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    onDestroy();
                }
            });
            dialog.setDialogTitle(title);
            dialog.setDialogContent(text);
 }


    private void switchLoginDialog(final String title, final String text) {
            dialog = new NormalDialog(c, title, text) {
                @Override
                protected void doNext() {
                    jumpToLogin();
                    onDestroy();
                }
            };
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            if (dialog.mCancelView != null) {
                dialog.mCancelView.setVisibility(View.INVISIBLE);
            }
            dialog.setDialogTitle(title);
            dialog.setDialogContent(text);
    }

    public void showDialog() {
        if (dialog != null) {
            if (!dialog.isShowing() && !((Activity) c).isFinishing()) {
                dialog.show();
            }
        }
    }
//    public void showRegisterDialog() {
//        if (textDialog != null) {
//            if (!textDialog.isShowing() && !((Activity) c).isFinishing()) {
//                textDialog.show();
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        jumpToRegister();
//                        textDialog.dismiss();
//                        onDestroy();
//                    }
//                }, 2000);
//            }
//        }
//    }

    //登录
    private void jumpToLogin() {
        ActivitySwitchAuthenticate.toLogin(c);
    }

    //注册
    private void jumpToRegister() {
        ActivitySwitchAuthenticate.toRegister(c, ActivitySwitchBase.ENTRANCE_SHOP);
    }
}
