package com.hxqc.mall.usedcar.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.hxqc.mall.core.fragment.SwipeRefreshFragmentForListView;
import com.hxqc.mall.usedcar.activity.BuyCarActivity;
import com.hxqc.mall.usedcar.adapter.BuyCarListAdapter;
import com.hxqc.mall.usedcar.api.UsedCarApiClient;
import com.hxqc.mall.usedcar.model.CarFilterResultModel;
import com.hxqc.mall.usedcar.model.UsedCarBase;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;
import com.hxqc.mall.usedcar.utils.UsedCarSPHelper;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * 买车 车辆列表
 * Created by huangyi on 15/10/21.
 */
public class BuyCarListFragment extends SwipeRefreshFragmentForListView {

    public Integer total = -1; //请求返回的数据总条数

    BuyCarActivity mActivity;
    ArrayList<UsedCarBase> mUsedCarBase = new ArrayList<>();
    BuyCarListAdapter mAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = (BuyCarActivity) getActivity();
        mAdapter = new BuyCarListAdapter(mContext, mUsedCarBase);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mUsedCarBase.get(position).isPersonal()) {
                    UsedCarActivitySwitcher.toPersonalCarDetail(mContext, mUsedCarBase.get(position).car_source_no);
                } else {
                    UsedCarActivitySwitcher.toPlatformCarDetail(mContext, mUsedCarBase.get(position).car_source_no);
                }
            }
        });
        mRequestFailView.setEmptyButtonClick("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });
    }

    public void refreshPage() {
        mPage = DEFAULT_PATE;
    }

    @Override
    public void refreshData(boolean hasLoadingAnim) {
        new UsedCarApiClient().getBuyCar(mActivity.selectedTipMap, new UsedCarSPHelper(mContext).getCity(), mActivity.keyword, mPage,
                getLoadingAnimResponseHandler(hasLoadingAnim));
    }

    @Override
    protected void onSuccessResponse(String response) {
        CarFilterResultModel temp = JSONUtils.fromJson(response, CarFilterResultModel.class);
        //初次加载
        if (mPage == DEFAULT_PATE) {
            mUsedCarBase.clear();
            if (null == temp || temp.data.size() == 0) {
                total = 0;
                showNoData();
                mActivity.hotKeywordsView.setVisibility(View.VISIBLE);
            } else {
                total = Integer.valueOf(temp.total);
                mUsedCarBase.addAll(temp.data);
                mPtrFrameLayoutView.setVisibility(View.VISIBLE);
                mRequestFailView.setVisibility(View.GONE);
                mActivity.hotKeywordsView.setVisibility(View.GONE);
            }
            mAdapter.notifyDataSetChanged();
        } else {
            if (null != temp && temp.data.size() != 0) {
                total = Integer.valueOf(temp.total);
                mUsedCarBase.addAll(temp.data);
                mAdapter.notifyDataSetChanged();
            }
        }

        if (mUsedCarBase.size() < total) {
            mPtrHelper.setHasMore(true);
        }else {
            mPtrHelper.setHasMore(false);
        }
    }

    @Override
    protected String getEmptyDescription() {
        return "搜索无结果";
    }

    @Override
    public String fragmentDescription() {
        return "买车车辆列表Fragment";
    }

}
