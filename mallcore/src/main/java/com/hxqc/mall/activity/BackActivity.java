package com.hxqc.mall.activity;

import android.os.Build;
import android.os.Bundle;


/**
 * 说明:左上角有返回键的Activity
 * <p>
 * author: 吕飞
 * since: 2015-03-09
 * Copyright:恒信汽车电子商务有限公司
 */
public class BackActivity extends NoBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


    protected void initActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //以下代码用于去除阴影
        if (Build.VERSION.SDK_INT >= 21) {
            getSupportActionBar().setElevation(0);
        }
    }
}

