package com.hxqc.newenergy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxqc.newenergy.bean.FilterItem;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Function:车型筛选的recyclerView的Adapter
 *
 * @author 袁秉勇
 * @since 2015年12月05日
 */
public class EV_PriceAndMileageAdapter extends RecyclerView.Adapter {
    private final static int IS_HEADER = 0;
    private final static int IS_NORMAL = 1;
    ArrayList< FilterItem > filterItems = new ArrayList<>();
    Context mContext;
    PriceMileageCallBack mPriceMileageCallBack;
    private int lastClickcPosition = 0;


    public void setPriceMileageCallBack(PriceMileageCallBack mPriceMileageCallBack) {
        this.mPriceMileageCallBack = mPriceMileageCallBack;
    }


    public EV_PriceAndMileageAdapter(Context context, ArrayList< FilterItem > filterItems) {
        this.filterItems = filterItems;
        mContext = context;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return IS_HEADER;
        } else {
            return IS_NORMAL;
        }
    }


    @Override
    public int getItemCount() {
        return filterItems==null? 0 : filterItems.size()+1;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == IS_HEADER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.ev_no_limit_item_header, null);
            return new ViewHolder(view, IS_HEADER);
        } else if (viewType == IS_NORMAL) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.ev_filter_price_item, parent, false);
            return new ViewHolder(view, IS_NORMAL);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == 0 && ((ViewHolder) holder).viewType == IS_HEADER) {
            ((ViewHolder) holder).mNoLimitView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mPriceMileageCallBack != null) mPriceMileageCallBack.onClickPriceMileageCallBack(null);
                    if (lastClickcPosition == 0) return;
                    notifyChangeStyle();
                    lastClickcPosition = 0;
                }
            });
            if (lastClickcPosition == 0) {
                ((ViewHolder) holder).mDividerLineView.setBackgroundColor(Color.parseColor("#FF7143"));
                ((ViewHolder) holder).mNoLimitView.setTextColor(mContext.getResources().getColor(R.color.filter_text_orange));
            } else {
                ((ViewHolder) holder).mDividerLineView.setBackgroundColor(Color.parseColor("#D9D9D9"));
                ((ViewHolder) holder).mNoLimitView.setTextColor(mContext.getResources().getColor(R.color.filter_text_black));
            }
        } else if (position > 0 && ((ViewHolder) holder).viewType == IS_NORMAL) {
            if (position - 1 >= 0 && position - 1 < filterItems.size()) {
                final FilterItem filterItem = filterItems.get(position - 1);
                ((ViewHolder) holder).priceView.setText(filterItem.label);
                ((ViewHolder) holder).priceView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mPriceMileageCallBack != null) mPriceMileageCallBack.onClickPriceMileageCallBack(filterItem);
                        if (lastClickcPosition == position) return;
                        notifyChangeStyle();
                        lastClickcPosition = position;
                    }
                });
            }

            if (position == lastClickcPosition) {
                ((ViewHolder) holder).mDividerLineView.setBackgroundColor(Color.parseColor("#FF7143"));
                ((ViewHolder) holder).priceView.setTextColor(mContext.getResources().getColor(R.color.filter_text_orange));
            } else {
                ((ViewHolder) holder).mDividerLineView.setBackgroundColor(Color.parseColor("#D9D9D9"));
                ((ViewHolder) holder).priceView.setTextColor(mContext.getResources().getColor(R.color.filter_text_black));
            }
        }
    }


    private void notifyChangeStyle() {
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView priceView;
        TextView mNoLimitView;
        View mDividerLineView;
        int viewType;


        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;

            if (viewType == IS_HEADER) {
                mNoLimitView = (TextView) itemView.findViewById(R.id.no_limit_condition);
                mDividerLineView = itemView.findViewById(R.id.divider_line_bottom);
            } else if (viewType == IS_NORMAL) {
                priceView = (TextView) itemView.findViewById(R.id.price_text);
                mDividerLineView = itemView.findViewById(R.id.divider_line_bottom);
            }
        }
    }

    public interface PriceMileageCallBack {
        void onClickPriceMileageCallBack(FilterItem filterItem);
    }
}
