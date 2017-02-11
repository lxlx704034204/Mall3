package com.hxqc.autonews.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.hxqc.autonews.widget.LazyFragment;
import com.hxqc.mall.comment.adapter.MyCommentListAdapter;
import com.hxqc.mall.comment.api.CommentApi;
import com.hxqc.mall.comment.model.MyCommentList;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import hxqc.mall.R;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * 我的评价 订单评价
 * Created by huangyi on 15/12/25.
 */
public class OrderCommentPagerFragment extends LazyFragment implements OnRefreshHandler {

    /**
     * 视图是否加载过
     **/
    private boolean isViewLoaded;
    /**
     * 数据是否加载过
     **/
    private boolean isDataLoaded;

    private CommentApi commentApi;
    private ArrayList<MyCommentList> myCommentLists;
    private ArrayList<MyCommentList> showMyCommentLists = new ArrayList<>();

    protected static final int DEFAULT_PATE = 1;
    protected final int PER_PAGE = 15;
    protected int mPage = DEFAULT_PATE;//当前页

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    MyCommentListAdapter mAdapter;

    protected PtrFrameLayout mPtrFrameLayoutView;
    protected UltraPullRefreshHeaderHelper mPtrHelper;
    protected RequestFailView mRequestFailView;

    private boolean isLoadMore = false ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_comment_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        commentApi = new CommentApi();

        mPtrFrameLayoutView = (PtrFrameLayout) view.findViewById(R.id.comment_ptr);
        mPtrHelper = new UltraPullRefreshHeaderHelper(getActivity(), mPtrFrameLayoutView, this);
        mPtrHelper.setOnRefreshHandler(this);

        mRecyclerView = (RecyclerView) view.findViewById(com.hxqc.mall.core.R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRequestFailView = (RequestFailView) view.findViewById(R.id.comment_fail);
        mRequestFailView.setEmptyDescription(getEmptyDescription());
        mRequestFailView.setFailButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData(false);
            }
        });

        mAdapter = new MyCommentListAdapter(getContext());

        isViewLoaded = true;
        lazyLoad();
    }

    @Override
    protected void lazyLoad() {
        if (!isViewLoaded || !isVisible || isDataLoaded) return;
        loadData();
    }

    private void loadData() {
        isDataLoaded = true;

        refreshData(true);
    }

    protected LoadingAnimResponseHandler getLoadingAnimResponseHandler(boolean isShowAnim) {
        return new LoadingAnimResponseHandler(getContext(), isShowAnim) {
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
                if (mPage == DEFAULT_PATE) {
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
            }
        };
    }

    private void onSuccessResponse(String response){
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
            mAdapter.initDate(showMyCommentLists);

            if(isLoadMore){
                mAdapter.notifyDataSetChanged();
            }else {
                mRecyclerView.setAdapter(mAdapter);

            }
        }
    }

    private void refreshData(boolean hasLoadingAnim){
        commentApi.MyCommentList(PER_PAGE, mPage, getLoadingAnimResponseHandler(hasLoadingAnim));
    }

    @Override
    public boolean hasMore() {
        return mPtrHelper.isHasMore();
    }

    @Override
    public void onRefresh() {
        mPage = DEFAULT_PATE;
        refreshData(false);
    }

    @Override
    public void onLoadMore() {
        mPage++;
        refreshData(true);
        isLoadMore = true;
    }

    protected void showNoData() {
        if (mPage == DEFAULT_PATE) {
            mPtrFrameLayoutView.setVisibility(View.GONE);
            mRequestFailView.setVisibility(View.VISIBLE);
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        }
    }

    private String getEmptyDescription(){
        mRequestFailView.setEmptyButtonClick("随便逛逛", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitchBase.toMain(getActivity(), 0);
            }
        });
        return "您还没有发布任何评价";
    }
}
