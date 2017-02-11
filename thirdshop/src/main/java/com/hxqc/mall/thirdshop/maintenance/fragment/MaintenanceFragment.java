//package com.hxqc.mall.thirdshop.maintenance.fragment;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.LinearLayoutCompat;
//import android.text.InputFilter;
//import android.text.InputType;
//import android.text.TextUtils;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.EditorInfo;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import com.hxqc.mall.auto.model.BrandGroup;
//import com.hxqc.mall.auto.model.MyAuto;
//import com.hxqc.mall.auto.view.CommenAutoInfoHeaderView;
//import com.hxqc.mall.auto.view.CommonEditInfoItemView;
//import com.hxqc.mall.auto.view.CommenPlateNumberView;
//import com.hxqc.mall.auto.controler.MyAutoInfoHelper;
//import com.hxqc.mall.auto.view.PlateNumberEditText;
//import com.hxqc.mall.core.controler.UserInfoHelper;
//import com.hxqc.mall.core.interfaces.LoadDataCallBack;
//import com.hxqc.mall.core.util.ToastHelper;
//import com.hxqc.mall.core.views.vedit.EditTextValidatorView;
//import com.hxqc.mall.thirdshop.R;
//import com.hxqc.mall.thirdshop.activity.shop.MaintenanceHomeActivity;
//import com.hxqc.mall.thirdshop.maintenance.adapter.OtherMaintenancePackageListAdapter;
//import com.hxqc.mall.thirdshop.maintenance.control.HomeDataHelper;
//import com.hxqc.mall.thirdshop.maintenance.control.SelectAutoHelper;
//import com.hxqc.mall.thirdshop.maintenance.model.Mechanic;
//import com.hxqc.mall.thirdshop.maintenance.model.ServiceAdviser;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceHome;
//import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenancePackage;
//import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
//import com.hxqc.mall.thirdshop.maintenance.views.AutoDetailView;
//import com.hxqc.mall.thirdshop.maintenance.views.RecommendMaintenanceProgramView;
//import com.hxqc.mall.thirdshop.maintenance.views.ServiceAdviserListView;
//import com.hxqc.mall.thirdshop.maintenance.views.TechnicianListView;
//import com.hxqc.mall.thirdshop.model.ShopInfo;
//import com.hxqc.util.DebugLog;
//import com.hxqc.widget.ListViewNoSlide;
//
//import java.util.ArrayList;
//
///**
// * Author:李烽
// * Date:2016-02-16
// * FIXME
// * Todo 维修保养
// */
//@Deprecated
//public class MaintenanceFragment extends MaintenanceBaseFragment implements
//        View.OnClickListener, RecommendMaintenanceProgramView.OnSeeDetailClickListener,
//        OtherMaintenancePackageListAdapter.OnPackageClickListener,
//        OtherMaintenancePackageListAdapter.OnMoreClickListener,
//        TextView.OnEditorActionListener, MaintenanceHomeActivity.OnShopInfoCallBack {
//    private static final String TAG = "MaintenanceFragment";
//    private ScrollView home_scroll_view;
//    private AutoDetailView autoDetailView;
//    private RecommendMaintenanceProgramView maintenanceProgramView;
//    //    private RecommendMaintenanceProgramView baseMaintenanceProgramView;
//    private ListViewNoSlide otherPackageList;
//    private ListViewNoSlide basePackageList;
//    private ServiceAdviserListView adviserListView;
//    private TechnicianListView technicianListView;
//    private LinearLayoutCompat maintenance_layout;
//    private MaintenanceHome maintenanceHome;//首页数据
//    private MaintenancePackage recommendProgram;
//    private MaintenancePackage baseMaintenancePackage;
//    private ArrayList<MaintenancePackage> baseListData = new ArrayList<>();
//    private ArrayList<MaintenancePackage> otherListData = new ArrayList<>();
//    private OtherMaintenancePackageListAdapter adapter;
//    private OtherMaintenancePackageListAdapter baseAdapter;
//    private ArrayList<ServiceAdviser> serviceAdvisers;
//    private ArrayList<Mechanic> techniciens;
//    /*没有数据的时候的view*/
//    private View noDataLayout;//无车辆数据的时候的现实的view
//    private LinearLayout last_data_layout;//橙色的字的父控件
//    //    private PlateNumberTextView license_city;
////    private PlateNumberTextView license_number;
////    private EditText auto_VIN, auto_distance,
////            your_name, your_phone;
////    private CommonEditInfoItemView nameItem, phoneItem;
//    private EditTextValidatorView auto_model;
//    private TextView last_maintenance_time, last_maintenance_distance;
//    private Button complete;
//
//    private MyAuto auto = new MyAuto();//我的车辆
//    private ArrayList<BrandGroup> shopBrandGroups;//店铺主营车辆
////    private CommenAutoInfoHeaderView commen_auto_info;
//    private PlateNumberEditText mPlateNumberView;
//    private MaintenanceHomeActivity activity;
//
//    private ShopInfo shopInfo = null;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_maintenance, container, false);
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        activity = (MaintenanceHomeActivity) context;
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        initView(view);
//        initNoDataView(view);
//        initData();
//        initAdapter();
//        initEvent();
////        loadShopBrands();
////        analyzingStatus();
////        showNoDataLayout(false);
//    }
//
//    public static MaintenanceFragment newInstance() {
//
//        Bundle args = new Bundle();
//
//        MaintenanceFragment fragment = new MaintenanceFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    private void initView(View view) {
//        home_scroll_view = (ScrollView) view.findViewById(R.id.home_scroll_view);
//        maintenance_layout = (LinearLayoutCompat) view.findViewById(R.id.maintenance_layout);
//        autoDetailView = (AutoDetailView) view.findViewById(R.id.auto_data_card);
////        autoDetailView.setIsPerfect(true);
//        maintenanceProgramView = (RecommendMaintenanceProgramView) view.findViewById(R.id.smart_maintenance_program);
////        baseMaintenanceProgramView = (RecommendMaintenanceProgramView) view.findViewById(R.id.base_maintenance_program);
//        //基本套餐
//        basePackageList = (ListViewNoSlide) view.findViewById(R.id.base_maintenance_package);
//        otherPackageList = (ListViewNoSlide) view.findViewById(R.id.other_maintenance_package);
//        /*服务顾问列表*/
//        adviserListView = (ServiceAdviserListView) view.findViewById(R.id.service_advisor_list);
//        /*技师列表*/
//        technicianListView = (TechnicianListView) view.findViewById(R.id.technician_list);
//    }
//
//    private void initNoDataView(View view) {
//
//        complete = (Button) view.findViewById(R.id.complete);
//        noDataLayout = view.findViewById(R.id.no_data_layout);
////        commen_auto_info = (CommenAutoInfoHeaderView) noDataLayout.findViewById(R.id.commen_auto_info);
////        last_data_layout = commen_auto_info.getmMaintainInfoView();
//        last_data_layout.setVisibility(View.GONE);
////        license_city = commen_auto_info.getmLicenseCityView();
////        license_city.setMode(PlateNumberTextView.MODE_CITY);
////        license_number = commen_auto_info.getmLicenseNumView();
////        license_number.setMode(PlateNumberTextView.MODE_WORD);
////        mPlateNumberView = commen_auto_info.getmPlateNumView();
////        auto_VIN = commen_auto_info.getmVINView();
////        auto_distance = commen_auto_info.getmMileageView();
////        commen_auto_info.getmMileageInfoView().setVisibility(View.VISIBLE);
////        nameItem = (CommonEditInfoItemView) noDataLayout.findViewById(R.id.name_item);
////        nameItem.getmContentView().setSingleLine();
////        phoneItem = (CommonEditInfoItemView) noDataLayout.findViewById(R.id.phone_item);
////        phoneItem.getmContentView().setInputType(InputType.TYPE_CLASS_PHONE);
////        phoneItem.getmContentView()
////                .setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
////        your_name = (EditText) noDataLayout.findViewById(R.id.your_name);
////        your_phone = (EditText) noDataLayout.findViewById(R.id.your_phone);
////        auto_model = commen_auto_info.getmAutoTypeView();
////        last_maintenance_distance = commen_auto_info.getmMaintainView();
////        last_maintenance_time = commen_auto_info.getmQualityDateView();
//    }
//
//    private void initData() {
//        serviceAdvisers = new ArrayList<>();
//        techniciens = new ArrayList<>();
//        shopBrandGroups = new ArrayList<>();
//    }
//
//    private void initAdapter() {
//        adapter = new OtherMaintenancePackageListAdapter(otherListData, getActivity());
//        otherPackageList.setAdapter(adapter);
//        baseAdapter = new OtherMaintenancePackageListAdapter(baseListData, getActivity());
//        baseAdapter.type = OtherMaintenancePackageListAdapter.Type.BASE;
//        basePackageList.setAdapter(baseAdapter);
//    }
//
//    private void initEvent() {
////        auto_distance.setOnFocusChangeListener(new View.OnFocusChangeListener() {
////            @Override
////            public void onFocusChange(View v, boolean hasFocus) {
////                if (hasFocus)
////                    home_scroll_view.scrollBy(0, 200);
////            }
////        });
////        your_phone.setOnEditorActionListener(this);
////        phoneItem.getmContentView().setOnEditorActionListener(this);
//        maintenanceProgramView.setOnSeeDetailClickListener(this);
////        baseMaintenanceProgramView.setOnSeeDetailClickListener(this);
//        adapter.setOnPackageClickListener(this);
//        adapter.setOnMoreClickListener(this);
//        baseAdapter.setOnPackageClickListener(this);
//        baseAdapter.setOnMoreClickListener(this);
//        auto_model.setOnClickListener(this);
//        complete.setOnClickListener(this);
////        mPlateNumberView.setCheckListener(checkListener);
///*        license_number.addTextChangedListener(new TextCompleteListener() {
//            @Override
//            public void onCompleteText(Editable s) {
//                if (s.length() == 6)
//                    //车牌号填完了
//                    checkAuto();
//            }
//        });*/
//        activity.setOnShopInfoCallBack(this);
//    }
//
//    private CommenPlateNumberView.OnCheckListener checkListener = new CommenPlateNumberView.OnCheckListener() {
//        @Override
//        public void checkListener(String plateNumber) {
////            checkAuto();
//        }
//    };
//
//    /**
//     * 检索车辆
//     */
//    @Deprecated
//    private void checkAuto() {
////        MyAutoInfoHelper.getInstance(getActivity()).checkAuto(shopBrandGroups, mPlateNumberView.getPlateNumber(),
////                auto_VIN.getText().toString(), new LoadDataCallBack<MyAuto>() {
////                    @Override
////                    public void onDataNull(String message) {
//////                        updateNoDateLayout(null);
////                    }
////
////                    @Override
////                    public void onDataGot(MyAuto obj) {
////                        auto = obj;
////                        updateNoDateLayout(obj);
////                    }
////                });
//    }
//
//    /**
//     * 判断状态
//     */
//    private void analyzingStatus() {
//        if (UserInfoHelper.getInstance().isLogin(getActivity())) {
//            MyAutoInfoHelper.getInstance(getActivity()).getMatchAuto(shopBrandGroups,
//                    new LoadDataCallBack<MyAuto>() {
//                        @Override
//                        public void onDataNull(String message) {
//                            //没有匹配车辆
//                            DebugLog.i(TAG, "没有匹配车辆");
//                            showNoDataLayout(true);
//                        }
//
//                        @Override
//                        public void onDataGot(MyAuto obj) {
//                            auto = obj;
//                            //用户有符合的车辆显示首页
//                            autoDetailView.addData(auto);
//                            showNoDataLayout(false);
//                        }
//                    });
//        } else {
//            ArrayList<MyAuto> autoData = MyAutoInfoHelper.getInstance(getActivity()).getAutoDataLocal();
//            chooseAuto(autoData);
//        }
//    }
//
//    /**
//     * 筛选车辆
//     *
//     * @param autoData
//     */
//    private void chooseAuto(final ArrayList<MyAuto> autoData) {
//        if (autoData == null || autoData.size() == 0) {
//            showNoDataLayout(true);
//            DebugLog.i(TAG, "没有车辆！");
//        } else {
//            filterAuto(shopBrandGroups, autoData);
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        SelectAutoHelper.getInstance(getActivity()).destory();
//        HomeDataHelper.getInstance(getActivity()).destory();
//    }
//
//    /**
//     * 先获取店铺品牌
//     */
//    private void loadShopBrands(String shopID) {
//        SelectAutoHelper.getInstance(getActivity()).getBrandData(shopID,
//                new LoadDataCallBack<ArrayList<BrandGroup>>() {
//                    @Override
//                    public void onDataNull(String message) {
//                        DebugLog.i(TAG, "店铺品牌列表为空！");
////                        showNoDataLayout(true);
//                    }
//
//                    @Override
//                    public void onDataGot(ArrayList<BrandGroup> obj) {
//                        shopBrandGroups.clear();
//                        shopBrandGroups.addAll(obj);
//                        analyzingStatus();
//                    }
//                });
//    }
//
//    /**
//     * 筛选符合品牌的车辆
//     *
//     * @param obj
//     * @param autoData
//     */
//    private void filterAuto(ArrayList<BrandGroup> obj, ArrayList<MyAuto> autoData) {
//        MyAutoInfoHelper.getInstance(getActivity()).checkBrand(autoData, obj,
//                new LoadDataCallBack<MyAuto>() {
//                    @Override
//                    public void onDataNull(String message) {
////            ToastHelper.showRedToast(getActivity(), "没有找到匹配的车辆！");
//                        DebugLog.i(TAG, "没有找到匹配的车辆！");
//                        showNoDataLayout(true);
//                    }
//
//                    @Override
//                    public void onDataGot(MyAuto obj) {
//                        auto = obj;
//                        autoDetailView.addData(auto);
//                        showNoDataLayout(false);
//                        DebugLog.i(TAG, "找到匹配的车辆！");
//                    }
//                });
//    }
//
////    @Override
////    public void onActivityResult(int requestCode, int resultCode, Intent data) {
////        if (requestCode == AutoInfoContants.GET_AUTO_DATA) {
////            if (resultCode == Activity.RESULT_OK)
////                if (data != null) {
////                    auto = data.getParcelableExtra("myAuto");
////                    autoDetailView.addData(auto);
////                }
////        }
////    }
//
//    /**
//     * 提交车辆信息
//     */
//    private void commitAutoData() {
//        //隐藏键盘
//        if (getActivity().getCurrentFocus() != null) {
//            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
//                    .hideSoftInputFromWindow(getActivity().getCurrentFocus()
//                                    .getWindowToken(),
//                            InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//        auto.plateNumber = mPlateNumberView.getPlateNumber();
////                completePlateNumber(license_city.getText().toString(),
////                license_number.getText().toString());
////        auto.VIN = auto_VIN.getText().toString();
////        auto.ownName = nameItem.getmContentView().getText().toString().trim();
////        auto.ownPhone = phoneItem.getmContentView().getText().toString().trim();
////        auto.ownName = your_name.getText().toString();
////        auto.ownPhone = your_phone.getText().toString();
////        auto.drivingDistance = auto_distance.getText().toString();
//        //车牌号为空
///*        if (license_number.getText().toString().isEmpty()
//                || license_city.getText().toString().isEmpty()) {
//            ToastHelper.showRedToast(getActivity(), "请填写车牌号");
//            return;
//        }*/
//        if (mPlateNumberView.getPlateNumber().length() < 7) {
//            ToastHelper.showRedToast(getActivity(), "请填写车牌号");
//            return;
//        }
//        //车辆信息为空
//        if (TextUtils.isEmpty(auto.brandID) || TextUtils.isEmpty(auto.seriesID) ||
//                TextUtils.isEmpty(auto.autoModelID)) {
//            ToastHelper.showRedToast(getActivity(), "请选择车辆信息");
//            return;
//        }
//        //行驶里程没有填写
//        if (auto.drivingDistance.isEmpty()) {
//            ToastHelper.showRedToast(getActivity(), "请填写行驶里程");
//            return;
//        }
//        if (!TextUtils.isEmpty(auto.brandID) && !TextUtils.isEmpty(auto.seriesID) &&
//                !TextUtils.isEmpty(auto.autoModelID) && !TextUtils.isEmpty(auto.plateNumber) &&
//                !TextUtils.isEmpty(auto.drivingDistance)
//                ) {
//            //车辆的信息和本店的品牌不符合
//            if (!MyAutoInfoHelper.getInstance(getActivity()).checkBrand(auto, shopBrandGroups)) {
//                ToastHelper.showRedToast(getActivity(),
//                        "本店暂时只为" + MyAutoInfoHelper.getInstance(getActivity())
//                                .getBrands(shopBrandGroups) + "品牌车辆提供维修保养服务");
//                return;
//            }
//            MyAutoInfoHelper.getInstance(getActivity()).addAutoLocal(auto);
//            if (UserInfoHelper.getInstance().isLogin(getActivity()))
//                MyAutoInfoHelper.getInstance(getActivity()).uploadAutoData(auto, new LoadDataCallBack<String>() {
//                    @Override
//                    public void onDataNull(String message) {
//                        ToastHelper.showRedToast(getActivity(), message);
//                    }
//
//                    @Override
//                    public void onDataGot(String obj) {
//                        //信息修改好了
//                        ToastHelper.showRedToast(getActivity(), "车辆信息添加成功");
////                    showNoDataLayout(false);
////                    autoDetailView.addData(auto);
//                    }
//                });
//            showNoDataLayout(false);
//            autoDetailView.addData(auto);
//        } else {
//            ToastHelper.showRedToast(getActivity(), "请填写车辆必要信息");
//        }
//    }
//
//
//    /**
//     * 获取首页数据
//     */
//    private void loadData(String autoModel, String autoModelID, String drivingDistance, String plateNumber) {
////        auto.brand = onMaintenanceListener.getBrand();
////        auto.brandID = onMaintenanceListener.getShopBrandID();
//        auto.brand = shopInfo.brand;
//        auto.brandID = shopInfo.brandID;
//        HomeDataHelper.getInstance(getActivity()).getHomeData(shopInfo.shopID, autoModel, autoModelID,
//                drivingDistance, plateNumber,
//                new LoadDataCallBack<MaintenanceHome>() {
//                    @Override
//                    public void onDataNull(String message) {
////                        showNoDataLayout(true);
//                    }
//
//                    @Override
//                    public void onDataGot(MaintenanceHome obj) {
//                        maintenanceHome = obj;
////                        if (maintenanceHome.myAuto != null &&
////                                !TextUtils.isEmpty(maintenanceHome.myAuto.brandID)) {
////                            auto = maintenanceHome.myAuto;
////                            autoDetailView.addData(auto);
////                        }
//                        recommendProgram = maintenanceHome.recommendProgram;
//                        //基本套餐
//                        if (maintenanceHome.baseMaintenancePackage != null &&
//                                maintenanceHome.baseMaintenancePackage.size() > 0) {
//                            baseMaintenancePackage = maintenanceHome.baseMaintenancePackage.get(0);
//                            baseListData.clear();
//                            baseListData.addAll(maintenanceHome.baseMaintenancePackage);
//                            basePackageList.setVisibility(View.VISIBLE);
//                        }
//
//                        maintenanceProgramView.addData(recommendProgram, maintenanceHome.maintenanceManual);
////                        baseMaintenanceProgramView.addData(baseMaintenancePackage);
//                        techniciens.clear();
//                        serviceAdvisers.clear();
//                        if (maintenanceHome.mechanic != null && maintenanceHome.mechanic.size() > 0) {
//                            techniciens.addAll(maintenanceHome.mechanic);
//                            technicianListView.addData(techniciens);
//                            technicianListView.setVisibility(View.VISIBLE);
//                        }
//
//                        if (maintenanceHome.serviceAdviser != null && maintenanceHome.serviceAdviser.size() > 0) {
//                            serviceAdvisers.addAll(maintenanceHome.serviceAdviser);
//                            adviserListView.addData(serviceAdvisers);
//                            adviserListView.setVisibility(View.VISIBLE);
//                        }
//                        if (maintenanceHome.maintenancePackage != null && maintenanceHome.maintenancePackage.size() > 0) {
//                            otherListData.clear();
//                            otherListData.addAll(maintenanceHome.maintenancePackage);
//                            otherPackageList.setVisibility(View.VISIBLE);
//                        }
//                        adapter.notifyDataSetChanged();
//                        baseAdapter.notifyDataSetChanged();
//                    }
//                });
//    }
//
//
//    /**
//     * 检索出车辆信息显示在上面
//     *
//     * @param auto
//     */
//    private void updateNoDateLayout(MyAuto auto) {
//        if (auto == null) {
//            auto_VIN.setText("");
//            auto_distance.setText("");
//            nameItem.getmContentView().setText("");
//            auto_model.setText("");
//            phoneItem.getmContentView().setText("");
//            last_data_layout.setVisibility(View.GONE);
//        } else {
//            auto_VIN.setText(auto.VIN);
//            auto_distance.setText(auto.drivingDistance + "");
////        your_name.setText(auto.ownName);
//            nameItem.getmContentView().setText(auto.ownName);
//            auto_model.setText(auto.brand + auto.series + auto.autoModel);
////        your_phone.setText(auto.ownPhone);
//            phoneItem.getmContentView().setText(auto.ownPhone);
//            boolean empty = TextUtils.isEmpty(auto.lastMaintenanceDate);
//            if (!empty || !TextUtils.isEmpty(auto.lastMaintenanceDistance + "")/*auto.lastMaintenanceDistance != -1*/) {
//                //有数据显示出来
//                last_data_layout.setVisibility(View.VISIBLE);
//            } else last_data_layout.setVisibility(View.GONE);
//            last_maintenance_distance.setText(auto.lastMaintenanceDistance + "公里");
//            last_maintenance_time.setText(auto.lastMaintenanceDate);
//            //没有数据就隐藏
//            last_maintenance_time.setVisibility(empty ? View.GONE : View.VISIBLE);
//        }
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        int i = v.getId();
//        if (i == R.id.commen_auto_info_type) {
//            //打开侧滑
//            activity.openSlidMenu();
//        } else if (i == R.id.complete)
//            //提交车辆信息
//            commitAutoData();
//    }
//
//
//    /**
//     * 显示首页或者添加车辆信息
//     *
//     * @param show
//     */
//    private void showNoDataLayout(boolean show) {
//        if (show) {
////            ToastHelper.showRedToast(getActivity(), "请输入您的品牌为\"" + auto.brand + "\"的车辆信息");
//            noDataLayout.setVisibility(View.VISIBLE);
//            maintenance_layout.setVisibility(View.GONE);
//        } else {
//            noDataLayout.setVisibility(View.GONE);
//            maintenance_layout.setVisibility(View.VISIBLE);
//            loadData(auto.autoModel, auto.autoModelID, auto.drivingDistance + "", auto.plateNumber);
//        }
//    }
//
//    /**
//     * 查看推荐套餐
//     *
//     * @param v
//     */
//    @Override
//    public void onSeeDetailClick(View v) {
//        int id = v.getId();
//        if (id == R.id.smart_maintenance_program) {
//            ActivitySwitcherMaintenance.toSmartMaintenance(getActivity(), auto, "1",
//                    shopInfo.shopID, recommendProgram.packageID);
//        }
////        else if (id == R.id.base_maintenance_program) {
////            ActivitySwitcherMaintenance.toSmartMaintenance(getActivity(), auto, "3",
////                    shopInfo.shopID, baseMaintenancePackage.packageID);
////        }
//    }
//
//    /**
//     * 查看套餐列表中的套餐
//     *
//     * @param position
//     */
//    @Override
//    public void onPackageClick(int position, OtherMaintenancePackageListAdapter.Type type) {
//        if (type == OtherMaintenancePackageListAdapter.Type.OTHER)
//            ActivitySwitcherMaintenance.toSmartMaintenance(getActivity(), auto, "2",
//                    shopInfo.shopID, otherListData.get(position).packageID);
//        else ActivitySwitcherMaintenance.toSmartMaintenance(getActivity(), auto, "3",
//                shopInfo.shopID, baseListData.get(position).packageID);
//    }
//
//    /**
//     * 更新选择车型
//     *
//     * @param series
//     * @param seriesID
//     * @param model
//     * @param modelID
//     */
//    public void updateModel(String brand, String brandID, String series, String seriesID, String model, String modelID) {
//        auto.brand = brand;
//        auto.brandID = brandID;
//        auto.series = series;
//        auto.seriesID = seriesID;
//        auto.autoModel = model;
//        auto.autoModelID = modelID;
//        auto_model.setText(brand + series + model);
//    }
//
//
//    @Override
//    public void onMoreClick(OtherMaintenancePackageListAdapter.Type type) {
//        ActivitySwitcherMaintenance.toPackageList(getActivity(), shopInfo.shopID,
//                type, auto);
//    }
//
//    @Override
//    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        if (actionId == EditorInfo.IME_ACTION_DONE) {
//            commitAutoData();
//            return true;
//        }
//        return false;
//    }
//
//
//    @Override
//    public void onShopInfoLoad(ShopInfo shopInfo) {
//        this.shopInfo = shopInfo;
//        loadShopBrands(shopInfo.shopID);
//
//        autoDetailView.shopID = shopInfo.shopID;
//    }
//
//    /**
//     * 重新选择了车型
//     *
//     * @param auto
//     */
//    public void updateAuto(MyAuto auto) {
//        this.auto = auto;
//        loadData(auto.autoModel, auto.autoModelID, auto.drivingDistance, auto.plateNumber);
////        autoDetailView.addData(auto);
//        //重新请求套餐
//
//    }
//}
