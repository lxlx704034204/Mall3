package com.hxqc.mall.activity.auto;

import android.os.Bundle;

import com.hxqc.mall.activity.AppBackActivity;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.fragment.auto.SpecialOfferForRecyclerFragment;

import hxqc.mall.R;

/**
 * 说明:特卖
 * <p/>
 * author: 吕飞
 * since: 2015-06-18
 * Copyright:恒信汽车电子商务有限公司
 */
public class SpecialOfferActivity extends AppBackActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_has_fragment);
        SpecialOfferForRecyclerFragment mUserOrderFragment =
                SpecialOfferForRecyclerFragment.newInstance(getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(AutoItem.ItemCategory));
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mUserOrderFragment).commit();
    }


}
