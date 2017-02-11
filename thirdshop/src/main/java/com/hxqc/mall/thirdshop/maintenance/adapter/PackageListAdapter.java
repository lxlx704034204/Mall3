package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenancePackage;
import com.hxqc.mall.thirdshop.maintenance.views.MaintenanceProgramItem;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-03-23
 * FIXME
 * Todo 优惠套餐列表的适配器
 */
public class PackageListAdapter extends BaseAdapter {
    public OtherMaintenancePackageListAdapter.Type type= OtherMaintenancePackageListAdapter.Type.OTHER;
    class ViewHolder {
        private MaintenanceProgramItem content;
    }

    private ArrayList<MaintenancePackage> mData;
    private Context mContext;
    private OtherMaintenancePackageListAdapter.OnPackageClickListener onPackageClickListener;

    public PackageListAdapter(ArrayList<MaintenancePackage> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_mainteenance_program_list, null);
            holder.content = (MaintenanceProgramItem) convertView.findViewById(R.id.other_package_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.content.addData(mData.get(position));
        holder.content.setOnSeeDetailClickListener(new MaintenanceProgramItem.OnSeeDetailClickListener() {
            @Override
            public void onSeeDetailClick(View view) {
                //去看详情
                if (onPackageClickListener != null)
                    onPackageClickListener.onPackageClick(position,type);
            }
        });
        return convertView;
    }

    public void setOnPackageClickListener(OtherMaintenancePackageListAdapter.OnPackageClickListener onPackageClickListener) {
        this.onPackageClickListener = onPackageClickListener;
    }


}
