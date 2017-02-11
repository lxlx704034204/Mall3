package com.hxqc.mall.thirdshop.accessory.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.fragment.SwipeRefreshFragmentForListView;
import com.hxqc.mall.thirdshop.accessory.activity.AccessorySaleActivity;
import com.hxqc.mall.thirdshop.accessory.adapter.AccessorySaleAdapter;
import com.hxqc.mall.thirdshop.accessory.api.AccessoryApiClient;
import com.hxqc.mall.thirdshop.accessory.model.ProductList;
import com.hxqc.mall.thirdshop.accessory.utils.ActivitySwitcherAccessory;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * 用品销售 用品列表 旧版本
 * Created by huangyi on 16/5/30.
 */
public class AccessorySaleFragment extends SwipeRefreshFragmentForListView {

    AccessorySaleActivity mActivity;
    ArrayList<ProductList> mProductList = new ArrayList<>();
    AccessorySaleAdapter mAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = (AccessorySaleActivity) getActivity();
        mAdapter = new AccessorySaleAdapter(mContext, mProductList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivitySwitcherAccessory.toProductDetail(mContext, mProductList.get(position).productID);
            }
        });
        mRequestFailView.setEmptyButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.clearCondition();
                refreshPage();
                refreshData(false);
            }
        });
        mPtrHelper.setHasMore(true);
    }

    public void refreshPage() {
        mPage = DEFAULT_PATE;
    }

    @Override
    public void refreshData(boolean hasLoadingAnim) {
        new AccessoryApiClient().getProductList(mPage, mActivity.class1stID, mActivity.class2ndID, mActivity.brandID, mActivity.seriesID, mActivity.priceOrder,
                getLoadingAnimResponseHandler(hasLoadingAnim));
    }

    @Override
    protected void onSuccessResponse(String response) {
        ArrayList<ProductList> temp = JSONUtils.fromJson(response, new TypeToken<ArrayList<ProductList>>() {
        });
        //初次加载
        if (mPage == DEFAULT_PATE) {
            mProductList.clear();
            if (null == temp || temp.size() == 0) {
                showNoData();
            } else {
                mPtrFrameLayoutView.setVisibility(View.VISIBLE);
                mRequestFailView.setVisibility(View.GONE);
                mProductList.addAll(temp);
            }
            mAdapter.notifyDataSetChanged();
        } else {
            if (null != temp && temp.size() != 0) {
                mProductList.addAll(temp);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected String getEmptyDescription() {
        return "搜索无结果";
    }

    @Override
    public String fragmentDescription() {
        return "用品销售用品列表Fragment";
    }

}
