package com.hxqc.mall.views.bill;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hxqc.mall.core.util.OtherUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-03-05
 * FIXME
 * Todo 一组key_value
 */
public class NormalMapListView extends LinearLayoutCompat {
    private LinearLayoutCompat containerLayout;
    private LinkedHashMap<String, String> map;
    private ArrayList<String> values;
    private ArrayList<String> keys;

    public NormalMapListView(Context context) {
        this(context, null);
    }

    public NormalMapListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NormalMapListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_normal_map_list, this);
        containerLayout = (LinearLayoutCompat) findViewById(R.id.list_container_1);
        map = new LinkedHashMap<>();
    }

    /**
     * 添加数据
     *
     * @param map
     */
    public void setKeyValues(LinkedHashMap<String, String> map) {
        this.map = map;
        values = OtherUtil.collectionToList(map.values());
        keys = OtherUtil.collectionToList(map.keySet());
        for (int i = 0; i < keys.size(); i++) {
            NormalMapView horizontalView = new NormalMapView(getContext());
            horizontalView.setGravity(NormalMapView.Gravity.LEFT);
            horizontalView.setKeyValue(keys.get(i), values.get(i));
            horizontalView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            containerLayout.addView(horizontalView);
        }
    }

    /**
     * 根据key获取对应的item
     *
     * @param key
     * @return
     */
    public NormalMapView getMapView(String key) {
        int index = -1;
        for (int i = 0; i < keys.size(); i++) {
            String mapKey = keys.get(i);
            if (mapKey.equals(key)) {
                index = i;
                break;
            }
        }
        if (index > -1)
            return (NormalMapView) containerLayout.getChildAt(index);
        return null;
    }


}
