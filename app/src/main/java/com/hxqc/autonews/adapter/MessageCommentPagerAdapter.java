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

import com.hxqc.autonews.model.MessageComment;
import com.hxqc.autonews.widget.AutoInfoItem;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.WidgetController;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import hxqc.mall.R;

/**
 * 资讯评价 ListView Adapter
 * Created by huangyi on 15/10/21.
 */
public class MessageCommentPagerAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<MessageComment> mComment;
    OnClickListener mListener;
    LayoutInflater mLayoutInflater;

    public MessageCommentPagerAdapter(Context mContext, ArrayList<MessageComment> mComment, OnClickListener mListener) {
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
    public MessageComment getItem(int position) {
        return mComment.get(position);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_message_comment_pager, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mPhotoView = (CircleImageView) convertView.findViewById(R.id.comment_photo);
            mViewHolder.mNameView = (TextView) convertView.findViewById(R.id.comment_name);
            mViewHolder.mDateView = (TextView) convertView.findViewById(R.id.comment_date);
            mViewHolder.mStatusView = (TextView) convertView.findViewById(R.id.comment_status);
            mViewHolder.mContentView = (TextView) convertView.findViewById(R.id.comment_content);
            mViewHolder.mParentNameView = (TextView) convertView.findViewById(R.id.comment_parent_name);
            mViewHolder.mParentContentView = (TextView) convertView.findViewById(R.id.comment_parent_content);
            mViewHolder.mParentView = (LinearLayout) convertView.findViewById(R.id.comment_parent);
            mViewHolder.mAutoView = (AutoInfoItem) convertView.findViewById(R.id.comment_auto);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        final MessageComment model = mComment.get(position);

        ImageUtil.setImageSquare(mContext, mViewHolder.mPhotoView, model.myComment.commentUser.userAvatar);
        mViewHolder.mNameView.setText(model.myComment.commentUser.nickName.trim());
        mViewHolder.mDateView.setText(model.myComment.time.trim());
        mViewHolder.mStatusView.setText(model.myComment.statusText.trim());
        mViewHolder.mContentView.setText(model.myComment.content.trim());
        if (null == model.parentComment || null == model.parentComment.commentUser) {
            //对资讯进行评价
            mViewHolder.mParentView.setVisibility(View.GONE);
            switch (model.myComment.status) {
                case "10": //待审核 删除 复制
                    mViewHolder.mContentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDeleteAndCopy(model.myComment.commentID, view, model.myComment.content.trim());
                        }
                    });
                    break;

                case "20": //审核通过 复制
                    mViewHolder.mContentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showCopy(view, model.myComment.content.trim());
                        }
                    });
                    break;

                case "30": //审核不通过 删除 复制
                    mViewHolder.mContentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDeleteAndCopy(model.myComment.commentID, view, model.myComment.content.trim());
                        }
                    });
                    break;
            }

        } else {
            mViewHolder.mParentView.setVisibility(View.VISIBLE);
            mViewHolder.mParentNameView.setText(model.parentComment.commentUser.nickName.trim() + ":");
            mViewHolder.mParentContentView.setText(model.parentComment.content.trim());
            switch (model.myComment.status) {
                case "10": //待审核 删除 复制
                    mViewHolder.mContentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDeleteAndCopy(model.myComment.commentID, view, model.myComment.content.trim());
                        }
                    });
                    break;

                case "20": //审核通过 复制
                    mViewHolder.mContentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showCopy(view, model.myComment.content.trim());
                        }
                    });
                    break;

                case "30": //审核不通过 删除 复制
                    mViewHolder.mContentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDeleteAndCopy(model.myComment.commentID, view, model.myComment.content.trim());
                        }
                    });
                    break;
            }
        }
        mViewHolder.mAutoView.addData(model.autoInfo);

        return convertView;
    }

    private void showCopy(final View view, final String content) {
        view.setBackgroundColor(Color.parseColor("#f4f5f6"));
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_option_one, null);
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
        TextView optionView = (TextView) contentView.findViewById(R.id.option);
        optionView.setText("复制");
        optionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("simple text", content);
                clipboard.setPrimaryClip(clip);
                popupWindow.dismiss();
            }
        });
        popupWindow.showAsDropDown(view, 200, -view.getHeight() - WidgetController.getHeight(contentView));
    }

    private void showDeleteAndCopy(final String commentID, final View view, final String content) {
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
        leftView.setText("删除");
        leftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mListener) mListener.onDelete(commentID);
                popupWindow.dismiss();
            }
        });
        TextView rightView = (TextView) contentView.findViewById(R.id.option_right);
        rightView.setText("复制");
        rightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("simple text", content);
                clipboard.setPrimaryClip(clip);
                popupWindow.dismiss();
            }
        });
        popupWindow.showAsDropDown(view, 200, -view.getHeight() - WidgetController.getHeight(contentView));
    }

    public interface OnClickListener {

        /**
         * 删除
         **/
        void onDelete(String infoID);
    }

    class ViewHolder {
        CircleImageView mPhotoView;
        TextView mNameView, mDateView, mStatusView, mContentView, mParentNameView, mParentContentView;
        LinearLayout mParentView;
        AutoInfoItem mAutoView;
    }
}
