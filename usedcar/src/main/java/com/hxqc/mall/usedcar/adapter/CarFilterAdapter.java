package com.hxqc.mall.usedcar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.model.CarFilterTip;

import java.util.ArrayList;

/**
 * @author yby
 * @since 2015年08月30日
 */
public class CarFilterAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<CarFilterTip> carFilterTipses;

    public CarFilterAdapter(Context context, ArrayList<CarFilterTip> carFilterTipses) {
        this.mContext = context;
        this.carFilterTipses = carFilterTipses;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return carFilterTipses.size();
    }

    @Override
    public CarFilterTip getItem(int position) {
        return carFilterTipses.get(position);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold viewHold;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_buycar_filter_group, null);
            viewHold = new ViewHold();
            viewHold.carFilterTipItem = (TextView) convertView.findViewById(R.id.tips_filter_sort);
            viewHold.carFilterSelectedTipItem = (TextView) convertView.findViewById(R.id.tips_selected_sort);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }

        CarFilterTip carFilterTip = getItem(position);
        viewHold.carFilterTipItem.setText(carFilterTip.carFilterTipItem);
        viewHold.carFilterSelectedTipItem.setText(carFilterTip.carFilterSelectedTipItem);
        return convertView;
    }

    class ViewHold {
        TextView carFilterTipItem;
        TextView carFilterSelectedTipItem;
    }
}
