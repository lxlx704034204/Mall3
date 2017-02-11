package com.hxqc.mall.thirdshop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hxqc.adapter.ObjectAdapter;
import com.hxqc.mall.auto.model.Filter;
import com.hxqc.mall.auto.model.FilterGroup;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.interfaces.FilterAction;

/**
 * Author: HuJunJie
 * Date: 2015-03-25
 * FIXME
 * Todo 找车条件
 */
public class FilterContentFragment extends FunctionFragment implements AdapterView.OnItemClickListener {
    FilterAction mFilterAction;
    ListView mListView;
    ObjectAdapter< Filter > mAdapter;
    View headView;
    FilterGroup mFilterGroup;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mFilterAction = (FilterAction) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter_content, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.filter_factor_listview);
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.item_filter_factor_head, null);
        mListView.addHeaderView(headView);
        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
        }
        mListView.setOnItemClickListener(this);
    }

    public void notifyFilterFactor(Context context, FilterGroup filterGroup) {
        this.mFilterGroup = filterGroup;
        mAdapter = new ObjectAdapter<>(context, this.mFilterGroup.filterItem, Filter.class, R.layout.item_filter_content,
                new String[]{"label"}, new int[]{R.id.filter_factor_content});
        if (mListView != null) {
            mListView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
        if (mFilterAction != null) {
            int pos = position != 0 ? position - 1 : 0;//有个不限为第一位，取0是为了获取筛选key
            Filter filter = mFilterGroup.filterItem.get(pos);
            mFilterAction.filterListener(position, filter, mFilterGroup);
        }
    }

    @Override
    public String fragmentDescription() {
        return "找车条件";
    }
}
