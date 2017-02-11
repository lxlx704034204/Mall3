package com.hxqc.mall.thirdshop.maintenance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.api.MaintenanceClient;
import com.hxqc.mall.thirdshop.maintenance.model.promotion.PromotionInfo_m;
import com.hxqc.mall.thirdshop.maintenance.util.ActivitySwitchMaintenance;
import com.hxqc.mall.thirdshop.maintenance.views.MaintenanceHTMLView;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.views.CallBar;
import com.hxqc.socialshare.manager.ShareController;
import com.hxqc.socialshare.pojo.ShareContent;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

/**
 * Author: wanghao
 * Date: 2016-03-23
 * FIXME
 * Todo  保养促销界面
 */
public class MaintenancePromotionActivity extends BackActivity {

    ViewStub mViewContent;
    View mTakenLineView;
    RequestFailView mRequestFailView;


    TextView mPageTopTitleView;
    TextView mSalesTimeView;
    TextView mSalesPostTimeView;
    TextView mShopNameView;

    MaintenanceHTMLView htmlView;

    CallBar mCallBar;
    ShareController shareController;

    MaintenanceClient client;

    PromotionInfo_m promotionInfo_m;


    //店铺块
    private View mShopBlockView;
    private ImageView mShopIconView;
    private TextView mShopTitleView;
    private TextView mShopAddressView;

