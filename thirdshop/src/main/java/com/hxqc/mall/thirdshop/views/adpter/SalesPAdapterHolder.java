package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.promotion.SalesPModel;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.util.DebugLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Author: wanghao
 * Date: 2015-11-30
 * FIXME      促销列表
 * Todo
 */
public class SalesPAdapterHolder extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    private ArrayList<SalesPModel> mData;

    public SalesPAdapterHolder(ArrayList<SalesPModel> mData) {
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View sView = mInflater.inflate(R.layout.t_item_sales_p_view, parent, false);
        return new VHolderSales(sView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final VHolderSales holderSales = (VHolderSales) holder;
        final SalesPModel salesPModel = mData.get(position);

        ImageUtil.setImage(context, holderSales.mSaleImg, salesPModel.thumb);
        holderSales.mTitle.setText(salesPModel.title);
        holderSales.mPostTime.setText(salesPModel.publishDate);
/**
 * 判断 促销状态的显示
 */
        String sDate = lastDate(salesPModel.serverTime, salesPModel.endDate);
        DebugLog.i("pom_tag", "onBindViewHolder sDate: " + sDate);
        if (sDate.equals("-1")) {
            DebugLog.i("pom_tag", "活动已结束  ");
            holderSales.mDTStartTag.setText("活动已结束");
            holderSales.mDTEndTag.setVisibility(View.GONE);
            holderSales.mDeadTime.setVisibility(View.GONE);
        } else {
            DebugLog.i("pom_tag", "有活动 sDate: " + sDate);
            holderSales.mDeadTime.setVisibility(View.VISIBLE);
            holderSales.mDTEndTag.setVisibility(View.VISIBLE);
            holderSales.mDeadTime.setText(sDate);
            holderSales.mDTStartTag.setText("距促销结束：");
        }

        //-------------------------------------------------------------------------------------------------

        holderSales.mInfo.setText(salesPModel.summary);

        holderSales.mClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (salesPModel.type == SalesPModel.MAINTENANCE_P) {
//                    ActivitySwitchMaintenance.toMaintenancePromotion(salesPModel.promotionID, context);
//                } else {
//                    ActivitySwitcherThirdPartShop.toSalesItemDetail(salesPModel.promotionID, context);
//                }
                ActivitySwitcherThirdPartShop.toSalesItemDetail(salesPModel.promotionID, context);

            }
        });

    }

    public class VHolderSales extends RecyclerView.ViewHolder {
        RelativeLayout mClick;
        ImageView mSaleImg;
        TextView mTitle;
        TextView mPostTime;
        TextView mDTStartTag;
        TextView mDeadTime;
        TextView mDTEndTag;
        TextView mInfo;

        public VHolderSales(View itemView) {
            super(itemView);
            mClick = (RelativeLayout) itemView.findViewById(R.id.rl_sales_item_click_view);
            mSaleImg = (ImageView) itemView.findViewById(R.id.iv_s_p_item);
            mTitle = (TextView) itemView.findViewById(R.id.tv_s_p_title);
            mPostTime = (TextView) itemView.findViewById(R.id.tv_s_p_post_time);
            mDTStartTag = (TextView) itemView.findViewById(R.id.sales_p_label_tips);
            mDTEndTag = (TextView) itemView.findViewById(R.id.tv_s_p_tips_end);
            mDeadTime = (TextView) itemView.findViewById(R.id.tv_s_p_deadline_time);
            mInfo = (TextView) itemView.findViewById(R.id.tv_s_p_info);
        }

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    private String lastDate(String currentDate, String endDate) {
        DebugLog.i("pom_tag", "currentData: " + currentDate + " endDate: " + endDate);
        try {
            double dDate = change2Timestamp(endDate + " 23:59:59") - change2Timestamp(currentDate);
            DebugLog.i("pom_tag", "dDate: " + dDate);
            if (dDate >= 0) {
                DebugLog.i("test_time", dDate + "");
                dDate = dDate / (3600 * 24 * 1000.00);
                return (int) Math.ceil(dDate) + "";
            } else {
                return "-1";
            }
        } catch (Exception e) {
            return "-1";
        }

    }

    /**
     * 转换成时间戳
     *
     * @param date
     * @return
     */
    private long change2Timestamp(String date) {
        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date d = simpleDateFormat.parse(date);
            return d.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
