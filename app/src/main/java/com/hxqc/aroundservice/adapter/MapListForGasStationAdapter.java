package com.hxqc.aroundservice.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.amap.model.GasStationModel;
import com.hxqc.mall.core.adapter.BaseMapListAdapter;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.OtherUtil;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Function: 地图列表Adapter（加油站）
 *
 * @author 袁秉勇
 * @since 2016年06月25日
 */
public class MapListForGasStationAdapter extends BaseMapListAdapter {
    private final static String TAG = MapListForGasStationAdapter.class.getSimpleName();
    private Context mContext;

    private ArrayList< GasStationModel > gasStationModels = new ArrayList<>();


    public MapListForGasStationAdapter(Context mContext) {
        this.mContext = mContext;
    }


    public void setData(ArrayList< GasStationModel > gasStationModels) {
        if (this.gasStationModels.size() > 0) {
            this.gasStationModels.clear();
        }
        this.gasStationModels.addAll(gasStationModels);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return gasStationModels.size();
    }


    @Override
    public GasStationModel getItem(int position) {
        return gasStationModels.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final GasStationModel gasStationModel = gasStationModels.get(position);

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_map_list_for_gas_station, null);
            viewHolder = new ViewHolder();

            viewHolder.mDividierView = convertView.findViewById(R.id.divider);
            viewHolder.mShopNameView = (TextView) convertView.findViewById(R.id.shop_name);
            viewHolder.mShopDistanceView = (TextView) convertView.findViewById(R.id.shop_distance);
            viewHolder.mShopAddressView = (TextView) convertView.findViewById(R.id.shop_address);
            viewHolder.mGotoThereView = (LinearLayout) convertView.findViewById(R.id.goThereButton);
            viewHolder.mGas90 = (TextView) convertView.findViewById(R.id.gas_90);
            viewHolder.mGas93 = (TextView) convertView.findViewById(R.id.gas_93);
            viewHolder.mGas97 = (TextView) convertView.findViewById(R.id.gas_97);
            viewHolder.mGas0 = (TextView) convertView.findViewById(R.id.gas_0);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            viewHolder.mDividierView.setVisibility(View.GONE);
        } else {
            viewHolder.mDividierView.setVisibility(View.VISIBLE);
        }

        if (position == selectedPosition) {
            convertView.setBackgroundColor(Color.parseColor("#E6F3F9"));
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }

        viewHolder.mShopNameView.setText((position + 1) + "." + gasStationModel.name);
        viewHolder.mShopDistanceView.setText(OtherUtil.reformatDistance(gasStationModel.distance));
        viewHolder.mShopAddressView.setText("地址: " + gasStationModel.address);
        viewHolder.mGas90.setText(gasStationModel.price.E90);
        viewHolder.mGas93.setText(gasStationModel.price.E93);
        viewHolder.mGas97.setText(gasStationModel.price.E97);
        viewHolder.mGas0.setText(gasStationModel.price.E0);

        viewHolder.mGotoThereView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.clickCallBack(new PickupPointT(gasStationModel.address, gasStationModel.getLatitude() + "", gasStationModel.getLongitude() + "", ""));
            }
        });
        return convertView;
    }


    class ViewHolder {
        View mDividierView;
        TextView mShopNameView, mShopDistanceView, mShopAddressView, mGas90, mGas93, mGas97, mGas0;
        LinearLayout mGotoThereView;
    }
}
