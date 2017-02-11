package com.hxqc.mall.activity.recharge;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.adapter.recharge.PaymentWayListAdapter;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.model.Error;
import com.hxqc.mall.core.model.recharge.RechargeRequest;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.paymethodlibrary.inter.PayResultCallBack;
import com.hxqc.mall.paymethodlibrary.manager.PayCallBackManager;
import com.hxqc.mall.paymethodlibrary.manager.PayMethodManager;
import com.hxqc.mall.paymethodlibrary.model.EventGetSuccessModel;
import com.hxqc.mall.paymethodlibrary.model.PayOnlineResponse;
import com.hxqc.mall.paymethodlibrary.util.PayCallBackTag;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.mall.paymethodlibrary.util.PayKeyConstants;
import com.hxqc.pay.model.PaymentMethod;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cz.msebera.android.httpclient.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-03-09
 * FIXME
 * Todo 支付方式列表
 */
public class PaymentWayListActivity extends BackActivity implements View.OnClickListener {
    public static final String DATA_TAG
            = "com.hxqc.mall.activity.recharge.PaymentListActivity_data_tag ";
    private RechargeRequest rechargeRequest;
    private ListView listView;
    private PaymentWayListAdapter listAdapter;
    private TextView total;
    //    private TextView account;
    private ArrayList<PaymentMethod> paymentMethods = new ArrayList<>();
    private UserApiClient apiClient = new UserApiClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rechargeRequest = (RechargeRequest) getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA)
                .getSerializable(DATA_TAG);
        setContentView(R.layout.activity_payment_list);
        listView = (ListView) findViewById(R.id.payment_list);
//        listAdapter = new PaymentWayListAdapter(paymentMethods, this);
//        listView.setAdapter(listAdapter);
        total = (TextView) findViewById(R.id.total_pay);
        total.setText(rechargeRequest.charge_number + getString(R.string.yuan));
//        account = (TextView) findViewById(R.id.account);
        findViewById(R.id.commit_btn).setOnClickListener(this);
        loadPaymentWay();
        EventBus.getDefault().register(this);
    }

    private void loadPaymentWay() {
        apiClient.listPayment(new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                DebugLog.i("listPayment", "onSuccess");
                paymentMethods.addAll(JSONUtils.fromJson(response, new TypeToken<ArrayList<PaymentMethod>>() {
                }));
                listAdapter = new PaymentWayListAdapter(paymentMethods, PaymentWayListActivity.this);
                listView.setAdapter(listAdapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                DebugLog.i("listPayment", "onFailure");
            }
        });
//        listAdapter.notifyDataSetChanged();
    }

    //接收返回结果显示
    @Subscribe
    public void onEventMainThread(EventGetSuccessModel event) {
        if (event.getPay_status() == PayCallBackTag.PAY_SUCCESS) {
            ToastHelper.showGreenToast(this, "支付成功");
            DebugLog.i("onEventMainThread", "支付成功");
            ActivitySwitcher.toReChargeSuccess(this, rechargeRequest.orderID);
            finish();
        } else if (event.getPay_status() == PayCallBackTag.PAY_FAIL) {
            ToastHelper.showRedToast(this, "支付失败");
            DebugLog.i("onEventMainThread", "支付失败");
        } else if (event.getPay_status() == PayCallBackTag.PAY_CANCEL) {
            ToastHelper.showRedToast(this, "交易取消");
            DebugLog.i("onEventMainThread", "交易取消");
            DebugLog.e("onEventMainThread", "orderID:" + rechargeRequest.orderID);

//            ActivitySwitcher.toReChargeSuccess(this, rechargeRequest.orderID);
//            finish();
        } else if (event.getPay_status() == PayCallBackTag.PAY_EXCEPTION) {
            ToastHelper.showRedToast(this, "数据异常");
            DebugLog.i("onEventMainThread", "数据异常");
        } else if (event.getPay_status() == PayCallBackTag.PAY_PROGRESSING) {
//            if (!isTimeout) {
//            ToastHelper.showYellowToast(this, "支付结果确认中");
            DebugLog.i("onEventMainThread", "支付结果确认中");
//            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.commit_btn) {
            PaymentMethod selectedWay = listAdapter.getSelectedWay();
            if (selectedWay == null)
                ToastHelper.showRedToast(this, "请选择付款方式！");
            else
                payOnline();
        }
    }

    private void payOnline() {
        String paymentID = listAdapter.getSelectedWay().paymentID;
        if (paymentID.equals(PayConstant.WEIXIN)) {
            try {
                IWXAPI msgApi = WXAPIFactory.createWXAPI(this, PayKeyConstants.getWxAppId());

                if (!(msgApi.isWXAppInstalled() && msgApi.isWXAppSupportAPI())) {
                    ToastHelper.showRedToast(this, "亲，使用微信支付需要先安装微信客户端哦。");
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        apiClient.payOnline(rechargeRequest.orderID, paymentID,
                new LoadingAnimResponseHandler(this) {
                    @Override
                    public void onSuccess(String response) {
                        PayOnlineResponse payOnlineResponse
                                = JSONUtils.fromJson(response, new TypeToken<PayOnlineResponse>() {
                        });
                        if (payOnlineResponse == null) {
                            ToastHelper.showRedToast(PaymentWayListActivity.this, "请求支付失败");
                        } else {
                            PayMethodManager payMethodManager = new PayMethodManager(
                                    PaymentWayListActivity.this, rechargeRequest.charge_number,
                                    payOnlineResponse);

                            PayCallBackManager.getInstance().setPayResultCallBack(new PayResultCallBack() {
                                @Override
                                public void paySuccess(EventGetSuccessModel model) {

                                }

                                @Override
                                public void payFail(EventGetSuccessModel model) {

                                }

                                @Override
                                public void payCancel(EventGetSuccessModel model) {

                                }

                                @Override
                                public void payException(EventGetSuccessModel model) {

                                }

                                @Override
                                public void payProgressing(EventGetSuccessModel model) {

                                }
                            });
                            try {
                                payMethodManager.paySwitch();
                            } catch (Exception e) {
                                ToastHelper.showRedToast(PaymentWayListActivity.this, "支付校验失败");
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        com.hxqc.mall.core.model.Error error = JSONUtils.fromJson(responseString, new TypeToken<Error>() {
                        });
                        ToastHelper.showRedToast(PaymentWayListActivity.this, error.message);
                    }
                });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
