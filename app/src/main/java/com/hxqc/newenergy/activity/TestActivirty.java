package com.hxqc.newenergy.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
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

import com.hxqc.newenergy.view.Menu_view;

import hxqc.mall.R;

import static hxqc.mall.R.id.toolbar;

/**
 * 说明:
 * author: 何玉
 * since: 2016/3/28.
 * Copyright:恒信汽车电子商务有限公司
 */
public class TestActivirty extends AppCompatActivity {


    TextView mTitle;
    ImageView mTitle_ico;
    LinearLayout mTitle_Button;
    Toolbar mToolBar;

    ImageView mHome_Button;

    PopupWindow mPopupWindow;
    Menu_view mMenu_view;
    private static final int DURATION = 400;

    private float DEGREES = 180;


    private ValueAnimator animator;
    private RotateAnimation rotateAnimator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplication().setTheme(R.style.Theme_ReactNative_AppCompat_Light);
        setContentView(R.layout.activity_test);
        toolbarInit();


    }

    public void toolbarInit() {

        mTitle_Button = (LinearLayout) findViewById(R.id.title_button);
        mTitle = (TextView) findViewById(R.id.title_content);
        mTitle_ico = (ImageView) findViewById(R.id.title_ico);
        mHome_Button = (ImageView) findViewById(R.id.home_button);


        mToolBar = (Toolbar) findViewById(toolbar);
        mMenu_view=(Menu_view)findViewById(R.id.mymenu);
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setTitle("");

//        setSupportActionBar(mToolBar);

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        mTitle.setText(getTitle());

        mTitle_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMenu_view.isShown()){

                    collapse();
                }else {
                    openmenu();
                }




            }
        });

        mHome_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TestActivirty.this,Ev_NewEnergyActivity.class));
            }
        });


    }

    /**
     * Collapse animation to hide the discoverable content.
     */
    public void collapse() {
        int finalHeight = mMenu_view.getHeight();


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

    public void  openmenu(){

        //set Visible
        mMenu_view.setVisibility(View.VISIBLE);


        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mMenu_view.measure(widthSpec, heightSpec);
        animator = slideAnimator(0, mMenu_view.getMeasuredHeight());
        animator.start();
    }

    private ValueAnimator slideAnimator(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(DURATION);

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
}
