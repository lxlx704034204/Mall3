package com.hxqc.mall.thirdshop.activity.auto.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.views.SideBar;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.activity.auto.activity.ControllerConstruct;
import com.hxqc.mall.thirdshop.activity.auto.control.BaseFilterController;
import com.hxqc.mall.thirdshop.model.Brand;
import com.hxqc.mall.thirdshop.model.Series;
import com.hxqc.mall.thirdshop.model.TCarSeriesModel;
import com.hxqc.mall.thirdshop.views.adpter.SeriesExpandableAdapter;
import com.hxqc.util.DebugLog;
import com.hxqc.util.ScreenUtil;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Function: 4S点车系列表
 *
 * @author 袁秉勇
 * @since 2016年06月22日
 */
public class FilterSeriesFragment1 extends FunctionFragment implements BaseFilterController.TSeriesHandler, ExpandableListView.OnChildClickListener, View.OnClickListener {
    private final static String TAG = FilterSeriesFragment1.class.getSimpleName();
    private Context mContext;

    protected PinnedHeaderExpandableListView mExpandableListView;
    protected SeriesExpandableAdapter seriesExpandableAdapter;
    protected FilterSeriesFragmentCallBack filterSeriesFragmentCallBack;
    protected View rootView;
    protected RequestFailView mRequestFailView;
    private ArrayList<TCarSeriesModel> tCarSeriesModels = new ArrayList<>();
    private boolean showNoLimit = true; // 默认显示“不限”(筛选条件)  true为显示
    protected SideBar sideBarView;
    private boolean showSideBar = false;

    protected Brand brand;

    private BaseFilterController baseFilterController;
    private boolean isFromNewCar; // 是否来自新车筛选列表
    private boolean fromCarCompare;  //来自车型对比

    public void setFilterSeriesFragmentCallBack(FilterSeriesFragmentCallBack filterSeriesFragmentCallBack) {
        this.filterSeriesFragmentCallBack = filterSeriesFragmentCallBack;
    }


    /**
     * 是否显示  "不限"
     * true 显示
     * false 不显示
     */
    public void setShowNoLimit(boolean showNoLimit) {
        this.showNoLimit = showNoLimit;
    }

    /**
     * 是否来自新车筛选列表
     */
    public void setFromNewCar(boolean isFromNewCar) {
        this.isFromNewCar = isFromNewCar;
    }


    public void setRecyclerViewHeaderImageView() {
        ImageView heardView = new ImageView(mContext);
        heardView.setBackgroundResource(R.drawable.pic_list_title);
        mExpandableListView.addHeaderView(heardView);
    }


