package com.hxqc.autonews.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.hxqc.autonews.adapter.MyCommentAdapter;
import com.hxqc.autonews.fragments.MessageCommentPagerFragment;
import com.hxqc.autonews.fragments.OrderCommentPagerFragment;
import com.hxqc.autonews.fragments.PublicCommentPagerFragment;
import com.hxqc.mall.activity.BackActivity;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 我的评价
 * Created by huangyi on 16/10/17.
 */
public class MyCommentActivity extends BackActivity {

    private static final String POSITION = "position";

    TabLayout mTabView;
    ViewPager mPagerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment);

        mTabView = (TabLayout) findViewById(R.id.comment_tab);
        mPagerView = (ViewPager) findViewById(R.id.comment_pager);

        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(new OrderCommentPagerFragment());
        mFragmentList.add(new PublicCommentPagerFragment());
        mFragmentList.add(new MessageCommentPagerFragment());
        ArrayList<String> mTitleList = new ArrayList<>();
        mTitleList.add("订单评价");
        mTitleList.add("口碑评价");
        mTitleList.add("资讯评价");
        mPagerView.setAdapter(new MyCommentAdapter(getSupportFragmentManager(), mFragmentList, mTitleList));

        //必须在ViewPager.setAdapter()之后调用
        mTabView.setupWithViewPager(mPagerView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, mTabView.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPagerView.setCurrentItem(savedInstanceState.getInt(POSITION));
    }

}
