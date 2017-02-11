package com.hxqc.mall.fragment.auto;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.api.NewAutoClient;
import com.hxqc.mall.core.adapter.SearchHintExpandableAdapter;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.model.Brand;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author: HuJunJie
 * Date: 2015-03-30
 * FIXME
 * Todo 搜索提示
 */
@Deprecated
public class SearchHintFragment extends FunctionFragment {

    PinnedHeaderExpandableListView mExpandableView;
    SearchHintExpandableAdapter mAdapter;
    ExpandableListView.OnChildClickListener mOnChildClickListener;
    NewAutoClient client;
    SearchHintListener mHintListener;

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
        mAdapter = new SearchHintExpandableAdapter(getActivity());
        mExpandableView.setAdapter(mAdapter);
        mExpandableView.setOnHeaderUpdateListener(mAdapter);
        mExpandableView.setOnChildClickListener(mOnChildClickListener);
    }

    public void notifyData(ArrayList< Brand > brandArrayList) {
        mAdapter.notifyData(brandArrayList);
        OtherUtil.openAllChild(mAdapter, mExpandableView);
    }

    @Override
    public String fragmentDescription() {
        return "搜索提示";
    }

    public void findHint(String keyword) {
        if (client == null) {
            client = new NewAutoClient();
        }
        client.searchHint(keyword, new BaseMallJsonHttpResponseHandler(getActivity()) {
            @Override
            public void onSuccess(String response) {
                ArrayList< Brand > brandArrayList = JSONUtils.fromJson(response, new TypeToken< ArrayList< Brand > >() {
                });
                notifyData(brandArrayList);
                if (brandArrayList.size() <= 0) {
                    mHintListener.showHintFragment(false);
                } else {
                    mHintListener.showHintFragment(true);
                }
            }
        });
    }

    public interface SearchHintListener {
        void showHintFragment(boolean isShow);
    }

}
