package com.hxqc.pay.activity;

import android.os.Bundle;

import com.hxqc.mall.activity.BackActivity;

import hxqc.mall.R;

public class PickCarHelpActivity extends BackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_car_help);
        //手势关闭
//        SlidrConfig config = new SlidrConfig.Builder()
//                .primaryColor(getResources().getColor(R.color.toast_yellow))
//                .sensitivity(1f).build();
//        Slidr.attach(this, config);
        getSupportActionBar().setTitle("如何提车");
    }

}
