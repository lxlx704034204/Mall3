package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.GoldenSeller;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2015-11-30
 * FIXME
 * Todo 金牌销售横向列表
 */
public class GoldenSellersView extends LinearLayout {
    private ListAdapter adapter;

    public void addData(ArrayList<GoldenSeller> data) {
        if (data == null || data.size() == 0) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            mData.clear();
            mData.addAll(data);
            adapter.notifyDataSetChanged();
        }

    }

    private ArrayList<GoldenSeller> mData;

    public GoldenSellersView(Context context) {
        this(context, null);
    }

    public GoldenSellersView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    private OnItemClickListener onItemClickListener;

    public GoldenSellersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.t_view_golden_sellers, this);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.golden_sellers_list);
        LinearLayoutManager manager = new LinearLayoutManager(context, HORIZONTAL, false);
        mRecyclerView.setLayoutManager(manager);
        mData = new ArrayList<>();
        adapter = new ListAdapter(mData);
        mRecyclerView.setAdapter(adapter);
    }


    class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<GoldenSeller> sellers;

        private static final int ITEM_TYPE_LAST = 1;
        private static final int ITEM_TYPE_NORMAL = 0;

        public ListAdapter(ArrayList<GoldenSeller> mData) {
            this.sellers = mData;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//            View view = LayoutInflater.from(getContext()).inflate(R.layout.t_item_golden_sellers_list, null);
            GoldenSellerItem view = new GoldenSellerItem(getContext());
            return new ViewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            return position == getItemCount() - 1 ? ITEM_TYPE_LAST : ITEM_TYPE_NORMAL;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
            ListAdapter.ViewHolder holder = (ViewHolder) viewHolder;
            holder.item.setGoldenSeller(sellers.get(i));
            if (getItemViewType(i) == ITEM_TYPE_LAST) {
                holder.item.setFullPadding();
//                DebugLog.setDebug(true);
//                DebugLog.d("GoldenItem", i + "");
            }
            holder.item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onItemClickListener)
                        onItemClickListener.onItemClick(GoldenSellersView.this, i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return sellers.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            private GoldenSellerItem item;

            public ViewHolder(GoldenSellerItem itemView) {
                super(itemView);
                item = itemView;
            }
        }
    }

}
