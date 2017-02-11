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
 * Todo 钣喷修复-工序列表
 */
@Deprecated
public class SprayRepairContentAdapter extends RecyclerView.Adapter<SprayRepairContentAdapter.SprayRepairContentViewHolder> {

    private final String[] stringArray;
    private LayoutInflater mLayoutInflater;

    public SprayRepairContentAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        stringArray = context.getResources().getStringArray(R.array.spray_9_process);
    }

    @Override
    public SprayRepairContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_spray_repair, parent, false);
        SprayRepairContentViewHolder recordEditViewHolder = new SprayRepairContentViewHolder(view);
        return recordEditViewHolder;
    }

    @Override
    public void onBindViewHolder(SprayRepairContentViewHolder holder, int position) {
        holder.mSprayRepairFlagView.setText(position + 1 + "");
        holder.mSprayRepairContentView.setText(stringArray[position]);
    }

    @Override
    public int getItemCount() {
        return stringArray.length;
    }

    class SprayRepairContentViewHolder extends RecyclerView.ViewHolder {

        private final TextView mSprayRepairFlagView;
        private final TextView mSprayRepairContentView;

        public SprayRepairContentViewHolder(View itemView) {
            super(itemView);
            mSprayRepairFlagView = (TextView) itemView.findViewById(R.id.spray_repair_flag);
            mSprayRepairContentView = (TextView) itemView.findViewById(R.id.spray_repair_content);
        }
    }
}
