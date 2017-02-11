package com.hxqc.mall.fragment.auto;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.auto.AutoListActivity;
import com.hxqc.mall.api.NewAutoClient;
import com.hxqc.mall.core.adapter.SearchHintExpandableAdapter_2;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.model.HintMode;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.HashMap;
import java.util.Map;

import hxqc.mall.R;

/**
 * Author: liaoguilong
 * Date: 2015-11-30 19:52:32
 * FIXME
 * Todob  搜索提示
 */
public class SearchHintFragment_2 extends FunctionFragment implements ExpandableListView.OnChildClickListener {

    PinnedHeaderExpandableListView mExpandableView;
    SearchHintExpandableAdapter_2 mAdapter;
    ExpandableListView.OnChildClickListener mOnChildClickListener;
    NewAutoClient client;
    SearchHintListener mHintListener;

    public interface SearchHintListener {
        void showHintFragment(boolean isShow);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if (activity instanceof ExpandableListView.OnChildClickListener) {
            mOnChildClickListener = (ExpandableListView.OnChildClickListener) activity;
        }
        if (activity instanceof SearchHintListener) {
            mHintListener = (SearchHintListener) activity;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_hint, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mExpandableView = (PinnedHeaderExpandableListView) view.findViewById(R.id.list);
        mAdapter = new SearchHintExpandableAdapter_2(getActivity());
        mExpandableView.setAdapter(mAdapter);
        mExpandableView.setOnHeaderUpdateListener(mAdapter);
        mExpandableView.setOnChildClickListener(this);
    }

    public void notifyData(HintMode hintMode) {
        mAdapter.notifyData(hintMode);
        OtherUtil.openAllChild(mAdapter, mExpandableView);
    }

    @Override
    public String fragmentDescription() {
        return "搜索提示";
    }

    public void findHint(final String keyword) {
        if (client == null) {
            client = new NewAutoClient();
        }
        client.searchHintV2(keyword, new BaseMallJsonHttpResponseHandler(getActivity()) {
            @Override
            public void onSuccess(String response) {
                HintMode hintMode = JSONUtils.fromJson(response, new TypeToken< HintMode >() {
                });
                if (hintMode != null ) {
                    if( hintMode.auto.size() > 0 || hintMode.electricVehicle.size() > 0)
                     mHintListener.showHintFragment(true);
                    else
                     mHintListener.showHintFragment(false);
                } else {
                    mHintListener.showHintFragment(false);
                }
                notifyData(hintMode);
            }
        });
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int i1, long l) {
        if (mOnChildClickListener != null) {
            mOnChildClickListener.onChildClick(expandableListView, view, groupPosition, i1, l);
        }
        Map<String,String> hintMap=new HashMap<>();

        hintMap.put(AutoItem.ItemCategory, String.valueOf(mAdapter.getGroupId(groupPosition)));
        hintMap.put(AutoListActivity.KEYWORD,((HintMode.Suggestion) view.getTag()).suggestion);
    /*    DebugLog.i("onChildClick","itemCategory:"+String.valueOf(mAdapter.getGroupId(groupPosition)));
        DebugLog.i("onChildClick","keyword:"+((HintMode.Suggestion) view.getTag()).suggestion);*/
        ActivitySwitcher.toAutoList(getActivity(),hintMap );
        return false;
    }
}
