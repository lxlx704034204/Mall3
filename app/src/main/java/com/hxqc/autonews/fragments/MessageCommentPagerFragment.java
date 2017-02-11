package com.hxqc.autonews.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.autonews.adapter.MessageCommentPagerAdapter;
import com.hxqc.autonews.api.AutoInformationApiClient;
import com.hxqc.autonews.model.MessageComment;
import com.hxqc.autonews.widget.LazyFragment;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

import hxqc.mall.R;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * 我的评价 资讯评价
 * Created by huangyi on 15/12/25.
 */
public class MessageCommentPagerFragment extends LazyFragment implements OnRefreshHandler, MessageCommentPagerAdapter.OnClickListener {

    protected static final int DEFAULT_PATE = 1;
    protected int mPage = DEFAULT_PATE; //当前页
    AutoInformationApiClient client;
    ListView mListView;
    PtrFrameLayout mPtrView;
    UltraPullRefreshHeaderHelper mPtrHelper;
    RequestFailView mFailView;
    ArrayList<MessageComment> mComment;
    MessageCommentPagerAdapter mAdapter;
    /**
     * 视图是否加载过
     **/
    private boolean isViewLoaded;
    /**
     * 数据是否加载过
     **/
    private boolean isDataLoaded;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_comment_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        client = new AutoInformationApiClient();
        mListView = (ListView) view.findViewById(R.id.comment_list);
        mPtrView = (PtrFrameLayout) view.findViewById(R.id.comment_ptr);
        mPtrHelper = new UltraPullRefreshHeaderHelper(getActivity(), mPtrView, this);
        mFailView = (RequestFailView) view.findViewById(R.id.comment_fail);
        mFailView.setEmptyDescription("您还没有发布任何评价");
        mFailView.setEmptyButtonClick("随便逛逛", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivitySwitchBase.toMain(getActivity(), 0);
            }
        });
        mFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFailView.setVisibility(View.GONE);
                loadData(true);
            }
        });

        mComment = new ArrayList<>();
        mAdapter = new MessageCommentPagerAdapter(getContext(), mComment, this);
        mListView.setAdapter(mAdapter);

        isViewLoaded = true;
        lazyLoad();
    }

    @Override
    protected void lazyLoad() {
        if (!isViewLoaded || !isVisible || isDataLoaded) return;
        loadData(true);
    }

    private void loadData(boolean hasLoadingAnim) {
        isDataLoaded = true;
        client.requestMyComments(mPage, new LoadingAnimResponseHandler(getContext(), hasLoadingAnim) {
            @Override
            public void onSuccess(String response) {
                ArrayList<MessageComment> temp = JSONUtils.fromJson(response, new TypeToken<ArrayList<MessageComment>>() {
                });

                //初次加载
                if (mPage == DEFAULT_PATE) {
                    mComment.clear();
                    if (null == temp || temp.size() == 0) {
                        mPtrView.setVisibility(View.GONE);
                        mFailView.setVisibility(View.VISIBLE);
                        mFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
                    } else {
                        mComment.addAll(temp);
                        mPtrView.setVisibility(View.VISIBLE);
                        mFailView.setVisibility(View.GONE);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    if (null != temp && temp.size() != 0) {
                        mComment.addAll(temp);
                        mAdapter.notifyDataSetChanged();
                    }
                }

                if (null != temp && temp.size() != 0) {
                    mPtrHelper.setHasMore(true);
                } else {
                    mPtrHelper.setHasMore(false);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (mPage == DEFAULT_PATE) {
                    mPtrView.setVisibility(View.GONE);
                    mFailView.setVisibility(View.VISIBLE);
                    mFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mPtrHelper.refreshComplete(mPtrView);
            }
        });
    }

    @Override
    public boolean hasMore() {
        return mPtrHelper.isHasMore();
    }

    @Override
    public void onRefresh() {
        mPage = DEFAULT_PATE;
        loadData(false);
    }

    @Override
    public void onLoadMore() {
        mPage++;
        loadData(true);
    }

    @Override
    public void onDelete(String commentID) {
        client.deleteAutoInfoComment(commentID, new LoadingAnimResponseHandler(getContext(), true) {
            @Override
            public void onSuccess(String response) {
                ToastHelper.showGreenToast(getContext(), "删除成功");
                onRefresh();
            }
        });
    }

}
