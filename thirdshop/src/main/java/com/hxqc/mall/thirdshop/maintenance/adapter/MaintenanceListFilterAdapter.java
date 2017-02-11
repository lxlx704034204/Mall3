package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.hxqc.mall.thirdshop.R;
import java.util.ArrayList;

/**
 * Function:
 *
 * @author 袁秉勇
 * @since 2016年07月13日
 */
public class MaintenanceListFilterAdapter extends BaseAdapter {
    private final static String TAG = MaintenanceListFilterAdapter.class.getSimpleName();
    private Context mContext;

    private ArrayList< String > lists = new ArrayList<>();
    private int lastClickPos = 0;


    public void setLastClickPos(int lastClickPos) {
        this.lastClickPos = lastClickPos;
    }


    public MaintenanceListFilterAdapter(ArrayList< String > lists, Context mContext) {
        this.lists = lists;
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return lists.size();
    }


    @Override
    public String getItem(int position) {
        return lists.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_maintenance_filter_list, null);
            viewHolder = new ViewHolder();

            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.filter_tip);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == lastClickPos) {
            viewHolder.mTextView.setTextColor(Color.parseColor("#FF845F"));
        } else {
            viewHolder.mTextView.setTextColor(Color.parseColor("#666666"));
        }
        viewHolder.mTextView.setText(lists.get(position));
        return convertView;
    }


    class ViewHolder {
        public TextView mTextView;
    }
}
