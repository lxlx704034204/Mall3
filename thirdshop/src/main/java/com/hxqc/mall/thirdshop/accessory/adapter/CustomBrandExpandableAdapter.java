package com.hxqc.mall.thirdshop.accessory.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.model.Brand;
import com.hxqc.mall.thirdshop.accessory.model.BrandGroup;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;

/**
 * 用品销售 筛选条 品牌 Left adapter
 * Created by huangyi on 16/5/30.
 */
public class CustomBrandExpandableAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.OnHeaderUpdateListener {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<BrandGroup> brandArrayList;

    public CustomBrandExpandableAdapter(Context context, ArrayList<BrandGroup> brandArrayList) {
        this.brandArrayList = brandArrayList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void notifyData(ArrayList<BrandGroup> brandArrayList) {
        this.brandArrayList = brandArrayList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return brandArrayList == null ? 0 : brandArrayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return getGroup(groupPosition).group.size();
    }

    @Override
    public BrandGroup getGroup(int groupPosition) {
        return brandArrayList.get(groupPosition);
    }

    @Override
    public Brand getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).group.get(childPosition);
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
            convertView = layoutInflater.inflate(R.layout.item_custom_brand_expandable, parent, false);
        convertView.setClickable(true);
        TextView textView = (TextView) convertView.findViewById(R.id.name);
        textView.setText(getGroup(groupPosition).groupTag);
        textView.setTextColor(ContextCompat.getColor(convertView.getContext(), R.color.text_color_subheading));
        //字体加粗
        TextPaint tp = textView.getPaint();
        tp.setFakeBoldText(true);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder;
        if (convertView == null) {
            childHolder = new ChildHolder();
            convertView = layoutInflater.inflate(R.layout.item_custom_brand_expandable, parent, false);
            childHolder.mParentView = (LinearLayout) convertView.findViewById(R.id.parent);
            childHolder.mNameView = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        Brand brand = getChild(groupPosition, childPosition);
        if (brand.isChecked) {
            childHolder.mParentView.setBackgroundColor(Color.parseColor("#e9eaeb"));
            childHolder.mNameView.setTextColor(Color.parseColor("#ff7043"));
        } else {
            childHolder.mParentView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            childHolder.mNameView.setTextColor(Color.parseColor("#de000000"));
        }
        childHolder.mNameView.setText(brand.brandName);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getPinnedHeader() {
        return null;
    }

    @Override
    public void updatePinnedHeader(View headerView, int groupPosition) {

    }

    class ChildHolder {
        LinearLayout mParentView;
        TextView mNameView;
    }
}
