package com.hxqc.mall.thirdshop.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.model.advisory.Advisory;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.util.MapUtils;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.maintenance.util.ActivitySwitchMaintenance;
import com.hxqc.mall.thirdshop.maintenance.views.AdvisoryListView;
import com.hxqc.mall.thirdshop.model.AutoBaseInfoThirdShop;
import com.hxqc.mall.thirdshop.model.GoldenSeller;
import com.hxqc.mall.thirdshop.model.ModelsQuote;
import com.hxqc.mall.thirdshop.model.ShopInfo;
import com.hxqc.mall.thirdshop.model.ThirdPartShop;
import com.hxqc.mall.thirdshop.model.promotion.SalesPModel;
import com.hxqc.mall.thirdshop.utils.AMapPicUrl;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.views.GoldenSellersView;
import com.hxqc.mall.thirdshop.views.MainAutoView;
import com.hxqc.mall.thirdshop.views.MainSaleGridView;
import com.hxqc.mall.thirdshop.views.PromotionsView;
import com.hxqc.mall.thirdshop.views.ShopAddressView;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

/**
 * Author:李烽
 * Date:2015-11-30
 * FIXME
 * Todo 店铺首页
 */
public class ShopHomePageFragment extends FunctionFragment {
    private static final String TAG = "ShopHomePageFragment";
    /*views*/
    private ShopAddressView shopAddressView;//底部地图
    private MainAutoView mainAutoView;//主推车型
    private MainSaleGridView mainSaleGridView;//主营系列
    private GoldenSellersView goldenSellersView;//金牌顾问
    private PromotionsView promotionsView;//最近促销
    //    private RequestFailView mRequestFailView;//数据加载错误的view
    /*监听器*/
    private MainAutoView.OnClickListener mainAutoClickListener;//主推车型点击监听
    //    private MainSaleGridView.OnItemClickListener mainSaleOnItemClickListener;//主营系列点击监听
    private GoldenSellersView.OnItemClickListener goldenItemClickListener;//金牌顾问点击监听
    private PromotionsView.OnClickListener proClickListener;//最近促销点击监听
    private ShopAddressView.OnClickListener onClickListener;//地图点击监听
    //    private View.OnClickListener onClickListener;
    /*数据*/
    private ThirdPartShop shopData = null;//店铺信息
    private ArrayList<SalesPModel> promotionData;//最近促销数据
    private ArrayList<GoldenSeller> goldenSellersData;//金牌顾问数据
    private ArrayList<ModelsQuote> mainSaleGridData;//主营系列数据
    private ArrayList<AutoBaseInfoThirdShop> mainAutoData;//主推车型数据
    private String locationData;//当前位置数据（维度|经度）

    private BackActivity activity;
    private AMapPicUrl aMapPicUrl;
    private AdvisoryListView advisoryListView;

    ThirdPartShopClient client;
    private View.OnClickListener alOnclickListener;

//    private ArrayList<ModelsQuote.Series> mSeries;

//    private CallBar callBar;
//    private LinearLayout layout;

