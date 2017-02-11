package com.hxqc.mall.thirdshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.views.DividerItemDecoration;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.core.views.pullrefreshhandler.RecyclerViewOnScrollListener;
import com.hxqc.mall.core.views.pullrefreshhandler.UltraPullRefreshHeaderHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.control.ShopDetailsController;
import com.hxqc.mall.thirdshop.model.promotion.SalesPModel;
import com.hxqc.mall.thirdshop.views.FullyLinearLayoutManager;
import com.hxqc.mall.thirdshop.views.adpter.SalesPAdapterHolder;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;
import in.srain.cube.views.ptr.PtrFrameLayout;

import java.util.ArrayList;


/**
 * Author: wanghao
 * Date: 2015-11-30
 * FIXME
 * 促销信息列表
 */
public class SalesPFragment extends FunctionFragment implements OnRefreshHandler {

    protected static final int DEFAULT_PAGE = 1;
    protected final int PER_PAGE = 15;
    protected int mPage = DEFAULT_PAGE;//当前页

    private ArrayList<SalesPModel> mData = new ArrayList<>();
    RecyclerView mSalesPRecycleView;
    SalesPAdapterHolder adapterHolder;
    protected PtrFrameLayout mPtrFrameLayoutView;
    protected UltraPullRefreshHeaderHelper mPtrHelper;
    RequestFailView mRequestFailView;

    ShopDetailsController shopDetailsController;

    public SalesPFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.t_fragment_sales_p, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shopDetailsController = ShopDetailsController.getInstance();

        mRequestFailView = (RequestFailView) view.findViewById(R.id.refresh_fail_view);
        mPtrFrameLayoutView = (PtrFrameLayout) view.findViewById(com.hxqc.mall.core.R.id.refresh_frame);
        mPtrHelper = new UltraPullRefreshHeaderHelper(getActivity(), mPtrFrameLayoutView, this);
        mPtrHelper.setOnRefreshHandler(this);

        mSalesPRecycleView = (RecyclerView) view.findViewById(R.id.rlv_sales_p_list);
        mSalesPRecycleView.setLayoutManager(new FullyLinearLayoutManager(getActivity()));
        mSalesPRecycleView.setHasFixedSize(true);
        mSalesPRecycleView.setItemAnimator(new DefaultItemAnimator());
        mSalesPRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mSalesPRecycleView.setOnScrollListener(new RecyclerViewOnScrollListener(this));

        adapterHolder = new SalesPAdapterHolder(mData);
        mSalesPRecycleView.setAdapter(adapterHolder);
        //初始化 列表信息
        getApiData(true);
    }

    private void getApiData(boolean showAnim) {

        shopDetailsController.ThirdPartShopClient.salesPListDatas("10", mPage + "", shopDetailsController.getShopID(), new LoadingAnimResponseHandler(getActivity(), showAnim) {
            @Override
            public void onSuccess(String response) {
                DebugLog.i("test_s_p_list", response);
                ArrayList<SalesPModel> mTempData = JSONUtils.fromJson(response, new TypeToken<ArrayList<SalesPModel>>() {
                });

                if (mPage == DEFAULT_PAGE) {
                    mData.clear();
                    if (mTempData != null) {
                        if (mTempData.size() == 0) {
                            requestFailView(1);
                        } else {
                            mRequestFailView.setVisibility(View.GONE);
                            mData.addAll(mTempData);
                            adapterHolder.notifyDataSetChanged();
                            mPtrHelper.setHasMore(true);
                        }
                    } else {
                        requestFailView(0);
                    }
                } else {
                    if (mTempData != null && mTempData.size() > 0) {

                        if (mTempData.size()<10){
                            mPtrHelper.setHasMore(false);
                        }

                        mData.addAll(mTempData);
                        adapterHolder.notifyDataSetChanged();
                    }
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                requestFailView(0);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mPtrHelper.refreshComplete(mPtrFrameLayoutView);
            }
        });
    }

    private void requestFailView(int showType) {
        if (showType == 0) {
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
        } else if (showType == 1) {
            mRequestFailView.setEmptyDescription("敬请期待");
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        }
        mRequestFailView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean hasMore() {
        return mPtrHelper.isHasMore();
    }

    @Override
    public void onRefresh() {
        mPage = DEFAULT_PAGE;
        getApiData(true);
    }

    @Override
    public void onLoadMore() {
        mPage++;
        getApiData(true);
    }

    @Override
    public String fragmentDescription() {
        return "促销列表";
    }
}
