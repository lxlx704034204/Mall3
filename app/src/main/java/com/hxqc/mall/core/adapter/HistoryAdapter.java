package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.auto.AutoBaseInformation;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.SharedPreferencesHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 说明:最近浏览
 *
 * author: 吕飞
 * since: 2015-04-17
 * Copyright:恒信汽车电子商务有限公司
 */
public class HistoryAdapter extends RecyclerView.Adapter {
    ArrayList<AutoBaseInformation> mHistories;
    Context mContext;
    SharedPreferencesHelper mSharedPreferencesHelper;

    public HistoryAdapter(ArrayList<AutoBaseInformation> mHistories, Context context) {
        this.mHistories = mHistories;
        this.mContext = context;
        mSharedPreferencesHelper = new SharedPreferencesHelper(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wish_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final AutoBaseInformation mHistory = mHistories.get(position);
        ((ViewHolder) holder).mAutoModelView.setText(mHistory.getItemDescription());
        ((ViewHolder) holder).mAutoPriceView.setText("¥" + mHistory.getItemPriceU());
        ((ViewHolder) holder).mStarView.setVisibility(View.GONE);
        ImageUtil.setImage(mContext, ((ViewHolder) holder).mAutoImageView, mHistory.getItemThumb());
        ((ViewHolder) holder).mWishListItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcher.toAutoItemDetail(mContext, AutoItem.AUTO_COMMON,
                        mHistory.getItemID(), mHistory.getItemDescription());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHistories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mAutoModelView;//车型
        TextView mAutoPriceView;//价格
        ImageView mStarView;//关注区域
        RelativeLayout mWishListItemView;
        ImageView mAutoImageView;//车图像

        public ViewHolder(View v) {
            super(v);
            mAutoModelView = (TextView) v.findViewById(R.id.auto_model);
            mAutoPriceView = (TextView) v.findViewById(R.id.auto_price);
            mStarView = (ImageView) v.findViewById(R.id.star);
            mWishListItemView = (RelativeLayout) v.findViewById(R.id.wish_list_item);
            mAutoImageView = (ImageView) v.findViewById(R.id.auto_image);
        }
    }
}
