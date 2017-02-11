package com.hxqc.mall.comment.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxqc.mall.comment.model.CommentListItem;
import com.hxqc.mall.comment.view.MyRatingBarView;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.views.ExpandableTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author: wanghao
 * Date: 2015-03-18
 * FIXME
 * Todo
 * 评论列表 adapter
 */
public class CommentListAdapterHolder extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 1.只有评论
     * 2.评论+照片
     * 加入评论的星星
     * 列表刷新
     */
    final int COMMENT_ONLY = 7;
    final int COMMENT_CIMAGES = 1;
    SparseBooleanArray mCollapsedStatus;
    OnItemClickListener mItemClickListener;
    Context context;
    private ArrayList<CommentListItem> mData;

    public CommentListAdapterHolder(ArrayList<CommentListItem> mData) {
        this.mData = mData;
        mCollapsedStatus = new SparseBooleanArray();
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        CommentListItem comment = mData.get(position);

        ArrayList<ImageModel> images_c = comment.imageItems();

        if (images_c == null || images_c.size() == 0) {
            return COMMENT_ONLY;
        } else if (images_c.size() != 0) {
            return COMMENT_CIMAGES;
        }

        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View sView = null;

        if (viewType == COMMENT_ONLY) {

            sView = mInflater.inflate(R.layout.c_item_comment_frame_comment, parent, false);
            return new VHolderComment_Only(sView);

        } else if (viewType == COMMENT_CIMAGES) {

            sView = mInflater.inflate(R.layout.c_item_comment_frame_com_cimage, parent, false);
            return new VHolderComment_CImages(sView);

        }
        // 啥都木有
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        CommentListItem comment = mData.get(position);
        ArrayList<ImageModel> images_c = comment.imageItems();

        if (mData == null || mData.size() == 0) {
        } else {
            if ((images_c == null || images_c.size() == 0)) {
                VHolderComment_Only cHolder = (VHolderComment_Only) holder;
                ImageUtil.setImage(context, cHolder.avatar, comment.userImage(), R.drawable.ic_productcomment_list_user);
                cHolder.mUsername.setText(comment.userNickname());
                cHolder.mCreateTime.setText(comment.dateTime());
                cHolder.starView.setStar(comment.scores());
                cHolder.tv_comment.setText(comment.content(), mCollapsedStatus, 0);
                if (TextUtils.isEmpty(comment.itemColor())){
                    cHolder.mColor.setVisibility(View.GONE);
                }else {
                    cHolder.mColor.setVisibility(View.VISIBLE);
                    cHolder.mColor.setText(String.format("车身：%s    内饰：%s", comment.itemColor(), comment.itemInterior()));
                }

            } else if ((images_c.size() != 0)) {
                VHolderComment_CImages ciHolder = (VHolderComment_CImages) holder;
                ImageUtil.setImage(context, ciHolder.avatar, comment.userImage(), R.drawable.ic_productcomment_list_user);
                //图片列表-------------------------------
                initImages(images_c, ciHolder.images);
                ciHolder.starView.setStar(comment.scores());
                ciHolder.mUsername.setText(comment.userNickname());
                ciHolder.mCreateTime.setText(comment.dateTime());
                ciHolder.tv_comment.setText(comment.content(), mCollapsedStatus, 0);
                if (TextUtils.isEmpty(comment.itemColor())){
                    ciHolder.mColor.setVisibility(View.GONE);
                }else {
                    ciHolder.mColor.setVisibility(View.VISIBLE);
                    ciHolder.mColor.setText(String.format("车身：%s    内饰：%s", comment.itemColor(), comment.itemInterior()));
                }
            }
        }
    }

    private void initImages(ArrayList<ImageModel> list, RecyclerView images) {

        CommentImageAdapterHolder adapterHolder = new CommentImageAdapterHolder(list);
        images.setAdapter(adapterHolder);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        images.setLayoutManager(layoutManager);
        images.setHasFixedSize(true);
        images.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, int type);
    }

    //1.只有评论
    public class VHolderComment_Only extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mUsername;
        TextView mCreateTime;
        ExpandableTextView tv_comment;
        CircleImageView avatar;
        MyRatingBarView starView;
        TextView mColor;


        public VHolderComment_Only(View itemView) {
            super(itemView);
            mColor = (TextView) itemView.findViewById(R.id.tv_car_color);
            mUsername = (TextView) itemView.findViewById(R.id.tv_username_comment);
            mCreateTime = (TextView) itemView.findViewById(R.id.tv_comment_post_time);
            tv_comment = (ExpandableTextView) itemView.findViewById(R.id.tv_this_one_comment);
            avatar = (CircleImageView) itemView.findViewById(R.id.me_avatar);
            starView = (MyRatingBarView) itemView.findViewById(R.id.starView);
        }

        @Override
        public void onClick(View v) {
        }
    }

    //2.评论+照片
    public class VHolderComment_CImages extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mUsername;
        TextView mCreateTime;
        ExpandableTextView tv_comment;
        RecyclerView images;
        CircleImageView avatar;
        MyRatingBarView starView;
        TextView mColor;


        public VHolderComment_CImages(View itemView) {
            super(itemView);
            mColor = (TextView) itemView.findViewById(R.id.tv_car_color);
            mUsername = (TextView) itemView.findViewById(R.id.tv_username_comment);
            mCreateTime = (TextView) itemView.findViewById(R.id.tv_comment_post_time);
            tv_comment = (ExpandableTextView) itemView.findViewById(R.id.tv_this_one_comment);
            images = (RecyclerView) itemView.findViewById(R.id.rlv_comment_images);
            avatar = (CircleImageView) itemView.findViewById(R.id.me_avatar);
            starView = (MyRatingBarView) itemView.findViewById(R.id.starView);

        }

        @Override
        public void onClick(View v) {
        }
    }

}
