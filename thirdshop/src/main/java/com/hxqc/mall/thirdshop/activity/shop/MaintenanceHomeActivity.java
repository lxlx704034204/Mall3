//package com.hxqc.mall.thirdshop.activity.shop;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.View;
//
//import com.hxqc.mall.auto.config.AutoInfoContants;
//import com.hxqc.mall.auto.model.MyAuto;
//import com.hxqc.mall.core.api.RequestFailView;
//import com.hxqc.mall.core.util.ToastHelper;
//import com.hxqc.mall.thirdshop.R;
//import com.hxqc.mall.thirdshop.control.ShopDetailsController;
//import com.hxqc.mall.thirdshop.maintenance.fragment.SelectAutoFragment;
//import com.hxqc.mall.thirdshop.model.ShopInfo;
//import com.hxqc.mall.thirdshop.model.ThirdPartShop;
//import com.hxqc.mall.thirdshop.views.CallBar;
//import com.hxqc.mall.thirdshop.views.ShopDetailsHeadView;
//
//import net.simonvt.menudrawer.MenuDrawer;
//import net.simonvt.menudrawer.OverlayDrawer;
//
///**
// * Author:李烽
// * Date:2016-04-05
// * FIXME
// * Todo 维修首页
// */
//@Deprecated
//public class MaintenanceHomeActivity extends BaseShopDetailsActivity
//        implements ShopDetailsController.ThirdPartShopHandler,
////        MaintenanceBaseFragment.OnMaintenanceListener,
//        SelectAutoFragment.OnSelectedCallBack {
//    private ShopDetailsHeadView mShopDetailsHeadView;
//    private MaintenanceFragment mMaintenanceFragment;
//    private OverlayDrawer mOverlayDrawer; //侧滑menudrawer
//    private String shopID;
//    private String brand;
//    private String brandID;
//    private SelectAutoFragment mSelectAutoFragment;
//    private CallBar mCallBar;
//    private OnShopInfoCallBack onShopIDCallBack;
//    private RequestFailView mRequestFailView;
//
//    public void setOnShopInfoCallBack(OnShopInfoCallBack onShopInfoCallBack) {
//        this.onShopIDCallBack = onShopInfoCallBack;
//    }
//
//    public interface OnShopInfoCallBack {
//        void onShopInfoLoad(ShopInfo shopInfo);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maintenance_home);
//
//        initFragment();
//        initView();
//    }
//
//    public void initView() {
//        mRequestFailView = (RequestFailView) findViewById(R.id.shopdetails_fail_view);
//        mCallBar = (CallBar) findViewById(R.id.call_bar);
//        mOverlayDrawer = (OverlayDrawer) findViewById(R.id.shopdetails_drawer);
//        mOverlayDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN_MENUOPEN);
//        mOverlayDrawer.setSidewardCloseMenu(true);
//        mShopDetailsHeadView = (ShopDetailsHeadView) findViewById(R.id.shop_detail_head_view);
//        mShopDetailsHeadView.setTabCheck(ShopDetailsHeadView.TAB_WXBY);
//        mShopDetailsController.requestThirdPartShop(this, this);
//    }
//
//    private void initFragment() {
//        mMaintenanceFragment =
//                (MaintenanceFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
////        mMaintenanceFragment = new MaintenanceFragment();
////        getSupportFragmentManager().beginTransaction()
////                .add(R.id.fragment_container, mMaintenanceFragment)
////                .show(mMaintenanceFragment).commit();
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        if (mOverlayDrawer.isMenuVisible()) {
//            mOverlayDrawer.closeMenu();
//            return false;
//        }
//        finish();
//        return super.onSupportNavigateUp();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == AutoInfoContants.GET_AUTO_DATA) {
//            if (resultCode == Activity.RESULT_OK)
//                if (data != null) {
//                    MyAuto auto = data.getParcelableExtra("myAuto");
////                    autoDetailView.addData(auto);
//                    mMaintenanceFragment.updateAuto(auto);
//                }
//        }
//    }
//
//    /**
//     * 侧滑
//     *
//     * @return
//     */
//    private Fragment getSelectAutoFragment() {
//        if (mSelectAutoFragment == null) {
//            mSelectAutoFragment = SelectAutoFragment.createInstance(shopID, this);
////            mListFragmentMenus.add(mSelectAutoFragment);
//            getSupportFragmentManager().beginTransaction().add(R.id.shopdetails_mdMenu_fragment,
//                    mSelectAutoFragment).commit();
//        }
//        return mSelectAutoFragment;
//    }
//
//    /**
//     * 打开车型侧滑菜单
//     */
//    public void openMenu() {
//        getSupportFragmentManager().beginTransaction()
//                .show(getSelectAutoFragment()).commit();
////        ShowAndHideMenu(fragment);
//        if (!mOverlayDrawer.isMenuVisible()) {
//            mOverlayDrawer.openMenu();
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//
//        if (mOverlayDrawer.isMenuVisible()) {
//            mOverlayDrawer.closeMenu();
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//
//    @Override
//    public void onSucceed(ThirdPartShop brandGroups) {
//        getSupportActionBar().setTitle(brandGroups.getShopInfo().shopTitle);
//        mCallBar.setTitle("咨询电话");
//        mCallBar.setNumber(brandGroups.getShopInfo().shopTel);
//        shopID = brandGroups.getShopInfo().shopID;
//        if (onShopIDCallBack != null) {
//            onShopIDCallBack.onShopInfoLoad(brandGroups.getShopInfo());
//        }
//        brand = brandGroups.getShopInfo().brand;
//        brandID = brandGroups.getShopInfo().brandID;
//        mShopDetailsHeadView.bindData(brandGroups.getShopInfo());
//        mRequestFailView.setVisibility(View.GONE);
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
////        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    public void onFailed(boolean offLine) {
//        if (offLine)
//            ToastHelper.showYellowToast(this, getString(R.string.app_net_error));
//        else notShop();
//    }
//
//    public void notShop() {
//        mRequestFailView.setEmptyDescription("店铺不存在");
//        mRequestFailView.setEmptyButtonClick("返回", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
//        mRequestFailView.setVisibility(View.VISIBLE);
//    }
////    @Override
////    public String getShopID() {
////        return shopID;
////    }
//
////    @Override
////    public String getBrand() {
////        return brand;
////    }
//
////    @Override
////    public String getShopBrandID() {
////        return brandID;
////    }
//
//
//    public void openSlidMenu() {
//        openMenu();
//    }
//
//    @Override
//    public void onSelected(String brand, String brandID, String series, String seriesID, String model, String modelID) {
//        if (mOverlayDrawer.isMenuVisible()) {
//            mOverlayDrawer.closeMenu();
//        }
//        mMaintenanceFragment.updateModel(brand, brandID, series, seriesID, model, modelID);
//    }
//}
