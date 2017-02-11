package com.hxqc.mall.core.views.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.hxqc.mall.core.views.ErrorView;
import com.hxqc.util.DebugLog;
import com.hxqc.util.ScreenUtil;

import java.lang.reflect.Field;


/**
 * @author zhaofan'
 */
public class DialogFragment extends android.support.v4.app.DialogFragment {
    private static final String TAG_FULL_LOADING = "full_loading";
    private Activity context;
    private Dialog dialog;
    private String text = "正在提交,请稍等";
    private boolean touchCancel = false; //点击屏幕取消dialog 默认false不可取消
    private boolean backDownCancel = true;//返回键取消dialog 默认true
    private boolean emptyBackground = false;
    private int isAdd = 0;

    private int type = DialogType.Loading;

    public DialogFragment() {
    }


    public static DialogFragment builder() {
        DialogFragment instance = new DialogFragment();
        return instance;
    }

    public static DialogFragment builder(int type) {
        DialogFragment instance = new DialogFragment();
        instance.type = type;
        return instance;
    }


    public DialogFragment setCancelTouchOutSide(boolean touchCancel) {
        this.touchCancel = touchCancel;
        return this;
    }

    public DialogFragment setCancelBackDown(boolean backDownCancel) {
        this.backDownCancel = backDownCancel;
        return this;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (type == DialogType.Loading)
            dialog = new LoadingDialog(context);
        else if (type == DialogType.LoadingWithText)
            dialog = new SubmitDialog(context, text);


        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0f;
        dialog.getWindow().setAttributes(lp);

        bindListner();
        return dialog;

    }


    private void bindListner() {
        dialog.setCanceledOnTouchOutside(touchCancel);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (backDownCancel) {
                        // hide();
                        if (isActivityRun(context))
                            context.finish();
                    }
                }
                return true;
            }
        });

    }


    public DialogFragment setText(String text) {
        this.type = DialogType.LoadingWithText;
        this.text = text;
        return this;
    }

    public void show(Context mA, String text) {
        setText(text);
        this.show(mA);
    }

    public void show(Context mA, boolean emptyBackground) {
        this.show(mA, emptyBackground, 0);
    }

    public void show(Context mA, boolean emptyBackground, int topMargin) {
        this.show(mA);
        this.emptyBackground = emptyBackground;
        if (emptyBackground)
            ErrorView.builder((Activity) mA).topMargin(topMargin).setTag(TAG_FULL_LOADING).showBlank();
    }


    public void show(Context mA) {
        if (!(mA instanceof Activity)) {
            throw new IllegalArgumentException("the Contxt is not a Activity!");
        }
        if (isAdd > 0) {
            return;
        }
        context = (AppCompatActivity) mA;

        if (isActivityRun(((Activity) mA)) && !isAdded() && !isVisible() && !isRemoving()) {
            this.show(((AppCompatActivity) context).getSupportFragmentManager(), "");
            isAdd++;
        }
    }

    /**
     * 重写show()方法
     * {@link android.support.v4.app.DialogFragment#show(FragmentManager, String)}.
     */
    public void show(FragmentManager manager, String tag) {
        try {
            Class<?> cls = this.getClass().getSuperclass();
            Field f = cls.getDeclaredField("mDismissed");
            f.setAccessible(true);
            f.set(this, false);
            Field f2 = cls.getDeclaredField("mShownByMe");
            f2.setAccessible(true);
            f2.set(this, true);
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void hide() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isActivityRun(context) && dialog != null && dialog.isShowing()) {
                    dismiss();
                }
            }
        }, 20);
    }


    private boolean isActivityRun(Activity mA) {
        return mA != null && !mA.isFinishing();
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (this.dialog != null) {
            this.dialog.cancel();
            this.dialog.dismiss();
            this.dialog = null;
            context = null;
            DebugLog.d("onDismiss", "释放dialog");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        reFetch();
    }

    private void reFetch() {
        isAdd = 0;
        if (emptyBackground) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ErrorView.remove(TAG_FULL_LOADING);
                }
            }, 0);
            emptyBackground = false;
        }
    }


    private void ShowFullScreen() {
        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = getHeight();//
        window.setAttributes(wlp);
    }

    private int getHeight() {
        int statusBarHeight = getStatusBarHeight();
        int merginTop = ScreenUtil.dip2px(context, 55);
        if (context instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) context).getSupportActionBar();
            if (actionBar != null) {
                TypedValue tv = new TypedValue();
                if (context.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, tv, true)) {
                    int h = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
                    merginTop = (h);
                }
                /*    TypedArray actionbarSizeTypedArray = context.obtainStyledAttributes(new int[]{android.support.v7.appcompat.R.attr.actionBarSize});
                float h = actionbarSizeTypedArray.getDimension(0, 0);*/
            }
        }
        return (ScreenUtil.getScreenHeight(context) - merginTop - statusBarHeight);
    }


    /**
     * 获取状态栏高度
     */
    public int getStatusBarHeight() {
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        Log.e("tag", "状态栏 :" + statusBarHeight1);

        return statusBarHeight1;
    }

}
