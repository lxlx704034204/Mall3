package com.hxqc.autonews.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hxqc.autonews.model.Comment;
import com.hxqc.mall.core.util.WidgetController;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 评论详情 ListView Adapter
 * Created by huangyi on 15/10/21.
 */
public class CommentDetailAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Comment> mComment;
    OnClickListener mListener;
    LayoutInflater mLayoutInflater;

    public CommentDetailAdapter(Context mContext, ArrayList<Comment> mComment, OnClickListener mListener) {
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
            convertView = mLayoutInflater.inflate(R.layout.item_comment_detail_hy, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mTopView = convertView.findViewById(R.id.comment_top);
            mViewHolder.mDividerView = convertView.findViewById(R.id.comment_divider);
            mViewHolder.mBottomView = convertView.findViewById(R.id.comment_bottom);
            mViewHolder.mNameView = (TextView) convertView.findViewById(R.id.comment_name);
            mViewHolder.mContentView = (TextView) convertView.findViewById(R.id.comment_content);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        final Comment model = mComment.get(position);

        if (position == 0) {
            mViewHolder.mTopView.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.mTopView.setVisibility(View.GONE);
        }
        if (position == mComment.size() - 1) {
            mViewHolder.mDividerView.setVisibility(View.GONE);
            mViewHolder.mBottomView.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.mDividerView.setVisibility(View.VISIBLE);
            mViewHolder.mBottomView.setVisibility(View.GONE);
        }
        if (model.toUser == null || TextUtils.isEmpty(model.toUser.nickName)) {
            mViewHolder.mNameView.setText(model.commentUser.nickName.trim());
        } else {
            mViewHolder.mNameView.setText(model.commentUser.nickName.trim() + " 回复 " + model.toUser.nickName);
        }
        mViewHolder.mContentView.setText(model.content.trim());
        mViewHolder.mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //view.setBackgroundColor(Color.parseColor("#f4f5f6"));
                View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_option_two, null);
                final PopupWindow popupWindow = new PopupWindow(contentView,
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        //view.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
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

        return convertView;
    }

    public interface OnClickListener {

        /**
         * 回复
         **/
        void onReply(String cCommentID);
    }

    class ViewHolder {
        View mTopView, mDividerView, mBottomView;
        TextView mNameView, mContentView;
    }
}
