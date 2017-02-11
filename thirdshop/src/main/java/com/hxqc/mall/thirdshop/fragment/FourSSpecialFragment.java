package com.hxqc.mall.thirdshop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.SwipeRefreshForRecyclerFragment;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.SingleSeckillItem;
import com.hxqc.mall.thirdshop.utils.AreaSiteUtil;
import com.hxqc.mall.thirdshop.views.adpter.FlashSaleAdapter;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * 说明:4s限时特价车
 *
 * @author: 吕飞
 * @since: 2016-11-22
 * Copyright:恒信汽车电子商务有限公司
 */

public class FourSSpecialFragment extends SwipeRefreshForRecyclerFragment {
    ArrayList<SingleSeckillItem> mSingleSeckillItems = new ArrayList<>();
    ThirdPartShopClient mThirdPartShopClient;
    AreaSiteUtil mAreaSiteUtil;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mThirdPartShopClient = new ThirdPartShopClient();
        mAreaSiteUtil = new AreaSiteUtil(activity);
    }

    @Override
    public void refreshData(boolean hasLoadingAnim) {
        String cityGroup = mAreaSiteUtil.getSiteName();
        String cityGroupID = mAreaSiteUtil.getSiteID();
        if (!cityGroup.contains("武汉")) {
            mThirdPartShopClient.getIndexSeckillList(cityGroupID, mPage, 15, getLoadingAnimResponseHandler(hasLoadingAnim));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new FlashSaleAdapter(getActivity(), mSingleSeckillItems);
    }

    @Override
    protected void onSuccessResponse(String response) {
        ArrayList<SingleSeckillItem> singleSeckillItems = JSONUtils.fromJson(response, new TypeToken<ArrayList<SingleSeckillItem>>() {
        });
        if (singleSeckillItems != null) {
            mPtrHelper.setHasMore(singleSeckillItems.size() < 15);
            if (mPage == DEFAULT_PATE) {
                mSingleSeckillItems.clear();
            }
            mSingleSeckillItems.addAll(singleSeckillItems);
            mAdapter.notifyDataSetChanged();
            mPtrFrameLayoutView.setVisibility(View.VISIBLE);
            mRequestFailView.setVisibility(View.GONE);
        } else {
            mPtrHelper.setHasMore(false);
        }
        if (mSingleSeckillItems.size() == 0) {
            mPtrFrameLayoutView.setVisibility(View.GONE);
            mRequestFailView.setVisibility(View.VISIBLE);
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        }
    }

    @Override
    protected String getEmptyDescription() {
        return "暂无数据";
    }

    @Override
    public String fragmentDescription() {
        return "4s限时特价车";
    }
}
