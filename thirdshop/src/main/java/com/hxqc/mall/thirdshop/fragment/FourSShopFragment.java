package com.hxqc.mall.thirdshop.fragment;

import android.content.Context;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.fragment.SwipeRefreshFragmentForListView;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.FourSShop;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.views.FourSShopItem;
import com.hxqc.util.JSONUtils;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;

/**
 * 说明:4S店店铺列表
 *
 * @author: 吕飞
 * @since: 2015-07-31
 * Copyright:恒信汽车电子商务有限公司
 */
public class FourSShopFragment extends SwipeRefreshFragmentForListView {
    ArrayList<FourSShop> mFourSShops;
    QuickAdapter<FourSShop> mFourSShopAdapter;
    ThirdPartShopClient mThirdPartShopClient;
    String mSiteID;

    @Override
    public void onRefresh() {
        refreshData(true);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mSiteID = getArguments().getString(ActivitySwitcherThirdPartShop.SITE_ID);
        mThirdPartShopClient = new ThirdPartShopClient();
    }

    @Override
    public void onLoadMore() {
        mPtrHelper.refreshComplete(mPtrFrameLayoutView);
    }

    private void showList() {
        mPtrFrameLayoutView.setVisibility(View.VISIBLE);
        mRequestFailView.setVisibility(View.GONE);
        mFourSShopAdapter = new QuickAdapter<FourSShop>(getActivity(), R.layout.item_4s_shop, mFourSShops) {
            @Override
            protected void convert(BaseAdapterHelper helper, FourSShop item) {
                ((FourSShopItem) helper.getView(R.id.item_4s_shop)).addData(item);
            }
        };
        mListView.setAdapter(mFourSShopAdapter);
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                mListener.completeChooseModel( mAutoModels.get(position).model_name, mAutoModels.get(position).model_id);
//            }
//        });

    }

    @Override
    public void refreshData(boolean hasLoadingAnim) {
        mThirdPartShopClient.getIndexShopList(24, mSiteID, getLoadingAnimResponseHandler(true));
    }

    @Override
    protected void onSuccessResponse(String response) {
        mFourSShops = JSONUtils.fromJson(response, new TypeToken<ArrayList<FourSShop>>() {
        });
        if (mFourSShops != null && mFourSShops.size() > 0) {
            mPtrFrameLayoutView.setVisibility(View.VISIBLE);
            mRequestFailView.setVisibility(View.GONE);
            showList();
        } else {
            showNoData();
        }
    }

    @Override
    protected String getEmptyDescription() {
        return "暂无数据";
    }

    @Override
    public String fragmentDescription() {
        return "4s店店铺列表";
    }
}
