package com.hxqc.mall.auto.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.auto.model.Record;
import com.hxqc.mall.core.R;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 10
 * FIXME
 * Todo 车辆信息列表
 */
@Deprecated
public class RepairRecordAdapterV2 extends BaseRecyclerAdapter<Record> implements View.OnClickListener {

    private LayoutInflater mLayoutInflater;
    private Context mContext;

    private int defFlag = 0;

    public RepairRecordAdapterV2(Context context, ArrayList mDatas) {
        super(mDatas);
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_repair_record, parent, false);
        RepairRecordViewHolder repairRecordViewHolder = new RepairRecordViewHolder(view);
        return repairRecordViewHolder;
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, Record data) {
        /*if (RealPosition == defFlag) {
            ((RepairRecordViewHolder) viewHolder).itemView.setBackgroundColor(mContext.getResources().getColor(R.color.express_background));
            ((RepairRecordViewHolder) viewHolder).mTimelineImgView.setImageResource(R.drawable.ic_repair_record_sel);
        } else {
            ((RepairRecordViewHolder) viewHolder).itemView.setBackgroundColor(mContext.getResources().getColor(R.color.divider_line_bg));
            ((RepairRecordViewHolder) viewHolder).mTimelineImgView.setBackgroundColor(android.graphics.Color.parseColor("#EBEBEB"));
            ((RepairRecordViewHolder) viewHolder).mTimelineImgView.setImageResource(R.drawable.ic_repair_record_del);
        }*/

//        ((RepairRecordViewHolder) viewHolder).mRepairDateView.setText(data.maintenancDate);
//        ((RepairRecordViewHolder) viewHolder).mRepairPriceView.setText(data.amount);
//        ((RepairRecordViewHolder) viewHolder).mRepairMileageView.setText(data.distance);
//        ((RepairRecordViewHolder) viewHolder).mRepairContentView.setText(data.maintenanceItem);
//        ((RepairRecordViewHolder) viewHolder).mRepair4sView.setText(data.shopName);
//
//        ((RepairRecordViewHolder) viewHolder).mRepairOrderView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        /*if (v.getId() == R.id.repair_record_order) {
            ActivitySwitchBase.toH5Activity(mContext, "保养信息", "http://www.hxqc.com/");
        }*/

    }

    class RepairRecordViewHolder extends RecyclerView.ViewHolder {

//        private final ImageView mTimelineImgView;
//        private final TextView mRepairDateView;
//        private final TextView mRepairPriceView;
//        private final TextView mRepairMileageView;
//        private final TextView mRepairContentView;
//        private final TextView mRepair4sView;
//        private final TextView mRepairOrderView;

        public RepairRecordViewHolder(View itemView) {
            super(itemView);
//            mTimelineImgView = (ImageView) itemView.findViewById(R.id.repair_record_timeline_img);
//            mRepairDateView = (TextView) itemView.findViewById(R.id.repair_record_repair_date);
//            mRepairPriceView = (TextView) itemView.findViewById(R.id.repair_record_repair_price);
//            mRepairMileageView = (TextView) itemView.findViewById(R.id.repair_record_repair_mileage);
//            mRepairContentView = (TextView) itemView.findViewById(R.id.repair_record_repair_content);
//            mRepair4sView = (TextView) itemView.findViewById(R.id.repair_record_4s);
//            mRepairOrderView = (TextView) itemView.findViewById(R.id.repair_record_order);

        }
    }
}
