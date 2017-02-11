package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Function: 地图上搜店页面中ViewPager的Adapter
 *
 * @author 袁秉勇
 * @since 2016年06月20日
 */
public abstract class BaseMapViewPagerAdapter extends PagerAdapter {
    private final static String TAG = BaseMapViewPagerAdapter.class.getSimpleName();
    private Context mContext;

    protected BaseMapListAdapter.ClickCallBack clickCallBack;


    public void setClickCallBack(BaseMapListAdapter.ClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }


    protected int selectedPosition = -1;

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
