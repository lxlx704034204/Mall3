package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Function: viewpager的adapter
 *
 * @author 袁秉勇
 * @since 2015年12月19日
 */
public class OrderViewPagerAdapter  extends FragmentPagerAdapter {

    private String tabTitles[] = new String[]{"电商自营", "网上4S店"};
    private Context context;
    private ArrayList<Fragment> fragments;

    public OrderViewPagerAdapter(Context context, FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
