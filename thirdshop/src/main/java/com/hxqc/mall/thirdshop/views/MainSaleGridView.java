package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.ModelsQuote;
import com.hxqc.widget.ListViewNoSlide;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2015-12-01
 * FIXME
 * Todo 主营系列
 */
public class MainSaleGridView extends LinearLayout {
//    private FlowLayout mGridViewNoSlide;

//    private ArrayList<ModelsQuote.Series> series = new ArrayList<>();

//    GridAdapter mAdapter;

    //    public void addData(ArrayList<ModelsQuote> data) {
//        if (data == null || data.size() == 0) {
//            setVisibility(GONE);
//        } else {
//            setVisibility(VISIBLE);
//            series.clear();
//            for (ModelsQuote modelsQuote : data) {
//                series.addAll(modelsQuote.series);
//            }
//            if (mAdapter == null) {
//                mAdapter = new GridAdapter();
//                mGridViewNoSlide.setAdapter(mAdapter);
//            }
//            mGridViewNoSlide.notifyData();
//        }
//    }
    private ArrayList<ModelsQuote > data;

    private ListViewNoSlide mListViewNoSlide;

    private SeriesAdapter adapter;

    public MainSaleGridView(Context context) {
        this(context, null);
    }

    public MainSaleGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainSaleGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.t_view_main_sale_grid_view, this);
//        mGridViewNoSlide = (FlowLayout) findViewById(R.id.main_sale_auto_list);
        mListViewNoSlide = (ListViewNoSlide) findViewById(R.id.main_sale_list);
        data = new ArrayList<>();
        adapter = new SeriesAdapter();
        mListViewNoSlide.setAdapter(adapter);
    }

    public void addData(ArrayList<ModelsQuote> data) {
        this.data.clear();
        this.data.addAll(data);
        adapter.notifyDataSetChanged();
    }

    class SeriesAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {

            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MainSaleBrandItem item = new MainSaleBrandItem(getContext());
            item.addData(data.get(position));
            if (getCount() == 1)
                item.hideBrand();
            return item;
        }
    }
}
