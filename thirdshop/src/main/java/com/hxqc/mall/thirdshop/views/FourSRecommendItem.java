package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.FourSNews;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;

/**
 * 说明:4s店首页推荐
 *
 * @author: 吕飞
 * @since: 2016-05-11
 * Copyright:恒信汽车电子商务有限公司
 */
public class FourSRecommendItem extends LinearLayout {
    ImageView mNewsPhotoView;
    TextView mNewsTitleView;
    TextView mNewsDateView;

    public FourSRecommendItem(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_4s_news_item, this);
        mNewsPhotoView = (ImageView) findViewById(R.id.news_photo);
        mNewsTitleView = (TextView) findViewById(R.id.news_title);
        mNewsDateView = (TextView) findViewById(R.id.news_date);
    }

    public FourSRecommendItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void addData(final FourSNews fourSNews) {
        ImageUtil.setImage(getContext(), mNewsPhotoView, fourSNews.thumbSmall);
        mNewsTitleView.setText(fourSNews.title);
        mNewsDateView.setText(fourSNews.publishDate);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //咨询详情
                switch (fourSNews.newsKind) {
                    case 10:
                        ActivitySwitchBase.toH5Activity(getContext(), "资讯详情", fourSNews.defaultNews.url);
                        break;
                    case 20:
                        ActivitySwitcherThirdPartShop.toCarDetail(fourSNews.newAuto.itemID, fourSNews.newAuto.shopID, "", getContext());
                        break;
                    case 30:
                        ActivitySwitcherThirdPartShop.toSpecialCarDetail(getContext(), fourSNews.newAuto.itemID);
                        break;
                    case 40:
                    case 50:
                    case 60:
                        ActivitySwitcherThirdPartShop.toSalesItemDetail(fourSNews.promotion.promotionID, getContext());
                        break;
                }
            }
        });
    }
}
