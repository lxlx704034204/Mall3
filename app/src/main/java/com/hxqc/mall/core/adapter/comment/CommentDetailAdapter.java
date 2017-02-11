package com.hxqc.mall.core.adapter.comment;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.comment.view.MyRatingBarView;
import com.hxqc.mall.core.model.comment.CommentDetailResponse;

import hxqc.mall.R;


/**
 * Author: wanghao
 * Date: 2015-05-04
 * FIXME
 * Todo
 */
public class CommentDetailAdapter extends BaseAdapter {

    CommentDetailResponse data;
    Context context;

    final static int only_comment = 0;
    final static int with_images = 1;

    public CommentDetailAdapter(CommentDetailResponse data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (data.images.size() == 0) {
            return only_comment;
        }
        return with_images;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CommentDetailResponse getItem(int position) {
        return data;
    }

    @Override
    public long getItemId(int position) {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (getItemViewType(position) == only_comment) {
            view = LayoutInflater.from(context).inflate(R.layout.item_comment_detail_only_text, null);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_comment_detail, null);
            RecyclerView mImages = (RecyclerView) view.findViewById(R.id.rlv_check_comment_images);
            mImages.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mImages.setLayoutManager(layoutManager);
            mImages.setItemAnimator(new DefaultItemAnimator());
            MyCommentImageAdapterHolder adapterHolder = new MyCommentImageAdapterHolder(data.images);
            mImages.setAdapter(adapterHolder);
        }

        MyRatingBarView myRatingBarView = (MyRatingBarView) view.findViewById(R.id.mrbv_press_star_check_comment);
        TextView mComment = (TextView) view.findViewById(R.id.tv_check_comment_);
        TextView mTime = (TextView) view.findViewById(R.id.tv_check_comment_time);

        LinearLayout replyView = (LinearLayout) view.findViewById(R.id.comment_reply_view);
        TextView replyTitle = (TextView) view.findViewById(R.id.comment_reply_title);
        TextView replyContent = (TextView) view.findViewById(R.id.comment_reply_content);
        TextView replyTime = (TextView) view.findViewById(R.id.comment_reply_time);

        if (!TextUtils.isEmpty(data.score))
            myRatingBarView.setStar(Integer.parseInt(data.score));

        mTime.setText(data.createTime);
        mComment.setText(data.content);

        if (data.reply != null) {

            if (TextUtils.isEmpty(data.reply.content)) {
                replyView.setVisibility(View.GONE);
            } else {
                replyView.setVisibility(View.VISIBLE);
                replyTitle.setText(data.reply.replier);
                replyContent.setText(data.reply.content);
                replyTime.setText(data.reply.createTime);
            }

        } else {
            replyView.setVisibility(View.GONE);
        }

        return view;
    }
}
