package com.hxqc.mall.auto.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 17
 * FIXME
 * Todo
 */
@Deprecated
public class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragmentList;

    public MyFrageStatePagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
