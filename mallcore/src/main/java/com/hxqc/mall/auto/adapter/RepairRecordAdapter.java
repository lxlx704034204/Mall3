package com.hxqc.mall.auto.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.core.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 10
 * FIXME
 * Todo
 */
@Deprecated
public class RepairRecordAdapter extends RecyclerView.Adapter<RepairRecordAdapter.RepairRecordViewHolder> {

    private LayoutInflater mLayoutInflater;

    public RepairRecordAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RepairRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_repair_record, parent, false);
        RepairRecordViewHolder repairRecordViewHolder = new RepairRecordViewHolder(view);
        return repairRecordViewHolder;
    }

    @Override
    public void onBindViewHolder(RepairRecordViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
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
