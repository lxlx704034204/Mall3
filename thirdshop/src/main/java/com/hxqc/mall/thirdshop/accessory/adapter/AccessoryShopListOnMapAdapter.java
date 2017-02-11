package com.hxqc.mall.thirdshop.accessory.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.adapter.BaseMapListAdapter;
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
 * Function: 地图选店列表适配器Adapter（用品模块）
 *
 * @author 袁秉勇
 * @since 2016年06月06日
 */
public class AccessoryShopListOnMapAdapter extends BaseMapListAdapter {
    private final static String TAG = AccessoryShopListOnMapAdapter.class.getSimpleName();
    private Context mContext;

    private LaborCostDialog mLaborCostDialog;
    private ArrayList<PickupShop> list = new ArrayList<>();


    public AccessoryShopListOnMapAdapter(Context mContext) {
        this.mContext = mContext;
        this.mLaborCostDialog = new LaborCostDialog(mContext);
    }


    public void setData(ArrayList<PickupShop> pickupShops) {
        if (this.list.size() > 0) {
            this.list.clear();
        }
        this.list.addAll(pickupShops);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public PickupShop getItem(int position) {
        return list.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PickupShop pickupShop = getItem(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shop_list_on_map_accessory, null);
            viewHolder = new ViewHolder();
            viewHolder.mShopNameView = (TextView) convertView.findViewById(R.id.shop_name);
            viewHolder.mShopDistanceView = (TextView) convertView.findViewById(R.id.shop_distance);
            viewHolder.mShopRatingBarView = (ShopRatingBar) convertView.findViewById(R.id.shop_rating_bar);
            viewHolder.mShopAddressView = (TextView) convertView.findViewById(R.id.shop_address);
            viewHolder.mPay1View = (ImageView) convertView.findViewById(R.id.pay_1);
            viewHolder.mPay2View = (ImageView) convertView.findViewById(R.id.pay_2);
            viewHolder.mPay3View = (ImageView) convertView.findViewById(R.id.pay_3);
            viewHolder.mPay4View = (ImageView) convertView.findViewById(R.id.pay_4);
            viewHolder.mLookLaborCostView = (Button) convertView.findViewById(R.id.look_labor_cost);
            viewHolder.mShopNavigationView = (Button) convertView.findViewById(R.id.shop_navigation_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == selectedPosition) {
            convertView.setBackgroundColor(Color.parseColor("#E6F3F9"));
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }

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
                            pickupShop.laborCost = JSONUtils.fromJson(response, new TypeToken<ArrayList<LaborCost>>() {
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
        return convertView;
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
