package com.hxqc.mall.thirdshop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hxqc.mall.auto.model.Filter;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.thirdshop.constant.FilterResultKey;
import com.hxqc.mall.thirdshop.fragment.FlashSaleListFragment;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author:李烽
 * Date:2016-05-05
 * FIXME
 * Todo 限时促销车辆列表
 */
public class FlashSaleListActivity extends ActionBarFilterActivity {

    public static final String AREAID = "ShopFlashSaleListActivity.siteID";
    private static final int REQUEST_CODE = 0x100;
    public static final java.lang.String IS_INDEX_API = "ShopFlashSaleListActivity.isIndexApi";


    private ArrayList<Filter> filters = new ArrayList<>();

    private String brand, serie, model;
    private FlashSaleListFragment flashSaleListFragment;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_flash_sale);
////        initView();
//    }

    @Override
    protected Fragment pushFragment() {
        flashSaleListFragment = FlashSaleListFragment.newInstance(getAreaID(),getIsIndexApi());
        return flashSaleListFragment;
    }

//    private void initView() {
//        flashSaleListFragment = FlashSaleListFragment.newInstance(getAreaID());
//        getSupportFragmentManager().beginTransaction().add(R.id.root_layout, flashSaleListFragment)
//                .show(flashSaleListFragment).commit();
//    }


    @Override
    protected String getAreaID() {
        return getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(AREAID);
    }
    protected boolean getIsIndexApi(){
        return getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getBoolean(IS_INDEX_API);
    }
    @Override
    protected void toFilter() {
        ActivitySwitcherThirdPartShop.toFilterFlashSaleCar(this, getAreaID(), REQUEST_CODE);
//        startActivityForResult(new Intent(this, FilterSeckillIndefiniteActivity.class)
//                .putExtra(FilterSeckillIndefiniteActivity.SITEID, getAreaID()), REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null) {
                Bundle bundleExtra = data.getBundleExtra(FilterResultKey.PARAMS);
                if (bundleExtra != null) {
                    HashMap<String, String> map =
                            (HashMap<String, String>) bundleExtra.getSerializable(FilterResultKey.KEY_VALUE);
                    flashSaleListFragment.upDate(map);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
