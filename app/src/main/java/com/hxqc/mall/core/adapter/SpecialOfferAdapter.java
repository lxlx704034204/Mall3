package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hxqc.mall.core.model.SpecialOffer;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 说明:特卖的Adapter
 * <p/>
 * author: 吕飞
 * since: 2015-05-07
 * Copyright:恒信汽车电子商务有限公司
 */
public class SpecialOfferAdapter extends RecyclerView.Adapter {
    ArrayList< SpecialOffer > mSpecialOffers;
    Context mContext;

    public SpecialOfferAdapter(ArrayList< SpecialOffer > mSpecialOffers, Context mContext) {
        this.mSpecialOffers = mSpecialOffers;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_special_offer,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final SpecialOffer mSpecialOffer = mSpecialOffers.get(position);
        ((ViewHolder) holder).mTimeView.setText(mSpecialOffer.reckonByTime(mContext));
        ImageUtil.setImage(mContext, ((ViewHolder) holder).mAutoImageView, mSpecialOffer.itemPic);
        ((ViewHolder) holder).mAutoNameView.setText(mSpecialOffer.getItemDescription());
        ((ViewHolder) holder).mPriceView.setText(String.format("¥%s", mSpecialOffer.getItemPrice()));
        ((ViewHolder) holder).mCutView.setText(mSpecialOffer.getItemFall());

        ((ViewHolder) holder).mDepositView.setText(String.format("¥%s", OtherUtil.amountFormat(mSpecialOffer.getSubscription())));
        if (mSpecialOffer.stock.equals("0")) {
            ((ViewHolder) holder).mSelloutView.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).mStockTextView.setVisibility(View.GONE);
            ((ViewHolder) holder).mStockView.setVisibility(View.GONE);
        } else {
            ((ViewHolder) holder).mStockView.setText(mSpecialOffer.getInventory());
            ((ViewHolder) holder).mSelloutView.setVisibility(View.GONE);
            ((ViewHolder) holder).mStockTextView.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).mStockView.setVisibility(View.VISIBLE);
        }
        ((ViewHolder) holder).mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcher.toAutoItemDetail(mContext, AutoItem.AUTO_PROMOTION,
                        mSpecialOffer.itemID, mSpecialOffer.getItemDescription());
            }
        });
        if (mSpecialOffer.status != -1) {
            int[] mStatus = {R.drawable.corner_green, R.drawable.corner_underway, R.drawable.corner_gray};
            ((ViewHolder) holder).mSpecialOfferStatus.setImageResource(
                    mStatus[mSpecialOffer.status]);
        }
        showProgress(mSpecialOffer, holder);

    }

    private void showProgress(SpecialOffer mSpecialOffer, RecyclerView.ViewHolder holder) {
        ((ViewHolder) holder).mProgressBar.setMax((int) (mSpecialOffer.getEndTimeLong() - mSpecialOffer.getStartTimeLong()));
        if (mSpecialOffer.getEndTimeLong() > mSpecialOffer.serverL && mSpecialOffer.getStartTimeLong() < mSpecialOffer.serverL) {
            try {
                ((ViewHolder) holder).mProgressBar.setProgress((int) (mSpecialOffer.getEndTimeLong() - mSpecialOffer.serverL));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            ((ViewHolder) holder).mProgressBar.setProgress(0);
        }
    }


    @Override
    public int getItemCount() {
        return mSpecialOffers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        ImageView mAutoImageView;
        ImageView mSpecialOfferStatus;
        TextView mAutoNameView;
        TextView mPriceView;
        TextView mCutView;
        TextView mDepositView;
        TextView mStockView;
        TextView mTimeView;
        TextView mStockTextView;
        ImageView mSelloutView;//抢光了图标
        ProgressBar mProgressBar;

        public ViewHolder(View v) {
            super(v);
            mSpecialOfferStatus = (ImageView) v.findViewById(R.id.special_offer_status);
            mCardView = (CardView) v.findViewById(R.id.card_view);
            mAutoImageView = (ImageView) v.findViewById(R.id.auto_image);
            mAutoNameView = (TextView) v.findViewById(R.id.auto_name);
            mPriceView = (TextView) v.findViewById(R.id.price);
            mCutView = (TextView) v.findViewById(R.id.cut);
            mDepositView = (TextView) v.findViewById(R.id.deposit);
            mStockView = (TextView) v.findViewById(R.id.stock);
            mTimeView = (TextView) v.findViewById(R.id.time);
            mStockTextView = (TextView) v.findViewById(R.id.stock_text);
            mSelloutView = (ImageView) v.findViewById(R.id.sellout);
            mProgressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        }
    }
}
