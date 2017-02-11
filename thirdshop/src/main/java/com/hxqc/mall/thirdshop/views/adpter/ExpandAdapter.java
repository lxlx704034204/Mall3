package com.hxqc.mall.thirdshop.views.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hxqc.mall.core.model.LocationAreaModel;
import com.hxqc.mall.thirdshop.R;

import java.util.ArrayList;

/**
 * Author :liukechong
 * Date : 2016-01-04
 * FIXME
 * Todo
 */
public class ExpandAdapter extends BaseExpandableListAdapter {
    private static final int[] EMPTY_STATE_SET = {};
    /** State indicating the group is expanded. */
    private static final int[] GROUP_EXPANDED_STATE_SET = {android.R.attr.state_expanded};
    /** State indicating the group is empty (has no children). */
    private static final int[] GROUP_EMPTY_STATE_SET = {android.R.attr.state_empty};
    /** State indicating the group is expanded and empty (has no children). */
    private static final int[] GROUP_EXPANDED_EMPTY_STATE_SET = {android.R.attr.state_expanded, android.R.attr.state_empty};
    /** States for the group where the 0th bit is expanded and 1st bit is empty. */
    private static final int[][] GROUP_STATE_SETS = {EMPTY_STATE_SET, // 00
            GROUP_EXPANDED_STATE_SET, // 01
            GROUP_EMPTY_STATE_SET, // 10
            GROUP_EXPANDED_EMPTY_STATE_SET // 11
    };
    ArrayList< LocationAreaModel > positionBeans;


    public ExpandAdapter(ArrayList< LocationAreaModel > list) {
        if (list != null) {
            positionBeans = list;
        } else {
            positionBeans = new ArrayList<>();
        }
    }


    @Override
    public int getGroupCount() {
        return positionBeans.size();
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return positionBeans.get(groupPosition).areaGroup.size();
    }


    @Override
    public Object getGroup(int groupPosition) {
        return positionBeans.get(groupPosition);
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return positionBeans.get(groupPosition);
    }


    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
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
        text1.setText(positionBeans.get(groupPosition).provinceName);
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
        text2.setText(positionBeans.get(groupPosition).areaGroup.get(childPosition).province);
        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);

    }


    private void getPosition() {

    }
}
