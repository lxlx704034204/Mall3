package com.hxqc.mall.thirdshop.activity;

import android.support.v4.app.Fragment;

import com.hxqc.mall.activity.SingleFragmentActivity;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.thirdshop.fragment.NewCarSeriesListFragment;

/**
 * Author:李烽
 * Date:2016-05-05
 * FIXME
 * Todo 新车销售车系列表
 */
public class NewCarSeriesListActivity extends SingleFragmentActivity {
    public static final String SERIES = "NewCarSeriesListFragment.series";
    public static final String AREAID = "NewCarSeriesListFragment.siteID";

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_car_sale_list);
//        initView();
//    }

    @Override
    protected Fragment pushFragment() {
        NewCarSeriesListFragment newCarSaleListFragment = NewCarSeriesListFragment.newInstance(getAreaID());
        return newCarSaleListFragment;
    }

//    private void initView() {
//
//        getSupportFragmentManager().beginTransaction().add(R.id.root_layout, newCarSaleListFragment)
//                .show(newCarSaleListFragment).commit();
//    }

    protected String getAreaID() {
        return getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(AREAID);
    }

}
