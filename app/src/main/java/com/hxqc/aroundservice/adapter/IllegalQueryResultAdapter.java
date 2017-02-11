package com.hxqc.aroundservice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.aroundservice.model.IllegalQueryResultInfo;
import com.hxqc.mall.core.util.OtherUtil;

import java.util.ArrayList;
import java.util.HashMap;

import hxqc.mall.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 09
 * FIXME
 * Todo 违章查询结果的Adapter
 */
public class IllegalQueryResultAdapter extends RecyclerView.Adapter<IllegalQueryResultAdapter.ItemViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<IllegalQueryResultInfo> mIllegalQueryResultInfos;
    private HashMap<Integer, Boolean> isSelected;
    private boolean isEdit = false;
    private OnCheckQueryListener mOnCheckQueryListener;
    private final String STATUS_BE_DECIDED = "10";//待确定
    private final String STATUS_FAIL = "20";//未通过
    private final String STATUS_PENDING = "30";//待处理
    private final String STATUS_PROCESSED = "40";//已处理
    private final String STATUS_REFUND = "50";//已退款
    private boolean isDetail = false;
    private OnItemClickListener mOnItemClickListener;

    public interface OnCheckQueryListener {
        void checkQuery(int position);
    }

    public interface OnItemClickListener {
        void itemClick(int position);
    }

    public void setOnCheckQueryListener(OnCheckQueryListener l) {
        this.mOnCheckQueryListener = l;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    public ArrayList<IllegalQueryResultInfo> getmIllegalQueryResultInfos() {
        return mIllegalQueryResultInfos;
    }

    public IllegalQueryResultAdapter(Context context, ArrayList<IllegalQueryResultInfo> illegalQueryResultInfos, boolean isEr, boolean isDetail) {
        this.mContext = context;
        this.mIllegalQueryResultInfos = illegalQueryResultInfos;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.isSelected = new HashMap<Integer, Boolean>();
        this.isEdit = isEr;
        this.isDetail = isDetail;
        initData();
    }

    private void initData() {
        if (mIllegalQueryResultInfos != null) {
            for (int i = 0; i < mIllegalQueryResultInfos.size(); i++) {
                getIsSelected().put(i, true);
            }
        }
    }

    public void notifyData(ArrayList<IllegalQueryResultInfo> illegalQueryResultInfos, boolean isDetail) {
        this.mIllegalQueryResultInfos = illegalQueryResultInfos;
        this.isDetail = isDetail;
        initData();
        this.notifyDataSetChanged();
    }

    public void notifyState(boolean isEdit, boolean isDetail) {
        this.isEdit = isEdit;
        this.isDetail = isDetail;
        this.notifyDataSetChanged();
    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_illegal_query, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        /*if (position == mIllegalQueryResultInfos.size() - 1) {
            holder.mCutOff.setVisibility(View.GONE);
        }*/

        if (mIllegalQueryResultInfos.get(position)
                .wfjfs.equals("12")) {
            holder.mCheckBoxView.setVisibility(View.GONE);
        } else {
            if (isDetail) {
                holder.mCheckBoxView.setVisibility(View.GONE);
            } else {
                if (isEdit) {
                    holder.mCheckBoxView.setVisibility(View.VISIBLE);
                    holder.mCheckBoxView.setSelected(getIsSelected().get(position));
                } else {
                    holder.mCheckBoxView.setVisibility(View.GONE);
                }
            }
        }
        holder.mDateView.setText(mIllegalQueryResultInfos.get(position).date);
        holder.mLocationView.setText(mIllegalQueryResultInfos.get(position).area);
        holder.mResonView.setText(mIllegalQueryResultInfos.get(position).act);
        if (TextUtils.isEmpty(mIllegalQueryResultInfos.get(position).statusCode)) {
            holder.mStateView.setVisibility(View.GONE);
        } else {

//            private final String STATUS_FAIL = "20";//未通过
//            private final String STATUS_PENDING = "30";//待处理
//            private final String STATUS_PROCESSED = "40";//已处理
//            private final String STATUS_REFUND = "50";//已退款
            if (mIllegalQueryResultInfos.get(position).statusCode.equals(STATUS_FAIL)) {
                holder.mStateView.setVisibility(View.VISIBLE);
                holder.mStateView.setBackgroundResource(R.drawable.ic_fail);
            } else if (mIllegalQueryResultInfos.get(position).statusCode.equals(STATUS_PENDING)) {
                holder.mStateView.setVisibility(View.VISIBLE);
                holder.mStateView.setBackgroundResource(R.drawable.ic_pending);
            } else if (mIllegalQueryResultInfos.get(position).statusCode.equals(STATUS_PROCESSED)) {
                holder.mStateView.setVisibility(View.VISIBLE);
                holder.mStateView.setBackgroundResource(R.drawable.ic_dispose);
            } else if (mIllegalQueryResultInfos.get(position).statusCode.equals(STATUS_REFUND)) {
                holder.mStateView.setVisibility(View.VISIBLE);
                holder.mStateView.setBackgroundResource(R.drawable.ic_refund);
            }
        }

//        holder.mMoneyView.setText("￥" + mIllegalQueryResultInfos.get(position).money);
        if (TextUtils.isEmpty(mIllegalQueryResultInfos.get(position).money)) {
            holder.mMoneyView.setText(OtherUtil.amountFormat("0", true));
        } else {
            holder.mMoneyView.setText(OtherUtil.amountFormat(mIllegalQueryResultInfos.get(position).money, true));
        }
        holder.mPointView.setText(mIllegalQueryResultInfos.get(position).wfjfs);
        clickCheckListener(holder, position);

        clickListener(holder, position);
    }

    private void clickListener(ItemViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.itemClick(position);
                }
            }
        });
    }

    private void clickCheckListener(ItemViewHolder holder, final int position) {
        holder.mCheckBoxView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnCheckQueryListener != null) {
                    mOnCheckQueryListener.checkQuery(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mIllegalQueryResultInfos != null ? mIllegalQueryResultInfos.size() : 0;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mDateView;
        private TextView mLocationView;
        private TextView mResonView;
        private TextView mMoneyView;
        private TextView mPointView;
        private ImageView mCheckBoxView;
        private ImageView mStateView;
        private View mCutOff;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mDateView = (TextView) itemView.findViewById(R.id.illegal_query_result_date);
            mLocationView = (TextView) itemView.findViewById(R.id.illegal_query_result_location);
            mResonView = (TextView) itemView.findViewById(R.id.illegal_query_result_reason);
            mMoneyView = (TextView) itemView.findViewById(R.id.illegal_query_result_money);
            mPointView = (TextView) itemView.findViewById(R.id.illegal_query_result_point);
            mCheckBoxView = (ImageView) itemView.findViewById(R.id.illegal_query_result_selecotor);
            mStateView = (ImageView) itemView.findViewById(R.id.illegal_query_result_state);
            mCutOff = itemView.findViewById(R.id.illegal_query_cut_off);
        }
    }
}
