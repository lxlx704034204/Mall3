package com.hxqc.mall.fragment.auto;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ExpandableListView;

import com.hxqc.mall.control.AutoBrandDataControl;
import com.hxqc.mall.core.model.Brand;
import com.hxqc.mall.fragment.BaseTabFragment;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.OverlayDrawer;

import hxqc.mall.R;

/**
 * Author: HuJunJie
 * Date: 2015-03-16
 * FIXME Todo
 */
public class BrandMenuCoreFragment extends BaseTabFragment implements ExpandableListView.OnChildClickListener {
    OverlayDrawer mOverlayDrawer;
    AutoSeriesFragment mSeriesFragment;
    int itemCategory = 10;//类型 电动车/汽车
    AutoBrandMainFragment autoBrandMainFragment;

    public BrandMenuCoreFragment() {

    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_auto;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOverlayDrawer = (OverlayDrawer) view.findViewById(R.id.drawer);
        mOverlayDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN_MENUOPEN);
        mOverlayDrawer.setSidewardCloseMenu(false);
        mSeriesFragment = (AutoSeriesFragment) getChildFragmentManager().findFragmentById(
                R.id.auto_brand_series);
    }

    public void setItemCategory(int itemCategory) {
        this.itemCategory = itemCategory;
        autoBrandMainFragment = AutoBrandMainFragment.instantiate(itemCategory);
        autoBrandMainFragment.setChildClickListener(this);
        getChildFragmentManager().beginTransaction().add(R.id.mdContent, autoBrandMainFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                int childPosition, long id) {
        Brand brand = AutoBrandDataControl.getInstance().getBrandGroups(itemCategory).get(
                groupPosition).group.get(childPosition);
        showSeries(brand, itemCategory);
        return false;
    }

    /**
     * 显示车系
     *
     * @param brand
     */
    public void showSeries(Brand brand, int itemCategory) {
        if (!mOverlayDrawer.isMenuVisible()) mOverlayDrawer.openMenu();
        AutoBrandDataControl.getInstance().getSeriesGroup(getActivity(), brand, itemCategory, mSeriesFragment);
    }

    public void showBrandSeries(Brand brand, int itemCategory) {
        showSeries(brand, itemCategory);
        autoBrandMainFragment.so(brand);
    }

    @Override
    public String fragmentDescription() {
        return getResources().getString(R.string.title_activity_auto);
    }

    /**
     * 是否关闭菜单
     * @return
     */
    public boolean closeMenu() {
        if (mOverlayDrawer.isMenuVisible()){
            mOverlayDrawer.closeMenu();
            return true;
        }
        return false;

    }
}
