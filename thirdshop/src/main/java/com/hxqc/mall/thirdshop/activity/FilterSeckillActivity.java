package com.hxqc.mall.thirdshop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.constant.FilterResultKey;
import com.hxqc.mall.thirdshop.control.TFilterController;
import com.hxqc.mall.thirdshop.activity.auto.fragment.FilterThirdShopCoreFragment_2;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.OverlayDrawer;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-05-05
 * FIXME
 * Todo 筛选特卖车界面（无不定项）
 */
public class FilterSeckillActivity extends BackActivity implements
        FilterThirdShopCoreFragment_2.FilterThirdShopCoreFragmentCallBack
        , FilterThirdShopCoreFragment_2.FilterMenuListener {

    private OverlayDrawer mOverlayDrawer;
    private Button filterResponseBtn;

    public String areaID = "";//分站id

    FilterThirdShopCoreFragment_2 filterThirdShopCoreFragment_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        mOverlayDrawer = (OverlayDrawer) findViewById(R.id.drawer);
        mOverlayDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
        mOverlayDrawer.setSidewardCloseMenu(false);

        areaID = getIntent().getStringExtra(FlashSaleListActivity.AREAID);

        filterResponseBtn = (Button) findViewById(R.id.filter_response);

        filterController = TFilterController.getInstance().setAreaID(areaID).setType(TFilterController.TYPE_SECKILL);

        filterThirdShopCoreFragment_2 = new FilterThirdShopCoreFragment_2();

        filterThirdShopCoreFragment_2.setFilterThirdShopCoreFragmentCallBack(this);

        getSupportFragmentManager().beginTransaction().add(R.id.collapse_view, filterThirdShopCoreFragment_2).commit();

    }


    public void toAutoList(View view) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FilterResultKey.KEY_VALUE, filterController.mFilterMap);
        intent.putExtra(FilterResultKey.PARAMS, bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    TFilterController filterController;

    @Override
    public void onCallBack(String key, String value) {
        if (!TextUtils.isEmpty(value)) filterController.putValue(key, value);
        switch (key) {

            case "brand":

                break;
            case "serie":

                break;
            case "model":

                break;
        }
    }

    ArrayList<Fragment> fragments = new ArrayList<>();
    public Fragment lastFragment = new Fragment();

    @Override
    public void showFilterFactor(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!fragments.contains(fragment)) {
            fragments.add(fragment);
            transaction.add(R.id.mdMenu, fragment);
            if (fragments.size() > 0 && lastFragment != fragment) {
                transaction.hide(lastFragment);
            }
        } else {
            if (lastFragment != fragment) transaction.hide(lastFragment).show(fragment);
        }
        transaction.commitAllowingStateLoss();
        lastFragment = fragment;
        mOverlayDrawer.openMenu();
    }

    @Override
    public void closeFilterFactor() {
        if (mOverlayDrawer.isMenuVisible()) mOverlayDrawer.closeMenu();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        filterController.destroy();
    }
}
