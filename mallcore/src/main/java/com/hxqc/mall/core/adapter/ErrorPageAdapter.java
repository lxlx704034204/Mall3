package com.hxqc.mall.core.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * 说明:错误和空数据的Adapter
 * 简直了，mPtrFrameLayoutView的addView竟然没有用
 *
 * author: 吕飞
 * since: 2015-07-28
 * Copyright:恒信汽车电子商务有限公司
 */
public class ErrorPageAdapter extends RecyclerView.Adapter {
    View mShowView;

    public void init(View mShowView) {
        this.mShowView = mShowView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RecyclerView.ViewHolder(mShowView) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {

        return mShowView == null ? 0 : 1;
    }
}
