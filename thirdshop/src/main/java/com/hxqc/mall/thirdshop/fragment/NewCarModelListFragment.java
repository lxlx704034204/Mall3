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
import com.hxqc.mall.thirdshop.model.FourSModel;
import com.hxqc.mall.thirdshop.model.Series;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.views.NewCarModelItem;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * 车型列表
 * Author:李烽
 * Date:2016-05-17
 * FIXME
 * Todo 新车车型列表
 */
public class NewCarModelListFragment extends SwipeRefreshForRecyclerFragment {
    public static final String SERIES = "series";
    public static final String BRAND = "brandName";
    final ThirdPartShopClient client = new ThirdPartShopClient();
    private ArrayList<FourSModel> fourSModels = new ArrayList<>();
    private BaseRecyclerAdapter<FourSModel> baseRecyclerAdapter;
    public static final String AREAID = "siteID";
    public static final String IS_FORM4S = "isForm4s";
    private String areaID;
    //    private String series;
    private boolean isFrom4S;
    private String brandName;
    private Series series;
    private int count = 15;

    public static NewCarModelListFragment newInstance(String areaID, String brandName, Series series, boolean isFrom4S) {
        Bundle args = new Bundle();
        args.putString(AREAID, areaID);
        args.putParcelable(SERIES, series);
        args.putBoolean(IS_FORM4S, isFrom4S);
        args.putString(BRAND, brandName);
        NewCarModelListFragment fragment = new NewCarModelListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        areaID = getArguments().getString(AREAID);
        series = getArguments().getParcelable(SERIES);
        brandName = getArguments().getString(BRAND);
        isFrom4S = getArguments().getBoolean(IS_FORM4S);
        baseRecyclerAdapter = new BaseRecyclerAdapter<FourSModel>(getActivity(), fourSModels) {
            @Override
            protected void bindData(RecyclerViewHolder holder, int position, final FourSModel fourSModel) {
                NewCarModelItem item = (NewCarModelItem) holder.getView(R.id.new_car_item);
                item.addData(fourSModel, areaID, series.seriesName);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转
                       if (isFrom4S)
                            ActivitySwitcherThirdPartShop.toFilterThirdSpecialActivity
                                    (getActivity(), brandName, series, fourSModel.modelName);
                        else
                            ActivitySwitcherThirdPartShop.toShopInfoOfNewCarList
                                    (getActivity(), areaID, series.getSeriesName(), fourSModel.modelName);
                    }
                });
            }

            @Override
            protected int getItemLayout(int viewType) {
                return R.layout.item_new_car_model;
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
        client.searchModelList(areaID, mPage, count, brandName, series.seriesName, getLoadingAnimResponseHandler(true));
    }

    @Override
    protected void onSuccessResponse(String response) {
        ArrayList<FourSModel> fourSModels = JSONUtils.fromJson(response, new TypeToken<ArrayList<FourSModel>>() {
        });
        if (fourSModels != null) {
            if (fourSModels.size() < count)
                mPtrHelper.setHasMore(false);
            else mPtrHelper.setHasMore(true);

            if (mPage == DEFAULT_PATE)
                this.fourSModels.clear();
            this.fourSModels.addAll(fourSModels);
            baseRecyclerAdapter.notifyDataSetChanged();
            mPtrFrameLayoutView.setVisibility(View.VISIBLE);
            mRequestFailView.setVisibility(View.GONE);
        } else {
            mPtrHelper.setHasMore(false);
        }
        if (this.fourSModels.size() == 0) {
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
