//package com.hxqc.mall.thirdshop.maintenance.adapter;
//
//import android.content.Context;
//import android.os.Build;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.hxqc.mall.thirdshop.R;
//import MaintenancePriceOrTitle;
//import MaintenanceShop;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//
///**
// * Function: 常规保养包含价格的Adapter
// *
// * @author 袁秉勇
// * @since 2016年02月19日
// */
//public class NormalMaintenancePriceAdapter extends RecyclerView.Adapter< NormalMaintenancePriceAdapter.Holder > {
//    private static final String TAG = NormalMaintenanceShopAdapter.class.getSimpleName();
//
//
//    public NormalMaintenancePriceAdapter(Context mContext) {
//        this.mContext = mContext;
//    }
//
//
//    private Context mContext;
//
//    public ArrayList< MaintenanceShop > list = new ArrayList<>();
//    private boolean showDistance = false;
//
//
//    public NormalMaintenancePriceAdapter() {
//    }
//
//
//    public void setShowDistance(boolean showDistance) {
//        this.showDistance = showDistance;
//    }
//
//
//    @Override
//    public Holder onCreateViewHolder(final ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_maintenance_price, parent, false);
//        return new Holder(view);
//    }
//
//
//    @Override
//    public void onBindViewHolder(Holder holder, int position) {
//        MaintenanceShop maintenanceShop = list.get(position);
//        Context context = holder.itemView.getContext();
//        if (maintenanceShop != null && context != null) {
//            Picasso.with(context).load(maintenanceShop.shopPhoto).placeholder(R.drawable.pic_normal).error(R.drawable.pic_normal).into(holder.shop_image);
//            Picasso.with(context).load(maintenanceShop.brandThumb).placeholder(R.drawable.pic_normal).error(R.drawable.pic_normal).into(holder.brands_image);
//            holder.shop_name.setText(maintenanceShop.shopTitle);
//            if ("0.0km".equals(maintenanceShop.distance) || "0km".equals(maintenanceShop.distance) || "0".equals(maintenanceShop.distance) || !showDistance) {
//                holder.shop_distance.setVisibility(View.GONE);
//            } else {
//                holder.shop_distance.setVisibility(View.VISIBLE);
//                holder.shop_distance.setText(maintenanceShop.distance + "");
//            }
//
//            holder.shop_content_1.setText(maintenanceShop.promotionTitle);
//            ArrayList< MaintenancePriceOrTitle > maintenancePriceOrTitles = maintenanceShop.maintenanceList;
//
//            if (maintenancePriceOrTitles != null) {
//                try {
//                    holder.shop_content_2.setText(maintenancePriceOrTitles.get(0).price + "");
//                } catch (IndexOutOfBoundsException e) {
//                    holder.shop_content_2.setText("");
//                }
//                try {
//                    holder.shop_content_3.setText(maintenancePriceOrTitles.get(1).price + "");
//                } catch (IndexOutOfBoundsException e) {
//                    holder.shop_content_3.setText("");
//                }
//            }
//        }
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return list.size();
//    }
//
//
//    public void addData(ArrayList< MaintenanceShop > itemList) {
//        if (list != null) {
//            list.addAll(itemList);
//        } else {
//            list = itemList;
//        }
//        notifyItemRangeInserted(getItemCount(), itemList.size());
//
//
//    }
//
//
//    public void replaceData(ArrayList< MaintenanceShop > itemList) {
//        if (list == null || list.isEmpty()) {
//            list = itemList;
//            notifyDataSetChanged();
//        } else {
//
////            notifyItemRangeChanged(0, itemList.size());
////            notifyDataSetChanged();
//            if (list.size() > itemList.size()) {
//                notifyItemRangeRemoved(itemList.size(), getItemCount() - itemList.size());
//                list.clear();
//                list.addAll(itemList);
//                notifyItemRangeChanged(0, itemList.size());
//
//
////                notifyItemRangeChanged(0,itemList.size());
//            } else {
//                list.clear();
//                list.addAll(itemList);
//                notifyItemRangeChanged(0, itemList.size());
//
//            }
//        }
//
//    }
//
//
//    public void clearData() {
//        if (list != null) {
//            if (!list.isEmpty()) {
//                notifyItemRangeRemoved(0, getItemCount());
//                list.clear();
//            }
//        }
//    }
//
//
//    public MaintenanceShop getItemData(int position) {
//
//        if (list != null) {
//            return list.get(position);
//        } else {
//            return null;
//        }
//    }
//
//
//    public static class Holder extends RecyclerView.ViewHolder {
//
//        public ImageView shop_image;
//        public ImageView brands_image;
//        public TextView shop_name;
//        public TextView shop_distance;
//        public TextView shop_content_1;
//        public TextView shop_content_2;
//        public TextView shop_content_3;
//
//
//        public Holder(View itemView) {
//            super(itemView);
//            if (Build.VERSION.SDK_INT < 21) {
//                itemView.setBackgroundResource(R.drawable.t_bg_item);
//            }
//            shop_image = (ImageView) itemView.findViewById(R.id.shop_image);
//            brands_image = (ImageView) itemView.findViewById(R.id.brands_image);
//            shop_name = (TextView) itemView.findViewById(R.id.shop_name);
//            shop_distance = (TextView) itemView.findViewById(R.id.shop_distance);
//            shop_content_1 = (TextView) itemView.findViewById(R.id.shop_content_1);
//            shop_content_2 = (TextView) itemView.findViewById(R.id.shop_content_2);
//            shop_content_3 = (TextView) itemView.findViewById(R.id.shop_content_3);
//        }
//    }
//}
