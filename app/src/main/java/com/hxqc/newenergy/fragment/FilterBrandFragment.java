package com.hxqc.newenergy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.newenergy.adapter.EV_FilterBrandAdapter;
import com.hxqc.newenergy.bean.FilterItem;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 新能源品牌
 */
public class FilterBrandFragment extends FunctionFragment implements EV_FilterBrandAdapter.CarBrandCallBack {
    private final static String TAG = FilterBrandFragment.class.getSimpleName();
    protected RecyclerView mRecyclerView;
    protected View rootView;
    protected RequestFailView mRequestFailView;
    private FilterBrandFragmentCallBack filterBrandFragmentCallBack;
    EV_FilterBrandAdapter ev_filterBrandAdapter;


    public void setFilterBrandFragmentCallBack(FilterBrandFragmentCallBack filterBrandFragmentCallBack) {
        this.filterBrandFragmentCallBack = filterBrandFragmentCallBack;
    }


    public static FilterBrandFragment newInstance(ArrayList< FilterItem > filterItems) {
        Bundle args = new Bundle();
        FilterBrandFragment fragment = new FilterBrandFragment();
        args.putParcelableArrayList("brand", filterItems);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.ev_filter_price_activity, container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRequestFailView = (RequestFailView) view.findViewById(R.id.request_view);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList< FilterItem > filterItems = getArguments().getParcelableArrayList("brand");

        if (filterItems!=null&&filterItems.size() > 0) {
            ev_filterBrandAdapter = new EV_FilterBrandAdapter(getContext(), filterItems);
            ev_filterBrandAdapter.setmCarBrandCallBack(this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setAdapter(ev_filterBrandAdapter);
        } else {
            mRequestFailView.setEmptyDescription("未找到结果");
            mRequestFailView.setEmptyButtonClick("重新加载", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


    @Override
    public String fragmentDescription() {
        return "4s店品牌";
    }


    @Override
    public void onClickBrandCallBack(FilterItem filterItem) {
        if (filterBrandFragmentCallBack != null) {
            filterBrandFragmentCallBack.onFilterBrandCallback(filterItem);
            ev_filterBrandAdapter.notifyDataSetChanged();
        }
    }


    public interface FilterBrandFragmentCallBack {
        void onFilterBrandCallback(FilterItem filterItem);
    }
}
