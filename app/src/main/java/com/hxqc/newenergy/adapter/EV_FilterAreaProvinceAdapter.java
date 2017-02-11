package com.hxqc.newenergy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hxqc.mall.core.db.area.AreaDBManager;
import com.hxqc.newenergy.bean.position.Province;
import com.hxqc.newenergy.util.EVSharePreferencesHelper;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Function: 新能源——筛选地区时，省ListView的Adapter
 *
 * @author 袁秉勇
 * @since 2016年03月18日
 */
public class EV_FilterAreaProvinceAdapter extends BaseAdapter {
    private final static String TAG = EV_FilterAreaProvinceAdapter.class.getSimpleName();
    private Context mContext;

    private ArrayList< Province > provinces = new ArrayList<>();

    private FilterProvinceCallBack filterProvinceCallBack;

    private int lastClickPos = -1;

    private AreaDBManager areaDBManager;
    private boolean isFirstChangedStyle = true;
    private int historyProvinceID;


    public EV_FilterAreaProvinceAdapter(Context mContext, ArrayList< Province > provinces) {
        this.mContext = mContext;
        this.provinces = provinces;
        areaDBManager = AreaDBManager.getInstance(mContext.getApplicationContext());
        historyProvinceID = areaDBManager.searchPIDAndCIDByTitle(EVSharePreferencesHelper.getLastHistoryCity(mContext))[0];
    }

	public void setFilterProvinceCallBack(FilterProvinceCallBack filterProvinceCallBack) {
		this.filterProvinceCallBack = filterProvinceCallBack;
	}

    @Override
    public int getCount() {
        Log.e(TAG, "----------- getCount");
        return provinces.size() == 0 ? 0 : provinces.size();
    }


    @Override
    public int getItemViewType(int position) {
        Log.e(TAG, " ------------ getItemViewType ");
        return super.getItemViewType(position);
    }


    @Override
    public int getViewTypeCount() {
        Log.e(TAG, " --------------- getViewTypeCount");
        return super.getViewTypeCount();
    }


    @Override
    public Province getItem(int position) {
        Log.e(TAG, " -------- getItem");
        return provinces.get(position);
    }


    @Override
    public long getItemId(int position) {
        Log.e(TAG, " ---------- getItemId");
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.e(TAG, " --------- getView " + position);
        Province province = provinces.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.ev_filter_area_province_item, null);

            viewHolder = new ViewHolder();
            viewHolder.mProvinceName = (TextView) convertView.findViewById(R.id.province_name);
            viewHolder.mDividerLineTop = convertView.findViewById(R.id.divider_line_top);
            viewHolder.mDividerLineRight = convertView.findViewById(R.id.divider_line_right);
            viewHolder.mDividerLineBottom = convertView.findViewById(R.id.divider_line_bottom);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (((position == lastClickPos) || (isFirstChangedStyle && historyProvinceID == province.provinceID))) {
            viewHolder.mDividerLineTop.setVisibility(View.VISIBLE);
            viewHolder.mDividerLineRight.setVisibility(View.GONE);
            viewHolder.mDividerLineBottom.setVisibility(View.VISIBLE);
            viewHolder.mProvinceName.setBackgroundColor(Color.WHITE);
            isFirstChangedStyle = false;
        } else {
            viewHolder.mDividerLineTop.setVisibility(View.GONE);
            viewHolder.mDividerLineRight.setVisibility(View.VISIBLE);
            viewHolder.mDividerLineBottom.setVisibility(View.GONE);
            viewHolder.mProvinceName.setBackgroundColor(mContext.getResources().getColor(R.color.ev_subsidy_filter_province_bg));
        }
        viewHolder.mProvinceName.setText(province.provinceName);
        viewHolder.mProvinceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == lastClickPos) {
//                    if (position == 0) {
//                        filterProvinceCallBack.onClickProvinceCallBack(position);
//                    }
                    return;
                }
                lastClickPos = position;
                if (isFirstChangedStyle) isFirstChangedStyle = false;
                if (filterProvinceCallBack != null) filterProvinceCallBack.onClickProvinceCallBack(position);
            }
        });
        return convertView;
    }


	public interface FilterProvinceCallBack {
		void onClickProvinceCallBack(int position);
	}

    class ViewHolder {
        TextView mProvinceName;
        View mDividerLineTop;
        View mDividerLineRight;
        View mDividerLineBottom;
    }
}
