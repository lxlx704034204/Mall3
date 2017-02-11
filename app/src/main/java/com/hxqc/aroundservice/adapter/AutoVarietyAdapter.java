package com.hxqc.aroundservice.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.aroundservice.view.kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

import hxqc.mall.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 09
 * FIXME
 * Todo
 */
public class AutoVarietyAdapter  extends AbstractWheelTextAdapter {

    private final String[] stringArray;

    public AutoVarietyAdapter(Context context) {
        super(context, R.layout.item_commen_wheel, NO_RESOURCE);
        stringArray = context.getResources().getStringArray(R.array.auto_variety);
        setItemTextResource(R.id.wheel_item_title);
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {

        return super.getItem(index, convertView, parent);
    }

    @Override
    protected CharSequence getItemText(int index) {
        return stringArray[index];
    }

    @Override
    public int getItemsCount() {
        return stringArray.length;
    }


}
