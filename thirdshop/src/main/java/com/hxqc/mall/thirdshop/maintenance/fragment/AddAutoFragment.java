package com.hxqc.mall.thirdshop.maintenance.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hxqc.mall.auto.controler.AutoHelper;
import com.hxqc.mall.auto.controler.AutoTypeControl;
import com.hxqc.mall.auto.controler.MyAutoInfoHelper;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.vedit.EditTextValidatorView;
import com.hxqc.mall.core.views.vedit.manager.VWholeEditManager;
import com.hxqc.mall.core.views.vedit.tech.VMallDivNotNull;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.activity.shop.MaintenanceHomeActivity_1;
import com.hxqc.mall.thirdshop.maintenance.control.HomeDataHelper;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2016-02-16
 * FIXME
 * Todo 维修保养添加车辆
 */
public class AddAutoFragment extends MaintenanceBaseFragment implements
        TextView.OnEditorActionListener, View.OnClickListener, View.OnTouchListener {
    private static final String TAG = "AddAutoFragment";
    private EditTextValidatorView auto_distance;
    private EditTextValidatorView auto_model;
    private Button complete;
    private MyAuto auto = new MyAuto();//我的车辆
    private ArrayList<BrandGroup> shopBrandGroups;//店铺主营车辆

    private MaintenanceHomeActivity_1 activity;

    private ScrollView mScrollView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_no_auto_data, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MaintenanceHomeActivity_1) context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
        initEvent();
    }

    public static AddAutoFragment newInstance(AddAutoListener listener) {
        Bundle args = new Bundle();
        AddAutoFragment fragment = new AddAutoFragment();
        fragment.setArguments(args);
        fragment.setAddAutoListener(listener);
        return fragment;
    }

    private void initView(View noDataLayout) {
        complete = (Button) noDataLayout.findViewById(R.id.complete);
        auto_distance = (EditTextValidatorView) noDataLayout.findViewById(R.id.commen_auto_info_mileage);
        auto_distance.addValidator(new VMallDivNotNull("请输入行驶里程(必填))", ""));
        auto_model = (EditTextValidatorView) noDataLayout.findViewById(R.id.commen_auto_info_type);
        auto_model.addValidator(new VMallDivNotNull("请选择车辆信息(必填)", ""));
        mScrollView = (ScrollView) noDataLayout.findViewById(R.id.home_scroll_view);
    }

    private void initData() {
        shopBrandGroups = new ArrayList<>();
    }


    private void initEvent() {
        auto_model.setOnClickListener(this);
        complete.setOnClickListener(this);
        mScrollView.setOnTouchListener(this);

    }


    public void notifyData(ArrayList<BrandGroup> shopBrandGroups) {
        this.shopBrandGroups = shopBrandGroups;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AutoTypeControl.getInstance().killInstance();
        HomeDataHelper.getInstance(getActivity()).destory();
        MyAutoInfoHelper.getInstance(getActivity()).destory();
        //本地数据库
        AutoHelper.getInstance().killInstance(getActivity());
    }

    /**
     * 提交车辆信息
     */
    private void commitAutoData() {
        //隐藏键盘
        if (getActivity().getCurrentFocus() != null) {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getActivity().getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        }
        auto.drivingDistance = auto_distance.getText().toString();

//        if (mPlateNumberView.getPlateNumber().length() < 7) {
//            ToastHelper.showRedToast(getActivity(), "请填写车牌号");
//            return;
//        }
        //车辆信息为空
//        if (TextUtils.isEmpty(auto.brandID) || TextUtils.isEmpty(auto.seriesID) ||
//                TextUtils.isEmpty(auto.autoModelID)) {
//            ToastHelper.showRedToast(getActivity(), "请选择车辆信息");
//            return;
//        }
        //行驶里程没有填写
//        if (auto.drivingDistance.isEmpty()) {
//            ToastHelper.showRedToast(getActivity(), "请填写行驶里程");
//            return;
//        }
        VWholeEditManager vWholeEditManager = new VWholeEditManager(getActivity());
        vWholeEditManager.autoAddVViews();
        if (!vWholeEditManager.validate()) {
            return;
        }

        if (!TextUtils.isEmpty(auto.brandID) && !TextUtils.isEmpty(auto.seriesID) &&
                !TextUtils.isEmpty(auto.autoModelID) /*&& !TextUtils.isEmpty(auto.plateNumber)*/ &&
                !TextUtils.isEmpty(auto.drivingDistance)
                ) {
            //车辆的信息和本店的品牌不符合

            if (!MyAutoInfoHelper.getInstance(getActivity()).checkBrand(auto, shopBrandGroups)) {
                ToastHelper.showRedToast(getActivity(),
                        "本店暂时只为" + MyAutoInfoHelper.getInstance(getActivity())
                                .getBrands(shopBrandGroups) + "品牌车辆提供维修保养服务");
                return;
            }

            //TODO 数据库储存
//            AutoHelper.getInstance().addMyAuto(getActivity(), auto);
            //JSON储存
//            MyAutoInfoHelper.getInstance(getActivity()).addAutoLocal(auto);
            AutoHelper.getInstance().addOrEditAutoLocaltoLinkedList(getActivity(),auto,AutoHelper.AUTO_LOCAL_INFO);
            if (null != addAutoListener) {
                addAutoListener.onAddLocal(auto);
            }

        }
    }

    /**
     * 检索出车辆信息显示在上面
     *
     * @param auto
     */
    private void updateNoDateLayout(MyAuto auto) {
        if (auto == null) {
//            auto_VIN.setText("");
            auto_distance.setText("");
//            nameItem.getmContentView().setText("");
            auto_model.setText("");
//            phoneItem.getmContentView().setText("");
//            last_data_layout.setVisibility(View.GONE);
        } else {
//            auto_VIN.setText(auto.VIN);
            auto_distance.setText(auto.drivingDistance + "");

//            nameItem.getmContentView().setText(auto.ownName);
            auto_model.setText(auto.brand + auto.series + auto.autoModel);

//            phoneItem.getmContentView().setText(auto.ownPhone);
            boolean empty = TextUtils.isEmpty(auto.lastMaintenanceDate);
            if (!empty || !TextUtils.isEmpty(auto.lastMaintenanceDistance + "")/*auto.lastMaintenanceDistance != -1*/) {
                //有数据显示出来
//                last_data_layout.setVisibility(View.VISIBLE);
            }
//            else last_data_layout.setVisibility(View.GONE);
//            last_maintenance_distance.setText("行驶里程" + auto.lastMaintenanceDistance + "公里");
//            last_maintenance_time.setText("您上次保养的时间" + auto.lastMaintenanceDate);
            //没有数据就隐藏
//            last_maintenance_time.setVisibility(empty ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.commen_auto_info_type) {
            //打开侧滑
            activity.openMenu();
        } else if (i == R.id.complete)
            //提交车辆信息
            commitAutoData();
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            commitAutoData();
            return true;
        }
        return false;
    }


    public void setAddAutoListener(AddAutoListener addAutoListener) {
        this.addAutoListener = addAutoListener;
    }

    private AddAutoListener addAutoListener;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
//            mPlateNumberView.dismissPopup();
        }
        return false;
    }

    public void updateModel(String brand, String brandID, String brandThumb, String seriesBrandName, String series, String seriesID, String model, String modelID) {
        auto.brand = brand;
        auto.brandID = brandID;
        auto.series = series;
        auto.seriesID = seriesID;
        auto.autoModel = model;
        auto.autoModelID = modelID;
        auto.brandThumb = brandThumb;
        auto.brandName = seriesBrandName;
        auto_model.setText(/*brand + series +*/ model);
    }

    public interface AddAutoListener {
        void onCompleted(MyAuto auto);

        void onAddLocal(MyAuto auto);

        void onCompleteFailture(String message);
    }

}
