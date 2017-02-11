package com.hxqc.mall.thirdshop.accessory.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.mall.core.views.ColorSquare;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.model.ChoseCondition;

import java.util.ArrayList;

/**
 * 商品详情 条件ListViewNoSlide Adapter
 * Created by huangyi on 15/12/17.
 */
public class ConditionListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<ChoseCondition> mChoseCondition; //已选条件
    LayoutInflater mLayoutInflater;

    public ConditionListAdapter(Context mContext, ArrayList<ChoseCondition> mChoseCondition) {
        this.mContext = mContext;
        this.mChoseCondition = mChoseCondition;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mChoseCondition.size();
    }

    @Override
    public ChoseCondition getItem(int position) {
        return mChoseCondition.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_condition_list, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mNameView = (TextView) convertView.findViewById(R.id.condition_list_name);
            mViewHolder.mColorView = (ColorSquare) convertView.findViewById(R.id.condition_list_color);
            mViewHolder.mValueView = (TextView) convertView.findViewById(R.id.condition_list_value);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        ChoseCondition model = mChoseCondition.get(position);

        mViewHolder.mNameView.setText(model.conditionName);
        mViewHolder.mValueView.setText(model.conditionValue);
        if (TextUtils.isEmpty(model.color)) {
            mViewHolder.mColorView.setVisibility(View.GONE);
        } else {
            mViewHolder.mColorView.setVisibility(View.VISIBLE);
            mViewHolder.mColorView.setColors(model.color.split(","));
        }

        return convertView;
    }

    class ViewHolder {
        TextView mNameView;
        ColorSquare mColorView;
        TextView mValueView;
    }
}
