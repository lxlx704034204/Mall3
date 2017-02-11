package com.hxqc.mall.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hxqc.mall.core.R;

/**
 * Author:李烽
 * Date:2016-05-18
 * FIXME
 * Todo 单纯加一个fragment的activity
 */
public abstract class SingleFragmentActivity extends BackActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        Fragment fragment = pushFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.root_layout, fragment)
                .show(fragment).commit();
    }
    protected abstract Fragment pushFragment();
}
