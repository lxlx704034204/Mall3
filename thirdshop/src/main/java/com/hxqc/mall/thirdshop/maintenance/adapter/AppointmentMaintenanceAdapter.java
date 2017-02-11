package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.model.MaintenancePriceOrTitle;
import com.hxqc.mall.thirdshop.maintenance.model.MaintenanceShop;

import java.util.ArrayList;

/**
 * Function: 预约维修列表Adapter
 *
 * @author 袁秉勇
 * @since 2016年02月19日
 */
public class AppointmentMaintenanceAdapter extends RecyclerView.Adapter<AppointmentMaintenanceAdapter.Holder> {
    private static final String TAG = NormalMaintenanceShopAdapter.class.getSimpleName();

    public ArrayList<MaintenanceShop> list = new ArrayList<>();
    private boolean showDistance = false;


    public void setShowDistance(boolean showDistance) {
        this.showDistance = showDistance;
    }


    @Override
    public Holder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_maintenance_appointment, parent, false);
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(Holder holder, int position) {
        MaintenanceShop maintenanceShop = list.get(position);
        Context context = holder.itemView.getContext();
        if (maintenanceShop != null && context != null) {
            ImageUtil.setImage(context, holder.shop_image, maintenanceShop.shopPhoto);
            ImageUtil.setImageSmall(context, holder.brands_image, maintenanceShop.brandThumb);


//            if (!TextUtils.isEmpty(maintenanceShop.brandThumb)) {
//                Picasso.with(context).load(maintenanceShop.brandThumb).placeholder
//                        (com.hxqc.mall.core.R.drawable.pic_normal_logo).error(com.hxqc.mall.core.R.drawable.pic_normal_logo).into(holder.brands_image);
//            } else {
//                Picasso.with(context).load(com.hxqc.mall.core.R.drawable.pic_normal_logo).into(holder.brands_image);
//            }
            holder.shop_name.setText(maintenanceShop.shopTitle);

            if ("0.0km".equals(maintenanceShop.distance + "") || "0km".equals(maintenanceShop.distance + "") || "0".equals(maintenanceShop.distance + "") || !showDistance) {
                holder.shop_distance.setVisibility(View.GONE);
            } else {
                holder.shop_distance.setVisibility(View.VISIBLE);
                holder.shop_distance.setText(OtherUtil.reformatDistance(maintenanceShop.distance));
            }

            ArrayList<MaintenancePriceOrTitle> maintenancePriceOrTitles = maintenanceShop.promotionList;
            if (maintenancePriceOrTitles != null) {
                try {
                    holder.shop_content_1.setText(maintenancePriceOrTitles.get(0).title);
                } catch (IndexOutOfBoundsException e) {
//                    holder.shop_content_2.setText("这是测试数据1，当前数据为空");
                }
                try {
                    holder.shop_content_2.setText(maintenancePriceOrTitles.get(1).title);
                } catch (IndexOutOfBoundsException e) {
//                    holder.shop_content_2.setText("这是测试数据1，当前数据为空");
                }
                try {
                    holder.shop_content_3.setText(maintenancePriceOrTitles.get(2).title);
                } catch (IndexOutOfBoundsException e) {
//                    holder.shop_content_3.setText("这是测试数据2，当前数据为空");
                }
            }
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public void addData(ArrayList<MaintenanceShop> itemList) {
        if (list != null) {
            list.addAll(itemList);
        } else {
            list = itemList;
        }
        notifyItemRangeInserted(getItemCount(), itemList.size());
    }


    public void replaceDate(ArrayList<MaintenanceShop> itemList) {
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


    public MaintenanceShop getItemData(int position) {

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
        public Button appointmentButton;


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
            appointmentButton = (Button) itemView.findViewById(R.id.make_appointment);
        }
    }
}
