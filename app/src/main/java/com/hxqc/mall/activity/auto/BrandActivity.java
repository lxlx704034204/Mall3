package com.hxqc.mall.activity.auto;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hxqc.mall.activity.AppBackActivity;
import com.hxqc.mall.control.AutoBrandDataControl;
import com.hxqc.mall.core.model.Brand;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.fragment.auto.BrandMenuCoreFragment;

import hxqc.mall.R;

/**
 * Author: HuJunJie
 * Date: 2015-06-18
 * FIXME
 * Todo 品牌列表
 * Edit 2016年1月13日 改做新车特卖品牌选择
 */
public class BrandActivity extends AppBackActivity {

    private static int itemCategory = 10;
    protected BrandMenuCoreFragment brandMenuCoreFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand);
                brandMenuCoreFragment =
                (BrandMenuCoreFragment) getSupportFragmentManager().findFragmentById(R.id.main_auto_fragment);
        prepareView();
    }

    protected void prepareView() {
        if (getIntent().hasExtra(ActivitySwitchBase.KEY_DATA)) {
            Bundle bundle = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);

            itemCategory = Integer.valueOf(bundle.
                    getString(AutoItem.ItemCategory, "10"));

            brandMenuCoreFragment.setItemCategory(itemCategory);
            switch (itemCategory) {
                case AutoItem.CATEGORY_AUTOMOBILE:
                    setTitle(getString(R.string.title_activity_brand));
                    break;
                case AutoItem.CATEGORY_NEW_ENERGY:
                    setTitle("新能源电动车");
                    break;
            }

            Brand brand = bundle.getParcelable("brand");
            if (brand != null) {
                brandMenuCoreFragment.showBrandSeries(brand, itemCategory);
            }
        } else {
            //toBrand(Context context) 跳入
            brandMenuCoreFragment.setItemCategory(itemCategory);
        }
    }

    @Override
    public void onBackPressed() {
        if (!brandMenuCoreFragment.closeMenu()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_brand_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_message:
                ActivitySwitcher.toMyMessageActivity(this);
                break;
            case R.id.action_brand_filter:
                //按条件找车
                String itemCategory = "10";//默认新车
                if (getIntent().hasExtra(ActivitySwitchBase.KEY_DATA)) {
                    itemCategory = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString(AutoItem.ItemCategory);
                }
                ActivitySwitcher.toAutoFilter(this, itemCategory);
                break;
        }
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        AutoBrandDataControl.getInstance().clearTempValue();
    }
}
