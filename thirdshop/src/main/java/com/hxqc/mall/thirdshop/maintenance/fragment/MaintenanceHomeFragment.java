package com.hxqc.mall.thirdshop.maintenance.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoHelper;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.controler.AutoTypeControl;
import com.hxqc.mall.auto.controler.MyAutoInfoHelper;
import com.hxqc.mall.auto.model.BrandGroup;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.adapter.OtherMaintenancePackageListAdapter;
import com.hxqc.mall.thirdshop.maintenance.control.FourSAndQuickHelper;
import com.hxqc.mall.thirdshop.maintenance.control.HomeDataHelper;
import com.hxqc.mall.thirdshop.maintenance.model.Mechanic;
import com.hxqc.mall.thirdshop.maintenance.model.ServiceAdviser;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceHome;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemGroup;
import com.hxqc.mall.thirdshop.maintenance.model.maintenance.MaintenanceItemN;
import com.hxqc.mall.thirdshop.maintenance.utils.ActivitySwitcherMaintenance;
import com.hxqc.mall.thirdshop.maintenance.views.AutoDetailView;
import com.hxqc.mall.thirdshop.maintenance.views.PlatformRecommendedList;
import com.hxqc.mall.thirdshop.maintenance.views.RecommendMaintenanceProgramView;
import com.hxqc.mall.thirdshop.maintenance.views.ServiceAdviserListView;
import com.hxqc.mall.thirdshop.maintenance.views.TechnicianListView;
import com.hxqc.mall.thirdshop.model.ShopInfo;

import java.util.ArrayList;


/**
 * Author:李烽
 * Date:2016-02-16
 * FIXME
 * Todo 维修保养
 */
