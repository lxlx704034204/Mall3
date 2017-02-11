package com.hxqc.mall.thirdshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.BaseInfo;
import com.hxqc.mall.thirdshop.model.ColorInfo;
import com.hxqc.mall.thirdshop.model.Series;
import com.hxqc.mall.thirdshop.model.ShopInfo;
import com.hxqc.mall.thirdshop.model.newcar.ModelInfo;
import com.hxqc.mall.thirdshop.model.newcar.ModelIntroduce;
import com.hxqc.mall.thirdshop.model.newcar.ParameterInfo;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.views.AppearanceToolbar;
import com.hxqc.mall.thirdshop.views.ReferenceDetailsDialog;
import com.hxqc.mall.thirdshop.views.ThirdAutoDetailIntroduce;
import com.hxqc.mall.thirdshop.views.UserGradeView;
import com.hxqc.mall.thirdshop.views.adpter.CarParameterAdapter;
import com.hxqc.mall.thirdshop.views.adpter.ColorDeatailItemAdapter;
import com.hxqc.mall.thirdshop.views.adpter.NewCarShopListAdapter;
import com.hxqc.util.CustomSinnper;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 新车 车型详情
 * Created by 赵帆
 * Date:2016-06-21
 */
public class NewCarModelDetailActivity extends NoBackActivity {
    public static final String MODEL_NAME = "MODEL_NAME";
    public static final String AREA_ID = "AREA_ID";
    public static final String BRAND = "brand";
    private ThirdPartShopClient apiClient;
    private View mainview;  //车型详情
    private ImageView choose_color_arrow;
    private ListView shop_lv; //店铺listview
    private List<ShopInfo> shopList;
    private ThirdAutoDetailIntroduce introduce; //图文介绍
    private AppearanceToolbar toolbar;
    private ImageView mBannerView;
    RequestFailView mRequestFailView;
    ModelInfo mModelInfo; // 车型信息
    private TextView priceRange; //经销商参考价
    private TextView itemOrigPrice; //厂家指导价
    private TextView mAutoDescriptionsView;
    private String siteId;
    private String brand, series;
    private String extID;
    private String shopSiteFrom;
    private TextView mShopType;
    private UserGradeView mUserGradeView;  //口碑评价
    private TextView siteName;
    private ColorDeatailItemAdapter colorDeatailItemAdapter;
    private GridView color_gv;  //车身颜色gv

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcarmodeldetail);
        mainview = View.inflate(this, R.layout.view_newcarmodeldetail_main, null);
        mUserGradeView = new UserGradeView(this);

        bindView();
        initResource();
        getData();
    }


    private void bindView() {
        toolbar = (AppearanceToolbar) findViewById(R.id.toolbar);
        shop_lv = (ListView) findViewById(R.id.shop_lv);
        mRequestFailView = (RequestFailView) findViewById(R.id.refresh_fail_view);
        choose_color_arrow = (ImageView) mainview.findViewById(R.id.choose_color_arrow);
        introduce = (ThirdAutoDetailIntroduce) mainview.findViewById(R.id.introduce);
        mBannerView = (ImageView) mainview.findViewById(R.id.banner);
        priceRange = (TextView) mainview.findViewById(R.id.car_content_1);
        itemOrigPrice = (TextView) mainview.findViewById(R.id.car_content_4);
        mAutoDescriptionsView = (TextView) mainview.findViewById(R.id.auto_descriptions);
        mShopType = (TextView) mainview.findViewById(R.id.shop_type);
        siteName = (TextView) mainview.findViewById(R.id.site_name);
        color_gv = (GridView) mainview.findViewById(R.id.color_gridView);
    }

    private void initResource() {
        extID = getIntent().getStringExtra("extID");
        siteId = getIntent().getStringExtra(AREA_ID);
        brand = getIntent().getStringExtra(BRAND);
        apiClient = new ThirdPartShopClient();
        shop_lv.addHeaderView(mainview);
        shop_lv.addFooterView(mUserGradeView);
        toolbar.setTitle("详情");
        toolbar.setOnHomeClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitchBase.toMain(NewCarModelDetailActivity.this, 0);
            }
        });
    }

    private void getData() {
        apiClient.modelIntroduce(siteId, extID, brand, getIntent().getStringExtra(MODEL_NAME),
                new LoadingAnimResponseHandler(this, true, false) {
                    @Override
                    public void onSuccess(String response) {
                        //       forTest1(response);
                        ModelIntroduce modelIntroduce
                                = JSONUtils.fromJson(response, ModelIntroduce.class);
                        if (modelIntroduce == null || modelIntroduce.getModelInfo() == null) {
                            empty();
                        } else {
                            mModelInfo = modelIntroduce.getModelInfo();
                            shopList = modelIntroduce.getShopList();
                            shopSiteFrom = modelIntroduce.shopSiteFrom;
                            showView();
                            setModelInfo(mModelInfo);
                            setShopList(shopList);
                            getUserGradeData();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBytes, Throwable throwable) {
                        DebugLog.i("Tag", "onFailure  ");
                        requestFailView();
                    }
                });
    }

    /**
     * 用户评价
     */
    private void getUserGradeData() {
        mUserGradeView.getData(extID, brand, series, null);
        mUserGradeView.showPublishButton(mModelInfo.getItemPic());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivitySwitcherThirdPartShop.REQUEST_CODE) {
            getUserGradeData();
        }


    }

    /**
     * 经销商列表
     */
    private void setShopList(List<ShopInfo> shopList) {
        if (shopList == null)
            return;
        if (shopList.isEmpty()) {
            findViewById(R.id.askprice).setVisibility(View.GONE);
        } else {
            mainview.findViewById(R.id.shop_lay).setVisibility(View.VISIBLE);
            if (shopSiteFrom.equals("10"))
                siteName.setText(shopList.get(0).siteName + "站");
            else {
                mainview.findViewById(R.id.site_lay).setVisibility(View.GONE);
                mShopType.setText("为您推荐周边经销商");
            }
        }
        NewCarShopListAdapter mNewCarShopListAdapter = new NewCarShopListAdapter(this);
        mNewCarShopListAdapter.setData(shopList);
        shop_lv.setAdapter(mNewCarShopListAdapter);
    }


    /**
     * 车型详情
     */
    private void setModelInfo(ModelInfo mModelInfo) {
        series = mModelInfo.getSeriesName();
        initColor(mModelInfo.getAppearance());
        setIntroduce(mModelInfo.getIntroduce());
        toolbar.setTitle(mModelInfo.getModelName());
        itemOrigPrice.setText(getPrice(mModelInfo.getItemOrigPrice()));
        priceRange.setText(getPrice(mModelInfo.getPriceRange()));
        ImageUtil.setImage(this, mBannerView, mModelInfo.getItemPic());
        mAutoDescriptionsView.setText(mModelInfo.getModelName());
        mShopType.setText(mModelInfo.getSeriesName() + "经销商");
        setParameter(mModelInfo.parameter);
    }

    /**
     * 车子参数
     */
    private void setParameter(List<ParameterInfo> parameter) {
        ListView parameter_lv = (ListView) findViewById(R.id.parameter_lv);
        CarParameterAdapter mAdapter = new CarParameterAdapter(this);
        mAdapter.setData(parameter);
        parameter_lv.setAdapter(mAdapter);

    }


    /**
     * 图文介绍
     */
    private void setIntroduce(String introduce) {
        BaseInfo data = new BaseInfo();
        data.setIntroduce(introduce);
        this.introduce.setAutoDetail(data);
        this.introduce.hideContentView(true); //默认隐藏
    }

    /**
     * 初始化车身颜色数据
     */
    public void initColor( List<ColorInfo> colorInfos) {
        ArrayList<ColorInfo> colors = new ArrayList<>();
        for (ColorInfo i : colorInfos) {
            if (!TextUtils.isEmpty(i.color))
                colors.add(i);
        }
        colorDeatailItemAdapter = new ColorDeatailItemAdapter(this, colors);
        color_gv.postDelayed(new Runnable() {
            @Override
            public void run() {
                int num = (int) (color_gv.getMeasuredWidth() / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        40, getResources().getDisplayMetrics()));
                color_gv.setNumColumns(num);
                color_gv.setAdapter(colorDeatailItemAdapter);
            }
        }, 50);

    }

    /**
     * 机身颜色
     */

    private void setCarColor(List<ColorInfo> appearance) {
        String[] colordes = new String[appearance.size()];
        int i = 0;
        for (ColorInfo entity : appearance) {
            colordes[i] = entity.colorDescription;
            i++;
        }
        CustomSinnper spinner = (CustomSinnper) findViewById(R.id.choose_color);
        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinnertext, colordes));
        spinner.setOnSinnperClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_color_arrow.setImageResource(R.drawable.ic_cbb_arrow_up);
            }
        });
        spinner.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                choose_color_arrow.setImageResource(R.drawable.ic_cbb_arrow_down);
            }
        });
    }


    /**
     * 图集
     */
    public void toPictures(View view) {
        try {
            String itemID = mModelInfo.extID;
            if (!TextUtils.isEmpty(itemID))
                ActivitySwitcherThirdPartShop.toAtlasExtID(this, itemID);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    /**
     * 跳转参数详情
     */
    public void clickToParameter(View view) {
        ActivitySwitcherThirdPartShop.toParameter(true, this, mModelInfo.getExtID());
    }

    /**
     * 价格明细
     */
    public void toShowPriceDetail(View view) {
        if (mModelInfo.getPriceInfo() != null)
            new ReferenceDetailsDialog(this, mModelInfo.getPriceInfo()).show();
    }

    /**
     * 询价按钮
     */
    public void askPrice(View v) {
        //  startActivity(new Intent(getApplicationContext(), AskLeastMoneyActivity2.class));
        Series mSeries = new Series();
        mSeries.setSeriesName(mModelInfo.getSeriesName());
        ActivitySwitcherThirdPartShop.toAskLeastPrice2(this, siteId, shopSiteFrom, mModelInfo.extID,
                mModelInfo.getBrand(), mSeries, mModelInfo.getModelName());
    }

    /**
     * 请求成功显示view
     */
    private void showView() {
        mRequestFailView.setVisibility(View.GONE);
        shop_lv.setVisibility(View.VISIBLE);
        findViewById(R.id.askprice).setVisibility(shopList.isEmpty() ? View.GONE : View.VISIBLE);
        mBannerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                toolbar.setAlphaAppearance(mBannerView.getHeight(), shop_lv);
            }
        }, 20);
    }


    /**
     * 价格区间格式化
     */
    public String getPrice(String price) {
        return "¥" + OtherUtil.formatPriceSingleOrRange(price);
    }


    private void empty() {
        commonError();
        mRequestFailView.setEmptyButtonClick("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.empty);
    }

    private void requestFailView() {
        commonError();
        mRequestFailView.setEmptyDescription("获取数据失败");
        mRequestFailView.setFailButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
    }

    private void commonError() {
        toolbar.showToolbar();
        mRequestFailView.setVisibility(View.VISIBLE);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void forTest1(String response) {
        try {
            JSONObject jb = new JSONObject(response);
            String a = jb.getJSONObject("modelInfo").toString();
            String b = jb.getJSONArray("shopList").toString();
            DebugLog.e("", a + b);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*
        List<ParameterInfo> parameter = new ArrayList<>();
        parameter.add(new ParameterInfo("车身尺寸", "一百米"));
        parameter.add(new ParameterInfo("综合耗油", "一百米"));
        parameter.add(new ParameterInfo("发动机", "一百米"));*/
    }

}
