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
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.util.ActivitySwitchAutoInfo;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.util.DebugLog;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.OverlayDrawer;

import org.greenrobot.eventbus.EventBus;

/**
 * Author:胡仲俊
 * Date: 2016 - 11 - 01
 * Des: 保养含车辆列表通用添加车辆信息
 * FIXME
 * Todo
 */
public class CenterAutoFragment extends Fragment implements CenterEditAutoFragment.OnFinishClickListener, CenterEditAutoFragment.OnAutoTyperClickListener, MenuDrawer.OnDrawerStateChangeListener, AutoTypeFragment.onBackData {

    private static final String TAG = AutoInfoContants.LOG_J;
    private View rootView;
    private CenterEditAutoFragment editAutoInfoFragment;
    private OverlayDrawer mOverlayDrawer;
    private AutoTypeFragment autoTypeFragment;
    private MyAuto myAuto;
    private boolean isAdd;
    private int flag;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_center_auto, container, false);
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

        mOverlayDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
        mOverlayDrawer.setSidewardCloseMenu(true);
        editAutoInfoFragment = (CenterEditAutoFragment) getChildFragmentManager().findFragmentById(R.id.add_auto_info_content);
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
                AutoInfoControl.getInstance().addAutoInfo(getActivity(), myAuto, aeCallBack);
            } else {
                DebugLog.i(TAG, "修改");
                AutoInfoControl.getInstance().editAutoInfo(getActivity(), myAuto, aeCallBack);
            }
            this.myAuto = myAuto;
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
    public void backData(String brand, String brandID, String brandThumb, String seriesBrandName, String series, String seriesID, String model, String modelID) {
        mOverlayDrawer.closeMenu();
        editAutoInfoFragment.setAutoType(brand, brandID, brandThumb, seriesBrandName, series, seriesID, model, modelID);
        if (myAuto == null) {
            myAuto = new MyAuto();
            myAuto.brand = brand;
            myAuto.brandID = brandID;
            myAuto.brandThumb = brandThumb;
            myAuto.series = series;
            myAuto.seriesID = seriesID;
            myAuto.autoModel = model;
            myAuto.autoModelID = modelID;
            myAuto.brandName = seriesBrandName;
        } else {
            myAuto.brand = brand;
            myAuto.brandID = brandID;
            myAuto.brandThumb = brandThumb;
            myAuto.series = series;
            myAuto.seriesID = seriesID;
            myAuto.autoModel = model;
            myAuto.autoModelID = modelID;
            myAuto.brandName = seriesBrandName;
        }
    }


    private CallBackControl.CallBack<String> aeCallBack = new CallBackControl.CallBack<String>() {
        @Override
        public void onSuccess(String response) {
            DebugLog.i(TAG, "onSuccess");
            /*if (flag == AutoInfoContants.SHOP_QUOTE) {
                DebugLog.i(TAG, "autoLocal");
//                AutoHelper.getInstance().addOrEditAutoLocaltoLinkedList(getActivity(), myAuto, AutoHelper.AUTO_LOCAL_INFO);
            }*/

            if(flag == AutoInfoContants.EDIT_PAGE) {
                DebugLog.i(TAG,"EDIT EDIT_PAGE");
                EventBus.getDefault().post(myAuto);
            } else {
                DebugLog.i(TAG,"EDIT OTHER");
                ActivitySwitchAutoInfo.toBackAutoData(getActivity());
            }

            if (isAdd) {
                AutoHelper.getInstance().addOrEditAutoLocaltoLinkedList(getActivity(), myAuto, AutoHelper.SWITCH_AUTO);
                AutoHelper.getInstance().addOrEditAutoLocaltoLinkedList(getActivity(), myAuto, AutoHelper.AUTO_DETAIL_INFO);
            } else {
                AutoHelper.getInstance().editAutoDataLocal(getActivity(), myAuto, AutoHelper.SWITCH_AUTO);
                AutoHelper.getInstance().editAutoDataLocal(getActivity(), myAuto, AutoHelper.AUTO_DETAIL_INFO);
            }

            getActivity().finish();
        }

        @Override
        public void onFailed(boolean offLine) {
            DebugLog.i(TAG, "onFailed");
        }
    };

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

    }*/

    @Override
    public void onDestroy() {
        myAuto = null;
        super.onDestroy();
    }
}
