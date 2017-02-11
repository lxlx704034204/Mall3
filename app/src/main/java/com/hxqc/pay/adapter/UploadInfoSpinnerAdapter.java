package com.hxqc.pay.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.hxqc.mall.core.model.loan.InstallmentDataKeyValue;

import java.util.ArrayList;

import hxqc.mall.R;


/**
 * Author: liukechong
 * Date: 2015-10-26
 * FIXME SpinnerAdapter
 * Todo
 */
public class UploadInfoSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    private ArrayList<InstallmentDataKeyValue> dateList;
    public UploadInfoSpinnerAdapter(ArrayList<InstallmentDataKeyValue> dateList){
        this.dateList = dateList;
    }
    @Override
    public int getCount() {
        return dateList.size();
    }

    @Override
    public String getItem(int i) {
        return String.valueOf(dateList.get(i).key);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view==null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_spinner_upload_info, viewGroup, false);
        }
        TextView item = (TextView) view.findViewById(R.id.item_text_spinner);
        item.setText(dateList.get(i).value);
        return view;
    }
}
