package com.hxqc.autonews.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.autonews.activities.CommentDetailActivity;
import com.hxqc.autonews.adapter.CommentDetailAdapter;
import com.hxqc.autonews.model.Comment;
import com.hxqc.autonews.model.CommentDetail;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.SwipeRefreshFragmentForListView;
import com.hxqc.mall.core.util.ACache;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.util.DisplayTools;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import hxqc.mall.R;

/**
 * 评论详情 列表
 * Created by huangyi on 16/10/24.
 */
public class CommentDetailFragment extends SwipeRefreshFragmentForListView {

    CommentDetailActivity mActivity;
    CircleImageView mPhotoView;
    TextView mNameView, mDateView, mContentView;
    ArrayList<Comment> mComment;
    CommentDetailAdapter mAdapter;
    CommentDetailAdapter.OnClickListener mListener;
    ACache mCache;
    ArrayList<String> mTemp; //多页数据<单页数据>

    public void setListener(CommentDetailAdapter.OnClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActivity = (CommentDetailActivity) getActivity();
        //addHeader
        View header = View.inflate(getContext(), R.layout.item_all_comment, null);
        mPhotoView = (CircleImageView) header.findViewById(R.id.comment_photo);
        mNameView = (TextView) header.findViewById(R.id.comment_name);
        mDateView = (TextView) header.findViewById(R.id.comment_date);
        mContentView = (TextView) header.findViewById(R.id.comment_content);
        header.findViewById(R.id.comment_more).setVisibility(View.GONE);
        mListView.addHeaderView(header);
        //setAdapter
        mComment = new ArrayList<>();
        mAdapter = new CommentDetailAdapter(getContext(), mComment, mListener);
        mListView.setAdapter(mAdapter);
        mListView.setDivider(null);
        mListView.setDividerHeight(0);
        mListView.setPadding(0, 0, 0, DisplayTools.dip2px(getContext(), 10));
        mListView.setClipToPadding(false);
        mRequestFailView.setEmptyButtonClick("返回", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });
        mCache = ACache.get(getContext());
        String cache = mCache.getAsString(mActivity.infoID + mActivity.pCommentID);
        if (!TextUtils.isEmpty(cache))
            mTemp = JSONUtils.fromJson(cache, new TypeToken<ArrayList<String>>() {
            });
    }

    @Override
    public void refreshData(boolean hasLoadingAnim) {
        //第一页加载 如果有缓存 不显示加载动画
        if (mPage == DEFAULT_PATE && null != mTemp && mTemp.size() != 0) {
            CommentDetail temp = JSONUtils.fromJson(mTemp.get(0), CommentDetail.class);
            //header
            ImageUtil.setImageSquare(mContext, mPhotoView, temp.parentComment.commentUser.userAvatar);
            mNameView.setText(temp.parentComment.commentUser.nickName.trim());
            mDateView.setText(temp.parentComment.time.trim());
            mContentView.setText(temp.parentComment.content.trim());
            //list
            mComment.clear();
            mComment.addAll(temp.chileCommentlist);
            mAdapter.notifyDataSetChanged();
            if (mTemp.size() > mPage) {
                mPtrHelper.setHasMore(true);
            } else {
                mPtrHelper.setHasMore(false);
            }
            mActivity.client.requestAutoInfoCommentC(mActivity.infoID, mActivity.pCommentID, mPage, getHandler(false));
            return;
        }

        mActivity.client.requestAutoInfoCommentC(mActivity.infoID, mActivity.pCommentID, mPage, getHandler(hasLoadingAnim));
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
        CommentDetail temp = JSONUtils.fromJson(response, CommentDetail.class);

        //初次加载
        if (mPage == DEFAULT_PATE) {
            mComment.clear();
            if (null == temp || temp.chileCommentlist.size() == 0) {
                showNoData();
                if (null == mTemp) mTemp = new ArrayList<>();
                mTemp.clear();
                mCache.put(mActivity.infoID + mActivity.pCommentID, "");
            } else {
                //header
                ImageUtil.setImageSquare(mContext, mPhotoView, temp.parentComment.commentUser.userAvatar);
                mNameView.setText(temp.parentComment.commentUser.nickName.trim());
                mDateView.setText(temp.parentComment.time.trim());
                mContentView.setText(temp.parentComment.content.trim());
                //list
                mComment.addAll(temp.chileCommentlist);
                mPtrFrameLayoutView.setVisibility(View.VISIBLE);
                mRequestFailView.setVisibility(View.GONE);
                if (null != mTemp && mTemp.size() != 0) {
                    mTemp.remove(0);
                    mTemp.add(0, response);
                } else {
                    mTemp = new ArrayList<>();
                    mTemp.add(response);
                }
                mCache.put(mActivity.infoID + mActivity.pCommentID, JSONUtils.toJson(mTemp));
            }
            mAdapter.notifyDataSetChanged();
        } else {
            if (null != mTemp && mTemp.size() >= mPage) mTemp.remove(mPage - 1);
            if (null != temp && temp.chileCommentlist.size() != 0) {
                mComment.addAll(temp.chileCommentlist);
                mAdapter.notifyDataSetChanged();
                mTemp.add(mPage - 1, response);
            }
            mCache.put(mActivity.infoID + mActivity.pCommentID, JSONUtils.toJson(mTemp));
        }

        if (null != temp && temp.chileCommentlist.size() != 0) {
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
            CommentDetail temp = JSONUtils.fromJson(mTemp.get(mPage - 1), CommentDetail.class);
            mComment.addAll(temp.chileCommentlist);
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
        return "评论详情列表Fragment";
    }

}
