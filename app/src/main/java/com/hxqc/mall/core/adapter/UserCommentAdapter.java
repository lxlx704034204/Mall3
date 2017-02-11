package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hxqc.mall.core.model.CommentForList;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.core.util.comment.SwitchHelper;
import com.hxqc.mall.core.views.Order.OrderDescription;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 说明:评论列表适配器
 *
 * author: 吕飞
 * since: 2015-04-02
 * Copyright:恒信汽车电子商务有限公司
 */
public class UserCommentAdapter extends RecyclerView.Adapter {
    ArrayList< CommentForList > mCommentForLists;
    Context mContext;

    public UserCommentAdapter(ArrayList< CommentForList > mCommentForLists, Context mContext) {
        this.mCommentForLists = mCommentForLists;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_comment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CommentForList mCommentForList = mCommentForLists.get(position);
//        ((ViewHeadHolder) holder).mOrderDescriptionView.showNumView();
        ((ViewHolder) holder).mOrderDescriptionView.initOrderDescription(mContext, mCommentForList,true);
        if (mCommentForList.commentStatus == 0) {
            ((ViewHolder) holder).mCommentBtnView.setText(R.string.me_emit_comment);
        } else {
            ((ViewHolder) holder).mCommentBtnView.setText(R.string.me_see_comment);
        }
        ((ViewHolder) holder).mCommentBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCommentForList.commentStatus == 0) {
                    ActivitySwitcher.toSendComment(mContext, mCommentForList.itemID, mCommentForList.orderID);
                } else {
                    SwitchHelper.toCommentDetail(mCommentForList.sku,mCommentForList.commentID,mContext);
                }
            }
        });
        ((ViewHolder) holder).mOrderDescriptionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //为什么一定普通车辆 因为特卖没有评论
                ActivitySwitcher.toAutoItemDetail(mContext, AutoItem.AUTO_COMMON,
                        mCommentForList.itemID, mCommentForList.getItemDescription());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCommentForLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        OrderDescription mOrderDescriptionView;
        Button mCommentBtnView;

        public ViewHolder(View v) {
            super(v);
            mCommentBtnView = (Button) v.findViewById(R.id.comment_btn);
            mOrderDescriptionView = (OrderDescription) v.findViewById(R.id.order_description);
        }
    }
}
