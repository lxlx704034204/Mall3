package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.model.MaintenancePriceOrTitle;
import com.hxqc.mall.thirdshop.maintenance.model.MaintenanceShop;
import com.hxqc.util.DebugLog;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Function: 常规保养未选择品牌和车系时的Adapter
 *
 * @author 袁秉勇
 * @since 2016年02月19日
 */
public class NormalMaintenanceShopAdapter extends RecyclerView.Adapter< NormalMaintenanceShopAdapter.Holder > {
    private static final String TAG = NormalMaintenanceShopAdapter.class.getSimpleName();
    public ArrayList< MaintenanceShop > list = new ArrayList<>();
    private Context mContext;
    private boolean showDistance = false;
    private boolean showPrice = false;


    public NormalMaintenanceShopAdapter(Context mContext) {
        this.mContext = mContext;
    }


    public void setShowDistance(boolean showDistance) {
        this.showDistance = showDistance;
    }


    public void setShowPrice(boolean showPrice) {
        this.showPrice = showPrice;
    }


    @Override
    public Holder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_maintenance_shop, parent, false);
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(Holder holder, int position) {
        MaintenanceShop maintenanceShop = list.get(position);
        Context context = holder.itemView.getContext();
        if (maintenanceShop != null && context != null) {
            ImageUtil.setImage(context, holder.shop_image, maintenanceShop.shopPhoto);

            ImageUtil.setImage(context, holder.brands_image, maintenanceShop.brandThumb);

            holder.shop_name.setText(maintenanceShop.shopTitle);
            if ("0km".equals(maintenanceShop.distance+"") || "0".equals(maintenanceShop.distance+"") || !showDistance) {
                holder.shop_distance.setVisibility(View.GONE);
            } else {
                holder.shop_distance.setVisibility(View.VISIBLE);
                holder.shop_distance.setText(OtherUtil.reformatDistance(maintenanceShop.distance));
            }

            ArrayList< MaintenancePriceOrTitle > maintenancePriceOrTitle = maintenanceShop.promotionList;
            try {
                holder.shop_content_1.setText(maintenancePriceOrTitle.get(0).title);
            } catch (IndexOutOfBoundsException e) {
//                    holder.shop_content_2.setText("这是测试数据1，当前数据为空");
            }

            ArrayList< MaintenancePriceOrTitle > maintenancePriceOrTitles;
            if (showPrice) {
                maintenancePriceOrTitles = maintenanceShop.maintenanceList;
            } else {
                maintenancePriceOrTitles = maintenanceShop.promotionList;
            }

            if (maintenancePriceOrTitles != null) {
                try {
                    if (showPrice) {
                        holder.shop_content_2.setText(reformatString(maintenancePriceOrTitles.get(0).title, ": " + reformatPrice(maintenancePriceOrTitles.get(0).price+"") + " 起"));
                    } else {
                        holder.shop_content_2.setText(maintenancePriceOrTitles.get(1).title);
                    }
                } catch (IndexOutOfBoundsException e) {
//                    holder.shop_content_2.setText("这是测试数据1，当前数据为空");
                }
                try {
                    if (showPrice) {
                        holder.shop_content_3.setText(reformatString(maintenancePriceOrTitles.get(1).title, ": " + reformatPrice(maintenancePriceOrTitles.get(1).price+"") + " 起"));
                    } else {
                        holder.shop_content_3.setText(maintenancePriceOrTitles.get(2).title);
                    }
                } catch (IndexOutOfBoundsException e) {
//                    holder.shop_content_3.setText("这是测试数据2，当前数据为空");
                }
            }
        }
    }


    protected String reformatPrice(String price) {
        if (TextUtils.isEmpty(price)) return "0";
        DecimalFormat df = new DecimalFormat("######0.00");
        try {
            price = df.format(Double.valueOf(price));
        } catch (Exception e) {
            DebugLog.e(TAG, "classCastException");
        }
        if (price.endsWith("0")) {
            price = price.substring(0, price.length() - 1);
            if (price.endsWith("0")) {
                return price.substring(0, price.length() - 2);
            }
        }
        return price;
    }


    /** 重新定义String显示的样式 **/
    public SpannableString reformatString(String title, String str) {
        if (TextUtils.isEmpty(title)) title = "套餐";
        SpannableString spannableString = new SpannableString(title + str);
        spannableString.setSpan(new TextAppearanceSpan(mContext, R.style.RedText13), title.length() + 2, title.length() + str.length() - 2, Spanned.SPAN_COMPOSING);
        return spannableString;
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


    public void replaceDate(ArrayList< MaintenanceShop > itemList) {
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
