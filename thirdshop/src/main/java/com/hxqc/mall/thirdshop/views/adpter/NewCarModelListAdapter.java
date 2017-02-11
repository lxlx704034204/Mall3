package com.hxqc.mall.thirdshop.views.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.PriceInfo;
import com.hxqc.mall.thirdshop.model.Series;
import com.hxqc.mall.thirdshop.model.newcar.ModelListInfo;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.views.ReferenceDetailsDialog;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.List;

/**
 * 新车 车型列表的adapter
 * Created by 赵帆
 */
public class NewCarModelListAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.OnHeaderUpdateListener {
    List<ModelListInfo> mAutoParmeterGroups;
    Context context;
    String siteId, brand, series, shopSiteFrom;
    private boolean show = true;

    public NewCarModelListAdapter(Context context, List<ModelListInfo> mAutoParmeterGroups) {
        this.mAutoParmeterGroups = mAutoParmeterGroups;
        this.context = context;
    }

    public void setParameter(String siteId, String brand, String series, String shopSiteFrom) {
        this.siteId = siteId;
        this.brand = brand;
        this.series = series;
        this.shopSiteFrom = shopSiteFrom;
    }

    @Override
    public int getGroupCount() {
        return mAutoParmeterGroups.isEmpty() ? 0 : mAutoParmeterGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mAutoParmeterGroups.get(groupPosition) == null ?
                0 : mAutoParmeterGroups.get(groupPosition).model == null ? 0 : mAutoParmeterGroups.get(groupPosition).model.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_newcarmodelist_tag, parent, false);
        TextView mGroupLabelView = (TextView) convertView.findViewById(R.id.text1);
        mGroupLabelView.setText(getGroup(groupPosition).getModelGroupTag());

        return convertView;
    }

    @Override
    public ModelListInfo getGroup(int groupPosition) {
        return mAutoParmeterGroups.get(groupPosition);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = convertView == null ? new ViewHolder() : (ViewHolder) convertView.getTag();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_newcarmodelist, null);
            viewHolder.car_title = (TextView) convertView.findViewById(R.id.car_title);
            viewHolder.car_content_1 = (TextView) convertView.findViewById(R.id.car_content_1);
            viewHolder.car_content_4 = (TextView) convertView.findViewById(R.id.car_content_4);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.show_price_popupView);
            viewHolder.ask_price = (TextView) convertView.findViewById(R.id.ask_price);

            convertView.setTag(viewHolder);
        }
        final ModelListInfo.model model = getChild(groupPosition, childPosition);

        viewHolder.car_title.setText(model.modelName);
        viewHolder.car_content_1.setText("¥" + OtherUtil.formatPriceSingleOrRange(model.priceRange));
        viewHolder.car_content_4.setText("¥" + OtherUtil.formatPriceSingleOrRange(model.itemOrigPrice));


        //价格明细
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PriceInfo mPriceInfo = model.priceInfo;
                if (mPriceInfo != null) {
                    // mPriceInfo.itemPrice = Float.valueOf(model.priceRange.split("-")[0].trim());
                    ReferenceDetailsDialog dialog = new ReferenceDetailsDialog(context, mPriceInfo);
                    dialog.show();
                }
            }
        });
        //询价
        viewHolder.ask_price.setVisibility(show ? View.VISIBLE : View.GONE);
        viewHolder.ask_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Series mSeries = new Series();
                mSeries.setSeriesName(series);
                ActivitySwitcherThirdPartShop.
                        toAskLeastPrice2(context, siteId, shopSiteFrom, model.extID, brand, mSeries, model.modelName);
            }
        });

        //跳转车型详情
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherThirdPartShop.toNewCarModelDetail(context, siteId, model.extID, brand, model.modelName);
            }
        });


        return convertView;
    }

    public void showShopAskPrice(boolean show) {
        this.show = show;
    }

    class ViewHolder {
        TextView car_title;
        TextView car_content_1;
        TextView car_content_4;
        ImageView img;
        TextView ask_price;
    }


    @Override
    public ModelListInfo.model getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).model.get(childPosition);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    @Override
    public View getPinnedHeader() {
        return null;
    }

    @Override
    public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {

    }
}
