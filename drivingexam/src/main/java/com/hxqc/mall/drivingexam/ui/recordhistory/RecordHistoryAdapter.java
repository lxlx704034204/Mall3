package com.hxqc.mall.drivingexam.ui.recordhistory;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.core.base.BaseAdapter;
import com.hxqc.mall.drivingexam.db.model.ScoreRecord;
import com.hxqc.util.ScreenUtil;


/**
 * 成绩列表adapter
 * Created by zhaofan on 2016/8/4.
 */
public class RecordHistoryAdapter extends BaseAdapter<ScoreRecord, BaseAdapter.BaseViewHolder> {

    public RecordHistoryAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseViewHolder createViewHolder(int position, ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_record_history, parent, false));
    }

    @Override
    protected void bindViewHolder(BaseViewHolder holder, View convertView, int position, ScoreRecord data) {
        final ViewHolder vh = (ViewHolder) holder;
        vh.score.setText(data.score);
        vh.score.setTextColor(Integer.parseInt(data.score) < 90 ? context.getResources().getColor(R.color.font_red)
                : context.getResources().getColor(R.color.green));

        vh.date.setText(data.date.substring(0, data.date.length() - 3));
        vh.time.setText(figureTime(data.time));

        if (Integer.parseInt(data.score) < 90) {
            vh.level.setCompoundDrawables(null, null, null, null);
            vh.level.setText("马路杀手");
        } else {
            Drawable weizhi = context.getResources().getDrawable(R.drawable.jiangbei);
            weizhi.setBounds(0, 0, ScreenUtil.dip2px(context, 16), ScreenUtil.dip2px(context, 16));
            vh.level.setCompoundDrawables(weizhi, null, null, null);
            vh.level.setText("恒信车神");
        }

    }

    private String figureTime(long millis) {
        long m, s;
        m = millis / (60 * 1000);
        s = (millis / 1000) - m * 60;
        return (m < 10 ? "0" + m : m) + "分" + (s < 10 ? "0" + s : s) + "秒";
    }


    private class ViewHolder extends BaseViewHolder {
        //店铺
        private TextView score, time, date, level;

        public ViewHolder(View itemView) {
            super(itemView);

            score = getView(R.id.score);
            time = getView(R.id.time);
            date = getView(R.id.date);
            level = getView(R.id.level);
        }
    }


}
