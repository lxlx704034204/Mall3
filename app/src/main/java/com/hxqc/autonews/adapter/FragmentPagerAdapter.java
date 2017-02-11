package com.hxqc.autonews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hxqc.mall.core.fragment.FunctionFragment;

import java.util.List;

/**
 * Author:李烽
 * Date:2016-09-30
 * FIXME
 * Todo 汽车资讯分类栏目的viewpager适配器
 */

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {
    private List<FunctionFragment> fragments;
    private String[] titles;

    public FragmentPagerAdapter(FragmentManager fm, List<FunctionFragment> functionFragments) {
        super(fm);
        fragments = functionFragments;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && titles.length > position) {
            return titles[position];
        } else
            return super.getPageTitle(position);
    }
}
