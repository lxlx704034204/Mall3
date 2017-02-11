package com.hxqc.mall.usedcar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.model.MyCollection;
import com.hxqc.mall.usedcar.utils.OtherUtil;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * 说明：卖家车辆列表
 *
 * @author 吕飞
 * @since 2015年10月19日
 */
public class SellerCarListAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<MyCollection> mMyCollections;
    LayoutInflater mLayoutInflater;

    public SellerCarListAdapter(Context context, ArrayList<MyCollection> mMyCollections) {
        this.mContext = context;
        this.mMyCollections = mMyCollections;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mMyCollections.size();
    }

    @Override
    public MyCollection getItem(int position) {
        return mMyCollections.get(position);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_seller_car_list, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mTitleView = (TextView) convertView.findViewById(R.id.car_title);
            mViewHolder.mThumbnailView = (ImageView) convertView.findViewById(R.id.car_thumbnail);
            mViewHolder.mDateRangeView = (TextView) convertView.findViewById(R.id.date_range);
            mViewHolder.mPriceView = (TextView) convertView.findViewById(R.id.car_price);
            mViewHolder.mBelongsTextView = (TextView) convertView.findViewById(R.id.car_belongs);
            mViewHolder.mBelongsImageView = (ImageView) convertView.findViewById(R.id.car_belongs_view);
            mViewHolder.mSubmitTimeView = (TextView) convertView.findViewById(R.id.car_submittime);
            mViewHolder.mStatusView = (ImageView) convertView.findViewById(R.id.status);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        MyCollection mMyCollection = getItem(position);
        com.hxqc.mall.core.util.ImageUtil.setImage(mContext, mViewHolder.mThumbnailView, mMyCollection.path);
        mMyCollection.showState(mViewHolder.mStatusView);
        mViewHolder.mTitleView.setText(mMyCollection.car_name);
        mViewHolder.mDateRangeView.setText(mMyCollection.getDateAndRange());
        mViewHolder.mPriceView.setText(mMyCollection.getItemPrice());

        if (mMyCollection.isPersonal()) {
            mViewHolder.mBelongsImageView.setImageResource(R.mipmap.ic_autobuyindividual);
            mViewHolder.mBelongsTextView.setText(mContext.getResources().getString(R.string.personal));
        } else if (mMyCollection.isPlatform()) {
            mViewHolder.mBelongsImageView.setImageResource(R.mipmap.ic_autobuyplatform);
            mViewHolder.mBelongsTextView.setText(mContext.getResources().getString(R.string.authentication));
        }

        try {
            mViewHolder.mSubmitTimeView.setText(OtherUtil.getTime(mMyCollection.publish_time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    class ViewHolder {
        ImageView mThumbnailView;
        TextView mTitleView;
        TextView mDateRangeView;
        TextView mPriceView;
        TextView mBelongsTextView;
        ImageView mBelongsImageView;
        TextView mSubmitTimeView;
        ImageView mStatusView;
    }
}
