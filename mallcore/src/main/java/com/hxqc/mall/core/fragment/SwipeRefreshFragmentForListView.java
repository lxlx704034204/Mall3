package com.hxqc.mall.core.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hxqc.mall.core.R;


/**
 * 说明:列表Fragment基类
 *
 * author: 吕飞
 * since: 2015-04-09
 * Copyright:恒信汽车电子商务有限公司
 */
public abstract class SwipeRefreshFragmentForListView extends SwipeRefreshFragment {
    protected ListView mListView;
//    protected Adapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_swipe_refresh_for_list_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.list_view);
    }
}
