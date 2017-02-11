package com.hxqc.mall.thirdshop.accessory.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.model.AccessoryShop;

import java.util.ArrayList;

/**
 * Function: 用品价格Adapter
 *
 * @author 袁秉勇
 * @since 2016年02月25日
 */
public class AccessoryShopAdapter extends RecyclerView.Adapter< AccessoryShopAdapter.Holder > {
    private final static String TAG = AccessoryPriceAdapter.class.getSimpleName();
    public ArrayList< AccessoryShop > list = new ArrayList<>();
    private Context mContext;
    private boolean showDistance = false;


    public AccessoryShopAdapter(Context mContext) {
        this.mContext = mContext;
    }


    public void setShowDistance(boolean showDistance) {
        this.showDistance = showDistance;
    }


    @Override
    public Holder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accessory_price, parent, false);
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final AccessoryShop accessoryShop = list.get(position);
        Context context = holder.itemView.getContext();
        if (accessoryShop != null && context != null) {
            ImageUtil.setImage(context, holder.shop_image, accessoryShop.shopPhoto);
            ImageUtil.setImage(context, holder.brand_image, accessoryShop.brandThumb, R.drawable.pic_normal_logo);
            holder.shop_name.setText(accessoryShop.shopTitle);

//            holder.shop_distance.setText(accessoryShop.distance);

            if (TextUtils.isEmpty(accessoryShop.distance) || "0km".equals(accessoryShop.distance + "") || "0".equals(accessoryShop.distance + "") || !showDistance) {
                holder.shop_distance.setVisibility(View.GONE);
            } else {
                holder.shop_distance.setVisibility(View.VISIBLE);
                holder.shop_distance.setText(OtherUtil.reformatDistance(accessoryShop.distance));
            }

            holder.accessory_price.setText(formatPriceString(accessoryShop.priceRange));
        }
    }


    private String judgeDistance(String distance) {
        if (TextUtils.isEmpty(distance)) {
            return "0.1km";
        } else if (Float.valueOf(distance) < 0.1) {
            return "0.1km";
        } else return distance + "km";
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


    public void addData(ArrayList< AccessoryShop > itemList) {
        if (list != null) {
            list.addAll(itemList);
        } else {
            list = itemList;
        }
        notifyItemRangeInserted(getItemCount(), itemList.size());
    }


    public void replaceDate(ArrayList< AccessoryShop > itemList) {
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


    public AccessoryShop getItemData(int position) {
        if (list != null) {
            return list.get(position);
        } else {
            return null;
        }
    }


    public static class Holder extends RecyclerView.ViewHolder {
        public LinearLayout contentView;
        public ImageView shop_image;
        public ImageView brand_image;
        public TextView shop_name;
        public TextView shop_distance;
        public TextView accessory_price;
        public Button buyView;
        public Button callView;


        public Holder(View itemView) {
            super(itemView);
            contentView = (LinearLayout) itemView.findViewById(R.id.content);
            shop_image = (ImageView) itemView.findViewById(R.id.shop_image);
            brand_image = (ImageView) itemView.findViewById(R.id.brands_image);
            shop_name = (TextView) itemView.findViewById(R.id.shop_name);
            shop_distance = (TextView) itemView.findViewById(R.id.shop_distance);
            accessory_price = (TextView) itemView.findViewById(R.id.accessory_price);
            buyView = (Button) itemView.findViewById(R.id.buy_button);
            callView = (Button) itemView.findViewById(R.id.call_button);
        }
    }
}
