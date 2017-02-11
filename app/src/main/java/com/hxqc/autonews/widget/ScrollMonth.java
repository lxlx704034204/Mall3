package com.hxqc.autonews.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-11-16
 * FIXME
 * Todo 可滑动的月份横条
 */

public class ScrollMonth extends ScrollView {
    private String[] months;

    private CheckedTextView[] checkedTextViews;
    private List<Boolean> select = new ArrayList<>();

    public ScrollMonth(Context context) {
        this(context, null);
    }

    public ScrollMonth(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollMonth(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_scroll_month, this);
        months = context.getResources().getStringArray(R.array.month);
        initMonth();
        setSelectData(0);
    }

    private void setSelectData(int position) {
        select.clear();
        for (int i = 0; i < 12; i++) {
            select.add(false);
        }
        select.set(position, true);
        onSelect(months[position]);
        upView();
    }

    private void upView() {
        for (int i = 0; i < select.size(); i++) {
            checkedTextViews[i].setChecked(select.get(i));
            if (select.get(i)) {
                checkedTextViews[i].setTextColor(Color.parseColor("#e02c36"));
            } else {
                checkedTextViews[i].setTextColor(Color.parseColor("#333333"));
            }
        }
    }

    private void onSelect(String month) {
        if (onMonthSelectListener != null) {
            onMonthSelectListener.onMonthSelected(month);
        }
    }

    public void setOnMonthSelectListener(OnMonthSelectListener onMonthSelectListener) {
        this.onMonthSelectListener = onMonthSelectListener;
    }

    private OnMonthSelectListener onMonthSelectListener;

    private void initMonth() {
        checkedTextViews = new CheckedTextView[12];
        checkedTextViews[0] = (CheckedTextView) findViewById(R.id.january);
        checkedTextViews[1] = (CheckedTextView) findViewById(R.id.february);
        checkedTextViews[2] = (CheckedTextView) findViewById(R.id.march);
        checkedTextViews[3] = (CheckedTextView) findViewById(R.id.april);
        checkedTextViews[4] = (CheckedTextView) findViewById(R.id.may);
        checkedTextViews[5] = (CheckedTextView) findViewById(R.id.june);
        checkedTextViews[6] = (CheckedTextView) findViewById(R.id.july);
        checkedTextViews[7] = (CheckedTextView) findViewById(R.id.august);
        checkedTextViews[8] = (CheckedTextView) findViewById(R.id.september);
        checkedTextViews[9] = (CheckedTextView) findViewById(R.id.october);
        checkedTextViews[10] = (CheckedTextView) findViewById(R.id.november);
        checkedTextViews[11] = (CheckedTextView) findViewById(R.id.december);
        for (int i = 0; i < checkedTextViews.length; i++) {
            final int finalI = i;
//            checkedTextViews[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked) {
//                        onSelect(months[finalI]);
//                        checkedTextViews[finalI].setTextColor(R.color.text_color_red);
//                    } else {
//                        checkedTextViews[finalI].setTextColor(R.color.text_color_subheading);
//                    }
//                }
//            });
            checkedTextViews[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelectData(finalI);
                }
            });
        }
    }

    public interface OnMonthSelectListener {
        void onMonthSelected(String month);
    }
}
