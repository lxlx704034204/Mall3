package com.hxqc.mall.thirdshop.accessory4s.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.WidgetController;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.accessory4s.adapter.AccessorySaleNewAdapter;
import com.hxqc.mall.thirdshop.accessory4s.api.Accessory4SApiClient;
import com.hxqc.mall.thirdshop.accessory4s.model.ProductList4S;
import com.hxqc.mall.thirdshop.accessory4s.utils.ActivitySwitcherAccessory4S;
import com.hxqc.mall.thirdshop.accessory4s.views.BorderScrollView;
import com.hxqc.mall.thirdshop.accessory4s.views.FilterTipFrom4SView;
import com.hxqc.mall.thirdshop.activity.shop.BaseShopDetailsActivity;
import com.hxqc.mall.thirdshop.control.ShopDetailsController;
import com.hxqc.mall.thirdshop.model.ShopInfo;
import com.hxqc.mall.thirdshop.model.ThirdPartShop;
import com.hxqc.mall.thirdshop.utils.AreaSiteUtil;
import com.hxqc.mall.thirdshop.views.CallBar;
import com.hxqc.mall.thirdshop.views.ShopDetailsHeadView;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.ListViewNoSlide;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

/**
 * 用品销售 4s店版
 * Created by huangyi on 16/2/24.
 */
public class AccessorySaleFrom4SActivity extends BaseShopDetailsActivity implements FilterTipFrom4SView.OnFilterClickListener, BorderScrollView.OnBorderListener {

    static final int DEFAULT_PATE = 1;
    int mPage = DEFAULT_PATE; //当前页
    boolean isLoading;

    ShopInfo mInfo;
    String mClass1stID; //一级分类id
    String mClass2ndID; //二级分类id
    String mBrand; //品牌名
    String mSeries; //车系名
    String mPriceOrder; //价格升降序 asc升序 desc降序

