package com.hxqc.autonews.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-11-16
 * FIXME
 * Todo 常规下来列表选择PopupWindow
 */

public class NormalListPop extends PopupWindow {
    public NormalListPop(Context context, BaseAdapter adapter,
                         AdapterView.OnItemClickListener listener, int width, int height) {
        View inflate = LayoutInflater.from(context)
                .inflate(R.layout.view_choose_year_list_pop, null);
        ListView listView = (ListView) inflate.findViewById(R.id.list);
        listView.setAdapter(adapter);
        setContentView(inflate);
        if (width > 0)
            setWidth(width);

        if (height > 0)
            setHeight(height);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        listView.setOnItemClickListener(listener);
    }
}
