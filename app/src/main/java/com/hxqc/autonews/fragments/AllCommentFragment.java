package com.hxqc.autonews.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.hxqc.autonews.activities.AllCommentActivity;
import com.hxqc.autonews.adapter.AllCommentAdapter;
import com.hxqc.autonews.model.Comment;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.SwipeRefreshFragmentForListView;
import com.hxqc.mall.core.util.ACache;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

/**
 * 全部评价 列表
 * Created by huangyi on 16/10/24.
 */
public class AllCommentFragment extends SwipeRefreshFragmentForListView {

    AllCommentActivity mActivity;
    ArrayList<Comment> mComment;
    AllCommentAdapter mAdapter;
    AllCommentAdapter.OnClickListener mListener;
    ACache mCache;
    ArrayList<String> mTemp; //多页数据<单页数据>

    public void setListener(AllCommentAdapter.OnClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActivity = (AllCommentActivity) getActivity();
        mComment = new ArrayList<>();
        mAdapter = new AllCommentAdapter(getContext(), mComment, mListener);
        mListView.setAdapter(mAdapter);
        mRequestFailView.setEmptyButtonClick("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });
        mCache = ACache.get(getContext());
        String cache = mCache.getAsString(mActivity.infoID);
        if (!TextUtils.isEmpty(cache))
            mTemp = JSONUtils.fromJson(cache, new TypeToken<ArrayList<String>>() {
            });
    }

    @Override
    public void refreshData(boolean hasLoadingAnim) {
        //第一页加载 如果有缓存 不显示加载动画
        if (mPage == DEFAULT_PATE && null != mTemp && mTemp.size() != 0) {
            mComment.clear();
            mComment.addAll(JSONUtils.fromJson(mTemp.get(0), new TypeToken<ArrayList<Comment>>() {
            }));
            mAdapter.notifyDataSetChanged();
            if (mTemp.size() > mPage) {
                mPtrHelper.setHasMore(true);
            } else {
                mPtrHelper.setHasMore(false);
            }
            mActivity.client.requestAutoInfoCommentP(mActivity.infoID, mPage, getHandler(false));
            return;
        }

        mActivity.client.requestAutoInfoCommentP(mActivity.infoID, mPage, getHandler(hasLoadingAnim));
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
        ArrayList<Comment> temp = JSONUtils.fromJson(response, new TypeToken<ArrayList<Comment>>() {
        });

        //初次加载
        if (mPage == DEFAULT_PATE) {
            mComment.clear();
            if (null == temp || temp.size() == 0) {
                showNoData();
                if (null == mTemp) mTemp = new ArrayList<>();
                mTemp.clear();
                mCache.put(mActivity.infoID, "");
            } else {
                mComment.addAll(temp);
                mPtrFrameLayoutView.setVisibility(View.VISIBLE);
                mRequestFailView.setVisibility(View.GONE);
                if (null != mTemp && mTemp.size() != 0) {
                    mTemp.remove(0);
                    mTemp.add(0, response);
                } else {
                    mTemp = new ArrayList<>();
                    mTemp.add(response);
                }
                mCache.put(mActivity.infoID, JSONUtils.toJson(mTemp));
            }
            mAdapter.notifyDataSetChanged();
        } else {
            if (null != mTemp && mTemp.size() >= mPage) mTemp.remove(mPage - 1);
            if (null != temp && temp.size() != 0) {
                mComment.addAll(temp);
                mAdapter.notifyDataSetChanged();
                mTemp.add(mPage - 1, response);
            }
            mCache.put(mActivity.infoID, JSONUtils.toJson(mTemp));
        }

        if (null != temp && temp.size() != 0) {
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
            mComment.addAll(JSONUtils.fromJson(mTemp.get(mPage - 1), new TypeToken<ArrayList<Comment>>() {
            }));
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
        return "全部评价列表Fragment";
    }

}
