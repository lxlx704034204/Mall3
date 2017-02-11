package com.hxqc.pay.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import hxqc.mall.R;

/**
 * Author: wanghao
 * Date: 2015-03-13
 * FIXME
 * Todo
 */
public class EventCardAdapterHolder extends RecyclerView.Adapter< RecyclerView.ViewHolder > {


    private List< String > mDatas;

    public EventCardAdapterHolder(List< String > mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View sView = mInflater.inflate(R.layout.item_event_list, parent, false);
        return new ViewHolderCard(sView);
    }

    public class ViewHolderCard extends RecyclerView.ViewHolder implements View.OnClickListener {

//        TextView mAdd;
//        Button mCompleteInfo;

        public ViewHolderCard(View itemView) {
            super(itemView);
//            mAdd = (TextView) itemView.findViewById(R.id.tv_add);
//            mAdd.setCompoundDrawablePadding(4);
//            mCompleteInfo = (Button) itemView.findViewById(R.id.btn_complete);
//            mAdd.setOnClickListener(this);
//            mCompleteInfo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
