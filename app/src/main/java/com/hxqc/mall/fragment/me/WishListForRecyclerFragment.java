package com.hxqc.mall.fragment.me;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.api.ApiClient;
import com.hxqc.mall.core.adapter.WishListAdapter;
import com.hxqc.mall.core.fragment.SwipeRefreshForRecyclerFragment;
import com.hxqc.mall.core.model.CollectInfo;
import com.hxqc.mall.core.util.SharedPreferencesHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * 说明:我的关注
 *
 * author: 吕飞
 * since: 2015-07-28
 * Copyright:恒信汽车电子商务有限公司
 */
public class WishListForRecyclerFragment extends SwipeRefreshForRecyclerFragment {
    ArrayList< CollectInfo > mCollectInfos;//关注列表
    ApiClient mApiClient;
    SharedPreferencesHelper mSharedPreferencesHelper;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mSharedPreferencesHelper = new SharedPreferencesHelper(mContext);
        mApiClient = new ApiClient();
    }

    @Override
    public void refreshData(boolean hasLoadingAnim) {
        mApiClient.wishList( getLoadingAnimResponseHandler(hasLoadingAnim));
    }

    @Override
    protected void onSuccessResponse(String response) {
        mCollectInfos = JSONUtils.fromJson(response, new TypeToken< ArrayList< CollectInfo > >() {
        });
        if (mCollectInfos != null && mCollectInfos.size() > 0) {
            mPtrFrameLayoutView.setVisibility(View.VISIBLE);
            mRequestFailView.setVisibility(View.GONE);
            showList();
        } else {
            showNoData();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRequestFailView.setEmptyButtonClick("随便逛逛", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcher.toMain(getContext(),0);
            }
        });
    }

    @Override
    protected String getEmptyDescription() {
        return  "您还没有关注的车辆";
    }

    @Override
    public String fragmentDescription() {
        return "我的关注";
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (mAdapter != null) {
//            upDate(true);
//        }
    }

    private void showList() {
        mAdapter = new WishListAdapter(mCollectInfos, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

}
