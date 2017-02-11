package com.hxqc.aroundservice.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.hxqc.mall.core.adapter.BaseMapListAdapter;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.OtherUtil;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Function: 地图上周边服务列表Adapter
 *
 * @author 袁秉勇
 * @since 2016年06月21日
 */
public class AroundServiceMapListAdapter extends BaseMapListAdapter {
    private final static String TAG = AroundServiceMapListAdapter.class.getSimpleName();
    private Context mContext;

    private ArrayList<PoiItem> list = new ArrayList<>();


    public AroundServiceMapListAdapter(Context mContext) {
        this.mContext = mContext;
    }


    public void setData(ArrayList<PoiItem> poiItems) {
        if (this.list.size() > 0) {
            this.list.clear();
        }
        this.list.addAll(poiItems);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public PoiItem getItem(int position) {
        return list.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PoiItem poiItem = list.get(position);

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_map_list, null);
            viewHolder = new ViewHolder();

            viewHolder.mDividierView = convertView.findViewById(R.id.divider);
            viewHolder.mShopNameView = (TextView) convertView.findViewById(R.id.shop_name);
            viewHolder.mShopDistanceView = (TextView) convertView.findViewById(R.id.shop_distance);
            viewHolder.mShopAddressView = (TextView) convertView.findViewById(R.id.shop_address);
            viewHolder.mGotoThereView = (LinearLayout) convertView.findViewById(R.id.goThereButton);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == list.size() - 1) {
            viewHolder.mDividierView.setVisibility(View.GONE);
        } else {
            viewHolder.mDividierView.setVisibility(View.VISIBLE);
        }

        if (position == selectedPosition) {
            convertView.setBackgroundColor(Color.parseColor("#E6F3F9"));
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }

        viewHolder.mShopNameView.setText((position + 1) + "." + poiItem.getTitle());
        viewHolder.mShopDistanceView.setText(OtherUtil.reformatDistance(poiItem.getDistance()+""));
        viewHolder.mShopAddressView.setText("地址: " + poiItem.getSnippet());
        viewHolder.mGotoThereView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.clickCallBack(new PickupPointT(poiItem.getSnippet(), poiItem.getLatLonPoint().getLatitude() + "", poiItem.getLatLonPoint().getLongitude() + "", poiItem.getTel()));
            }
        });
        return convertView;
    }

    class ViewHolder {
        View mDividierView;
        TextView mShopNameView, mShopDistanceView, mShopAddressView;
        LinearLayout mGotoThereView;
    }
}
