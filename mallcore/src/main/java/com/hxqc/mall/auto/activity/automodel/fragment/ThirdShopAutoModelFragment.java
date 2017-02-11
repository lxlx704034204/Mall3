package com.hxqc.mall.auto.activity.automodel.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hxqc.mall.auto.activity.automodel.adapter.ThirdShopAutoModelExpandableAdapter;
import com.hxqc.mall.auto.model.AutoModelGroup;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 18
 * FIXME
 * Todo 选择车型
 */
public class ThirdShopAutoModelFragment extends BaseAutoTypeFragment {

    private static final String TAG = "ThirdShopAutoModelFragment";
    private ThirdShopAutoModelExpandableAdapter mAutoModelAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        TextView heardView = new TextView(mContext);
//        heardView.setText("选择车型");
//        heardView.setTextColor(getActivity().getResources().getColor(R.color.black));
//        heardView.setTextSize(16);
//        heardView.setPadding(32, 16, 0, 16);
//        mExpandableListView.addHeaderView(heardView);
        sideBarView.setVisibility(View.GONE);
    }

    @Override
    public String fragmentDescription() {
        return "选择车型";
    }

    public void showAutoModel(ArrayList<AutoModelGroup> modelGroups, String series) {
        if (mAutoModelAdapter == null) {
            mAutoModelAdapter = new ThirdShopAutoModelExpandableAdapter(getActivity(), modelGroups,series);
            mExpandableListView.setAdapter(mAutoModelAdapter);
            mExpandableListView.setOnHeaderUpdateListener(mAutoModelAdapter);
            OtherUtil.openAllChild(mAutoModelAdapter, mExpandableListView);
        } else {
            DebugLog.i(TAG, "刷新数据");
            mAutoModelAdapter.notifyData(modelGroups,series);
            for (int i = 0, count = mAutoModelAdapter.getGroupCount(); i < count; i++) {
                mExpandableListView.expandGroup(i);
            }
        }
    }

    public void showEmptyAutoModel() {
        mExpandableListView.setEmptyView(mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail));
    }



}
