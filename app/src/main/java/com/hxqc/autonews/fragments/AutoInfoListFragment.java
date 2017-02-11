package com.hxqc.autonews.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.autonews.adapter.AutoInfoListAdapter_2;
import com.hxqc.autonews.model.AutoInformationModel;
import com.hxqc.autonews.model.pojos.AutoInformation;
import com.hxqc.autonews.presenter.Presenter;
import com.hxqc.autonews.view.IMoreDataWithCacheHandler;
import com.hxqc.autonews.view.RequestDataWithCacheHandler;
import com.hxqc.autonews.widget.IScrollSateHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.mall.core.views.pullrefreshhandler.UpLoadListener;
import com.hxqc.util.DebugLog;
import com.hxqc.widget.RecyclerViewItemDecoration;

import java.util.ArrayList;

import hxqc.mall.R;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Author:李烽
 * Date:2016-09-30
 * FIXME
 * Todo 资讯列表 (不包含 “最新”)
 */

public class AutoInfoListFragment extends FunctionFragment implements RequestDataWithCacheHandler<ArrayList<AutoInformation>>,
        IMoreDataWithCacheHandler<ArrayList<AutoInformation>>, OnRefreshHandler, IScrollSateHandler {
    private static final String CODE = "code";
    private RecyclerView recyclerView;
    private PtrFrameLayout mPtrFrameLayout;
    private UltraPullRefreshHeaderHelper helper;
    private String mGuideCode = "";

    private Presenter mPresenter;
    private AutoInformationModel model;
    private int page = 1;
    private ArrayList<AutoInformation> autoInformations = new ArrayList<>();
    private AutoInfoListAdapter_2 adapter;
    private RequestFailView requestFailView;


    public static AutoInfoListFragment newInstance(String guideCode) {
        Bundle args = new Bundle();
        args.putString(CODE, guideCode);
        AutoInfoListFragment fragment = new AutoInfoListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mGuideCode = bundle.getString(CODE);
        }
        mPresenter = new Presenter();
        model = new AutoInformationModel(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auto_info_list, null);
        initPtrFrameLayout(view);
        requestFailView = (RequestFailView) view.findViewById(R.id.request_fail_view);
        requestFailView.setEmptyDescription("暂无相关资讯", R.drawable.ic_auto_info_empty);
        requestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
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
        adapter = new AutoInfoListAdapter_2(autoInformations, mContext);
        recyclerView.setAdapter(adapter);
//        container.addView(recyclerView);
        return view;
    }

    private void initPtrFrameLayout(View view) {
        mPtrFrameLayout = (PtrFrameLayout) view.findViewById(R.id.ptr_frame_layout);
        helper = new UltraPullRefreshHeaderHelper(mContext, mPtrFrameLayout, this);
        helper.setOnRefreshHandler(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        loadData();
    }

//
//    public void onStart() {
//    @Override
//        super.onStart();
//        loadData();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        loadData();
//    }

    private void loadData() {
        if (!TextUtils.isEmpty(mGuideCode))
            mPresenter.getAutoInfoByType(mGuideCode, this, model);
    }


    public void loadMore() {
        if (!TextUtils.isEmpty(mGuideCode))
            mPresenter.loadMoreAutoInfoListDataByType(mGuideCode, this, model);
    }

    @Override
    public String fragmentDescription() {
        return "";
    }

    @Override
    public boolean hasMore() {
        return false;
    }

    @Override
    public void onRefresh() {
        page = 1;
        loadData();
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onNestedPreScroll() {
        if (mPtrFrameLayout != null)
            mPtrFrameLayout.requestDisallowInterceptTouchEvent(false);
    }

    @Override
    public void onNestedScroll() {
        if (mPtrFrameLayout != null)
            mPtrFrameLayout.requestDisallowInterceptTouchEvent(true);
    }


    @Override
    public void onDataNull(String message) {
        //刷新
        refreshComplete();
        if (message != null && message.equals(""))
            return;
//        mPtrFrameLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        requestFailView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDataResponse(ArrayList<AutoInformation> informations) {
        //刷新
        refreshComplete();
        if (page == 1)
            autoInformations.clear();
        autoInformations.addAll(informations);
        adapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
        requestFailView.setVisibility(View.GONE);
    }

    @Override
    public void onCacheDataBack(ArrayList<AutoInformation> informations) {
        //刷新
        refreshComplete();
        if (page == 1)
            autoInformations.clear();
        autoInformations.addAll(informations);
        adapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
        requestFailView.setVisibility(View.GONE);
    }

    @Override
    public void onCacheDataNull() {
        //刷新
        recyclerView.setVisibility(View.GONE);
        requestFailView.setVisibility(View.VISIBLE);
    }

    private ArrayList<AutoInformation> listTempData = new ArrayList<>();

    @Override
    public void onMoreCacheDataBack(ArrayList<AutoInformation> informations) {
        //加载更多
        listTempData.clear();
        listTempData.addAll(informations);
        autoInformations.addAll(informations);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMoreCacheDataNull(String msg) {
        //加载更多
    }

    @Override
    public void onMoreDataBack(ArrayList<AutoInformation> informations) {
        //加载更多
        autoInformations.removeAll(listTempData);
        DebugLog.d(getClass().getSimpleName(), "autoInformations.size() = " + autoInformations.size());
        autoInformations.addAll(informations);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMoreDataNull(String msg) {

    }


    private void refreshComplete() {
        if (mPtrFrameLayout.isRefreshing())
            helper.refreshComplete(mPtrFrameLayout);
    }

}
