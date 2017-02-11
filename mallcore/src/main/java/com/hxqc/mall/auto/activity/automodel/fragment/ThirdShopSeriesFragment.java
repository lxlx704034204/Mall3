package com.hxqc.mall.auto.activity.automodel.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hxqc.mall.auto.activity.automodel.adapter.ThirdShopSeriesExpandableAdapter;
import com.hxqc.mall.auto.model.SeriesGroup;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 18
 * FIXME
 * Todo 选择车系
 */
public class ThirdShopSeriesFragment extends BaseAutoTypeFragment {

    private static final String TAG = "ThirdShopSeriesFragment";
    private ThirdShopSeriesExpandableAdapter mAutoSeriesAdapter;
    private ArrayList<SeriesGroup> mSeriesGroups;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*TextView heardView = new TextView(mContext);
        heardView.setText("选择车系");
        heardView.setTextColor(getActivity().getResources().getColor(R.color.black));
        heardView.setTextSize(16);
        heardView.setPadding(32, 16, 0, 16);
        mExpandableListView.addHeaderView(heardView);*/
        sideBarView.setVisibility(View.GONE);

    }

    @Override
    public String fragmentDescription() {
        return "选择车系";
    }

    public void showSeries(ArrayList<SeriesGroup> seriesGroups) {
        DebugLog.i(TAG, "ThirdShopSeriesFragment:" + seriesGroups.size());
        if (seriesGroups == null) {
            DebugLog.i(TAG, "没有数据");
            return;
        }
        mSeriesGroups = seriesGroups;
        if (mAutoSeriesAdapter == null) {
            DebugLog.i(TAG, "有数据");
            mAutoSeriesAdapter = new ThirdShopSeriesExpandableAdapter(mContext, seriesGroups);
            mExpandableListView.setAdapter(mAutoSeriesAdapter);
            mExpandableListView.setOnHeaderUpdateListener(mAutoSeriesAdapter);
            OtherUtil.openAllChild(mAutoSeriesAdapter, mExpandableListView);
        } else {
            DebugLog.i(TAG, "刷新数据");
            mAutoSeriesAdapter.notifyData(seriesGroups);
            for (int i = 0, count = mAutoSeriesAdapter.getGroupCount(); i < count; i++) {
                mExpandableListView.expandGroup(i);
            }
        }
    }

    public void showEmptySeries() {
        mExpandableListView.setEmptyView(mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail));
    }

    public ArrayList<SeriesGroup> getSeriesGroups() {
        return mSeriesGroups;
    }

}
