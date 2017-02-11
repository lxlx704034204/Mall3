package com.hxqc.fastreqair.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.reflect.TypeToken;
import com.hxqc.fastreqair.adapter.WashCarCommentListAdapter;
import com.hxqc.fastreqair.api.CarWashApiClient;
import com.hxqc.fastreqair.model.CarWashComment;
import com.hxqc.mall.core.fragment.SwipeRefreshForRecyclerFragment;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * @Author : 钟学东
 * @Since : 2016-05-19
 * FIXME
 * Todo
 */
public class WashCarCommentListFragment extends SwipeRefreshForRecyclerFragment {

    private CarWashApiClient carWashApiClient;
    private ArrayList<CarWashComment> carWashComments;
    private ArrayList<CarWashComment> showCarWashComments =  new ArrayList<>();
    private String shopID;
    private WashCarCommentListAdapter commentListAdapter;


    private boolean isLoadMore = false ;

    public WashCarCommentListFragment() {
    }

    @SuppressWarnings("ValidFragment")
    public WashCarCommentListFragment(String shopID, Context context){
        this.shopID = shopID;
        commentListAdapter = new WashCarCommentListAdapter(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carWashApiClient = new CarWashApiClient();

    }

    @Override
    public void refreshData(boolean hasLoadingAnim) {
        carWashApiClient.ShopCommentList(mPage,PER_PAGE,shopID,getLoadingAnimResponseHandler(hasLoadingAnim));
    }

    @Override
    protected void onSuccessResponse(String response) {
        carWashComments = JSONUtils.fromJson(response,new TypeToken<ArrayList<CarWashComment>>(){});

        if(carWashComments.size() >= PER_PAGE){
            mPtrHelper.setHasMore(true);
        }else {
            mPtrHelper.setHasMore(false);
        }
        DebugLog.i("TAG","mPage :" + mPage);
        if(mPage == DEFAULT_PATE){
            showCarWashComments.clear();
        }
        showCarWashComments.addAll(carWashComments);
        for(int i = 0 ; i < showCarWashComments.size(); i++){
            showCarWashComments.get(i).positionTag = i;
        }
        commentListAdapter.setCarWashComments(showCarWashComments);
        if(isLoadMore){
            commentListAdapter.notifyDataSetChanged();
        }else {
            mRecyclerView.setAdapter(commentListAdapter);
        }

    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        isLoadMore = true;
    }

    @Override
    protected String getEmptyDescription() {
        return "暂无评论";
    }

    @Override
    public String fragmentDescription() {
        return "评论列表";
    }
}
