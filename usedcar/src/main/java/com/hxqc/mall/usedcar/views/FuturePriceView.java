package com.hxqc.mall.usedcar.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.utils.MyBarDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * 说明:未来价格
 *
 * @author: 吕飞
 * @since: 2016-05-12
 * Copyright:恒信汽车电子商务有限公司
 */
public class FuturePriceView extends LinearLayout {
    public static final int X_COUNT = 5;
    public static final int[] MATERIAL_COLORS = {
            Color.parseColor("#FD6621"), Color.parseColor("#F98724"), Color.parseColor("#FEA432"), Color.parseColor("#FCC350"), Color.parseColor("#FED23E")
    };
    TextView mFutureTitleView;
    TextView mChartUnitView;
    BarChart mBarChartView;
    String mFutureTitle;
    String mChartUnit;
    float[] mChartData;

    public FuturePriceView(Context context) {
        super(context);
        initView();
    }

    public FuturePriceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FuturePriceView);
        mFutureTitle = typedArray.getString(R.styleable.FuturePriceView_futureTitle);
        mChartUnit = typedArray.getString(R.styleable.FuturePriceView_chartUnit);
        typedArray.recycle();
        mFutureTitleView.setText(mFutureTitle);
        mChartUnitView.setText(mChartUnit);
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_future_price, this);
        mFutureTitleView = (TextView) findViewById(R.id.future_title);
        mChartUnitView = (TextView) findViewById(R.id.chart_unit);
        mBarChartView = (BarChart) findViewById(R.id.bar_chart);
    }

    public void setChartData(float[] chartData,float originalPrice) {
        mChartData = chartData;
        mBarChartView.setDrawBarShadow(false);
        mBarChartView.setDrawValueAboveBar(true);
        mBarChartView.setDescription("");
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mBarChartView.setMaxVisibleValueCount(8);
        mBarChartView.setTouchEnabled(false); // 设置是否可以触摸
        mBarChartView.setDragEnabled(true);// 是否可以拖拽

        mBarChartView.setScaleEnabled(false);// 是否可以缩放 x和y轴, 默认是true

        mBarChartView.setDoubleTapToZoomEnabled(false);//设置是否可以通过双击屏幕放大图表。默认是true
        // scaling can now only be done on x- and y-axis separately
        mBarChartView.setPinchZoom(false);
        mBarChartView.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);
        mBarChartView.setHighlightPerTapEnabled(false);
        XAxis xAxis = mBarChartView.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineColor(getResources().getColor(R.color.divider_line_bg));
        xAxis.setSpaceBetweenLabels(2);

        YAxisValueFormatter custom = new MyYAxisValueFormatter();
        YAxis leftAxis = mBarChartView.getAxisLeft();
        leftAxis.setLabelCount(6, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setAxisLineColor(getResources().getColor(R.color.transparent));
        leftAxis.setValueFormatter(custom);
        leftAxis.setSpaceTop(10f);
        leftAxis.setAxisMaxValue(originalPrice);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        mBarChartView.getAxisRight().setEnabled(false);
        mBarChartView.getLegend().setEnabled(false);
        setData();
        mBarChartView.animateY(2500);
    }

    private void setData() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        ArrayList<String> xVals = new ArrayList<>();
        for (int i = 0; i < X_COUNT; i++) {
            xVals.add(year + 1 + i + "年");
        }
        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        for (int i = 0; i < X_COUNT; i++) {
            yVals1.add(new BarEntry(mChartData[i], i));
        }

        MyBarDataSet set1;

        if (mBarChartView.getData() != null &&
                mBarChartView.getData().getDataSetCount() > 0) {
            set1 = (MyBarDataSet) mBarChartView.getData().getDataSetByIndex(0);
            set1.setYVals(yVals1);
            for (int i = 0; i < xVals.size(); i++) {
                mBarChartView.getData().addXValue(xVals.get(i));
            }
            mBarChartView.getData().notifyDataChanged();
            mBarChartView.notifyDataSetChanged();
        } else {
            set1 = new MyBarDataSet(yVals1, "DataSet");
            set1.setBarSpacePercent(35f);
            set1.setColors(MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(xVals, dataSets);
            data.setValueFormatter(new MyValueFormatter());
            data.setValueTextSize(10f);
            mBarChartView.setData(data);
        }
    }

    private class MyYAxisValueFormatter implements YAxisValueFormatter {
        @Override
        public String getFormattedValue(float value, YAxis yAxis) {
            return new DecimalFormat("###,###,###,##0.00").format(value);
        }
    }

    private class MyValueFormatter implements com.github.mikephil.charting.formatter.ValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return new DecimalFormat("###,###,###,##0.00").format(value);
        }
    }
}
