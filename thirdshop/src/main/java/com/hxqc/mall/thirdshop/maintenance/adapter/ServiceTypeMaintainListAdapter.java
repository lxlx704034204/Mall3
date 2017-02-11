package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 08
 * FIXME
 * Todo 服务类型-维修列表
 */
@Deprecated
public class ServiceTypeMaintainListAdapter extends RecyclerView.Adapter<ServiceTypeMaintainListAdapter.ServiceTypeMaintainListViewHolder>{

    private final String[] mRepairDatas;
    private LayoutInflater mLayoutInflater;

    public ServiceTypeMaintainListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mRepairDatas = context.getResources().getStringArray(R.array.service_type_repair_list);
    }

    @Override
    public ServiceTypeMaintainListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_service_type_child, parent, false);
        ServiceTypeMaintainListViewHolder serviceTypeMaintainListViewHolder = new ServiceTypeMaintainListViewHolder(view);
        return serviceTypeMaintainListViewHolder;
    }

    @Override
    public void onBindViewHolder(ServiceTypeMaintainListViewHolder holder, int position) {
        holder.mRepairItemContentView.setText(mRepairDatas[position]);
    }

    @Override
    public int getItemCount() {
        return mRepairDatas.length;
    }

    class ServiceTypeMaintainListViewHolder extends RecyclerView.ViewHolder {


//        private final SelectableRoundedImageView mRepairItemImgView;
        private final TextView mRepairItemContentView;

        public ServiceTypeMaintainListViewHolder(View itemView) {
            super(itemView);
//            mRepairItemImgView = (SelectableRoundedImageView) itemView.findViewById(R.id.service_type_repair_item_img);
            mRepairItemContentView = (TextView) itemView.findViewById(R.id.service_type_repair_item_content);
        }
    }
}
