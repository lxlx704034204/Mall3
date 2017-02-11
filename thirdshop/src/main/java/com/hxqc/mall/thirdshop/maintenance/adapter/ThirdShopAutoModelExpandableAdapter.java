package com.hxqc.mall.thirdshop.maintenance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.auto.model.AutoModel;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;

/**
 * Author: 胡仲俊
 * Date: 2016-04-20
 * FIXME
 * Todo 品牌列表
 */
public class ThirdShopAutoModelExpandableAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.OnHeaderUpdateListener {
    private static final String TAG = "ThirdShopSeriesExpandableAdapter";
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<AutoModel> modelArrayList;
    private String series;

    public ThirdShopAutoModelExpandableAdapter(Context context, ArrayList<AutoModel> modelGroups, String series) {
        this.modelArrayList = modelGroups;
        this.series = series;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void notifyData(ArrayList<AutoModel> modelGroups, String series) {
        this.modelArrayList = modelGroups;
        this.series = series;
        this.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return modelArrayList == null ? 0 : modelArrayList.size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return series;
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
            convertView = layoutInflater.inflate(R.layout.item_brandexpandable_group, parent, false);
        }
        convertView.setClickable(true);
        TextView textView = (TextView) convertView.findViewById(R.id.auto_brand_group_name);
        textView.setText(series);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_brandexpandable_child, parent, false);
            ImageView mBrandLogoView = (ImageView) convertView.findViewById(R.id.item_auto_logo);
            TextView mBrandNameView = (TextView) convertView.findViewById(R.id.item_auto_name);
            childHolder = new ChildHolder(mBrandLogoView, mBrandNameView);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        childHolder.setValue(getChild(groupPosition, childPosition));
        return convertView;
    }

    @Override
    public AutoModel getChild(int groupPosition, int childPosition) {
        return modelArrayList.get(childPosition);
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
        TextView textView = (TextView) headerView.findViewById(R.id.auto_brand_group_name);
        textView.setText(series);
    }

    class ChildHolder {
        TextView mBrandNameView;
        ImageView mBrandLogoView;

        ChildHolder(ImageView mBrandLogoView, TextView mBrandNameView) {
            this.mBrandLogoView = mBrandLogoView;
            this.mBrandNameView = mBrandNameView;
        }

        public void setValue(AutoModel autoModel) {
            mBrandNameView.setText(autoModel.autoModel);
            //缺少失败的图片和预加载图片
            mBrandLogoView.setVisibility(View.GONE);
/*            if (!TextUtils.isEmpty(autoSeries.seriesThumb)) {
                Picasso.with(context).load(autoSeries.seriesThumb).placeholder(R.drawable.pic_normal).error(R.drawable.pic_normal).into(mBrandLogoView);
            } else {
                mBrandLogoView.setImageResource(R.drawable.pic_normal);
            }*/
        }

    }
}
