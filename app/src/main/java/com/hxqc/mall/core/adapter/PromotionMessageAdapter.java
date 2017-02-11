package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hxqc.mall.core.model.PushMessage;
import com.hxqc.mall.core.model.PushMessageGroup;
import com.hxqc.mall.core.views.ExpandableTextView;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * 说明:活动消息数据适配
 *
 * author: 吕飞
 * since: 2015-04-01
 * Copyright:恒信汽车电子商务有限公司
 */
public class PromotionMessageAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.OnHeaderUpdateListener {
    Context mContext;
    ArrayList< PushMessageGroup > mPromotionMessageGroups;
    LayoutInflater mLayoutInflater;
    SparseBooleanArray mCollapsedStatus;

    public PromotionMessageAdapter(Context mContext, ArrayList< PushMessageGroup > mPromotionMessageGroups) {
        this.mContext = mContext;
        this.mPromotionMessageGroups = mPromotionMessageGroups;
        mCollapsedStatus = new SparseBooleanArray();
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mPromotionMessageGroups.get(groupPosition).pushMessages.size();
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
        if (convertView == null)
            convertView = mLayoutInflater.inflate(R.layout.item_message_group, parent, false);
        TextView mTimeView = (TextView) convertView.findViewById(R.id.time);
        mTimeView.setText(getGroup(groupPosition).date);
        return convertView;
    }

    @Override
    public PushMessageGroup getGroup(int groupPosition) {
        return mPromotionMessageGroups.get(groupPosition);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final PromotionMessageHolder mPromotionMessageHolder;
        PushMessage mPromotionMessage = getChild(groupPosition, childPosition);
        if (convertView == null) {
            mPromotionMessageHolder = new PromotionMessageHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_promotion_message_child, parent, false);
            mPromotionMessageHolder.mExpandableTextView = (ExpandableTextView) convertView.findViewById(R.id.expand_text_view);
            mPromotionMessageHolder.mTitleView = (TextView) convertView.findViewById(R.id.title);
            mPromotionMessageHolder.mLineView = convertView.findViewById(R.id.line);
            convertView.setTag(mPromotionMessageHolder);
        } else {
            mPromotionMessageHolder = (PromotionMessageHolder) convertView.getTag();
        }
        setColor(mPromotionMessageHolder, mPromotionMessage);
        mPromotionMessageHolder.mExpandableTextView.setText(mPromotionMessage.content, mCollapsedStatus, childPosition);
        mPromotionMessageHolder.mTitleView.setText(mPromotionMessage.title);
        if (isLastChild && groupPosition != getGroupCount() - 1) {
            mPromotionMessageHolder.mLineView.setVisibility(View.GONE);
        } else {
            mPromotionMessageHolder.mLineView.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public PushMessage getChild(int groupPosition, int childPosition) {
        return mPromotionMessageGroups.get(groupPosition).pushMessages.get(childPosition);
    }

    private void setColor(PromotionMessageHolder mPromotionMessageHolder, PushMessage mPromotionMessage) {
        if (mPromotionMessage.isRead == 1) {
            mPromotionMessageHolder.mExpandableTextView.mTv.setTextColor(mContext.getResources().getColor(R.color.straight_matter_and_secondary_text));
            mPromotionMessageHolder.mTitleView.setTextColor(mContext.getResources().getColor(R.color.straight_matter_and_secondary_text));
        } else {
            mPromotionMessageHolder.mExpandableTextView.mTv.setTextColor(mContext.getResources().getColor(R.color.title_and_main_text));
            mPromotionMessageHolder.mTitleView.setTextColor(mContext.getResources().getColor(R.color.text_color_orange));
        }
    }

    @Override
    public int getGroupCount() {
        return mPromotionMessageGroups.size();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getPinnedHeader() {
        View headerView = mLayoutInflater.inflate(R.layout.item_message_group, null);
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        headerView.setClickable(false);
        return headerView;
    }

    @Override
    public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
        if (firstVisibleGroupPos == -1) return;
        TextView mTimeView = (TextView) headerView.findViewById(R.id.time);
        mTimeView.setText(getGroup(firstVisibleGroupPos).date);
    }

    class PromotionMessageHolder {
        TextView mTitleView;
        ExpandableTextView mExpandableTextView;//可以上下展开的TextView
        View mLineView;
    }
}
