package com.hxqc.mall.core.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.util.DebugLog;

/**
 * 说明:自定义toast
 * <p>
 * author: 吕飞
 * since: 2015-03-17
 * Copyright:恒信汽车电子商务有限公司
 */
public class ToastHelper {
    private static final int DURATION = 2200;
    private static final int GREEN = 1;
    private static final int YELLOW = 2;
    private static final int ORANGE = 3;
    private Context mContext;
    private Handler mHandler;
    private String mToastContent = "";
    private int duration = 2200;
    private int animStyleId = R.style.Toast;
    private PopupWindow toastWindow;
    private OnToastDismissListener mToastDismissListener = null;

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            removeView();
        }
    };

    private ToastHelper(Context context) {
        if (context == null) {
            DebugLog.w("toast", "context 为空");
            return;
        }
        this.mContext = context;
        init();
    }

    private ToastHelper(Context context, OnToastDismissListener listener) {
        if (context == null) {
            DebugLog.w("toast", "context 为空");
            return;
        }

        this.mToastDismissListener = listener;
        this.mContext = context;
        init();
    }

    private void init() {
        toastWindow = new PopupWindow(mContext);
        toastWindow.setAnimationStyle(animStyleId);
        toastWindow.setHeight((int) mContext.getResources().getDimension(R.dimen.toast_height_56));
        toastWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        toastWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    public static void showRedToast(Context context, String mToastContent) {
        makeText(context, mToastContent).show(ORANGE);
    }

    public static void showGreenToast(Context context, String mToastContent) {
        makeText(context, mToastContent).show(GREEN);
    }

    public static void showYellowToast(Context context, String mToastContent) {
        makeText(context, mToastContent).show(YELLOW);
    }


    public static void showRedToast(Context context, int resId) {
        makeText(context, resId).show(ORANGE);
    }

    public static void showGreenToast(Context context, int resId) {
        makeText(context, resId).show(GREEN);
    }

    public static void showYellowToast(Context context, int resId) {
        makeText(context, resId).show(YELLOW);
    }

    public static ToastHelper makeText(Context context, String content) {
        ToastHelper helper = new ToastHelper(context);
        helper.setContent(content);
        return helper;
    }

    public static ToastHelper makeText(Context context, int strId) {
        ToastHelper helper = new ToastHelper(context);
        helper.setContent(context.getString(strId));
        return helper;
    }

    /**
     * TODO
     */
    public static ToastHelper makeText(Context context, String content, OnToastDismissListener toastDismissListener) {
        ToastHelper helper = new ToastHelper(context, toastDismissListener);
        helper.setContent(content);
        return helper;
    }

    public void show(int color) {
        if (toastWindow == null) {
            return;
        }
        if (!OtherUtil.isBackground(mContext)) {
            View toastView = getDefaultToastView(color);
            toastWindow.setContentView(toastView);
            try {
                if (mContext instanceof Activity) {
                    Activity activity = (Activity) mContext;
                    View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);

                    //计算状态栏高度
                    Rect rectangle = new Rect();
                    ((Activity) mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
                    int h = rectangle.top;
                    toastWindow.showAtLocation(rootView, Gravity.TOP | Gravity.LEFT, 0, h);
                    if (mHandler == null) {
                        mHandler = new Handler();
                    }
                    mHandler.postDelayed(timerRunnable, duration);
                } else {
                    DebugLog.w("toast", "context 不是 activity 的实例");
                }
            } catch (Exception e) {
                DebugLog.w("toast", "context 转换 activity 失败");
            }

        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private View getDefaultToastView(int color) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = null;
        switch (color) {
            case GREEN:
                v = inflater.inflate(R.layout.toast_green, null);
                break;
            case ORANGE:
                v = inflater.inflate(R.layout.toast_orange, null);
                break;
            case YELLOW:
                v = inflater.inflate(R.layout.toast_yellow, null);
                break;
        }
        TextView textView = (TextView) (v != null ? v.findViewById(R.id.content) : null);
        if (textView != null) {
            textView.setText(mToastContent);
        }
        return v;
    }


    public void removeView() {
        if (mContext instanceof Activity) {
            if (!((Activity) mContext).isFinishing() && toastWindow != null && toastWindow.isShowing()) {
                toastWindow.dismiss();

                if (mToastDismissListener != null) {
                    mToastDismissListener.onToastDismiss();
                    mToastDismissListener = null;
                }
            }

            if (mHandler != null)
                mHandler.removeCallbacks(timerRunnable);
        }
    }


    public ToastHelper setContent(String content) {
        this.mToastContent = content;
        return this;
    }

    public ToastHelper setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public ToastHelper setAnimation(int animStyleId) {
        if (toastWindow != null)
            toastWindow.setAnimationStyle(animStyleId);
        return this;
    }

    public interface OnToastDismissListener {
        void onToastDismiss();
    }

    public static void toastFinish(final Activity activity) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.finish();
            }
        }, DURATION);
    }
}

