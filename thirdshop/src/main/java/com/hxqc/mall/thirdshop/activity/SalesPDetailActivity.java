package com.hxqc.mall.thirdshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.RequestFailView;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.api.ThirdPartShopClient;
import com.hxqc.mall.thirdshop.model.promotion.SalesDetail;
import com.hxqc.mall.thirdshop.model.promotion.SalesPModel;
import com.hxqc.mall.thirdshop.utils.ActivitySwitcherThirdPartShop;
import com.hxqc.mall.thirdshop.views.CallBar;
import com.hxqc.mall.thirdshop.views.InfoIntroduceView;
import com.hxqc.socialshare.manager.ShareController;
import com.hxqc.socialshare.pojo.ShareContent;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import cz.msebera.android.httpclient.Header;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 促销详情
 */
public class SalesPDetailActivity extends BackActivity {

    ViewStub mViewContent;
    View mTakenLineView;
    RequestFailView mRequestFailView;
    Button askPriceView;
    //----------------------------------------------------------------------------
    Button orderDriveView;
    TextView mPageTopTitleView;
    TextView mSalesTimeView;
    TextView mSalesPostTimeView;
    TextView mShopNameView;
    TextView mPageTopInfoView;
    TextView mPageBottomInfoView;
    TextView mDisclaimerView;
    InfoIntroduceView mCarImgInfoView;
    Button mToPay;
    RelativeLayout mBottomView;
    //    ImageView mShopTelCallView;
    CallBar mCallBar;
    ThirdPartShopClient apiClient;
    SalesDetail salesDetail;
    ShareController shareController;
    //店铺块
    private View mShopBlockView;
    private ImageView mShopIconView;
    private TextView mShopTitleView;
    private TextView mShopAddressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_activity_sales_pdetail_framwork);
        apiClient = new ThirdPartShopClient();
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
//        mShopTelCallView = (ImageView) findViewById(R.id.shop_tel_call);
//        mShopTelCallView.setVisibility(View.VISIBLE);
        mBottomView = (RelativeLayout) findViewById(R.id.rl_sales_detail_bottom);

        mToPay = (Button) findViewById(R.id.btn_deposit_pay);
        mPageTopTitleView = (TextView) findViewById(R.id.tv_sales_title);
        mSalesTimeView = (TextView) findViewById(R.id.tv_sales_time);
        mSalesPostTimeView = (TextView) findViewById(R.id.tv_s_detail_post_time);
        mShopNameView = (TextView) findViewById(R.id.tv_s_detail_shop_name);
        mPageTopInfoView = (TextView) findViewById(R.id.tv_s_detail_info);
        mPageTopInfoView.setVisibility(View.GONE);
        mPageBottomInfoView = (TextView) findViewById(R.id.tv_s_bottom_info);
        mPageBottomInfoView.setVisibility(View.GONE);
        mDisclaimerView = (TextView) findViewById(R.id.tv_s_disclaimer);
        mCarImgInfoView = (InfoIntroduceView) findViewById(R.id.v_car_img_info);
        askPriceView = (Button) findViewById(R.id.tv_s_ask_price);
        orderDriveView = (Button) findViewById(R.id.tv_s_order_driver);
        mCallBar = (CallBar) findViewById(R.id.call_bar);
    }

    /**
     * 初始化数据
     */
    private void getApiData() {
        String promotion_id = "";
        if (getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA) != null) {
            promotion_id = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA)
                    .getString(ActivitySwitcherThirdPartShop.PROMOTION_ID);
        } else {
            promotion_id = getIntent().getStringExtra(ActivitySwitcherThirdPartShop.PROMOTION_ID);
        }
        DebugLog.i("test_s_p_pid", promotion_id);
        apiClient.salesPItemDetail(promotion_id, new LoadingAnimResponseHandler(SalesPDetailActivity.this, true) {
            @Override
            public void onSuccess(String response) {
                salesDetail = new SalesDetail();
                salesDetail = JSONUtils.fromJson(response, new TypeToken< SalesDetail >() {
                });
                if (salesDetail != null) {
                    mRequestFailView.setVisibility(View.GONE);
                    mViewContent.inflate();
                    initView();
                    setUpSth();
                    mCallBar.setVisibility(View.VISIBLE);
                } else {
                    requestFailView();
                }

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
     * 设置点击
     */
    private void setUpSth() {
        mPageTopTitleView.setText(salesDetail.title);

        if (TextUtils.isEmpty(salesDetail.shopTel)) {
            mCallBar.setVisibility(View.GONE);
        } else {
            mCallBar.setTitle("咨询电话");
            mCallBar.setNumber(salesDetail.shopTel);
        }

        DebugLog.e("test_time",
                "publishDate:" +
                        change2Timestamp(salesDetail.publishDate + " 23:59:59") +
                        " endDate:" + change2Timestamp(salesDetail.endDate + " 23:59:59") +
                        " serverTime" + change2Timestamp(salesDetail.serverTime)
        );


        //订金金额
        float subscription = salesDetail.subscription;
        /**
         * 判断初始化 底部付款按钮
         */
        initSubscriptionBottomView(subscription);


        StringBuffer sb = new StringBuffer();
        sb.append("促销时间：");
        if (TextUtils.isEmpty(salesDetail.startDate)) {

            sb.append(salesDetail.publishDate.replace("-", "."));
        } else {
            sb.append(salesDetail.startDate.replace("-", "."));
        }
        sb.append("-");
        sb.append(salesDetail.endDate.replace("-", "."));
        mSalesTimeView.setText(sb.toString());
        mSalesPostTimeView.setText(String.format("发布时间：%s", salesDetail.publishDate.replace("-", ".")));
        mShopNameView.setText(String.format("经销商：%s", salesDetail.shopInfo.shopTitle));

        mCarImgInfoView.setSalesDetailInfo(salesDetail.content, salesDetail.items,
                salesDetail.itemsTableHeader, salesDetail.itemsTableFooter, salesDetail.attachments);
        mToPay.setText(String.format("预付订金%s", OtherUtil.amountFormat(subscription, false)));

        ImageUtil.setImage(SalesPDetailActivity.this, mShopIconView, salesDetail.shopInfo.shopLogoThumb);

        mShopTitleView.setText(salesDetail.shopInfo.shopName);
        mShopAddressView.setText(String.format("地址：%s", salesDetail.shopInfo.getShopLocation().address));

        mShopBlockView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitcherThirdPartShop.toShopDetails(SalesPDetailActivity.this, salesDetail.shopID);
            }
        });

        //判断是否打开  进入询问底价  按钮
        if (salesDetail.items == null) {
            askPriceView.setVisibility(View.GONE);
            orderDriveView.setVisibility(View.GONE);
        } else {
            if (salesDetail.items.size() <= 0) {
                askPriceView.setVisibility(View.GONE);
                orderDriveView.setVisibility(View.GONE);
            } else {
                askPriceView.setVisibility(View.VISIBLE);
                orderDriveView.setVisibility(View.VISIBLE);
            }
        }


        askPriceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (salesDetail.items == null) {
                    ActivitySwitcherThirdPartShop.toAskLeastPrice(
                            SalesPDetailActivity.this,
                            salesDetail.shopInfo.shopID,
                            "", "",
                            salesDetail.shopInfo.shopTel,
                            false, null);
                } else {
                    if (salesDetail.items.size() <= 0) {
                        ActivitySwitcherThirdPartShop.toAskLeastPrice(
                                SalesPDetailActivity.this,
                                salesDetail.shopInfo.shopID,
                                "", "", salesDetail.shopInfo.shopTel,
                                false, null);
                    } else {
                        ActivitySwitcherThirdPartShop.toAskLeastPrice(
                                SalesPDetailActivity.this,
                                salesDetail.shopInfo.shopID,
                                salesDetail.items.get(0).baseInfo.itemID,
                                salesDetail.items.get(0).baseInfo.itemName,
                                salesDetail.shopInfo.shopTel,
                                false, salesDetail.items);
                    }
                }

            }
        });

        orderDriveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (salesDetail.items == null) {
                    ActivitySwitcherThirdPartShop.toTestDrive(
                            SalesPDetailActivity.this,
                            salesDetail.shopInfo.shopID,
                            salesDetail.items.get(0).baseInfo.itemID,
                            "",
                            salesDetail.shopInfo.shopTel,
                            false, null);
                } else {
                    if (salesDetail.items.size() <= 0) {
                        ActivitySwitcherThirdPartShop.toTestDrive(
                                SalesPDetailActivity.this,
                                salesDetail.shopInfo.shopID,
                                salesDetail.items.get(0).baseInfo.itemID,
                                "",
                                salesDetail.shopInfo.shopTel,
                                false, null);
                    } else {
                        ActivitySwitcherThirdPartShop.toTestDrive(
                                SalesPDetailActivity.this,
                                salesDetail.shopInfo.shopID,
                                salesDetail.items.get(0).baseInfo.itemID,
                                salesDetail.items.get(0).baseInfo.itemName,
                                salesDetail.shopInfo.shopTel,
                                false, salesDetail.items);
                    }
                }

            }
        });
    }

    private void initSubscriptionBottomView(float subscription) {

        String status = salesDetail.status;

        //无状态 就不显示底部支付按钮
        if (TextUtils.isEmpty(status)) {
            mBottomView.setVisibility(View.GONE);
            return;
        }

        //是发布才判断  不然不显示
        if (status.equals(SalesPModel.P_IS_PUBLISH)) {

            if (change2Timestamp(salesDetail.serverTime) > change2Timestamp(salesDetail.endDate + " 23:59:59") ||
                    change2Timestamp(salesDetail.serverTime) < change2Timestamp(salesDetail.startDate + " 00:00:00")) {
                mBottomView.setVisibility(View.GONE);
            } else {
                if (subscription <= 0) {
                    mBottomView.setVisibility(View.GONE);
                } else {
                    mBottomView.setVisibility(View.VISIBLE);
                }
            }

        } else {
            mBottomView.setVisibility(View.GONE);
        }


    }

    private long change2Timestamp(String date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date d = simpleDateFormat.parse(date);
            return d.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
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
            ActivitySwitchBase.toMain(SalesPDetailActivity.this, 0);

        } else if (i == R.id.action_to_share) {
            if (salesDetail == null)
                return false;

            if (salesDetail.share == null)
                return false;

            ShareContent shareContent = salesDetail.share;
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
     * 预付订金
     */
    public void depositPay(View v) {
        UserInfoHelper.getInstance().loginAction(SalesPDetailActivity.this, new UserInfoHelper.OnLoginListener() {
            @Override
            public void onLoginSuccess() {
                ActivitySwitcherThirdPartShop.toConfirmOrder(salesDetail, SalesPDetailActivity.this);
            }
        });
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
