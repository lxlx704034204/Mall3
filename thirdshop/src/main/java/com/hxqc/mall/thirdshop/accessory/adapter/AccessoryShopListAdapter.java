package com.hxqc.mall.thirdshop.accessory.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.api.AccessoryApiClient;
import com.hxqc.mall.thirdshop.accessory.fragment.AccessoryShopListFragment;
import com.hxqc.mall.thirdshop.accessory.model.LaborCost;
import com.hxqc.mall.thirdshop.accessory.model.PickupShop;
import com.hxqc.mall.thirdshop.accessory.views.LaborCostDialog;
import com.hxqc.mall.thirdshop.maintenance.model.Payment;
import com.hxqc.mall.thirdshop.views.ShopRatingBar;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.GridViewNoSlide;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;

/**
 * 说明:订单中的商品
 *
 * @author: 吕飞
 * @since: 2016-02-19
 * Copyright:恒信汽车电子商务有限公司
 */
public class AccessoryShopListAdapter extends BaseAdapter {
    ArrayList<PickupShop> mPickupShops;
    Context mContext;
    LayoutInflater mLayoutInflater;
    LaborCostDialog mLaborCostDialog;

    public AccessoryShopListAdapter(Context context, ArrayList<PickupShop> pickupShops, LaborCostDialog laborCostDialog) {
        mContext = context;
        mPickupShops = pickupShops;
        mLaborCostDialog = laborCostDialog;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mPickupShops.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_accessory_shop_list, null);
            viewHolder = new ViewHolder();
            viewHolder.mShopImageView = (ImageView) convertView.findViewById(R.id.shop_image);
            viewHolder.mUsedLogoView = (ImageView) convertView.findViewById(R.id.used_logo);
            viewHolder.mShopNameView = (TextView) convertView.findViewById(R.id.shop_name);
            viewHolder.mShopDistanceView = (TextView) convertView.findViewById(R.id.shop_distance);
            viewHolder.mShopRatingBarView = (ShopRatingBar) convertView.findViewById(R.id.shop_rating_bar);
            viewHolder.mPaymentGridView = (GridViewNoSlide) convertView.findViewById(R.id.payment_grid);
            viewHolder.mLookLaborCostView = (TextView) convertView.findViewById(R.id.look_labor_cost);
            viewHolder.mCompanyInformationView = (TextView) convertView.findViewById(R.id.company_information);
            viewHolder.mLineView = convertView.findViewById(R.id.line);
            viewHolder.mShadowView = convertView.findViewById(R.id.shadow);
            viewHolder.mRootView= (LinearLayout) convertView.findViewById(R.id.root);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final PickupShop pickupShop = mPickupShops.get(position);
        ImageUtil.setImage(mContext, viewHolder.mShopImageView, pickupShop.shopPhoto);
//        OtherUtil.setVisible(viewHolder.mUsedLogoView, position == 0);
        viewHolder.mShopNameView.setText(pickupShop.shopTitle);
        viewHolder.mShopDistanceView.setText(OtherUtil.reformatDistance(pickupShop.distance));
        viewHolder.mShopRatingBarView.setData(pickupShop.score);
        QuickAdapter<Payment> quickAdapter = new QuickAdapter<Payment>(mContext, R.layout.item_shop_payment, pickupShop.paymentID) {
            @Override
            protected void convert(BaseAdapterHelper helper, Payment item) {
                ImageUtil.setImageSmall(mContext, (ImageView) (helper.getView(R.id.pay_icon)), item.thumb);
            }
        };
        viewHolder.mPaymentGridView.setAdapter(quickAdapter);
        viewHolder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(AccessoryShopListFragment.CHOOSE_SHOP_ID, mPickupShops.get(position).shopID);
                intent.putExtra(AccessoryShopListFragment.CHOOSE_SHOP_NAME, mPickupShops.get(position).shopTitle);
                ((Activity)mContext).setResult(Activity.RESULT_OK, intent);
                ((Activity)mContext).finish();
            }
        });
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
        viewHolder.mCompanyInformationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitchBase.toH5Activity(mContext, "企业信息", pickupShop.queryShop);
            }
        });
//        OtherUtil.setVisible(viewHolder.mLineView, !(position == 0));
//        OtherUtil.setVisible(viewHolder.mShadowView, position == 0);
        return convertView;
    }

    class ViewHolder {
        ImageView mShopImageView;
        ImageView mUsedLogoView;
        TextView mShopNameView;
        TextView mShopDistanceView;
        ShopRatingBar mShopRatingBarView;
        GridViewNoSlide mPaymentGridView;
        TextView mLookLaborCostView;
        TextView mCompanyInformationView;
        View mLineView;
        View mShadowView;
        LinearLayout mRootView;
    }
}
