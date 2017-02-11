package com.hxqc.mall.thirdshop.maintenance.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.hxqc.mall.auto.controler.AutoTypeControl;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.AutoModelGroup;
import com.hxqc.mall.auto.model.Brand;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.auto.model.Series;
import com.hxqc.mall.auto.model.SeriesGroup;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.dialog.LoadingDialog;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.adapter.AutoBrandsListAdapter;
import com.hxqc.mall.thirdshop.maintenance.adapter.AutoModelListAdapter;
import com.hxqc.mall.thirdshop.maintenance.adapter.AutoSeriesListAdapter;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-03-16
 * FIXME
 * Todo 选择车型
 */
public class SelectAutoFragment extends FunctionFragment implements View.OnClickListener, ExpandableListView.OnChildClickListener {
    private OnSelectedCallBack onSelectedCallBack;
    private String shopID = "";
    private String brandID = "";
    private String brandName = "";
    private String brandThumb = "";
    private String seriesName = "";
    private String seriesID = "";
    private static final int BRAND = 0;
    private static final int SERIES = 1;
    private static final int MODEL = 2;
    private int flag = BRAND;
    private ArrayList<SeriesGroup> seriesGroups = new ArrayList<>();
    private ArrayList<BrandGroup> brandGroups = new ArrayList<>();
    private AutoBrandsListAdapter autoBrandsListAdapter;
    private AutoSeriesListAdapter autoSeriesListAdapter;
    private AutoModelListAdapter autoModelListAdapter;
    private Context context;
    private ArrayList<AutoModelGroup> modelGroups = new ArrayList<>();
    private TextView title, back;
    private PinnedHeaderExpandableListView listViewBrands;

    private LoadingDialog dialog;
    private String seriesBrandName;

