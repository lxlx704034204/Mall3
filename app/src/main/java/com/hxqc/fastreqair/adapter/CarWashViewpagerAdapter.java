package com.hxqc.fastreqair.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hxqc.fastreqair.model.CarWashShopListBean;
import com.hxqc.fastreqair.util.CarWashActivitySwitcher;
import com.hxqc.mall.core.adapter.BaseMapViewPagerAdapter;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.thirdshop.views.ShopRatingBar;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年06月24日
 */
public class CarWashViewpagerAdapter extends BaseMapViewPagerAdapter {
    private final static String TAG = CarWashViewpagerAdapter.class.getSimpleName();
    private Context mContext;

    private ArrayList< CarWashShopListBean > carWashShopListBeans = new ArrayList<>();

    private ArrayList< View > views;

    private CallBack callBack;

    private LayoutInflater mLayoutInflater;


    private CarWashShopListBean carWashShopListBean;


    public void setCallback(CallBack callback) {
        this.callBack = callback;
    }


    public CarWashViewpagerAdapter(final Context mContext) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);

        views = new ArrayList<>();
    }


    public void setData(ArrayList< CarWashShopListBean > carWashShopListBeans) {
        if (this.carWashShopListBeans.size() > 0) {
            this.carWashShopListBeans.clear();
        }

        if (views.size() > 0) {
            views.clear();
        }

        for (int i = 0; i < carWashShopListBeans.size(); i++) {
            View view = mLayoutInflater.inflate(R.layout.item_shop_list_on_map_car_wash, null);
            views.add(view);
        }

        this.carWashShopListBeans.addAll(carWashShopListBeans);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return views.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ViewHolder viewHolder = new ViewHolder();

        carWashShopListBean = carWashShopListBeans.get(position);

        View view = views.get(position);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null) callBack.callBack(position);
            }
        });

        viewHolder.mShopNameView = (TextView) view.findViewById(R.id.shop_name);
        viewHolder.mShopDistanceView = (TextView) view.findViewById(R.id.shop_distance);
        viewHolder.mShopPriceView = (TextView) view.findViewById(R.id.shop_price);
        viewHolder.mShopAddressView = (TextView) view.findViewById(R.id.shop_address);
        viewHolder.mRatingBarView = (ShopRatingBar) view.findViewById(R.id.shop_rating_bar);
        viewHolder.mOrderNumView = (TextView) view.findViewById(R.id.order_number);
        viewHolder.mNavigationView = (Button) view.findViewById(R.id.shop_navigation_btn);
        viewHolder.mShopHomeView = (Button) view.findViewById(R.id.shop_home_btn);

        viewHolder.mShopNameView.setText((position + 1) + "." + carWashShopListBean.shopTitle);
        viewHolder.mShopDistanceView.setText(OtherUtil.reformatDistance(carWashShopListBean.distance));

        viewHolder.mRatingBarView.setData(Float.valueOf(TextUtils.isEmpty(carWashShopListBean.evaluate) ? "4.65" : carWashShopListBean.evaluate));

        viewHolder.mShopAddressView.setText(carWashShopListBean.address);

        viewHolder.mShopPriceView.setText(OtherUtil.formatPriceString(carWashShopListBean.amount));
        viewHolder.mOrderNumView.setText(carWashShopListBean.orderCount + "单");

        viewHolder.mShopHomeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarWashActivitySwitcher.toWashCarShop(mContext, carWashShopListBean.shopID);
            }
        });

        viewHolder.mNavigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcher.toAMapNvai(mContext, new PickupPointT(carWashShopListBean.address, carWashShopListBean.latitude + "", carWashShopListBean.longitude + "", carWashShopListBean.shopTel));
            }
        });

        container.addView(views.get(position));

        return views.get(position);
    }


    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }


    public interface CallBack {
        void callBack(int position);
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
