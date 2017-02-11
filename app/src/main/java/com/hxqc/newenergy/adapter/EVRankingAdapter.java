package com.hxqc.newenergy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.newenergy.bean.EVNewenergyAutoSample;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 说明:
 * author: 何玉
 * since: 2016/3/14.
 * Copyright:恒信汽车电子商务有限公司
 */
public class EVRankingAdapter extends BaseAdapter {

    Context mContext;

    String Title;

    ArrayList<EVNewenergyAutoSample>mAutoSample_List;
   public EVRankingAdapter(Context context){
        mContext=context;
    }

    @Override
    public int getCount() {
        return mAutoSample_List.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void setData( ArrayList<EVNewenergyAutoSample> AutoSample_List,String title){
        mAutoSample_List=AutoSample_List;
        this.Title=title;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        viewHolder mViewHolder;
        if (convertView==null){
            convertView=LayoutInflater.from(mContext).inflate(R.layout.item_ev_newwenergyranking_listview_adapter,null);
            mViewHolder=new viewHolder();

            mViewHolder.mCar_img=(ImageView) convertView.findViewById(R.id.car_img);
            mViewHolder.mContentcar_img=(ImageView) convertView.findViewById(R.id.content_ico);
            mViewHolder.mContentcar_number_text=(TextView) convertView.findViewById(R.id.content_number_text);
            mViewHolder.mContentcar_title_text=(TextView) convertView.findViewById(R.id.content_title_text);
            mViewHolder.mContentcar_price_text=(TextView) convertView.findViewById(R.id.content_price);
            mViewHolder.mType_text=(TextView) convertView.findViewById(R.id.type_text);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder=(viewHolder) convertView.getTag();
        }


        if (position==0){
            ImageUtil.setImage(mContext, mViewHolder.mCar_img, mAutoSample_List.get(position).itemThumb);
            mViewHolder.mCar_img.setVisibility(View.VISIBLE);
            mViewHolder.mContentcar_img.setVisibility(View.GONE);
            mViewHolder.mContentcar_number_text.setText(String.valueOf(position+1));
            mViewHolder.mContentcar_number_text.setBackgroundResource(R.drawable.red_shape);
            mViewHolder.mContentcar_title_text.setText(mAutoSample_List.get(position).itemName);


        }else {
            mViewHolder.mCar_img.setVisibility(View.GONE);
            ImageUtil.setImage(mContext, mViewHolder.mContentcar_img, mAutoSample_List.get(position).itemThumb);
            mViewHolder.mContentcar_img.setVisibility(View.VISIBLE);
            mViewHolder.mContentcar_number_text.setText(String.valueOf(position+1));
            mViewHolder.mContentcar_number_text.setBackgroundResource(R.drawable.grey_shape);
            mViewHolder.mContentcar_title_text.setText(mAutoSample_List.get(position).itemName);
//            mViewHolder.mContentcar_price_text.setText(OtherUtil.amountFormat(mAutoSample_List.get(position).itemPrice,true));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcher.toAutoItemDetail(mContext, AutoItem.AUTO_COMMON,mAutoSample_List.get(position).itemID,"车辆详情");
            }
        });

        if (Title.equals("用户关注")){
            mViewHolder.mType_text.setText("特价:");
            mViewHolder.mContentcar_price_text.setText(OtherUtil.amountFormat(mAutoSample_List.get(position).itemPrice,true));
        }
        if (Title.equals("纯电续航")){
            mViewHolder.mType_text.setText("续航:");
            mViewHolder.mContentcar_price_text.setText(mAutoSample_List.get(position).batteryLife+"公里");
        }
        if (Title.equals("购车补贴")){
            mViewHolder.mType_text.setText("补贴:");
            mViewHolder.mContentcar_price_text.setText(OtherUtil.amountFormat(mAutoSample_List.get(position).subsidyTotal,true));
        }

        return convertView;
    }

    class  viewHolder{

        private ImageView mCar_img;


        private ImageView mContentcar_img;
        private TextView mContentcar_number_text;
        private TextView mContentcar_title_text;
        private TextView mContentcar_price_text;
        private TextView mType_text;

    }
}
