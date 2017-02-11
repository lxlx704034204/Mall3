package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.ShopSearchShop;
import com.hxqc.mall.thirdshop.model.promotion.SalesPModel;

import java.util.ArrayList;

/**
 * Author :liukechong
 * Date : 2015-12-02
 * FIXME
 * Todo
 */
public class SearchShopAdapter extends RecyclerView.Adapter<SearchShopAdapter.Holder> {
    private static final String TAG = "SearchShopAdapter";
    public ArrayList<ShopSearchShop> list = new ArrayList<>();
    private boolean showDistance = false;

    public void setShowDistance(boolean showDistance) {
        this.showDistance = showDistance;
    }

    @Override
    public Holder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.t_item_search_shop, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ShopSearchShop shopSearchShop = list.get(position);
        Context context = holder.itemView.getContext();
        if (shopSearchShop != null && context != null) {
            ImageUtil.setImage(context, holder.shop_image, shopSearchShop.shopPhoto);
            ImageUtil.setImageSmall(context, holder.brands_image, shopSearchShop.brandThumb);

            holder.shop_name.setText(shopSearchShop.shopTitle);
            if (TextUtils.isEmpty(shopSearchShop.distance) || !showDistance) {
                holder.shop_distance.setVisibility(View.GONE);
            } else {
                holder.shop_distance.setVisibility(View.VISIBLE);
                holder.shop_distance.setText(OtherUtil.reformatDistance(shopSearchShop.distance));
            }
//            holder.shop_content_1.setVisibility(View.GONE);
//            holder.shop_content_2.setVisibility(View.GONE);
//            holder.shop_content_3.setVisibility(View.GONE);
            ArrayList<SalesPModel> promotionList = shopSearchShop.promotionList;

            if (promotionList != null) {
//                for (int i = 0; i < promotionList.size(); i++) {
//                    if (i == 0) {
////                        holder.shop_content_1.setVisibility(View.VISIBLE);
//                        holder.shop_content_1.setText(promotionList.get(i).title);
//                    } else if (i == 1) {
////                        holder.shop_content_2.setVisibility(View.VISIBLE);
//                        holder.shop_content_2.setText(promotionList.get(i).title);
//                    } else if (i == 2) {
////                        holder.shop_content_3.setVisibility(View.VISIBLE);
//                        holder.shop_content_3.setText(promotionList.get(i).title);
//                    }
//                }
                try {
                    holder.shop_content_1.setText(promotionList.get(0).title);
                } catch (IndexOutOfBoundsException e) {
                    holder.shop_content_1.setText("");
                }
                try {
                    holder.shop_content_2.setText(promotionList.get(1).title);
                } catch (IndexOutOfBoundsException e) {
                    holder.shop_content_2.setText("");
                }
                try {
                    holder.shop_content_3.setText(promotionList.get(2).title);
                } catch (IndexOutOfBoundsException e) {
                    holder.shop_content_3.setText("");
                }
            }
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addDate(ArrayList<ShopSearchShop> itemList) {
        if (list != null) {
            list.addAll(itemList);
        } else {
            list = itemList;
        }
        notifyItemRangeInserted(getItemCount(), itemList.size());


    }

    public void replaceDate(ArrayList<ShopSearchShop> itemList) {
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

    public ShopSearchShop getItemData(int position) {

        if (list != null) {
            return list.get(position);
        } else {
            return null;
        }
    }


    public static class Holder extends RecyclerView.ViewHolder {

        public ImageView shop_image;
        public ImageView brands_image;
        public TextView shop_name;
        public TextView shop_distance;
        public TextView shop_content_1;
        public TextView shop_content_2;
        public TextView shop_content_3;

        public Holder(View itemView) {
            super(itemView);
            if (Build.VERSION.SDK_INT < 21) {
                itemView.setBackgroundResource(R.drawable.t_bg_item);
            }
            shop_image = (ImageView) itemView.findViewById(R.id.shop_image);
            brands_image = (ImageView) itemView.findViewById(R.id.brands_image);
            shop_name = (TextView) itemView.findViewById(R.id.shop_name);
            shop_distance = (TextView) itemView.findViewById(R.id.shop_distance);
            shop_content_1 = (TextView) itemView.findViewById(R.id.shop_content_1);
            shop_content_2 = (TextView) itemView.findViewById(R.id.shop_content_2);
            shop_content_3 = (TextView) itemView.findViewById(R.id.shop_content_3);


        }
    }
}
