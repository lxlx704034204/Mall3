package com.hxqc.mall.thirdshop.activity;

import android.support.v4.app.Fragment;

import com.hxqc.mall.activity.SingleFragmentActivity;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.thirdshop.fragment.NewCarModelListFragment;
import com.hxqc.mall.thirdshop.model.Series;

/**
 * Author:李烽
 * Date:2016-05-17
 * FIXME
 * Todo 新车销售车型列表
 */
public class NewCarModelListActivity extends SingleFragmentActivity {
    public static final String BRAND = "NewCarSeriesListFragment.brand";
    public static final String SERIES = "NewCarSeriesListFragment.series";
    public static final String AREAID = "NewCarSeriesListFragment.siteID";
    public static final String FROM4SMENU = "NewCarSeriesListFragment.from4Smenu";
    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_car_sale_list);
//        initView();
//    }

    @Override
    protected Fragment pushFragment() {
        NewCarModelListFragment newCarModelListFragment = NewCarModelListFragment.newInstance(getAreaID(),getBrandName(), getSeries(),isFromFourS());
        return newCarModelListFragment;
    }



//    private void initView() {
//        NewCarModelListFragment newCarModelListFragment = NewCarModelListFragment.newInstance(getAreaID(), getSeries());
//        getSupportFragmentManager().beginTransaction().add(R.id.root_layout, newCarModelListFragment)
//                .show(newCarModelListFragment).commit();
//
//    }


    @Override
    protected void onStart() {
        super.onStart();
        setTitle(getSeries().getSeriesName());
    }

    protected String getAreaID() {
        return getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(AREAID);
    }
    private String getBrandName() {
        return  getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(BRAND);
    }
    protected Series getSeries() {
        return getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getParcelable(SERIES);
    }

    protected boolean isFromFourS(){
        return getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getBoolean(FROM4SMENU);
    }
}
