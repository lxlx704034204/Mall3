package com.hxqc.mall.fragment.auto;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.hxqc.mall.control.AutoBrandDataControl;
import com.hxqc.mall.core.model.Brand;
import com.hxqc.mall.core.model.BrandGroup;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.views.SideBar;

import java.util.List;

import hxqc.mall.R;

/**
 * 新车销售品牌
 */
public class AutoBrandMainFragment extends AutoBrandFragment implements AutoBrandDataControl.BrandHandler,
        ExpandableListView.OnChildClickListener {
    //    HotBrandView mHotBrandView;
    SideBar sideBarView;

    public static AutoBrandMainFragment instantiate(int itemCategory) {
        AutoBrandMainFragment autoBrandFragment = new AutoBrandMainFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(AutoItem.ItemCategory, itemCategory);
        autoBrandFragment.setArguments(bundle);
        return autoBrandFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mHotBrandView = new HotBrandView(getActivity());
//        mHotBrandView.setOnBrandClickListener(mOnBrandClickListener);
//        mExpandableListView.addHeaderView(mHotBrandView);
        ImageView heardView = new ImageView(mContext);
        heardView.setBackgroundResource(R.drawable.pic_list_title);
        mExpandableListView.addHeaderView(heardView);

        sideBarView = (SideBar) view.findViewById(R.id.SideBar);
        sideBarView.setOnTouchingLetterChangedListener(
                new SideBar.OnTouchingLetterChangedListener() {
                    @Override
                    public void onTouchingLetterChanged(int index, String s, StringBuffer s1) {
                        if (index == 0) {
                            mExpandableListView.smoothScrollToPosition(0);
                        } else {
                            mExpandableListView.setSelectedGroup(index - 1);
                        }

                    }

                });
    }

    @Override
    public void onSucceed(List< BrandGroup > brandGroups) {
        super.onSucceed(brandGroups);
//        mHotBrandView.setBrandGroup(mBrandDataProvider.getHotBrandGroup());
        initSideTag(brandGroups);
        if (brand != null) {
            aa(brandGroups);
        }
    }

    /**
     * 创建边侧导航
     */
    private void initSideTag(List< BrandGroup > brandGroups) {//
        String[] tag = new String[brandGroups.size()];
        for (int i = 0; i < brandGroups.size(); i++) {
            tag[i] = brandGroups.get(i).groupTag;
        }
        sideBarView.setSideTag(tag);
    }

    @Override
    public String fragmentDescription() {
        return "品牌";
    }

//   /* //策划菜单栏滑动状态
//    MenuDrawer.OnDrawerStateChangeListener mOnDrawerStateChangeListener = new MenuDrawer.OnDrawerStateChangeListener() {
//        int state;
//        int oldPixels = 0;
//
//        @Override
//        public void onDrawerStateChange(int oldState, int newState) {
//            this.state = newState;
//            if (oldState == 1 && newState == 8) {
//                if (mHotBrandAnimation != null) mHotBrandAnimation.openMenu(false);
//            }
//            if (oldState == 4 && newState == 0) {
//                if (mHotBrandAnimation != null) {
////                    mHotBrandView.setmHotBrandAnimation(null);
//                    mHotBrandAnimation = null;
//                }
//
//            }
//
//        }
//
//        @Override
//        public void onDrawerSlide(float openRatio, int offsetPixels) {
//            if (mHotBrandAnimation != null && (state == 2 || state == 4)) {
//                if (oldPixels == 0) {
//                    oldPixels = offsetPixels;
//                }
//                mHotBrandAnimation.moveMenu(openRatio, offsetPixels, oldPixels - offsetPixels);
//                oldPixels = offsetPixels;
//            }
//        }
//    };*/
//    public MenuDrawer.OnDrawerStateChangeListener getOnDrawerStateChangeListener() {
//        return mOnDrawerStateChangeListener;
//    }
//    HotBrandView.OnBrandClickListener mOnBrandClickListener = new HotBrandView.OnBrandClickListener() {
//        @Override
//        public void brandClick(HotBrandView hotBrandView, View rootView, View clickView,
//                               Brand brand) {
//            MainAutoFragment mainAutoFragment = (MainAutoFragment) getParentFragment();
//            OverlayDrawer mOverlayDrawer = mainAutoFragment.mOverlayDrawer;
//            if (!mOverlayDrawer.isMenuVisible()) {
//                mOverlayDrawer.openMenu();
//                int blankWidth = mOverlayDrawer.getWidth() - mOverlayDrawer.getMenuSize();//菜单距离屏幕边界的距离
//                mHotBrandAnimation = hotBrandView.getmHotBrandAnimation(rootView, clickView,
//                        blankWidth);
//                mHotBrandAnimation.openMenu(true);
//                mainAutoFragment.showSeries(brand);
//            }
//        }
//
//    };
//    public HotBrandView.HotBrandAnimation mHotBrandAnimation;

    Brand brand;

    public void so(final Brand brand) {
        this.brand = brand;
    }

    /**
     * 计算位置
     *
     * @param brandGroups
     */
    public void aa(List< BrandGroup > brandGroups) {
        int groupCount = 0;
        int chileCount;
        for (BrandGroup brandGroup : brandGroups) {
            ++groupCount;
            chileCount = 0;
            for (Brand brand1 : brandGroup.group) {
                ++chileCount;
                if (brand.equals(brand1)) {
                    int groupNum = groupCount - 2;
                    if (groupNum < 0) {
                        groupNum = 1;
                    }
                    mExpandableListView.setSelectedChild(groupNum, chileCount, true);
                    return;
                }
            }
        }
    }




}
