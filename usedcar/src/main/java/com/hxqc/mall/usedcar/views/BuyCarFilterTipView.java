package com.hxqc.mall.usedcar.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.adapter.ChooseBrandAdapter;
import com.hxqc.mall.usedcar.api.UsedCarApiClient;
import com.hxqc.mall.usedcar.fragment.ChooseFragment;
import com.hxqc.mall.usedcar.model.BrandGroup;
import com.hxqc.mall.usedcar.model.Choose;
import com.hxqc.mall.usedcar.model.IdAndValue;
import com.hxqc.util.DisplayTools;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.PinnedHeaderExpandableListView;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;

/**
 * 买车 筛选条
 * Created by huangyi on 15/10/21.
 */
public class BuyCarFilterTipView extends LinearLayout implements View.OnClickListener, SideBar.OnTouchingLetterChangedListener {

    Choose mChoose; //数据源

    OnConditionClickListener mOnConditionClickListener;
    LinearLayout mTipSortView; //排序
    LinearLayout mTipPriceView; //价格
    LinearLayout mTipBrandView; //品牌
    LinearLayout mTipFilterView; //筛选
    View mShadeView; //遮罩
    RelativeLayout mConditionParentView; //list parent
    ListView mSortListView; //排序条件
    ListView mPriceListView; //价格条件
    PinnedHeaderExpandableListView mBrandListView; //品牌条件
    SideBar mBarView;
    /**
     * 记录最后一个点击过的Tip id
     **/
    int mLastCheckedTipID;
    QuickAdapter<IdAndValue> mSortAdapter;
    QuickAdapter<IdAndValue> mPriceAdapter;
    ChooseBrandAdapter mBrandAdapter;

    public BuyCarFilterTipView(Context context) {
        this(context, null);
    }

    public BuyCarFilterTipView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BuyCarFilterTipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View root = LayoutInflater.from(context).inflate(R.layout.view_buy_car_filter_tip, this);
        mTipSortView = (LinearLayout) root.findViewById(R.id.filter_tip_sort);
        mTipPriceView = (LinearLayout) root.findViewById(R.id.filter_tip_price);
        mTipBrandView = (LinearLayout) root.findViewById(R.id.filter_tip_brand);
        mTipFilterView = (LinearLayout) root.findViewById(R.id.filter_tip_filter);
        mConditionParentView = (RelativeLayout) root.findViewById(R.id.filter_tip_condition_parent);
        mSortListView = (ListView) root.findViewById(R.id.filter_tip_sort_list);
        mPriceListView = (ListView) root.findViewById(R.id.filter_tip_price_list);
        mBrandListView = (PinnedHeaderExpandableListView) root.findViewById(R.id.filter_tip_brand_list);
        mBarView = (SideBar) root.findViewById(R.id.filter_tip_bar);

        mTipSortView.setOnClickListener(this);
        mTipPriceView.setOnClickListener(this);
        mTipBrandView.setOnClickListener(this);
        mTipFilterView.setOnClickListener(this);
        mBarView.setOnTouchingLetterChangedListener(this);

        ViewGroup.LayoutParams params = mConditionParentView.getLayoutParams();
        params.height = DisplayTools.getScreenHeight(context) / 5 * 3;
        mConditionParentView.setLayoutParams(params);

