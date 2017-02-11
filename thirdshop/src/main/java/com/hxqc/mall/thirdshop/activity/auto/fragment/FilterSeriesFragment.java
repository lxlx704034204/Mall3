package com.hxqc.mall.thirdshop.activity.auto.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.activity.auto.activity.ControllerConstruct;
import com.hxqc.mall.thirdshop.activity.auto.control.BaseFilterController;
import com.hxqc.mall.thirdshop.model.Brand;
import com.hxqc.mall.thirdshop.model.Series;
import com.hxqc.mall.thirdshop.model.TCarSeriesModel;
import com.hxqc.mall.thirdshop.views.adpter.CarSeriesRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * 4S店车系
 * 袁秉勇
 */
@Deprecated
public class FilterSeriesFragment extends FunctionFragment implements BaseFilterController.TSeriesHandler, CarSeriesRecyclerViewAdapter.carSeriesRecyclerViewCallBack {
    private final static String TAG = FilterSeriesFragment.class.getSimpleName();

    protected RecyclerView mRecyclerView;
    protected CarSeriesRecyclerViewAdapter carSeriesRecyclerViewAdapter;
    protected View rootView;
    protected ArrayList< Series > series = new ArrayList<>();
    protected RequestFailView mRequestFailView;
    protected FilterSeriesFragmentCallBack filterSeriesFragmentCallBack;
    protected LinearLayoutManager linearLayoutManager;
    protected LinearLayout mBrandNameTipView;
    protected TextView mBrandNameTextView;

    private BaseFilterController baseFilterController;

    protected Brand brand;

    private boolean showNoLimit = true;//默认显示“不限”(筛选条件)  true为显示


    public void setShowNoLimit(boolean showNoLimit) {
        this.showNoLimit = showNoLimit;
    }


    public void setFilterSeriesFragmentCallBack(FilterSeriesFragmentCallBack filterSeriesFragmentCallBack) {
        this.filterSeriesFragmentCallBack = filterSeriesFragmentCallBack;
    }


    public FilterSeriesFragment() {
    }


    public static FilterSeriesFragment newInstance(Brand brand) {
        FilterSeriesFragment fragment = new FilterSeriesFragment();
        Bundle args = new Bundle();
        args.putParcelable("brand", brand);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.brand = getArguments().getParcelable("brand") != null ? (Brand) getArguments().getParcelable("brand") : null;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.t_fragment_filter_series, container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mBrandNameTipView = (LinearLayout) view.findViewById(R.id.brand_tip_view);
        mBrandNameTextView = (TextView) view.findViewById(R.id.no_limit_condition);
        mRequestFailView = (RequestFailView) view.findViewById(R.id.request_view);

        if (carSeriesRecyclerViewAdapter == null && brand != null) getData(brand.brandName, true);
    }


    @Override
    public void onResume() {
        super.onResume();

    }


    /**
     * 获取车系数据
     *
     * @param brandName
     */
    public void getData(@NonNull String brandName, boolean showLoading) {
        brand = new Brand(brandName);
        if (baseFilterController == null) {
            if (getActivity() instanceof ControllerConstruct) {
                baseFilterController = ((ControllerConstruct)getActivity()).getController();
            } else {
                baseFilterController = BaseFilterController.getInstance();
            }
        }
        baseFilterController.requestThirdShopSeries(mContext, brandName, this, showLoading);
    }


//    @Override
//    public void onSucceed(ArrayList< Series > series) {
//        if (series == null || series.size() <= 0) {
//            onFailed(false);
//            return;
//        } else {
////            if (series.size() == 0) {
////                mRecyclerView.setAdapter(null);
////                onFailed(false);
////                return;
////            }
//            this.series.clear();
//            this.series.addAll(series);
//            mRequestFailView.setVisibility(View.GONE);
//        }
//        if (carSeriesRecyclerViewAdapter == null) {
//            carSeriesRecyclerViewAdapter = new CarSeriesRecyclerViewAdapter(this.series, mContext);
//            carSeriesRecyclerViewAdapter.setShowNoLimit(showNoLimit, brand.brandName);
//            carSeriesRecyclerViewAdapter.setCarSeriesRecyclerViewCallBack(this);
//            mRecyclerView.setAdapter(carSeriesRecyclerViewAdapter);
//            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//            if (!showNoLimit) setListener();
//        } else {
//            if (mRecyclerView.getAdapter() == null) mRecyclerView.setAdapter(carSeriesRecyclerViewAdapter);
//            if (!showNoLimit) carSeriesRecyclerViewAdapter.setBrandName(brand.brandName);
//            carSeriesRecyclerViewAdapter.notifyDataSetChanged();
//            mRecyclerView.scrollToPosition(0);
//        }
//    }


    public void setListener() {
        if (linearLayoutManager == null) linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() != 0) {
                    if (mBrandNameTipView.getVisibility() == View.GONE) {
                        mBrandNameTextView.setText(brand.brandName);
                        mBrandNameTipView.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (mBrandNameTipView.getVisibility() == View.VISIBLE) mBrandNameTipView.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public void onSucceed(ArrayList< TCarSeriesModel > series) {

    }


    @Override
    public void onFailed(boolean offLine) {
        mRequestFailView.setVisibility(View.VISIBLE);
        if (offLine) {
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
        } else {
            mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        }
    }


    @Override
    public String fragmentDescription() {
        return "车系选择fragment";
    }


    @Override
    public void carSeriesCallBack(Series series) {
        filterSeriesFragmentCallBack.onFilterSeriesCallBack(series);
    }


    public interface FilterSeriesFragmentCallBack {
        void onFilterSeriesCallBack(Series series);
    }
}
