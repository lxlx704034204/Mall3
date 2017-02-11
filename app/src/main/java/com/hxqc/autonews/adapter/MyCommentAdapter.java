package com.hxqc.autonews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 我的评价 ViewPager adapter
 * Created by huangyi on 15/12/25.
 */
public class MyCommentAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> mFragmentList;
    ArrayList<String> mTitleList;

    public MyCommentAdapter(FragmentManager manager, ArrayList<Fragment> mFragmentList, ArrayList<String> mTitleList) {
        super(manager);
        this.mFragmentList = mFragmentList;
        this.mTitleList = mTitleList;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //防止频繁的销毁视图
        //super.destroyItem(container, position, object);
    }

}
