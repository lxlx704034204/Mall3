package com.hxqc.newenergy.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import hxqc.mall.R;

/**
 * 说明:
 * author: 何玉
 * since: 2016/3/28.
 * Copyright:恒信汽车电子商务有限公司
 */
public class MyToolbar extends Toolbar {

    PopupWindow mPopupWindow;
    Menu_view  menu_view;
    LinearLayout   mTitle_Button;
    public MyToolbar(Context context) {
        super(context);
        inIt();
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inIt();
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inIt();
    }

    public void inIt(){

//        setTitle();

        inflate(getContext(), R.layout.view_toolbar_layout,this);
        mTitle_Button=(LinearLayout) findViewById(R.id.title_button);
        menu_view=new Menu_view(getContext());
        mPopupWindow =new PopupWindow(menu_view,300,300,true);


        mTitle_Button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.showAtLocation(MyToolbar.this, Gravity.CENTER, 0, 0);
            }
        });



    }

}
