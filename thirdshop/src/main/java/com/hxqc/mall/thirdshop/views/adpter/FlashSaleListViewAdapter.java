package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hxqc.mall.thirdshop.model.SingleSeckillItem;
import com.hxqc.mall.thirdshop.views.FlashSaleItem;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-05-17
 * FIXME
 * Todo 特买车列表适配器，倒计时控制
 */
public class FlashSaleListViewAdapter extends BaseAdapter {
    //    private final Thread thread;
    private final Context context;
    private final ArrayList<SingleSeckillItem> mData;
    private final ArrayList<FlashSaleItem> items;

    private static final int TYPE_END = 2;
    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_NOT_START = 0;


    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder = null;
        if (convertView == null) {
            convertView = new FlashSaleItem(context);
//            holder = new ViewHolder();
//            convertView.setTag(holder);
        }
//        else holder = (ViewHolder) convertView.getTag();
//        holder.item = (FlashSaleItem) convertView;
//        holder.item.addData(mData.get(position));
        ((FlashSaleItem) convertView).addData(mData.get(position));
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {

        SingleSeckillItem singleSeckillItem = mData.get(position);
        if (singleSeckillItem.isStarted == 0)
            return TYPE_NOT_START;
        else if (singleSeckillItem.isEnded == 1)
            return TYPE_END;
        else return TYPE_NORMAL;
    }

    public FlashSaleListViewAdapter(Context context, ArrayList<SingleSeckillItem> mData) {
        this.context = context;
        this.mData = mData;
        this.items = new ArrayList<>();
    }

//    @Override
//    public FlashSaleListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        FlashSaleItem item = new FlashSaleItem(context);
//        items.add(item);
//        return new ViewHolder(item);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, final int position) {
//        holder.item.addData(mData.get(position));
//    }


//    @Override
//    public int getItemCount() {
//
//    }

//    public class ViewHolder {
//        private FlashSaleItem item;
//    }

    public void onDestroy() {
        for (FlashSaleItem item : items) {
            item.onDestroy();
        }
    }
}
