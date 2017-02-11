package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.activity.auto.ParameterActivity;
import com.hxqc.mall.core.model.AutoParmeterGroup;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author: HuJunJie
 * Date: 2015-04-13
 * FIXME
 * Todo
 */
public class ParameterExpandableAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.OnHeaderUpdateListener {
    ArrayList<AutoParmeterGroup > mAutoParmeterGroups;
    Context context;

    public ParameterExpandableAdapter(Context context, ArrayList<AutoParmeterGroup> mAutoParmeterGroups) {
        this.mAutoParmeterGroups = mAutoParmeterGroups;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return mAutoParmeterGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mAutoParmeterGroups.get(groupPosition) == null ? 0 : mAutoParmeterGroups.get(groupPosition).chileParameter == null ? 0 : mAutoParmeterGroups.get(groupPosition).chileParameter.size();
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
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_parameter_group, parent, false);
        TextView mGroupLabelView = (TextView) convertView.findViewById(R.id.parameter_group_label);
        mGroupLabelView.setText(getGroup(groupPosition).groupLabel);
        ImageView mExpandedView = (ImageView) convertView.findViewById(R.id.parameter_group_expand);
        if (isExpanded) {
            mExpandedView.setImageResource(R.drawable.ic_cbb_arrow_up);
        } else mExpandedView.setImageResource(R.drawable.ic_cbb_arrow_down);
        return convertView;
    }

    @Override
    public AutoParmeterGroup getGroup(int groupPosition) {
        return mAutoParmeterGroups.get(groupPosition);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_parameter_child, null);
        TextView mChildLabelView = (TextView) convertView.findViewById(R.id.parameter_child_label);
        TextView mChildValueView = (TextView) convertView.findViewById(R.id.parameter_child_value);

        AutoParmeterGroup.AutoParameter autoParameter = getChild(groupPosition, childPosition);
        mChildLabelView.setText(autoParameter.label);
        mChildValueView.setText(autoParameter.value);
        return convertView;
    }

    @Override
    public AutoParmeterGroup.AutoParameter getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).chileParameter.get(childPosition);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public View getPinnedHeader() {
        View headerView = LayoutInflater.from(context).inflate(R.layout.item_parameter_group, null);
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        return headerView;
    }


    @Override
    public void updatePinnedHeader(View convertView, int groupPosition) {
        if (groupPosition == -1) return;
        TextView mGroupLabelView = (TextView) convertView.findViewById(R.id.parameter_group_label);
        mGroupLabelView.setText(getGroup(groupPosition).groupLabel);
        ImageView mExpandedView = (ImageView) convertView.findViewById(R.id.parameter_group_expand);
        if (((ParameterActivity) context).mExpandListView.isGroupExpanded(groupPosition)) {
            mExpandedView.setImageResource(R.drawable.ic_cbb_arrow_up);
        } else mExpandedView.setImageResource(R.drawable.ic_cbb_arrow_down);
    }
}
