package com.hxqc.mall.thirdshop.activity.shop;

import android.os.Bundle;

import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.control.ShopDetailsController;
import com.hxqc.mall.thirdshop.fragment.CarTypeFragment;
import com.hxqc.mall.thirdshop.model.ThirdPartShop;
import com.hxqc.mall.thirdshop.views.CallBar;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.OverlayDrawer;

/**
 * liaoguilong
 * 车型报价
 */
public class ModelsOfferActivity extends BaseShopDetailsActivity implements ShopDetailsController.ThirdPartShopHandler {
    private OverlayDrawer mOverlayDrawer; //侧滑menudrawer

    private CallBar mCallBar;
    public CarTypeFragment mCarTypeFragment;

    //    public  ModelsQuoteFragment mModelsQuoteFragment;
//    private ShopDetailsHeadView mShopDetailsHeadView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_models_offer);
        initView();
    }

    public void initView() {
        mOverlayDrawer = (OverlayDrawer) findViewById(R.id.shopdetails_drawer);
        mOverlayDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN_MENUOPEN);
        mOverlayDrawer.setSidewardCloseMenu(true);
        mCallBar = (CallBar) findViewById(R.id.shopdetails_call_bar);

        mCarTypeFragment = (CarTypeFragment) getSupportFragmentManager().findFragmentById(R.id.shopdetails_mdMenu_fragment);
//        mModelsQuoteFragment= (ModelsQuoteFragment) getSupportFragmentManager().findFragmentById(R.id.shopdetails_frame_content);
//        mShopDetailsHeadView= (ShopDetailsHeadView) findViewById(R.id.shopdetails_head_view);
//        mShopDetailsHeadView.setTabCheck(mShopDetailsHeadView.TAB_CXBJ);

        mShopDetailsController.requestThirdPartShop(ModelsOfferActivity.this, this);
//        if(getIntent().getStringExtra(ShopDetailsController.SERIESID_KEY) !=null)
//            mModelsQuoteFragment.ScrollToSeriesID(getIntent().getStringExtra(ShopDetailsController.SERIESID_KEY));
    }


    @Override
    public void onSucceed(ThirdPartShop thirdPartShop) {
        int from = mShopDetailsController.getFrom();
        mCallBar.setNumber(from==0?thirdPartShop.getShopInfo().shopTel:thirdPartShop.getShopInfo().rescueTel);
        mCallBar.setFrom(from);
        mCallBar.setmShopLocation(thirdPartShop.getShopInfo().getShopLocation());
        getSupportActionBar().setTitle(thirdPartShop.getShopInfo().shopTitle);
//        mShopDetailsHeadView.bindData(thirdPartShop.getShopInfo());
    }

    @Override
    public void onFailed(boolean offLine) {
        if (offLine)
            ToastHelper.showYellowToast(this, getString(R.string.app_net_error));
    }

    public void openMenu() {
        if (!mOverlayDrawer.isMenuVisible())
            mOverlayDrawer.openMenu();
    }
}
