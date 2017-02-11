package com.hxqc.newenergy.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.newenergy.view.Menu_view;
import com.hxqc.util.DebugLog;

import hxqc.mall.R;

import static hxqc.mall.R.id.toolbar;

/**
 * 说明:
 * author: 何玉
 * since: 2016/3/28.
 * Copyright:恒信汽车电子商务有限公司
 */
public class ToolBarActivity extends AppCompatActivity {

    TextView mTitle;
    ImageView mTitle_ico;
    LinearLayout mTitle_Button;
    Toolbar mToolBar;

    ImageView mHome_Button;

    PopupWindow mPopupWindow;
    Menu_view mMenu_view;

    boolean isFinish;

    private float DEGREES = 90;
    private ValueAnimator animator;
    private RotateAnimation rotateAnimator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplication().setTheme(R.style.Theme_ReactNative_AppCompat_Light);
        setContentView(R.layout.activity_oolbar);
        toolbarInit();


    }

    public void toolbarInit() {

        mTitle_Button = (LinearLayout) findViewById(R.id.title_button);
        mTitle = (TextView) findViewById(R.id.title_content);
        mTitle_ico = (ImageView) findViewById(R.id.title_ico);
        mHome_Button = (ImageView) findViewById(R.id.home_button);

        mToolBar = (Toolbar) findViewById(toolbar);
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(ToolBarActivity.this instanceof Ev_NewsWebActivity)) {
                    finish();
                }
            }
        });

        mTitle.setText(getTitle());

        mMenu_view = (Menu_view) findViewById(R.id.mymenu);

        mTitle_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMenu_view.isShown()) {
                    collapse();
                } else {
                    openmenu();
                }
            }
        });

        mHome_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DebugLog.i("ev_vt", "home");
                ActivitySwitchBase.toMain(ToolBarActivity.this, 0);
            }
        });

    }


    /**
     * Collapse animation to hide the discoverable content.
     */
    public void collapse() {
        int finalHeight = mMenu_view.getHeight();
        mTitle_ico.setBackgroundResource(R.drawable.toolbar_top);
//
//        int x = mTitle_ico.getMeasuredWidth() / 2;
//        int y = mTitle_ico.getMeasuredHeight() / 2;
//
//        rotateAnimator = new RotateAnimation(0f, -DEGREES, x, y);
//        rotateAnimator.setInterpolator(new LinearInterpolator());
//        rotateAnimator.setRepeatCount(Animation.ABSOLUTE);
//        rotateAnimator.setFillAfter(true);
//        rotateAnimator.setDuration(500);
//        rotateAnimator.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                mTitle_ico.setBackgroundResource(R.drawable.toolbar_top);
//            }
//
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//            }
//
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        mTitle_ico.startAnimation(rotateAnimator);

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                mMenu_view.setVisibility(View.GONE);
            }


            @Override
            public void onAnimationStart(Animator animator) {

            }


            @Override
            public void onAnimationCancel(Animator animator) {
            }


            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }

    public void openmenu() {

        //set Visible
        mMenu_view.setVisibility(View.VISIBLE);
        mTitle_ico.setBackgroundResource(R.drawable.toolbar_down);
//        int x = mTitle_ico.getMeasuredWidth() / 2;
//        int y = mTitle_ico.getMeasuredHeight() / 2;
//
//        rotateAnimator = new RotateAnimation(DEGREES, 0f, x, y);
//        rotateAnimator.setInterpolator(new LinearInterpolator());
//        rotateAnimator.setRepeatCount(Animation.ABSOLUTE);
//        rotateAnimator.setFillAfter(true);
//        rotateAnimator.setDuration(500);
//        rotateAnimator.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                mTitle_ico.setBackgroundResource(R.drawable.toolbar_down);
//            }
//
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        mTitle_ico.startAnimation(rotateAnimator);


        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mMenu_view.measure(widthSpec, heightSpec);
        animator = slideAnimator(0, mMenu_view.getMeasuredHeight());
        animator.start();
    }

    private ValueAnimator slideAnimator(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(500);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = mMenu_view.getLayoutParams();
                layoutParams.height = value;

                mMenu_view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    public void closePopWindowMenu() {
        collapse();
    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public void setContentView( int layoutResID) {
//        super.setContentView(layoutResID);
//        mToolBarHelper = new ToolBarHelper(this,layoutResID) ;
//        mToolbar = mToolBarHelper.getToolBar() ;
//        setContentView(mToolBarHelper.getContentView()); /*把 toolbar 设置到Activity 中*/
//        setSupportActionBar(mToolbar); /*自定义的一些操作*/
//        onCreateCustomToolBar(mToolbar) ;
//    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onCreateCustomToolBar(Toolbar toolbar) {
        toolbar.setContentInsetsRelative(0, 0);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
