package com.hxqc.mall.thirdshop.accessory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.adapter.BaseMapViewPagerAdapter;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.api.AccessoryApiClient;
import com.hxqc.mall.thirdshop.accessory.model.LaborCost;
import com.hxqc.mall.thirdshop.accessory.model.PickupShop;
import com.hxqc.mall.thirdshop.accessory.views.LaborCostDialog;
import com.hxqc.mall.thirdshop.views.ShopRatingBar;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年06月24日
 */
public class AccessoryViewPagerAdapter extends BaseMapViewPagerAdapter {
    private final static String TAG = AccessoryViewPagerAdapter.class.getSimpleName();
    private Context mContext;

    private ArrayList< PickupShop > pickupShops = new ArrayList<>();

    private ArrayList< View > views;

    private CallBack callBack;

    private LayoutInflater mLayoutInflater;

    private LaborCostDialog mLaborCostDialog;

    private PickupShop pickupShop;


    public void setCallback(CallBack callback) {
        this.callBack = callback;
    }


    public AccessoryViewPagerAdapter(Context context) {
        this.mContext = context;

        this.mLaborCostDialog = new LaborCostDialog(mContext);

        mLayoutInflater = LayoutInflater.from(mContext);

        views = new ArrayList<>();
    }


    public void setData(ArrayList< PickupShop > pickupShops) {
        if (this.pickupShops.size() > 0) {
            this.pickupShops.clear();
        }

        if (views.size() > 0) {
            views.clear();
        }

        for (int i = 0; i < pickupShops.size(); i++) {
            View view = mLayoutInflater.inflate(R.layout.item_shop_list_on_map_accessory, null);
            views.add(view);
        }

        this.pickupShops.addAll(pickupShops);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return views.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ViewHolder viewHolder = new ViewHolder();

        pickupShop = pickupShops.get(position);

        View view = views.get(position);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null) callBack.callBack(position);
            }
        });

        viewHolder.mShopNameView = (TextView) view.findViewById(R.id.shop_name);
        viewHolder.mShopDistanceView = (TextView) view.findViewById(R.id.shop_distance);
        viewHolder.mShopRatingBarView = (ShopRatingBar) view.findViewById(R.id.shop_rating_bar);
        viewHolder.mShopAddressView = (TextView) view.findViewById(R.id.shop_address);
        viewHolder.mPay1View = (ImageView) view.findViewById(R.id.pay_1);
        viewHolder.mPay2View = (ImageView) view.findViewById(R.id.pay_2);
        viewHolder.mPay3View = (ImageView) view.findViewById(R.id.pay_3);
        viewHolder.mPay4View = (ImageView) view.findViewById(R.id.pay_4);
        viewHolder.mLookLaborCostView = (Button) view.findViewById(R.id.look_labor_cost);
        viewHolder.mShopNavigationView = (Button) view.findViewById(R.id.shop_navigation_btn);

        viewHolder.mShopNameView.setText((position + 1) + "." + pickupShop.shopTitle);
        viewHolder.mShopDistanceView.setText(OtherUtil.reformatDistance(pickupShop.distance));

        viewHolder.mShopRatingBarView.setData(pickupShop.score);
        viewHolder.mShopAddressView.setText(pickupShop.address);

        ImageView[] pays = new ImageView[]{viewHolder.mPay1View, viewHolder.mPay2View, viewHolder.mPay3View, viewHolder.mPay4View};
        for (int i = 0; i < pays.length; i++) {
            if (i < pickupShop.paymentID.size()) {
                pays[i].setVisibility(View.VISIBLE);
                ImageUtil.setImageSmall(mContext, pays[i], pickupShop.paymentID.get(i).thumb);
            } else {
                pays[i].setVisibility(View.GONE);
            }
        }

        viewHolder.mLookLaborCostView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickupShop.laborCost != null && pickupShop.laborCost.size() > 0) {
                    mLaborCostDialog.showDialog(pickupShop.laborCost);
                } else {
                    new AccessoryApiClient().getLaborCost(pickupShop.shopID, new LoadingAnimResponseHandler(mContext) {
                        @Override
                        public void onSuccess(String response) {
                            pickupShop.laborCost = JSONUtils.fromJson(response, new TypeToken< ArrayList< LaborCost > >() {
                            });
                            if (pickupShop.laborCost != null && pickupShop.laborCost.size() > 0) {
                                mLaborCostDialog.showDialog(pickupShop.laborCost);
                            } else {
                                ToastHelper.showYellowToast(mContext, "暂无工时费信息，具体工时费以线下为主");
                            }
                        }
                    });
                }
            }
        });

        viewHolder.mShopNavigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitchBase.toAMapNvai(mContext, new PickupPointT(pickupShop.address, pickupShop.latitude + "", pickupShop.longitude + "", pickupShop.shopTel));
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
        TextView mShopNameView;
        TextView mShopDistanceView;
        ShopRatingBar mShopRatingBarView;
        TextView mShopAddressView;
        ImageView mPay1View;
        ImageView mPay2View;
        ImageView mPay3View;
        ImageView mPay4View;
        Button mLookLaborCostView;
        Button mShopNavigationView;
    }
}
