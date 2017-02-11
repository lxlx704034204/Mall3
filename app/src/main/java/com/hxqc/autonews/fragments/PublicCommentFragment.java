package com.hxqc.autonews.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.hxqc.autonews.activities.PublicCommentActivity;
import com.hxqc.autonews.adapter.PublicCommentGridAdapter;
import com.hxqc.autonews.adapter.PublicCommentListAdapter;
import com.hxqc.autonews.model.PublicComment;
import com.hxqc.autonews.model.UserGradeComment;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.SwipeRefreshFragmentForListView;
import com.hxqc.mall.core.util.ACache;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.GridViewNoSlide;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 口碑评价 列表
 * Created by huangyi on 16/10/24.
 */
public class PublicCommentFragment extends SwipeRefreshFragmentForListView {

    PublicCommentActivity mActivity;
    GridViewNoSlide mGridView;
    ArrayList<UserGradeComment> mComment;
    PublicCommentListAdapter mAdapter;
    ACache mCache;
    ArrayList<String> mTemp; //多页数据<单页数据>

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActivity = (PublicCommentActivity) getActivity();
        //addHeader
        View header = View.inflate(getContext(), R.layout.layout_public_comment, null);
        mGridView = (GridViewNoSlide) header.findViewById(R.id.comment_grid);
        mListView.addHeaderView(header);
        //setAdapter
        mComment = new ArrayList<>();
        mAdapter = new PublicCommentListAdapter(false, getContext(), mComment, null);
        mListView.setAdapter(mAdapter);
        mRequestFailView.setEmptyButtonClick("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });
        mCache = ACache.get(getContext());
        String cache = mCache.getAsString(mActivity.extID + mActivity.brand + mActivity.series);
        if (!TextUtils.isEmpty(cache))
            mTemp = JSONUtils.fromJson(cache, new TypeToken<ArrayList<String>>() {
            });
    }

    @Override
    public void refreshData(boolean hasLoadingAnim) {
        //第一页加载 如果有缓存 不显示加载动画
        if (mPage == DEFAULT_PATE && null != mTemp && mTemp.size() != 0) {
            PublicComment temp = JSONUtils.fromJson(mTemp.get(0), PublicComment.class);
            //header
            mGridView.setAdapter(new PublicCommentGridAdapter(getContext(), temp.grade));
            //list
            mComment.clear();
            mComment.addAll(temp.userGradeComment);
            mAdapter.notifyDataSetChanged();
            if (mTemp.size() > mPage) {
                mPtrHelper.setHasMore(true);
            } else {
                mPtrHelper.setHasMore(false);
            }
            mActivity.client.requestAutoGrade(mActivity.extID, mActivity.brand, mActivity.series, mPage, getHandler(false));
            return;
        }

        mActivity.client.requestAutoGrade(mActivity.extID, mActivity.brand, mActivity.series, mPage, getHandler(hasLoadingAnim));
    }

    protected LoadingAnimResponseHandler getHandler(boolean isShowAnim) {
        return new LoadingAnimResponseHandler(mContext, isShowAnim) {
            @Override
            public void onSuccess(String response) {
                onSuccessResponse(response);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mPtrHelper.refreshComplete(mPtrFrameLayoutView);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                onFailureResponse();
            }
        };
    }

    @Override
    protected void onSuccessResponse(String response) {
        PublicComment temp = JSONUtils.fromJson(response, PublicComment.class);

        //初次加载
        if (mPage == DEFAULT_PATE) {
            mComment.clear();
            if (null == temp || temp.userGradeComment.size() == 0) {
                showNoData();
                if (null == mTemp) mTemp = new ArrayList<>();
                mTemp.clear();
                mCache.put(mActivity.extID + mActivity.brand + mActivity.series, "");
            } else {
                //header
                mGridView.setAdapter(new PublicCommentGridAdapter(getContext(), temp.grade));
                //list
                mComment.addAll(temp.userGradeComment);
                mPtrFrameLayoutView.setVisibility(View.VISIBLE);
                mRequestFailView.setVisibility(View.GONE);
                if (null != mTemp && mTemp.size() != 0) {
                    mTemp.remove(0);
                    mTemp.add(0, response);
                } else {
                    mTemp = new ArrayList<>();
                    mTemp.add(response);
                }
                mCache.put(mActivity.extID + mActivity.brand + mActivity.series, JSONUtils.toJson(mTemp));
            }
            mAdapter.notifyDataSetChanged();
        } else {
            if (null != mTemp && mTemp.size() >= mPage) mTemp.remove(mPage - 1);
            if (null != temp && temp.userGradeComment.size() != 0) {
                mComment.addAll(temp.userGradeComment);
                mAdapter.notifyDataSetChanged();
                mTemp.add(mPage - 1, response);
            }
            mCache.put(mActivity.extID + mActivity.brand + mActivity.series, JSONUtils.toJson(mTemp));
        }

        if (null != temp && temp.userGradeComment.size() != 0) {
            mPtrHelper.setHasMore(true);
        } else {
            mPtrHelper.setHasMore(false);
        }
    }

    private void onFailureResponse() {
        if (mPage == DEFAULT_PATE && (null == mTemp || mTemp.size() == 0)) {
            mPtrFrameLayoutView.setVisibility(View.GONE);
            mRequestFailView.setVisibility(View.VISIBLE);
            mRequestFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRequestFailView.setVisibility(View.GONE);
                    refreshData(true);
                }
            });
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
        }

        //取缓存数据
        if (mPage != DEFAULT_PATE && null != mTemp && mTemp.size() >= mPage) {
            PublicComment temp = JSONUtils.fromJson(mTemp.get(mPage - 1), PublicComment.class);
            mComment.addAll(temp.userGradeComment);
            mAdapter.notifyDataSetChanged();
            if (mTemp.size() > mPage) {
                mPtrHelper.setHasMore(true);
            } else {
                mPtrHelper.setHasMore(false);
            }
        }
    }

    @Override
    protected String getEmptyDescription() {
        return "搜索无结果";
    }

    @Override
    public String fragmentDescription() {
        return "口碑评价列表Fragment";
    }

}
