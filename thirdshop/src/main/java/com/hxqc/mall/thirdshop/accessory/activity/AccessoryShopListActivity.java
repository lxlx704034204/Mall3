package com.hxqc.mall.thirdshop.accessory.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.fragment.AccessoryShopListFragment;
import com.hxqc.mall.thirdshop.accessory.utils.ActivitySwitcherAccessory;
/**
 * 说明:用品店铺列表
 *
 * @author: 吕飞
 * @since: 2016-05-30
 * Copyright:恒信汽车电子商务有限公司
 */
public class AccessoryShopListActivity extends NoBackActivity {
    TextView mToMapView;
    FragmentManager mFragmentManager;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessory_shop_list);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("配送门店");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(mToolbar);
        mToMapView = (TextView) findViewById(R.id.to_map);
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.fragment_container, new AccessoryShopListFragment());
        mFragmentTransaction.commit();
        mToMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherAccessory.toMapList(AccessoryShopListActivity.this);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent();
            intent.putExtra(AccessoryShopListFragment.CHOOSE_SHOP_ID, data.getStringExtra(AccessoryShopListFragment.CHOOSE_SHOP_ID));
            intent.putExtra(AccessoryShopListFragment.CHOOSE_SHOP_NAME,data.getStringExtra(AccessoryShopListFragment.CHOOSE_SHOP_NAME));
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }
}