    /**
     * 测试假数据
     */
    String testDataJson = "{\n" +
            "  \"promotionID\":\"test_p_1\",\n" +
            "  \"img\":\"\",\n" +
            "  \"title\":\"假数据促销标题\",\n" +
            "  \"shopInfo\":{\n" +
            "    \"brand\":\"修仙牌\",\n" +
            "    \"brandID\":\"\",\n" +
            "    \"shopID\": \"shop1585067251822281\",\n" +
            "    \"shopLogoThumb\": \"http://10.0.15.203:8082/merchant/c9/bb/c9bb1c1d1dc2531480f5c361f8c580f7.jpg\",\n" +
            "    \"shopName\": \"武汉恒信瑞沃汽车销售服务有限公司\",\n" +
            "    \"shopTitle\": \"武汉瑞沃沃尔沃(进口)\",\n" +
            "    \"shopTel\": \"027-85316160\",\n" +
            "    \"shopLocation\": {\n" +
            "      \"name\": \"武汉瑞沃沃尔沃(进口)\",\n" +
            "      \"address\": \"湖北省武汉市江岸区兴业路黄浦科技园特11号Test11\",\n" +
            "      \"latitude\": 30.635815,\n" +
            "      \"longitude\": 114.285132,\n" +
            "      \"tel\": \"027-85316160\"\n" +
            "      }\n" +
            "  },\n" +
            "  \"serverTime\": \"2016-03-23 15:49:12\",\n" +
            "  \"publishDate\": \"2016-01-19\",\n" +
            "  \"endDate\": \"2016-01-31\",\n" +
            "  \"startDate\": \"2016-01-19\",\n" +
            "  \"subscription\": 199,\n" +
            "  \"status\": \"20\",\n" +
            "  \"attachments\": [\n" +
            "    {\n" +
            "    \"url\": \"http://s.hxqc.com/newcar/merchant/be/16/be16fbc93dd398fc03f3aad2f3e13991.jpg\"\n" +
            "    },\n" +
            "    {\n" +
            "    \"url\": \"http://s.hxqc.com/newcar/merchant/29/8a/298a0e48f34054d9438492742888166d.jpg\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"summary\": \"【汉口恒信沃尔沃金猴跨年大礼包】通过网络订车的客户再送价值千元油卡，VOLVO原厂台历，VOLVO原厂钥匙扣，VOLVO原厂真皮养护套装，发动机下护板，VOLVO原厂羊毛脚垫。\",\n" +
            "  \"content\": \"<p>[IMG_1][IMG_2]</p><div>[data]</div>\",\n" +
            "  \"maintenancePackages\":[\n" +
            "    {\n" +
            "      \"packageID\":\"tc1\",\n" +
            "      \"name\":\"套餐一\",\n" +
            "      \"amount\":\"500.00\",\n" +
            "      \"discount\":\"400.00\",\n" +
            "      \"suitable\":\"['沃尔沃XC Classic 2014款2.5T T5 行政版','沃尔沃XC Classic 2014款2.5T T5 行政版']\",\n" +
            "      \"items\":[\n" +
            "        {\n" +
            "          \"name\":\"更换机油、机滤\",\n" +
            "          \"itemId\":\"xm1\",\n" +
            "          \"amount\":\"\",\n" +
            "          \"workCost\":\"28.00\",\n" +
            "          \"discount\":\"\",\n" +
            "          \"deduction\":\"\",\n" +
            "          \"summary\":\"\",\n" +
            "          \"goods\":[\n" +
            "            {\n" +
            "              \"name\":\"嘉实多高级全合成机油\",\n" +
            "              \"price\":\"208.00\",\n" +
            "              \"count\":\"1\",\n" +
            "              \"goodsID\":\"wp1\",\n" +
            "              \"img\":\"\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\":\"奥迪A3机油滤清器\",\n" +
            "              \"price\":\"28.00\",\n" +
            "              \"count\":\"1\",\n" +
            "              \"goodsID\":\"wp2\",\n" +
            "              \"img\":\"\"\n" +
            "            }\n" +
            "          ]\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\":\"发动机清洗\",\n" +
            "          \"itemId\":\"xm2\",\n" +
            "          \"amount\":\"\",\n" +
            "          \"workCost\":\"28.00\",\n" +
            "          \"discount\":\"\",\n" +
            "          \"deduction\":\"\",\n" +
            "          \"summary\":\"\",\n" +
            "          \"goods\":[\n" +
            "            {\n" +
            "              \"name\":\"发动机清洗油\",\n" +
            "              \"price\":\"208.00\",\n" +
            "              \"count\":\"1\",\n" +
            "              \"goodsID\":\"\",\n" +
            "              \"img\":\"\"\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"packageID\":\"tc2\",\n" +
            "      \"name\":\"套餐二\",\n" +
            "      \"amount\":\"400.00\",\n" +
            "      \"discount\":\"300.00\",\n" +
            "      \"suitable\":\"['沃尔沃XC Classic 2014款2.5T T5 行政版','沃尔沃XC Classic 2014款2.5T T5 行政版']\",\n" +
            "      \"items\":[\n" +
            "        {\n" +
            "          \"name\":\"更换机油、机滤\",\n" +
            "          \"itemId\":\"xm3\",\n" +
            "          \"amount\":\"\",\n" +
            "          \"workCost\":\"28.00\",\n" +
            "          \"discount\":\"\",\n" +
            "          \"deduction\":\"\",\n" +
            "          \"summary\":\"\",\n" +
            "          \"goods\":[\n" +
            "            {\n" +
            "              \"name\":\"嘉实多高级全合成机油\",\n" +
            "              \"price\":\"208.00\",\n" +
            "              \"count\":\"1\",\n" +
            "              \"goodsID\":\"\",\n" +
            "              \"img\":\"\"\n" +
            "            },\n" +
            "            {\n" +
            "              \"name\":\"奥迪A3机油滤清器\",\n" +
            "              \"price\":\"28.00\",\n" +
            "              \"count\":\"1\",\n" +
            "              \"goodsID\":\"\",\n" +
            "              \"img\":\"\"\n" +
            "            }\n" +
            "          ]\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\":\"更换雨刮器\",\n" +
            "          \"itemId\":\"xm4\",\n" +
            "          \"amount\":\"\",\n" +
            "          \"workCost\":\"28.00\",\n" +
            "          \"discount\":\"\",\n" +
            "          \"deduction\":\"\",\n" +
            "          \"summary\":\"\",\n" +
            "          \"goods\":[\n" +
            "            {\n" +
            "              \"name\":\"更换雨刮器\",\n" +
            "              \"price\":\"108.00\",\n" +
            "              \"count\":\"1\",\n" +
            "              \"goodsID\":\"\",\n" +
            "              \"img\":\"\"\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"share\":{\n" +
            "    \"img\": \"http://10.0.15.203:8082/merchant/c9/bb/c9bb1c1d1dc2531480f5c361f8c580f7.jpg\",\n" +
            "    \"title\": \"岁末倒计时，百款车型感恩回馈\",\n" +
            "    \"content\": \"【汉口恒信沃尔沃金猴跨年大礼包】通过网络订车的客户再送价值千元油卡，VOLVO原厂台历，VOLVO原厂钥匙扣，VOLVO原厂真皮养护套装，发动机下护板，VOLVO原厂羊毛脚垫。,点击查看:http://10.0.15.201:8086/Promotion/Index/detail/aid/1604475752920807.html\",\n" +
            "    \"url\": \"http://10.0.15.201:8086/Promotion/Index/detail/aid/1604475752920807.html\"\n" +
            "    }\n" +
            "}\n";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_activity_pdetail_framwork);
        client = new MaintenanceClient();
        if (shareController == null) {
            shareController = new ShareController(this);
        }
        mViewContent = (ViewStub) findViewById(R.id.view_stub_sales_detail);
        mTakenLineView = findViewById(R.id.show_view);
        mRequestFailView = (RequestFailView) findViewById(R.id.refresh_fail_view);
        getApiData();
    }

    private void initView() {
        mShopBlockView = findViewById(R.id.shop_detail);
        mShopIconView = (ImageView) findViewById(R.id.p_shop_icon);
        mShopTitleView = (TextView) findViewById(R.id.p_shop_title);
        mShopAddressView = (TextView) findViewById(R.id.p_shop_address);
        mPageTopTitleView = (TextView) findViewById(R.id.tv_sales_title);
        mSalesTimeView = (TextView) findViewById(R.id.tv_sales_time);
        mSalesPostTimeView = (TextView) findViewById(R.id.tv_s_detail_post_time);
        mShopNameView = (TextView) findViewById(R.id.tv_s_detail_shop_name);
        htmlView = (MaintenanceHTMLView) findViewById(R.id.v_html_img_info);
        mCallBar = (CallBar) findViewById(R.id.call_bar);
    }

    /**
     * 初始化数据
     */
    private void getApiData() {

        String promotion_id = "";

        if (getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA) != null) {
            promotion_id = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA)
                    .getString(ActivitySwitchMaintenance.PROMOTION_ID);
        } else {
            promotion_id = getIntent().getStringExtra(ActivitySwitchMaintenance.PROMOTION_ID);
        }




        DebugLog.i("test_s_p_pid", promotion_id);

