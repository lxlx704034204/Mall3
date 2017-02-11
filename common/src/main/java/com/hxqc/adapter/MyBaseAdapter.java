package com.hxqc.adapter;

/**
 * Created by CPR011 on 2015/2/5.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class MyBaseAdapter< T > extends BaseAdapter {
    protected Context ctx;
    protected LayoutInflater layoutInflater;
    protected List< T > list;

    public MyBaseAdapter(Context ctx, List< T > list) {
        super();
        this.list = list;
        ctx = ctx.getApplicationContext();

    }

    public void addList(ArrayList< T > list) {
        if (list == null) {
            return;
        }
        if (this.list != null) {
            this.list.addAll(list);
        } else {
            this.list = list;
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (list == null || list.size() == 0) {
            return 0;
        } else {
            return list.size();
        }

    }

    @Override
    public T getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return initView(position, convertView);
    }

    /**
     * getView方法需重写
     *
     * @param position
     * @return
     */
    public abstract View initView(int position, View convertView);
}
