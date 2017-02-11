package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.district.DistrictItem;
import com.hxqc.mall.core.R;

import java.util.ArrayList;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年06月25日
 */
public class MapListFilterAdapter extends BaseAdapter {
    private final static String TAG = MapListFilterAdapter.class.getSimpleName();
    private Context mContext;

    private int lastClickPos = -1;

    private ArrayList< DistrictItem > districtItems;

    public MapListFilterAdapter(Context mContext, ArrayList< DistrictItem > districtItems) {
        this.mContext = mContext;
        this.districtItems = districtItems;
    }


    public void setLastClickPos(int lastClickPos) {
        this.lastClickPos = lastClickPos;
    }


    @Override
    public int getCount() {
        return districtItems.size();
    }


    @Override
    public DistrictItem getItem(int position) {
        return districtItems.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_map_filter_list, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.filter_text);
            viewHolder.mView = convertView.findViewById(R.id.selected_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == lastClickPos) {
            viewHolder.mView.setVisibility(View.VISIBLE);
            viewHolder.mTextView.setBackgroundColor(Color.parseColor("#F5F5F5"));
        } else {
            viewHolder.mView.setVisibility(View.GONE);
            viewHolder.mTextView.setBackgroundColor(Color.WHITE);
        }

        viewHolder.mTextView.setText(getItem(position).getName());
        return convertView;
    }

    class ViewHolder {
        TextView mTextView;
        View mView;
    }
}
