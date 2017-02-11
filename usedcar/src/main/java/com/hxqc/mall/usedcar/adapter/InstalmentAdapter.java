package com.hxqc.mall.usedcar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.model.Instalment;

import java.util.ArrayList;

/**
 * 分期购车ListViewNoSlide adapter
 * Created by huangyi on 16/1/8.
 */
public class InstalmentAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Instalment> mInstalment;
    LayoutInflater mLayoutInflater;

    public InstalmentAdapter(Context mContext, ArrayList<Instalment> mInstalment) {
        this.mContext = mContext;
        this.mInstalment = mInstalment;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return null == mInstalment ? 0 : mInstalment.size();
    }

    @Override
    public Instalment getItem(int position) {
        return mInstalment.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_instalment, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mParentView = (LinearLayout) convertView.findViewById(R.id.instalment_parent);
            mViewHolder.mYearView = (TextView) convertView.findViewById(R.id.instalment_year);
            mViewHolder.mLimitView = (TextView) convertView.findViewById(R.id.instalment_limit);
            mViewHolder.mMonthView = (TextView) convertView.findViewById(R.id.instalment_month);
            mViewHolder.mStatusView = (ImageView) convertView.findViewById(R.id.instalment_status);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        Instalment model = mInstalment.get(position);

        mViewHolder.mYearView.setText(model.year + "年");
        mViewHolder.mLimitView.setText("贷款额度: " + model.limit + "元");
        mViewHolder.mMonthView.setText(" " + OtherUtil.amountFormat(model.per_month, false));
        if (model.isChecked) {
            mViewHolder.mParentView.setBackgroundColor(Color.parseColor("#f9fafb"));
            mViewHolder.mStatusView.setImageResource(R.drawable.ic_repair_record_sel);
        } else {
            mViewHolder.mParentView.setBackgroundColor(Color.parseColor("#ffffff"));
            mViewHolder.mStatusView.setImageResource(R.drawable.radiobutton_normal);
        }
        return convertView;
    }

    class ViewHolder {
        LinearLayout mParentView;
        TextView mYearView;
        TextView mLimitView;
        TextView mMonthView;
        ImageView mStatusView;
    }

}
