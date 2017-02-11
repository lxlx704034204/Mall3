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
import com.hxqc.mall.thirdshop.maintenance.utils.CalendarUtil;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 10
 * Des: 预约时间-日期
 * FIXME
 * Todo
 */
public class ReserveDateAdapter extends RecyclerView.Adapter<ReserveDateAdapter.ReserveDateViewHolder> {

    private static final String TAG = AutoInfoContants.LOG_J;
    public OnDateItemClickListener mOnDateItemClickListener;
    private LayoutInflater mLayoutInflater;
    private String[] strings = {"今天", "明天", "后天"};
    private Context mContext;
    private int defFlag = 0;
    private ArrayList<List<String[]>> mDateAndTimes;

    private ArrayList<ApppintmentDateNew> mApppintmentDateNews;

    public ReserveDateAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public ReserveDateAdapter(Context context, ArrayList<List<String[]>> dateAndTimes) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mDateAndTimes = dateAndTimes;
    }

    public void setData(ArrayList<List<String[]>> dateAndTimes) {
        this.mDateAndTimes = dateAndTimes;
    }

    public void setDataNew(ArrayList<ApppintmentDateNew> apppintmentDateNews) {
        this.mApppintmentDateNews = apppintmentDateNews;
        for (int i = 0; i < apppintmentDateNews.size(); i++) {
            if (apppintmentDateNews.get(i).enable != 0) {
                defFlag = i;
                break;
            }
        }
    }

    public void setOnDateItemClickListener(OnDateItemClickListener l) {
        this.mOnDateItemClickListener = l;
    }

    @Override
    public ReserveDateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_reserve_date, parent, false);
        ReserveDateViewHolder reserveDateViewHolder = new ReserveDateViewHolder(view);
        return reserveDateViewHolder;
    }

    @Override
    public void onBindViewHolder(ReserveDateViewHolder holder, int position) {
        if (mApppintmentDateNews.get(position).enable == 0) {
            holder.mReserveDateCommenView.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.mReserveDateWeekView.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.itemView.setBackgroundResource(R.drawable.shape_reserve_nenable_stroke);
        } else if (mApppintmentDateNews.get(position).enable == 1) {
            if (position == defFlag) {
                holder.mReserveDateCommenView.setTextColor(mContext.getResources().getColor(R.color.font_orange));
                holder.mReserveDateWeekView.setTextColor(mContext.getResources().getColor(R.color.font_orange));
                holder.itemView.setBackgroundResource(R.drawable.shape_reserve_checked_stroke);
            } else {
                holder.mReserveDateCommenView.setTextColor(mContext.getResources().getColor(R.color.title_and_main_text));
                holder.mReserveDateWeekView.setTextColor(mContext.getResources().getColor(R.color.font_blue));
                holder.itemView.setBackgroundResource(R.drawable.shape_reserve_nchecked_stroke);
            }
        }
//        holder.mReserveDateCommenView.setText(CalendarUtil.getFiveDates().get(position));
        /*holder.mReserveDateCommenView.setText(mDateAndTimes.get(position).get(0)[0].substring(5, mDateAndTimes.get(position).get(0)[0].length()));
        if (CalendarUtil.getTodayMonth().equals(mDateAndTimes.get(position).get(0)[0].substring(5, mDateAndTimes.get(position).get(0)[0].length()))) {
            if (position < 2) {
                holder.mReserveDateWeekView.setText("(" + strings[position] + ")");
                DebugLog.i(TAG, "11111" + CalendarUtil.getTodayMonth());
            } else if (position > 1) {
                holder.mReserveDateWeekView.setText("(" + CalendarUtil.getDayofYear(position) + ")");
            }
        } else {
            if (position < 3) {
                holder.mReserveDateWeekView.setText("(" + strings[position] + ")");
                DebugLog.i(TAG, "11111" + CalendarUtil.getTodayMonth());
            } else if (position > 2) {
                holder.mReserveDateWeekView.setText("(" + CalendarUtil.getDayofYear(position + 1) + ")");
            }
        }*/
        holder.mReserveDateCommenView.setText(mApppintmentDateNews.get(position).time.substring(5, mApppintmentDateNews.get(position).time.length()));
        if (!CalendarUtil.getTodayMonth().equals(mApppintmentDateNews.get(position).time)) {
            if (position < 2) {
                if (position + 1 < strings.length) {
                    holder.mReserveDateWeekView.setText("(" + strings[position + 1] + ")");
                    DebugLog.i(TAG, "11111" + CalendarUtil.getTodayMonth());
                }
            } else if (position > 1) {
                holder.mReserveDateWeekView.setText("(" + CalendarUtil.getDayofYear(position + 1) + ")");
            }
        } else {
            if (position < 3) {
                holder.mReserveDateWeekView.setText("(" + strings[position] + ")");
                DebugLog.i(TAG, "11111" + CalendarUtil.getTodayMonth());
            } else if (position > 2) {
                holder.mReserveDateWeekView.setText("(" + CalendarUtil.getDayofYear(position) + ")");
            }
        }
        getCheckedDate(holder, position);
    }

    public void setDefFlag(int defFlag) {
        this.defFlag = defFlag;
    }

    public int getDefFlag() {
        return defFlag;
    }

    public String getDefDate() {
//        return CalendarUtil.getFiveDates().get(0);
        DebugLog.i(TAG, defFlag + "----" + mDateAndTimes.size());
        return mDateAndTimes.get(defFlag).get(0)[0];
    }

    public String getDefDateNew() {
        return mApppintmentDateNews.get(defFlag).time;
    }

    private void getCheckedDate(final ReserveDateViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defFlag = position;
                CharSequence getDate = holder.mReserveDateCommenView.getText();
//                mOnDateItemClickListener.onDateItemClick(v, getDate,position);
//                DebugLog.i(TAG, mDateAndTimes.get(position).get(0)[0]);
                if (mApppintmentDateNews.get(position).enable != 0) {
                    DebugLog.i(TAG, mApppintmentDateNews.get(position).time);
//                    mOnDateItemClickListener.onDateItemClick(v, mDateAndTimes.get(position).get(0)[0], position);
                    mOnDateItemClickListener.onDateItemClick(v, mApppintmentDateNews.get(position).time, position);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mApppintmentDateNews == null ? 0 : mApppintmentDateNews.size() == 5 ? 5 : mApppintmentDateNews.size();
    }


    public interface OnDateItemClickListener {
        void onDateItemClick(View v, String date, int position);
    }

    class ReserveDateViewHolder extends RecyclerView.ViewHolder {

        private final TextView mReserveDateCommenView;
        private final TextView mReserveDateWeekView;

        public ReserveDateViewHolder(View itemView) {
            super(itemView);
            mReserveDateCommenView = (TextView) itemView.findViewById(R.id.reserve_date_commen);
            mReserveDateWeekView = (TextView) itemView.findViewById(R.id.reserve_date_week);
        }
    }
}
