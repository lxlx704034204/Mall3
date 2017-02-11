package com.hxqc.newenergy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.newenergy.bean.position.City;
import com.hxqc.newenergy.util.EVSharePreferencesHelper;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Function: 新能源——筛选地区时，城市ListView的Adapter
 *
 * @author 袁秉勇
 * @since 2016年03月18日
 */
public class EV_FilterAreaCityAdapter extends BaseAdapter {
    private final static String TAG = EV_FilterAreaCityAdapter.class.getSimpleName();
    private Context mContext;
    private FilterAreaCityCallBack filterAreaCityCallBack;
    ArrayList< City > cities = new ArrayList<>();
    private int lastClickPos = -1;
    private boolean isFirstChangedStyled = true;
    private String historyCity;


    public void setIsFirstChangedStyled(boolean isFirstChangedStyled) {
        this.isFirstChangedStyled = isFirstChangedStyled;
    }


    public void setLastClickPos(int lastClickPos) {
        this.lastClickPos = lastClickPos;
    }

    public void setFilterAreaCityCallBack(FilterAreaCityCallBack filterAreaCityCallBack) {
        this.filterAreaCityCallBack = filterAreaCityCallBack;
    }


    public EV_FilterAreaCityAdapter() {
    }


    public EV_FilterAreaCityAdapter(Context mContext, ArrayList< City > cities) {
        this.mContext = mContext;
        this.cities.addAll(cities);
        historyCity = EVSharePreferencesHelper.getLastHistoryCity(mContext);
    }


    @Override
    public int getCount() {
        return cities.size() == 0 ? 0 : cities.size();
    }


    @Override
    public City getItem(int position) {
        return cities.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final City city = cities.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ev_filter_area_city_item, null);

            viewHolder = new ViewHolder();
            viewHolder.mCityNameView = (TextView) convertView.findViewById(R.id.city_name);
            viewHolder.mDividerLineBottom = convertView.findViewById(R.id.divider_line_bottom);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == lastClickPos || (isFirstChangedStyled && historyCity.equals(city.province))) {
            viewHolder.mCityNameView.setTextColor(Color.parseColor("#FF7143"));
            viewHolder.mDividerLineBottom.setBackgroundColor(Color.parseColor("#FF7143"));
        } else {
            viewHolder.mCityNameView.setTextColor(Color.parseColor("#333333"));
            viewHolder.mDividerLineBottom.setBackgroundColor(Color.parseColor("#D9D9D9"));
        }

        viewHolder.mCityNameView.setText(city.province);
        viewHolder.mCityNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastClickPos = position;
                if (isFirstChangedStyled) isFirstChangedStyled = false;
                if (filterAreaCityCallBack != null) filterAreaCityCallBack.onClickCityCallBack(cities.get(position).province);
            }
        });
        return convertView;
    }


    /** 更新数据 **/
    public void refreshData(ArrayList< City > cities) {
        if (this.cities != null && this.cities.size() > 0) {
            this.cities.clear();
        }
        if (cities != null) this.cities.addAll(cities);
        notifyDataSetChanged();
    }


    public interface FilterAreaCityCallBack {
        void onClickCityCallBack(String cityName);
    }

    class ViewHolder {
        TextView mCityNameView;
        View mDividerLineBottom;
    }
}
