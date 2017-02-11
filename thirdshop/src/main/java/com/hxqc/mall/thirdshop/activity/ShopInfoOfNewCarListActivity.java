package com.hxqc.mall.thirdshop.activity;

import android.support.v4.app.Fragment;

import com.hxqc.mall.activity.SingleFragmentActivity;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.thirdshop.fragment.ShopInfoOfNewCarListFragment;

/**
 * Author:李烽
 * Date:2016-05-18
 * FIXME
 * Todo 店铺列表（已经没有用这个）
 */
public class ShopInfoOfNewCarListActivity extends SingleFragmentActivity {
    public static final String SERIES = "ShopInfoOfNewCarListActivity.series";
    public static final String AREAID = "ShopInfoOfNewCarListActivity.siteID";
    public static final String MODEL = "ShopInfoOfNewCarListActivity.model";

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(getModel());
    }

    @Override
    protected Fragment pushFragment() {
        ShopInfoOfNewCarListFragment listFragment
                = ShopInfoOfNewCarListFragment.newInstance(getSiteID(), getSeries(), getModel());
        return listFragment;
    }

    private String getSiteID() {
        return getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(AREAID);
    }

    protected String getSeries() {
        return getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(SERIES);
    }

    private String getModel() {
        return getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(MODEL);
    }

}
