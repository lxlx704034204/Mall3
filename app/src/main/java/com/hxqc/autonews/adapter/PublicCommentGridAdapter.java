package com.hxqc.autonews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.autonews.model.Grade;
import com.hxqc.autonews.model.UserGradeComment;

import hxqc.mall.R;

/**
 * 口碑评价 GridView Adapter
 * Created by huangyi on 15/10/21.
 */
public class PublicCommentGridAdapter extends BaseAdapter {

    Context mContext;
    Grade mGrade;
    LayoutInflater mLayoutInflater;

    public PublicCommentGridAdapter(Context mContext, Grade mGrade) {
        this.mContext = mContext;
        this.mGrade = mGrade;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public UserGradeComment getItem(int position) {
        return null;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_public_comment_grid, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mNameView = (TextView) convertView.findViewById(R.id.grid_name);
            mViewHolder.mValueView = (TextView) convertView.findViewById(R.id.grid_value);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        switch (position) {
            case 0:
                mViewHolder.mNameView.setText("空间");
                mViewHolder.mValueView.setText(mGrade.space + "分");
                break;

            case 1:
                mViewHolder.mNameView.setText("动力");
                mViewHolder.mValueView.setText(mGrade.power + "分");
                break;

            case 2:
                mViewHolder.mNameView.setText("油耗");
                mViewHolder.mValueView.setText(mGrade.fuelConsumption + "分");
                break;

            case 3:
                mViewHolder.mNameView.setText("舒适性");
                mViewHolder.mValueView.setText(mGrade.comfort + "分");
                break;

            case 4:
                mViewHolder.mNameView.setText("外观");
                mViewHolder.mValueView.setText(mGrade.appearance + "分");
                break;

            case 5:
                mViewHolder.mNameView.setText("内饰");
                mViewHolder.mValueView.setText(mGrade.interiorTrimming + "分");
                break;
        }

        return convertView;
    }

    class ViewHolder {
        TextView mNameView, mValueView;
    }
}
