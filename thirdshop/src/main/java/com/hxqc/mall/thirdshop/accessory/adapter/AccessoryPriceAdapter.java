package com.hxqc.mall.thirdshop.accessory.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.model.AccessoryInfo;

import java.util.ArrayList;

/**
 * Function: 用品销售列表Adapter（用品首页列表）
 *
 * @author 袁秉勇
 * @since 2016年02月22日
 */
public class AccessoryPriceAdapter extends RecyclerView.Adapter< AccessoryPriceAdapter.Holder > {
    private final static String TAG = AccessoryPriceAdapter.class.getSimpleName();
    public ArrayList< AccessoryInfo > list = new ArrayList<>();
    private Context mContext;
    private boolean showDistance = false;


    public void setShowDistance(boolean showDistance) {
        this.showDistance = showDistance;
    }


    @Override
    public Holder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accessory_sale, parent, false);
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(Holder holder, int position) {
        AccessoryInfo accessoryInfo = list.get(position);
        Context context = holder.itemView.getContext();
        if (accessoryInfo != null && context != null) {
            ImageUtil.setImageSquare(context, holder.accessory_img, accessoryInfo.productSeries.smallPhoto);
            holder.accessory_name.setText(accessoryInfo.productSeries.name);
            holder.accessory_price.setText(formatPriceString(accessoryInfo.productSeries.priceRange));
        }
    }


    private String formatPriceString(String price) {
        if (TextUtils.isEmpty(price)) return "价格保密，进店有惊喜哦";
        if (!price.startsWith("￥")) price = "￥" + price;

        String[] prices = price.split("-");
        if (prices.length > 1) {
            return prices[0] + " ~ " + prices[1];
        } else {
            return prices[0];
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public void addData(ArrayList< AccessoryInfo > itemList) {
        if (list != null) {
            list.addAll(itemList);
        } else {
            list = itemList;
        }
        notifyItemRangeInserted(getItemCount(), itemList.size());
    }


    public void replaceDate(ArrayList< AccessoryInfo > itemList) {
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
            if (!list.isEmpty()) {
                notifyItemRangeRemoved(0, getItemCount());
                list.clear();
            }
        }
    }


    public AccessoryInfo getItemData(int position) {
        if (list != null) {
            return list.get(position);
        } else {
            return null;
        }
    }


    public static class Holder extends RecyclerView.ViewHolder {
        public LinearLayout contentView;
        public ImageView accessory_img;
        public TextView accessory_name;
        public TextView accessory_price;


        public Holder(View itemView) {
            super(itemView);
            contentView = (LinearLayout) itemView.findViewById(R.id.content);
            accessory_img = (ImageView) itemView.findViewById(R.id.accessory_img);
            accessory_name = (TextView) itemView.findViewById(R.id.accessory_name);
            accessory_price = (TextView) itemView.findViewById(R.id.accessory_price);
        }
    }
}
