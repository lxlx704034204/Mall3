package com.hxqc.mall.views;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hxqc.mall.auto.util.ActivitySwitchAutoInfo;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-04-11
 * FIXME
 * Todo 底部菜单
 */
public class MenuPopupWindow extends PopupWindow implements View.OnClickListener {
    private Activity activity;
    private LinearLayout pop_layout;
    private OnStateChangeListener onStateChangeListener;

    public MenuPopupWindow(Activity activity) {
        this.activity = activity;
        LayoutInflater inflater = LayoutInflater.from(activity);
        final View view = inflater.inflate(R.layout.layout_menu_popup_window_2, null);
//        view.findViewById(R.id.button_maintenance).setOnClickListener(this);
//        view.findViewById(R.id.button1).setOnClickListener(this);
//        view.findViewById(R.id.button_accessory).setOnClickListener(this);
//        view.findViewById(R.id.button_fours_store).setOnClickListener(this);
//        view.findViewById(R.id.button_new_energy_car).setOnClickListener(this);
//        view.findViewById(R.id.button_wash_car).setOnClickListener(this);
        view.findViewById(R.id.item_4s_shop).setOnClickListener(this);
        view.findViewById(R.id.item_maintence).setOnClickListener(this);
        pop_layout = (LinearLayout) view.findViewById(R.id.poplayout);
//        int height = pop_layout.getLayoutParams().height;
//        int height = ScreenUtil.dip2px(activity, 130);
        int height = activity.getResources().getDimensionPixelOffset(R.dimen.wallet_bottom_menu_window_height);
        this.setContentView(view);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(height);
        this.setFocusable(true);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.MenuPopupWindow);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = pop_layout.getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP)
                    if (y < height)
                        dismiss();
                return true;
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (onStateChangeListener != null)
            onStateChangeListener.onStateChange(false);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (onStateChangeListener != null) {
            onStateChangeListener.onStateChange(true);
        }
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.button_maintenance:
////                ActivitySwitcher.toNormalMaintenance(activity);
////                ActivitySwitcherMaintenance.toShopQuote(activity);
//                ActivitySwitchAutoInfo.toFlowMaintain(activity);
//                break;
//            case R.id.button1:
//                Brand brand = new Brand("");
//                brand.brandID = "";
////                ActivitySwitcher.toBrand(activity, brand);
//                ActivitySwitcher.toBrand(activity);
//                break;
//            case R.id.button_accessory:
//                ActivitySwitcherAccessory4S.toAccessorySaleFromHome(activity);
//                break;
//            case R.id.button_fours_store:
//                activity.startActivity(new Intent(activity, FilterThirdShopActivity.class));
//                break;
//            case R.id.button_new_energy_car:
//
//                activity.startActivity(new Intent(activity, Ev_NewEnergyActivity.class));
//                break;
//            case R.id.button_wash_car:
//                CarWashActivitySwitcher.toWashCar(activity);
//                break;
            case R.id.item_4s_shop:
//                activity.startActivity(new Intent(activity, FilterThirdShopActivity.class));
                ActivitySwitcherThirdPartShop.to4S(activity,0);
                break;
            case R.id.item_maintence:
                ActivitySwitchAutoInfo.toFlowMaintain(activity);
                break;
        }
        dismiss();
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }


    public interface OnStateChangeListener {
        void onStateChange(boolean isShow);
    }
}