public class MaintenanceHomeFragment extends MaintenanceBaseFragment implements
        RecommendMaintenanceProgramView.OnSeeDetailClickListener,
        OtherMaintenancePackageListAdapter.OnPackageClickListener,
        OtherMaintenancePackageListAdapter.OnMoreClickListener {
    private static final String TAG = "MaintenanceFragment";
    //    private ListViewNoSlide otherPackageList;

    //    private ListViewNoSlide basePackageList;
//    private NewRecommendMaintenanceProgramView newMaintenanceProgramsView;
//    private RecommendMaintenanceProgramView maintenanceProgramView;

    private AutoDetailView autoDetailView;
    private PlatformRecommendedList platformRecommendedList;//平台推荐项目
    private ServiceAdviserListView adviserListView;
    private TechnicianListView technicianListView;

    private LinearLayoutCompat maintenance_layout;
    private MaintenanceHome maintenanceHome;//首页数据
    //    private MaintenancePackage recommendProgram;
//    private MaintenancePackage baseMaintenancePackage;
//    private ArrayList<MaintenancePackage> baseListData = new ArrayList<>();
//    private ArrayList<MaintenancePackage> otherListData = new ArrayList<>();
//    private OtherMaintenancePackageListAdapter adapter;
//    private OtherMaintenancePackageListAdapter baseAdapter;


    private ArrayList<ServiceAdviser> serviceAdvisers;
    private ArrayList<Mechanic> techniciens;


    private MyAuto auto = new MyAuto();//我的车辆
    private String shopID = "";
    private ShopInfo shopInfo = new ShopInfo();

    private static final String HintKey = "Hint";
    private static final int HintMessage = 1;
    private static final int DelayedSearchTime = 500;//延迟提示时间
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == HintMessage) {
                String keyValue = msg.getData().getString(HintKey);
                auto.drivingDistance = keyValue;
                //车辆里程数的修改存储
                if (UserInfoHelper.getInstance().isLogin(getContext()))
                    AutoHelper.getInstance().addOrEditAutoLocaltoLinkedList(getContext(), auto, AutoHelper.SWITCH_AUTO);
                else
                    AutoHelper.getInstance().addOrEditAutoLocaltoLinkedList(getContext(), auto, AutoHelper.AUTO_LOCAL_INFO);

                FourSAndQuickHelper.getInstance().setMyAuto(auto);//
                FourSAndQuickHelper.getInstance().setFlag("3");//
                FourSAndQuickHelper.getInstance().setShopID(shopID);//
                FourSAndQuickHelper.getInstance().getItemsNFor4S(getActivity(), new FourSAndQuickHelper.ItemsNHandleFor4S() {

                    @Override
                    public void onSuccess(ArrayList<MaintenanceItemGroup> recommentItemsFor4S, ArrayList<MaintenanceItemGroup> optionalItemsFor4S) {

                    }

                    @Override
                    public void onSuccessFor4SHome(ArrayList<MaintenanceItemGroup> recommentItemsFor4S, int optionSize) {
                        platformRecommendedList.shopInfo = shopInfo;
                        platformRecommendedList.addData(recommentItemsFor4S, optionSize);
                        autoDetailView.hideFocus();
                    }

                    @Override
                    public void onFailure() {
                        platformRecommendedList.setVisibility(View.GONE);
                    }
                });
            }
            return false;
        }
    });

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maintenance_home, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
        initAdapter();
        initEvent();
    }

    public static MaintenanceHomeFragment newInstance() {
        Bundle args = new Bundle();
        MaintenanceHomeFragment fragment = new MaintenanceHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 更新数据
     *
     * @param auto
     */
    public void notifyData(MyAuto auto, String shopID, ArrayList<BrandGroup> brandGroups) {
        this.auto = auto;
        autoDetailView.addData(auto);
        autoDetailView.shopID = shopID;
        autoDetailView.mBrands = AutoHelper.getInstance().getAllBrands(brandGroups);
        this.shopID = shopID;
        loadData(shopID);
    }

    public void setShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;

    }

    private void initView(View view) {
        maintenance_layout = (LinearLayoutCompat) view.findViewById(R.id.maintenance_layout);
        autoDetailView = (AutoDetailView) view.findViewById(R.id.auto_data_card);
//        maintenanceProgramView = (RecommendMaintenanceProgramView) view.findViewById(R.id.smart_maintenance_program);
//        newMaintenanceProgramsView = (NewRecommendMaintenanceProgramView) view.findViewById(R.id.recommend_programs);

        //基本套餐
//        basePackageList = (ListViewNoSlide) view.findViewById(R.id.base_maintenance_package);
//        otherPackageList = (ListViewNoSlide) view.findViewById(R.id.other_maintenance_package);
        //平台推荐项目
        platformRecommendedList = (PlatformRecommendedList) view.findViewById(R.id.platform_recommended_list);
        platformRecommendedList.shopInfo = shopInfo;
        /*服务顾问列表*/
        adviserListView = (ServiceAdviserListView) view.findViewById(R.id.service_advisor_list);
        /*技师列表*/
        technicianListView = (TechnicianListView) view.findViewById(R.id.technician_list);
    }


    private void initData() {
        serviceAdvisers = new ArrayList<>();
        techniciens = new ArrayList<>();
    }

    private void initAdapter() {
//        adapter = new OtherMaintenancePackageListAdapter(otherListData, getActivity());
//        otherPackageList.setAdapter(adapter);
//        baseAdapter = new OtherMaintenancePackageListAdapter(baseListData, getActivity());
//        baseAdapter.type = OtherMaintenancePackageListAdapter.Type.BASE;
//        basePackageList.setAdapter(baseAdapter);
    }

    private void initEvent() {
        autoDetailView.setdrivenDistanceChangeListener(drivenDistanceChangeCallBack);
//        newMaintenanceProgramsView.setOnSeeDetailClickListener
//                (new NewRecommendMaintenanceProgramView.OnSeeDetailClickListener() {
//                    @Override
//                    public void onSeeDetailClick(View view) {
//                        ActivitySwitcherMaintenance.toFourSAndQuickShopQuote(getActivity(), shopInfo);
//                    }
//                });

//        maintenanceProgramView.setOnSeeDetailClickListener(this);
//        adapter.setOnPackageClickListener(this);
//        adapter.setOnMoreClickListener(this);
//        baseAdapter.setOnPackageClickListener(this);
//        baseAdapter.setOnMoreClickListener(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        AutoTypeControl.getInstance().killInstance();
        HomeDataHelper.getInstance(getActivity()).destory();
        MyAutoInfoHelper.getInstance(getActivity()).destory();
        AutoHelper.getInstance().killInstance(getActivity());
        AutoInfoContants.clearTime();
//        FourSAndQuickHelper.getInstance().destroy();
    }

    /**
     * 获取首页数据
     */
    private void loadData(final String shopID) {
        autoDetailView.addData(auto);
        //服务顾问
//        HomeDataHelper.getInstance(getActivity()).getServiceAdvisers(shopID,
//                new LoadDataCallBack<ArrayList<ServiceAdviser>>() {
//                    @Override
//                    public void onDataNull(String message) {
//                        adviserListView.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onDataGot(ArrayList<ServiceAdviser> obj) {
////                        adviserListView.setVisibility(View.VISIBLE);
//                        adviserListView.addData(obj);
//                    }
//                });
        //技师
//        HomeDataHelper.getInstance(getActivity()).getMechanic(shopID,
//                new LoadDataCallBack<ArrayList<Mechanic>>() {
//                    @Override
//                    public void onDataNull(String message) {
//                        technicianListView.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onDataGot(ArrayList<Mechanic> obj) {
////                        technicianListView.setVisibility(View.VISIBLE);
//                        technicianListView.addData(obj);
//                    }
//                });

        //项目推荐
        FourSAndQuickHelper.getInstance().setMyAuto(auto);//
        FourSAndQuickHelper.getInstance().setFlag("3");//
        FourSAndQuickHelper.getInstance().setShopID(shopID);//
        FourSAndQuickHelper.getInstance().getItemsNFor4S(getActivity(), new FourSAndQuickHelper.ItemsNHandleFor4S() {

            @Override
            public void onSuccess(ArrayList<MaintenanceItemGroup> recommentItemsFor4S, ArrayList<MaintenanceItemGroup> optionalItemsFor4S) {

            }

            @Override
            public void onSuccessFor4SHome(ArrayList<MaintenanceItemGroup> recommentItemsFor4S, int optionSize) {
                platformRecommendedList.shopInfo = shopInfo;
                platformRecommendedList.addData(recommentItemsFor4S, optionSize);
            }


            @Override
            public void onFailure() {
                platformRecommendedList.setVisibility(View.GONE);
            }
        });


//        HomeDataHelper.getInstance(getActivity()).getHomeData(shopID, myAuto.autoModel, myAuto.autoModelID,
//                myAuto.drivingDistance, myAuto.plateNumber,
//                new LoadDataCallBack<MaintenanceHome>() {
//                    @Override
//                    public void onDataNull(String message) {
////                        showNoDataLayout(true);
//                    }
//
//                    @Override
//                    public void onDataGot(MaintenanceHome obj) {
//
//                        maintenanceHome = obj;
//                        if (maintenanceHome.myAuto != null &&
//                                !TextUtils.isEmpty(maintenanceHome.myAuto.brandID)) {
//                            auto = maintenanceHome.myAuto;
//                            autoDetailView.addData(auto);
//                        }
//
////********************************************推荐保养方案数据添加*************************************/
//                        FourSAndQuickHelper.getInstance().setMyAuto(auto);//
//                        FourSAndQuickHelper.getInstance().setFlag("3");//
//                        FourSAndQuickHelper.getInstance().setShopID(shopID);//
//                        FourSAndQuickHelper.getInstance().getItemsN(getActivity(), new FourSAndQuickHelper.ItemsNHandle() {
//                            @Override
//                            public void onSuccess(ArrayList<MaintenanceItemN> maintenanceShowItems) {
//
//                            }
//
//                            @Override
//                            public void onSuccessFor4S(ArrayList<MaintenanceItemN> maintenanceItems) {
////                                newMaintenanceProgramsView.addData(maintenanceItems, maintenanceHome.maintenanceManual);
//                            }
//
//                            @Override
//                            public void onFailure() {
//
//                            }
//                        });
////**************************************************************************************************/
//
////                        recommendProgram = maintenanceHome.recommendProgram;
//                        //基本套餐
//                        if (maintenanceHome.baseMaintenancePackage != null && maintenanceHome.baseMaintenancePackage.size() > 0) {
////                            baseMaintenancePackage = maintenanceHome.baseMaintenancePackage.get(0);
////                            baseListData.clear();
////                            baseListData.addAll(maintenanceHome.baseMaintenancePackage);
////                            basePackageList.setVisibility(View.VISIBLE);
//                        }
//
////                        maintenanceProgramView.addData(recommendProgram, maintenanceHome.maintenanceManual);
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
////                            otherListData.clear();
////                            otherListData.addAll(maintenanceHome.maintenancePackage);
////                            otherPackageList.setVisibility(View.VISIBLE);
//                        }
////                        adapter.notifyDataSetChanged();
////                        baseAdapter.notifyDataSetChanged();
//                    }
//                });
    }

    /**
     * 查看推荐套餐
     *
     * @param v
     */
    @Override
    public void onSeeDetailClick(View v) {
        int id = v.getId();
//        if (id == R.id.smart_maintenance_program) {
//            ActivitySwitcherMaintenance.toSmartMaintenance(getActivity(), auto, "1",
//                    shopID, recommendProgram.packageID);
//        }
//        else if (id == R.id.base_maintenance_program) {
//            ActivitySwitcherMaintenance.toSmartMaintenance(getActivity(), auto, "3",
//                    shopInfo.shopID, baseMaintenancePackage.packageID);
//        }
    }

    /**
     * 查看套餐列表中的套餐
     *
     * @param position
     */
    @Override
    public void onPackageClick(int position, OtherMaintenancePackageListAdapter.Type type) {
//        if (type == OtherMaintenancePackageListAdapter.Type.OTHER)
//            ActivitySwitcherMaintenance.toSmartMaintenance(getActivity(), auto, "2",
//                    shopID, otherListData.get(position).packageID);
//        else ActivitySwitcherMaintenance.toSmartMaintenance(getActivity(), auto, "3",
//                shopID, baseListData.get(position).packageID);
    }


    @Override
    public void onMoreClick(OtherMaintenancePackageListAdapter.Type type) {
        ActivitySwitcherMaintenance.toPackageList(getActivity(), shopID,
                type, auto);
    }

    /**
     * 重新选择了车型
     *
     * @param auto
     */
    public void updateAuto(MyAuto auto) {
        this.auto = auto;
        loadData(shopID);
        autoDetailView.addData(auto);
        //重新请求套餐
    }

    private AutoDetailView.DrivenDistanceChangeCallbck drivenDistanceChangeCallBack = new AutoDetailView.DrivenDistanceChangeCallbck() {
        @Override
        public void drivenDistanceChange(Editable s) {
            handler.removeMessages(HintMessage);
            Message message = new Message();
            message.what = HintMessage;
            message.getData().putString(HintKey, s.toString());
            handler.sendMessageDelayed(message, DelayedSearchTime);
        }
    };
}
