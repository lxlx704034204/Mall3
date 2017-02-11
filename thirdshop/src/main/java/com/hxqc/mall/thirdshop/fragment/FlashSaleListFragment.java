package com.hxqc.mall.thirdshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.SwipeRefreshForRecyclerFragment;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.SingleSeckillItem;
import com.hxqc.mall.thirdshop.views.adpter.FlashSaleAdapter;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author:李烽
 * Date:2016-05-17
 * FIXME
 * Todo 限时特价车列表
 */
public class FlashSaleListFragment extends SwipeRefreshForRecyclerFragment {

    final ThirdPartShopClient client = new ThirdPartShopClient();
    private String areaID;
    private boolean isIndexApi;
    private ArrayList<SingleSeckillItem> singleSeckillItems;

    //    private BaseRecyclerAdapter<SingleSeckillItem> adapter;
    private FlashSaleAdapter adapter;

    //    private String model;
//    private String serie;
//    private String brand;
    private static final String API = "api";
    public static final String AREAID = "siteID";
    private HashMap<String, String> params = new HashMap<>();
    private int count = 15;

    public static FlashSaleListFragment newInstance(String areaID, boolean isIndexAPI) {
        Bundle args = new Bundle();
        args.putString(AREAID, areaID);
        args.putBoolean(API, isIndexAPI);
        FlashSaleListFragment fragment = new FlashSaleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    public void upDate(String brand, String serie, String model) {
//        this.brand = brand;
//        this.serie = serie;
//        this.model = model;
//        mPage = DEFAULT_PATE;
//        refreshData(true);
//    }

    public void upDate(HashMap<String, String> params) {
        this.params = params;
        mPage = DEFAULT_PATE;
        refreshData(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        areaID = getArguments().getString(AREAID);
        isIndexApi = getArguments().getBoolean(API);
        singleSeckillItems = new ArrayList<>();
        adapter = new FlashSaleAdapter(getActivity(), singleSeckillItems);

//                = new BaseRecyclerAdapter<SingleSeckillItem>(getActivity(), singleSeckillItems) {
//            @Override
//            protected void bindData(RecyclerViewHolder holder, int position, SingleSeckillItem item) {
//                FlashSaleItem flashSaleItem = (FlashSaleItem) holder.getView(R.id.flash_item);
//                flashSaleItem.addData(item);
//            }
//
//            @Override
//            protected int getItemLayout(int viewType) {
//                return R.layout.item_flash_sale_auto;
//            }
//        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setAdapter(adapter);
    }

//    public void refreshDataWhitFilter(boolean hasLoadingAnim) {
//        client.getSeckill(siteID, count, mPage, params, getLoadingAnimResponseHandler(hasLoadingAnim));
//    }

    @Override
    public void refreshData(boolean hasLoadingAnim) {
        if (params.isEmpty() && isIndexApi)
            client.getIndexSeckillList(areaID, mPage, count, getLoadingAnimResponseHandler(hasLoadingAnim));
        else
            client.getSeckill(areaID, count, mPage, params, getLoadingAnimResponseHandler(hasLoadingAnim));
    }

    @Override
    protected void onSuccessResponse(String response) {
        ArrayList<SingleSeckillItem> items = JSONUtils.fromJson(response, new TypeToken<ArrayList<SingleSeckillItem>>() {
        });
        if (items != null) {
            if (items.size() < count)
                mPtrHelper.setHasMore(false);
            else mPtrHelper.setHasMore(true);

            if (mPage == DEFAULT_PATE)
                singleSeckillItems.clear();
            singleSeckillItems.addAll(items);
            adapter.notifyDataSetChanged();
            mPtrFrameLayoutView.setVisibility(View.VISIBLE);
            mRequestFailView.setVisibility(View.GONE);
        } else {
            mPtrHelper.setHasMore(false);
        }
        if (singleSeckillItems.size() == 0) {
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
        return "限时促销车辆列表";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.onDestroy();
    }

}
