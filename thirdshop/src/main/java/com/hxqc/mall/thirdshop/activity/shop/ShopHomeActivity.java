package com.hxqc.mall.thirdshop.activity.shop;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.control.ShopDetailsController;
import com.hxqc.mall.thirdshop.fragment.ShopHomePageFragment;
import com.hxqc.mall.thirdshop.model.ThirdPartShop;
import com.hxqc.mall.thirdshop.views.CallBar;
import com.hxqc.mall.thirdshop.views.ShopDetailsHeadView;

/**
 * Author:李烽
 * Date:2016-04-05
 * FIXME
 * Todo 店铺首页
 */
public class ShopHomeActivity extends BaseShopDetailsActivity implements ShopDetailsController.ThirdPartShopHandler {

    private ShopDetailsHeadView mShopDetailsHeadView;
    private ShopHomePageFragment shopHomePageFragment;
    private CallBar mCallBar;
    private RequestFailView mRequestFailView;
    private RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_home);
        initFragment();
        mRequestFailView = (RequestFailView) findViewById(R.id.shopdetails_fail_view);
        mShopDetailsHeadView = (ShopDetailsHeadView) findViewById(R.id.shop_detail_head_view);
        rootLayout = (RelativeLayout) findViewById(R.id.root_layout);
        mCallBar = (CallBar) findViewById(R.id.call_bar);
        mShopDetailsHeadView.setMODE_TYPE(1);
        mShopDetailsHeadView.setTabCheck(ShopDetailsHeadView.TAB_SY);
        mShopDetailsController.requestThirdPartShop(this, this);
    }

    private void initFragment() {
        shopHomePageFragment =
                (ShopHomePageFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        mShopDetailsHeadView.setTabCheck(ShopDetailsHeadView.TAB_SY);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSucceed(ThirdPartShop brandGroups) {
        mRequestFailView.setVisibility(View.GONE);
        getSupportActionBar().setTitle(brandGroups.getShopInfo().shopTitle);
        mCallBar.setTitle("咨询电话");
        int from = mShopDetailsController.getFrom();
        mCallBar.setNumber(from==0?brandGroups.getShopInfo().shopTel:brandGroups.getShopInfo().rescueTel);
        mCallBar.setFrom(from);
        mCallBar.setmShopLocation(brandGroups.getShopInfo().getShopLocation());
        shopHomePageFragment.setShopData(brandGroups);
        mShopDetailsHeadView.bindData(brandGroups.getShopInfo());
        rootLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailed(boolean offLine) {
        mRequestFailView.setEmptyDescription("店铺不存在");
        mRequestFailView.setEmptyButtonClick("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        mRequestFailView.setVisibility(View.VISIBLE);
    }
}
