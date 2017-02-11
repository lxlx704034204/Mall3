package com.hxqc.mall.core.adapter.comment;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.comment.view.MyRatingBarView;
import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.model.comment.AdditionModel;
import com.hxqc.mall.core.model.comment.CommentModel;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.views.ExpandableTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import hxqc.mall.R;

/**
 * Author: wanghao
 * Date: 2015-03-18
 * FIXME
 * Todo
 * 评论列表 adapter
 */
public class MyCommentListAdapterHolder extends RecyclerView.Adapter< RecyclerView.ViewHolder > {

    /**
     * 1.只有评论
     * 2.评论+照片
     * 3.评论+追评
     * 4.评论+照片+追评
     * 5.评论+追评+追评照片
     * 6.评论+照片+追评+追评照片
     * <p/>
     * 加入评论的星星
     * 列表刷新
     */
    final int COMMENT_ONLY = 7;
    final int COMMENT_CIMAGES = 1;
    final int COMMENT_APPEND = 2;
    final int COMMENT_CIMAGES_APPEND = 3;
    final int COMMENT_APPEND_AIMAGES = 4;
    final int COMMENT_CIMAGES_APPEND_AIMAGES = 5;
    final int NOTHING = 6;
    //    DisplayImageOptions options = new DisplayImageOptions.Builder()
//            .cacheInMemory(true)
//            .showImageForEmptyUri(
//                    R.drawable.ic_comment_user)
//            .showImageOnFail(
//                    R.drawable.ic_comment_user)
//            .showImageOnLoading(
//                    R.drawable.ic_comment_user)
//            .cacheOnDisk(true)
//            .showImageForEmptyUri(null).build();
    SparseBooleanArray mCollapsedStatus;
    OnItemClickListener mItemClickListener;
    Context context;
    private ArrayList< CommentModel > mData;

