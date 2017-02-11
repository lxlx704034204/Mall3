/*
package com.hxqc.mall.auto.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoHelper;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.inter.CallBack;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.util.ActivitySwitchAutoInfo;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.util.DebugLog;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.OverlayDrawer;

*/
/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 20
 * FIXME
 * Todo 含车辆列表通用添加车辆信息
 *//*

public class AutoInfoFragment extends Fragment implements EditAutoInfoFragment.OnFinishClickListener, EditAutoInfoFragment.OnAutoTyperClickListener, MenuDrawer.OnDrawerStateChangeListener, AutoTypeFragment.onBackData {

    private View rootView;
    private EditAutoInfoFragment editAutoInfoFragment;
    private OverlayDrawer mOverlayDrawer;
    private AutoTypeFragment autoTypeFragment;
    private MyAuto myAuto;
    private boolean isAdd;
    private int flag;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_add_auto, container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

        initEvent();
    }

    private void initEvent() {
        mOverlayDrawer.setOnDrawerStateChangeListener(this);
        editAutoInfoFragment.setOnFinishClickListener(this);
        editAutoInfoFragment.setmOnAutoTyperClickListener(this);
        autoTypeFragment.setOnBackDataListener(this);
    }

    private void initView() {
        mOverlayDrawer = (OverlayDrawer) rootView.findViewById(R.id.add_auto_drawer);

        mOverlayDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN_MENUOPEN);
        mOverlayDrawer.setSidewardCloseMenu(false);
        editAutoInfoFragment = (EditAutoInfoFragment) getChildFragmentManager().findFragmentById(R.id.add_auto_info_content);
        autoTypeFragment = (AutoTypeFragment) getChildFragmentManager().findFragmentById(R.id.add_auto_info_menu);

    }

    @Override
    public void onFinishClick(View v, final MyAuto myAuto, boolean isAdd, int flag) {

        this.isAdd = isAdd;
        this.flag = flag;
        DebugLog.i(TAG, myAuto.toString() + "-----" + isAdd + "------" + flag);
        if (UserInfoHelper.getInstance().isLogin(getActivity())) {
            DebugLog.i(TAG, "登录后");
            if (isAdd) {
                DebugLog.i(TAG, "添加");
                //首页进入常规保养下的添加车辆
                if (flag == AutoInfoContants.HOME_PAGE) {
                    DebugLog.i(TAG,"首页进入常规保养下的添加车辆");
                    */
/*AutoInfoControl.getInstance().addAutoInfo(getActivity(), myAuto, new CallBack<String>() {
                        @Override
                        public void onSuccess(String response) {
                            AutoHelper.getInstance().addAutoLocaltoLinkedList(getActivity(), myAuto, AutoHelper.AUTO_LOCAL_INFO);
                            ActivitySwitchBase.toWhere(getActivity(), "com.hxqc.mall.thirdshop.maintenance.activity.ShopQuoteActivity");
                            getActivity().finish();
                        }

                        @Override
                        public void onFailed(boolean offLine) {

                        }
                    });*//*

                    AutoHelper.getInstance().addOrEditAutoLocaltoLinkedList(getActivity(), myAuto, AutoHelper.AUTO_DETAIL_INFO);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("myAuto", myAuto);
                    ActivitySwitchBase.toWhere(getActivity(), "com.hxqc.mall.thirdshop.maintenance.activity.ShopQuoteActivity", bundle);
                    getActivity().finish();
                } else if (flag == AutoInfoContants.AUTO_DETAIL) {
                    //常规保养下的添加车辆
                    DebugLog.i(TAG,"常规保养下的添加车辆");
                    AutoHelper.getInstance().editAutoDataLocal(getActivity(), myAuto, AutoHelper.AUTO_LOCAL_INFO);
                    AutoHelper.getInstance().addOrEditAutoLocaltoLinkedList(getActivity(), myAuto, AutoHelper.AUTO_DETAIL_INFO);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("myAuto", myAuto);
                    ActivitySwitchBase.toWhere(getActivity(), "com.hxqc.mall.thirdshop.maintenance.activity.ShopQuoteActivity", bundle);
                    getActivity().finish();
                } else if(flag == AutoInfoContants.RESERVE_MAINTAIN) {
                    DebugLog.i(TAG,"常规保养下的添加车辆");
                    AutoHelper.getInstance().editAutoDataLocal(getActivity(), myAuto, AutoHelper.RESERVE_MAINTAIN_AUTO_INFO);

                }else {
//                    AutoInfoControl.getInstance().addAutoInfo(getActivity(), myAuto, aeCallBack);
                }
//                AutoHelper.getInstance().addMyAuto(getActivity(),myAuto);
            } else {
                AutoInfoControl.getInstance().editAutoInfo(getActivity(), myAuto, aeCallBack);
                DebugLog.i(TAG, "修改");
                //本地数据库的修改
//                AutoHelper.getInstance().updateMyAuto(getActivity(), myAuto);
            }
            this.myAuto = myAuto;
        } else {
            //本地数据库的添加
            DebugLog.i(TAG, "未登录");
//            MyAutoInfoHelper.getInstance(getActivity()).addAutoLocal(myAuto);
            AutoHelper.getInstance().addOrEditAutoLocaltoLinkedList(getActivity(), myAuto, AutoHelper.AUTO_DETAIL_INFO);
            if (flag == AutoInfoContants.HOME_PAGE) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("myAuto", myAuto);
                ActivitySwitchBase.toWhere(getActivity(), "com.hxqc.mall.thirdshop.maintenance.activity.ShopQuoteActivity", bundle);
            } else {
                ActivitySwitchAutoInfo.toAutoInfo(getActivity());
            }
            getActivity().finish();
        }

    }

    @Override
    public void onAutoTyperClick(View v) {
        if (!mOverlayDrawer.isMenuVisible()) mOverlayDrawer.openMenu();

    }

    @Override
    public void onDrawerStateChange(int oldState, int newState) {
        DebugLog.i(TAG, "newState:" + newState);
        if (newState == 8) {
            autoTypeFragment.setData(myAuto);
        }
    }

    @Override
    public void onDrawerSlide(float openRatio, int offsetPixels) {
    }

    @Override
    public void backData(String brand, String brandID, String brandThumb, String series, String seriesID, String model, String modelID) {
        mOverlayDrawer.closeMenu();
        editAutoInfoFragment.setAutoType(brand, brandID, brandThumb, series, seriesID, model, modelID);
        if (myAuto == null) {
            myAuto = new MyAuto();
            myAuto.brand = brand;
            myAuto.brandID = brandID;
            myAuto.brandThumb = brandThumb;
            myAuto.series = series;
            myAuto.seriesID = seriesID;
            myAuto.autoModel = model;
            myAuto.autoModelID = modelID;
        } else {
            myAuto.brand = brand;
            myAuto.brandID = brandID;
            myAuto.brandThumb = brandThumb;
            myAuto.series = series;
            myAuto.seriesID = seriesID;
            myAuto.autoModel = model;
            myAuto.autoModelID = modelID;
        }
    }


    private CallBack<String> aeCallBack = new CallBack<String>() {
        @Override
        public void onSuccess(String response) {
            DebugLog.i(TAG, "onAESucceed");
            if (flag == AutoInfoContants.SHOP_QUOTE) {
                DebugLog.i(TAG, "autoLocal");
                AutoHelper.getInstance().addAutoLocal(getActivity(), myAuto, AutoHelper.AUTO_LOCAL_INFO);
            }
            ActivitySwitchAutoInfo.toAutoInfo(getActivity(), myAuto, isAdd);
            getActivity().finish();
        }

        @Override
        public void onFailed(boolean offLine) {

        }
    };

    */
/*@Override
    public void onAESucceed(String response) {
        DebugLog.i(TAG, "onAESucceed");
        if (flag == AutoInfoContants.SHOP_QUOTE) {
            DebugLog.i(TAG, "autoLocal");
            AutoHelper.getInstance().addAutoLocal(getActivity(), myAuto, AutoHelper.AUTO_DETAIL_INFO);
        }
        ActivitySwitchAutoInfo.toAutoInfo(getActivity(), myAuto, isAdd);
        getActivity().finish();
    }

    @Override
    public void onAEFailed(boolean offLine) {

    }*//*


    @Override
    public void onDestroy() {
        myAuto = null;
        super.onDestroy();
    }
}
*/