    public static FilterSeriesFragment1 newInstance(Brand brand) {
        FilterSeriesFragment1 fragment = new FilterSeriesFragment1();
        Bundle args = new Bundle();
        args.putParcelable("brand", brand);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        DebugLog.e(TAG, " ----------- onAttach ");
        super.onAttach(context);

        mContext = context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
//        DebugLog.e(TAG, " ------------ onCreate ");
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.brand = getArguments().getParcelable("brand") != null ? (Brand) getArguments().getParcelable("brand") : null;
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        DebugLog.e(TAG, " ---------- onCreateView ");
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.t_fragment_filter_brand, container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        DebugLog.e(TAG, " ------------ onViewCreated ");
        super.onViewCreated(view, savedInstanceState);
        mExpandableListView = (PinnedHeaderExpandableListView) view.findViewById(R.id.list);
        mExpandableListView.setOnChildClickListener(this);
        mRequestFailView = (RequestFailView) view.findViewById(R.id.request_view);

        sideBarView = (SideBar) view.findViewById(R.id.SideBar);
        sideBarView.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(int index, String s, StringBuffer s1) {
                if (index == 0) {
                    mExpandableListView.smoothScrollToPosition(0);
                } else {
                    mExpandableListView.setSelectedGroup(index - 1);
                }
            }
        });

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (baseFilterController == null) {
            if (getActivity() instanceof ControllerConstruct) {
                baseFilterController = ((ControllerConstruct) getActivity()).getController();
            } else {
                baseFilterController = BaseFilterController.getInstance();
            }
        }

        if (seriesExpandableAdapter == null && brand != null) getData(brand.brandName, true);
    }


    /**
     * 获取车系数据
     *
     * @param brandName
     */
    public void getData(@NonNull String brandName, boolean showLoading) {
        brand = new Brand(brandName);
        if (baseFilterController.getBrand() != null && brandName.equals(baseFilterController.getBrand().brandEName))
            return;
        baseFilterController.requestThirdShopSeries(mContext, brandName, this, showLoading);
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (filterSeriesFragmentCallBack != null)
            filterSeriesFragmentCallBack.onFilterSeriesCallBack(tCarSeriesModels.get(groupPosition).series.get(childPosition));

        //下面用于新车车系筛选列表回调
        if (isFromNewCar)
            EventBus.getDefault().post(tCarSeriesModels.get(groupPosition));
        return true;
    }


    @Override
    public void onSucceed(ArrayList<TCarSeriesModel> tCarSeriesModels) {
        if (tCarSeriesModels == null || tCarSeriesModels.size() <= 0) {
            onFailed(false);
            return;
        } else {
            this.tCarSeriesModels = tCarSeriesModels;
            mRequestFailView.setVisibility(View.GONE);
            if (showSideBar && sideBarView.getVisibility() == View.GONE)
                sideBarView.setVisibility(View.VISIBLE);
        }

        initSideTag(tCarSeriesModels);

        if (seriesExpandableAdapter == null) {
            seriesExpandableAdapter = new SeriesExpandableAdapter(mContext, tCarSeriesModels);
            seriesExpandableAdapter.setShowAllBrandFilter(fromCarCompare);
            if (showNoLimit) {
                mExpandableListView.addHeaderView(createHeaderView(mContext));
            } else {
//                setRecyclerViewHeaderImageView();
            }
            if (fromCarCompare)
                setHeaderTitle("选择车系");

            mExpandableListView.setAdapter(seriesExpandableAdapter);
            mExpandableListView.setOnHeaderUpdateListener(seriesExpandableAdapter);
            OtherUtil.openAllChild(seriesExpandableAdapter, mExpandableListView);
        } else {
            seriesExpandableAdapter.notifyData(tCarSeriesModels);
            OtherUtil.openAllChild(seriesExpandableAdapter, mExpandableListView);
            mExpandableListView.setSelection(0);
        }
    }


    @Override
    public void onFailed(boolean offLine) {
        if (sideBarView.getVisibility() == View.GONE) sideBarView.setVisibility(View.GONE);
        if (offLine) {
            mExpandableListView.setEmptyView(mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail));
        } else {
            mExpandableListView.setEmptyView(mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty));
        }
    }


    @Override
    public String fragmentDescription() {
        return "4s店车系";
    }


    private TextView createHeaderView(Context context) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());
        textView.setPadding(padding, padding / 2, padding, padding / 2);
        textView.setText("不限");
        textView.setTextColor(getResources().getColor(R.color.text_gray));
        textView.setBackgroundColor(getResources().getColor(R.color.white));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterSeriesFragmentCallBack != null)
                    filterSeriesFragmentCallBack.onFilterSeriesCallBack(null);
            }
        });
        return textView;
    }

    public void setHeaderTitle(String title) {
        RelativeLayout heardView = new RelativeLayout(mContext);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        TextView tv = new TextView(getActivity());
        int padding = ScreenUtil.dip2px(mContext, 16);
        tv.setPadding(padding, padding / 2, padding, padding / 2);
        tv.setTextColor(Color.parseColor("#333333"));
        tv.setText(title);
        tv.setTextSize(16);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
        heardView.setBackgroundColor(Color.parseColor("#ffffff"));
        heardView.addView(tv, layoutParams);
        mExpandableListView.addHeaderView(heardView);
    }


    @Override
    public void onClick(View v) {
        Series series = tCarSeriesModels.get(0).series.get(0);
        if (filterSeriesFragmentCallBack != null)
            filterSeriesFragmentCallBack.onFilterSeriesCallBack(series);
    }


    /**
     * 创建边侧导航
     */
    private void initSideTag(List<TCarSeriesModel> tCarSeriesModels) {
        String[] tag = new String[tCarSeriesModels.size()];
        for (int i = 0; i < tCarSeriesModels.size(); i++) {
            tag[i] = tCarSeriesModels.get(i).brandName;
        }

    }

    public void setShowAllBrandFilter(boolean fromCarCompare) {
        this.fromCarCompare = fromCarCompare;
    }


    public interface FilterSeriesFragmentCallBack {
        void onFilterSeriesCallBack(Series series);
    }
}