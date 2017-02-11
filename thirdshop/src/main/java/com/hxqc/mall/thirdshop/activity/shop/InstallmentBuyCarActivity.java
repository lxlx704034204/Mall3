package com.hxqc.mall.thirdshop.activity.shop;

import android.os.Bundle;

import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.control.ShopDetailsController;
import com.hxqc.mall.thirdshop.model.ThirdPartShop;
import com.hxqc.mall.thirdshop.views.CallBar;

/***
 * liaoguilong
 * 2016年11月16日 15:08:33
 * 分期购车
 */
public class InstallmentBuyCarActivity extends BaseShopDetailsActivity implements ShopDetailsController.ThirdPartShopHandler{
    private CallBar mCallBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installment_buy_car);
        initView();
    }

    private void initView() {
        mCallBar = (CallBar) findViewById(R.id.shopdetails_call_bar);
        mShopDetailsController.requestThirdPartShop(InstallmentBuyCarActivity.this, this);
    }

    @Override
    public void onSucceed(ThirdPartShop thirdPartShop) {
        int from = mShopDetailsController.getFrom();
        mCallBar.setNumber(from==0?thirdPartShop.getShopInfo().shopTel:thirdPartShop.getShopInfo().rescueTel);
        mCallBar.setFrom(from);
        mCallBar.setmShopLocation(thirdPartShop.getShopInfo().getShopLocation());
        getSupportActionBar().setTitle(thirdPartShop.getShopInfo().shopTitle);
    }


    @Override
    public void onFailed(boolean offLine) {
        if (offLine)
            ToastHelper.showYellowToast(this, getString(R.string.app_net_error));
    }
}
