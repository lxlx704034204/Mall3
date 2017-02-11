package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.views.autotext.AutofitTextView;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.ThirdOrderModel;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;

import java.util.ArrayList;


/**
 * Function:第三方店铺订单列表Adapter
 *
 * @author 袁秉勇
 * @since 2015年11月30日
 */
public class ThirdShopOrderAdapter extends RecyclerView.Adapter {
    ArrayList< ThirdOrderModel > thirdOrderModels = new ArrayList< ThirdOrderModel >();
    Context mContext;

    public ThirdShopOrderAdapter(ArrayList< ThirdOrderModel > thirdOrderModels, Context context) {
        this.thirdOrderModels = thirdOrderModels;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.t_third_order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ((ViewHolder) holder).dividerLineView.setVisibility(View.GONE);
        } else {
            ((ViewHolder) holder).dividerLineView.setVisibility(View.VISIBLE);
        }
        final ThirdOrderModel thirdOrderModel = thirdOrderModels.get(position);
        ((ViewHolder) holder).mOrderIDView.setText(String.format("%s%s", mContext.getResources().getString(R.string
                .order_number), thirdOrderModel.orderID));

        showOrderOperateView((ViewHolder) holder, thirdOrderModel);

        ImageUtil.setImage(mContext, ((ViewHolder) holder).mOrderImageView, thirdOrderModel.promotion.thumb);
        ((ViewHolder) holder).mFavorableNameView.setText(thirdOrderModel.promotion.title);
        ((ViewHolder) holder).mFavorableTimeView.setText(String.format("%s 至 %s", thirdOrderModel.promotion
                .startDate, thirdOrderModel.promotion.endDate));
        ((ViewHolder) holder).mOrderDeatailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherThirdPartShop.toOrderDetail(mContext,thirdOrderModel.orderID);
            }
        });
    }

    @Override
    public int getItemCount() {
        return thirdOrderModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View dividerLineView;
        AutofitTextView mOrderIDView;
        TextView mOrderStatusView;
        ImageView mOrderImageView;
        TextView mFavorableNameView;
        TextView mFavorableTimeView;
        LinearLayout mOrderDeatailView;
        Button mOrderStatusButtonView;
        LinearLayout mOrderStatusBottomView;

        public ViewHolder(View itemView) {
            super(itemView);
            dividerLineView = itemView.findViewById(R.id.divider_line);
            mOrderIDView = (AutofitTextView) itemView.findViewById(R.id.order_id);
            mOrderStatusView = (TextView) itemView.findViewById(R.id.order_status);
            mOrderImageView = (ImageView) itemView.findViewById(R.id.third_order_item_image);
            mFavorableNameView = (TextView) itemView.findViewById(R.id.third_order_item_favorable_name);
            mFavorableTimeView = (TextView) itemView.findViewById(R.id.third_order_item_favorable_time);
            mOrderDeatailView = (LinearLayout) itemView.findViewById(R.id.third_order_detail);
            mOrderStatusButtonView = (Button) itemView.findViewById(R.id.order_status_button);
            mOrderStatusBottomView = (LinearLayout) itemView.findViewById(R.id.order_status_bottom);
        }
    }

    public void showOrderOperateView(ViewHolder holder, final ThirdOrderModel thirdOrderModel) {
        holder.mOrderStatusView.setVisibility(View.GONE);
        holder.mOrderStatusBottomView.setVisibility(View.GONE);

        if (thirdOrderModel.orderStatusCode == 0) {
            //1. 判断活动是否过期 2.判断活动是否下线 3.判断活动是否可支付
//            if (OtherUtil.date2TimeStamp(thirdOrderModel.promotion.getEndDateByTime(), "yyyy-MM-dd HH:mm:ss") <
// OtherUtil.date2TimeStamp(thirdOrderModel.promotion.serverTime, "yyyy-MM-dd HH:mm:ss") || "30".equals
// (thirdOrderModel.promotion.status) || !thirdOrderModel.promotion.getPaymentAvailable()) {
            if (OtherUtil.date2TimeStamp(thirdOrderModel.promotion.getEndDateByTime(), "yyyy-MM-dd HH:mm:ss") <
                    OtherUtil.date2TimeStamp(thirdOrderModel.promotion.serverTime, "yyyy-MM-dd HH:mm:ss") || "30"
                    .equals(thirdOrderModel.promotion.status)) {
                holder.mOrderStatusView.setVisibility(View.VISIBLE);
                holder.mOrderStatusView.setText("已关闭");
                holder.mOrderStatusView.setTextColor(mContext.getResources().getColor(R.color.main_and_price));
                return;
            }
            //满足以上条件就显示付款按钮
            holder.mOrderStatusView.setVisibility(View.VISIBLE);
            holder.mOrderStatusView.setText("待付款");
            holder.mOrderStatusView.setTextColor(mContext.getResources().getColor(R.color.main_and_price));
            holder.mOrderStatusBottomView.setVisibility(View.VISIBLE);
            holder.mOrderStatusButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivitySwitcherThirdPartShop.toPayDeposit(thirdOrderModel.subscription, thirdOrderModel.orderID,
                            thirdOrderModel.shopTel, mContext);
                }
            });
        } else if (thirdOrderModel.orderStatusCode == 10 || thirdOrderModel.orderStatusCode == 20) {
            holder.mOrderStatusView.setVisibility(View.VISIBLE);
            holder.mOrderStatusView.setTextColor(mContext.getResources().getColor(R.color.main_and_price));
            holder.mOrderStatusView.setText("已付款");
        } else if (thirdOrderModel.orderStatusCode == -40) {
            holder.mOrderStatusView.setVisibility(View.VISIBLE);
            holder.mOrderStatusView.setText("已关闭");
            holder.mOrderStatusView.setTextColor(mContext.getResources().getColor(R.color.main_and_price));
        } else {
            holder.mOrderStatusView.setVisibility(View.VISIBLE);
            holder.mOrderStatusView.setText(thirdOrderModel.paymentStatus);
            holder.mOrderStatusView.setTextColor(mContext.getResources().getColor(R.color.main_and_price));
        }
    }
}
