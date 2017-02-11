package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.model.ApppintmentDateNew;

import java.util.List;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 10
 * Des: 预约时间-小时
 * FIXME
 * Todo
 */
public class ReserveTimeAdapter extends RecyclerView.Adapter<ReserveTimeAdapter.ReserveDateViewHolder> {

    private static final String TAG = AutoInfoContants.LOG_J;
    private List<String[]> timeIntervals;
    private List<ApppintmentDateNew> mTimeIntervalsNew;
    public OnTimeItemClickListener mOnTimeItemClickListener;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int defFlag = 0;

    public ReserveTimeAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
//        timeIntervals = CalendarUtil.getTimeInterval();
    }

    public ReserveTimeAdapter(Context context, List<String[]> timeIntervals) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.timeIntervals = timeIntervals;
    }

    public void setOnTimeItemClickListener(OnTimeItemClickListener l) {
        this.mOnTimeItemClickListener = l;
    }

    public void setData(List<String[]> timeIntervals) {
        this.timeIntervals = timeIntervals;
    }

    public void notifyData(List<String[]> timeIntervals) {
        this.timeIntervals = timeIntervals;
        notifyDataSetChanged();
    }

    public void setDataNew(List<ApppintmentDateNew> timeIntervals) {
        this.mTimeIntervalsNew = timeIntervals;
        try {
            for (int i = 0; i < mTimeIntervalsNew.size(); i++) {
                if (mTimeIntervalsNew.get(i).enable != 0) {
                    defFlag = i;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void notifyDataNew(List<ApppintmentDateNew> timeIntervals) {
        this.mTimeIntervalsNew = timeIntervals;
        notifyDataSetChanged();
    }

    @Override
    public ReserveDateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_reserve_date, parent, false);
        ReserveDateViewHolder reserveDateViewHolder = new ReserveDateViewHolder(view);
        return reserveDateViewHolder;
    }

    @Override
    public void onBindViewHolder(ReserveDateViewHolder holder, int position) {
        holder.mReserveDateTimeView.setVisibility(View.VISIBLE);
        holder.mReserveDateWeekView.setVisibility(View.GONE);
        holder.mReserveDateCommenView.setVisibility(View.GONE);
//        holder.mReserveDateTimeView.setText(timeIntervals.get(position)[1]);
        holder.mReserveDateTimeView.setText(mTimeIntervalsNew.get(position).time.substring(11,mTimeIntervalsNew.get(position).time.length()));
  /*      if(timeIntervals.get(position).length()>5) {
            holder.mReserveDateTimeView.setText(timeIntervals.get(position)[1]);
        }else {
            holder.mReserveDateTimeView.setText(timeIntervals.get(position));
        }*/
        if (mTimeIntervalsNew.get(position).enable == 0) {
            holder.mReserveDateTimeView.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.itemView.setBackgroundResource(R.color.reserve_date_no);
        } else if (mTimeIntervalsNew.get(position).enable == 1) {
            if (position == defFlag) {
                holder.mReserveDateTimeView.setTextColor(mContext.getResources().getColor(R.color.font_orange));
            } else {
                holder.mReserveDateTimeView.setTextColor(mContext.getResources().getColor(R.color.title_and_main_text));
            }
        }

        getCheckedTime(holder, position);
    }

    public void setDefFlag(int defFlag) {
        this.defFlag = defFlag;
    }

    public int getDefFlag() {
        return defFlag;
    }

    public String getDefTime() {
        return timeIntervals.get(defFlag)[1];
    }

    public String getDefTimeNew() {
        return mTimeIntervalsNew.get(defFlag).time;
    }

    private void getCheckedTime(final ReserveDateViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimeIntervalsNew.get(position).enable != 0) {
                    defFlag = position;
                    CharSequence getDate = holder.mReserveDateTimeView.getText();
//                mOnTimeItemClickListener.onTimeItemClick(v, getDate);
//                    mOnTimeItemClickListener.onTimeItemClick(v, timeIntervals.get(position)[1]);
                    mOnTimeItemClickListener.onTimeItemClick(v, mTimeIntervalsNew.get(position).time);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTimeIntervalsNew == null ? 0 : mTimeIntervalsNew.size();
    }

    public interface OnTimeItemClickListener {
        void onTimeItemClick(View v, String time);
    }

    class ReserveDateViewHolder extends RecyclerView.ViewHolder {

        private final TextView mReserveDateCommenView;
        private final TextView mReserveDateWeekView;
        private final TextView mReserveDateTimeView;

        public ReserveDateViewHolder(View itemView) {
            super(itemView);
            mReserveDateCommenView = (TextView) itemView.findViewById(R.id.reserve_date_commen);
            mReserveDateWeekView = (TextView) itemView.findViewById(R.id.reserve_date_week);
            mReserveDateTimeView = (TextView) itemView.findViewById(R.id.reserve_date_time);
        }
    }
}
