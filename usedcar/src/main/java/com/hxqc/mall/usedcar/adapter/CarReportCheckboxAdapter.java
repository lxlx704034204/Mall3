package com.hxqc.mall.usedcar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.model.ReportData;

import java.util.ArrayList;
import java.util.List;

/**
 * 举报GridView Adapte
 * Created by huangyi on 15/10/28.
 */
public class CarReportCheckboxAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<ReportData> reportDatas;
    private List<String> selectedDatas;

    public CarReportCheckboxAdapter(ArrayList<ReportData> reportDatas, Context context) {
        this.mContext = context;
        this.reportDatas = reportDatas;
        selectedDatas = new ArrayList<>();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return reportDatas.size();
    }

    @Override
    public ReportData getItem(int position) {
        return reportDatas.get(position);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_report_gridview, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textview);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ReportData reportData = getItem(position);
        viewHolder.textView.setText(reportData.content);
        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.checkBox.setChecked(! viewHolder.checkBox.isChecked());
                if(viewHolder.checkBox.isChecked()) {
                    //选中
                    selectedDatas.add(getItem(position).id);
                }else {
                    if(selectedDatas.contains(getItem(position).id))
                        selectedDatas.remove(getItem(position).id);
                }
            }
        });
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.checkBox.isChecked()) {
                    //选中
                    selectedDatas.add(getItem(position).id);
                }else {
                    if(selectedDatas.contains(getItem(position).id))
                        selectedDatas.remove(getItem(position).id);
                }
            }
        });
        return convertView;
    }

    public List<String> getSelectedDatas() {
        return selectedDatas;
    }

    public static class ViewHolder {
        public CheckBox checkBox;
        public TextView textView;
    }
}
