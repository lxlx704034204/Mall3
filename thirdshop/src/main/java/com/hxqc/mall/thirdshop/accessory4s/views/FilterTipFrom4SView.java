package com.hxqc.mall.thirdshop.accessory4s.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory.adapter.CustomBrandExpandableAdapter;
import com.hxqc.mall.thirdshop.accessory.adapter.CustomConditionAdapter;
import com.hxqc.mall.thirdshop.accessory.model.AccessoryBigCategory;
import com.hxqc.mall.thirdshop.accessory.model.AccessorySmallCategory;
import com.hxqc.mall.thirdshop.accessory.model.Brand;
import com.hxqc.mall.thirdshop.accessory.model.BrandGroup;
import com.hxqc.mall.thirdshop.accessory.model.ProductListFilter;
import com.hxqc.mall.thirdshop.accessory.model.Series;
import com.hxqc.mall.thirdshop.accessory4s.api.Accessory4SApiClient;
import com.hxqc.util.DisplayTools;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * 用品销售 筛选条 4s店版
 * Created by huangyi on 16/5/30.
 */
public class FilterTipFrom4SView extends LinearLayout implements View.OnClickListener {

    ProductListFilter mProductListFilter; //数据源
    OnFilterClickListener mOnFilterClickListener;

    String mSiteID = "", mShopID = "", mLatitude = "", mLongitude = "";
    ArrayList<View> mShadeList = new ArrayList<>(); //可能有多个遮罩
    View mShadeView, mBrandRightHeaderView, mClassRightHeaderView; //单个遮罩 品牌2级分类头部 品类2级分类头部
    FrameLayout mConditionParentView; //parent
    LinearLayout mBrandConditionParentView, mClassConditionParentView;
    InnerPinnedHeaderExpandableListView mBrandConditionLeftView;
    InnerListView mBrandConditionRightView, mClassConditionLeftView, mClassConditionRightView;
    RadioButton mBrandView, mClassView, mPriceView;
    boolean mBrandViewIsChecked, mClassViewIsChecked;
    int mGroupPosition = -1, mChildPosition = -1, mBrandRightPosition = -1, mClassLeftPosition = -1, mClassRightPosition = -1;
    Status mStatus = Status.UNSELECTED;

    CustomBrandExpandableAdapter mBrandConditionLeftAdapter;
    CustomConditionAdapter mBrandConditionRightAdapter, mClassConditionLeftAdapter, mClassConditionRightAdapter;
    /**
     * 临时存储
     **/
    int mGroupPositionTemp, mChildPositionTemp, mClassLeftPositionTemp;
    ArrayList<Series> mSeries = new ArrayList<>(); //车系
    ArrayList<AccessorySmallCategory> mClass2nd = new ArrayList<>(); //二级分类

    public FilterTipFrom4SView(Context context) {
        this(context, null);
    }

    public FilterTipFrom4SView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterTipFrom4SView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View root = LayoutInflater.from(context).inflate(R.layout.view_filter_tip_from_4s, this);

        mConditionParentView = (FrameLayout) root.findViewById(R.id.filter_tip_condition_parent);
        mBrandConditionParentView = (LinearLayout) root.findViewById(R.id.filter_tip_brand_condition_parent);
        mBrandConditionLeftView = (InnerPinnedHeaderExpandableListView) root.findViewById(R.id.filter_tip_brand_condition_left);
        mBrandConditionRightView = (InnerListView) root.findViewById(R.id.filter_tip_brand_condition_right);
        mClassConditionParentView = (LinearLayout) root.findViewById(R.id.filter_tip_class_condition_parent);
        mClassConditionLeftView = (InnerListView) root.findViewById(R.id.filter_tip_class_condition_left);
        mClassConditionRightView = (InnerListView) root.findViewById(R.id.filter_tip_class_condition_right);

        mBrandView = (RadioButton) root.findViewById(R.id.filter_tip_brand);
        mClassView = (RadioButton) root.findViewById(R.id.filter_tip_class);
        mPriceView = (RadioButton) root.findViewById(R.id.filter_tip_price);

        mBrandView.setOnClickListener(this);
        mClassView.setOnClickListener(this);
        mPriceView.setOnClickListener(this);

