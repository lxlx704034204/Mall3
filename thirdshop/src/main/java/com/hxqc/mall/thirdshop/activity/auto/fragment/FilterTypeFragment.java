package com.hxqc.mall.thirdshop.activity.auto.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.activity.auto.activity.ControllerConstruct;
import com.hxqc.mall.thirdshop.activity.auto.control.BaseFilterController;
import com.hxqc.mall.thirdshop.model.Brand;
import com.hxqc.mall.thirdshop.model.CarType;
import com.hxqc.mall.thirdshop.model.Series;
import com.hxqc.mall.thirdshop.views.adpter.CarTypeRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * 筛选车型
 */
public class FilterTypeFragment extends FunctionFragment implements CarTypeRecyclerViewAdapter.CarTypeRecyclerViewCallBack, BaseFilterController.TTypeHandler {
    private View rootView;
    protected RecyclerView mRecyclerView;
    protected CarTypeRecyclerViewAdapter carTypeRecyclerViewAdapter;
    RequestFailView mRequestFailView;
    ArrayList< CarType > carTypes = new ArrayList<>();
    FilterTypeFragmentCallBack filterTypeFragmentCallBack;
    Brand brand;
    Series series;
    LinearLayout mContentView;
    ImageView mAutoSeriesLogoView;
    TextView mAutoSeriesNameView;
    TextView mAutoSeriesPriceView;

    private BaseFilterController baseFilterController;


    public void setFilterTypeFragmentCallBack(FilterTypeFragmentCallBack filterTypeFragmentCallBack) {
        this.filterTypeFragmentCallBack = filterTypeFragmentCallBack;
    }


    public FilterTypeFragment() {
    }


    // TODO: Rename and change types and number of parameters
    public static FilterTypeFragment newInstace(Brand brand, Series series) {
        FilterTypeFragment fragment = new FilterTypeFragment();
        Bundle args = new Bundle();
        args.putParcelable("brand", brand);
        args.putParcelable("series", series);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.brand = getArguments().getParcelable("brand") != null ? (Brand) getArguments().getParcelable("brand") : new Brand("");
            this.series = getArguments().getParcelable("series") != null ? (Series) getArguments().getParcelable("series") : new Series();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.t_fragment_filter_model, container, false);
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
        mContentView = (LinearLayout) view.findViewById(R.id.content_view);
        mAutoSeriesLogoView = (ImageView) view.findViewById(R.id.car_series_image);
        mAutoSeriesNameView = (TextView) view.findViewById(R.id.car_series_name);
        mAutoSeriesPriceView = (TextView) view.findViewById(R.id.car_series_price);

        ImageUtil.setImage(getContext(), mAutoSeriesLogoView, series.seriesThumb);
        mAutoSeriesNameView.setText(VerifyString(series.seriesName));
        mAutoSeriesPriceView.setText(OtherUtil.formatPriceRange(series.priceRange));

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRequestFailView = (RequestFailView) view.findViewById(R.id.request_view);

        if (carTypeRecyclerViewAdapter == null) getData(brand.brandName, series.seriesName, true);
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


    public void getData(String brandName, String seriesName, boolean showLoading) {
        if (baseFilterController == null) {
            if (getActivity() instanceof ControllerConstruct) {
                baseFilterController = ((ControllerConstruct)getActivity()).getController();
            } else {
                baseFilterController = BaseFilterController.getInstance();
            }
        }
        baseFilterController.requestThirdShopType(brandName, seriesName, getActivity(), this, showLoading); // 获取车辆数
    }


    @Override
    public void onSucceed(ArrayList< CarType > carTypes) {
        if (carTypes == null) {
            onFailed(false);
            return;
        } else {
            if (carTypes.size() <= 0) {
                mContentView.setVisibility(View.GONE);
                mRecyclerView.setAdapter(null);
//                mRequestFailView.setEmptyDescription("未找到结果");
//                mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
//                mRequestFailView.setVisibility(View.VISIBLE);
                onFailed(false);
                return;
            }
            this.carTypes.clear();
            this.carTypes.addAll(carTypes);
            mRequestFailView.setVisibility(View.GONE);
            mContentView.setVisibility(View.VISIBLE);
        }
        if (carTypeRecyclerViewAdapter == null) {
            carTypeRecyclerViewAdapter = new CarTypeRecyclerViewAdapter(this.carTypes, mContext);
            carTypeRecyclerViewAdapter.setmCarTypeRecyclerViewCallBack(this);
            mRecyclerView.setAdapter(carTypeRecyclerViewAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            if (mRecyclerView.getAdapter() == null) mRecyclerView.setAdapter(carTypeRecyclerViewAdapter);
            carTypeRecyclerViewAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(0);
        }
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
        return "4S点车型";
    }


    @Override
    public void carTypeCallBack(CarType carType) {
        filterTypeFragmentCallBack.onFilterTypeCallBack(carType);
    }


    public interface FilterTypeFragmentCallBack {
        void onFilterTypeCallBack(CarType carType);
    }
}
