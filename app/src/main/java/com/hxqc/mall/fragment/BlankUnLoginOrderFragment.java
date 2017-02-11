package com.hxqc.mall.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.fragment.order.MyOrderEntranceFragment;
import com.hxqc.mall.views.order.UnloginOrderView;

import hxqc.mall.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankUnLoginOrderFragment extends BaseFragment {

    UnloginOrderView unloginOrderView;
    private Toolbar mToolbar;
    public BlankUnLoginOrderFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blank_un_login_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unloginOrderView = (UnloginOrderView) view.findViewById(R.id.fl_null);

        mToolbar= (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("订单");
        mToolbar.setTitleTextColor(Color.WHITE);
    }

    @Override
    public void onResume() {
        super.onResume();
        //添加列表项
        addOrderList();
    }

    private void addOrderList() {
        boolean login = UserInfoHelper.getInstance().isLogin(getActivity());
        FragmentManager fragmentManager = getActivity()
                .getSupportFragmentManager();
        MyOrderEntranceFragment mOrderCollectionFragment = (MyOrderEntranceFragment) fragmentManager
                .findFragmentByTag("OrderCollectionFragment");
        if (login) {
            unloginOrderView.setVisibility(View.GONE);
            mToolbar.setVisibility(View.GONE);
            if (mOrderCollectionFragment != null) {
                if (mOrderCollectionFragment.isHidden()) {
                    fragmentManager.beginTransaction().show(mOrderCollectionFragment).commitAllowingStateLoss();

                }
                mOrderCollectionFragment.onResume();
            } else {
                mOrderCollectionFragment = new MyOrderEntranceFragment();
                fragmentManager.beginTransaction().add(R.id.fl_taken, mOrderCollectionFragment,
                        "OrderCollectionFragment")
                        .commitAllowingStateLoss();
            }
        } else {
            if (mOrderCollectionFragment != null) {
                if (mOrderCollectionFragment.isVisible()) {
                    fragmentManager.beginTransaction().hide(mOrderCollectionFragment).commitAllowingStateLoss();
                }
            }
            unloginOrderView.setVisibility(View.VISIBLE);
            mToolbar.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public String fragmentDescription() {
        return "首页订单列表";
    }
}
