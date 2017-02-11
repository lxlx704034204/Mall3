package com.hxqc.mall.thirdshop.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.auto.adapter.FlashSaleSeriesExpandableAdapter;
import com.hxqc.mall.auto.model.FilterGroup;
import com.hxqc.mall.auto.model.Series;
import com.hxqc.mall.auto.model.SeriesGroup;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.constant.FilterResultKey;
import com.hxqc.mall.thirdshop.interfaces.FilterAction;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-05-06
 * FIXME
 * Todo 筛选车辆车系菜单
 */
public class FilterAutoSerieFragment extends FunctionFragment implements ExpandableListView.OnChildClickListener, View.OnClickListener {

    private RequestFailView autoBrandView;
    private PinnedHeaderExpandableListView autoBrandList;
    private View mHeaderView;
    private ThirdPartShopClient client;
    private String areaID;
    private ArrayList<SeriesGroup> seriesGroups = new ArrayList<>();
    private FlashSaleSeriesExpandableAdapter adapter;
    private FilterAction mFilterAction;

    public void setBrand(String brand) {
        this.brand = brand;
    }

    private String brand = "";

    public void setmFilterGroup(FilterGroup mFilterGroup) {
        this.mFilterGroup = mFilterGroup;
    }

    FilterGroup mFilterGroup;

    public static FilterAutoSerieFragment newInstance(String areaID) {

        Bundle args = new Bundle();

        FilterAutoSerieFragment fragment = new FilterAutoSerieFragment();
        fragment.setArguments(args);
        fragment.areaID = areaID;
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFilterAction = (FilterAction) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auto_brand_2, null);
    }

    public void upBrand(String brand) {
        this.brand = brand;
        loadData();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        autoBrandView = (RequestFailView) view.findViewById(R.id.auto_brand_view_2);
        autoBrandList = (PinnedHeaderExpandableListView) view.findViewById(R.id.auto_brand_list_2);

        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.item_filter_factor_head, null);
        mHeaderView.setOnClickListener(this);
        autoBrandList.addHeaderView(mHeaderView);

        client = new ThirdPartShopClient();
//        loadData();


//        //****************测试数据**********************/
//        ArrayList<SeriesGroup> brandGroups = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            SeriesGroup brandGroup = new SeriesGroup();
//            brandGroup.brandName = i + "";
//            ArrayList<Series> brands = new ArrayList<>();
//            for (int j = 0; j < 5; j++) {
//                Series brand = new Series();
//                brand.seriesName = "车系" + i + j;
//                brand.seriesThumb = "";
////                brand.filterValue = "filterValue" + i + j;
////                brand.filterKey = "filterKey" + i + j;
////                brand.brandInitial = i + "";
//                brands.add(brand);
//            }
//            brandGroup.series = brands;
//            brandGroups.add(brandGroup);
//        }
//        seriesGroups.clear();
//        seriesGroups.addAll(brandGroups);
//
//        adapter = new SeriesExpandableAdapter(getActivity(), brandGroups);
//        autoBrandList.setAdapter(adapter);
//        autoBrandList.setOnHeaderUpdateListener(adapter);
//        OtherUtil.openAllChild(adapter, autoBrandList);
    }

    private void loadData() {
        //加载数据
        client.filterAutoSeries(brand, areaID, new LoadingAnimResponseHandler(getActivity()) {
            @Override
            public void onSuccess(String response) {

              /*  ArrayList<SeriesGroup> tBrandsGroups = new ArrayList<>();

                SeriesGroup seriesGroup = new SeriesGroup();
                ArrayList<Series> series = JSONUtils.fromJson(response,
                        new TypeToken<ArrayList<Series>>() {
                        });
                seriesGroup.brandName = brand;
                seriesGroup.series = series;
                tBrandsGroups.add(seriesGroup);*/
                ArrayList<SeriesGroup> tBrandsGroups = JSONUtils.fromJson(response, new TypeToken<ArrayList<SeriesGroup>>() {
                });
                seriesGroups.clear();
                seriesGroups.addAll(tBrandsGroups);
                adapter = new FlashSaleSeriesExpandableAdapter(getActivity(), tBrandsGroups);
                autoBrandList.setAdapter(adapter);
                autoBrandList.setOnHeaderUpdateListener(adapter);
                OtherUtil.openAllChild(adapter, autoBrandList);
            }
        });
        autoBrandList.setOnChildClickListener(this);
    }


    @Override
    public String fragmentDescription() {
        return "车系";
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Series series = seriesGroups.get(groupPosition).series.get(childPosition);
        series.filterKey = FilterResultKey.SERIES_KEY;
        series.filterValue = series.seriesName;
        mFilterAction.filterListener(1, series, mFilterGroup);
        return false;
    }

    @Override
    public void onClick(View v) {
        Series series = new Series();
        series.filterKey = FilterResultKey.SERIES_KEY;
        mFilterAction.filterListener(0, series, mFilterGroup);
    }
}
