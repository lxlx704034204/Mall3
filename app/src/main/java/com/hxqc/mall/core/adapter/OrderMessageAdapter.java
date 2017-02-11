//package com.hxqc.hxqcmall.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.BaseExpandableListAdapter;
//import android.widget.TextView;
//
//import com.hxqc.widget.PinnedHeaderExpandableListView;
//
//import OrderMessage;
//import OrderMessageGroup;
//
//import java.util.ArrayList;
//
///**
// * 说明:订单消息适配器
// *
// * author: 吕飞
// * since: 2015-04-02
// * Copyright:恒信汽车电子商务有限公司
// */
//@Deprecated
//public class OrderMessageAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.OnHeaderUpdateListener {
//    Context mContext;
//    ArrayList< OrderMessageGroup > mOrderMessageGroups;
//    LayoutInflater mLayoutInflater;
//
//    public OrderMessageAdapter(Context mContext, ArrayList< OrderMessageGroup > mOrderMessageGroups) {
//        this.mContext = mContext;
//        this.mOrderMessageGroups = mOrderMessageGroups;
//        mLayoutInflater = LayoutInflater.from(mContext);
//    }
//
//    @Override
//    public int getChildrenCount(int groupPosition) {
//        return mOrderMessageGroups.get(groupPosition).orderMessages.size();
//    }
//
//    @Override
//    public long getGroupId(int groupPosition) {
//        return groupPosition;
//    }
//
//    @Override
//    public long getChildId(int groupPosition, int childPosition) {
//        return childPosition;
//    }
//
//    @Override
//    public boolean hasStableIds() {
//        return false;
//    }
//
//    @Override
//    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//        if (convertView == null)
//            convertView = mLayoutInflater.inflate(R.layout.item_message_group, parent, false);
//        TextView mTimeView = (TextView) convertView.findViewById(R.id.time);
//        mTimeView.setText(getGroup(groupPosition).date);
//        return convertView;
//    }
//
//    @Override
//    public OrderMessageGroup getGroup(int groupPosition) {
//        return mOrderMessageGroups.get(groupPosition);
//    }
//
//    @Override
//    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        final OrderMessageHolder mOrderMessageHolder;
//        OrderMessage mOrderMessage = getChild(groupPosition, childPosition);
//        if (convertView == null) {
//            mOrderMessageHolder = new OrderMessageHolder();
//            convertView = mLayoutInflater.inflate(R.layout.item_order_message_child, parent, false);
//            mOrderMessageHolder.mContentView = (TextView) convertView.findViewById(R.id.content);
//            mOrderMessageHolder.mTitleView = (TextView) convertView.findViewById(R.id.title);
//            mOrderMessageHolder.mLineView = convertView.findViewById(R.id.line);
//            convertView.setTag(mOrderMessageHolder);
//        } else {
//            mOrderMessageHolder = (OrderMessageHolder) convertView.getTag();
//        }
//        setColor(mOrderMessageHolder, mOrderMessage);
//        mOrderMessageHolder.mContentView.setText(mOrderMessage.content);
//        mOrderMessageHolder.mTitleView.setText(mOrderMessage.title);
//        if (isLastChild && groupPosition != getGroupCount() - 1) {
//            mOrderMessageHolder.mLineView.setVisibility(View.GONE);
//        } else {
//            mOrderMessageHolder.mLineView.setVisibility(View.VISIBLE);
//        }
//        return convertView;
//    }
//
//    @Override
//    public OrderMessage getChild(int groupPosition, int childPosition) {
//        return mOrderMessageGroups.get(groupPosition).orderMessages.get(childPosition);
//    }
//
//    private void setColor(OrderMessageHolder mOrderMessageHolder, OrderMessage mOrderMessage) {
//        if (mOrderMessage.isRead == 1) {
//            mOrderMessageHolder.mTitleView.setTextColor(mContext.getResources().getColor(R.color.straight_matter_and_secondary_text));
//        } else {
//            mOrderMessageHolder.mTitleView.setTextColor(mContext.getResources().getColor(R.color.title_and_main_text));
//        }
//    }
//
//    @Override
//    public int getGroupCount() {
//        return mOrderMessageGroups.size();
//    }
//
//    @Override
//    public boolean isChildSelectable(int groupPosition, int childPosition) {
//        return true;
//    }
//
//    @Override
//    public View getPinnedHeader() {
//        View headerView = mLayoutInflater.inflate(R.layout.item_message_group, null);
//        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
//        headerView.setClickable(false);
//        return headerView;
//    }
//
//    @Override
//    public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
//        if (firstVisibleGroupPos == -1) return;
//        TextView mTimeView = (TextView) headerView.findViewById(R.id.time);
//        mTimeView.setText(getGroup(firstVisibleGroupPos).date);
//    }
//
//    class OrderMessageHolder {
//        TextView mTitleView;
//        TextView mContentView;
//        View mLineView;
//    }
//}
//
