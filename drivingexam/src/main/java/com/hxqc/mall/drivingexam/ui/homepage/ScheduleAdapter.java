package com.hxqc.mall.drivingexam.ui.homepage;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.core.base.BaseAdapter;

/**
 * 流程
 * Created by zhaofan on 2016/8/15.
 */
public class ScheduleAdapter extends BaseAdapter<String, BaseAdapter.BaseViewHolder> {

    public ScheduleAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseViewHolder createViewHolder(int position, ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exam_schedule, parent, false));
    }

    @Override
    protected void bindViewHolder(BaseViewHolder holder, View convertView, int position, String data) {
        final ViewHolder vh = (ViewHolder) holder;
        vh.tv.setText(data);
        if (!TextUtils.isEmpty(data)) {
            vh.num.setText((position + 1) + "");
         /*   if (position < 2)
                vh.num.setBackgroundResource(R.drawable.bg_circle_orange);
            else if (position < 6)
                vh.num.setBackgroundResource(R.drawable.bg_cirlce_green_light2);
            else
                vh.num.setBackgroundResource(R.drawable.bg_circle_red_light);*/
        }
        else {
            vh.num.setText("");
            vh.num.setBackgroundResource(0);
        }



      /*  if (position == 0 || position == 1 || position == 4 || position == 5) {
            vh.lay.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else
            vh.lay.setBackgroundColor(context.getResources().getColor(R.color.transparent));*/
    }


    private class ViewHolder extends BaseViewHolder {
        private TextView tv, num;
        private LinearLayout lay;

        public ViewHolder(View itemView) {
            super(itemView);
            num = getView(R.id.num);
            tv = getView(R.id.tv1);
            lay = getView(R.id.main);

        }
    }
}
