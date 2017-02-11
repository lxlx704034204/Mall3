package com.hxqc.newenergy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.newenergy.adapter.EV_PriceAndMileageAdapter;
import com.hxqc.newenergy.bean.FilterItem;
import com.hxqc.newenergy.control.FilterController;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年03月18日
 */
public class FilterMileageFragment extends FunctionFragment implements EV_PriceAndMileageAdapter.PriceMileageCallBack {
    private final static String TAG = FilterMileageFragment.class.getSimpleName();
    private Context mContext;

    private FilterController mFilterController;
    protected View rootView;
    protected RecyclerView mRecyclerView;
    RequestFailView mRequestFailView;
    FilterMileageFragmentCallBack filterMileageFragmentCallBack;


    public void setFilterMileageCallBack(FilterMileageFragmentCallBack filterMileageFragmentCallBack) {
        this.filterMileageFragmentCallBack = filterMileageFragmentCallBack;
    }


    public static FilterMileageFragment newInstance(ArrayList< FilterItem > filterItems) {
        Bundle args = new Bundle();
        FilterMileageFragment fragment = new FilterMileageFragment();
        args.putParcelableArrayList("mileage", filterItems);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        ArrayList< FilterItem > filterItems = getArguments().getParcelableArrayList("mileage");

        if (filterItems.size() > 0) {
            EV_PriceAndMileageAdapter ev_priceAndMileageAdapter = new EV_PriceAndMileageAdapter(getContext(), filterItems);
            ev_priceAndMileageAdapter.setPriceMileageCallBack(this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setAdapter(ev_priceAndMileageAdapter);
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
        return "4S点车型";
    }


    @Override
    public void onClickPriceMileageCallBack(FilterItem filterItem) {
        if (filterMileageFragmentCallBack != null) filterMileageFragmentCallBack.onFilterMileageCallBack(filterItem);
    }


    public interface FilterMileageFragmentCallBack {
        void onFilterMileageCallBack(FilterItem filterItem);
    }
}