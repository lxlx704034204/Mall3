package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hxqc.mall.core.model.HintMode;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;
import java.util.List;

import hxqc.mall.R;

/**
 * Author: liaoguilong
 * Date: 2015-11-26
 * FIXME Todo 搜索提示
 */
public class SearchHintExpandableAdapter_2 extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.OnHeaderUpdateListener {
    Context context;
    LayoutInflater layoutInflater;
    private List< String > groupArray;//车- 组列表
    private List< List< HintMode.Suggestion > > childArray;//车- 子列表

    public SearchHintExpandableAdapter_2(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public SearchHintExpandableAdapter_2(Context context, HintMode hintModes) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        initData(hintModes);
    }

    /**
     * 初始化数据
     *
     * @param hintModes
     */
    public void initData(HintMode hintModes) {
        groupArray = new ArrayList< String >();
        childArray = new ArrayList< List< HintMode.Suggestion > >();
        if (hintModes.electricVehicle != null && hintModes.electricVehicle.size() > 0) {
            groupArray.add("新能源电动车");
            childArray.add(hintModes.electricVehicle);
        }
        if (hintModes.auto != null && hintModes.auto.size() > 0) {
            groupArray.add("电商自营");
            childArray.add(hintModes.auto);
        }

    }

    public void notifyData(HintMode hintModes) {
        initData(hintModes);
        this.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return groupArray == null ? 0 : groupArray.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childArray == null ? 0 : childArray.get(groupPosition).size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        if(groupArray.size()==2)
        {
            switch (groupPosition) {
                case 0:
                    return 20;//新能源电动车
                case 1:
                    return 10;//电商自营
            }
        }
        return 10;
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
        TextView mTextView;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_searchhint_group, parent, false);
            mTextView = (TextView) convertView.findViewById(R.id.search_hint_group_label);
            convertView.setTag(mTextView);
        } else {
            mTextView = (TextView) convertView.getTag();
        }
        convertView.setClickable(true);
        mTextView.setText(getGroup(groupPosition));
        return convertView;
    }

    @Override
    public String getGroup(int groupPosition) {
        return groupArray.get(groupPosition);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_searchhint_child, parent, false);
        }

        TextView mLabelView = (TextView) convertView.findViewById(R.id.search_hint_child_label);
        HintMode.Suggestion Suggestion = getChild(groupPosition, childPosition);
        mLabelView.setText(Suggestion.suggestion);
        convertView.setTag(Suggestion);
        return convertView;
    }

    @Override
    public HintMode.Suggestion getChild(int groupPosition, int childPosition) {
        return childArray.get(groupPosition).get(childPosition);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getPinnedHeader() {
        View headerView = layoutInflater.inflate(R.layout.item_searchhint_group, null);
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        headerView.setClickable(true);
        return headerView;
    }

    @Override
    public void updatePinnedHeader(View headerView, int groupPosition) {
        if (groupPosition == -1) return;
        TextView mTextView = (TextView) headerView.findViewById(R.id.search_hint_group_label);
        mTextView.setText(getGroup(groupPosition));
    }

}
