package com.hxqc.mall.core.views.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author:李烽
 * Date:2016-04-16
 * FIXME
 * Todo 适配器
 */
public abstract class VRecyclerViewAdapter extends RecyclerView.Adapter {
    protected static final int TYPE_FOOTER = 2;
    protected static final int TYPE_NORMAL = 1;

    public VRecyclerViewAdapter(VRecyclerViewFooter vRecyclerViewFooter) {
        mVRecyclerViewFooter = vRecyclerViewFooter;
    }

    public VRecyclerViewFooter getVRecyclerViewFooter() {
        return mVRecyclerViewFooter;
    }


    private VRecyclerViewFooter mVRecyclerViewFooter;

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) return TYPE_FOOTER;
        else
            return getVItemViewType(position);
    }

    protected abstract int getVItemViewType(int position);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            ViewGroup.LayoutParams layoutParams =
                    new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mVRecyclerViewFooter.setLayoutParams(layoutParams);
            return new FootHolder(mVRecyclerViewFooter);
        } else
            return onVCreateViewHolder(parent, viewType);
    }

    public abstract RecyclerView.ViewHolder onVCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == TYPE_FOOTER)
            mVRecyclerViewFooter = ((FootHolder) holder).vRecyclerViewFooter;
        else
            onVBindViewHolder(holder, position);
    }

    public abstract void onVBindViewHolder(RecyclerView.ViewHolder holder, int position);


    @Override
    public abstract int getItemCount();

    public class FootHolder extends RecyclerView.ViewHolder {
        VRecyclerViewFooter vRecyclerViewFooter;

        public FootHolder(View itemView) {
            super(itemView);
            try {
                vRecyclerViewFooter = (VRecyclerViewFooter) itemView;
            } catch (Exception e) {
                Log.e("TypeErrorException", "vRecyclerViewFooter must instanceof VRecyclerViewFooter");
            }

        }
    }
}
