package com.hxqc.mall.thirdshop.activity;

import android.support.v4.app.Fragment;

import com.hxqc.mall.activity.SingleFragmentActivity;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.thirdshop.fragment.SiteNewsListFragment;

/**
 * Author:李烽
 * Date:2016-05-13
 * FIXME
 * Todo 资讯列表
 */
public class SiteNewsListActivity extends SingleFragmentActivity {
    public static final String SITE_ID = "site_id";
    public String siteID;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        siteID = getIntent().getStringExtra(SITE_ID);
//        setContentView(R.layout.activity_site_news_list);
////        setContentView(frameLayout);
//        SiteNewsListFragment siteNewsListFragment = SiteNewsListFragment.newInstance(siteID);
//        getSupportFragmentManager().beginTransaction().add(R.id.root_layout, siteNewsListFragment)
////        getSupportFragmentManager().beginTransaction().add(siteNewsListFragment, "")
//                .show(siteNewsListFragment).commit();
//    }

    @Override
    protected Fragment pushFragment() {
        SiteNewsListFragment siteNewsListFragment = SiteNewsListFragment.newInstance(getAreaID());
        return siteNewsListFragment;
    }

    protected String getAreaID() {
        return getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(SITE_ID);
    }
}
