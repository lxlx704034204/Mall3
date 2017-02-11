package com.hxqc.mall.thirdshop.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.model.FourSNews;
import com.hxqc.mall.thirdshop.model.FourSSeries;
import com.hxqc.mall.thirdshop.model.FourSShop;
import com.hxqc.mall.thirdshop.model.SingleSeckillItem;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;

import java.util.ArrayList;

/**
 * 说明:4s店商城首页item
 *
 * @author: 吕飞
 * @since: 2016-05-05
 * Copyright:恒信汽车电子商务有限公司
 */
public class FourSHomeItem extends LinearLayout implements View.OnClickListener {
    public static final int NEWS = 0;
    public static final int NEW_CAR = 1;
    public static final int SHOP = 2;
    public static final int SECKILL = 3;
    RelativeLayout mTitleView;
    LinearLayout mRootView;
    int mItemType;
    TextView mTitleTextView;
    String mSiteID;

    public FourSHomeItem(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_4s_home_item, this);
        mRootView = (LinearLayout) findViewById(R.id.root);
        mTitleTextView = (TextView) findViewById(R.id.title_text);
        mTitleView = (RelativeLayout) findViewById(R.id.item_title);
        mTitleView.setOnClickListener(this);
    }

    public FourSHomeItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FourSHomeItem);
        mItemType = typedArray.getInteger(R.styleable.FourSHomeItem_itemType4S, 0);
        typedArray.recycle();
        initTitle();
    }

    private void initTitle() {
        switch (mItemType) {
            case NEWS:
                mTitleTextView.setText("资讯");
                break;
            case NEW_CAR:
                mTitleTextView.setText("新车销售");
                break;
            case SHOP:
                mTitleTextView.setText("推荐4S店");
                break;
            case SECKILL:
                mTitleTextView.setText("限时特价车");
                break;
        }
    }

    public void setNewsView(ArrayList<FourSNews> fourSNewses, String siteID) {
        mSiteID = siteID;
        mRootView.removeAllViews();
        int count = 3;
        if (fourSNewses.size() < 3) {
            count = fourSNewses.size();
        }
        for (int i = 0; i < count; i++) {
            FourSNewsItem fourSNewsItem = new FourSNewsItem(getContext());
            fourSNewsItem.addData(fourSNewses.get(i));
            mRootView.addView(fourSNewsItem);
        }
    }

    public void setNewCarView(ArrayList<FourSSeries> fourSSeries, String siteID) {
        mSiteID = siteID;
        mRootView.removeAllViews();
        int count = 3;
        if (fourSSeries.size() < 3) {
            count = fourSSeries.size();
        }
        for (int i = 0; i < count; i++) {
            NewCarSeriesItem newCarSeriesItem = new NewCarSeriesItem(getContext());
            newCarSeriesItem.addData(fourSSeries.get(i), siteID);
            mRootView.addView(newCarSeriesItem);
        }
    }

    public void setShopView(ArrayList<FourSShop> fourSShops, String siteID) {
        mSiteID = siteID;
        mRootView.removeAllViews();
        int count = 3;
        if (fourSShops.size() < 3) {
            count = fourSShops.size();
        }
        for (int i = 0; i < count; i++) {
            FourSShopItem fourSShopItem = new FourSShopItem(getContext());
            fourSShopItem.addData(fourSShops.get(i));
            mRootView.addView(fourSShopItem);
        }
    }

    private FlashSaleItem[] flashSaleItems = null;

    public void setSeckillView(ArrayList<SingleSeckillItem> singleSeckillItems, String siteID) {
        mSiteID = siteID;
        mRootView.removeAllViews();
        int count = 3;
        if (singleSeckillItems.size() < 3) {
            count = singleSeckillItems.size();
        }
        flashSaleItems = new FlashSaleItem[count];
        for (int i = 0; i < count; i++) {
            FlashSaleItem flashSaleItem = new FlashSaleItem(getContext());
            flashSaleItem.addData(singleSeckillItems.get(i));
            mRootView.addView(flashSaleItem);
            flashSaleItems[i] = flashSaleItem;
        }
    }

    public void destory() {
        if (flashSaleItems != null)
            for (FlashSaleItem flashSaleItem : flashSaleItems) {
                flashSaleItem.onDestroy();
            }
    }

    @Override
    public void onClick(View v) {
        switch (mItemType) {
            case NEWS:
                ActivitySwitcherThirdPartShop.toSiteNewsList(getContext(), mSiteID);
                break;
            case NEW_CAR:
                ActivitySwitcherThirdPartShop.toNewCarSaleList(getContext(), mSiteID);
                break;
            case SHOP:
                ActivitySwitcherThirdPartShop.toFilterThirdSpecialActivity(getContext(), "", null, "");
//                ActivitySwitcherThirdPartShop.to4SShopList(getContext(), mSiteID);
                break;
            case SECKILL:
                ActivitySwitcherThirdPartShop.toFlashSaleList(getContext(), mSiteID, true);
                break;
        }
    }
}