    ShopDetailsHeadView mHeadView;
    View mShadeView;
    FilterTipFrom4SView mTipView;
    ListViewNoSlide mListView;
    CallBar mCallView;
    RequestFailView mFail1View; //管理List
    RequestFailView mFail2View; //管理整个
    ArrayList< ProductList4S > mProductList = new ArrayList<>();
    AccessorySaleNewAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessory_sale_from_4s);

        initView();
        initHeadData();
    }


    private void initView() {
        mHeadView = (ShopDetailsHeadView) findViewById(R.id.sale_head);
        mShadeView = findViewById(R.id.sale_shade1);
        mTipView = (FilterTipFrom4SView) findViewById(R.id.sale_tip);
        mTipView.setOnFilterClickListener(this);
        ArrayList< View > mShadeList = new ArrayList<>();
        mShadeList.add(mShadeView);
        mShadeList.add(findViewById(R.id.sale_shade2));
        mShadeList.add(findViewById(R.id.sale_shade3));
        mTipView.setShadeList(mShadeList);
        mListView = (ListViewNoSlide) findViewById(R.id.sale_list);
        mAdapter = new AccessorySaleNewAdapter(this, mProductList, false);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
                ActivitySwitcherAccessory4S.toProductDetail(AccessorySaleFrom4SActivity.this, "", mProductList.get(position).productID);
            }
        });
        mCallView = (CallBar) findViewById(R.id.sale_call);
        mFail1View = (RequestFailView) findViewById(R.id.sale_fail1);
        mFail2View = (RequestFailView) findViewById(R.id.sale_fail2);
        mFail1View.setEmptyDescription("没有符合条件的商品", R.mipmap.ic_acc_empty);
        mFail1View.setEmptyButtonClick("随便逛逛", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearCondition();
                initListData(false);
            }
        });
        mFail1View.setFailButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initListData(false);
            }
        });
        mFail2View.setEmptyDescription("暂无店铺");
        mFail2View.setEmptyButtonClick("关闭", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mFail2View.setFailButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initHeadData();
            }
        });

        ((BorderScrollView) findViewById(R.id.sale_scroll)).setOnBorderListener(this);
    }


    /**
     * 加载店铺信息
     **/
    private void initHeadData() {
        mShopDetailsController.requestThirdPartShop(this, new ShopDetailsController.ThirdPartShopHandler() {
            @Override
            public void onSucceed(ThirdPartShop thirdPartShop) {
                if (null == thirdPartShop) {
                    mHeadView.setVisibility(View.GONE);
                    mTipView.setVisibility(View.GONE);
                    mListView.setVisibility(View.GONE);
                    mCallView.setVisibility(View.GONE);
                    mFail2View.setVisibility(View.VISIBLE);
                    mFail2View.setRequestViewType(RequestFailView.RequestViewType.empty);
                } else {
                    mHeadView.setVisibility(View.VISIBLE);
                    mTipView.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.VISIBLE);
                    mCallView.setVisibility(View.VISIBLE);
                    mFail2View.setVisibility(View.GONE);

                    ShopInfo info = thirdPartShop.getShopInfo();
                    mInfo = info;
                    getSupportActionBar().setTitle(info.shopTitle);
                    mHeadView.bindData(info);
                    mHeadView.setTabCheck(ShopDetailsHeadView.TAB_YPXS);
                    mTipView.initData(AreaSiteUtil.getInstance(AccessorySaleFrom4SActivity.this).getSiteID(), info.shopID, info.getShopLocation().latitude, info.getShopLocation().longitude);
                    mCallView.setNumber("400 872 5542");

                    ViewGroup.LayoutParams params = mShadeView.getLayoutParams();
                    params.height = WidgetController.getHeight(mHeadView);
                    mShadeView.setLayoutParams(params);

                    initListData(true);
                }
            }


            @Override
            public void onFailed(boolean offLine) {
                mHeadView.setVisibility(View.GONE);
                mTipView.setVisibility(View.GONE);
                mListView.setVisibility(View.GONE);
                mCallView.setVisibility(View.GONE);
                mFail2View.setVisibility(View.VISIBLE);
                mFail2View.setRequestViewType(RequestFailView.RequestViewType.fail);
            }
        });
    }


    /**
     * 加载列表
     **/
    private void initListData(boolean hasLoadingAnim) {
        isLoading = true;

        new Accessory4SApiClient().productList(mPage + "", mBrand, mSeries, mInfo.getShopLocation().latitude, mInfo.getShopLocation().longitude, AreaSiteUtil.getInstance(this).getSiteID(), mInfo.shopID, mClass1stID, mClass2ndID, mPriceOrder, new LoadingAnimResponseHandler(this, hasLoadingAnim) {
            @Override
            public void onSuccess(String response) {
                ArrayList< ProductList4S > temp = JSONUtils.fromJson(response, new TypeToken< ArrayList< ProductList4S > >() {
                });
                //初次加载
                if (mPage == DEFAULT_PATE) {
                    mProductList.clear();
                    if (null == temp || temp.size() == 0) {
                        mListView.setVisibility(View.GONE);
                        mFail1View.setVisibility(View.VISIBLE);
                        mFail1View.setRequestType(RequestFailView.RequestViewType.empty);
                    } else {
                        mListView.setVisibility(View.VISIBLE);
                        mFail1View.setVisibility(View.GONE);
                        mProductList.addAll(temp);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    if (null != temp && temp.size() != 0) {
                        mProductList.addAll(temp);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (mPage == DEFAULT_PATE) {
                    mListView.setVisibility(View.GONE);
                    mFail1View.setVisibility(View.VISIBLE);
                    mFail1View.setRequestViewType(RequestFailView.RequestViewType.fail);
                }
            }


            @Override
            public void onFinish() {
                super.onFinish();
                isLoading = false;
            }
        });
    }


    @Override
    public void onBrandClick(String brand, String series) {
        this.mBrand = brand;
        this.mSeries = series;
        this.mPriceOrder = "";
        mPage = DEFAULT_PATE;
        initListData(true);
    }


    @Override
    public void onClassClick(String class1stID, String class2ndID) {
        this.mClass1stID = class1stID;
        this.mClass2ndID = class2ndID;
        this.mPriceOrder = "";
        mPage = DEFAULT_PATE;
        initListData(true);
    }


    @Override
    public void onPriceClick(String priceOrder) {
        this.mPriceOrder = priceOrder;
        mPage = DEFAULT_PATE;
        initListData(true);
    }


    private void clearCondition() {
        mTipView.clearCondition();
        this.mBrand = "";
        this.mSeries = "";
        this.mClass1stID = "";
        this.mClass2ndID = "";
        this.mPriceOrder = "";
        mPage = DEFAULT_PATE;
    }


    @Override
    public void onBottom() {
        //加载下一页
        if (!isLoading && mShadeView.getVisibility() == View.GONE) {
            mPage++;
            initListData(true);
        }
    }


    @Override
    public void onTop() {

    }

}
