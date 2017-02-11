package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.ModelsQuote;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-01-12
 * FIXME
 * Todo 主营系列的品牌单项
 */
public class MainSaleBrandItem extends LinearLayout {
    private FlowLayout autoList;
    private TextView brandNameTv;
    private ArrayList<ModelsQuote.Series> data;
    private GridAdapter adapter;

    public void addData(ModelsQuote modelsQuote) {
        if (modelsQuote == null /*|| modelsQuote.series.size() == 0*/)
            setVisibility(GONE);
        else if ( modelsQuote.series.size() == 0)
            setVisibility(GONE);
        else {
            setVisibility(VISIBLE);
            this.data.clear();
            this.data.addAll(modelsQuote.series);
            brandNameTv.setText(modelsQuote.brandName);
            if (adapter == null) {
                adapter = new GridAdapter();
                autoList.setAdapter(adapter);
            }
            autoList.notifyData();
        }

    }

    public void hideBrand() {
        brandNameTv.setVisibility(GONE);
    }

    public MainSaleBrandItem(Context context) {
        this(context, null);
    }

    public MainSaleBrandItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainSaleBrandItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.t_view_main_sale_brand_item, this);
        autoList = (FlowLayout) findViewById(R.id.main_sale_brand_item_list);
        brandNameTv = (TextView) findViewById(R.id.main_sale_brand_item_brand_name);
        data = new ArrayList<>();
    }

    private MainSaleBrandItem.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            GridAdapter.ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.t_item_main_sale_grid_view, null);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.main_sale_series_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(data.get(position).seriesName);

            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivitySwitcherThirdPartShop.toModelsOfferBySericesID(data.get(position).seriesID, getContext());
                    if (null != onItemClickListener)
                        onItemClickListener.onItemClick(MainSaleBrandItem.this, position);
                }
            });
            return convertView;
        }

        class ViewHolder {
            private TextView textView;
        }
    }
}
