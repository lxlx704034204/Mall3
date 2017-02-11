package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.auto.util.TestDataUtil;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 11
 * FIXME
 * Todo 选择车型
 */
@Deprecated
public class AutoExpandableAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.OnHeaderUpdateListener {

    private static final String TAG = "BrandExpandableAdapter";
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<TestDataUtil.AutoSeriesGroup> autoSeriesGroup;

    public AutoExpandableAdapter(Context context, ArrayList<TestDataUtil.AutoSeriesGroup> autoSeriesGroup) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.autoSeriesGroup = autoSeriesGroup;
    }

    public void notifyData(ArrayList<TestDataUtil.AutoSeriesGroup> autoSeriesGroup) {
        this.autoSeriesGroup = autoSeriesGroup;
        this.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return autoSeriesGroup == null ? 0 : autoSeriesGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return autoSeriesGroup == null ? 0 : (autoSeriesGroup.get(groupPosition) == null ? 0 : autoSeriesGroup.get(groupPosition).group.size());
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
            convertView = layoutInflater.inflate(R.layout.item_brandexpandable_group, parent, false);
        convertView.setClickable(true);
        TextView textView = (TextView) convertView.findViewById(R.id.auto_brand_group_name);
        textView.setText(getGroup(groupPosition).groupTag);
        return convertView;
    }

    @Override
    public TestDataUtil.AutoSeriesGroup getGroup(int groupPosition) {
        return autoSeriesGroup.get(groupPosition);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_brandexpandable_child, null);
            ImageView mSeriesLogoView = (ImageView) convertView.findViewById(R.id.item_auto_logo);
            TextView mSeriesNameView = (TextView) convertView.findViewById(R.id.item_auto_name);
            childHolder = new ChildHolder(mSeriesLogoView, mSeriesNameView);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        childHolder.setValue(getChild(groupPosition, childPosition));

        return convertView;
    }

    @Override
    public TestDataUtil.Series getChild(int groupPosition, int childPosition) {
        return autoSeriesGroup.get(groupPosition).group.get(childPosition);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getPinnedHeader() {
        View headerView = layoutInflater.inflate(R.layout.item_brandexpandable_group, null);
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        headerView.setClickable(true);
        return headerView;
    }

    @Override
    public void updatePinnedHeader(View headerView, int groupPosition) {
        if (groupPosition == -1) return;
        TextView textView = (TextView) headerView
                .findViewById(R.id.auto_brand_group_name);

        textView.setText(getGroup(groupPosition).groupTag);
    }

    class ChildHolder {
        TextView mSeriesNameView;
        ImageView mSeriesLogoView;

        ChildHolder(ImageView mSeriesLogoView, TextView mSeriesNameView) {
            this.mSeriesLogoView = mSeriesLogoView;
            this.mSeriesNameView = mSeriesNameView;
        }

        public void setValue(TestDataUtil.Series autoAutoSeries) {
            mSeriesNameView.setText(String.format("%s系列车型", autoAutoSeries.getSeriesName()));
            if (TextUtils.isEmpty(autoAutoSeries.getSeriesThumb())) {
                mSeriesLogoView.setImageResource(R.drawable.pic_normal);
            } else {
                ImageUtil.setImage(context, mSeriesLogoView, autoAutoSeries.getSeriesThumb());
            }
        }
    }
}