    public MyCommentListAdapterHolder(ArrayList< CommentModel > mData) {
        this.mData = mData;
        mCollapsedStatus = new SparseBooleanArray();
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        CommentModel comment = mData.get(position);
        ArrayList< ImageModel > images_a = null;

        AdditionModel addition = comment.addition;

        if (TextUtils.isEmpty(addition.content)) {
            addition = null;
        }

        ArrayList< ImageModel > images_c = comment.images;
        if (addition != null) {
            images_a = addition.images;
        }

        if (mData == null || mData.size() == 0) {
            return NOTHING;
        } else {
            if (addition == null && (images_c == null || images_c.size() == 0)) {
                return COMMENT_ONLY;
            } else if (addition == null && (images_c.size() != 0)) {
                return COMMENT_CIMAGES;
            } else if (addition != null && (images_c == null || images_c.size() == 0) && (images_a == null || images_a.size() == 0)) {
                return COMMENT_APPEND;
            } else if (addition != null && (images_c != null && images_c.size() != 0) && (images_a == null || images_a.size() == 0)) {
                return COMMENT_CIMAGES_APPEND;
            } else if (addition != null && (images_c == null || images_c.size() == 0) && (images_a != null && images_a.size() != 0)) {
                return COMMENT_APPEND_AIMAGES;
            } else if (addition != null && (images_c != null && images_c.size() != 0) && (images_a != null && images_a.size() != 0)) {
                return COMMENT_CIMAGES_APPEND_AIMAGES;
            }
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View sView;

        if (viewType == COMMENT_ONLY) {

            sView = mInflater.inflate(R.layout.item_comment_frame_comment, parent, false);
            return new VHolderComment_Only(sView);

        } else if (viewType == COMMENT_CIMAGES) {

            sView = mInflater.inflate(R.layout.item_comment_frame_com_cimage, parent, false);
            return new VHolderComment_CImages(sView);

        } else if (viewType == COMMENT_APPEND) {

            sView = mInflater.inflate(R.layout.item_comment_base_frame_list_and_append, parent, false);
            return new VHolderComment_Append(sView);

        } else if (viewType == COMMENT_CIMAGES_APPEND) {

            sView = mInflater.inflate(R.layout.item_comment_frame_c_cimg_append, parent, false);
            return new VHolderComment_CImagesAndAppend(sView);

        } else if (viewType == COMMENT_APPEND_AIMAGES) {

            sView = mInflater.inflate(R.layout.item_comment_frame_c_append_aimg, parent, false);
            return new VHolderComment_AppendAndAImages(sView);

        } else if (viewType == COMMENT_CIMAGES_APPEND_AIMAGES) {

            sView = mInflater.inflate(R.layout.item_comment_frame_c_cimg_append_aimg, parent, false);
            return new VHolderComment_All(sView);

        }
        // 啥都木有
        sView = mInflater.inflate(R.layout.item_comment_nothing, parent, false);
        return new VHolderComment_Nothing(sView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        CommentModel comment = mData.get(position);
        ArrayList< ImageModel > images_a = null;
        AdditionModel addition = comment.addition;
        ArrayList< ImageModel > images_c = comment.images;

        if (addition != null) {
            if (TextUtils.isEmpty(addition.content)) {
                addition = null;
            } else {
                images_a = addition.images;
            }
        }

        if (mData == null || mData.size() == 0) {

        } else {


            if (addition == null && (images_c == null || images_c.size() == 0)) {
                VHolderComment_Only cHolder = (VHolderComment_Only) holder;
//                ImageLoader.getInstance().displayImage(comment.userAvatar, cHolder.avatar,
//                        options);
                ImageUtil.setImage(context, cHolder.avatar, comment.userAvatar, R.drawable.ic_productcomment_list_user);
                cHolder.mUsername.setText(comment.userNickname);
                cHolder.mCreateTime.setText(comment.createTime);
                cHolder.starView.setStar(Integer.parseInt(comment.score));
                cHolder.tv_comment.setText(comment.content, mCollapsedStatus, 0);
                cHolder.mColor.setText("车身：" + comment.itemColor + "   " + " 内饰：" + comment.itemInterior);

                if (comment.reply != null) {

                    if (TextUtils.isEmpty(comment.reply.content)) {
                        cHolder.mReplayView.setVisibility(View.GONE);
                    } else {
                        cHolder.mReplayView.setVisibility(View.VISIBLE);
                        cHolder.mReplyTitle.setText(comment.reply.replier);
                        cHolder.mReplyContent.setText(comment.reply.content);
                        cHolder.mReplyTime.setText(comment.reply.createTime);
                    }

                } else {
                    cHolder.mReplayView.setVisibility(View.GONE);
                }

            } else if (addition == null && (images_c.size() != 0)) {
                VHolderComment_CImages ciHolder = (VHolderComment_CImages) holder;
//                ImageLoader.getInstance().displayImage(comment.userAvatar, ciHolder.avatar,
//                        options);
                ImageUtil.setImage(context, ciHolder.avatar, comment.userAvatar, R.drawable.ic_productcomment_list_user);
                //图片列表-------------------------------
                initImages(images_c, ciHolder.images);
                ciHolder.starView.setStar(Integer.parseInt(comment.score));
                ciHolder.mUsername.setText(comment.userNickname);
                ciHolder.mCreateTime.setText(comment.createTime);
                ciHolder.tv_comment.setText(comment.content, mCollapsedStatus, 0);
                ciHolder.mColor.setText("车身：" + comment.itemColor + "   " + " 内饰：" + comment.itemInterior);

                if (comment.reply != null) {

                    if (TextUtils.isEmpty(comment.reply.content)) {
                        ciHolder.mReplayView.setVisibility(View.GONE);
                    } else {
                        ciHolder.mReplayView.setVisibility(View.VISIBLE);
                        ciHolder.mReplyTitle.setText(comment.reply.replier);
                        ciHolder.mReplyContent.setText(comment.reply.content);
                        ciHolder.mReplyTime.setText(comment.reply.createTime);
                    }

                } else {
                    ciHolder.mReplayView.setVisibility(View.GONE);
                }

            } else if (addition != null && (images_c == null || images_c.size() == 0) && (images_a == null || images_a.size() == 0)) {

                VHolderComment_Append caHolder = (VHolderComment_Append) holder;
                ImageUtil.setImage(context, caHolder.avatar, comment.userAvatar, R.drawable.ic_productcomment_list_user);
                caHolder.mUsername.setText(comment.userNickname);
                caHolder.mCreateTime.setText(comment.createTime);
                caHolder.tv_comment.setText(comment.content, mCollapsedStatus, 0);
                caHolder.starView.setStar(Integer.parseInt(comment.score));

//                caHolder.mDeltaTime.setText();
                caHolder.mAppendText.setText(addition.content, mCollapsedStatus, 0);
                caHolder.mAppendTime.setText(addition.createTime);

            } else if (addition != null && (images_c != null && images_c.size() != 0) && (images_a == null || images_a.size() == 0)) {
                VHolderComment_CImagesAndAppend ccaHolder = (VHolderComment_CImagesAndAppend) holder;
//                ImageLoader.getInstance().displayImage(comment.userAvatar, ccaHolder.avatar,
//                        options);
                ImageUtil.setImage(context, ccaHolder.avatar, comment.userAvatar, R.drawable.ic_productcomment_list_user);
                //图片列表-------------------------------
                initImages(images_c, ccaHolder.images);
                ccaHolder.starView.setStar(Integer.parseInt(comment.score));
                ccaHolder.mUsername.setText(comment.userNickname);
                ccaHolder.mCreateTime.setText(comment.createTime);
                ccaHolder.tv_comment.setText(comment.content, mCollapsedStatus, 0);

                ccaHolder.mAppendText.setText(addition.content, mCollapsedStatus, 0);
                ccaHolder.mAppendTime.setText(addition.createTime);

            } else if (addition != null && (images_c == null || images_c.size() == 0) && (images_a != null && images_a.size() != 0)) {

                VHolderComment_AppendAndAImages caaiHolder = (VHolderComment_AppendAndAImages) holder;

//                ImageLoader.getInstance().displayImage(comment.userAvatar, caaiHolder.avatar,
//                        options);
                ImageUtil.setImage(context, caaiHolder.avatar, comment.userAvatar, R.drawable.ic_productcomment_list_user);
                //图片列表-------------------------------
                caaiHolder.starView.setStar(Integer.parseInt(comment.score));
                caaiHolder.mUsername.setText(comment.userNickname);
                caaiHolder.mCreateTime.setText(comment.createTime);
                caaiHolder.tv_comment.setText(comment.content, mCollapsedStatus, 0);

                initImages(images_a, caaiHolder.mAppendImages);
                caaiHolder.mAppendText.setText(addition.content, mCollapsedStatus, 0);
                caaiHolder.mAppendTime.setText(addition.createTime);


            } else if (addition != null && (images_c != null && images_c.size() != 0) && (images_a != null && images_a.size() != 0)) {
                VHolderComment_All cAllHolder = (VHolderComment_All) holder;


//                ImageLoader.getInstance().displayImage(comment.userAvatar, cAllHolder.avatar,
//                        options);
                ImageUtil.setImage(context, cAllHolder.avatar, comment.userAvatar, R.drawable.ic_productcomment_list_user);
                //图片列表-------------------------------
                initImages(images_c, cAllHolder.images);
                cAllHolder.starView.setStar(Integer.parseInt(comment.score));
                cAllHolder.mUsername.setText(comment.userNickname);
                cAllHolder.mCreateTime.setText(comment.createTime);
                cAllHolder.tv_comment.setText(comment.content, mCollapsedStatus, 0);

                initImages(images_a, cAllHolder.mAppendImages);
                cAllHolder.mAppendText.setText(addition.content, mCollapsedStatus, 0);
                cAllHolder.mAppendTime.setText(addition.createTime);

            }
        }


    }

    private void initImages(ArrayList< ImageModel > list, RecyclerView images) {

        MyCommentImageAdapterHolder adapterHolder = new MyCommentImageAdapterHolder(list);
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

        LinearLayout mReplayView;
        TextView mReplyTitle;
        TextView mReplyContent;
        TextView mReplyTime;

        public VHolderComment_Only(View itemView) {
            super(itemView);
            mColor = (TextView) itemView.findViewById(R.id.tv_car_color);
            mUsername = (TextView) itemView.findViewById(R.id.tv_username_comment);
            mCreateTime = (TextView) itemView.findViewById(R.id.tv_comment_post_time);
            tv_comment = (ExpandableTextView) itemView.findViewById(R.id.tv_this_one_comment);
            avatar = (CircleImageView) itemView.findViewById(R.id.me_avatar);
            starView = (MyRatingBarView) itemView.findViewById(R.id.starView);

            mReplayView = (LinearLayout) itemView.findViewById(R.id.comment_reply_view);
            mReplyTitle = (TextView) itemView.findViewById(R.id.comment_reply_title);
            mReplyContent = (TextView) itemView.findViewById(R.id.comment_reply_content);
            mReplyTime = (TextView) itemView.findViewById(R.id.comment_reply_time);
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

        LinearLayout mReplayView;
        TextView mReplyTitle;
        TextView mReplyContent;
        TextView mReplyTime;

        public VHolderComment_CImages(View itemView) {
            super(itemView);
            mColor = (TextView) itemView.findViewById(R.id.tv_car_color);
            mUsername = (TextView) itemView.findViewById(R.id.tv_username_comment);
            mCreateTime = (TextView) itemView.findViewById(R.id.tv_comment_post_time);
            tv_comment = (ExpandableTextView) itemView.findViewById(R.id.tv_this_one_comment);
            images = (RecyclerView) itemView.findViewById(R.id.rlv_comment_images);
            avatar = (CircleImageView) itemView.findViewById(R.id.me_avatar);
            starView = (MyRatingBarView) itemView.findViewById(R.id.starView);

            mReplayView = (LinearLayout) itemView.findViewById(R.id.comment_reply_view);
            mReplyTitle = (TextView) itemView.findViewById(R.id.comment_reply_title);
            mReplyContent = (TextView) itemView.findViewById(R.id.comment_reply_content);
            mReplyTime = (TextView) itemView.findViewById(R.id.comment_reply_time);
        }

        @Override
        public void onClick(View v) {
//            if (mItemClickListener != null) {
//
//                switch (v.getId()) {
//                }
//            }
        }
    }

    //3.评论+追评
    public class VHolderComment_Append extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mUsername;
        TextView mCreateTime;
        ExpandableTextView tv_comment;
        CircleImageView avatar;
        MyRatingBarView starView;

        TextView mDeltaTime;
        ExpandableTextView mAppendText;
        TextView mAppendTime;

        public VHolderComment_Append(View itemView) {
            super(itemView);
            mUsername = (TextView) itemView.findViewById(R.id.tv_username_comment);
            mCreateTime = (TextView) itemView.findViewById(R.id.tv_comment_post_time);
            tv_comment = (ExpandableTextView) itemView.findViewById(R.id.tv_this_one_comment);
            avatar = (CircleImageView) itemView.findViewById(R.id.me_avatar);
            starView = (MyRatingBarView) itemView.findViewById(R.id.starView);

            mDeltaTime = (TextView) itemView.findViewById(R.id.tv_append_since_post_time);
            mAppendText = (ExpandableTextView) itemView.findViewById(R.id.tv_comment_append_text);
            mAppendTime = (TextView) itemView.findViewById(R.id.tv_append_post_time);

        }

        @Override
        public void onClick(View v) {
//            if (mItemClickListener != null) {
//
//                switch (v.getId()) {
//                }
//            }
        }
    }

    // 4.评论+照片+追评
    public class VHolderComment_CImagesAndAppend extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mUsername;
        TextView mCreateTime;
        ExpandableTextView tv_comment;
        RecyclerView images;
        CircleImageView avatar;
        MyRatingBarView starView;

        TextView mDeltaTime;
        ExpandableTextView mAppendText;
        TextView mAppendTime;

        public VHolderComment_CImagesAndAppend(View itemView) {
            super(itemView);
            mUsername = (TextView) itemView.findViewById(R.id.tv_username_comment);
            mCreateTime = (TextView) itemView.findViewById(R.id.tv_comment_post_time);
            tv_comment = (ExpandableTextView) itemView.findViewById(R.id.tv_this_one_comment);
            images = (RecyclerView) itemView.findViewById(R.id.rlv_comment_images);
            avatar = (CircleImageView) itemView.findViewById(R.id.me_avatar);
            starView = (MyRatingBarView) itemView.findViewById(R.id.starView);
            mDeltaTime = (TextView) itemView.findViewById(R.id.tv_append_since_post_time);
            mAppendText = (ExpandableTextView) itemView.findViewById(R.id.tv_comment_append_text);
            mAppendTime = (TextView) itemView.findViewById(R.id.tv_append_post_time);
        }

        @Override
        public void onClick(View v) {
//            if (mItemClickListener != null) {
//
//                switch (v.getId()) {
//                }
//            }
        }
    }

    //5.评论+追评+追评照片
    public class VHolderComment_AppendAndAImages extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mUsername;
        TextView mCreateTime;
        ExpandableTextView tv_comment;
        CircleImageView avatar;
        MyRatingBarView starView;

        TextView mDeltaTime;
        ExpandableTextView mAppendText;
        TextView mAppendTime;
        RecyclerView mAppendImages;

        public VHolderComment_AppendAndAImages(View itemView) {
            super(itemView);
            mUsername = (TextView) itemView.findViewById(R.id.tv_username_comment);
            mCreateTime = (TextView) itemView.findViewById(R.id.tv_comment_post_time);
            tv_comment = (ExpandableTextView) itemView.findViewById(R.id.tv_this_one_comment);
            avatar = (CircleImageView) itemView.findViewById(R.id.me_avatar);
            starView = (MyRatingBarView) itemView.findViewById(R.id.starView);

            mDeltaTime = (TextView) itemView.findViewById(R.id.tv_append_since_post_time);
            mAppendText = (ExpandableTextView) itemView.findViewById(R.id.tv_comment_append_text);
            mAppendTime = (TextView) itemView.findViewById(R.id.tv_append_post_time);
            mAppendImages = (RecyclerView) itemView.findViewById(R.id.rlv_comment_append_images);
        }

        @Override
        public void onClick(View v) {
//            if (mItemClickListener != null) {
//
//                switch (v.getId()) {
//                }
//            }
        }
    }

