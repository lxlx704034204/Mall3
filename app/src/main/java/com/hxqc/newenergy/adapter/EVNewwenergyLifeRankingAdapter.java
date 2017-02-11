package com.hxqc.newenergy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import hxqc.mall.R;

/** 新能源汽车排行列表适配器
 * author: 何玉
 * since: 2016年3月9日
 * Copyright:恒信汽车电子商务有限公司
 */
public class EVNewwenergyLifeRankingAdapter extends BaseAdapter{

   private Context mContext;

    public EVNewwenergyLifeRankingAdapter() {
        super();
    }

   public EVNewwenergyLifeRankingAdapter(Context context){

       mContext=context;
    }

    public void setData(Context mContext) {

    }

    @Override
    public int getCount() {
        return 5;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolder=null;
        if (convertView==null){

            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_ev_newwenergyranking_listview_adapter,null);
            mViewHolder=new ViewHolder();
            convertView.setTag(mViewHolder);

        }else {

            mViewHolder=(ViewHolder) convertView.getTag();
        }


        return convertView;
    }

    class  ViewHolder{


    }

}
