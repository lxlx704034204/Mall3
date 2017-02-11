package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.promotion.SalesDetailPriceModel;
import com.hxqc.mall.thirdshop.views.SalesDetailPricePieceView;

import java.util.ArrayList;


/**
 * Author: wanghao
 * Date: 2015-12-01
 * FIXME
 * Todo
 */
public class SalesPDetailPriceAdapter extends BaseAdapter {

    Context context;
    ArrayList< SalesDetailPriceModel > items;

    public SalesPDetailPriceAdapter(Context context, ArrayList< SalesDetailPriceModel > items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public SalesDetailPriceModel getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.t_item_sales_detail_price_view, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.setValue(getItem(position));
        return convertView;
    }

    public class ViewHolder {
        TextView mTitle;
        SalesDetailPricePieceView mKnockdownView;
        SalesDetailPricePieceView mPreferentialView;
        SalesDetailPricePieceView mIndicativeView;
        SalesDetailPricePieceView mNakedCarView;
        SalesDetailPricePieceView mGiftView;
        SalesDetailPriceModel model;

        public ViewHolder(View v) {
            mTitle = (TextView) v.findViewById(R.id.tv_s_detail_price_item_title);
            mKnockdownView = (SalesDetailPricePieceView) v.findViewById(R.id.sales_dppv_knockdown_price);
            mPreferentialView = (SalesDetailPricePieceView) v.findViewById(R.id.sales_dppv_preferential_price);
            mIndicativeView = (SalesDetailPricePieceView) v.findViewById(R.id.sales_dppv_indicative_price);
            mNakedCarView = (SalesDetailPricePieceView) v.findViewById(R.id.sales_dppv_naked_price);
            mGiftView = (SalesDetailPricePieceView) v.findViewById(R.id.sales_dppv_gift);
        }

        public void setValue(SalesDetailPriceModel model) {
            if (model == null) return;
            this.model = model;
            mTitle.setText(model.carTitle);

            mKnockdownView.setValues(
                    "成交价：", model.getKnockdownPrice(),
                    true, R.drawable.ic_table);
            mKnockdownView.setValueColor(R.color.main_and_price);

            mPreferentialView.setValues(
                    "优惠：", model.getPreferentialPrice(),
                    true, R.drawable.ic_search_drop
            );

            mIndicativeView.setValues(
                    "指导价：", model.getIndicativePrice(),
                    false, 0
            );

            mNakedCarView.setValues(
                    "裸车价：", model.getNakedCarPrice(),
                    true, R.drawable.ic_inquiry
            );

            mGiftView.setValues(
                    "赠送礼包：", model.gift, false, 0
            );

        }
    }
}
