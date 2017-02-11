package com.hxqc.newenergy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.core.views.CustomRecyclerView;
import com.hxqc.newenergy.activity.Ev_ModelAndSubsidyActivity;
import com.hxqc.newenergy.bean.ModelAndSubsidy;
import com.hxqc.newenergy.control.FilterController;
import com.hxqc.newenergy.util.ActivitySwitcherEV;
import com.hxqc.newenergy.view.ExpandableView;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Function: 车辆类型及补贴资料页面Adapter
 *
 * @author 袁秉勇
 * @since 2016年03月11日
 */
public class EV_ModelSubsidyAdapter extends RecyclerView.Adapter< EV_ModelSubsidyAdapter.ViewHolder > {
    private final static String TAG = EV_ModelSubsidyAdapter.class.getSimpleName();
    private Context mContext;

    private Ev_ModelAndSubsidyActivity relatedActivity;

    private ArrayList< ModelAndSubsidy > modelAndSubsidies = new ArrayList<>();

    private ArrayList< Boolean > booleans;


    public EV_ModelSubsidyAdapter(Context context) {
        this.mContext = context;
        if (mContext instanceof Ev_ModelAndSubsidyActivity) {
            relatedActivity = (Ev_ModelAndSubsidyActivity) context;
        }

        booleans = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            booleans.add(false);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DebugLog.e(TAG, " ---------- onCreateViewHolder ");
        View view = LayoutInflater.from(mContext).inflate(R.layout.ev_modelsandsubsidy_item, null);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        DebugLog.e(TAG, " ----------- onBindViewHolder ");
        holder.expandableView.initData(modelAndSubsidies.get(position));

        if (booleans.get(position) == false) {
            holder.expandableView.getContentLayout().setVisibility(View.GONE);
            holder.expandableView.getRightIcon().setImageResource(R.drawable.ph_jiantou02);
        } else {
            holder.expandableView.getContentLayout().setVisibility(View.VISIBLE);
            holder.expandableView.getRightIcon().setImageResource(R.drawable.ph_jiantou01);
        }

        holder.expandableView.getmMoreInfomationView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherEV.toModelAndSubsidyDetail(mContext, modelAndSubsidies.get(position).autoID, FilterController.getInstance().mFilterMap.get("area"));
            }
        });

        holder.expandableView.getClickableLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.expandableView.doClick();

                booleans.set(position, !booleans.get(position));

                if (relatedActivity != null) {
                    relatedActivity.getRecyclerView().setScrollType(CustomRecyclerView.SMOOTHSCROLL);
                    relatedActivity.getRecyclerView().move(position);
                }
            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        DebugLog.e(TAG, " ------------- getItemViewType ");
        return super.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
        DebugLog.e(TAG, " ------------- getItemCount ");
        return modelAndSubsidies.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ExpandableView expandableView;


        public ViewHolder(View itemView) {
            super(itemView);

            expandableView = (ExpandableView) itemView.findViewById(R.id.expandable_view);
        }
    }


    public void addData(ArrayList< ModelAndSubsidy > modelAndSubsidies) {
        if (this.modelAndSubsidies != null) {
            this.modelAndSubsidies.addAll(modelAndSubsidies);
        } else {
            this.modelAndSubsidies = modelAndSubsidies;
        }
        notifyDataSetChanged();

        for (int i = 0; i < modelAndSubsidies.size(); i++) {
            booleans.add(false);
        }
    }


    public void refreshData(ArrayList< ModelAndSubsidy > modelAndSubsidies) {
        if (this.modelAndSubsidies != null && !this.modelAndSubsidies.isEmpty()) {
            this.modelAndSubsidies.clear();
        }
        if (this.modelAndSubsidies.addAll(modelAndSubsidies)) notifyDataSetChanged();

        if (this.booleans != null && !this.booleans.isEmpty()) {
            this.booleans.clear();
        }

        for (int i = 0; i < modelAndSubsidies.size(); i++) {
            this.booleans.add(false);
        }
    }
}
