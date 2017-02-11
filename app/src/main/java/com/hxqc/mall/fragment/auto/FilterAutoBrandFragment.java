package com.hxqc.mall.fragment.auto;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import com.hxqc.mall.auto.model.FilterGroup;
import com.hxqc.mall.core.model.Brand;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.thirdshop.interfaces.FilterAction;

import hxqc.mall.R;

/**
 * 品牌筛选
 */
public class FilterAutoBrandFragment extends AutoBrandFragment implements ExpandableListView.OnChildClickListener,
        View.OnClickListener {

    FilterAction mFilterAction;
    FilterGroup mFilterGroup;
    View mHeaderView;

    public static FilterAutoBrandFragment instantiate(int itemCategory) {
        FilterAutoBrandFragment autoBrandFragment = new FilterAutoBrandFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(AutoItem.ItemCategory, itemCategory);
        autoBrandFragment.setArguments(bundle);
        return autoBrandFragment;
    }

    public FilterAutoBrandFragment() {

    }


    public void setFilterGroup(FilterGroup mFilterGroup) {
        this.mFilterGroup = mFilterGroup;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mFilterAction = (FilterAction) activity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.item_filter_factor_head, null);
        mHeaderView.setOnClickListener(this);
        mExpandableListView.addHeaderView(mHeaderView);
        mExpandableListView.setOnChildClickListener(this);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Brand brand = mBrandDataProvider.getBrandGroups(itemCategory).get(groupPosition).group.get(childPosition);
        mFilterAction.filterListener(1, brand, mFilterGroup);//0会移除
        return false;
    }

    @Override
    public void onClick(View v) {
        Brand brand = mBrandDataProvider.getBrandGroups(itemCategory).get(0).group.get(0);
        mFilterAction.filterListener(0, brand, mFilterGroup);
    }

    @Override
    public String fragmentDescription() {
        return "筛选品牌";
    }
}
