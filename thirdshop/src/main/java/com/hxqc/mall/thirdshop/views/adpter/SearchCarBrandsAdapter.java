package com.hxqc.mall.thirdshop.views.adpter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.ShopSearchAuto;

import java.util.ArrayList;

/**
 * Author :liukechong
 * Date : 2015-12-02
 * FIXME
 * Todo
 */
public class SearchCarBrandsAdapter extends RecyclerView.Adapter<SearchCarBrandsAdapter.Holder> {
    private static final String TAG = "SearchCarBrandsAdapter";
    public ArrayList<ShopSearchAuto > list = new ArrayList<>();
//    DecimalFormat decimalFormat1 = new DecimalFormat("¥0.00万");
//    DecimalFormat decimalFormat2 = new DecimalFormat("¥#,##0.00");

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.t_item_search_car_brands, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ShopSearchAuto item = list.get(position);
        if (item != null) {
            holder.car_title.setText(item.autoInfo.itemName);

            try {
                float price1 = Float.parseFloat(item.autoInfo.itemPrice);
//                holder.car_content_1.setText(price1 > 10000f ? decimalFormat1.format(price1 / 10000f) : decimalFormat2.format(price1));
                holder.car_content_1.setText( OtherUtil.amountFormat(price1, true));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            try {
                float price2 = Float.parseFloat(item.autoInfo.itemOrigPrice);
                holder.car_content_4.setText( OtherUtil.amountFormat(price2, true));


//                holder.car_content_4.setText(price2 > 10000f ? decimalFormat1.format(price2 / 10000f) : decimalFormat2.format(price2));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            try {
                float priceDep = Float.parseFloat(item.autoInfo.getItemFall());
//                holder.car_content_2.setText(Math.abs(priceDep) > 10000f ? decimalFormat1.format(priceDep / 10000f) : decimalFormat2.format(priceDep));
                holder.car_content_2.setText( OtherUtil.amountFormat(priceDep, false));

            } catch (NumberFormatException e) {

                e.printStackTrace();
            }


            holder.car_content_4.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.car_content_3.setText(item.autoInfo.decrease);
            holder.car_content_5.setText(item.shopInfo.shopTitle);
        }

    }

    public ShopSearchAuto getItemData(int position) {
        if (list != null) {
            return list.get(position);
        } else {
            return null;
        }
    }

    public void addDate(ArrayList<ShopSearchAuto> itemList) {
        if (list != null) {
            list.addAll(itemList);
        } else {
            list = itemList;
        }
        notifyItemRangeInserted(getItemCount(), itemList.size());
    }

    public void replaceDate(ArrayList<ShopSearchAuto> itemList) {
        if (list == null || list.isEmpty()) {
            list = itemList;
            notifyDataSetChanged();
        } else {

//            notifyItemRangeChanged(0, itemList.size());
//            notifyDataSetChanged();
            if (list.size() > itemList.size()) {
                notifyItemRangeRemoved(itemList.size(), getItemCount() - itemList.size());
                list.clear();
                list.addAll(itemList);
                notifyItemRangeChanged(0, itemList.size());




//                notifyItemRangeChanged(0,itemList.size());
            } else {
                list.clear();
                list.addAll(itemList);
                notifyItemRangeChanged(0, itemList.size());

            }
        }
    }

    public void clearData() {
        if (list != null) {
            if (!list.isEmpty()){
                notifyItemRangeRemoved(0, getItemCount());
                list.clear();
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        public TextView car_title;
        public TextView car_content_1;
        public TextView car_content_2;
        public TextView car_content_3;
        public TextView car_content_4;
        public TextView car_content_5;
        public TextView car_dial;
        public TextView ask_price;

        public Holder(View itemView) {
            super(itemView);

            car_title = (TextView) itemView.findViewById(R.id.car_title);
            car_content_1 = (TextView) itemView.findViewById(R.id.car_content_1);
            car_content_2 = (TextView) itemView.findViewById(R.id.car_content_2);
            car_content_3 = (TextView) itemView.findViewById(R.id.car_content_3);
            car_content_4 = (TextView) itemView.findViewById(R.id.car_content_4);
            car_content_5 = (TextView) itemView.findViewById(R.id.car_content_5);
            car_dial = (TextView) itemView.findViewById(R.id.car_dial);
            ask_price = (TextView) itemView.findViewById(R.id.ask_price);

        }
    }
}
