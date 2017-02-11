package com.hxqc.mall.thirdshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.adapter.BaseRecyclerAdapter;
import com.hxqc.mall.core.adapter.RecyclerViewHolder;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.SwipeRefreshForRecyclerFragment;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.FourSSeries;
import com.hxqc.mall.thirdshop.views.NewCarSeriesItem;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-05-17
 * FIXME
 * Todo 新车车系列表
 */
public class NewCarSeriesListFragment extends SwipeRefreshForRecyclerFragment {
    final ThirdPartShopClient client = new ThirdPartShopClient();
    private ArrayList<FourSSeries> series = new ArrayList<>();
    private BaseRecyclerAdapter<FourSSeries> baseRecyclerAdapter;
    public static final String AREAID = "siteID";
    private String areaID;
    private int count = 15;

    public static NewCarSeriesListFragment newInstance(String areaID) {
        Bundle args = new Bundle();
        args.putString(AREAID, areaID);
        NewCarSeriesListFragment fragment = new NewCarSeriesListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        areaID = getArguments().getString(AREAID);
        baseRecyclerAdapter = new BaseRecyclerAdapter<FourSSeries>(getActivity(), series) {
            @Override
            protected void bindData(RecyclerViewHolder holder, int position, final FourSSeries fourSSeries) {
                NewCarSeriesItem item = (NewCarSeriesItem) holder.getView(R.id.new_car_item);
                item.addData(fourSSeries, areaID);
            }

            @Override
            protected int getItemLayout(int viewType) {
                return R.layout.item_new_car;
            }
        };

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setAdapter(baseRecyclerAdapter);
    }

    @Override
    public void refreshData(boolean hasLoadingAnim) {
        client.getIndexSeriesList(areaID, mPage, count, getLoadingAnimResponseHandler(true));
    }

    @Override
    protected void onSuccessResponse(String response) {
        ArrayList<FourSSeries> series1 = JSONUtils.fromJson(response, new TypeToken<ArrayList<FourSSeries>>() {
        });
        if (series1 != null) {
            if (series1.size() < count)
                mPtrHelper.setHasMore(false);
            else mPtrHelper.setHasMore(true);

            if (mPage == DEFAULT_PATE)
                series.clear();
            series.addAll(series1);
            baseRecyclerAdapter.notifyDataSetChanged();
            mPtrFrameLayoutView.setVisibility(View.VISIBLE);
            mRequestFailView.setVisibility(View.GONE);
        } else {
            mPtrHelper.setHasMore(false);
        }
        if (series.size() == 0) {
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
