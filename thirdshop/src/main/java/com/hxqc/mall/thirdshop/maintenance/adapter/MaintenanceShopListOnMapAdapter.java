package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.adapter.BaseMapListAdapter;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.model.NewMaintenanceShop;
import com.hxqc.mall.thirdshop.views.ShopRatingBar;

import java.util.ArrayList;

/**
 * Function: 地图选店列表适配器Adapter（保养模块）
 *
 * @author 袁秉勇
 * @since 2016年06月03日
 */
public class MaintenanceShopListOnMapAdapter extends BaseMapListAdapter {
    private final static String TAG = MaintenanceShopListOnMapAdapter.class.getSimpleName();
    private Context mContext;

    private ArrayList< NewMaintenanceShop > newMaintenanceShops = new ArrayList<>();

    private boolean isQuickShop = false;


    public MaintenanceShopListOnMapAdapter(Context mContext, boolean isQuickShop) {
        this.mContext = mContext;
        this.isQuickShop = isQuickShop;
    }


    public void setData(ArrayList< NewMaintenanceShop > newMaintenanceShops) {
        if (this.newMaintenanceShops.size() > 0) {
            this.newMaintenanceShops.clear();
        }
        this.newMaintenanceShops.addAll(newMaintenanceShops);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return newMaintenanceShops.size();
    }


    @Override
    public NewMaintenanceShop getItem(int position) {
        return newMaintenanceShops.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final NewMaintenanceShop newMaintenanceShop = getItem(position);
        
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shop_list_on_map_maintenance, null);
            viewHolder = new ViewHolder();
            viewHolder.mShopNameView = (TextView) convertView.findViewById(R.id.shop_name);
            viewHolder.mShopDistanceView = (TextView) convertView.findViewById(R.id.shop_distance);
            viewHolder.mShopAddressView = (TextView) convertView.findViewById(R.id.shop_address);
            viewHolder.shopRatingBar = (ShopRatingBar) convertView.findViewById(R.id.shop_rating_bar);
            viewHolder.mPayContent = (LinearLayout) convertView.findViewById(R.id.pay_content);
            viewHolder.mShopPriceView = (TextView) convertView.findViewById(R.id.shop_price);
            viewHolder.mNavigationContentView = (LinearLayout) convertView.findViewById(R.id.navigation_view);
            viewHolder.mNavigationView = (Button) convertView.findViewById(R.id.shop_navigation_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == selectedPosition ) {
            convertView.setBackgroundColor(Color.parseColor("#E6F3F9"));
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }


        if (isQuickShop) {
            viewHolder.shopRatingBar.setVisibility(View.VISIBLE);
            viewHolder.mPayContent.setVisibility(View.VISIBLE);
            viewHolder.mNavigationContentView.setVisibility(View.VISIBLE);

            try {
                viewHolder.shopRatingBar.setData(Float.valueOf(TextUtils.isEmpty(newMaintenanceShop.level) ? "4.65" : newMaintenanceShop.level));
            } catch (Exception e) {
                viewHolder.shopRatingBar.setData(4.5f);
            }

            if (newMaintenanceShop.pays != null && newMaintenanceShop.pays.size() > 0) {
                viewHolder.mPayContent.removeAllViews();
                for (int i = 0; i < newMaintenanceShop.pays.size(); i++) {
                    ImageView imageView = new ImageView(mContext);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, mContext.getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, mContext.getResources().getDisplayMetrics()));
                    layoutParams.setMargins(0, 0, 10, 0);
                    imageView.setLayoutParams(layoutParams);
                    ImageUtil.setImage(mContext, imageView, newMaintenanceShop.pays.get(i).thumb);
                    viewHolder.mPayContent.addView(imageView);
                }
            } else {
                viewHolder.mPayContent.removeAllViews();
            }

            viewHolder.mNavigationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivitySwitchBase.toAMapNvai(mContext, new PickupPointT(newMaintenanceShop.address, newMaintenanceShop.latitude + "", newMaintenanceShop.longitude + "", newMaintenanceShop.shopTel));
                }
            });
        } else {
            viewHolder.shopRatingBar.setVisibility(View.GONE);
            viewHolder.mPayContent.setVisibility(View.GONE);
            viewHolder.mNavigationContentView.setVisibility(View.GONE);
        }

        viewHolder.mShopNameView.setText((position + 1) + "." + newMaintenanceShop.shopTitle);
//        if (showDistance) {
            viewHolder.mShopDistanceView.setText(OtherUtil.reformatDistance(newMaintenanceShop.distance));
//            viewHolder.mShopDistanceView.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.mShopDistanceView.setVisibility(View.GONE);
//        }



        viewHolder.mShopPriceView.setText(OtherUtil.reformatPrice(newMaintenanceShop.amount+"") + "元");

        viewHolder.mShopAddressView.setText(newMaintenanceShop.address);
        return convertView;
    }

    class ViewHolder {
        TextView mShopNameView;
        TextView mShopDistanceView;
        ShopRatingBar shopRatingBar;
        TextView mShopAddressView;
        LinearLayout mPayContent;
        LinearLayout mNavigationContentView;
        Button mNavigationView;
        TextView mShopPriceView;
    }
}
