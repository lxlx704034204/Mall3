package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.SearchModel;

import java.util.ArrayList;


/**
 * searchModelList对应的适配器
 * by 李烽
 */
public class ShopAutoListAdapter extends BaseAdapter {
    ArrayList<SearchModel > autoBaseInformations;
    Context context;

    public ShopAutoListAdapter(Context context) {
        this.context = context;
    }

    public ShopAutoListAdapter(Context context, ArrayList<SearchModel> autoBaseInformations) {
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

            convertView = LayoutInflater.from(context).inflate(R.layout.item_search_auto, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.setValue(getItem(position));

        return convertView;
    }

    @Override
    public SearchModel getItem(int position) {
        return autoBaseInformations.get(position);
    }

    public class ViewHolder {
        TextView mAutoNameView;//车型
        TextView mAutoPriceOrangeView;//价格区间
        TextView mAutoMsrpView;//厂家指导价
        ImageView mAutoImageView;//车图像
        SearchModel autoBaseInformation;

        public ViewHolder(View v) {
            mAutoNameView = (TextView) v.findViewById(R.id.auto_name);
            mAutoPriceOrangeView = (TextView) v.findViewById(R.id.auto_price_range);
            mAutoMsrpView = (TextView) v.findViewById(R.id.auto_msrp);
            mAutoImageView = (ImageView) v.findViewById(R.id.auto_image);
        }

        public void setValue(SearchModel autoBaseInformation) {
            if (autoBaseInformation == null) return;
            this.autoBaseInformation = autoBaseInformation;
            mAutoNameView.setText(autoBaseInformation.modelName);
            if (!TextUtils.isEmpty(autoBaseInformation.itemOrigPrice))
                mAutoMsrpView.setText(OtherUtil.amountFormat(autoBaseInformation.itemOrigPrice,true));
            if (!TextUtils.isEmpty(autoBaseInformation.priceRange))
                mAutoPriceOrangeView.setText("¥ "+OtherUtil.formatPriceRange(autoBaseInformation.priceRange));
            ImageUtil.setImage(context, mAutoImageView, autoBaseInformation.modelThumb);

        }
    }
}
