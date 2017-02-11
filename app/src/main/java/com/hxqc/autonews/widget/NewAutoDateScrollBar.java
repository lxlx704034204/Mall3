package com.hxqc.autonews.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hxqc.autonews.api.AutoInformationApiClient;
import com.hxqc.autonews.model.AutoCalendarModel;
import com.hxqc.autonews.presenter.Presenter;
import com.hxqc.autonews.view.IView;
import com.hxqc.mall.core.adapter.CommonAdapter;
import com.hxqc.mall.core.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-11-16
 * FIXME
 * Todo 新车上市的滑动日期选择器
 */

public class NewAutoDateScrollBar extends LinearLayout implements View.OnClickListener, IView<ArrayList<String>>, CompoundButton.OnCheckedChangeListener {
    private final float itemHeight;
    private CheckBox year;

    private ScrollMonth scrollMonth;
    private NormalListPop yearListPop;

    private List<String> years = new ArrayList<>();

    private String currentYear = "";
    private CommonAdapter<String> adapter;

    private int popWidth;

    public void setOnMonthSelectListener(ScrollMonth.OnMonthSelectListener onMonthSelectListener) {
        this.onMonthSelectListener = onMonthSelectListener;
        scrollMonth.setOnMonthSelectListener(onMonthSelectListener);
    }

    private ScrollMonth.OnMonthSelectListener onMonthSelectListener;

    public NewAutoDateScrollBar(Context context) {
        this(context, null);
    }

    public NewAutoDateScrollBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewAutoDateScrollBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        itemHeight = context.getResources().getDimension(R.dimen.height_list_year);
        LayoutInflater.from(context).inflate(R.layout.view_new_auto_date_scroll_bar, this);
        year = (CheckBox) findViewById(R.id.year);
//        year.setOnClickListener(this);
        year.setOnCheckedChangeListener(this);
        scrollMonth = (ScrollMonth) findViewById(R.id.scroll_month);
        year.postDelayed(new Runnable() {
            @Override
            public void run() {
                popWidth = year.getWidth();
            }
        }, 200);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.year)
            showPop(v);
    }

    /**
     * 显示年份的下拉条选择
     * @param v
     */
    private void showPop(View v) {
        if (yearListPop == null)
            initPop();
//        if (yearListPop.isShowing()) {
//            yearListPop.dismiss();
//        } else
        yearListPop.showAsDropDown(v);

    }

    /**
     * 初始化年份的显示下拉条
     */
    private void initPop() {
        adapter = new CommonAdapter<String>(getContext(), years, R.layout.item_year_list) {
            @Override
            public void convert(ViewHolder helper, String item, int position) {
                helper.setText(R.id.textview, item);
            }
        };
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                yearListPop.dismiss();
                currentYear = years.get(position);
                year.setText(currentYear);
//                year.setChecked(false);
                if (onYearSelectListener != null) {
                    onYearSelectListener.onYearSelected(currentYear);
                }
            }
        };
        int height = (int) (years.size() > 5 ? 5 : years.size() * itemHeight);
        yearListPop = new NormalListPop(getContext(), adapter, listener, popWidth, height);
        yearListPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                year.setChecked(false);
            }
        });
    }

    public void initData(Context context) {
        AutoInformationApiClient client = new AutoInformationApiClient();
        Presenter presenter = new Presenter();
        presenter.getAutoCalendarYears(this, new AutoCalendarModel(client, context));
    }

    @Override
    public void onDataBack(ArrayList<String> data) {
        this.years.clear();
        years.addAll(data);
        if (!years.isEmpty()) {
            year.setText(data.get(0));
            if (onYearSelectListener != null) {
                onYearSelectListener.onYearSelected(data.get(0));
            }
        }
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onDataNull(String message) {
        //没有获取到year数据
    }

    public void setOnYearSelectListener(OnYearSelectListener onYearSelectListener) {
        this.onYearSelectListener = onYearSelectListener;
    }

    private OnYearSelectListener onYearSelectListener;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked)
            showPop(buttonView);
    }

    public interface OnYearSelectListener {
        void onYearSelected(String year);
    }

}
