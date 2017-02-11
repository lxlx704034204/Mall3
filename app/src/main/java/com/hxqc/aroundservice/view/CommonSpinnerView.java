package com.hxqc.aroundservice.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hxqc.aroundservice.adapter.AutoVarietyAdapter;
import com.hxqc.aroundservice.view.kankan.wheel.widget.WheelView;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.util.DebugLog;

import hxqc.mall.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 08
 * Des: 通用的Spinner
 * FIXME
 * Todo
 */
public class CommonSpinnerView extends TextView implements View.OnFocusChangeListener {

    private static final String TAG = AutoInfoContants.LOG_J;
    private PopupWindow mPopupWindow;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            show();
        }
    };

    public CommonSpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.setOnClickListener(clickListener);
        this.setOnFocusChangeListener(this);
    }

    public void show() {
        if (mPopupWindow == null) {
            DebugLog.i(TAG, "show");
            mPopupWindow = new PopupWindow(mContext);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            AutoVarietyAdapter autoVarietyAdapter = new AutoVarietyAdapter(mContext);
            View view = mLayoutInflater.inflate(R.layout.layout_auto_variety, null);
            WheelView mAutoVarietyWheelView = (WheelView) view.findViewById(R.id.auto_variety_wheel);
            mAutoVarietyWheelView.setVisibleItems(5); // Number of items
            mAutoVarietyWheelView.setWheelBackground(R.drawable.bg_wheel_holo);
            mAutoVarietyWheelView.setWheelForeground(R.drawable.val_wheel_holo);
            mAutoVarietyWheelView.setShadowColor(0xffffff, 0xffffff, 0xffffff);
            mAutoVarietyWheelView.setViewAdapter(autoVarietyAdapter);
            mAutoVarietyWheelView.setCurrentItem(3);
            mPopupWindow.setContentView(view);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.update();
            mPopupWindow.showAtLocation(this, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            if (mPopupWindow != null) {
                DebugLog.i(TAG, "dismiss");
                mPopupWindow.dismiss();
            }
        }
    }
}
