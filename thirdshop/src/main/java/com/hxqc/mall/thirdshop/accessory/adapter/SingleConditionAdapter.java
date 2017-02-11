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
import com.hxqc.mall.thirdshop.accessory.model.Condition;

import java.util.ArrayList;

/**
 * 商品详情 选择条件ListPopupWindow Adapter
 * Created by huangyi on 15/12/17.
 */
public class SingleConditionAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Condition> mConditions;
    LayoutInflater mLayoutInflater;

    public SingleConditionAdapter(Context mContext, ArrayList<Condition> mConditions) {
        this.mContext = mContext;
        this.mConditions = mConditions;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mConditions.size();
    }

    @Override
    public Condition getItem(int position) {
        return mConditions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_single_condition, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mColorView = (ColorSquare) convertView.findViewById(R.id.single_condition_color);
            mViewHolder.mValueView = (TextView) convertView.findViewById(R.id.single_condition_value);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        Condition model = mConditions.get(position);

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
        ColorSquare mColorView;
        TextView mValueView;
    }
}
