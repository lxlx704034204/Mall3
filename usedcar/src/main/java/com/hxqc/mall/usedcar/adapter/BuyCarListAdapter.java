package com.hxqc.mall.usedcar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.model.UsedCarBase;
import com.hxqc.mall.usedcar.utils.OtherUtil;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * 买车 车辆列表 ListView Adapter
 * Created by huangyi on 15/10/21.
 */
public class BuyCarListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<UsedCarBase> mUsedCarBase;
    LayoutInflater mLayoutInflater;

    public BuyCarListAdapter(Context mContext, ArrayList<UsedCarBase> mUsedCarBase) {
        this.mContext = mContext;
        this.mUsedCarBase = mUsedCarBase;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return null == mUsedCarBase ? 0 : mUsedCarBase.size();
    }

    @Override
    public UsedCarBase getItem(int position) {
        return mUsedCarBase.get(position);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_buy_car_list, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mPhotoView = (ImageView) convertView.findViewById(R.id.buy_car_list_photo);
            mViewHolder.mSoldFlagView = (ImageView) convertView.findViewById(R.id.buy_car_list_sold_flag);
            mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.buy_car_list_title);
            mViewHolder.mDateView = (TextView) convertView.findViewById(R.id.buy_car_list_date);
            mViewHolder.mFromFlagView = (ImageView) convertView.findViewById(R.id.buy_car_list_from_flag);
            mViewHolder.mFromView = (TextView) convertView.findViewById(R.id.buy_car_list_from);
            mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.buy_car_list_price);
            mViewHolder.mSubmitTimeView = (TextView) convertView.findViewById(R.id.buy_car_list_submit_time);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        UsedCarBase model = mUsedCarBase.get(position);

        ImageUtil.setImageSquare(mContext, mViewHolder.mPhotoView, model.small_path);
        if (!TextUtils.isEmpty(model.car_on_sale) && "3".equals(model.car_on_sale.trim())) {
            mViewHolder.mSoldFlagView.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.mSoldFlagView.setVisibility(View.GONE);
        }
        mViewHolder.mTitleView.setText(model.car_name);
        mViewHolder.mDateView.setText(model.getDateAndRange());
        if (model.isPersonal()) {
            mViewHolder.mFromFlagView.setImageResource(R.mipmap.ic_autobuyindividual);
            mViewHolder.mFromView.setText(mContext.getResources().getString(R.string.personal));
        } else {
            mViewHolder.mFromFlagView.setImageResource(R.mipmap.ic_autobuyplatform);
            mViewHolder.mFromView.setText(mContext.getResources().getString(R.string.authentication));
        }
        mViewHolder.mPriceView.setText(model.getItemPrice());
        try {
            mViewHolder.mSubmitTimeView.setText(OtherUtil.getTime(model.publish_time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    class ViewHolder {
        ImageView mPhotoView;
        ImageView mSoldFlagView;
        TextView mTitleView;
        TextView mDateView;
        ImageView mFromFlagView;
        TextView mFromView;
        TextView mPriceView;
        TextView mSubmitTimeView;
    }
}
