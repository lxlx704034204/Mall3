package com.hxqc.mall.thirdshop.accessory4s.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.SwipeRefreshFragmentForListView;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory4s.activity.AccessorySaleFromHomeActivity;
import com.hxqc.mall.thirdshop.accessory4s.adapter.AccessorySaleNewAdapter;
import com.hxqc.mall.thirdshop.accessory4s.api.Accessory4SApiClient;
import com.hxqc.mall.thirdshop.accessory4s.model.ProductList4S;
import com.hxqc.mall.thirdshop.accessory4s.utils.ActivitySwitcherAccessory4S;
import com.hxqc.mall.thirdshop.utils.AreaSiteUtil;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * 用品销售 用品列表
 * Created by huangyi on 16/5/30.
 */
public class AccessorySale4SFragment extends SwipeRefreshFragmentForListView {

    AccessorySaleFromHomeActivity mActivity;
    ArrayList< ProductList4S > mProductList = new ArrayList<>();
    AccessorySaleNewAdapter mAdapter;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = (com.hxqc.mall.thirdshop.accessory4s.activity.AccessorySaleFromHomeActivity) getActivity();
        mAdapter = new AccessorySaleNewAdapter(mContext, mProductList, true);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
                ActivitySwitcherAccessory4S.toProductDetail(mContext, "", mProductList.get(position).productID);
            }
        });
        mRequestFailView.setEmptyDescription("没有符合条件的商品", R.mipmap.ic_acc_empty);
        mRequestFailView.setEmptyButtonClick("随便逛逛", new View.OnClickListener() {
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
        new Accessory4SApiClient().productList(mPage + "", mActivity.brand, mActivity.series, mActivity.helper.getLatitudeBD(), mActivity.helper.getLongitudeBD(), AreaSiteUtil.getInstance(mContext).getSiteID(), mActivity.shopID, mActivity.class1stID, mActivity.class2ndID, mActivity.priceOrder, getLoadingAnimResponseHandler(hasLoadingAnim));
    }


    @Override
    protected void onSuccessResponse(String response) {
        ArrayList< ProductList4S > temp = JSONUtils.fromJson(response, new TypeToken< ArrayList< ProductList4S > >() {
        });
        //初次加载
        if (mPage == DEFAULT_PATE) {
            mProductList.clear();
            if (null == temp || temp.size() == 0) {
                mPtrFrameLayoutView.setVisibility(View.GONE);
                mRequestFailView.setVisibility(View.VISIBLE);
                mRequestFailView.setRequestType(RequestFailView.RequestViewType.empty);
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
        return "";
    }


    @Override
    public String fragmentDescription() {
        return "用品销售用品列表Fragment";
    }

}
