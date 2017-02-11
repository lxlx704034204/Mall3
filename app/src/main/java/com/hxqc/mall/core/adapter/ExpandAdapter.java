package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hxqc.newenergy.bean.position.Province;

import java.util.ArrayList;

/**
 * Function: 首页地区选择Adapter
 *
 * @author 袁秉勇
 * @since 2016年04月22日
 */
public class ExpandAdapter extends BaseExpandableListAdapter {
    private final static String TAG = ExpandAdapter.class.getSimpleName();
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
    ArrayList< Province > locationAreaModels;
	private Context mContext;


    public ExpandAdapter(ArrayList< Province > list) {
        if (list != null) {
            locationAreaModels = list;
        } else {
            locationAreaModels = new ArrayList<>();
        }
    }


    @Override
    public int getGroupCount() {
        return locationAreaModels.size();
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return locationAreaModels.get(groupPosition).areaGroup.size();
    }


    @Override
    public Object getGroup(int groupPosition) {
        return locationAreaModels.get(groupPosition);
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return locationAreaModels.get(groupPosition);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(com.hxqc.mall.thirdshop.R.layout.item_position_province, parent, false);
        }
        TextView text1 = (TextView) convertView.findViewById(com.hxqc.mall.thirdshop.R.id.text1);
        text1.setText(locationAreaModels.get(groupPosition).provinceName);
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(com.hxqc.mall.thirdshop.R.layout.item_position_city, parent, false);
        }
        TextView text2 = (TextView) convertView.findViewById(com.hxqc.mall.thirdshop.R.id.text2);
	    text2.setText(locationAreaModels.get(groupPosition).areaGroup.get(childPosition).title);
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
