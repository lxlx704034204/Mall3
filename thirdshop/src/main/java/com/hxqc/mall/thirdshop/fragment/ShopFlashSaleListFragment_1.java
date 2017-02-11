package com.hxqc.mall.thirdshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.views.pullrefreshhandler.OnRefreshHandler;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.control.ShopDetailsController;
import com.hxqc.mall.thirdshop.model.SingleSeckillItem;
import com.hxqc.mall.thirdshop.views.adpter.FlashSaleListViewAdapter;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.ListViewNoSlide;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-05-09
 * FIXME
 * Todo 店铺限时特卖车辆列表
 */
public class ShopFlashSaleListFragment_1 extends FunctionFragment implements OnRefreshHandler {

    boolean hasMore = false;

    private ArrayList<SingleSeckillItem> mData;

    ListViewNoSlide mSalesPRecycleView;

    FlashSaleListViewAdapter adapterHolder;

    RequestFailView mRequestFailView;

    ShopDetailsController shopDetailsController;
    private String shopID = "";

    public ShopFlashSaleListFragment_1() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_flash_sale, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shopDetailsController = ShopDetailsController.getInstance();


        mRequestFailView = (RequestFailView) view.findViewById(R.id.refresh_fail_view);

        mSalesPRecycleView = (ListViewNoSlide) view.findViewById(R.id.list_view);
        //初始化 列表信息
//        getApiData(true);
    }

    private void getApiData(boolean showAnim) {

        shopDetailsController.ThirdPartShopClient.getShopSeckill(shopID, new LoadingAnimResponseHandler(getActivity(), showAnim) {
            @Override
            public void onSuccess(String response) {

                mData = JSONUtils.fromJson(response, new TypeToken<ArrayList<SingleSeckillItem>>() {
                });
                adapterHolder = new FlashSaleListViewAdapter(getActivity(), mData);
                mSalesPRecycleView.setAdapter(adapterHolder);

                if (mData != null) {
                    if (mData.size() == 0) {
                        requestFailView(1);
                    } else {
                        mRequestFailView.setVisibility(View.GONE);
                    }
                } else {
                    requestFailView(1);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (statusCode == 400)
                    requestFailView(1);
                requestFailView(0);
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
        return "限时特价车";
    }

    public void upDate(String shopID) {
        this.shopID = shopID;
        getApiData(true);
    }


    public void destroy() {
        if (adapterHolder != null)
            adapterHolder.onDestroy();
    }
}
