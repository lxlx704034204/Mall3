package com.hxqc.fastreqair.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.fastreqair.model.CarWashShopListBean;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.maintenance.adapter.MaintenanceShopListAdapter;
import com.hxqc.mall.thirdshop.views.ShopRatingBar;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Function: 洗车店铺列表数据Adapter
 *
 * @author 袁秉勇
 * @since 2016年05月16日
 */
public class CarWashShopListAdapter extends RecyclerView.Adapter< CarWashShopListAdapter.ViewHolder > {
    private final static String TAG = MaintenanceShopListAdapter.class.getSimpleName();
    private Context mContext;

    private boolean showDistance = false;

    ArrayList< CarWashShopListBean > list = new ArrayList<>();


    public void setShowDistance(boolean showDistance) {
        this.showDistance = showDistance;
    }


    public CarWashShopListAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_car_wash_shop_list, null));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CarWashShopListBean carWashShopListBean = list.get(position);

        if (carWashShopListBean.shopPhoto != null && carWashShopListBean.shopPhoto.size() > 0
                && !TextUtils.isEmpty(carWashShopListBean.shopPhoto.get(0))) {
            ImageUtil.setImage(mContext, holder.mShopImageView, carWashShopListBean.shopPhoto.get(0));
        } else {
            holder.mShopImageView.setImageResource(R.drawable.pic_normal);
        }

        if (carWashShopListBean.used) {
            holder.mUsedLogoView.setVisibility(View.VISIBLE);
            holder.mLineView.setVisibility(View.GONE);
            holder.mShadowView.setVisibility(View.VISIBLE);
        } else {
            holder.mUsedLogoView.setVisibility(View.GONE);
            holder.mLineView.setVisibility(View.VISIBLE);
            holder.mShadowView.setVisibility(View.GONE);
        }

        holder.mShopNameView.setText(carWashShopListBean.shopTitle);
//        if (showDistance) {
            holder.mShopDistanceView.setText(OtherUtil.reformatDistance(carWashShopListBean.distance));
//            holder.mShopDistanceView.setVisibility(View.VISIBLE);
//        } else {
//            holder.mShopDistanceView.setVisibility(View.GONE);
//        }

        holder.mShopPriceView.setText(OtherUtil.formatPriceString(carWashShopListBean.amount, "-", "—", false));

        holder.mOrderNumView.setText(carWashShopListBean.orderCount+"");

        holder.mRatingBarView.setData(carWashShopListBean.star > 5 ? 5 : carWashShopListBean.star);

        if (carWashShopListBean.workstatus == 10) {
            holder.mWorkStateView.setVisibility(View.VISIBLE);
        } else {
            holder.mWorkStateView.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData(ArrayList< CarWashShopListBean > itemList) {
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

    public CarWashShopListBean getItemData(int position) {

        if (list != null) {
            return list.get(position);
        } else {
            return null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mContentView;
        ImageView mShopImageView;
        ImageView mUsedLogoView;
        TextView mShopNameView;
        TextView mShopDistanceView;
        TextView mShopPriceView;
        ShopRatingBar mRatingBarView;
        TextView mOrderNumView;
        View mLineView;
        View mShadowView;
        ImageView mWorkStateView;

        public ViewHolder(View itemView) {
            super(itemView);

            mContentView = (LinearLayout) itemView.findViewById(R.id.content_view);
            mShopImageView = (ImageView) itemView.findViewById(R.id.shop_image);
            mUsedLogoView = (ImageView) itemView.findViewById(R.id.used_logo);
            mShopNameView = (TextView) itemView.findViewById(R.id.shop_name);
            mShopDistanceView = (TextView) itemView.findViewById(R.id.shop_distance);
            mShopPriceView = (TextView) itemView.findViewById(R.id.shop_price);
            mRatingBarView = (ShopRatingBar) itemView.findViewById(R.id.shop_rating_bar);
            mOrderNumView = (TextView) itemView.findViewById(R.id.order_number);
            mLineView = itemView.findViewById(R.id.line);
            mShadowView = itemView.findViewById(R.id.shadow);
            mWorkStateView = (ImageView) itemView.findViewById(R.id.shop_work_state);
        }
    }
}
