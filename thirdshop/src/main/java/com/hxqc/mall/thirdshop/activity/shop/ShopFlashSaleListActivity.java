package com.hxqc.mall.thirdshop.activity.shop;

import android.os.Bundle;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.control.ShopDetailsController;
import com.hxqc.mall.thirdshop.fragment.ShopFlashSaleListFragment_1;
import com.hxqc.mall.thirdshop.model.ThirdPartShop;
import com.hxqc.mall.thirdshop.views.CallBar;
import com.hxqc.mall.thirdshop.views.ShopDetailsHeadView;

/**
 * Author:李烽
 * Date:2016-05-09
 * FIXME
 * Todo 店铺特价车模块
 */
public class ShopFlashSaleListActivity extends BaseShopDetailsActivity implements ShopDetailsController.ThirdPartShopHandler {
    private CallBar callBar;

    private ShopFlashSaleListFragment_1 mFlashSaleListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_flash_sale_list);

        callBar = (CallBar) findViewById(R.id.shop_flash_sale_call_bar);
        mShopDetailsHeadView = (ShopDetailsHeadView) findViewById(R.id.shop_flash_sale_head_view);
        mFlashSaleListFragment = (ShopFlashSaleListFragment_1) getSupportFragmentManager()
                .findFragmentById(R.id.shop_flash_sale_frame_content);

        mShopDetailsHeadView.setTabCheck(ShopDetailsHeadView.TAB_XSTJC);
        mShopDetailsController.requestThirdPartShop(this, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mShopDetailsHeadView !=null)
        mShopDetailsHeadView.setTabCheck(ShopDetailsHeadView.TAB_XSTJC);
    }

    @Override
    public void onSucceed(ThirdPartShop thirdPartShop) {
        int from = mShopDetailsController.getFrom();
        callBar.setNumber(from==0?thirdPartShop.getShopInfo().shopTel:thirdPartShop.getShopInfo().rescueTel);
        callBar.setmShopLocation(thirdPartShop.getShopInfo().getShopLocation());
        callBar.setFrom(from);
        getSupportActionBar().setTitle(thirdPartShop.getShopInfo().shopTitle);
        mShopDetailsHeadView.bindData(thirdPartShop.getShopInfo());
        mFlashSaleListFragment.upDate(thirdPartShop.getShopInfo().shopID);
    }

    @Override
    public void onFailed(boolean offLine) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFlashSaleListFragment.destroy();
    }
}
