package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.model.auto.AutoBaseInformation;
import com.hxqc.mall.core.util.ImageUtil;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author: HuJunJie
 * Date: 2015-04-02
 * FIXME
 * Todo
 */
public class AutoListAdapter extends BaseAdapter {
    ArrayList< AutoBaseInformation > autoBaseInformations;
    Context context;

    public AutoListAdapter(Context context) {
        this.context = context;
    }

    public AutoListAdapter(Context context, ArrayList< AutoBaseInformation > autoBaseInformations) {
        this.autoBaseInformations = autoBaseInformations;
        this.context = context;
    }


    @Override
    public int getCount() {
        return autoBaseInformations == null ? 0 : autoBaseInformations.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.item_auto, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.setValue(getItem(position));

        return convertView;
    }

    @Override
    public AutoBaseInformation getItem(int position) {
        return autoBaseInformations.get(position);
    }

    public class ViewHolder {
        TextView mAutoNameView;//车型
        TextView mAutoFallView;//降幅
        TextView mAutoPriceView;//价格
        TextView mSalesNumView;//销量
        TextView mCommentCountView;//评论数
        ImageView mAutoImageView;//车图像
        AutoBaseInformation autoBaseInformation;

        public ViewHolder(View v) {
            mAutoNameView = (TextView) v.findViewById(R.id.auto_name);
            mAutoPriceView = (TextView) v.findViewById(R.id.auto_price);
            mAutoFallView = (TextView) v.findViewById(R.id.auto_fall);
            mSalesNumView = (TextView) v.findViewById(R.id.auto_sales);
            mCommentCountView = (TextView) v.findViewById(R.id.auto_comment);
            mAutoImageView = (ImageView) v.findViewById(R.id.auto_image);

        }

        public void setValue(AutoBaseInformation autoBaseInformation) {
            if (autoBaseInformation == null) return;
            this.autoBaseInformation = autoBaseInformation;
            mAutoNameView.setText(autoBaseInformation.getItemDescription());
            mAutoPriceView.setText(String.format("¥%s", autoBaseInformation.getItemPriceU()));
            mAutoFallView.setText(String.format("直降:%s", autoBaseInformation.getItemFallU()));
            mSalesNumView.setText(String.format("销量:%s", autoBaseInformation.getItemSales()));
            mCommentCountView.setText(String.format("评论:%s", autoBaseInformation.getItemCommentCount()));
            ImageUtil.setImage(context, mAutoImageView, autoBaseInformation.getItemThumb());

        }
    }
}
