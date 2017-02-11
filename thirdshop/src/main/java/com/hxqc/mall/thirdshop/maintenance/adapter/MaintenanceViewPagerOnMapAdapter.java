package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.adapter.BaseMapViewPagerAdapter;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.model.NewMaintenanceShop;
import com.hxqc.mall.thirdshop.views.ShopRatingBar;

import java.util.ArrayList;

/**
 * Function: 地图上找店ViewPager的Adapter
 *
 * @author 袁秉勇
 * @since 2016年06月20日
 */
public class MaintenanceViewPagerOnMapAdapter extends BaseMapViewPagerAdapter {
    private final static String TAG = MaintenanceViewPagerOnMapAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private CallBack callBack;

//    private View convertView;

    private boolean isQuickShop = false;

    private ArrayList< NewMaintenanceShop > newMaintenanceShops = new ArrayList<>();

    private ArrayList< View > views;


    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }


    public MaintenanceViewPagerOnMapAdapter(Context mContext, boolean isQuickShop) {
        this.mContext = mContext;
        this.isQuickShop = isQuickShop;
        mLayoutInflater = LayoutInflater.from(mContext);

        views = new ArrayList<>();
    }


    public void setData(ArrayList< NewMaintenanceShop > newMaintenanceShops) {
        if (this.newMaintenanceShops.size() > 0) {
            this.newMaintenanceShops.clear();
        }

        if (views.size() > 0) {
            views.clear();
        }

        for (int i = 0; i < newMaintenanceShops.size(); i++) {
            View convertView = mLayoutInflater.inflate(R.layout.item_shop_list_on_map_maintenance_viewpager, null);
            views.add(convertView);
        }

        this.newMaintenanceShops.addAll(newMaintenanceShops);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        Log.e(TAG, " ============ getCount ");
//        if (newMaintenanceShops.size() == 1) {
//            return 1;
//        } else {
//            return Integer.MAX_VALUE;
//        }
        return views.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        Log.e(TAG, " ============ instantiateItem ");
        ViewHolder viewHolder;
//
//        position %= newMaintenanceShops.size();
//
//        if (position < 0) {
//            position = newMaintenanceShops.size() + position;
//        }
//
//
//        View view = views.get(position);
//
        final NewMaintenanceShop newMaintenanceShop = newMaintenanceShops.get(position);
//
////        if (convertView == null) {
////            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shop_list_on_map_maintenance_viewpager, null);
//
//            viewHolder = new ViewHolder();
//            viewHolder.mShopNameView = (TextView) view.findViewById(R.id.shop_name);
//            viewHolder.mShopDistanceView = (TextView) view.findViewById(R.id.shop_distance);
//            viewHolder.mShopAddressView = (TextView) view.findViewById(R.id.shop_address);
//            viewHolder.shopRatingBar = (ShopRatingBar) view.findViewById(R.id.shop_rating_bar);
//            viewHolder.mPayContent = (LinearLayout) view.findViewById(R.id.pay_content);
//            viewHolder.mShopPriceView = (TextView) view.findViewById(R.id.shop_price);
//
////            viewHolder.mNavigationView = (Button) convertView.findViewById(R.id.shop_navigation_btn);
//
////            convertView.setTag(viewHolder);
////        }
////        else {
////            viewHolder = (ViewHolder) convertView.getTag();
////        }
//
//        viewHolder.mShopNameView.setText(newMaintenanceShop.shopTitle);
////        if (showDistance) {
//        viewHolder.mShopDistanceView.setText(OtherUtil.reformatDistance(newMaintenanceShop.distance));
////            viewHolder.mShopDistanceView.setVisibility(View.VISIBLE);
////        } else {
////            viewHolder.mShopDistanceView.setVisibility(View.GONE);
////        }
//
//        try {
//            viewHolder.shopRatingBar.setData(Float.valueOf(TextUtils.isEmpty(newMaintenanceShop.level) ? "4.65" : newMaintenanceShop.level));
//        } catch (Exception e) {
//            viewHolder.shopRatingBar.setData(4.5f);
//        }
//
//        viewHolder.mShopPriceView.setText(OtherUtil.reformatPrice(newMaintenanceShop.amount + "") + "元");
//
//        viewHolder.mShopAddressView.setText(newMaintenanceShop.address);
//
//        if (newMaintenanceShop.pays != null && newMaintenanceShop.pays.size() > 0) {
//            viewHolder.mPayContent.removeAllViews();
//            for (int i = 0; i < newMaintenanceShop.pays.size(); i++) {
//                ImageView imageView = new ImageView(mContext);
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, mContext.getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, mContext.getResources().getDisplayMetrics()));
//                layoutParams.setMargins(0, 0, 10, 0);
//                imageView.setLayoutParams(layoutParams);
//                ImageUtil.setImage(mContext, imageView, newMaintenanceShop.pays.get(i).thumb);
//                viewHolder.mPayContent.addView(imageView);
//            }
//        }
//
////        viewHolder.mNavigationView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                ActivitySwitchBase.toAMapNvai(mContext, new PickupPointT(newMaintenanceShop.address, newMaintenanceShop.latitude + "", newMaintenanceShop.longitude + "", newMaintenanceShop.shopTel));
////            }
////        });
//
//        if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);

        container.addView(views.get(position));

        View view = views.get(position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null) callBack.callBack(position);
            }
        });

        viewHolder = new ViewHolder();
        viewHolder.mShopNameView = (TextView) view.findViewById(R.id.shop_name);
        viewHolder.mShopDistanceView = (TextView) view.findViewById(R.id.shop_distance);
        viewHolder.mShopAddressView = (TextView) view.findViewById(R.id.shop_address);
        viewHolder.shopRatingBar = (ShopRatingBar) view.findViewById(R.id.shop_rating_bar);
        viewHolder.mPayContent = (LinearLayout) view.findViewById(R.id.pay_content);
        viewHolder.mShopPriceView = (TextView) view.findViewById(R.id.shop_price);

        viewHolder.mShopNameView.setText((position + 1) + "." + newMaintenanceShop.shopTitle);
