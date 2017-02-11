package com.hxqc.mall.core.adapter.me;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.views.recyclerview.VRecyclerViewAdapter;
import com.hxqc.mall.core.views.recyclerview.VRecyclerViewFooter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-03-22
 * FIXME
 * Todo
 */
public class MyBillListAdapter_1 extends VRecyclerViewAdapter
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
    protected static final int TYPE_HEAD = 3;
    protected Context mContext;

    public MyBillListAdapter_1(VRecyclerViewFooter vRecyclerViewFooter) {
        super(vRecyclerViewFooter);
    }

    @Override
    protected int getVItemViewType(int position) {

        if (position == 0)
            return TYPE_HEAD;
        else
            return TYPE_NORMAL;
    }


    @Override
    public RecyclerView.ViewHolder onVCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater from = LayoutInflater.from(mContext);
        Log.d("vioson", "viewType:" + viewType);
        switch (viewType) {
            default:
                return null;
            case TYPE_HEAD:
                View view = from.inflate(R.layout.layout_bill_list_head, null);
                view.setLayoutParams(new ViewGroup.MarginLayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new HeaderHolder(view);
            case TYPE_NORMAL:
                View childView = from.inflate(R.layout.item_my_bill_list_child, null);
                return new ViewChildHolder(childView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    public void onVBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    @Override
    public long getHeaderId(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View headView = LayoutInflater.from(mContext).inflate(R.layout.item_my_bill_list_head, null);
        return new ViewHeadHolder(headView);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    protected class HeaderHolder extends RecyclerView.ViewHolder {
        TextView label_type, total_charge, total_cost, number, unit;
        LinearLayout empty_layout;
        ImageView icon_empty;
        TextView text_empty;

        public HeaderHolder(View itemView) {
            super(itemView);
            label_type = (TextView) itemView.findViewById(R.id.label_type);
            total_charge = (TextView) itemView.findViewById(R.id.total_charge);
            total_cost = (TextView) itemView.findViewById(R.id.total_cost);
            number = (TextView) itemView.findViewById(R.id.number);
            unit = (TextView) itemView.findViewById(R.id.unit);
            text_empty = (TextView) itemView.findViewById(R.id.text_empty);
            empty_layout = (LinearLayout) itemView.findViewById(R.id.empty_layout);
            icon_empty = (ImageView) itemView.findViewById(R.id.icon_empty);
        }
    }


    protected class ViewChildHolder extends RecyclerView.ViewHolder {
        TextView number;
        TextView date;
        TextView description;
        View rootView;

        public ViewChildHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            number = (TextView) itemView.findViewById(R.id.bill_number);
            date = (TextView) itemView.findViewById(R.id.bill_date);
            description = (TextView) itemView.findViewById(R.id.bill_description);
        }
    }

    protected class ViewHeadHolder extends RecyclerView.ViewHolder {
        TextView month;
        TextView totalCost;

        public ViewHeadHolder(View itemView) {
            super(itemView);
            month = (TextView) itemView.findViewById(R.id.month);
            totalCost = (TextView) itemView.findViewById(R.id.total_cost);
        }
    }

}