        ViewGroup.LayoutParams params = mConditionParentView.getLayoutParams();
        params.height = DisplayTools.getScreenHeight(context) / 5 * 3;
        mConditionParentView.setLayoutParams(params);
    }

    public void setOnFilterClickListener(OnFilterClickListener mOnFilterClickListener) {
        this.mOnFilterClickListener = mOnFilterClickListener;
    }

    public void initData(String mSiteID, String mShopID, String mLatitude, String mLongitude) {
        this.mSiteID = mSiteID;
        this.mShopID = mShopID;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        loadData();
    }

    public void setShadeList(ArrayList<View> mShadeList) {
        this.mShadeList = mShadeList;
        for (View v : this.mShadeList) {
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideConditionRootView();
                }
            });
        }
    }

    public void setShadeView(View mShadeView) {
        this.mShadeView = mShadeView;
        this.mShadeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hideConditionRootView();
            }
        });
    }

    public void clearCondition() {
        hideConditionRootView();
        mGroupPosition = -1;
        mChildPosition = -1;
        mBrandRightPosition = -1;
        mBrandView.setText("品牌");
        mClassLeftPosition = -1;
        mClassRightPosition = -1;
        mClassView.setText("品类");
        mStatus = Status.UNSELECTED;
        updateStatus();
    }

    private void loadData() {
        new Accessory4SApiClient().filter(mSiteID, mShopID, mLatitude, mLongitude, new LoadingAnimResponseHandler(getContext(), false) {
            @Override
            public void onSuccess(String response) {
                mProductListFilter = JSONUtils.fromJson(response, ProductListFilter.class);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.filter_tip_brand) {
            if (null == mProductListFilter || null == mProductListFilter.brandList || 0 == mProductListFilter.brandList.size()) {
                loadData();
            } else {
                mBrandViewIsChecked = !mBrandViewIsChecked;
                if (mBrandViewIsChecked) {
                    mBrandView.setCompoundDrawables(null, null, getUpDrawable(), null);
                    showConditionRootView(-1);

                    if (mClassViewIsChecked) {
                        mClassView.setCompoundDrawables(null, null, getDownDrawable(), null);
                        mClassViewIsChecked = false;
                    }
                } else { //2次点击收起
                    mBrandView.setCompoundDrawables(null, null, getDownDrawable(), null);
                    for (View v : mShadeList) {
                        v.setVisibility(GONE);
                    }
                    if (null != mShadeView) mShadeView.setVisibility(GONE);
                    mConditionParentView.setVisibility(GONE);
                }
            }

        } else if (id == R.id.filter_tip_class) {
            if (null == mProductListFilter || null == mProductListFilter.classification || 0 == mProductListFilter.classification.size()) {
                loadData();
            } else {
                mClassViewIsChecked = !mClassViewIsChecked;
                if (mClassViewIsChecked) {
                    mClassView.setCompoundDrawables(null, null, getUpDrawable(), null);
                    showConditionRootView(0);

                    if (mBrandViewIsChecked) {
                        mBrandView.setCompoundDrawables(null, null, getDownDrawable(), null);
                        mBrandViewIsChecked = false;
                    }
                } else { //2次点击收起
                    mClassView.setCompoundDrawables(null, null, getDownDrawable(), null);
                    for (View v : mShadeList) {
                        v.setVisibility(GONE);
                    }
                    if (null != mShadeView) mShadeView.setVisibility(GONE);
                    mConditionParentView.setVisibility(GONE);
                }
            }

        } else if (id == R.id.filter_tip_price) {
            hideConditionRootView();
            if (Status.UNSELECTED == mStatus) {
                mStatus = Status.DOWN;
                if (null != mOnFilterClickListener) mOnFilterClickListener.onPriceClick("desc");

            } else if (Status.DOWN == mStatus) {
                mStatus = Status.UP;
                if (null != mOnFilterClickListener) mOnFilterClickListener.onPriceClick("asc");

            } else if (Status.UP == mStatus) {
                mStatus = Status.DOWN;
                if (null != mOnFilterClickListener) mOnFilterClickListener.onPriceClick("desc");
            }
            updateStatus();
        }
    }

    /**
     * 显示 条件
     **/
    private void showConditionRootView(final int ID) {
        for (View v : mShadeList) {
            v.setVisibility(VISIBLE);
        }
        if (null != mShadeView) mShadeView.setVisibility(VISIBLE);
        mConditionParentView.setVisibility(VISIBLE);

        switch (ID) {
            case -1: //品牌
                mBrandConditionParentView.setVisibility(VISIBLE);
                mClassConditionParentView.setVisibility(GONE);

                mGroupPositionTemp = mGroupPosition;
                mChildPositionTemp = mChildPosition;
                if (null == mBrandConditionLeftAdapter) {
                    //加头 不限
                    View brandLeftHeaderView = View.inflate(getContext(), R.layout.item_custom_brand_expandable, null);
                    mBrandRightHeaderView = View.inflate(getContext(), R.layout.item_custom_brand_expandable, null);
                    mBrandConditionLeftView.addHeaderView(brandLeftHeaderView);
                    brandLeftHeaderView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mGroupPosition = -1;
                            mChildPosition = -1;
                            mBrandRightPosition = -1;
                            mBrandView.setText("品牌");
                            hideConditionRootView();
                            mStatus = Status.UNSELECTED;
                            updateStatus();
                            if (null != mOnFilterClickListener)
                                mOnFilterClickListener.onBrandClick("", "");
                        }
                    });

                    mBrandConditionLeftAdapter = new CustomBrandExpandableAdapter(getContext(), mProductListFilter.brandList);
                    mBrandConditionLeftView.setAdapter(mBrandConditionLeftAdapter);
                    OtherUtil.openAllChild(mBrandConditionLeftAdapter, mBrandConditionLeftView);
                    mBrandConditionLeftView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                            return true;
                        }
                    }, false);
                    mBrandConditionLeftView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                            mGroupPositionTemp = groupPosition;
                            mChildPositionTemp = childPosition;
                            for (BrandGroup bg : mProductListFilter.brandList) {
                                for (Brand b : bg.group) {
                                    b.isChecked = false;
                                }
                            }
                            Brand brand = mProductListFilter.brandList.get(groupPosition).group.get(childPosition);
                            brand.isChecked = true;
                            mBrandConditionLeftAdapter.notifyDataSetChanged();

                            //品牌2级联动
                            if (null == mBrandConditionRightAdapter) {
                                //加头 不限
                                mBrandConditionRightView.addHeaderView(mBrandRightHeaderView);
                                mBrandRightHeaderView.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mGroupPosition = mGroupPositionTemp;
                                        mChildPosition = mChildPositionTemp;
                                        mBrandRightPosition = -1;
                                        mBrandView.setText(mProductListFilter.brandList.get(mGroupPosition).group.get(mChildPosition).brandName);
                                        hideConditionRootView();
                                        mStatus = Status.UNSELECTED;
                                        updateStatus();
                                        if (null != mOnFilterClickListener)
                                            mOnFilterClickListener.onBrandClick(mProductListFilter.brandList.get(mGroupPosition).group.get(mChildPosition).brandName, "");
                                    }
                                });

                                mBrandConditionRightAdapter = new CustomConditionAdapter(getContext(), CustomConditionAdapter.Type.SERIES, mSeries);
                                mBrandConditionRightView.setAdapter(mBrandConditionRightAdapter);
                                mBrandConditionRightView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        if (position != 0) {
                                            mGroupPosition = mGroupPositionTemp;
                                            mChildPosition = mChildPositionTemp;
                                            mBrandRightPosition = position - 1;
                                            mBrandView.setText(mSeries.get(position - 1).seriesName);
                                            hideConditionRootView();
                                            mStatus = Status.UNSELECTED;
                                            updateStatus();
                                            if (null != mOnFilterClickListener)
                                                mOnFilterClickListener.onBrandClick(mProductListFilter.brandList.get(mGroupPosition).group.get(mChildPosition).brandName, mSeries.get(position - 1).seriesName);
                                        }
                                    }
                                });
                            }
                            mSeries.clear();
                            mSeries.addAll(brand.seriesList);
                            for (Series s : mSeries) {
                                s.isChecked = false;
                            }
                            mBrandConditionRightAdapter.notifyDataSetChanged();
                            if (mSeries.size() == 0) {
                                mBrandRightHeaderView.setVisibility(GONE);
                            } else {
                                mBrandRightHeaderView.setVisibility(VISIBLE);
                            }
                            return true;
                        }
                    });
                }
                if (mGroupPosition != -1 && mChildPosition != -1) {
                    for (BrandGroup bg : mProductListFilter.brandList) {
                        for (Brand b : bg.group) {
                            b.isChecked = false;
                        }
                    }
                    Brand brand = mProductListFilter.brandList.get(mGroupPosition).group.get(mChildPosition);
                    brand.isChecked = true;
                    mBrandConditionLeftAdapter.notifyDataSetChanged();

                    mSeries.clear();
                    mSeries.addAll(brand.seriesList);
                    for (Series s : mSeries) {
                        s.isChecked = false;
                    }
                    if (mBrandRightPosition != -1)
                        mSeries.get(mBrandRightPosition).isChecked = true;
                    mBrandConditionRightAdapter.notifyDataSetChanged();
                    if (mSeries.size() == 0) {
                        mBrandRightHeaderView.setVisibility(GONE);
                    } else {
                        mBrandRightHeaderView.setVisibility(VISIBLE);
                    }
                    mBrandConditionLeftView.setSelectedChild(mGroupPosition, mChildPosition, true);
                    if (mBrandRightPosition != -1)
                        mBrandConditionRightView.setSelection(mBrandRightPosition);
                } else {
                    for (BrandGroup bg : mProductListFilter.brandList) {
                        for (Brand b : bg.group) {
                            b.isChecked = false;
                        }
                    }
                    mBrandConditionLeftAdapter.notifyDataSetChanged();

                    if (null != mBrandConditionRightAdapter) {
                        mSeries.clear();
                        mBrandConditionRightAdapter.notifyDataSetChanged();
                        mBrandRightHeaderView.setVisibility(GONE);
                    }
                    mBrandConditionLeftView.setSelectedGroup(-1);
                }
                break;

            case 0: //品类
                mBrandConditionParentView.setVisibility(GONE);
                mClassConditionParentView.setVisibility(VISIBLE);

                mClassLeftPositionTemp = mClassLeftPosition;
                if (null == mClassConditionLeftAdapter) {
                    //加头 不限
                    View classLeftHeaderView = View.inflate(getContext(), R.layout.item_custom_brand_expandable, null);
                    mClassRightHeaderView = View.inflate(getContext(), R.layout.item_custom_brand_expandable, null);
                    mClassConditionLeftView.addHeaderView(classLeftHeaderView);
                    classLeftHeaderView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mClassLeftPosition = -1;
                            mClassRightPosition = -1;
                            mClassView.setText("品类");
                            hideConditionRootView();
                            mStatus = Status.UNSELECTED;
                            updateStatus();
                            if (null != mOnFilterClickListener)
                                mOnFilterClickListener.onClassClick("", "");
                        }
                    });

                    mClassConditionLeftAdapter = new CustomConditionAdapter(getContext(), CustomConditionAdapter.Type.ACCESSORY_BIG_CATEGORY, mProductListFilter.classification);
                    mClassConditionLeftView.setAdapter(mClassConditionLeftAdapter);
                    mClassConditionLeftView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position != 0) {
                                mClassLeftPositionTemp = position - 1;
                                for (AccessoryBigCategory abc : mProductListFilter.classification) {
                                    abc.isChecked = false;
                                }
                                AccessoryBigCategory category = mProductListFilter.classification.get(position - 1);
                                category.isChecked = true;
                                mClassConditionLeftAdapter.notifyDataSetChanged();

                                //二级分类2级联动
                                if (null == mClassConditionRightAdapter) {
                                    //加头 不限
                                    mClassConditionRightView.addHeaderView(mClassRightHeaderView);
                                    mClassRightHeaderView.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            mClassLeftPosition = mClassLeftPositionTemp;
                                            mClassRightPosition = -1;
                                            mClassView.setText(mProductListFilter.classification.get(mClassLeftPositionTemp).class1stName);
                                            hideConditionRootView();
                                            mStatus = Status.UNSELECTED;
                                            updateStatus();
                                            if (null != mOnFilterClickListener)
                                                mOnFilterClickListener.onClassClick(mProductListFilter.classification.get(mClassLeftPositionTemp).class1stID, "");
                                        }
                                    });

                                    mClassConditionRightAdapter = new CustomConditionAdapter(getContext(), CustomConditionAdapter.Type.ACCESSORY_SMALL_CATEGORY, mClass2nd);
                                    mClassConditionRightView.setAdapter(mClassConditionRightAdapter);
                                    mClassConditionRightView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            if (position != 0) {
                                                mClassLeftPosition = mClassLeftPositionTemp;
                                                mClassRightPosition = position - 1;
                                                mClassView.setText(mClass2nd.get(position - 1).class2ndName);
                                                hideConditionRootView();
                                                mStatus = Status.UNSELECTED;
                                                updateStatus();
                                                if (null != mOnFilterClickListener)
                                                    mOnFilterClickListener.onClassClick(mProductListFilter.classification.get(mClassLeftPositionTemp).class1stID, mClass2nd.get(position - 1).class2ndID);
                                            }
                                        }
                                    });
                                }
                                mClass2nd.clear();
                                mClass2nd.addAll(category.class2nd);
                                for (AccessorySmallCategory a : category.class2nd) {
                                    a.isChecked = false;
                                }
                                mClassConditionRightAdapter.notifyDataSetChanged();
                                if (mClass2nd.size() == 0) {
                                    mClassRightHeaderView.setVisibility(GONE);
                                } else {
                                    mClassRightHeaderView.setVisibility(VISIBLE);
                                }
                            }
                        }
                    });
                }
                if (mClassLeftPosition != -1) {
                    for (AccessoryBigCategory abc : mProductListFilter.classification) {
                        abc.isChecked = false;
                    }
                    AccessoryBigCategory category = mProductListFilter.classification.get(mClassLeftPosition);
                    category.isChecked = true;
                    mClassConditionLeftAdapter.notifyDataSetChanged();

                    mClass2nd.clear();
                    mClass2nd.addAll(category.class2nd);
                    for (AccessorySmallCategory a : mClass2nd) {
                        a.isChecked = false;
                    }
                    if (mClassRightPosition != -1)
                        mClass2nd.get(mClassRightPosition).isChecked = true;
                    mClassConditionRightAdapter.notifyDataSetChanged();
                    if (mClass2nd.size() == 0) {
                        mClassRightHeaderView.setVisibility(GONE);
                    } else {
                        mClassRightHeaderView.setVisibility(VISIBLE);
                    }
                    mClassConditionLeftView.setSelection(mClassLeftPosition);
                    if (mClassRightPosition != -1)
                        mClassConditionRightView.setSelection(mClassRightPosition);
                } else {
                    for (AccessoryBigCategory abc : mProductListFilter.classification) {
                        abc.isChecked = false;
                    }
                    mClassConditionLeftAdapter.notifyDataSetChanged();

                    if (null != mClassConditionRightAdapter) {
                        mClass2nd.clear();
                        mClassConditionRightAdapter.notifyDataSetChanged();
                        mClassRightHeaderView.setVisibility(GONE);
                    }
                    mClassConditionLeftView.setSelection(-1);
                }
                break;
        }

        TranslateAnimation mTranslateAnimation = new TranslateAnimation(0, 0, -(float) (DisplayTools.getScreenHeight(getContext()) / 5 * 3), 0);
        mTranslateAnimation.setDuration(200);
        mConditionParentView.setAnimation(mTranslateAnimation);
    }

    /**
     * 隐藏 条件
     **/
    private void hideConditionRootView() {
        for (View v : mShadeList) {
            v.setVisibility(GONE);
        }
        if (null != mShadeView) mShadeView.setVisibility(GONE);
        mConditionParentView.setVisibility(GONE);

        mBrandView.setCompoundDrawables(null, null, getDownDrawable(), null);
        mClassView.setCompoundDrawables(null, null, getDownDrawable(), null);
        mBrandViewIsChecked = false;
        mClassViewIsChecked = false;
    }

    private Drawable getDownDrawable() {
        Drawable d = ContextCompat.getDrawable(getContext(), R.drawable.ic_cbb_arrow_down);
        d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
        return d;
    }

    private Drawable getUpDrawable() {
        Drawable d = ContextCompat.getDrawable(getContext(), R.drawable.ic_cbb_arrow_up);
        d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
        return d;
    }

    private void updateStatus() {
        if (Status.UNSELECTED == mStatus) {
            mPriceView.setBackgroundResource(R.drawable.ic_3type_sort);
        } else if (Status.DOWN == mStatus) {
            mPriceView.setBackgroundResource(R.drawable.ic_3type_sortdown);
        } else if (Status.UP == mStatus) {
            mPriceView.setBackgroundResource(R.drawable.ic_3type_sortup);
        }
    }

    public enum Status {
        UNSELECTED, //未选择
        DOWN, //箭头向下
        UP //箭头向上
    }

    public interface OnFilterClickListener {
        void onBrandClick(String brand, String series);

        void onClassClick(String class1stID, String class2ndID);

        void onPriceClick(String priceOrder);
    }

}
