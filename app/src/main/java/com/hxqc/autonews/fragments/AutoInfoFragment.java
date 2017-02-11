package com.hxqc.autonews.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.autonews.adapter.AutoInfoListAdapter;
import com.hxqc.autonews.model.AutoInformationModel;
import com.hxqc.autonews.model.pojos.AutoInfoHomeData;
import com.hxqc.autonews.model.pojos.AutoInformation;
import com.hxqc.autonews.presenter.Presenter;
import com.hxqc.autonews.view.RefrashableView;
import com.hxqc.mall.core.views.pullrefreshhandler.FixRequestDisallowTouchEventPtrFrameLayout;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.mall.core.views.pullrefreshhandler.UpLoadListener;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

import hxqc.mall.R;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Author:李烽
 * Date:2016-09-01
 * FIXME
 * Todo 汽车资讯（第一版，已过期，可删除）
 */
@Deprecated
public class AutoInfoFragment extends FunctionFragment implements RefrashableView<AutoInfoHomeData>, OnRefreshHandler {
    private Toolbar mToolbar;
    private PtrFrameLayout mPtrFrameLayout;
    private UltraPullRefreshHeaderHelper helper;
    private RecyclerView mRecyclerView;
    private RequestFailView mRequestFailView;
    private AutoInfoListAdapter adapter;
    //    private AutoInfoHomeData homeData;
    private Presenter mPresenter;
    private AutoInformationModel model;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auto_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRequestFailView = (RequestFailView) view.findViewById(R.id.request_fail_view);
        mRequestFailView.setEmptyButtonClick("暂无数据", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        mRequestFailView.setFailButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        initPtrFrameLayout(view);
        initRecyclerView(view);
        initToolBar(view);
        model = new AutoInformationModel(mContext);
        mPresenter = new Presenter();
        loadData();
    }

    private void initPtrFrameLayout(View view) {
        mPtrFrameLayout = (FixRequestDisallowTouchEventPtrFrameLayout) view.findViewById(R.id.event_active_refresh);
        helper = new UltraPullRefreshHeaderHelper(mContext, mPtrFrameLayout, this);
        helper.setOnRefreshHandler(this);
    }

    @Deprecated
    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.auto_info_list);
//        homeData = new AutoInfoHomeData();
        adapter = new AutoInfoListAdapter(null, mContext);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mRecyclerView.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
        mRecyclerView.addOnScrollListener(new UpLoadListener(layoutManager) {
            @Override
            protected void onLoadMore() {
                mPresenter.loadMoreRecommentAutoInfoListData(AutoInfoFragment.this, model);
            }
        });
    }

    private void initToolBar(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("汽车资讯");
        mToolbar.setTitleTextColor(Color.WHITE);
    }

    private void loadData() {
        DebugLog.d(getClass().getSimpleName(), "loadData");
        mPresenter.getAutoInfoListData(this, model);
    }


    public void onDataBack(AutoInfoHomeData data) {
        mRequestFailView.setVisibility(View.GONE);
        mPtrFrameLayout.setVisibility(View.VISIBLE);
        adapter.notifyData(data);
        if (mPtrFrameLayout.isRefreshing())
            helper.refreshComplete(mPtrFrameLayout);
    }


    public void onDataNull(String message) {
        if (mPtrFrameLayout.isRefreshing())
            helper.refreshComplete(mPtrFrameLayout);
        mRequestFailView.setVisibility(View.VISIBLE);
        mPtrFrameLayout.setVisibility(View.GONE);
        if (TextUtils.isEmpty(message)) {
            DebugLog.i("", "暂无数据");
            mRequestFailView.setRequestType(RequestFailView.RequestViewType.empty);

        } else {
            DebugLog.i("", "重新试试");
            mRequestFailView.setRequestType(RequestFailView.RequestViewType.fail);
        }
    }

    public void onMoreDataResponse(ArrayList<AutoInformation> list) {
        if (list != null && list.size() > 0) {
            adapter.notifyListData(list);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.onDestroy();
        }
    }

    @Override
    public String fragmentDescription() {
        return "汽车资讯";
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onMoreDataResponse(AutoInfoHomeData data) {
        onMoreDataResponse(data.infoList);
    }
}
