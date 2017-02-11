package com.hxqc.autonews.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.autonews.adapter.AutoInfoListAdapter_2;
import com.hxqc.autonews.model.AutoEvent;
import com.hxqc.autonews.model.AutoInformationModel;
import com.hxqc.autonews.model.pojos.AutoInformation;
import com.hxqc.autonews.presenter.Presenter;
import com.hxqc.autonews.view.IMoreDataWithCacheHandler;
import com.hxqc.autonews.view.RequestDataWithCacheHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.FixRequestDisallowTouchEventPtrFrameLayout;
import com.hxqc.autonews.widget.IScrollSateHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.mall.core.views.pullrefreshhandler.UpLoadListener;
import com.hxqc.util.DebugLog;
import com.hxqc.widget.RecyclerViewItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-09-30
 * FIXME
 * Todo 资讯列表 (“最新”)
 */

public class RecommentAutoInfoListFragment extends FunctionFragment implements
        RequestDataWithCacheHandler<ArrayList<AutoInformation>>, IMoreDataWithCacheHandler<ArrayList<AutoInformation>>,
        OnRefreshHandler, IScrollSateHandler {
    private RecyclerView recyclerView;
    private FixRequestDisallowTouchEventPtrFrameLayout mPtrFrameLayout;
    private UltraPullRefreshHeaderHelper helper;
    private Presenter mPresenter;
    private AutoInformationModel model;
    private int page = 1;
    private ArrayList<AutoInformation> autoInformations = new ArrayList<>();
    private AutoInfoListAdapter_2 adapter;
    private RequestFailView requestFailView;


    public static RecommentAutoInfoListFragment newInstance() {
        Bundle args = new Bundle();
        RecommentAutoInfoListFragment fragment = new RecommentAutoInfoListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugLog.i(getClass().getSimpleName(), "onCreate");
        mPresenter = new Presenter();
        model = new AutoInformationModel(mContext);
        adapter = new AutoInfoListAdapter_2(autoInformations, mContext);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DebugLog.i(getClass().getSimpleName(), "onCreateView");
        View view = inflater.inflate(R.layout.fragment_auto_info_list, null);
        requestFailView = (RequestFailView) view.findViewById(R.id.request_fail_view);
        requestFailView.setEmptyDescription("暂无相关资讯", R.drawable.ic_auto_info_empty);
        requestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        initPtrFrameLayout(view);
        initRecyclerView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        DebugLog.i(getClass().getSimpleName(), "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        DebugLog.i(getClass().getSimpleName(), "onResume");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DebugLog.i(getClass().getSimpleName(), "onActivityCreated");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        DebugLog.i(getClass().getSimpleName(), "onAttach");
    }

    @Override
    public void onPause() {
        super.onPause();
        DebugLog.i(getClass().getSimpleName(), "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        DebugLog.i(getClass().getSimpleName(), "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DebugLog.i(getClass().getSimpleName(), "onDestroy");
    }

    private void initRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.auto_info_list);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new RecyclerViewItemDecoration(RecyclerViewItemDecoration.MODE_HORIZONTAL
                , Color.parseColor("#d5d5d5"), 2, 0, 0));
        recyclerView.addOnScrollListener(new UpLoadListener(manager) {

            @Override
            protected void onLoadMore() {
                loadMore();
            }
        });
        if (!autoInformations.isEmpty()) {
            requestFailView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private void initPtrFrameLayout(View view) {
        mPtrFrameLayout = (FixRequestDisallowTouchEventPtrFrameLayout) view.findViewById(R.id.ptr_frame_layout);
        helper = new UltraPullRefreshHeaderHelper(mContext, mPtrFrameLayout, this);
        helper.setOnRefreshHandler(this);
    }


    private void loadData() {
        if (mPresenter == null)
            mPresenter = new Presenter();
        if (model == null)
            model = new AutoInformationModel(mContext);
        mPresenter.getRecommentAutoInfo(this, model);
    }

    /**
     * 手动添加数据
     *
     * @param informations
     */
    public void addData(ArrayList<AutoInformation> informations) {
        if (informations == null || informations.isEmpty())
            loadData();
        else {
            autoInformations.clear();
            autoInformations.addAll(informations);
            if (adapter == null)
                adapter = new AutoInfoListAdapter_2(autoInformations, mContext);
            adapter.notifyDataSetChanged();
            if (recyclerView != null && requestFailView != null) {
                requestFailView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
            refreshComplete();
        }
    }

    public void loadMore() {
        DebugLog.d(getClass().getSimpleName(), "loadMore:" + page);
//        page++;
//        loadData();
        mPresenter.loadMoreRecommentAutoInfoListData(this, model);
    }


    @Override
    public boolean hasMore() {
        return false;
    }


    @Override
    public void onRefresh() {
        page = 1;
//        loadData();
        EventBus.getDefault().post(new AutoEvent(AutoInfoFragment_3.REFRESH_CODE));
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public String fragmentDescription() {
        return "";
    }

    @Override
    public void onNestedPreScroll() {
        DebugLog.d(getClass().getSimpleName(), "onNestedPreScroll");
        if (mPtrFrameLayout != null)
            mPtrFrameLayout.requestDisallowInterceptTouchEvent(false);
    }


    @Override
    public void onNestedScroll() {
        DebugLog.d(getClass().getSimpleName(), "onNestedScroll");
        if (mPtrFrameLayout != null)
            mPtrFrameLayout.requestDisallowInterceptTouchEvent(true);
    }

    private ArrayList<AutoInformation> listTempData = new ArrayList<>();

    @Override
    public void onMoreCacheDataBack(ArrayList<AutoInformation> informations) {
        //加载更多缓存数据
        listTempData.clear();
        listTempData.addAll(informations);
        autoInformations.addAll(informations);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMoreCacheDataNull(String msg) {
        //加载更多缓存数据
    }

    @Override
    public void onMoreDataBack(ArrayList<AutoInformation> informations) {
        //加载更多网络数据
        autoInformations.removeAll(listTempData);
        DebugLog.d(getClass().getSimpleName(), "autoInformations.size() = " + autoInformations.size());
        autoInformations.addAll(informations);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMoreDataNull(String msg) {
        //加载更多网络数据
    }

    @Override
    public void onCacheDataBack(ArrayList<AutoInformation> data) {
        //刷新获取网络数据
        refreshComplete();
        autoInformations.clear();
        autoInformations.addAll(data);
        adapter.notifyDataSetChanged();
        requestFailView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void refreshComplete() {
        if (mPtrFrameLayout != null && mPtrFrameLayout.isRefreshing())
            helper.refreshComplete(mPtrFrameLayout);
    }

    @Override
    public void onCacheDataNull() {
        //刷新获取缓存数据
        requestFailView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onDataNull(String message) {
        //刷新获取网络数据
        refreshComplete();
        mPtrFrameLayout.setVisibility(View.GONE);
        requestFailView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onDataResponse(ArrayList<AutoInformation> data) {
        //刷新获取网络数据
        refreshComplete();
        autoInformations.clear();
        autoInformations.addAll(data);
        adapter.notifyDataSetChanged();
        requestFailView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
