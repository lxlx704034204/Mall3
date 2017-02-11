package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.Series;
import com.hxqc.mall.thirdshop.model.TCarSeriesModel;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;

/**
 * Function: 4S店车系列表Adapter
 *
 * @author 袁秉勇
 * @since 2016年06月22日
 */
public class SeriesExpandableAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.OnHeaderUpdateListener {
    Context mContext;
    LayoutInflater layoutInflater;
    ArrayList<TCarSeriesModel> tCarSeriesModels;
    private boolean fromCarCompare;   //是否来自车型对比  by zf

    public SeriesExpandableAdapter(Context context, ArrayList<TCarSeriesModel> tCarSeriesModels) {
        this.tCarSeriesModels = tCarSeriesModels;
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }


    public void notifyData(ArrayList<TCarSeriesModel> brandArrayList) {
        this.tCarSeriesModels = brandArrayList;
        this.notifyDataSetChanged();
    }


    @Override
    public int getGroupCount() {
        return tCarSeriesModels == null ? 0 : tCarSeriesModels.size();
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return getGroup(groupPosition).series.size();
    }


    @Override
    public TCarSeriesModel getGroup(int groupPosition) {
        return tCarSeriesModels.get(groupPosition);
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
            convertView = layoutInflater.inflate(fromCarCompare ? R.layout.item_filter_model_tag : R.layout.item_brandexpandable_group, parent, false);
        }
        convertView.setClickable(true);
        if (!fromCarCompare) {
            TextView textView = (TextView) convertView.findViewById(R.id.auto_brand_group_name);
            textView.setText(getGroup(groupPosition).brandName);
        } else {
            TextView textView = (TextView) convertView.findViewById(R.id.text1);
            textView.setText(getGroup(groupPosition).brandName);
        }

        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.t_car_series_recyclerview_item, parent, false);
            childHolder = new ChildHolder();

            childHolder.mCarSeriesNameView = (TextView) convertView.findViewById(R.id.car_series_name);
            childHolder.mCarSeriesPriceView = (TextView) convertView.findViewById(R.id.car_series_price);
            childHolder.mCarSeriesImageView = (ImageView) convertView.findViewById(R.id.car_series_image);

            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        childHolder.mCarSeriesPriceView.setVisibility(fromCarCompare ? View.GONE : View.VISIBLE);
        childHolder.mCarSeriesImageView.setVisibility(fromCarCompare ? View.GONE : View.VISIBLE);

        Series series = getGroup(groupPosition).series.get(childPosition);
        childHolder.mCarSeriesNameView.setText(VerifyString(series.seriesName));
        childHolder.mCarSeriesPriceView.setText(OtherUtil.formatPriceRange(series.priceRange));
        ImageUtil.setImage(mContext, childHolder.mCarSeriesImageView, series.seriesThumb);

        return convertView;
    }

    public String VerifyString(String str) {
        if (!TextUtils.isEmpty(str)) {
//            if (str.length() > 1 && "系".equals(str.substring(str.length() - 1, str.length()))) {
//                return str + "列车型";
//            } else
            if (str.length() > 2 && "系列".equals(str.substring(str.length() - 2, str.length()))) {
                return str + "车型";
            }
            return str + "系列车型";
        }
        return "未知车系车型";
    }


    @Override
    public Series getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).series.get(childPosition);
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    @Override
    public View getPinnedHeader() {
        View headerView = layoutInflater.inflate(fromCarCompare ? R.layout.item_filter_model_tag : R.layout.item_brandexpandable_group, null);
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        headerView.setClickable(true);
        return headerView;
    }


    @Override
    public void updatePinnedHeader(View headerView, int groupPosition) {
        if (groupPosition == -1) return;

        if (!fromCarCompare) {
            TextView textView = (TextView) headerView.findViewById(R.id.auto_brand_group_name);
            textView.setText(getGroup(groupPosition).brandName);
        } else {
            TextView textView = (TextView) headerView.findViewById(R.id.text1);
            textView.setText(getGroup(groupPosition).brandName);
        }

    }

    public void setShowAllBrandFilter(boolean fromCarCompare) {
        this.fromCarCompare = fromCarCompare;
    }


    class ChildHolder {
        TextView mCarSeriesNameView;
        TextView mCarSeriesPriceView;
        ImageView mCarSeriesImageView;
    }
}
