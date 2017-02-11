package com.hxqc.fastreqair.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hxqc.fastreqair.model.CarWashShopListBean;
import com.hxqc.fastreqair.util.CarWashActivitySwitcher;
import com.hxqc.mall.core.adapter.BaseMapListAdapter;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.thirdshop.views.ShopRatingBar;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Function: 地图选店列表适配器Adapter（洗车模块）
 *
 * @author 袁秉勇
 * @since 2016年06月04日
 */
public class CarWashShopListOnMapAdapter extends BaseMapListAdapter {
    private final static String TAG = CarWashShopListOnMapAdapter.class.getSimpleName();
    private Context mContext;

    private ArrayList< CarWashShopListBean > carWashShopListBeans = new ArrayList<>();


    public CarWashShopListOnMapAdapter(Context mContext) {
        this.mContext = mContext;
    }


    public void setData(ArrayList< CarWashShopListBean > carWashShopListBeans) {
        if (this.carWashShopListBeans.size() > 0) {
            this.carWashShopListBeans.clear();
        }

        this.carWashShopListBeans.addAll(carWashShopListBeans);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return carWashShopListBeans.size();
    }


    @Override
    public CarWashShopListBean getItem(int position) {
        return carWashShopListBeans.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CarWashShopListBean carWashShopListBean = getItem(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shop_list_on_map_car_wash, null);
            viewHolder = new ViewHolder();
//            viewHolder.mContentView = (LinearLayout) convertView.findViewById(R.id.content_view);
//            viewHolder.mShopImageView = (ImageView) convertView.findViewById(R.id.shop_image);
            viewHolder.mShopNameView = (TextView) convertView.findViewById(R.id.shop_name);
            viewHolder.mShopDistanceView = (TextView) convertView.findViewById(R.id.shop_distance);
            viewHolder.mShopPriceView = (TextView) convertView.findViewById(R.id.shop_price);
            viewHolder.mShopAddressView = (TextView) convertView.findViewById(R.id.shop_address);
            viewHolder.mRatingBarView = (ShopRatingBar) convertView.findViewById(R.id.shop_rating_bar);
            viewHolder.mOrderNumView = (TextView) convertView.findViewById(R.id.order_number);
            viewHolder.mNavigationView = (Button) convertView.findViewById(R.id.shop_navigation_btn);
            viewHolder.mShopHomeView = (Button) convertView.findViewById(R.id.shop_home_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == selectedPosition ) {
            convertView.setBackgroundColor(Color.parseColor("#E6F3F9"));
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }

//        if (carWashShopListBean.shopPhoto != null && carWashShopListBean.shopPhoto.size() > 0) {
//            ImageUtil.setImage(mContext, viewHolder.mShopImageView, carWashShopListBean.shopPhoto.get(0));
//        } else {
//            viewHolder.mShopImageView.setImageResource(R.drawable.pic_normal);
//        }

        viewHolder.mShopNameView.setText((position + 1) + "." + carWashShopListBean.shopTitle);
        viewHolder.mShopDistanceView.setText(OtherUtil.reformatDistance(carWashShopListBean.distance));

        viewHolder.mRatingBarView.setData(carWashShopListBean.star > 5 ? 5 : carWashShopListBean.star);

        viewHolder.mShopAddressView.setText(carWashShopListBean.address);

        viewHolder.mShopPriceView.setText(OtherUtil.formatPriceString(carWashShopListBean.amount, "-", "—", false));
        viewHolder.mOrderNumView.setText(carWashShopListBean.orderCount+"");

        viewHolder.mShopHomeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarWashActivitySwitcher.toWashCarShop(mContext, carWashShopListBean.shopID);
            }
        });

        viewHolder.mNavigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcher.toAMapNvai(mContext, new PickupPointT(carWashShopListBean.address, carWashShopListBean.latitude+"", carWashShopListBean.longitude+"", carWashShopListBean.shopTel));
            }
        });

        return convertView;
    }


    @Override
    public boolean areAllItemsEnabled() {
        return super.areAllItemsEnabled();
    }


    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }


    class ViewHolder {
//        LinearLayout mContentView;
//        ImageView mShopImageView;
        TextView mShopNameView;
        TextView mShopDistanceView;
        TextView mShopAddressView;
        TextView mShopPriceView;
        ShopRatingBar mRatingBarView;
        TextView mOrderNumView;
        Button mNavigationView;
        Button mShopHomeView;
    }
}
