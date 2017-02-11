package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.model.CancelReason;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 说明:取消订单理由
 *
 * author: 吕飞
 * since: 2015-04-15
 * Copyright:恒信汽车电子商务有限公司
 */
public class CancelReasonAdapter extends BaseAdapter {
    ArrayList< CancelReason > mCancelReasons;
    Context mContext;
    LayoutInflater mInflater;

    public CancelReasonAdapter(ArrayList< CancelReason > mCancelReasons, Context mContext) {
        mInflater = LayoutInflater.from(mContext);
        this.mCancelReasons = mCancelReasons;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mCancelReasons.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CancelReasonHolder mCancelReasonHolder;
        CancelReason mCancelReason = getItem(position);
        if (convertView == null) {
            mCancelReasonHolder = new CancelReasonHolder();
            convertView = mInflater.inflate(R.layout.item_cancel_reason, parent, false);
            mCancelReasonHolder.mReasonLabelView = (ImageView) convertView.findViewById(R.id.reason_label);
            mCancelReasonHolder.mLabelTextView= (TextView) convertView.findViewById(R.id.label_text);
            convertView.setTag(mCancelReasonHolder);
        } else {
            mCancelReasonHolder = (CancelReasonHolder) convertView.getTag();
        }
        mCancelReasonHolder.mLabelTextView.setText(mCancelReason.reasonContent);
        if (mCancelReason.isChosen) {
            mCancelReasonHolder.mReasonLabelView.setImageResource(R.drawable.checkbox_selected);
        } else {
            mCancelReasonHolder.mReasonLabelView.setImageResource(R.drawable.checkbox_normal);
        }
        return convertView;
    }

    @Override
    public CancelReason getItem(int position) {
        return mCancelReasons.get(position);
    }

    public class CancelReasonHolder {
        ImageView mReasonLabelView;
        TextView mLabelTextView;
    }
}