    //6.评论+照片+追评+追评照片
    public class VHolderComment_All extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mUsername;
        TextView mCreateTime;
        ExpandableTextView tv_comment;
        RecyclerView images;
        CircleImageView avatar;
        MyRatingBarView starView;

        TextView mDeltaTime;
        ExpandableTextView mAppendText;
        TextView mAppendTime;
        RecyclerView mAppendImages;

        public VHolderComment_All(View itemView) {
            super(itemView);
            mUsername = (TextView) itemView.findViewById(R.id.tv_username_comment);
            mCreateTime = (TextView) itemView.findViewById(R.id.tv_comment_post_time);

            tv_comment = (ExpandableTextView) itemView.findViewById(R.id.tv_this_one_comment);
            images = (RecyclerView) itemView.findViewById(R.id.rlv_comment_images);
            avatar = (CircleImageView) itemView.findViewById(R.id.me_avatar);
            starView = (MyRatingBarView) itemView.findViewById(R.id.starView);

            mDeltaTime = (TextView) itemView.findViewById(R.id.tv_append_since_post_time);
            mAppendText = (ExpandableTextView) itemView.findViewById(R.id.tv_comment_append_text);
            mAppendTime = (TextView) itemView.findViewById(R.id.tv_append_post_time);
            mAppendImages = (RecyclerView) itemView.findViewById(R.id.rlv_comment_append_images);
        }

        @Override
        public void onClick(View v) {
//            if (mItemClickListener != null) {
//
//                switch (v.getId()) {
//                }
//            }
        }
    }

    //7.无评论
    public class VHolderComment_Nothing extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;

        public VHolderComment_Nothing(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_nothing);
        }

        @Override
        public void onClick(View v) {
//            if (mItemClickListener != null) {
//
//                switch (v.getId()) {
//                }
//            }
        }
    }
}
