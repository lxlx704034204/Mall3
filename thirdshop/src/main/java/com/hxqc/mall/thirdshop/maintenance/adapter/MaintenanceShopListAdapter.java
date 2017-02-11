package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.model.NewMaintenanceShop;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
import com.hxqc.mall.thirdshop.views.ShopRatingBar;

import java.util.ArrayList;

/**
 * Function: 保养店铺列表Adapter
 *
 * @author 袁秉勇
 * @since 2016年04月18日
 */
public class MaintenanceShopListAdapter extends RecyclerView.Adapter< MaintenanceShopListAdapter.ViewHolder > {
    private final static String TAG = MaintenanceShopListAdapter.class.getSimpleName();
    private Context mContext;

    private boolean showDistance = false;

    private boolean isQuickShop = false;

    ArrayList< NewMaintenanceShop > list = new ArrayList<>();


    public void setShowDistance(boolean showDistance) {
        this.showDistance = showDistance;
    }


    public MaintenanceShopListAdapter(Context mContext, boolean isQuickShop) {
        this.mContext = mContext;
        this.isQuickShop = isQuickShop;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_maintenance_shop_list, null));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NewMaintenanceShop newMaintenanceShop = list.get(position);

        if (newMaintenanceShop.shopPhoto != null && newMaintenanceShop.shopPhoto.size() > 0 && !TextUtils.isEmpty(newMaintenanceShop.shopPhoto.get(0))) {
            ImageUtil.setImage(mContext, holder.mShopImageView, newMaintenanceShop.shopPhoto.get(0));
        } else {
            holder.mShopImageView.setImageResource(R.drawable.pic_normal);
        }

        if (newMaintenanceShop.used == 1) {
            holder.mMaintenanceOftenView.setVisibility(View.VISIBLE);
        } else {
            holder.mMaintenanceOftenView.setVisibility(View.GONE);
        }

        if (isQuickShop) {
            holder.mContentHiddenView.setVisibility(View.VISIBLE);
            holder.mShopAddressView.setVisibility(View.GONE);

            holder.shopRatingBar.setData(Float.valueOf(TextUtils.isEmpty(newMaintenanceShop.score) ? "4.65" : newMaintenanceShop.score));

            if (newMaintenanceShop.pays != null && newMaintenanceShop.pays.size() > 0) {
                holder.mPayContent.removeAllViews();
                for (int i = 0; i < newMaintenanceShop.pays.size(); i++) {
                    ImageView imageView = new ImageView(mContext);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, mContext.getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, mContext.getResources().getDisplayMetrics()));
                    layoutParams.setMargins(0, 0, 10, 0);
                    imageView.setLayoutParams(layoutParams);
                    ImageUtil.setImage(mContext, imageView, newMaintenanceShop.pays.get(i).thumb);
                    holder.mPayContent.addView(imageView);
                }
            } else {
                holder.mPayContent.removeAllViews();
            }
        } else {
            holder.mContentHiddenView.setVisibility(View.GONE);
            holder.mShopAddressView.setVisibility(View.VISIBLE);

            holder.mShopAddressView.setText("地址: " + newMaintenanceShop.address);
        }

        holder.mShopNameView.setText(newMaintenanceShop.shopTitle);
//        if (showDistance) {
        holder.mShopDistanceView.setText(OtherUtil.reformatDistance(newMaintenanceShop.distance));
//            holder.mShopDistanceView.setVisibility(View.VISIBLE);
//        } else {
//            holder.mShopDistanceView.setVisibility(View.GONE);
//        }

        holder.mShopPriceView.setText(OtherUtil.reformatPrice(newMaintenanceShop.amount + "") + "元");
        holder.mShopInformationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherMaintenance.toShopIntroduceInfo(mContext, newMaintenanceShop.queryShop);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public void addData(ArrayList< NewMaintenanceShop > itemList) {
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


    public NewMaintenanceShop getItemData(int position) {

        if (list != null) {
            return list.get(position);
        } else {
            return null;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mContentView;
        //        ImageView mUsedLogoView;
        ImageView mShopImageView;
        ImageView mMaintenanceOftenView;
        TextView mShopNameView;
        TextView mShopDistanceView;
        LinearLayout mContentHiddenView;
        TextView mShopAddressView;
        LinearLayout mPayContent;
        ShopRatingBar shopRatingBar;
        TextView mShopPriceView;
        TextView mShopInformationView;
        View mLineView;
        View mShadowView;


        public ViewHolder(View itemView) {
            super(itemView);

            mContentView = (LinearLayout) itemView.findViewById(R.id.content_view);
//            mUsedLogoView = (ImageView) itemView.findViewById(R.id.used_logo);
            mMaintenanceOftenView = (ImageView) itemView.findViewById(R.id.maintenance_often_img);
            mShopImageView = (ImageView) itemView.findViewById(R.id.shop_image);
            mShopNameView = (TextView) itemView.findViewById(R.id.shop_name);
            mShopDistanceView = (TextView) itemView.findViewById(R.id.shop_distance);
            mContentHiddenView = (LinearLayout) itemView.findViewById(R.id.quick_shop_hide_view);
            mShopAddressView = (TextView) itemView.findViewById(R.id.shop_address);
            mPayContent = (LinearLayout) itemView.findViewById(R.id.pay_content);
            shopRatingBar = (ShopRatingBar) itemView.findViewById(R.id.shop_rating_bar);
            mShopPriceView = (TextView) itemView.findViewById(R.id.shop_price);
            mShopInformationView = (TextView) itemView.findViewById(R.id.shop_information);
            mLineView = itemView.findViewById(R.id.line);
            mShadowView = itemView.findViewById(R.id.shadow);
        }
    }
}
