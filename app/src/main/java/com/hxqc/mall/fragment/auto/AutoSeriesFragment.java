package com.hxqc.mall.fragment.auto;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.hxqc.mall.control.AutoBrandDataControl;
import com.hxqc.mall.core.adapter.AutoSeriesExpandableAdapter;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.model.AutoSeriesGroup;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.List;

import hxqc.mall.R;

/**
 * Author: HuJunJie
 * Date: 2015-03-13
 * FIXME
 * Todo  车系列表
 */
public class AutoSeriesFragment extends FunctionFragment implements
        AutoBrandDataControl.SeriesHandler,ExpandableListView.OnChildClickListener {

    public static AutoSeriesFragment newInstance() {
        AutoSeriesFragment fragment = new AutoSeriesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    PinnedHeaderExpandableListView mExpandableListView;
    AutoSeriesExpandableAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auto_series, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mExpandableListView = (PinnedHeaderExpandableListView) view.findViewById(R.id.auto_series_ExpandableListView);
        mExpandableListView.setOnChildClickListener(this);
    }

    @Override
    public void onSucceed(List< AutoSeriesGroup > seriesGroups) {
        if (mAdapter == null) {
            mAdapter = new AutoSeriesExpandableAdapter(mContext, (java.util.ArrayList< AutoSeriesGroup >) seriesGroups);
            mExpandableListView.setAdapter(mAdapter);
            mExpandableListView.setOnHeaderUpdateListener(mAdapter);
        }
        mAdapter.notifyData((java.util.ArrayList< AutoSeriesGroup >) seriesGroups);
        for (int i = 0, count = mAdapter.getGroupCount(); i < count; i++) {
            mExpandableListView.expandGroup(i);
        }
    }

    @Override
    public void onFailed() {

    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        AutoBrandDataControl.getInstance().toAutoList(getActivity(), groupPosition,  childPosition);
        return false;
    }

    @Override
    public String fragmentDescription() {
        return "车系选择";
    }
}
