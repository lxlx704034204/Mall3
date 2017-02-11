package com.hxqc.mall.thirdshop.activity.shop;

import android.os.Bundle;
import android.view.View;

import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.control.ShopDetailsController;
import com.hxqc.mall.thirdshop.model.ThirdPartShop;
import com.hxqc.mall.thirdshop.views.CallBar;
import com.hxqc.mall.thirdshop.views.ShopDetailsHeadView;

public class ShopSalesPromotionActivity extends BaseShopDetailsActivity implements ShopDetailsController.ThirdPartShopHandler {

    private CallBar mCallBar;
    private RequestFailView mRequestFailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_sales_promotion);
        initView();
    }

    void initView() {
        mRequestFailView = (RequestFailView) findViewById(R.id.shop_promotion_fail_view);
        mCallBar = (CallBar) findViewById(R.id.shop_promotion_call_bar);

        mShopDetailsHeadView = (ShopDetailsHeadView) findViewById(R.id.shop_promotion_head_view);
        mShopDetailsHeadView.setTabCheck(ShopDetailsHeadView.TAB_CXXX);
        mShopDetailsController.requestThirdPartShop(this, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mShopDetailsHeadView !=null)
        mShopDetailsHeadView.setTabCheck(ShopDetailsHeadView.TAB_CXXX);
    }

    @Override
    public void onSucceed(ThirdPartShop thirdPartShop) {
        int from = mShopDetailsController.getFrom();
        mCallBar.setNumber(from==0?thirdPartShop.getShopInfo().shopTel:thirdPartShop.getShopInfo().rescueTel);
        mCallBar.setFrom(from);
        mCallBar.setmShopLocation(thirdPartShop.getShopInfo().getShopLocation());
        getSupportActionBar().setTitle(thirdPartShop.getShopInfo().shopTitle);
        mShopDetailsHeadView.bindData(thirdPartShop.getShopInfo());
        mRequestFailView.setVisibility(View.GONE);
    }

    @Override
    public void onFailed(boolean offLine) {
        NotShop();
    }

    public void NotShop() {
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
