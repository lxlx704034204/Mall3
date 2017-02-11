package com.hxqc.mall.thirdshop.accessory.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.fragment.SwipeRefreshFragmentForListView;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.thirdshop.accessory.adapter.AccessoryShopListAdapter;
import com.hxqc.mall.thirdshop.accessory.api.AccessoryApiClient;
import com.hxqc.mall.thirdshop.accessory.model.PickupShop;
import com.hxqc.mall.thirdshop.accessory.views.LaborCostDialog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * 说明:
 *
 * @author: 吕飞
 * @since: 2016-05-30
 * Copyright:恒信汽车电子商务有限公司
 */
public class AccessoryShopListFragment extends SwipeRefreshFragmentForListView {
    ArrayList<PickupShop> mPickupShops = new ArrayList<>();
    AccessoryShopListAdapter mAccessoryShopListAdapter;
    LaborCostDialog mLaborCostDialog;
    BaseSharedPreferencesHelper mBaseSharedPreferencesHelper;
    public static final String CHOOSE_SHOP_ID = "choose_shop_id";
    public static final String CHOOSE_SHOP_NAME = "choose_shop_name";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBaseSharedPreferencesHelper = new BaseSharedPreferencesHelper(getActivity());
        mListView.setDividerHeight(0);
        mRequestFailView.setEmptyButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshData(true);
            }
        });
    }

    @Override
    public void refreshData(boolean hasLoadingAnim) {
        new AccessoryApiClient().getShopList(mPage, 15, mBaseSharedPreferencesHelper.getLatitudeBD(), mBaseSharedPreferencesHelper.getLongitudeBD(),
                getLoadingAnimResponseHandler(true));
    }

    @Override
    protected void onSuccessResponse(String response) {
        ArrayList<PickupShop> pickupShops = JSONUtils.fromJson(response, new TypeToken<ArrayList<PickupShop>>() {
        });
        if (pickupShops != null && pickupShops.size() > 0) {
            mPtrHelper.setHasMore(!(pickupShops.size() < 15));
            if (mPage == 1) {
                mPickupShops = pickupShops;
                mLaborCostDialog = new LaborCostDialog(getActivity());
                mAccessoryShopListAdapter = new AccessoryShopListAdapter(getActivity(), mPickupShops, mLaborCostDialog);
                mListView.setAdapter(mAccessoryShopListAdapter);
            } else {
                mPickupShops.addAll(pickupShops);
                mAccessoryShopListAdapter.notifyDataSetChanged();
            }
        } else {
            if (mPage == 1) {
                showNoData();
            } else {
                mPtrHelper.setHasMore(false);
            }
        }
    }

    @Override
    protected String getEmptyDescription() {
        return "暂无门店";
    }

    @Override
    public String fragmentDescription() {
        return "用品选择门店";
    }

}
