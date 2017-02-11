package com.hxqc.carcompare.ui.discuss;

import android.os.Bundle;

import com.hxqc.mall.activity.BackActivity;

import hxqc.mall.R;

/**
 * 口碑评价
 * Created by zhaofan on 16/11/1.
 */
public class UserDiscussActivity extends BackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_comment);

        getSupportFragmentManager().beginTransaction().replace(R.id.comment_list,
                DiscussFragment.newInstance(getIntent().getStringExtra("extID"),
                        getIntent().getStringExtra("brand"),
                        getIntent().getStringExtra("series")))
                .commit();
    }

}
