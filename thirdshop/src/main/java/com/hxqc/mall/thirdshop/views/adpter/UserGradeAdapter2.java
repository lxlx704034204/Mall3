package com.hxqc.mall.thirdshop.views.adpter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.WidgetController;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.newcar.UserGradeComment;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.utils.AreaSiteUtil;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 口碑评价 ListView Adapter
 * Created by huangyi on 15/10/21.
 */
public class UserGradeAdapter2 extends BaseAdapter {

    boolean isShowCar;
    Context mContext;
    ArrayList<UserGradeComment> mComment = new ArrayList<>();
    OnClickListener mListener;
    LayoutInflater mLayoutInflater;

    public UserGradeAdapter2(Context mContext, boolean isShowCar) {
        this.isShowCar = isShowCar;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public UserGradeAdapter2(boolean isShowCar, Context mContext, ArrayList<UserGradeComment> mComment, OnClickListener mListener) {
        this.isShowCar = isShowCar;
        this.mContext = mContext;
        this.mComment = mComment;
        this.mListener = mListener;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    /**
     * @param isClear true刷新 false加载
     */
    public void setData(ArrayList<UserGradeComment> data, boolean isClear) {
        if (isClear) {
            this.mComment.clear();
            this.mComment = data;
        } else {
            addDate(data);
        }
        notifyDataSetChanged();
    }

    // 拼接list
    public void addDate(ArrayList<UserGradeComment> list) {
        for (int i = 0; i < list.size(); i++) {
            this.mComment.add(list.get(i));
        }
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
    public UserGradeComment getItem(int position) {
        return mComment.get(position);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_user_discuss2, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mPhotoView = (CircleImageView) convertView.findViewById(R.id.comment_photo);
            mViewHolder.mNameView = (TextView) convertView.findViewById(R.id.comment_name);
            mViewHolder.mStatusView = (TextView) convertView.findViewById(R.id.comment_status);
            mViewHolder.mScoreView = (TextView) convertView.findViewById(R.id.comment_score);
            mViewHolder.mDateView = (TextView) convertView.findViewById(R.id.comment_date);
            mViewHolder.mContentView = (TextView) convertView.findViewById(R.id.comment_content);
            mViewHolder.mCarView = (TextView) convertView.findViewById(R.id.comment_car);
            mViewHolder.mRecyclerView = (RecyclerView) convertView.findViewById(R.id.comment_recycler);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        final UserGradeComment model = mComment.get(position);

        ImageUtil.setImageSquare(mContext, mViewHolder.mPhotoView, model.userInfo.userAvatar);
        mViewHolder.mNameView.setText(model.userInfo.nickName.trim());
        mViewHolder.mScoreView.setText(model.grade.average + "分");
        try {
            String date1 = model.time.split(" ")[0];
            String date2 = model.time.split(" ")[1].replace(".", ":").substring(0, 5);
            mViewHolder.mDateView.setText(date1 + " " + date2);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        mViewHolder.mContentView.setText(model.content.trim());
        if (isShowCar) {
            mViewHolder.mStatusView.setVisibility(View.VISIBLE);
            mViewHolder.mStatusView.setText(model.statusText);
            mViewHolder.mCarView.setVisibility(View.VISIBLE);
            mViewHolder.mCarView.setText(model.auto.model.trim() + " >>");
            mViewHolder.mCarView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivitySwitcherThirdPartShop.toNewCarModelDetail(mContext, AreaSiteUtil.getInstance(mContext).getSiteID(),
                            model.auto.extID, model.auto.brand, model.auto.model);
                }
            });
            switch (model.status) {
                case 10: //待审核 删除 复制
                    mViewHolder.mContentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDeleteAndCopy(model.gradeID, view, model.content.trim());
                        }
                    });
                    break;

                case 20: //审核通过 复制
                    mViewHolder.mContentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showCopy(view, model.content.trim());
                        }
                    });
                    break;

                case 30: //审核不通过 删除 复制
                    mViewHolder.mContentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDeleteAndCopy(model.gradeID, view, model.content.trim());
                        }
                    });
                    break;
            }

        } else {
            mViewHolder.mStatusView.setVisibility(View.GONE);
            mViewHolder.mCarView.setVisibility(View.GONE);
            mViewHolder.mContentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCopy(view, model.content.trim());
                }
            });
        }
        if (null == model.images || model.images.size() == 0) {
            mViewHolder.mRecyclerView.setVisibility(View.GONE);

        } else {
            mViewHolder.mRecyclerView.setVisibility(View.VISIBLE);
            mViewHolder.mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
            mViewHolder.mRecyclerView.setAdapter(new CommentPhotoAdapter(model.images));
        }

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

    private void showDeleteAndCopy(final String gradeID, final View view, final String content) {
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
                if (null != mListener) mListener.onDelete(gradeID);
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
        void onDelete(String gradeID);
    }

    class ViewHolder {
        CircleImageView mPhotoView;
        TextView mNameView, mStatusView, mScoreView, mDateView, mContentView, mCarView;
        RecyclerView mRecyclerView;
    }
}
