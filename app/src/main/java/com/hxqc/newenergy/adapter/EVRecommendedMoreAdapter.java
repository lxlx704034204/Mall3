package com.hxqc.newenergy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.newenergy.bean.EVNewenergyAutoSample;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 新能源爆款推荐列表适配器
 * Created by 何玉
 * on 2016/3/9.
 */
public class EVRecommendedMoreAdapter extends BaseAdapter {
    private Context mContext;

    private ArrayList<EVNewenergyAutoSample> mRecommendList;

    public EVRecommendedMoreAdapter() {
        super();
    }

    public EVRecommendedMoreAdapter(Context context) {

        mContext = context;
    }

    public void setData(ArrayList<EVNewenergyAutoSample> RecommendList) {
        mRecommendList = RecommendList;
    }


    @Override
    public int getCount() {
        return mRecommendList == null ? 0 : mRecommendList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_ev_newwenergyrecommended_listview_adapter, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mCarIco = (ImageView) convertView.findViewById(R.id.car_ico);
            mViewHolder.mCarName = (TextView) convertView.findViewById(R.id.car_name);
            mViewHolder.mCarPrice = (TextView) convertView.findViewById(R.id.car_price);
            mViewHolder.mTotalPrice = (TextView) convertView.findViewById(R.id.car_TotalPrice);
            mViewHolder.mBatteryLife = (TextView) convertView.findViewById(R.id.car_batteryLife);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        ImageUtil.setImage(mContext, mViewHolder.mCarIco, mRecommendList.get(position).itemThumb);
       mViewHolder.mCarName.setText(mRecommendList.get(position).itemName);
       mViewHolder.mCarPrice.setText(OtherUtil.amountFormat(mRecommendList.get(position).itemPrice,true));
       mViewHolder.mTotalPrice.setText(OtherUtil.amountFormat(mRecommendList.get(position).itemTotalPrice,true));
      mViewHolder.mBatteryLife.setText(((int) mRecommendList.get(position).batteryLife)+"公里");


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcher.toAutoItemDetail(mContext,"0",mRecommendList.get(position).itemID,"车辆详情");
            }
        });
        return convertView;
    }

    class ViewHolder {

        private ImageView mCarIco;
        private TextView mCarName;
        private TextView mCarPrice;
        private TextView mTotalPrice;
        private TextView mBatteryLife;
    }

}
