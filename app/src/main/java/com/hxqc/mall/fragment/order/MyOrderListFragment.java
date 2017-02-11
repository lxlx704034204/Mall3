package com.hxqc.mall.fragment.order;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.adapter.order.MyOrderListAdapter;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.fragment.SwipeRefreshForRecyclerFragment;
import com.hxqc.mall.core.model.order.OrderListBean;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.utils.SharedPreferencesHelper;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * liaoguilong
 * Created by CPR113 on 2016/3/1.
 * 我的订单列表
 */
public class MyOrderListFragment extends SwipeRefreshForRecyclerFragment implements MyOrderListAdapter.OnAdapterChangeListener{
    private UserApiClient mUserApiClient;
    private ArrayList<OrderListBean> mOrderList = new ArrayList<>();
    private String orderType = "";
    private SharedPreferencesHelper mSharedPreferencesHelper;
    private MyOrderListAdapter mMyOrderListAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mUserApiClient = new UserApiClient();
        mSharedPreferencesHelper = new SharedPreferencesHelper(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRequestFailView.setEmptyButtonClick("随便逛逛", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherThirdPartShop.toMain(getContext(), 0);
            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        if(mSharedPreferencesHelper.getClearOrder())
        {
            mSharedPreferencesHelper.setClearOrder(false);
            mPage = DEFAULT_PATE;
            mRecyclerView.setAdapter(null);
        }
        if (mSharedPreferencesHelper.getOrderChange()) {
            mSharedPreferencesHelper.setOrderChange(false);
            mPage = DEFAULT_PATE;
            mRecyclerView.setAdapter(null);
            refreshData(true);
        }

    }

    @Override
    public void refreshData(boolean hasLoadingAnim) {
        if (mUserApiClient != null)
            mUserApiClient.getMyOrderList(UserInfoHelper.getInstance().getToken(mContext), orderType, mPage, PER_PAGE, getLoadingAnimResponseHandler(hasLoadingAnim));
    }

    @Override
    protected void onSuccessResponse(String response) {
        ArrayList<OrderListBean> mOrders = JSONUtils.fromJson(response, new TypeToken<ArrayList<OrderListBean>>() {
        });
        if (mOrders == null) return;
        mPtrHelper.setHasMore((mOrders.size() >= PER_PAGE));
        if (mPage == DEFAULT_PATE) {
            mOrderList.clear();
            if (mOrders.size() > 0) {
                mPtrFrameLayoutView.setVisibility(View.VISIBLE);
                mRequestFailView.setVisibility(View.GONE);
                mMyOrderListAdapter = new MyOrderListAdapter(mOrderList, getContext());
                mMyOrderListAdapter.setOnAdapterChangeListener(this);
                mRecyclerView.setAdapter(mMyOrderListAdapter);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            } else {
                showNoData();
                return;
            }
        }
        mOrderList.addAll(mOrders);
        mMyOrderListAdapter.notifyDataSetChanged();
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
        mPage = DEFAULT_PATE;
        refreshData(true);
    }

    @Override
    protected String getEmptyDescription() {
        return "没有相关的订单";
    }

    @Override
    public String fragmentDescription() {
        return "我的订单";
    }

    @Override
    public void onChange() {
        mPage = DEFAULT_PATE;
        refreshData(true);
    }
}
