package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.promotion.SalesNewsModel;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;

import java.util.ArrayList;

/**
 * Author: wanghao
 * Date: 2015-11-30
 * FIXME      促销列表
 * Todo
 */
public class SalesNewsAdapterHolder extends RecyclerView.Adapter< RecyclerView.ViewHolder > {


    Context context;
    private ArrayList< SalesNewsModel > mData;

    public SalesNewsAdapterHolder(ArrayList< SalesNewsModel > mData) {
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View sView = mInflater.inflate(R.layout.t_item_sales_news_view, parent, false);
        return new VHolderNews(sView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final VHolderNews holderNews = (VHolderNews) holder;
        final SalesNewsModel newsModel = mData.get(position);

        ImageUtil.setImage(context, holderNews.mSaleImg, newsModel.thumb);
        holderNews.mTitle.setText(newsModel.title);
        holderNews.mPostTime.setText(newsModel.publishDate);
        holderNews.mInfo.setText(newsModel.summary);

        holderNews.mClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"点击了第："+position+"个",Toast.LENGTH_SHORT).show();
                ActivitySwitcherThirdPartShop.toSalesNewsDetail(newsModel.newsID, context);
            }
        });
    }

    public class VHolderNews extends RecyclerView.ViewHolder {
        RelativeLayout mClick;
        ImageView mSaleImg;
        TextView mTitle;
        TextView mPostTime;
        TextView mInfo;

        public VHolderNews(View itemView) {
            super(itemView);
            mClick = (RelativeLayout) itemView.findViewById(R.id.rl_news_item_click_view);
            mSaleImg = (ImageView) itemView.findViewById(R.id.iv_news_item);
            mTitle = (TextView) itemView.findViewById(R.id.tv_news_title);
            mPostTime = (TextView) itemView.findViewById(R.id.tv_news_post_time);
            mInfo = (TextView) itemView.findViewById(R.id.tv_news_info);
        }

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
}
