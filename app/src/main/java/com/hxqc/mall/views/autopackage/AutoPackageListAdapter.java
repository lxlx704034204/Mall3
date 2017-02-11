package com.hxqc.mall.views.autopackage;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2015-11-19
 * FIXME
 * Todo 套餐包滑动界面
 */
public class AutoPackageListAdapter extends PagerAdapter {

    private ArrayList<View> views;

    public AutoPackageListAdapter(ArrayList<View> views) {
        this.views = views;
    }

    public final String viewTag = "view_tag";

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        container.addView(views.get(position));
        views.get(position).setTag(viewTag + position);
        return views.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }
}
