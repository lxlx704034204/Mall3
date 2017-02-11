package com.hxqc.mall.thirdshop.views.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.AreaCategory;
import java.util.ArrayList;

/**
 * Function: 特价车分站地区选择列表Adapter
 *
 * @author 袁秉勇
 * @since 2016年05月10日
 */
public class ExpandAdapterForSpecialCar extends BaseExpandableListAdapter {
    private ArrayList< AreaCategory > areaCategories;


    public ExpandAdapterForSpecialCar(ArrayList< AreaCategory > list) {
        if (list != null) {
            areaCategories = list;
        } else {
            areaCategories = new ArrayList<>();
        }
    }


    @Override
    public int getGroupCount() {
        return areaCategories.size();
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return areaCategories.get(groupPosition).areaGroup.size();
    }


    @Override
    public Object getGroup(int groupPosition) {
        return areaCategories.get(groupPosition);
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return areaCategories.get(groupPosition);
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_position_province, parent, false);
        }
        TextView text1 = (TextView) convertView.findViewById(R.id.text1);
        text1.setText(areaCategories.get(groupPosition).areaName);
//            if (isExpanded){
//                text1.setCompoundDrawables(null,null,getResources().getDrawable(R.drawable.ic_cbb_arrow_up),null);
//            }else {
//                text1.setCompoundDrawables(null,null,getResources().getDrawable(R.drawable.ic_cbb_arrow_down),null);
//            }
        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_position_city, parent, false);
        }
        TextView text2 = (TextView) convertView.findViewById(R.id.text2);
        text2.setText(areaCategories.get(groupPosition).areaGroup.get(childPosition).city);
        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