    public static SelectAutoFragment createInstance(String shopID, OnSelectedCallBack onSelectedCallBack2) {
        SelectAutoFragment selectAutoFragment = new SelectAutoFragment();
        selectAutoFragment.shopID = shopID;
        selectAutoFragment.onSelectedCallBack = onSelectedCallBack2;
        return selectAutoFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    //10.0.14.113:8080
    @Override
    public void onClick(View v) {
        if (flag == MODEL) {
            loadSeriesData();
        } else if (flag == SERIES) {
            loadBrandData();
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (flag == MODEL)
//            loadModelData();
//        if (flag == SERIES)
//            loadSeriesData();
//        if (flag == BRAND)
//            loadBrandData();
//    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        int viewID = parent.getId();
        if (viewID == R.id.select_brand_list) {
            if (flag == BRAND) {
                Brand brand = brandGroups.get(groupPosition).group.get(childPosition);
                brandID = brand.brandID;
                brandName = brand.brandName;
                brandThumb = brand.brandThumb;
                loadSeriesData();
            } else if (flag == SERIES) {
                Series series = seriesGroups.get(groupPosition).series.get(childPosition);
                seriesID = series.seriesID;
                seriesName = series.seriesName;
                seriesBrandName = seriesGroups.get(groupPosition).brandName;
                loadModelData();
            } else if (flag == MODEL) {
                String model = modelGroups.get(groupPosition).autoModel.get(childPosition).autoModel;
                String modelID = modelGroups.get(groupPosition).autoModel.get(childPosition).autoModelID;
                onSelectedCallBack.onSelected(brandName, brandID, brandThumb, seriesBrandName, seriesName, seriesID, model, modelID);
            }
        }
        return false;

    }

    private void loadBrandData() {
        dialog.show();
        AutoTypeControl.getInstance().requestBrand(getActivity(), shopID, new CallBackControl.CallBack<ArrayList<BrandGroup>>() {
            @Override
            public void onFailed(boolean offLine) {
                dismissDialog();
            }

            @Override
            public void onSuccess(ArrayList<BrandGroup> obj) {
                brandGroups.clear();
                brandGroups.addAll(obj);
                autoBrandsListAdapter = new AutoBrandsListAdapter(brandGroups, getActivity());
                listViewBrands.setAdapter(autoBrandsListAdapter);
                listViewBrands.setOnHeaderUpdateListener(autoBrandsListAdapter);
                OtherUtil.openAllChild(autoBrandsListAdapter, listViewBrands);
                flag = BRAND;
                back.setVisibility(View.GONE);
                title.setText("选择品牌");
                dismissDialog();
            }
        });
    }

    private void dismissDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    private void loadSeriesData() {
        dialog.show();
        AutoTypeControl.getInstance().requestSeries(getActivity(), shopID, brandName, brandID,
                new CallBackControl.CallBack<ArrayList<SeriesGroup>>() {
                    @Override
                    public void onFailed(boolean offLine) {
                        ToastHelper.showRedToast(getActivity(), "暂无" + brandName + "品牌车辆");
                        dismissDialog();
                    }

                    @Override
                    public void onSuccess(ArrayList<SeriesGroup> data) {
                        seriesGroups.clear();
                        seriesGroups.addAll(data);
                        autoSeriesListAdapter = new AutoSeriesListAdapter(seriesGroups, getActivity());
                        listViewBrands.setAdapter(autoSeriesListAdapter);
                        listViewBrands.setOnHeaderUpdateListener(autoSeriesListAdapter);
                        OtherUtil.openAllChild(autoSeriesListAdapter, listViewBrands);
                        flag = SERIES;
                        back.setVisibility(View.VISIBLE);
                        title.setText("选择车系");
                        dismissDialog();
                    }
                });

    }

    private void loadModelData() {
        dialog.show();
        AutoTypeControl.getInstance().requestAutoModelN(getActivity(), shopID, brandName, brandID, seriesName, seriesID,
                new CallBackControl.CallBack<ArrayList<AutoModelGroup>>() {
                    @Override
                    public void onFailed(boolean offLine) {
                        ToastHelper.showRedToast(getActivity(), "暂无" + seriesName + "系列车辆");
                        dismissDialog();
                    }

                    @Override
                    public void onSuccess(ArrayList<AutoModelGroup> data) {
//                        ModelGroup mg = new ModelGroup();
//                        mg.models = data;
//                        mg.groupName = seriesName;
//                        modelGroups.clear();
//                        modelGroups.add(mg);
                        modelGroups = data;
                        autoModelListAdapter = new AutoModelListAdapter(modelGroups, getActivity());
                        autoModelListAdapter.notifyDataSetChanged();
                        listViewBrands.setAdapter(autoModelListAdapter);
                        listViewBrands.setOnHeaderUpdateListener(autoModelListAdapter);
                        OtherUtil.openAllChild(autoModelListAdapter, listViewBrands);
                        flag = MODEL;
                        back.setVisibility(View.VISIBLE);
                        title.setText("选择车型");
                        dismissDialog();
                    }
                });

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_auto, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = (TextView) view.findViewById(R.id.title_txt);
        back = (TextView) view.findViewById(R.id.back);
        listViewBrands = (PinnedHeaderExpandableListView) view.findViewById(R.id.select_brand_list);
        autoBrandsListAdapter = new AutoBrandsListAdapter(brandGroups, context);
        autoSeriesListAdapter = new AutoSeriesListAdapter(seriesGroups, context);
        autoModelListAdapter = new AutoModelListAdapter(modelGroups, context);
        dialog = new LoadingDialog(getActivity());
        initEvent();
        loadBrandData();
    }


    private void initEvent() {
        back.setOnClickListener(this);
        listViewBrands.setOnChildClickListener(this);
    }

    @Override
    public String fragmentDescription() {
        return "选择车型";
    }

    public interface OnSelectedCallBack {
        void onSelected(String brand, String brandID, String brandThumb, String seriesBrandName, String series, String seriesID, String model, String modelID);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AutoTypeControl.getInstance().killInstance();
    }
}
