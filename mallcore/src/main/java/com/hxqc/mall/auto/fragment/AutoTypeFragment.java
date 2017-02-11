package com.hxqc.mall.auto.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.auto.adapter.AutoModelExpandableAdapter;
import com.hxqc.mall.auto.adapter.BrandExpandableAdapter;
import com.hxqc.mall.auto.adapter.SeriesExpandableAdapter;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoHelper;
import com.hxqc.mall.auto.controler.AutoTypeControl;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.AutoModelGroup;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.model.SeriesGroup;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.util.DebugLog;
import com.hxqc.widget.PinnedHeaderExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 16
 * FIXME
 * Todo 车辆列表
 */
public class AutoTypeFragment extends Fragment implements ExpandableListView.OnChildClickListener, View.OnClickListener {

    private static final String TAG = AutoInfoContants.LOG_J;
    private View rootView;
    private PinnedHeaderExpandableListView mAutoTypeListView;
    private BrandExpandableAdapter brandExpandableAdapter;
    private SeriesExpandableAdapter seriesExpandableAdapter;
    private View viewHeader;
    private String brandID;
    private String seriesName;
    private TextView mHeaderTitleView;
    private ImageView mHeaderBackGBView;
    private TextView mHeaderBackView;
    private AutoModelExpandableAdapter autoModelExpandableAdapter;
    private String brandName;
    private String brandThumb;
    private ArrayList<AutoModelGroup> autoModelGroups;
    private onBackData mOnBackData;
    private String seriesID;
    private String shopID;
    private RequestFailView mRequestFailView;
    private String seresBrandName;
    //    private AutoModelGroup autoModelGroup;

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public interface onBackData {
        void backData(String brand, String brandID, String brandThumb, String seresBrandName, String series, String seriesID, String model, String modelID);
    }

    public void setOnBackDataListener(onBackData onBackData) {
        this.mOnBackData = onBackData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DebugLog.i(TAG, "onCreateView");
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_auto_type, container, false);
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
        DebugLog.i(TAG, "onViewCreated");
        initView();

        initData();

