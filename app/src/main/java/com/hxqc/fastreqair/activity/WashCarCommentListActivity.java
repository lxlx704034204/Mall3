package com.hxqc.fastreqair.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.hxqc.fastreqair.fragment.WashCarCommentListFragment;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.util.ActivitySwitchBase;

import hxqc.mall.R;

/**
 * @Author : 钟学东
 * @Since : 2016-05-25
 * FIXME
 * Todo 洗车评论列表activity
 */
public class WashCarCommentListActivity extends BackActivity {

    private WashCarCommentListFragment commentListFragment;
    private String shopID;
    @SuppressWarnings("ValidFragment")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wash_car_comment_list);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.toolbar_background)));
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(ActivitySwitchBase.KEY_DATA);
        shopID = bundle.getString("shopID");
        commentListFragment = new WashCarCommentListFragment(shopID,this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl,commentListFragment).commit();
    }
}
