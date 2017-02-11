package com.hxqc.mall.core.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;

import cz.msebera.android.httpclient.Header;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 说明:列表Fragment基类
 * <p>
 * author: 吕飞
 * since: 2015-04-09
 * Copyright:恒信汽车电子商务有限公司
 */
public abstract class SwipeRefreshFragment extends FunctionFragment implements OnRefreshHandler {

    protected static final int DEFAULT_PATE = 1;
    protected final int PER_PAGE = 15;
    protected int mPage = DEFAULT_PATE;//当前页

    protected PtrFrameLayout mPtrFrameLayoutView;
    protected UltraPullRefreshHeaderHelper mPtrHelper;
    protected RequestFailView mRequestFailView;
    private onFailureResponse mOnFailureListener;

    /**
     * 获取数据
     */
    public abstract void refreshData(boolean hasLoadingAnim);

    /**
     * 数据成功
     */
    protected abstract void onSuccessResponse(String response);

    /**
     * 数据为空描述
     */
    protected abstract String getEmptyDescription();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPtrFrameLayoutView = (PtrFrameLayout) view.findViewById(R.id.refresh_frame);
        mPtrHelper = new UltraPullRefreshHeaderHelper(getActivity(), mPtrFrameLayoutView, this);
        mPtrHelper.setOnRefreshHandler(this);

        mRequestFailView = (RequestFailView) view.findViewById(R.id.fail_view);
        mRequestFailView.setEmptyDescription(getEmptyDescription());
        mRequestFailView.setFailButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData(false);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshData(true);
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
    }

    @Override
    public boolean hasMore() {
        return mPtrHelper.isHasMore();
    }

    protected LoadingAnimResponseHandler getLoadingAnimResponseHandler(boolean isShowAnim) {
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
                if (mPage == DEFAULT_PATE) {
                    mPtrFrameLayoutView.setVisibility(View.GONE);
                    mRequestFailView.setVisibility(View.VISIBLE);
                    mRequestFailView.setFailButtonClick("重新加载", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPtrFrameLayoutView.setVisibility(View.VISIBLE);
                            mRequestFailView.setVisibility(View.GONE);
                            refreshData(true);
                        }
                    });
                    mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
                } else {
                    mPage--;
                }
                //请求失败监听
                if (mOnFailureListener != null)
                    mOnFailureListener.onFailureResponse();
            }
        };
    }

    protected void showNoData() {
        if (mPage == DEFAULT_PATE) {
            mPtrFrameLayoutView.setVisibility(View.GONE);
            mRequestFailView.setVisibility(View.VISIBLE);
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        }
    }

    public void setOnFailureListener(onFailureResponse mOnFailureListener) {
        this.mOnFailureListener = mOnFailureListener;

    }

    public interface onFailureResponse {
        void onFailureResponse();
    }

}
