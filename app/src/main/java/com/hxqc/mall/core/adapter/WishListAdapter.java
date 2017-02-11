package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.api.ApiClient;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.model.CollectInfo;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 说明:关注列表
 * <p>
 * author: 吕飞
 * since: 2015-03-16
 * Copyright:恒信汽车电子商务有限公司
 */
public class WishListAdapter extends RecyclerView.Adapter {
    ArrayList<CollectInfo> mCollectInfos;
    Context mContext;

    public WishListAdapter(ArrayList<CollectInfo> collectInfos, Context context) {
        this.mCollectInfos = collectInfos;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wish_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final CollectInfo mCollectInfo = mCollectInfos.get(position);
//        mCollectInfo.collectState = true;
        showCollectState(holder, mCollectInfo);
        ((ViewHolder) holder).mAutoModelView.setText(mCollectInfo.itemDescription);
        ((ViewHolder) holder).mAutoPriceView.setText(mCollectInfo.getItemPrice());
        ((ViewHolder) holder).mStarView.setVisibility(View.VISIBLE);
        ImageUtil.setImage(mContext, ((ViewHolder) holder).mAutoImageView, mCollectInfo.itemThumb);
        ((ViewHolder) holder).mStarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewHolder) holder).mStarView.setClickable(false);
                mCollectInfo.collectState = !mCollectInfo.collectState;
                showCollectState(holder, mCollectInfo);
                new ApiClient().collect(mCollectInfo.itemID, mCollectInfo.collectState, new BaseMallJsonHttpResponseHandler(mContext) {
                    @Override
                    public void onSuccess(String response) {
                        ((ViewHolder) holder).mStarView.setClickable(true);
                    }
                });
            }
        });
        ((ViewHolder) holder).mWishListItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcher.toAutoItemDetail(mContext, AutoItem.AUTO_COMMON,
                        mCollectInfo.itemID, mCollectInfo.getItemDescription());
            }
        });
    }

    private void showCollectState(RecyclerView.ViewHolder holder, CollectInfo mCollectInfo) {
        if (mCollectInfo.collectState) {
            ((ViewHolder) holder).mStarView.setImageResource(R.drawable.ic_wishlist_focus);
        } else {
            ((ViewHolder) holder).mStarView.setImageResource(R.drawable.ic_me_wishlist);
        }
    }

    @Override
    public int getItemCount() {
        return mCollectInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mAutoModelView;//车型
        TextView mAutoPriceView;//价格
        ImageView mStarView;//关注数量
        RelativeLayout mWishListItemView;
        ImageView mAutoImageView;//车图像

        public ViewHolder(View v) {
            super(v);
            mStarView = (ImageView) v.findViewById(R.id.star);
            mAutoModelView = (TextView) v.findViewById(R.id.auto_model);
            mAutoPriceView = (TextView) v.findViewById(R.id.auto_price);
            mWishListItemView = (RelativeLayout) v.findViewById(R.id.wish_list_item);
            mAutoImageView = (ImageView) v.findViewById(R.id.auto_image);
        }
    }
}
