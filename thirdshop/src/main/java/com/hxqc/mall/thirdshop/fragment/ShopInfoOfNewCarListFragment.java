package com.hxqc.mall.thirdshop.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.SwipeRefreshForRecyclerFragment;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.ShopSearchAuto;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.views.adpter.SearchCarBrandsAdapter;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-05-17
 * FIXME
 * Todo 车辆对应店铺的列表
 */
public class ShopInfoOfNewCarListFragment extends SwipeRefreshForRecyclerFragment {
    public static final String SERIES = "series";
    public static final String MODEL = "model";
    final ThirdPartShopClient client = new ThirdPartShopClient();
    private ArrayList<ShopSearchAuto> shopSearchAutos = new ArrayList<>();
    private SearchCarBrandsAdapter adapter;
    public static final String AREAID = "siteID";
    private String areaID;
    private String series;
    private String model;
    private int count = 15;

    public static ShopInfoOfNewCarListFragment newInstance(String areaID, String series, String model) {
        Bundle args = new Bundle();
        args.putString(AREAID, areaID);
        args.putString(SERIES, series);
        args.putString(MODEL, model);
        ShopInfoOfNewCarListFragment fragment = new ShopInfoOfNewCarListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        areaID = getArguments().getString(AREAID);
        series = getArguments().getString(SERIES);
        model = getArguments().getString(MODEL);
        //初始车辆adapter
        adapter = new SearchCarBrandsAdapter() {
            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                final Holder holder = super.onCreateViewHolder(parent, viewType);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShopSearchAuto itemData = getItemData(holder.getAdapterPosition());
                        ActivitySwitcherThirdPartShop.toCarDetail(itemData.autoInfo.itemID,
                                itemData.shopInfo.shopID, itemData.shopInfo.shopTitle, getActivity());
                    }
                });
                holder.car_dial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        com.hxqc.mall.core.util.OtherUtil.callPhone(getActivity(),
                                getItemData(holder.getAdapterPosition()).shopInfo.shopTel);
                    }
                });
                holder.ask_price.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShopSearchAuto itemData = getItemData(holder.getAdapterPosition());
                        ActivitySwitcherThirdPartShop.toAskLeastPrice(getActivity(),
                                itemData.shopInfo.shopID, itemData.autoInfo.itemID,
                                itemData.autoInfo.itemName, itemData.shopInfo.shopTel, true, null);
                    }
                });
                return holder;

            }

            @Override
            public void onBindViewHolder(Holder holder, int position) {
                super.onBindViewHolder(holder, position);
                holder.car_content_4.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void refreshData(boolean hasLoadingAnim) {
        client.searchShopModelList(mPage, count, "", series, model, areaID, getLoadingAnimResponseHandler(true));
    }

    @Override
    protected void onSuccessResponse(String response) {
        ArrayList<ShopSearchAuto> searchAutos = JSONUtils.fromJson(response, new TypeToken<ArrayList<ShopSearchAuto>>() {
        });
        if (searchAutos != null) {
            if (searchAutos.size() < count)
                mPtrHelper.setHasMore(false);
            else mPtrHelper.setHasMore(true);

            if (mPage == DEFAULT_PATE)
                this.shopSearchAutos.clear();
            this.shopSearchAutos.addAll(searchAutos);
            adapter.addDate(shopSearchAutos);
            adapter.notifyDataSetChanged();
            mPtrFrameLayoutView.setVisibility(View.VISIBLE);
            mRequestFailView.setVisibility(View.GONE);
        } else {
            mPtrHelper.setHasMore(false);
        }
        if (this.shopSearchAutos.size() == 0) {
            mPtrFrameLayoutView.setVisibility(View.GONE);
            mRequestFailView.setVisibility(View.VISIBLE);
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        }
    }

    @Override
    protected String getEmptyDescription() {
        return "暂无数据";
    }

    @Override
    public String fragmentDescription() {
        return "新车车系列表";
    }
}
