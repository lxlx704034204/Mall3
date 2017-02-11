package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.content.Context;
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
import com.hxqc.mall.thirdshop.maintenance.model.MaintenanceShop;

import java.util.ArrayList;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年09月30日
 */
public class AppointmentMaintenanceGridAdapter extends RecyclerView.Adapter< AppointmentMaintenanceGridAdapter.Holder > {
    private final static String TAG = AppointmentMaintenanceGridAdapter.class.getSimpleName();
    private Context mContext;

    public ArrayList< MaintenanceShop > list = new ArrayList<>();
    private boolean showDistance = false;


    public void setShowDistance(boolean showDistance) {
        this.showDistance = showDistance;
    }


    @Override
    public Holder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maintance_appiontment_gird_item, parent, false);
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(Holder holder, int position) {
        MaintenanceShop maintenanceShop = list.get(position);
        Context context = holder.itemView.getContext();
        if (maintenanceShop != null && context != null) {
            ImageUtil.setImage(context, holder.shop_image, maintenanceShop.shopPhoto);
            ImageUtil.setImageSmall(context, holder.brands_image, maintenanceShop.brandThumb);

            if (maintenanceShop.used == 1) {
                holder.maintenanceOftenImg.setVisibility(View.VISIBLE);
            } else {
                holder.maintenanceOftenImg.setVisibility(View.GONE);
            }

            holder.shop_name.setText(maintenanceShop.shopTitle);

            if ("0.0km".equals(maintenanceShop.distance + "") || "0km".equals(maintenanceShop.distance + "") || "0".equals(maintenanceShop.distance + "") || !showDistance) {
//                holder.shop_distance.setVisibility(View.GONE);
                holder.shop_distance.setText("");
            } else {
                holder.shop_distance.setVisibility(View.VISIBLE);
                holder.shop_distance.setText("距离 " + OtherUtil.reformatDistance(maintenanceShop.distance));
            }
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public void addData(ArrayList< MaintenanceShop > itemList) {
        if (list != null) {
            list.addAll(itemList);
        } else {
            list = itemList;
        }
        notifyItemRangeInserted(getItemCount(), itemList.size());
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
        public ImageView maintenanceOftenImg;
        public ImageView brands_image;
        public TextView shop_name;
        public TextView shop_distance;
        public Button maintenanceBtn;


        public Holder(View itemView) {
            super(itemView);
//            if (Build.VERSION.SDK_INT < 21) {
//                itemView.setBackgroundResource(R.drawable.t_bg_item);
//            }
            shop_image = (ImageView) itemView.findViewById(R.id.shop_image);
            maintenanceOftenImg = (ImageView) itemView.findViewById(R.id.maintenance_often_img);
            brands_image = (ImageView) itemView.findViewById(R.id.shop_brand);
            shop_name = (TextView) itemView.findViewById(R.id.shop_name);
            shop_distance = (TextView) itemView.findViewById(R.id.shop_distance);
            maintenanceBtn = (Button) itemView.findViewById(R.id.maintain_btn);
        }
    }
}
