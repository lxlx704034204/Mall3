package com.hxqc.mall.drivingexam.ui.doexam.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxqc.mall.drivingexam.R;
import com.hxqc.mall.core.base.BaseAdapter;
import com.hxqc.mall.drivingexam.db.model.ExamRecord;
import com.hxqc.mall.core.model.Event;
import com.hxqc.mall.drivingexam.ui.doexam.ExamActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhaofan on 2016/8/2.
 */
public class SubjectAmountAdapter extends BaseAdapter<ExamRecord, BaseAdapter.BaseViewHolder> {

    private int mNowPage;

    public SubjectAmountAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseViewHolder createViewHolder(int position, ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_exam_count, parent, false));
    }

    @Override
    protected void bindViewHolder(BaseViewHolder holder, View convertView, final int position, final ExamRecord entity) {
        ViewHolder viewHodler = (ViewHolder) holder;
        viewHodler.tv.setText(entity.getNum() + "");

        //已完成的题
        if (entity.getIsFinish().equals("1")) {
            if (entity.getIsRight().equals("1")) {
                viewHodler.tv.setBackgroundResource(R.drawable.bg_circle_green_light);
                viewHodler.tv.setTextColor(Color.parseColor("#78BE75"));
            } else if (entity.getIsRight().equals("0")) {
                viewHodler.tv.setBackgroundResource(R.drawable.bg_circle_pink);
                viewHodler.tv.setTextColor(Color.parseColor("#DB948E"));
            }
        }
        //未做的题
        else {
            viewHodler.tv.setBackgroundResource(position == mNowPage ?
                    R.drawable.bg_circle_press_light : R.drawable.btn_click_circle_light);
            viewHodler.tv.setTextColor(Color.parseColor("#555555"));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new Event(position, ExamActivity.TO_PAGE));
            }
        });
    }

    public void setNowPage(int mNowPage) {
        this.mNowPage = mNowPage;
    }

    private class ViewHolder extends BaseViewHolder {
        private TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = getView(R.id.tv);
        }
    }
}