    /**
     * 设置店铺信息
     *
     * @param shopData
     */
    public void setShopData(ThirdPartShop shopData) {
        this.shopData = shopData;
        initData();
//        mSeries = getSeriesData();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (BackActivity) activity;
        client = new ThirdPartShopClient();
        aMapPicUrl = new AMapPicUrl(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.t_fragment_shop_home_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initListener();
        setListener();
//        initData();
    }

    /**
     * 初始化控件
     *
     * @param view
     */
    private void initView(View view) {
        shopAddressView = (ShopAddressView) view.findViewById(R.id.shop_address_view);
        mainAutoView = (MainAutoView) view.findViewById(R.id.main_auto_view);
        mainSaleGridView = (MainSaleGridView) view.findViewById(R.id.main_sale_grid_view);
        goldenSellersView = (GoldenSellersView) view.findViewById(R.id.golden_sellers_view);
        promotionsView = (PromotionsView) view.findViewById(R.id.promotions_view);
        advisoryListView = (AdvisoryListView) view.findViewById(R.id.advisory_answer_view);

    }

    private ArrayList<Advisory> advisories = new ArrayList<>();

    /**
     * 初始化数据 添加进界面
     */
    private void initData() {
        promotionData = getPromotionData();
        goldenSellersData = getGoldenSellersData();
        mainSaleGridData = getMainSaleGridData();
        mainAutoData = getMainAutoData();
        locationData = getLocationData();
        client.getConsultList(1, 15, shopData.getShopInfo().shopID,
                new LoadingAnimResponseHandler(getActivity()) {


                    @Override
                    public void onSuccess(String response) {
                        ArrayList<Advisory> advisories
                                = JSONUtils.fromJson(response, new TypeToken<ArrayList<Advisory>>() {
                        });
                        ShopHomePageFragment.this.advisories.addAll(advisories);
                        advisoryListView.addData(advisories);
                    }
                });

        promotionsView.addData(promotionData);
        goldenSellersView.addData(goldenSellersData);
        mainSaleGridView.addData(mainSaleGridData);
        mainAutoView.addData(mainAutoData);
        shopAddressView.addData(locationData);


    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        //***************************主推车辆*************************/
        mainAutoClickListener = new MainAutoView.OnClickListener() {
            @Override
            public void onClickTestDrive(View view, int position) {
                //主推车辆试驾
                DebugLog.d(TAG, "testD" + position);
                ActivitySwitcherThirdPartShop.toTestDrive(activity,
                        shopData.getShopInfo().shopID,
                        mainAutoData.get(position).itemID,
                        mainAutoData.get(position).itemName,
                        shopData.getShopInfo().shopTel,
                        true, null);
            }

            @Override
            public void onClickInquiry(View view, int position) {
                //主推车辆询价
                DebugLog.d(TAG, shopData.getShopInfo().shopID);
                ActivitySwitcherThirdPartShop.toAskLeastPrice(activity,
                        shopData.getShopInfo().shopID,
                        mainAutoData.get(position).itemID,
                        mainAutoData.get(position).itemName,
                        shopData.getShopInfo().shopTel,
                        true, null);
            }

            @Override
            public void onClickCheckAll(View view) {
                //查看所有主推车辆
                DebugLog.d(TAG, "toAutos");
//                activity.toModelsQuote();
                ActivitySwitcherThirdPartShop.toModelsOffer
                        (shopData.getShopInfo().shopID, getActivity());
            }

            @Override
            public void onClickItem(View view, int position) {
                //查看主推车辆
                DebugLog.d(TAG, "toAutoDetail" + position);
                ActivitySwitcherThirdPartShop.toCarDetail(mainAutoData.get(position).itemID,
                        shopData.getShopInfo().shopID, shopData.getShopInfo().shopTitle, activity);
            }
        };
//
//        mainSaleOnItemClickListener = new MainSaleGridView.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                //主营系列单项
//                Log.d(TAG, "mainSale" + position);
//                activity.toModelsQuote(mSeries.get(position).seriesID);
//            }
//        };
        //***************************金牌顾问*************************/
        goldenItemClickListener = new GoldenSellersView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //给金牌顾问致电
                DebugLog.d(TAG, "GoldenSeller" + position);
                String sellerTel = goldenSellersData.get(position).sellerTel;
                if (!TextUtils.isEmpty(sellerTel))
                    showCallDialog(sellerTel);
//                    new CallPhoneDialog(activity, sellerTel
//                    ).show();
            }
        };
        //***************************促销信息*************************/
        proClickListener = new PromotionsView.OnClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //查看单项热销商品
                DebugLog.d(TAG, "toProItem" + position);
                if (promotionData.get(position).type == SalesPModel.CAR_P) {
                    ActivitySwitcherThirdPartShop.toSalesItemDetail(promotionData.get(position).promotionID, getActivity());
                } else if (promotionData.get(position).type == SalesPModel.MAINTENANCE_P) {
                    ActivitySwitchMaintenance.toMaintenancePromotion(promotionData.get(position).promotionID, getActivity());
                }

//                if (promotionData.get(position).newsType == 10)
//                    ActivitySwitcherThirdPartShop
//                            .toSalesItemDetail(promotionData.get(position).promotionID, getContext());
//                else
//                    ActivitySwitcherThirdPartShop
//                            .toSalesNewsDetail(promotionData.get(position).promotionID, getContext());
            }

            @Override
            public void onClickCheckAll(View view) {
                //查看所有热销商品
                DebugLog.d(TAG, "toPromotion");
//                activity.toSalesP();
                ActivitySwitcherThirdPartShop.toShopPromotionList
                        (shopData.getShopInfo().shopID, getActivity());
            }
        };

        //***************************店铺地址*************************/
        onClickListener = new ShopAddressView.OnClickListener() {
            @Override
            public void onLocaClick() {
                toMap(1);
                DebugLog.i("test_home_map", "onLocaClick");
            }

            @Override
            public void onNaviClick() {
                toMap(2);
                DebugLog.i("test_home_map", "onNaviClick");
            }

            @Override
            public void onMapClick() {
                toMap(0);
                DebugLog.i("test_home_map", "onMapClick");
            }

            private void toMap(int requestType) {
                if (null != shopData)
                    if (null != shopData.getShopInfo())
                        if (null != shopData.getShopInfo().getShopLocation()) {
                            PickupPointT mShopLocation = shopData.getShopInfo().getShopLocation();
                            ActivitySwitcherThirdPartShop.toAMapNvai(activity, requestType, mShopLocation);
                        }
            }

        };

        //***************************咨询列表*************************/
        alOnclickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherThirdPartShop.toAdvisoryList(getActivity(), advisories);
            }
        };

    }

    /**
     * 打电话的应用
     *
     * @param sellerTel
     */
    private void showCallDialog(final String sellerTel) {
        new AlertDialog.Builder(getActivity(), R.style.MaterialDialog)
                .setTitle(getActivity().getString(R.string.call_phone))
                .setMessage(sellerTel)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                OtherUtil.callPhone(getActivity(), sellerTel);
                            }
                        }
                )
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                )
                .create().show();
    }

    /**
     * 设置监听
     */
    private void setListener() {
        mainAutoView.setOnClickListener(mainAutoClickListener);
//        mainSaleGridView.setOnItemClickListener(mainSaleOnItemClickListener);
        promotionsView.setOnClickListener(proClickListener);
        shopAddressView.setOnClickListener(onClickListener);
        goldenSellersView.setOnItemClickListener(goldenItemClickListener);
        advisoryListView.onClickListener = alOnclickListener;
    }

    /**
     * @return 主营车系数据
     */
    private ArrayList<ModelsQuote> getMainSaleGridData() {
        ArrayList<ModelsQuote> data = new ArrayList<>();
        //获取数据
        if (null != shopData) {
            ArrayList<ModelsQuote> series = shopData.getSeries();
            data.clear();
            data.addAll(series);
        }


        return data;
    }

    /**
     * @return 主推车型数据
     */
    private ArrayList<AutoBaseInfoThirdShop> getMainAutoData() {
        ArrayList<AutoBaseInfoThirdShop> data = new ArrayList<>();
        //获取数据
        if (null != shopData) {
            ArrayList<AutoBaseInfoThirdShop> items = shopData.getItems();
            data.clear();
            data.addAll(items);
        }
        return data;
    }

    /**
     * @return 最近促销数据
     */
    private ArrayList<SalesPModel> getPromotionData() {
        ArrayList<SalesPModel> data = new ArrayList<>();

        //获取数据
        if (null != shopData) {
            ArrayList<SalesPModel> shopPromotion = shopData.getShopPromotion();
            data.clear();
            data.addAll(shopPromotion);
        }
        return data;
    }

    /**
     * @return 金牌顾问数据
     */
    private ArrayList<GoldenSeller> getGoldenSellersData() {
        ArrayList<GoldenSeller> mData = new ArrayList<>();


        //获取数据
        if (null != shopData) {
            ArrayList<GoldenSeller> sellers = shopData.getSellers();
            mData.clear();
            mData.addAll(sellers);
        }
        return mData;
    }

    /**
     * @return 获取图片url
     */
    private String getLocationData() {
        String picUrl = "";
        if (null != shopData) {
            ShopInfo shopInfo = shopData.getShopInfo();
            if (null != shopInfo) {
                PickupPointT shopLocation = shopInfo.getShopLocation();
                if (null != shopLocation) {
                    double lon = shopLocation.getLongitude();
                    double lat = shopLocation.getLatitude();
                    LatLng latLng = MapUtils.bd_decrypt(lat, lon);
                    return aMapPicUrl.getLocationPicUrl(latLng);
                }
            }
        }
        return picUrl;
    }

    @Override
    public String fragmentDescription() {
        return "店铺首页";
    }


}
