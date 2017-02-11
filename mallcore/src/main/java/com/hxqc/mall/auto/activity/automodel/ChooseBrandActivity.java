package com.hxqc.mall.auto.activity.automodel;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.auto.activity.automodel.fragment.ThirdShopBrandFragment;
import com.hxqc.mall.auto.activity.automodel.fragment.ThirdShopSeriesFragment;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoTypeControl;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.Brand;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.model.SeriesGroup;
import com.hxqc.mall.auto.util.ActivitySwitchAutoInfo;
import com.hxqc.mall.auto.util.ActivityUtil;
import com.hxqc.mall.core.R;
import com.hxqc.util.DebugLog;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.OverlayDrawer;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 18
 * FIXME
 * Todo 选择品牌
 */
public class ChooseBrandActivity extends BackActivity {

    private static final String TAG = AutoInfoContants.LOG_J;
    private OverlayDrawer mOverlayDrawer;
    private ThirdShopBrandFragment mBrandFragment;
    private ThirdShopSeriesFragment mSeriesFragment;
    private MyAuto hmMyAuto;
    private int flagActivity = -1;
    private String shopID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_third_shop_brand);

        initView();

        initData();

    }

    private void initData() {
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra(ActivitySwitchAutoInfo.KEY_DATA);
            if (bundle != null) {
                hmMyAuto = bundle.getParcelable("myAuto");
                flagActivity = bundle.getInt("flagActivity");
                shopID = bundle.getString("shopID", "");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        initEvent();

    }

    private void initView() {
        mOverlayDrawer = (OverlayDrawer) findViewById(R.id.drawer);
        mOverlayDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN_MENUOPEN);
        mOverlayDrawer.setSidewardCloseMenu(false);
        mBrandFragment = (ThirdShopBrandFragment) getSupportFragmentManager().findFragmentById(R.id.third_shop_brand);

        mSeriesFragment = (ThirdShopSeriesFragment) getSupportFragmentManager().findFragmentById(R.id.third_shop_series);

    }

    private void initEvent() {
        mBrandFragment.setChildClickListener(brandItemClickListener);
        mSeriesFragment.setChildClickListener(seriesItemClickListener);
        if (hmMyAuto == null) {
            hmMyAuto = new MyAuto();
        }
    }

    private ExpandableListView.OnChildClickListener brandItemClickListener = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            ArrayList<BrandGroup> brandGroups = mBrandFragment.getBrandGroups();
            BrandGroup brandGroup = brandGroups.get(groupPosition);
            Brand brand = brandGroup.group.get(childPosition);
            showSeries(brand);
            return false;
        }
    };

    private ExpandableListView.OnChildClickListener seriesItemClickListener = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            hmMyAuto.series = mSeriesFragment.getSeriesGroups().get(groupPosition).series.get(childPosition).seriesName;
            hmMyAuto.seriesID = mSeriesFragment.getSeriesGroups().get(groupPosition).series.get(childPosition).seriesID;
            hmMyAuto.brandName = mSeriesFragment.getSeriesGroups().get(groupPosition).brandName;
            DebugLog.i(TAG, hmMyAuto.toString());
            ActivitySwitchAutoInfo.toChooseAutoModelActivity(ChooseBrandActivity.this, hmMyAuto, shopID, flagActivity);
            if (flagActivity != AutoInfoContants.HOME_PAGE || flagActivity != AutoInfoContants.FLAG_ACTIVITY_MAINTAIN_SHOP_QUOTE || flagActivity != AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_LIST || flagActivity != AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_REPAIR_DETAIL || flagActivity != AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_4S || flagActivity != AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_AUTO_DETAIL) {
                ActivityUtil.getInstance().addActivity(ChooseBrandActivity.this);
            }
            return false;
        }
    };

    private void showSeries(Brand brand) {
        if (!mOverlayDrawer.isMenuVisible()) mOverlayDrawer.openMenu();
        hmMyAuto.brand = brand.brandName;
        hmMyAuto.brandID = brand.brandID;
        hmMyAuto.brandThumb = brand.brandThumb;
        DebugLog.i(TAG, hmMyAuto.toString());
        AutoTypeControl.getInstance().requestSeries(this, shopID, brand.brandName, brand.brandID, seriesCallBack);
    }

    private CallBackControl.CallBack<ArrayList<SeriesGroup>> seriesCallBack = new CallBackControl.CallBack<ArrayList<SeriesGroup>>() {
        @Override
        public void onSuccess(ArrayList<SeriesGroup> response) {
            mSeriesFragment.showSeries(response);
        }

        @Override
        public void onFailed(boolean offLine) {
            mSeriesFragment.showEmptySeries();
        }
    };

}
