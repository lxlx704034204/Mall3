package com.hxqc.newenergy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.newenergy.bean.FilterItem;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Function: 新能源品牌筛选Adapter
 *
 * @author 袁秉勇
 * @since 2016年03月21日
 */
public class EV_FilterBrandAdapter extends RecyclerView.Adapter {
    Context mContext;

    private final static int IS_HEADER = 0;
    private final static int IS_NORMAL = 1;
    ArrayList< FilterItem > filterItems = new ArrayList<>();
    CarBrandCallBack mCarBrandCallBack;
    private int lastClickcPosition = 0;


    public void setmCarBrandCallBack(CarBrandCallBack mCarBrandCallBack) {
        this.mCarBrandCallBack = mCarBrandCallBack;
    }


    public EV_FilterBrandAdapter(Context mContext, ArrayList< FilterItem > filterItems) {
        this.filterItems = filterItems;
        this.mContext = mContext;
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
        return filterItems==null?0: filterItems.size() + 1;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == IS_HEADER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.ev_no_limit_item_header, null);
            return new ViewHolder(view, IS_HEADER);
        } else if (viewType == IS_NORMAL) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.ev_filter_brand_item, parent, false);
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
                    if (mCarBrandCallBack != null) mCarBrandCallBack.onClickBrandCallBack(null);
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
                ImageUtil.setImage(mContext, ((ViewHolder) holder).mBrandImageView, filterItem.picUrl);
                ((ViewHolder) holder).mBrandNameView.setText(filterItem.label);
                ((ViewHolder) holder).mBrandNameView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCarBrandCallBack != null) mCarBrandCallBack.onClickBrandCallBack(filterItem);
                        if (lastClickcPosition == position) return;
                        notifyChangeStyle();
                        lastClickcPosition = position;
                    }
                });

                if (position == lastClickcPosition) {
                    ((ViewHolder) holder).mDividerLineView.setBackgroundColor(Color.parseColor("#FF7143"));
                    ((ViewHolder) holder).mBrandNameView.setTextColor(mContext.getResources().getColor(R.color.filter_text_orange));
                } else {
                    ((ViewHolder) holder).mDividerLineView.setBackgroundColor(Color.parseColor("#D9D9D9"));
                    ((ViewHolder) holder).mBrandNameView.setTextColor(mContext.getResources().getColor(R.color.filter_text_black));
                }
            }
        }
    }


    private void notifyChangeStyle() {
        notifyItemChanged(0);
        notifyItemChanged(lastClickcPosition);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mBrandImageView;
        TextView mBrandNameView;
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
                mNoLimitView = (TextView) itemView.findViewById(R.id.no_limit_condition);
                mBrandImageView = (ImageView) itemView.findViewById(R.id.brand_image);
                mBrandNameView = (TextView) itemView.findViewById(R.id.brand_name);
                mDividerLineView = itemView.findViewById(R.id.divider_line_bottom);
            }
        }
    }

    public interface CarBrandCallBack {
        void onClickBrandCallBack(FilterItem filterItem);
    }
}
