package com.hxqc.mall.drivingexam.ui.doexam.popuwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;

import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.drivingexam.config.C;
import com.hxqc.mall.core.util.utils.SPUtils;

/**
 * 错题设置
 * Created by zhaofan on 2016/8/11.
 */
public class WrongSubjectSettingPopuwindow extends PopupWindow {
    private Activity context;
    private View mMenuView;
    private CheckBox mRemoveWrongCb;
    private SPUtils mSpUtils;
    WindowManager.LayoutParams lp;

    public WrongSubjectSettingPopuwindow(Activity context) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mMenuView = inflater.inflate(R.layout.popuwindow_wrongsubject_setting, null);
        mSpUtils = SPUtils.getInstance(context);
        init();
        setting();

    }

    private void init() {
        mRemoveWrongCb = (CheckBox) mMenuView.findViewById(R.id.remove_wrong_cb);
        mRemoveWrongCb.setChecked(((Boolean) mSpUtils.get(C.REMOVE_WRONG_SUB_SETTING, true)));
        mRemoveWrongCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSpUtils.put(C.REMOVE_WRONG_SUB_SETTING, isChecked);
            }
        });

    }

    @Override
    public void showAsDropDown(View parent) {
        super.showAsDropDown(parent);
        setAnim(mMenuView.findViewById(R.id.active_lay), R.anim.push_in_from_top);
    }

    private void setAnim(View view, int animrid) {
        Animation anim = AnimationUtils.loadAnimation(context, animrid);
        anim.setFillAfter(true);
        view.startAnimation(anim);
    }

    private void setting() {
        this.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                setAnim(mMenuView.findViewById(R.id.active_lay), R.anim.slide_in_from_top);
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
        ColorDrawable dw = new ColorDrawable(0x44000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);


        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (y > mMenuView.findViewById(R.id.active_lay).getBottom()) {
                        dismiss();
                    }
                }
                return true;
            }
        });


    }

}
