package com.hxqc.autonews.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hxqc.autonews.model.Comment;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.WidgetController;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import hxqc.mall.R;

/**
 * 全部评价 ListView Adapter
 * Created by huangyi on 15/10/21.
 */
public class AllCommentAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Comment> mComment;
    OnClickListener mListener;
    LayoutInflater mLayoutInflater;

    public AllCommentAdapter(Context mContext, ArrayList<Comment> mComment, OnClickListener mListener) {
        this.mContext = mContext;
        this.mComment = mComment;
        this.mListener = mListener;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return null == mComment ? 0 : mComment.size();
    }

    @Override
    public Comment getItem(int position) {
        return mComment.get(position);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_all_comment, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mPhotoView = (CircleImageView) convertView.findViewById(R.id.comment_photo);
            mViewHolder.mNameView = (TextView) convertView.findViewById(R.id.comment_name);
            mViewHolder.mDateView = (TextView) convertView.findViewById(R.id.comment_date);
            mViewHolder.mContentView = (TextView) convertView.findViewById(R.id.comment_content);
            mViewHolder.mMoreView = (TextView) convertView.findViewById(R.id.comment_more);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        final Comment model = mComment.get(position);

        ImageUtil.setImageSquare(mContext, mViewHolder.mPhotoView, model.commentUser.userAvatar);
        mViewHolder.mNameView.setText(model.commentUser.nickName.trim());
        mViewHolder.mDateView.setText(model.time.trim());
        mViewHolder.mContentView.setText(model.content.trim());
        mViewHolder.mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setBackgroundColor(Color.parseColor("#f4f5f6"));
                View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_option_two, null);
                final PopupWindow popupWindow = new PopupWindow(contentView,
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        view.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
                    }
                });
                TextView leftView = (TextView) contentView.findViewById(R.id.option_left);
                leftView.setText("回复");
                leftView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null != mListener) mListener.onReply(model.commentID);
                        popupWindow.dismiss();
                    }
                });
                TextView rightView = (TextView) contentView.findViewById(R.id.option_right);
                rightView.setText("复制");
                rightView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("simple text", model.content.trim());
                        clipboard.setPrimaryClip(clip);
                        popupWindow.dismiss();
                    }
                });
                popupWindow.showAsDropDown(view, 200, -view.getHeight() - WidgetController.getHeight(contentView));
            }
        });
        if (model.chileCommentCount == 0) {
            mViewHolder.mMoreView.setVisibility(View.GONE);

        } else {
            mViewHolder.mMoreView.setVisibility(View.VISIBLE);
            mViewHolder.mMoreView.setText("查看全部" + model.chileCommentCount + "条回复 >>");
            mViewHolder.mMoreView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mListener) mListener.onLook(model.commentID);
                }
            });
        }

        return convertView;
    }

    public interface OnClickListener {

        /**
         * 回复
         **/
        void onReply(String pCommentID);

        /**
         * 查看全部回复
         **/
        void onLook(String pCommentID);
    }

    class ViewHolder {
        CircleImageView mPhotoView;
        TextView mNameView, mDateView, mContentView, mMoreView;
    }
}
