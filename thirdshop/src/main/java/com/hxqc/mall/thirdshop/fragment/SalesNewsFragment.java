package com.hxqc.mall.thirdshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.views.DividerItemDecoration;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.RecyclerViewOnScrollListener;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.promotion.SalesNewsModel;
import com.hxqc.mall.thirdshop.views.adpter.SalesNewsAdapterHolder;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;


/**
 * Author: wanghao
 * Date: 2015-11-30
 * FIXME
 * 新闻资讯
 */
public class SalesNewsFragment extends ShopDetailBaseFragment implements OnRefreshHandler {
    boolean hasMore = false;

    private ArrayList< SalesNewsModel > mData;
    RecyclerView mSalesNewsRecycleView;
    SalesNewsAdapterHolder adapterHolder;

    RequestFailView mRequestFailView;

    public SalesNewsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.t_fragment_news_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRequestFailView = (RequestFailView) view.findViewById(R.id.refresh_fail_view);

        mSalesNewsRecycleView = (RecyclerView) view.findViewById(R.id.rlv_news_list);
        mSalesNewsRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSalesNewsRecycleView.setHasFixedSize(true);
        mSalesNewsRecycleView.setItemAnimator(new DefaultItemAnimator());
        mSalesNewsRecycleView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mSalesNewsRecycleView.setOnScrollListener(new RecyclerViewOnScrollListener(this));
        //初始化 列表信息
        getApiData(true);


    }

    private void getApiData(boolean showAnim) {

        ThirdPartShopClient.newsInfoList(mShopDetailListener.getShopID(), new LoadingAnimResponseHandler(getActivity(), showAnim) {
            @Override
            public void onSuccess(String response) {

                mData = JSONUtils.fromJson(response,new TypeToken< ArrayList< SalesNewsModel > >(){});
                adapterHolder = new SalesNewsAdapterHolder(mData);
                mSalesNewsRecycleView.setAdapter(adapterHolder);

                if (mData != null) {
                    if(mData.size() ==0){
                        requestFailView(1);
                    }else {
                        mRequestFailView.setVisibility(View.GONE);
                    }
                } else {
                    requestFailView(0);
                }

                DebugLog.i("test_s_p_list",response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                requestFailView(0);
            }
        });



    }

    private void requestFailView(int showType) {

        if (showType == 0){

            mRequestFailView.setEmptyDescription("获取数据失败");
            mRequestFailView.setEmptyButtonClick("刷新", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getApiData(true);
                }
            });
            mRequestFailView.setFailButtonClick("刷新", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getApiData(true);
                }
            });
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);

        }else if (showType == 1){

            mRequestFailView.setEmptyDescription("敬请期待");
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);

        }

        mRequestFailView.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean hasMore() {
        return hasMore;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public String fragmentDescription() {
        return "促销列表";
    }
}
