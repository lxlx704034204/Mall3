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
 * Todo 检查故障列表
 */
public class InspectListAdapter extends RecyclerView.Adapter<InspectListAdapter.InspectListViewHolder>{

    private LayoutInflater mLayoutInflater;

    public InspectListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public InspectListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_inspect,parent,false);
        InspectListViewHolder inspectListViewHolder = new InspectListViewHolder(view);
        return inspectListViewHolder;
    }

    @Override
    public void onBindViewHolder(InspectListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    class InspectListViewHolder extends RecyclerView.ViewHolder{


        private final TextView mSolutionView;
        private final TextView mEstimatedCostView;
        private final TextView mTimeView;

        public InspectListViewHolder(View itemView) {
            super(itemView);
            mSolutionView = (TextView) itemView.findViewById(R.id.inspect_solution_content);
            mEstimatedCostView = (TextView) itemView.findViewById(R.id.inspect_estimated_cost_content);
            mTimeView = (TextView) itemView.findViewById(R.id.inspect_time);
        }
    }
}