//        if (showDistance) {
        viewHolder.mShopDistanceView.setText(OtherUtil.reformatDistance(newMaintenanceShop.distance));
//            viewHolder.mShopDistanceView.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.mShopDistanceView.setVisibility(View.GONE);
//        }

        if (isQuickShop) {
            viewHolder.shopRatingBar.setVisibility(View.VISIBLE);
            viewHolder.mPayContent.setVisibility(View.VISIBLE);

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

            try {
                viewHolder.shopRatingBar.setData(Float.valueOf(TextUtils.isEmpty(newMaintenanceShop.level) ? "4.65" : newMaintenanceShop.level));
            } catch (Exception e) {
                viewHolder.shopRatingBar.setData(4.5f);
            }
        } else {
            viewHolder.shopRatingBar.setVisibility(View.GONE);
            viewHolder.mPayContent.setVisibility(View.GONE);
        }

        viewHolder.mShopPriceView.setText(OtherUtil.reformatPrice(newMaintenanceShop.amount + "") + "元");

        viewHolder.mShopAddressView.setText(newMaintenanceShop.address);

//        viewHolder.mNavigationView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ActivitySwitchBase.toAMapNvai(mContext, new PickupPointT(newMaintenanceShop.address, newMaintenanceShop.latitude + "", newMaintenanceShop.longitude + "", newMaintenanceShop.shopTel));
//            }
//        });

//        viewHolder.mNavigationView = (Button) convertView.findViewById(R.id.shop_navigation_btn);


        return views.get(position);
    }


    @Override
    public int getItemPosition(Object object) {
        Log.e(TAG, " ============ getItemPosition ");
        return super.getItemPosition(object);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.e(TAG, " ================ destroyItem ");
        container.removeView(views.get(position));
    }


    class ViewHolder {
        TextView mShopNameView;
        TextView mShopDistanceView;
        ShopRatingBar shopRatingBar;
        TextView mShopAddressView;
        LinearLayout mPayContent;
        //        Button mNavigationView;
        TextView mShopPriceView;
    }


    public interface CallBack {
        void callBack(int position);
    }
}
