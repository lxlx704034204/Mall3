package com.hxqc.carcompare.ui.popuwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.TextView;

import hxqc.mall.R;

/**
 * 车型对比 选择查看模式
 * Created by zhaofan on 2016/11/13.
 */
public class ChooseModePopuwindow extends PopupWindow {
    private CheckBox checkbox;
    private String chooseText;
    private Activity context;
    private View mMenuView;
    WindowManager.LayoutParams lp;
    private TextView[] tv;
    private OnCallBackkListener listener;

    public ChooseModePopuwindow(Activity context, String chooseText, CheckBox checkbox) {
        super(context);
        this.context = context;
        this.chooseText = chooseText;
        this.checkbox = checkbox;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mMenuView = inflater.inflate(R.layout.popuwindow_choose_mode, null);
        init();
        setting();

    }

    private void init() {
        checkbox.setChecked(true);
        tv = new TextView[3];
        tv[0] = (TextView) mMenuView.findViewById(R.id.tv1);
        tv[1] = (TextView) mMenuView.findViewById(R.id.tv2);
        tv[2] = (TextView) mMenuView.findViewById(R.id.tv3);
        for (int i = 0; i < tv.length; i++) {
            if (tv[i].getText().toString().equals(chooseText)) {
                tv[i].setTextColor(context.getResources().getColor(R.color.font_orange));
            }
            tv[i].setOnClickListener(new MyClickListener(i));
        }


    }


    private void setting() {
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                checkbox.setChecked(false);
            }
        });
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //  this.setAnimationStyle(R.style.AnimSlideFromTop);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);


        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int y = (int) event.getY();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (y > mMenuView.findViewById(R.id.active_lay).getBottom()
                            || x < mMenuView.findViewById(R.id.active_lay).getLeft()
                            || x > mMenuView.findViewById(R.id.active_lay).getRight()) {
                        dismiss();
                    }
                }

                return true;
            }
        });


    }

    public void setCallBack(OnCallBackkListener listener) {
        this.listener = listener;
    }

    public interface OnCallBackkListener {
        void onCallBack(int index);
    }


    private class MyClickListener implements View.OnClickListener {

        private int index;

        public MyClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View view) {
            if (listener != null)
                listener.onCallBack(index);
            dismiss();
        }
    }
}
