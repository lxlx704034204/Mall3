package com.hxqc.mall.fragment.me;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.api.ApiClient;
import com.hxqc.mall.core.adapter.UserCommentAdapter;
import com.hxqc.mall.core.fragment.SwipeRefreshForRecyclerFragment;
import com.hxqc.mall.core.model.CommentForList;
import com.hxqc.mall.core.util.SharedPreferencesHelper;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * 说明: 评论基类fragment
 *
 * author: 吕飞
 * since: 2015-04-02
 * Copyright:恒信汽车电子商务有限公司
 */
public abstract class UserCommentForRecyclerFragment extends SwipeRefreshForRecyclerFragment {
    public static final String UNCOMMENT = "0";
    public static final String COMMENTED = "1";
    public ArrayList<CommentForList > mCommentForLists = new ArrayList<>();
    public SharedPreferencesHelper mSharedPreferencesHelper;
    public ApiClient mApiClient;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mSharedPreferencesHelper = new SharedPreferencesHelper(mContext);
        mApiClient = new ApiClient();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mSharedPreferencesHelper.getCommentChange()) {
            mPage = 1;
            refreshData(true);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new UserCommentAdapter(mCommentForLists, mContext);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onSuccessResponse(String response) {
        ArrayList<CommentForList> mMoreCommentForLists = JSONUtils.fromJson(response, new TypeToken<ArrayList<CommentForList>>() {
        });

        if (mMoreCommentForLists == null) {
            return;
        }
        mPtrHelper.setHasMore((mMoreCommentForLists.size() >= PER_PAGE));
        if (mPage == DEFAULT_PATE) {
            mCommentForLists.clear();
            if (mMoreCommentForLists.size() > 0) {
                mSharedPreferencesHelper.setCommentChange(false);
            } else {
//                showNoData();
                return;
            }
        }
        mCommentForLists.addAll(mMoreCommentForLists);
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

    }
    @Override
    public boolean hasMore() {
        return mPtrHelper.isHasMore();
    }
}
