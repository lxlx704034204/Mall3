package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
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
import com.hxqc.mall.thirdshop.maintenance.model.MaintenanceShop;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.util.DebugLog;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Function: 紧急救援页面Adapter
 *
 * @author 袁秉勇
 * @since 2016年02月26日
 */
public class EmergencyRescueAdapter extends RecyclerView.Adapter< EmergencyRescueAdapter.Holder > {
    private static final String TAG = NormalMaintenanceShopAdapter.class.getSimpleName();
    public ArrayList< MaintenanceShop > list = new ArrayList<>();
    private Context mContext;
    private boolean showDistance = false;


    public EmergencyRescueAdapter(Context mContext) {
        this.mContext = mContext;
    }


    public void setShowDistance(boolean showDistance) {
        this.showDistance = showDistance;
    }


    @Override
    public Holder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emergency_rescue, parent, false);
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final MaintenanceShop maintenanceShop = list.get(position);
        Context context = holder.itemView.getContext();
        if (maintenanceShop != null && context != null) {
            ImageUtil.setImage(context, holder.shop_image, maintenanceShop.shopPhoto);
            ImageUtil.setImageSmall(context, holder.brands_image, maintenanceShop.brandThumb);
//            if (!TextUtils.isEmpty(maintenanceShop.brandThumb)) {
//                Picasso.with(context).load(maintenanceShop.brandThumb).placeholder(R.drawable.pic_normal_logo).error(R.drawable.pic_normal_logo).into(holder.brands_image);
//            } else {
//                Picasso.with(context).load(R.drawable.pic_normal_logo).into(holder.brands_image);
//            }

            holder.shop_name.setText(maintenanceShop.shopTitle);

            if ("0.0km".equals(maintenanceShop.distance + "") || "0km".equals(maintenanceShop.distance + "") || "0".equals(maintenanceShop.distance + "") || !showDistance) {
//                holder.shop_distance.setVisibility(View.GONE);
                holder.shop_distance.setText("");
            } else {
//                holder.shop_distance.setVisibility(View.VISIBLE);
                holder.shop_distance.setText("距离: " + OtherUtil.reformatDistance(maintenanceShop.distance));
            }

            if (maintenanceShop.shopPoint != null && !TextUtils.isEmpty(maintenanceShop.shopPoint.address)) {
                holder.shop_address.setText("地址: " + maintenanceShop.shopPoint.address);
            } else {
                holder.shop_address.setText("请进店查看详情");
            }

//            final ArrayList< MaintenancePriceOrTitle > maintenancePriceOrTitle = maintenanceShop.promotionList;
//            if (maintenancePriceOrTitle != null) {
//                try {
//                    holder.shop_content_1.setText(maintenancePriceOrTitle.get(0).title);
//                } catch (IndexOutOfBoundsException e) {
////                    holder.shop_content_1.setText("这是测试数据0，当前数据为空");
//                }
//            }
//
//            final ArrayList< MaintenancePriceOrTitle > maintenancePriceOrTitles = maintenanceShop.maintenanceList;
//            if (maintenancePriceOrTitles != null) {
//                try {
//                    holder.shop_content_2.setText(ReformatString(maintenancePriceOrTitles.get(0).title, ": " + reformatPrice(maintenancePriceOrTitles.get(0).price + "") + " 起"));
//                } catch (IndexOutOfBoundsException e) {
////                    holder.shop_content_2.setText("这是测试数据1，当前数据为空");
//                }
//                try {
//                    holder.shop_content_3.setText(ReformatString(maintenancePriceOrTitles.get(1).title, ": " + reformatPrice(maintenancePriceOrTitles.get(1).price + "") + " 起"));
//                } catch (IndexOutOfBoundsException e) {
////                    holder.shop_content_3.setText("这是测试数据2，当前数据为空");
//                }
//            }

            holder.mNavigationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivitySwitcherMaintenance.toAMapNvai(mContext, 0, maintenanceShop.shopPoint);
                }
            });

            holder.mCallView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MaterialDialog);
                    builder.setTitle("拨打电话").setMessage(maintenanceShop.rescueTel).setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            Uri uri = Uri.parse("tel:" + maintenanceShop.rescueTel);
                            intent.setData(uri);
                            mContext.startActivity(intent);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
                }
            });

            holder.contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ActivitySwitcherMaintenance.toShopDetails(mContext, 3, maintenanceShop.shopTel);
                    ActivitySwitcherThirdPartShop.toShopDetailsRescue(mContext, maintenanceShop.shopID);
                }
            });
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
    public SpannableString ReformatString(String title, String str) {
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
        public LinearLayout contentView;
        public ImageView shop_image;
        public ImageView brands_image;
        public TextView shop_name;
        public TextView shop_distance;
        public TextView shop_content_1;
        public TextView shop_content_2;
        public TextView shop_content_3;
        public TextView shop_address;
        public Button mCallView;
        public Button mNavigationView;


        public Holder(View itemView) {
            super(itemView);
            if (Build.VERSION.SDK_INT < 21) {
                itemView.setBackgroundResource(R.drawable.t_bg_item);
            }
            contentView = (LinearLayout) itemView.findViewById(R.id.content);
            shop_image = (ImageView) itemView.findViewById(R.id.shop_image);
            brands_image = (ImageView) itemView.findViewById(R.id.brands_image);
            shop_name = (TextView) itemView.findViewById(R.id.shop_name);
            shop_distance = (TextView) itemView.findViewById(R.id.shop_distance);
            shop_content_1 = (TextView) itemView.findViewById(R.id.shop_content_1);
            shop_content_2 = (TextView) itemView.findViewById(R.id.shop_content_2);
            shop_content_3 = (TextView) itemView.findViewById(R.id.shop_content_3);
            shop_address = (TextView) itemView.findViewById(R.id.shop_address);
            mCallView = (Button) itemView.findViewById(R.id.call_view);
            mNavigationView = (Button) itemView.findViewById(R.id.navigation_view);
        }
    }
}
