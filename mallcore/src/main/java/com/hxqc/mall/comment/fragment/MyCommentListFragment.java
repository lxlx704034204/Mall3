package com.hxqc.mall.comment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.comment.adapter.MyCommentListAdapter;
import com.hxqc.mall.comment.api.CommentApi;
import com.hxqc.mall.comment.model.MyCommentList;
import com.hxqc.mall.core.fragment.SwipeRefreshForRecyclerFragment;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-06-06
 * FIXME
 * Todo 我的评论列表
 */
public class MyCommentListFragment extends SwipeRefreshForRecyclerFragment {

    private CommentApi commentApi;
    private ArrayList<MyCommentList> myCommentLists;
    private ArrayList<MyCommentList> showMyCommentLists = new ArrayList<>();
    private MyCommentListAdapter myCommentListAdapter;
    private boolean isLoadMore = false ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commentApi = new CommentApi();
        myCommentListAdapter = new MyCommentListAdapter(getContext());

    }

    @Override
    public void refreshData(boolean hasLoadingAnim) {
        commentApi.MyCommentList(PER_PAGE, mPage, getLoadingAnimResponseHandler(hasLoadingAnim));
    }

    @Override
    protected void onSuccessResponse(String response) {
        myCommentLists = JSONUtils.fromJson(response,new TypeToken<ArrayList<MyCommentList>>(){});
        if(myCommentLists == null || myCommentLists.size() == 0){
            showNoData();
        }else {
            if(myCommentLists.size() >= PER_PAGE){
                mPtrHelper.setHasMore(true);
            }
            if(mPage == DEFAULT_PATE){
                showMyCommentLists.clear();
            }
            showMyCommentLists.addAll(myCommentLists);
            for(int i = 0 ; i < showMyCommentLists.size(); i++){
                showMyCommentLists.get(i).positionTag = i;
            }
            myCommentListAdapter.initDate(showMyCommentLists);

            if(isLoadMore){
                myCommentListAdapter.notifyDataSetChanged();
            }else {
                mRecyclerView.setAdapter(myCommentListAdapter);

            }
        }
    }

    @Override
    protected String getEmptyDescription() {
        mRequestFailView.setEmptyButtonClick("随便逛逛", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitchBase.toMain(getActivity(), 0);
            }
        });
        return "您还没有发布任何评价";
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        isLoadMore = true;
    }

    @Override
    public String fragmentDescription() {
        return null;
    }
}