//        requestSuccessOperate(testDataJson);

        client.getMaintenancePromotionDetail(promotion_id, new LoadingAnimResponseHandler(MaintenancePromotionActivity.this, true) {
            @Override
            public void onSuccess(String response) {
                requestSuccessOperate(response);
                DebugLog.i("test_s_p_list", response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                requestFailView();
            }
        });

    }

    /**
     * 请求成功操作
     *
     * @param response
     *         成功回调json 数据
     */
    private void requestSuccessOperate(String response) {
        promotionInfo_m = new PromotionInfo_m();
        promotionInfo_m = JSONUtils.fromJson(response, new TypeToken< PromotionInfo_m >() {
        });
        if (promotionInfo_m != null) {
            mRequestFailView.setVisibility(View.GONE);
            mViewContent.inflate();
            initView();
            setUpSth();
            mCallBar.setVisibility(View.VISIBLE);
        } else {
            requestFailView();
        }
    }

    /**
     * 设置点击
     */
    private void setUpSth() {
        mPageTopTitleView.setText(promotionInfo_m.title);

        if (TextUtils.isEmpty(promotionInfo_m.shopInfo.shopTel)) {
            mCallBar.setVisibility(View.GONE);
        } else {
            mCallBar.setTitle("咨询电话");
            mCallBar.setNumber(promotionInfo_m.shopInfo.shopTel);
        }

        StringBuffer sb = new StringBuffer();
        sb.append("促销时间：");
        if (TextUtils.isEmpty(promotionInfo_m.startDate)) {

            sb.append(promotionInfo_m.publishDate.replace("-", "."));
        } else {
            sb.append(promotionInfo_m.startDate.replace("-", "."));
        }
        sb.append("-");
        sb.append(promotionInfo_m.endDate.replace("-", "."));
        mSalesTimeView.setText(sb.toString());
        mSalesPostTimeView.setText(String.format("发布时间：%s", promotionInfo_m.publishDate.replace("-", ".")));
        mShopNameView.setText(String.format("经销商：%s", promotionInfo_m.shopInfo.shopTitle));

        htmlView.setHTMLDatas(promotionInfo_m);

        ImageUtil.setImage(MaintenancePromotionActivity.this, mShopIconView, promotionInfo_m.shopInfo.shopLogoThumb);

        mShopTitleView.setText(promotionInfo_m.shopInfo.shopName);
        mShopAddressView.setText(String.format("地址：%s", promotionInfo_m.shopInfo.getShopLocation().address));

        mShopBlockView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherThirdPartShop.toShopDetails(MaintenancePromotionActivity.this, promotionInfo_m.shopInfo.shopID);
            }
        });

    }

    /**
     * 回到主页
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_sales_p_detail, menu);
        return true;
    }

    /**
     * 菜单条目点击
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int i = item.getItemId();
        if (i == R.id.action_to_home) {
            ActivitySwitchBase.toMain(MaintenancePromotionActivity.this, 0);

        } else if (i == R.id.action_to_share) {
            if (promotionInfo_m == null)
                return false;

            if (promotionInfo_m.share == null)
                return false;

            ShareContent shareContent = promotionInfo_m.share;
            shareContent.setContext(this);
            shareController.showSharePopupWindow(shareContent);

        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (shareController != null) {
            shareController.onActivityResult(this, requestCode, resultCode, data);
        }
    }


    /**
     * 获取数据失败  刷新显示
     */
    private void requestFailView() {
        mRequestFailView.setEmptyDescription("获取数据失败");
        mRequestFailView.setEmptyButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApiData();
            }
        });
        mRequestFailView.setFailButtonClick("刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApiData();
            }
        });
        mRequestFailView.setRequestViewType(RequestFailView.RequestViewType.fail);
        mRequestFailView.setVisibility(View.VISIBLE);
    }

}