        initEvent();
    }


    /**
     * 初始化数据
     */
    private void initData() {
        if (getActivity().getIntent() != null) {
//            shopID = getActivity().getIntent().getStringExtra("shopID");
            Bundle bundleExtra = getActivity().getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            shopID = bundleExtra.getString("shopID");
            DebugLog.i(TAG, "shopID:" + shopID);
            if (TextUtils.isEmpty(shopID)) {
                shopID = "";
            }
        }
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        mAutoTypeListView.setOnChildClickListener(this);
        mHeaderBackView.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mRequestFailView = (RequestFailView) rootView.findViewById(R.id.auto_type_view);
        mAutoTypeListView = (PinnedHeaderExpandableListView) rootView.findViewById(R.id.auto_type_list);
//        RelativeLayout headerView = (RelativeLayout) rootView.findViewById(R.id.auto_type_header);
        mHeaderTitleView = (TextView) rootView.findViewById(R.id.auto_type_list_header_title);
        mHeaderBackGBView = (ImageView) rootView.findViewById(R.id.auto_type_list_header_back_bg);
        mHeaderBackView = (TextView) rootView.findViewById(R.id.auto_type_list_header_back);
        /*if (viewHeader == null) {
            viewHeader = LayoutInflater.from(getActivity()).inflate(R.layout.layout_auto_type_list_header, null);
            mAutoTypeListView.addHeaderView(viewHeader, null, false);
            mHeaderTitleView = (TextView) viewHeader.findViewById(R.id.auto_type_list_header_title);
            mHeaderBackGBView = (ImageView) viewHeader.findViewById(R.id.auto_type_list_header_back_bg);
            mHeaderBackView = (TextView) viewHeader.findViewById(R.id.auto_type_list_header_back);
        }*/
        mHeaderBackView.setVisibility(View.GONE);
        mHeaderBackGBView.setVisibility(View.GONE);
    }

    private HashMap<Integer, Boolean> hashMap = new HashMap<>();

    /**
     * 请求品牌数据
     */
    public void setData(MyAuto mMyAuto) {
     /*   ArrayList<BrandGroup> brandGroupLocal = AutoHelper.getInstance().getBrandGroupLocal(getActivity(), AutoHelper.JSON_BRAND);
        if (brandGroupLocal.isEmpty()) {
            AutoTypeControl.getInstance().requestBrand(getActivity(), shopID, this);
        } else {
            brandSucceed(brandGroupLocal);
        }*/

        for (int i = 0; i < 3; i++) {
            hashMap.put(i, false);
        }

        if (mMyAuto != null) {
            DebugLog.i(TAG, mMyAuto.toString());
          /*  if (!TextUtils.isEmpty(editAutoInfoFragment.mMyAuto.brand)) {
                if (!AutoHelper.getInstance().getBrandGroupLocal(getActivity(), AutoHelper.JSON_BRAND).isEmpty()) {
                    int size = AutoHelper.getInstance().getBrandGroupLocal(getActivity(), AutoHelper.JSON_BRAND).size();
                    for (int i = 0; i < size; i++) {
                        if (!AutoHelper.getInstance().getBrandGroupLocal(getActivity(), AutoHelper.JSON_BRAND).get(i).group.isEmpty()) {
                            int size1 = AutoHelper.getInstance().getBrandGroupLocal(getActivity(), AutoHelper.JSON_BRAND).get(i).group.size();
                            for (int j = 0; j < size1; j++) {
                                hvBrand = editAutoInfoFragment.mMyAuto.brand.equals(AutoHelper.getInstance().getBrandGroupLocal(getActivity(), AutoHelper.JSON_BRAND).get(i).group.get(j).brandName);
                                if (hvBrand) {
                                    hashMap.put(0, true);
                                }
                            }
                        }
                    }
                }
            }*/
           /* if (!TextUtils.isEmpty(editAutoInfoFragment.mMyAuto.series)) {
                if (hvBrand) {
                    if (!AutoHelper.getInstance().getSeriesGroupLocal(getActivity(), AutoHelper.JSON_SERIES).isEmpty()) {
                        int size = AutoHelper.getInstance().getSeriesGroupLocal(getActivity(), AutoHelper.JSON_SERIES).size();
                        for (int i = 0; i < size; i++) {
                            if (!AutoHelper.getInstance().getSeriesGroupLocal(getActivity(), AutoHelper.JSON_SERIES).get(i).series.isEmpty()) {
                                int size1 = AutoHelper.getInstance().getSeriesGroupLocal(getActivity(), AutoHelper.JSON_SERIES).get(i).series.size();
                                for (int j = 0; j < size1; j++) {
                                    hvSeries = editAutoInfoFragment.mMyAuto.series.equals(AutoHelper.getInstance().getSeriesGroupLocal(getActivity(), AutoHelper.JSON_SERIES).get(i).series.get(j));
                                    if (hvSeries) {
                                        hashMap.put(1, true);
                                    }
                                }
                            }
                        }
                    }
                }
            }*/
          /*  if (!TextUtils.isEmpty(editAutoInfoFragment.mMyAuto.autoModel)) {
                if (AutoHelper.getInstance().getAutoModelGroupLocal(getActivity(), AutoHelper.JSON_MODEL).autoModelGroup != null && !AutoHelper.getInstance().getAutoModelGroupLocal(getActivity(), AutoHelper.JSON_MODEL).autoModelGroup.isEmpty()) {
                    int size = AutoHelper.getInstance().getAutoModelGroupLocal(getActivity(), AutoHelper.JSON_MODEL).autoModelGroup.size();
                    for (int i = 0; i < size; i++) {
                        hvAutoModel = editAutoInfoFragment.mMyAuto.autoModel.equals(AutoHelper.getInstance().getAutoModelGroupLocal(getActivity(), AutoHelper.JSON_MODEL).autoModelGroup.get(i).autoModel);
                        if (hvAutoModel) {
                            hashMap.put(2, true);
                        }
                    }
                }
            }*/
            if (!TextUtils.isEmpty(mMyAuto.autoModel)) {
                if (AutoHelper.getInstance().getAutoModelGroupNLocal(getActivity(), AutoHelper.JSON_MODEL) != null && !AutoHelper.getInstance().getAutoModelGroupNLocal(getActivity(), AutoHelper.JSON_MODEL).isEmpty()) {
                    int size = AutoHelper.getInstance().getAutoModelGroupNLocal(getActivity(), AutoHelper.JSON_MODEL).size();
                    DebugLog.i(TAG, "autoModel:" + size);
                    for (int i = 0; i < size; i++) {
                        int sizeN = AutoHelper.getInstance().getAutoModelGroupNLocal(getActivity(), AutoHelper.JSON_MODEL).get(i).autoModel.size();
                        for (int j = 0; j < sizeN; j++) {
                            hvAutoModel = mMyAuto.autoModel.equals(AutoHelper.getInstance().getAutoModelGroupNLocal(getActivity(), AutoHelper.JSON_MODEL).get(i).autoModel.get(j).autoModel);
                            if (hvAutoModel) {
                                hashMap.put(2, true);
                            }
                        }
                    }
                }
            }
        }
        DebugLog.i(TAG, hashMap.get(0) + "----" + hashMap.get(1) + "----" + hashMap.get(2));
        if (hashMap.get(2)) {
            DebugLog.i(TAG, "有匹配");
//            AutoTypeControl.getInstance().requestBrand(getActivity(), shopID, this);
            if (brandExpandableAdapter == null) {
                DebugLog.i(TAG, "brandExpandableAdapter");
                AutoTypeControl.getInstance().requestBrand(getActivity(), shopID, requestBrands);
            }
        } else {
            DebugLog.i(TAG, "无匹配");
            AutoTypeControl.getInstance().requestBrand(getActivity(), shopID, requestBrands);
        }
    }

    /**
     * 请求品牌数据
     *
     * @param myAutoGroups
     */
   /* public void setData(ArrayList<BrandGroup> myAutoGroups) {
        if (brandExpandableAdapter == null) {
            DebugLog.i(TAG, "BRAND_DATA");
            brandExpandableAdapter = new BrandExpandableAdapter(getActivity(), myAutoGroups);
            mAutoTypeListView.setAdapter(brandExpandableAdapter);
            mAutoTypeListView.setOnHeaderUpdateListener(brandExpandableAdapter);
            OtherUtil.openAllChild(brandExpandableAdapter, mAutoTypeListView);
        }
        brandExpandableAdapter.notifyData(myAutoGroups);
    }*/

    /**
     * 切换数据显示头
     *
     * @param flag
     */
    private void changeData(int flag) {
        if (flag == BRAND_DATA) {
            mHeaderTitleView.setText("选择品牌");
            mHeaderBackGBView.setVisibility(View.GONE);
            mHeaderBackView.setVisibility(View.GONE);
        } else if (flag == SERIES_DATA) {
            mHeaderTitleView.setText("选择车系");
            mHeaderBackGBView.setVisibility(View.VISIBLE);
            mHeaderBackView.setVisibility(View.VISIBLE);
        } else if (flag == AUTO_MODEL_DATA) {
            mHeaderTitleView.setText("选择车型");
            mHeaderBackGBView.setVisibility(View.VISIBLE);
            mHeaderBackView.setVisibility(View.VISIBLE);
        }
    }

    /*@Override
    public void onBrandSucceed(ArrayList<BrandGroup> myAutoGroups) {
        brandSucceed(myAutoGroups);
        AutoHelper.getInstance().createBrandGroupLocal(getActivity(), myAutoGroups, AutoHelper.JSON_BRAND);
    }

    @Override
    public void onBrandFailed(boolean offLine) {
        dataType = SERIES_DATA;
        failedAction();
//        mAutoTypeListView.setEmptyView(mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail));
    }*/

    private CallBackControl.CallBack<ArrayList<BrandGroup>> requestBrands = new CallBackControl.CallBack<ArrayList<BrandGroup>>() {
        @Override
        public void onSuccess(ArrayList<BrandGroup> response) {
            brandSucceed(response);
            AutoHelper.getInstance().createBrandGroupLocal(getActivity(), response, AutoHelper.JSON_BRAND);
        }

        @Override
        public void onFailed(boolean offLine) {
            dataType = SERIES_DATA;
            failedAction();
        }
    };

    private final int BRAND_DATA = 0;
    private final int SERIES_DATA = 1;
    private final int AUTO_MODEL_DATA = 2;
    private final int Finish_DATA = 3;
    private int dataType = BRAND_DATA;
    private boolean isLocal = false;
    private String autoModelID;
    private String autoModel;
    private boolean hvBrand = false;
    private boolean hvSeries = false;
    private boolean hvAutoModel = false;

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (dataType == SERIES_DATA) {
            if (!AutoHelper.getInstance().getBrandGroupLocal(getActivity(), AutoHelper.JSON_BRAND).isEmpty()) {
                DebugLog.i(TAG, "本地品牌");
                brandID = AutoHelper.getInstance().getBrandGroupLocal(getActivity(), AutoHelper.JSON_BRAND).get(groupPosition).group.get(childPosition).brandID;
                brandName = AutoHelper.getInstance().getBrandGroupLocal(getActivity(), AutoHelper.JSON_BRAND).get(groupPosition).group.get(childPosition).brandName;
                brandThumb = AutoHelper.getInstance().getBrandGroupLocal(getActivity(), AutoHelper.JSON_BRAND).get(groupPosition).group.get(childPosition).brandThumb;
                if (!AutoHelper.getInstance().getSeriesGroupLocal(getActivity(), AutoHelper.JSON_SERIES).isEmpty()) {
                    DebugLog.i(TAG, "本地车系");
                    int size = AutoHelper.getInstance().getSeriesGroupLocal(getActivity(), AutoHelper.JSON_SERIES).size();
                    for (int i = 0; i < size; i++) {
                        hvSeries = AutoHelper.getInstance().getSeriesGroupLocal(getActivity(), AutoHelper.JSON_SERIES).get(i).brandName.equals(brandName);
                    }
                    if (hvSeries) {
                        DebugLog.i(TAG, "有匹配本地车系");
                        seriesSucceed(AutoHelper.getInstance().getSeriesGroupLocal(getActivity(), AutoHelper.JSON_SERIES));
                    } else {
                        DebugLog.i(TAG, "无匹配本地车系");
                        AutoTypeControl.getInstance().requestSeries(getActivity(), shopID, brandName, this.brandID, requestSeries);
                    }
                } else {
                    DebugLog.i(TAG, "无本地车系");
                    AutoTypeControl.getInstance().requestSeries(getActivity(), shopID, brandName, this.brandID, requestSeries);
                }
            } else {
                DebugLog.i(TAG, "无地品牌");
                brandID = AutoTypeControl.getInstance().getBrandGroups().get(groupPosition).group.get(childPosition).brandID;
                brandName = AutoTypeControl.getInstance().getBrandGroups().get(groupPosition).group.get(childPosition).brandName;
                brandThumb = AutoHelper.getInstance().getBrandGroupLocal(getActivity(), AutoHelper.JSON_BRAND).get(groupPosition).group.get(childPosition).brandThumb;
                DebugLog.i(TAG, "SERIES_DATA:" + brandID);
                DebugLog.i(TAG, "SERIES_DATA:" + brandName);
                DebugLog.i(TAG, "SERIES_DATA:" + brandThumb);
                AutoTypeControl.getInstance().requestSeries(getActivity(), shopID, brandName, this.brandID, requestSeries);
            }
        } else if (dataType == AUTO_MODEL_DATA) {
            if (!AutoHelper.getInstance().getSeriesGroupLocal(getActivity(), AutoHelper.JSON_SERIES).isEmpty()) {
                seriesID = AutoHelper.getInstance().getSeriesGroupLocal(getActivity(), AutoHelper.JSON_SERIES).get(groupPosition).series.get(childPosition).seriesID;
                seriesName = AutoHelper.getInstance().getSeriesGroupLocal(getActivity(), AutoHelper.JSON_SERIES).get(groupPosition).series.get(childPosition).seriesName;
                seresBrandName = AutoHelper.getInstance().getSeriesGroupLocal(getActivity(), AutoHelper.JSON_SERIES).get(groupPosition).brandName;
            /*    if (AutoHelper.getInstance().getAutoModelGroupLocal(getActivity(), AutoHelper.JSON_MODEL).autoModelGroup != null && !AutoHelper.getInstance().getAutoModelGroupLocal(getActivity(), AutoHelper.JSON_MODEL).autoModelGroup.isEmpty()) {
                    DebugLog.i(TAG, "本地车型");
                    hvAutoModel = AutoHelper.getInstance().getAutoModelGroupLocal(getActivity(), AutoHelper.JSON_MODEL).seriesName.equals(seriesName);
                    if (hvAutoModel) {
                        DebugLog.i(TAG, "有匹配本地车型");
                        autoModelSuccess(AutoHelper.getInstance().getAutoModelGroupLocal(getActivity(), AutoHelper.JSON_MODEL));
                        isLocal = true;
                    } else {
                        DebugLog.i(TAG, "无匹配本地车型");
//                        AutoTypeControl.getInstance().requestAutoModel(getActivity(), shopID, brandName, brandID, seriesName, seriesID, autoModelHandler);
                        AutoTypeControl.getInstance().requestAutoModelN(getActivity(), shopID, brandName, brandID, seriesName, seriesID, autoModelN);
                        isLocal = false;
                    }*/

                if (AutoHelper.getInstance().getAutoModelGroupNLocal(getActivity(), AutoHelper.JSON_MODEL) != null && !AutoHelper.getInstance().getAutoModelGroupNLocal(getActivity(), AutoHelper.JSON_MODEL).isEmpty()) {
                    DebugLog.i(TAG, "本地车系");
                    int size = AutoHelper.getInstance().getAutoModelGroupNLocal(getActivity(), AutoHelper.JSON_MODEL).size();
                    for (int i = 0; i < size; i++) {
                        hvAutoModel = AutoHelper.getInstance().getAutoModelGroupNLocal(getActivity(), AutoHelper.JSON_MODEL).get(i).seriesName.equals(seriesName);
                    }
                    if (hvAutoModel) {
                        DebugLog.i(TAG, "有匹配本地车系");
                        autoModelNSuccess(AutoHelper.getInstance().getAutoModelGroupNLocal(getActivity(), AutoHelper.JSON_MODEL));
                        isLocal = true;
                    } else {
                        DebugLog.i(TAG, "无匹配本地车系");
                        AutoTypeControl.getInstance().requestAutoModelN(getActivity(), shopID, brandName, brandID, seriesName, seriesID, autoModelN);
                        isLocal = false;
                    }
                } else {
                    DebugLog.i(TAG, "无本地车型");
//                    AutoTypeControl.getInstance().requestAutoModel(getActivity(), shopID, brandName, brandID, seriesName, seriesID, autoModelHandler);
                    AutoTypeControl.getInstance().requestAutoModelN(getActivity(), shopID, brandName, brandID, seriesName, seriesID, autoModelN);
                    isLocal = false;
                }
            } else {
                seriesID = AutoTypeControl.getInstance().getSeriesGroups().get(groupPosition).series.get(childPosition).seriesID;
                seriesName = AutoTypeControl.getInstance().getSeriesGroups().get(groupPosition).series.get(childPosition).seriesName;
                seresBrandName = AutoTypeControl.getInstance().getSeriesGroups().get(groupPosition).brandName;
                DebugLog.i(TAG, "AUTO_MODEL_DATA:" + seriesID + seriesName + seresBrandName);
//                AutoTypeControl.getInstance().requestAutoModel(getActivity(), shopID, brandName, brandID, seriesName, seriesID, this);
                AutoTypeControl.getInstance().requestAutoModelN(getActivity(), shopID, brandName, brandID, seriesName, seriesID, autoModelN);
            }
        } else if (dataType == Finish_DATA) {
            DebugLog.i(TAG, "Finish~~~~");
            if (isLocal) {
                autoModelID = AutoHelper.getInstance().getAutoModelGroupNLocal(getActivity(), AutoHelper.JSON_MODEL).get(groupPosition).autoModel.get(childPosition).autoModelID;
                autoModel = AutoHelper.getInstance().getAutoModelGroupNLocal(getActivity(), AutoHelper.JSON_MODEL).get(groupPosition).autoModel.get(childPosition).autoModel;
            } else {
//                autoModelID = autoModelGroups.get(groupPosition).autoModelGroup.get(childPosition).autoModelID;
//                autoModel = autoModelGroups.get(groupPosition).autoModelGroup.get(childPosition).autoModel;
                if (autoModelGroups != null && !autoModelGroups.isEmpty()) {
                    autoModelID = autoModelGroups.get(groupPosition).autoModel.get(childPosition).autoModelID;
                    autoModel = autoModelGroups.get(groupPosition).autoModel.get(childPosition).autoModel;
                }
            }
//            String backData = brandName + seriesName + autoModel;
            if (!TextUtils.isEmpty(brandName) && !TextUtils.isEmpty(brandID) && !TextUtils.isEmpty(seriesName) && !TextUtils.isEmpty(seriesID) && !TextUtils.isEmpty(autoModel) && !TextUtils.isEmpty(autoModelID)) {

                DebugLog.i(TAG, "brandName:" + brandName + ",brandID:" + brandID);
                DebugLog.i(TAG, "seriesName:" + seriesName + ",seriesID:" + seriesID);
                DebugLog.i(TAG, "autoModel:" + autoModel + ",autoModelID:" + autoModelID);
                mOnBackData.backData(brandName, brandID, brandThumb, seresBrandName, seriesName, seriesID, autoModel, autoModelID);
            }
        }
        return false;
    }

    /*@Override
    public void onSeriesSucceed(ArrayList<SeriesGroup> myAutoGroups) {
        seriesSucceed(myAutoGroups);
        AutoHelper.getInstance().createSeriesGroupLocal(getActivity(), myAutoGroups, AutoHelper.JSON_SERIES);
    }

    @Override
    public void onSeriesFailed(boolean offLine) {
        dataType = AUTO_MODEL_DATA;
        failedAction();
//        mAutoTypeListView.setEmptyView(mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail));
    }*/

    private CallBackControl.CallBack<ArrayList<SeriesGroup>> requestSeries = new CallBackControl.CallBack<ArrayList<SeriesGroup>>() {
        @Override
        public void onSuccess(ArrayList<SeriesGroup> response) {
            seriesSucceed(response);
            AutoHelper.getInstance().createSeriesGroupLocal(getActivity(), response, AutoHelper.JSON_SERIES);
        }

        @Override
        public void onFailed(boolean offLine) {
            dataType = AUTO_MODEL_DATA;
            failedAction();
            changeData(SERIES_DATA);
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.auto_type_list_header_back) {
            DebugLog.i(TAG, "返回" + dataType);
            backAction();
        }
    }

    private void backAction() {
        mRequestFailView.setVisibility(View.GONE);
        if (dataType == Finish_DATA) {
            DebugLog.i(TAG, "SERIES_DATA");
//                autoModelGroup = null;
            autoModelGroups = null;
            if (AutoTypeControl.getInstance().getSeriesGroups() == null) {
                seriesExpandableAdapter.notifyData(AutoHelper.getInstance().getSeriesGroupLocal(getActivity(), AutoHelper.JSON_SERIES));
            } else {
                seriesExpandableAdapter.notifyData(AutoTypeControl.getInstance().getSeriesGroups());
            }
            mAutoTypeListView.setAdapter(seriesExpandableAdapter);
            mAutoTypeListView.setOnHeaderUpdateListener(seriesExpandableAdapter);
            OtherUtil.openAllChild(seriesExpandableAdapter, mAutoTypeListView);
            changeData(SERIES_DATA);
            dataType = AUTO_MODEL_DATA;
        } else if (dataType == SERIES_DATA) {
            DebugLog.i(TAG, "BRAND_DATA");
            brandExpandableAdapter.notifyData(AutoTypeControl.getInstance().getBrandGroups());
            mAutoTypeListView.setAdapter(brandExpandableAdapter);
            mAutoTypeListView.setOnHeaderUpdateListener(brandExpandableAdapter);
            OtherUtil.openAllChild(brandExpandableAdapter, mAutoTypeListView);
            changeData(BRAND_DATA);
        } else if (dataType == AUTO_MODEL_DATA) {
            DebugLog.i(TAG, "BRAND_DATA");
            brandExpandableAdapter.notifyData(AutoTypeControl.getInstance().getBrandGroups());
            mAutoTypeListView.setAdapter(brandExpandableAdapter);
            mAutoTypeListView.setOnHeaderUpdateListener(brandExpandableAdapter);
            OtherUtil.openAllChild(brandExpandableAdapter, mAutoTypeListView);
            dataType = SERIES_DATA;
            changeData(BRAND_DATA);
        }
    }

    /**
     * 刷新页面
     */
    private void refresh() {

        mRequestFailView.setEmptyButtonClick("刷新页面", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataType == SERIES_DATA) {
                    DebugLog.i(TAG, "刷新品牌");
                    AutoTypeControl.getInstance().requestBrand(getActivity(), shopID, refreshBrands);
                } else if (dataType == AUTO_MODEL_DATA) {
                    DebugLog.i(TAG, "刷新车系");
                    AutoTypeControl.getInstance().requestSeries(getActivity(), shopID, brandName, brandID, refreshSeries);
                } else if (dataType == Finish_DATA) {
                    DebugLog.i(TAG, "刷新车型");
//                    AutoTypeControl.getInstance().requestAutoModel(getActivity(), shopID, brandName, brandID, seriesName, seriesID, autoModelHandler);
                    AutoTypeControl.getInstance().requestAutoModelN(getActivity(), shopID, brandName, brandID, seriesName, seriesID, autoModelN);
                }
            }
        });
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
        mRequestFailView.setVisibility(View.VISIBLE);

    }

    /**
     * 错误显示
     */
    private void failedAction() {
        refresh();
    }

    /**
     * 数据空的显示
     */
    private void emptyAction() {
        mRequestFailView.setVisibility(View.VISIBLE);
        mRequestFailView.setEmptyDescription("未找到结果");
        mRequestFailView.setEmptyButtonClick("选择其他车系", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backAction();
                mRequestFailView.setVisibility(View.GONE);
            }
        });
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
    }

    /*private AutoTypeControl.BrandHandler brandHandler = new AutoTypeControl.BrandHandler() {
        @Override
        public void onBrandSucceed(ArrayList<BrandGroup> myAutoGroups) {
            if (myAutoGroups == null || myAutoGroups.isEmpty()) {
                emptyAction();
            } else {
                mRequestFailView.setVisibility(View.GONE);
                brandSucceed(myAutoGroups);
            }
        }

        @Override
        public void onBrandFailed(boolean offLine) {
            dataType = SERIES_DATA;
            failedAction();
            changeData(BRAND_DATA);
        }
    };*/

    private CallBackControl.CallBack<ArrayList<BrandGroup>> refreshBrands = new CallBackControl.CallBack<ArrayList<BrandGroup>>() {
        @Override
        public void onSuccess(ArrayList<BrandGroup> response) {
            if (response == null || response.isEmpty()) {
                emptyAction();
            } else {
                mRequestFailView.setVisibility(View.GONE);
                brandSucceed(response);
            }
        }

        @Override
        public void onFailed(boolean offLine) {
            dataType = SERIES_DATA;
            failedAction();
            changeData(BRAND_DATA);
        }
    };

    /*private AutoTypeControl.SeriesHandler seriesHandler = new AutoTypeControl.SeriesHandler() {
        @Override
        public void onSeriesSucceed(ArrayList<SeriesGroup> myAutoGroups) {
            if (myAutoGroups == null || myAutoGroups.isEmpty()) {
                emptyAction();
            } else {
                mRequestFailView.setVisibility(View.GONE);
                seriesSucceed(myAutoGroups);
            }
        }

        @Override
        public void onSeriesFailed(boolean offLine) {
            dataType = AUTO_MODEL_DATA;
            failedAction();
            changeData(SERIES_DATA);
        }
    };*/

    private CallBackControl.CallBack<ArrayList<SeriesGroup>> refreshSeries = new CallBackControl.CallBack<ArrayList<SeriesGroup>>() {
        @Override
        public void onSuccess(ArrayList<SeriesGroup> response) {
            if (response == null || response.isEmpty()) {
                emptyAction();
            } else {
                mRequestFailView.setVisibility(View.GONE);
                seriesSucceed(response);
            }
        }

        @Override
        public void onFailed(boolean offLine) {
            dataType = AUTO_MODEL_DATA;
            failedAction();
            changeData(SERIES_DATA);
        }
    };


    /*private AutoTypeControl.AutoModelHandler autoModelHandler = new AutoTypeControl.AutoModelHandler() {
        @Override
        public void onAutoModelSucceed(ArrayList<AutoModel> myAutoGroups) {
            autoModelSuccess(myAutoGroups);
        }

        @Override
        public void onAutoModelFailed(boolean offLine) {
            failedAction();
        }
    };*/

    /**
     * @param myAutoGroups
     */
    private void brandSucceed(ArrayList<BrandGroup> myAutoGroups) {
        if (brandExpandableAdapter == null) {
            brandExpandableAdapter = new BrandExpandableAdapter(getActivity(), myAutoGroups);
            mAutoTypeListView.setAdapter(brandExpandableAdapter);
            mAutoTypeListView.setOnHeaderUpdateListener(brandExpandableAdapter);
            OtherUtil.openAllChild(brandExpandableAdapter, mAutoTypeListView);
        } else {
            brandExpandableAdapter.notifyData(myAutoGroups);
            mAutoTypeListView.setAdapter(brandExpandableAdapter);
            mAutoTypeListView.setOnHeaderUpdateListener(brandExpandableAdapter);
            OtherUtil.openAllChild(brandExpandableAdapter, mAutoTypeListView);
        }
        dataType = SERIES_DATA;
        changeData(BRAND_DATA);
    }

    /**
     * @param myAutoGroups
     */
    private void seriesSucceed(ArrayList<SeriesGroup> myAutoGroups) {
        dataType = AUTO_MODEL_DATA;
        if (seriesExpandableAdapter == null) {
            seriesExpandableAdapter = new SeriesExpandableAdapter(getActivity(), myAutoGroups);
            mAutoTypeListView.setAdapter(seriesExpandableAdapter);
            mAutoTypeListView.setOnHeaderUpdateListener(seriesExpandableAdapter);
            OtherUtil.openAllChild(seriesExpandableAdapter, mAutoTypeListView);
        } else {
            seriesExpandableAdapter.notifyData(myAutoGroups);
            mAutoTypeListView.setAdapter(seriesExpandableAdapter);
            mAutoTypeListView.setOnHeaderUpdateListener(seriesExpandableAdapter);
            OtherUtil.openAllChild(seriesExpandableAdapter, mAutoTypeListView);
        }
        changeData(SERIES_DATA);
    }

    /**
     * @param myAutoGroups
     */
    /*private void autoModelSuccess(ArrayList<AutoModel> myAutoGroups) {
        if (autoModelGroup == null) {
            autoModelGroup = new AutoModelGroup(seriesID, seriesName, myAutoGroups);
            AutoHelper.getInstance().createAutoModelGroupLocal(getActivity(), autoModelGroup, AutoHelper.JSON_MODEL);
        }
        if (autoModelGroups == null) {
            autoModelGroups = new ArrayList<AutoModelGroup>();
        }
        autoModelGroups.add(autoModelGroup);
        if (autoModelExpandableAdapter == null) {
            autoModelExpandableAdapter = new AutoModelExpandableAdapter(getActivity(), autoModelGroups);
            mAutoTypeListView.setAdapter(autoModelExpandableAdapter);
            mAutoTypeListView.setOnHeaderUpdateListener(autoModelExpandableAdapter);
            OtherUtil.openAllChild(autoModelExpandableAdapter, mAutoTypeListView);
        } else {
            autoModelExpandableAdapter.notifyData(autoModelGroups);
            mAutoTypeListView.setAdapter(autoModelExpandableAdapter);
            mAutoTypeListView.setOnHeaderUpdateListener(autoModelExpandableAdapter);
            OtherUtil.openAllChild(autoModelExpandableAdapter, mAutoTypeListView);
        }
        changeData(AUTO_MODEL_DATA);
    }*/

    /**
     * @param autoModelGroup
     */
    /*private void autoModelSuccess(AutoModelGroup autoModelGroup) {
        if (autoModelGroups == null) {
            autoModelGroups = new ArrayList<AutoModelGroup>();
        }
        autoModelGroups.add(autoModelGroup);
        if (autoModelExpandableAdapter == null) {
            autoModelExpandableAdapter = new AutoModelExpandableAdapter(getActivity(), autoModelGroups);
            mAutoTypeListView.setAdapter(autoModelExpandableAdapter);
            mAutoTypeListView.setOnHeaderUpdateListener(autoModelExpandableAdapter);
            OtherUtil.openAllChild(autoModelExpandableAdapter, mAutoTypeListView);
        } else {
            autoModelExpandableAdapter.notifyData(autoModelGroups);
            mAutoTypeListView.setAdapter(autoModelExpandableAdapter);
            mAutoTypeListView.setOnHeaderUpdateListener(autoModelExpandableAdapter);
            OtherUtil.openAllChild(autoModelExpandableAdapter, mAutoTypeListView);
        }
        changeData(AUTO_MODEL_DATA);
    }*/

    /**
     * @param autoModelNGroups
     */
    private void autoModelNSuccess(ArrayList<AutoModelGroup> autoModelNGroups) {
        dataType = Finish_DATA;
        if (autoModelNGroups != null && !autoModelNGroups.isEmpty()) {
            autoModelGroups = autoModelNGroups;
            int size = autoModelNGroups.size();
            for (int i = 0; i < size; i++) {
                autoModelGroups.get(i).seriesID = seriesID;
                autoModelGroups.get(i).seriesName = seriesName;
            }
            AutoHelper.getInstance().createAutoModelGroupLocal(getActivity(), autoModelGroups, AutoHelper.JSON_MODEL);
        }
        if (autoModelExpandableAdapter == null) {
            autoModelExpandableAdapter = new AutoModelExpandableAdapter(getActivity(), autoModelNGroups);
            mAutoTypeListView.setAdapter(autoModelExpandableAdapter);
            mAutoTypeListView.setOnHeaderUpdateListener(autoModelExpandableAdapter);
            OtherUtil.openAllChild(autoModelExpandableAdapter, mAutoTypeListView);
        } else {
            autoModelExpandableAdapter.notifyData(autoModelNGroups);
            mAutoTypeListView.setAdapter(autoModelExpandableAdapter);
            mAutoTypeListView.setOnHeaderUpdateListener(autoModelExpandableAdapter);
            OtherUtil.openAllChild(autoModelExpandableAdapter, mAutoTypeListView);
        }
        changeData(AUTO_MODEL_DATA);
    }

    private CallBackControl.CallBack<ArrayList<AutoModelGroup>> autoModelN = new CallBackControl.CallBack<ArrayList<AutoModelGroup>>() {
        @Override
        public void onSuccess(ArrayList<AutoModelGroup> response) {
            if (response == null || response.isEmpty()) {
                emptyAction();
            } else {
                mRequestFailView.setVisibility(View.GONE);
                autoModelNSuccess(response);
            }
        }

        @Override
        public void onFailed(boolean offLine) {
            dataType = Finish_DATA;
            failedAction();
            changeData(AUTO_MODEL_DATA);
        }
    };

    @Override
    public void onDestroy() {
        AutoHelper.getInstance().killInstance(getActivity());
        super.onDestroy();
    }
}
