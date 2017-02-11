package com.hxqc.mall.auto.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.model.NextMaintenance;
import com.hxqc.mall.auto.model.Record;
import com.hxqc.mall.auto.util.ActivitySwitchAutoInfo;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;


/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 10
 * Des: 车辆信息列表
 * FIXME
 * Todo
 */
public class RepairRecordAdapterV3 extends HFRecyclerView<Record> {

    private static final String TAG = "RepairRecordAdapterV3";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private ArrayList<Record> mData;
    private MyAuto myAutoV2;
    private NextMaintenance mNextMaintenance;

    private int defFlag = 0;

    public RepairRecordAdapterV3(Context context, ArrayList<Record> data, MyAuto myAutoV2, NextMaintenance nextMaintenance) {
        super(data, true, false);
        this.mContext = context;
        this.mData = data;
        this.myAutoV2 = myAutoV2;
        this.mNextMaintenance = nextMaintenance;
    }

    public void notifyData(ArrayList<Record> data, MyAuto myAutoV2, NextMaintenance nextMaintenance) {
        this.mData = data;
        this.myAutoV2 = myAutoV2;
        this.notifyDataSetChanged();
        this.mNextMaintenance = nextMaintenance;
    }

    @Override
    protected RecyclerView.ViewHolder getItemView(LayoutInflater inflater, ViewGroup parent) {
        return new ItemViewHolder(inflater.inflate(R.layout.item_repair_record, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderView(LayoutInflater inflater, ViewGroup parent) {
        return new HeaderViewHolder(inflater.inflate(R.layout.layout_repair_record_header, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getFooterView(LayoutInflater inflater, ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DebugLog.i(TAG, "position:" + position);
        if (holder instanceof ItemViewHolder) {
            /*if (position - 1 == defFlag) {
                ((ItemViewHolder) holder).itemView.setBackgroundColor(mContext.getResources().getColor(R.color.express_background));
                ((ItemViewHolder) holder).mTimelineImgView.setImageResource(R.drawable.ic_repair_record_sel);
            } else {
                ((ItemViewHolder) holder).itemView.setBackgroundColor(mContext.getResources().getColor(R.color.divider_line_bg));
                ((ItemViewHolder) holder).mTimelineImgView.setBackgroundColor(android.graphics.Color.parseColor("#EBEBEB"));
                ((ItemViewHolder) holder).mTimelineImgView.setImageResource(R.drawable.ic_repair_record_del);
            }*/

            ((ItemViewHolder) holder).mRepairDateView.setText(mData.get(position - 1).maintenancDate);
            ((ItemViewHolder) holder).mRepairPriceView.setText(OtherUtil.amountFormat(mData.get(position - 1).amount, false));
            ((ItemViewHolder) holder).mRepairMileageView.setText(mData.get(position - 1).distance + "km");
            ((ItemViewHolder) holder).mRepairContentView.setText(mData.get(position - 1).maintenanceItem);
            ((ItemViewHolder) holder).mRepair4sView.setText(mData.get(position - 1).shopName);

            /*if (TextUtils.isEmpty(mData.get(position - 1).workOrderID)) {
                ((ItemViewHolder) holder).mRepairOrderView.setVisibility(View.GONE);
            } else {
                workOrderDetailOnClick(holder, position);
            }*/

            to4SShop(holder, position);

        } else if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).mAutoTypeView.setText(myAutoV2.autoModel);
            ((HeaderViewHolder) holder).mLicenseNumView.setText(myAutoV2.plateNumber);
            ImageUtil.setImage(mContext, ((HeaderViewHolder) holder).mAutoLogView, myAutoV2.brandThumb);
//            yout mNextMaintenanceDistanceView;
//            private TextView mNextMaintenanceDistanceContentView;
//            private LinearLayout mMaintenanceItemsTotalView;
//            private TextView mMaintenanceItemsTotalContentView;
            if (mNextMaintenance != null) {
               /* if (TextUtils.isEmpty(mNextMaintenance.nextMaintenanceDistance) && TextUtils.isEmpty(mNextMaintenance.maintenanceItemsTotal)) {
                    ((HeaderViewHolder) holder).mOtherInfoView.setVisibility(View.GONE);
                } else {
                    ((HeaderViewHolder) holder).mOtherInfoView.setVisibility(View.VISIBLE);
                    ((HeaderViewHolder) holder).mNextMaintenanceDistanceView.setVisibility(View.VISIBLE);
                    ((HeaderViewHolder) holder).mNextMaintenanceDistanceContentView.setText(mNextMaintenance.nextMaintenanceDistance);
                    ((HeaderViewHolder) holder).mMaintenanceItemsTotalView.setVisibility(View.VISIBLE);
                    ((HeaderViewHolder) holder).mMaintenanceItemsTotalContentView.setText(mNextMaintenance.maintenanceItemsTotal);
                    ((HeaderViewHolder) holder).mRequestFailView.setVisibility(View.GONE);
                }*/

                /*if (TextUtils.isEmpty(mNextMaintenance.nextMaintenanceDistance)) {
                    ((HeaderViewHolder) holder).mNextMaintenanceDistanceView.setVisibility(View.GONE);
                } else {
                    ((HeaderViewHolder) holder).mOtherInfoView.setVisibility(View.VISIBLE);
                    ((HeaderViewHolder) holder).mNextMaintenanceDistanceView.setVisibility(View.VISIBLE);
                    ((HeaderViewHolder) holder).mNextMaintenanceDistanceContentView.setText(mNextMaintenance.nextMaintenanceDistance);
                    ((HeaderViewHolder) holder).mRequestFailView.setVisibility(View.GONE);
                }*/

                /*if (TextUtils.isEmpty(mNextMaintenance.maintenanceItemsTotal)) {
                    ((HeaderViewHolder) holder).mMaintenanceItemsTotalView.setVisibility(View.GONE);
                } else {
                    ((HeaderViewHolder) holder).mMaintenanceItemsTotalView.setVisibility(View.VISIBLE);
                    ((HeaderViewHolder) holder).mMaintenanceItemsTotalContentView.setText(mNextMaintenance.maintenanceItemsTotal);
                    ((HeaderViewHolder) holder).mRequestFailView.setVisibility(View.GONE);
                }*/
                if (mData.isEmpty()) {
                    ((HeaderViewHolder) holder).mRequestFailView.notShop("没有保养记录");
                } else {
                    ((HeaderViewHolder) holder).mRequestFailView.setVisibility(View.GONE);
                }

            } else {
                if (mData.isEmpty()) {
//                    ((HeaderViewHolder) holder).mOtherInfoView.setVisibility(View.GONE);
                    ((HeaderViewHolder) holder).mRequestFailView.notShop("没有保养记录");
                } else {
                    ((HeaderViewHolder) holder).mRequestFailView.setVisibility(View.GONE);
                }
            }
//            LookOnClick(holder);
        }
    }

    private void to4SShop(RecyclerView.ViewHolder holder, final int position) {
        ((ItemViewHolder) holder).mTo4SView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitchAutoInfo.toShopDetails(mContext,mData.get(position).shopID);
            }
        });
    }

    /*private void LookOnClick(RecyclerView.ViewHolder holder) {
        ((HeaderViewHolder) holder).mLookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //shop1585064992385057
//                ActivitySwitchBase.toShopDetails(mContext,"com.hxqc.mall.thirdshop.activity.ShopDetailsActivity","shop1585064992385057");
                ActivitySwitchBase.toMain(mContext, 0);
            }
        });
    }*/

    /*private void workOrderDetailOnClick(RecyclerView.ViewHolder holder, final int position) {
        ((ItemViewHolder) holder).mRepairOrderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ActivitySwitchBase.toH5Activity(mContext, "保养信息", "http://www.hxqc.com/");
//                AutoInfoControl.getInstance().workOrderDetail(mContext, mData.get(position-1).workOrderID, workOrderDetailHandler);
                DebugLog.i(TAG, "position " + position);
                ActivitySwitchBase.toH5Activity(mContext, "工单详情",
                        new UserApiClient().getWorkOrderUrl(mData.get(position - 1).workOrderID, mData.get(position - 1).erpShopCode));
            }
        });

    }*/

    /*AutoInfoControl.WorkOrderDetailHandler workOrderDetailHandler = new AutoInfoControl.WorkOrderDetailHandler() {
        @Override
        public void onWorkOrderDetailSucceed(String response) {

        }

        @Override
        public void onWorkOrderDetailFailed(boolean offLine) {

        }
    };*/


    class ItemViewHolder extends RecyclerView.ViewHolder {

//        private final ImageView mTimelineImgView;
        private final TextView mRepairDateView;
        private final TextView mRepairPriceView;
        private final TextView mRepairMileageView;
        private final TextView mRepairContentView;
        private final TextView mRepair4sView;
//        private final TextView mRepairOrderView;
        private final TextView mTo4SView;

        public ItemViewHolder(View itemView) {
            super(itemView);

//            mTimelineImgView = (ImageView) itemView.findViewById(R.id.repair_record_timeline_img);
            mRepairDateView = (TextView) itemView.findViewById(R.id.repair_record_repair_date);
            mRepairPriceView = (TextView) itemView.findViewById(R.id.repair_record_repair_price);
            mRepairMileageView = (TextView) itemView.findViewById(R.id.repair_record_repair_mileage);
            mRepairContentView = (TextView) itemView.findViewById(R.id.repair_record_repair_content);
            mRepair4sView = (TextView) itemView.findViewById(R.id.repair_record_4s);
//            mRepairOrderView = (TextView) itemView.findViewById(R.id.repair_record_order);
            mTo4SView = (TextView) itemView.findViewById(R.id.repair_record_to_4s);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private ImageView mAutoLogView;
        private TextView mAutoTypeView;
        private TextView mLicenseNumView;
        //        private Button mLookView;
//        private LinearLayout mNextMaintenanceDistanceView;
//        private TextView mNextMaintenanceDistanceContentView;
//        private LinearLayout mMaintenanceItemsTotalView;
//        private TextView mMaintenanceItemsTotalContentView;
        private RequestFailView mRequestFailView;
//        private RelativeLayout mOtherInfoView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
//            mLookView = (Button) itemView.findViewById(R.id.repair_record_look);
            mAutoLogView = (ImageView) itemView.findViewById(R.id.repair_record_auto_bg);
            mAutoTypeView = (TextView) itemView.findViewById(R.id.repair_record_auto);
            mLicenseNumView = (TextView) itemView.findViewById(R.id.repair_record_license_number);

//            mNextMaintenanceDistanceView = (LinearLayout) itemView.findViewById(R.id.repair_record_next_maintenance_distance);
//            mNextMaintenanceDistanceContentView = (TextView) itemView.findViewById(R.id.repair_record_next_maintenance_distance_content);
//            mMaintenanceItemsTotalView = (LinearLayout) itemView.findViewById(R.id.repair_record_maintenance_items_total);
//            mMaintenanceItemsTotalContentView = (TextView) itemView.findViewById(R.id.repair_record_maintenance_items_total_content);

            mRequestFailView = (RequestFailView) itemView.findViewById(R.id.repair_record_fail_view);

//            mOtherInfoView = (RelativeLayout) itemView.findViewById(R.id.repair_record_other_info);
        }
    }
}
