package com.hxqc.mall.thirdshop.views.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by CPR113 on 2015/12/28.
 */
public class ShopDetailsViewPageAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mListFragments;
    private List< String > mTAB;
    public ShopDetailsViewPageAdapter(FragmentManager fm, List<Fragment> listFragments,List<String> TAB) {
        super(fm);
        this.mListFragments=listFragments;
        this.mTAB=TAB;
    }

    @Override
    public Fragment getItem(int position) {
        return mListFragments.get(position);
    }

    @Override
    public int getCount() {
        return mListFragments.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {
        return mTAB.get(position);
    }



}