        initData();
    }

    public void setOnConditionClickListener(OnConditionClickListener mOnConditionClickListener) {
        this.mOnConditionClickListener = mOnConditionClickListener;
    }

    public void setShadeView(View mShadeView) {
        this.mShadeView = mShadeView;
        this.mShadeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                restoreFilterTipView();
            }
        });
    }

    private void initData() {
        new UsedCarApiClient().getFilter(new LoadingAnimResponseHandler(getContext(), false) {
            @Override
            public void onSuccess(String response) {
                mChoose = JSONUtils.fromJson(response, Choose.class);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.filter_tip_sort) {
            if (mLastCheckedTipID == R.id.filter_tip_sort) {
                switchTipBackgroundColor(mTipSortView, false);
                hideConditionRootView();
            } else {
                switchTipBackgroundColor(mTipSortView, true);
                switchTipBackgroundColor(mTipPriceView, false);
                switchTipBackgroundColor(mTipBrandView, false);
                mLastCheckedTipID = id;
                showConditionRootView(-1);
            }
        } else if (id == R.id.filter_tip_price) {
            if (mLastCheckedTipID == R.id.filter_tip_price) {
                switchTipBackgroundColor(mTipPriceView, false);
                hideConditionRootView();
            } else {
                if (null == mChoose || null == mChoose.price || mChoose.price.size() == 0) {
                    initData();
                } else {
                    switchTipBackgroundColor(mTipSortView, false);
                    switchTipBackgroundColor(mTipPriceView, true);
                    switchTipBackgroundColor(mTipBrandView, false);
                    mLastCheckedTipID = id;
                    showConditionRootView(0);
                }
            }
        } else if (id == R.id.filter_tip_brand) {
            if (mLastCheckedTipID == R.id.filter_tip_brand) {
                switchTipBackgroundColor(mTipBrandView, false);
                hideConditionRootView();
            } else {
                if (null == mChoose || null == mChoose.brand || mChoose.brand.size() == 0) {
                    initData();
                } else {
                    switchTipBackgroundColor(mTipSortView, false);
                    switchTipBackgroundColor(mTipPriceView, false);
                    switchTipBackgroundColor(mTipBrandView, true);
                    mLastCheckedTipID = id;
                    showConditionRootView(1);
                }
            }
        } else if (id == R.id.filter_tip_filter) {
            if (mLastCheckedTipID != 0) restoreFilterTipView();
            if (null != mOnConditionClickListener) mOnConditionClickListener.onFilter();
        }
    }

    @Override
    public void onTouchingLetterChanged(String s, StringBuffer s1) {
        int position = 0;
        for (int i = 0; i < mChoose.brand.size(); i++) {
            if (mChoose.brand.get(i).groupTag.charAt(0) == (s.charAt(0))) {
                mBrandListView.setSelection(position);
                break;
            } else {
                position = position + mChoose.brand.get(i).group.size() + 1;
            }
        }
    }

    /**
     * 切换Tip颜色
     **/
    private void switchTipBackgroundColor(View view, boolean isChecked) {
        if (isChecked) {
            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tag_background));
        } else {
            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        }
    }

    /**
     * 还原 筛选条
     **/
    private void restoreFilterTipView() {
        switchTipBackgroundColor(mTipSortView, false);
        switchTipBackgroundColor(mTipPriceView, false);
        switchTipBackgroundColor(mTipBrandView, false);
        hideConditionRootView();
    }

    /**
     * 隐藏 条件
     **/
    private void hideConditionRootView() {
        if (null != mShadeView) mShadeView.setVisibility(GONE);
        mConditionParentView.setVisibility(GONE);
        mLastCheckedTipID = 0;
    }

    /**
     * 显示 条件
     **/
    private void showConditionRootView(final int ID) {
        if (null != mShadeView) mShadeView.setVisibility(VISIBLE);
        mConditionParentView.setVisibility(VISIBLE);
        mSortListView.setVisibility(GONE);
        mPriceListView.setVisibility(GONE);
        mBrandListView.setVisibility(GONE);
        mBarView.setVisibility(GONE);

        switch (ID) {
            case -1: //排序
                mSortListView.setVisibility(VISIBLE);
                if (null == mSortAdapter) {
                    String str = "[{\"id\":\"\",\"text\":\"\",\"value\":\"默认排序\"},\n" +
                            "{\"id\":\"desc\",\"text\":\"publish_time\",\"value\":\"最新发布\"},\n" +
                            "{\"id\":\"desc\",\"text\":\"estimate_price\",\"value\":\"价格最高\"},\n" +
                            "{\"id\":\"asc\",\"text\":\"estimate_price\",\"value\":\"价格最低\"},\n" +
                            "{\"id\":\"desc\",\"text\":\"first_on_card\",\"value\":\"车龄最短\"},\n" +
                            "{\"id\":\"asc\",\"text\":\"car_mileage\",\"value\":\"里程最少\"}]";
                    final ArrayList<IdAndValue> sort = JSONUtils.fromJson(str, new TypeToken<ArrayList<IdAndValue>>() {
                    });
                    mSortAdapter = new QuickAdapter<IdAndValue>(getContext(), R.layout.item_buycar_filter_child) {
                        @Override
                        protected void convert(BaseAdapterHelper helper, IdAndValue item) {
                            helper.setText(R.id.textview, item.value);
                        }
                    };
                    mSortAdapter.addAll(sort);
                    mSortListView.setAdapter(mSortAdapter);
                    mSortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            restoreFilterTipView();
                            if (null != mOnConditionClickListener)
                                mOnConditionClickListener.onCondition(false, ID, new String[]{sort.get(position).id, sort.get(position).text});
                        }
                    });
                }
                break;

            case 0: //价格
                mPriceListView.setVisibility(VISIBLE);
                if (null == mPriceAdapter) {
                    final ArrayList<IdAndValue> price = mChoose.price;
                    mPriceAdapter = new QuickAdapter<IdAndValue>(getContext(), R.layout.item_buycar_filter_child) {
                        @Override
                        protected void convert(BaseAdapterHelper helper, IdAndValue item) {
                            helper.setText(R.id.textview, item.value);
                        }
                    };
                    mPriceAdapter.addAll(price);
                    mPriceListView.setAdapter(mPriceAdapter);
                    mPriceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            restoreFilterTipView();
                            if (position != 0) {
                                if (null != mOnConditionClickListener)
                                    mOnConditionClickListener.onCondition(false, ID, new String[]{price.get(position).id, price.get(position).value});
                            } else {
                                if (null != mOnConditionClickListener)
                                    mOnConditionClickListener.onCondition(true, ID, null);
                            }
                        }
                    });
                }
                break;

            case 1: //品牌
                mBrandListView.setVisibility(VISIBLE);
                mBarView.setVisibility(VISIBLE);
                if (null == mBrandAdapter) {
                    final ArrayList<BrandGroup> brand = mChoose.brand;
                    mBrandAdapter = new ChooseBrandAdapter(getContext(), brand, ChooseFragment.COMMON);
                    mBrandListView.setAdapter(mBrandAdapter);
                    mBrandListView.setOnHeaderUpdateListener(mBrandAdapter);
                    OtherUtil.openAllChild(mBrandAdapter, mBrandListView);
                    mBrandListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                        @Override
                        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                            return true;
                        }
                    }, false);
                    mBrandListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                            restoreFilterTipView();
                            if (null != mOnConditionClickListener)
                                mOnConditionClickListener.onCondition(false, ID,
                                        new String[]{brand.get(groupPosition).group.get(childPosition).id, brand.get(groupPosition).group.get(childPosition).brand_name});
                            return true;
                        }
                    });
                }
                break;
        }

        TranslateAnimation mTranslateAnimation = new TranslateAnimation(0, 0, -(float) (DisplayTools.getScreenHeight(getContext()) / 5 * 3), 0);
        mTranslateAnimation.setDuration(200);
        mConditionParentView.setAnimation(mTranslateAnimation);
    }

    public interface OnConditionClickListener {
        /**
         * 排序 价格 品牌
         **/
        void onCondition(boolean isUnlimited, int ID, String[] condition);

        /**
         * 点击筛选
         **/
        void onFilter();
    }

}
