package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenancePackage;
import com.hxqc.mall.thirdshop.maintenance.views.MaintenanceProgramItem;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-02-19
 * FIXME
 * Todo 其他优惠套餐列表
 */
public class OtherMaintenancePackageListAdapter extends
        BaseAdapter {
    private ArrayList<MaintenancePackage> mData;
    private Context mContext;
    private OnPackageClickListener onPackageClickListener;

    public MyAuto auto;
    public String shopID;
    public Type type = Type.OTHER;

    public OtherMaintenancePackageListAdapter(ArrayList<MaintenancePackage> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (mData == null)
            return 0;
        else if (mData.size() > 2)
            return 2;
        else return mData.size();
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
            holder.title = (LinearLayout) convertView.findViewById(R.id.other_package_title);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.title_textView);
            holder.title_more = (TextView) convertView.findViewById(R.id.title_more);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String title;
        if (type == Type.BASE)
            title = mContext.getString(R.string.base_maintenance_program);
        else title = mContext.getString(R.string.other_maintenance_package);
        if (position == 0) {
            holder.title.setVisibility(View.VISIBLE);
            holder.titleTextView.setText(title);
        } else
            holder.title.setVisibility(View.GONE);


        holder.content.addData(mData.get(position));
        holder.title_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去看其他套餐列表
                if (null != onMoreClickListener)
                    onMoreClickListener.onMoreClick(type);
            }
        });
        holder.content.setOnSeeDetailClickListener(new MaintenanceProgramItem.OnSeeDetailClickListener() {
            @Override
            public void onSeeDetailClick(View view) {
                if (onPackageClickListener != null)
                    onPackageClickListener.onPackageClick(position, type);
            }
        });
        return convertView;
    }

    public void setOnPackageClickListener(OnPackageClickListener onPackageClickListener) {
        this.onPackageClickListener = onPackageClickListener;
    }

    private OnMoreClickListener onMoreClickListener;

    public void setOnMoreClickListener(OnMoreClickListener onMoreClickListener) {
        this.onMoreClickListener = onMoreClickListener;
    }

    public interface OnMoreClickListener {
        void onMoreClick(Type type);
    }

    public interface OnPackageClickListener {
        void onPackageClick(int position, Type type);
    }

    class ViewHolder {
        private LinearLayout title;
        private TextView titleTextView;
        private MaintenanceProgramItem content;
        private TextView title_more;
    }

    public enum Type {
        BASE, OTHER
    }
}
