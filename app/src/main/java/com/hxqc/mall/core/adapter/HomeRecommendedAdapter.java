package com.hxqc.mall.core.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.model.HomeRecommendedModel;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author: wanghao
 * Date: 2015-04-20
 * FIXME
 * 首页 推荐车型 adapter
 */
public class HomeRecommendedAdapter extends BaseAdapter {

    public ArrayList< HomeRecommendedModel > homeRecommendedModels;

    Context context;

    public HomeRecommendedAdapter(Context context, ArrayList< HomeRecommendedModel > homeRecommendedModels) {
        this.homeRecommendedModels = homeRecommendedModels;
        this.context = context;
    }


    @Override
    public int getCount() {
        return homeRecommendedModels == null ? 0 : homeRecommendedModels.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderRecommend viewHolderRecommend;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home_recommended, parent,false);
            viewHolderRecommend = new ViewHolderRecommend(convertView);

            convertView.setTag(viewHolderRecommend);
        } else {
            viewHolderRecommend = (ViewHolderRecommend) convertView.getTag();
        }
        viewHolderRecommend.setValue(getItem(position));
        return convertView;
    }

    @Override
    public HomeRecommendedModel getItem(int position) {
        return homeRecommendedModels.get(position);
    }

    public class ViewHolderRecommend {

        RelativeLayout mWholeClick;
        ImageView mThumb;
        TextView mTitle;
        TextView mPrice;

        public ViewHolderRecommend(View itemView) {
            mWholeClick = (RelativeLayout) itemView.findViewById(R.id.rl_recommend_onclick);
            mThumb = (ImageView) itemView.findViewById(R.id.iv_auto_image);
            mTitle = (TextView) itemView.findViewById(R.id.tv_auto_name);
            mPrice = (TextView) itemView.findViewById(R.id.tv_auto_price);
        }

        public void setValue(final HomeRecommendedModel model) {
            ImageUtil.setImage(context, mThumb, model.thumb);
            mTitle.setText(model.title);
            mPrice.setText(String.format("¥%s", model.getItemPriceU()));
            mWholeClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivitySwitcher.toAutoItemDetail(context, AutoItem.AUTO_PROMOTION,
                            model.getItemID(), model.getItemDescription());
                }
            });
        }
    }
}
