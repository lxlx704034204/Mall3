package com.hxqc.mall.thirdshop.activity.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.dialog.LoadingDialog;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.control.ShopDetailsController;
import com.hxqc.mall.thirdshop.maintenance.fragment.AddAutoFragment;
import com.hxqc.mall.thirdshop.maintenance.fragment.MaintenanceHomeFragment;
import com.hxqc.mall.thirdshop.maintenance.fragment.SelectAutoFragment;
import com.hxqc.mall.thirdshop.model.ShopInfo;
import com.hxqc.mall.thirdshop.model.ThirdPartShop;
import com.hxqc.mall.thirdshop.views.CallBar;
import com.hxqc.mall.thirdshop.views.ShopDetailsHeadView;
import com.hxqc.util.DebugLog;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.OverlayDrawer;

import java.util.ArrayList;


/**
 * Author:李烽
 * Date:2016-04-05
 * FIXME
 * Todo 4s店铺点击常规保养界面
 */
public class MaintenanceHomeActivity_1 extends BaseShopDetailsActivity
        implements ShopDetailsController.ThirdPartShopHandler,
        SelectAutoFragment.OnSelectedCallBack {
    private static final String TAG = "MaintenanceHomeActivity_1";
    private ShopDetailsHeadView mShopDetailsHeadView;
    private MaintenanceHomeFragment mMaintenanceHomeFragment;
    private AddAutoFragment mAddAutoFragment;
    private OverlayDrawer mOverlayDrawer; //侧滑menudrawer
    private String shopID;

    private SelectAutoFragment mSelectAutoFragment;
    private CallBar mCallBar;
    private OnShopInfoCallBack onShopIDCallBack;
    private MyAuto auto;
    private ArrayList<BrandGroup> shopBrandGroups = new ArrayList<>();
    private RequestFailView mRequestFailView;
    private LoadingDialog loadingDialog;

    public void setOnShopInfoCallBack(OnShopInfoCallBack onShopInfoCallBack) {
        this.onShopIDCallBack = onShopInfoCallBack;
    }


    public interface OnShopInfoCallBack {
        void onShopInfoLoad(ShopInfo shopInfo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_home_1);
        DebugLog.i("Tag", "onCreate");
        initFragment();
        initView();
    }

    private void initData() {
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            auto = bundle.getParcelable("myAuto");
            DebugLog.i(TAG, "MyAuto: " + auto);
            if (auto != null) {
                if (mMaintenanceHomeFragment != null) {
                    mMaintenanceHomeFragment.updateAuto(auto);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DebugLog.i(TAG,"onResume");
        initData();
        if(mShopDetailsHeadView !=null)
        mShopDetailsHeadView.setTabCheck(ShopDetailsHeadView.TAB_WXBY);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initData();
    }

    public void initView() {
        if (mRequestFailView != null) {
            return;
        }
        loadingDialog = new LoadingDialog(this);
        mRequestFailView = (RequestFailView) findViewById(R.id.shopdetails_fail_view);
        mCallBar = (CallBar) findViewById(R.id.call_bar);
        mOverlayDrawer = (OverlayDrawer) findViewById(R.id.shopdetails_drawer);
        mOverlayDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN_MENUOPEN);
        mOverlayDrawer.setSidewardCloseMenu(true);
        mShopDetailsHeadView = (ShopDetailsHeadView) findViewById(R.id.shop_detail_head_view);
        mShopDetailsHeadView.setTabCheck(ShopDetailsHeadView.TAB_WXBY);
        mShopDetailsController.requestThirdPartShop(this, this);//请求店铺
    }


    private void initFragment() {
        mMaintenanceHomeFragment = MaintenanceHomeFragment.newInstance();
        mAddAutoFragment = AddAutoFragment.newInstance(new AddAutoFragment.AddAutoListener() {
            @Override
            public void onCompleted(MyAuto myauto) {
                auto = myauto;
                showNoDataLayout(false);
            }

            @Override
            public void onAddLocal(MyAuto myauto) {
                auto = myauto;
                showNoDataLayout(false);
            }

            @Override
            public void onCompleteFailture(String message) {
                //保存失败
            }
        });
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mAddAutoFragment)
                .add(R.id.fragment_container, mMaintenanceHomeFragment)
                .hide(mAddAutoFragment)
                .hide(mMaintenanceHomeFragment)
                .commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (mOverlayDrawer.isMenuVisible()) {
            mOverlayDrawer.closeMenu();
            return false;
        }
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     * 侧滑
     *
     * @return
     */
    private Fragment getSelectAutoFragment() {
        if (mSelectAutoFragment == null) {
            mSelectAutoFragment = SelectAutoFragment.createInstance(shopID, this);
//            mListFragmentMenus.add(mSelectAutoFragment);
            getSupportFragmentManager().beginTransaction().add(R.id.shopdetails_mdMenu_fragment,
                    mSelectAutoFragment).commit();
        }
        return mSelectAutoFragment;
    }

    /**
     * 打开车型侧滑菜单
     */
    public void openMenu() {
        getSupportFragmentManager().beginTransaction()
                .show(getSelectAutoFragment()).commit();
//        ShowAndHideMenu(fragment);
        if (!mOverlayDrawer.isMenuVisible()) {
            mOverlayDrawer.openMenu();
        }
    }

    @Override
    public void onBackPressed() {
        if (mOverlayDrawer.isMenuVisible()) {
            mOverlayDrawer.closeMenu();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onSucceed(ThirdPartShop brandGroups) {
        getSupportActionBar().setTitle(brandGroups.getShopInfo().shopTitle);
        mCallBar.setTitle("售后电话");
        int from = mShopDetailsController.getFrom();
        mCallBar.setNumber(from == 0 ? brandGroups.getShopInfo().serviceHotline : brandGroups.getShopInfo().rescueTel);
        mCallBar.setFrom(from);
        mCallBar.setmShopLocation(brandGroups.getShopInfo().getShopLocation());
        shopID = brandGroups.getShopInfo().shopID;
        if (onShopIDCallBack != null)
            onShopIDCallBack.onShopInfoLoad(brandGroups.getShopInfo());
        mShopDetailsHeadView.bindData(brandGroups.getShopInfo());
        mRequestFailView.setVisibility(View.GONE);
        mMaintenanceHomeFragment.setShopInfo(brandGroups.getShopInfo());
//        loadShopBrands(shopID);
        if (auto == null) {
            AutoInfoControl.getInstance().getMatchAuto(this, shopID, new CallBackControl.AutoCallBack<MyAuto>() {
                @Override
                public void onSuccess(MyAuto response, ArrayList<BrandGroup> brandGroup) {
                    DebugLog.i(AutoInfoContants.LOG_J, "MyAuto: " + response);
                    auto = response;
                    shopBrandGroups = brandGroup;
                    showNoDataLayout(false);
                }

                @Override
                public void onFailed(boolean offLine, ArrayList<BrandGroup> brandGroup) {
                    shopBrandGroups = brandGroup;
                    showNoDataLayout(true);
                }
            });
        }
    }

    /**
     * 判断状态
     */
    /*private void analyzingStatus() {
        if (UserInfoHelper.getInstance().isLogin(this)) {
            MyAutoInfoHelper.getInstance(this).getMatchAuto(shopBrandGroups,
                    new LoadDataCallBack<MyAuto>() {
                        @Override
                        public void onDataNull(String message) {
                            //没有匹配车辆
                            DebugLog.i(TAG, "没有匹配车辆");
                            showNoDataLayout(true);
                        }

                        @Override
                        public void onDataGot(MyAuto obj) {
                            auto = obj;
                            //用户有符合的车辆显示首页
                            showNoDataLayout(false);
                        }
                    });
        } else {
            ArrayList<MyAuto> autoData = MyAutoInfoHelper.getInstance(this).getAutoDataLocal();
            chooseAuto(autoData);
        }
    }*/

    /**
     * 筛选车辆
     *
     * @param autoData
     */
    /*private void chooseAuto(final ArrayList<MyAuto> autoData) {
        if (autoData == null || autoData.size() == 0) {
            showNoDataLayout(true);
            DebugLog.i(TAG, "没有车辆！");
        } else {
            filterAuto(shopBrandGroups, autoData);
        }
    }*/

    /**
     * 筛选符合品牌的车辆
     *
     * @param obj
     * @param autoData
     */
    /*private void filterAuto(ArrayList<BrandGroup> obj, ArrayList<MyAuto> autoData) {
        MyAutoInfoHelper.getInstance(this).checkBrand(autoData, obj,
                new LoadDataCallBack<MyAuto>() {
                    @Override
                    public void onDataNull(String message) {
                        DebugLog.i(TAG, "没有找到匹配的车辆！");
                        showNoDataLayout(true);
                    }

                    @Override
                    public void onDataGot(MyAuto obj) {
                        auto = obj;
                        showNoDataLayout(false);
                        DebugLog.i(TAG, "找到匹配的车辆！");
                    }
                });
    }*/

    private void showNoDataLayout(boolean b) {
        loadingDialog.dismiss();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (b) {
            fragmentTransaction.show(mAddAutoFragment).hide(mMaintenanceHomeFragment).commit();
            mAddAutoFragment.notifyData(shopBrandGroups);
        } else {
            fragmentTransaction.show(mMaintenanceHomeFragment).hide(mAddAutoFragment).commit();
            mMaintenanceHomeFragment.notifyData(auto, shopID, shopBrandGroups);
        }
    }

    /**
     * 先获取店铺品牌
     */
    /*private void loadShopBrands(String shopID) {
        loadingDialog.show();
        AutoTypeControl.getInstance().requestBrand(this, shopID,
                new CallBack<ArrayList<BrandGroup>>() {
                    @Override
                    public void onFailed(boolean offLine) {
                        DebugLog.i(TAG, "店铺品牌列表为空！");
//                        showNoDataLayout(true);
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(ArrayList<BrandGroup> obj) {
                        shopBrandGroups.clear();
                        shopBrandGroups.addAll(obj);
                        analyzingStatus();
                    }
                });
    }*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
    public void onFailed(boolean offLine) {
        if (offLine)
            ToastHelper.showYellowToast(this, getString(R.string.app_net_error));
        else notShop();
    }

    public void notShop() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AutoInfoContants.GET_AUTO_DATA) {
            if (resultCode == Activity.RESULT_OK)
                if (data != null) {
                    Bundle bundle = data.getBundleExtra(ActivitySwitchBase.KEY_DATA);
                    MyAuto auto = bundle.getParcelable("myAuto");
//                    autoDetailView.addData(auto);
                    mMaintenanceHomeFragment.updateAuto(auto);
                }
        }
    }

    @Override
    public void onSelected(String brand, String brandID, String brandThumb, String seriesBrandName, String series, String seriesID, String model, String modelID) {
        if (mOverlayDrawer.isMenuVisible()) {
            mOverlayDrawer.closeMenu();
        }
        mAddAutoFragment.updateModel(brand, brandID, brandThumb, seriesBrandName, series, seriesID, model, modelID);
    }
}
