package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.thirdshop.model.SingleSeckillItem;
import com.hxqc.mall.thirdshop.views.FlashSaleItem;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-05-17
 * FIXME
 * Todo 特买车列表适配器，倒计时控制
 */
public class FlashSaleAdapter extends RecyclerView.Adapter<FlashSaleAdapter.ViewHolder> {
    //    private final Thread thread;
    private final Context context;
    private final ArrayList<SingleSeckillItem> mData;
    private final ArrayList<FlashSaleItem> items;

    private static final int TYPE_END = 2;
    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_NOT_START = 0;


    @Override
    public int getItemViewType(int position) {
        SingleSeckillItem singleSeckillItem = mData.get(position);
        if (singleSeckillItem.isStarted == 0)
            return TYPE_NOT_START;
        else if (singleSeckillItem.isEnded == 1)
            return TYPE_END;
        else return TYPE_NORMAL;
    }

    public FlashSaleAdapter(Context context, ArrayList<SingleSeckillItem> mData) {
        this.context = context;
        this.mData = mData;
        this.items = new ArrayList<>();
    }

    @Override
    public FlashSaleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FlashSaleItem item = new FlashSaleItem(context);
        items.add(item);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);//暂时不复用，解决倒计时混乱问题
        holder.item.addData(mData.get(position));
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final FlashSaleItem item;

        public ViewHolder(View itemView) {
            super(itemView);
            item = (FlashSaleItem) itemView;
        }
    }

    public void onDestroy() {
        for (FlashSaleItem item : items) {
            item.onDestroy();
        }
    }

}
