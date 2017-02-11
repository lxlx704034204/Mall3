package com.hxqc.pay.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.fragment.FunctionAppCompatFragment;
import com.hxqc.mall.core.model.order.OrderModel;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.dialog.NormalDialog;
import com.hxqc.mall.core.views.dialog.PayStatusDialog;
import com.hxqc.mall.core.views.dialog.SubmitDialog;
import com.hxqc.mall.payment.model.Wallet;
import com.hxqc.mall.paymethodlibrary.inter.PayResultCallBack;
import com.hxqc.mall.paymethodlibrary.manager.PayCallBackManager;
import com.hxqc.mall.paymethodlibrary.manager.PayMethodManager;
import com.hxqc.mall.paymethodlibrary.model.EventGetSuccessModel;
import com.hxqc.mall.paymethodlibrary.model.PayOnlineResponse;
import com.hxqc.mall.paymethodlibrary.util.PayCallBackTag;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.mall.paymethodlibrary.util.PayKeyConstants;
import com.hxqc.mall.paymethodlibrary.view.input.PayPwdViewManager;
import com.hxqc.pay.adapter.BankListAdapter;
import com.hxqc.pay.api.PayApiClient;
import com.hxqc.pay.inter.OnFragmentChangeListener;
import com.hxqc.pay.model.DepositAmountModel;
import com.hxqc.pay.model.PayStatusResponse;
import com.hxqc.pay.model.PaymentMethod;
import com.hxqc.pay.util.ConstantValue;
import com.hxqc.pay.util.Switchhelper;
import com.hxqc.pay.util.TimeUtil;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.simonvt.menudrawer.MenuDrawer;

import cz.msebera.android.httpclient.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import hxqc.mall.R;


/**
 * A simple {@link Fragment} subclass.
 * 支付订金
 */
public class OrderStep2DepositFragment extends FunctionAppCompatFragment implements View.OnTouchListener {

    Context mCtx;

    //判断订单是否超时
    boolean isTimeout = false;

    //是否可以余额支付
    boolean isCanBalancePay = false;

    boolean clickAble = true;
    final int GET_PAYMENT_LIST = 0;//支付方式列表
    final int GET_DEPOSIT_AMOUNT = 1;//订金金额
    final int TO_PAY = 2;//去支付
    final int GET_ORDER_DETAIL = 3;//获取订单详情
    final int GET_BALANCE = 4;//获取余额
    OnFragmentChangeListener mListener;
    Button mPayDeposit;
    EditText mGetPayMethod;
    TextView mDepositAomunt;
    BaseSharedPreferencesHelper baseSharedPreferencesHelper;

    String bottom_text_install = "";
    String bottom_text_offline = "";

    //支付失败head
//    RelativeLayout getOrderFailure;
//    TextView phoneClientSer;
    //下单成功head
    RelativeLayout getOrderSuccess;
    TextView showOrderID;
    TextView orderLeftTime;
    TextView mDepositNotifyView;
    CountDownTimer countDownTimer;

    //支付方式
    MenuDrawer menuDrawer;
    ListView mBankList;
    BankListAdapter mAdapter;
    PayApiClient apiClient;
    PaymentMethod paymentMethod;

    String order_id = "+";
    int pay_type;
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            createApiData(msg);
            return false;
        }
    });
    private Wallet wallet;

    public OrderStep2DepositFragment() {
        // Required empty public constructor
    }

    DepositAmountModel amountModel;

    //退出提示框
    public void backDialog(String text) {

        PayStatusDialog dialog = new PayStatusDialog(getActivity(), "重要提示", text) {

            @Override
            protected void doCall() {
                baseSharedPreferencesHelper.setOrderChange(true);
                dismiss();
                getActivity().finish();
            }

            @Override
            protected void doAgain() {
                dismiss();
            }
        };
        dialog.mCancelView.setVisibility(View.GONE);
        dialog.mOkView.setText("我知道了");
        dialog.show();

        isTimeout = true;

    }

    private void createApiData(Message msg) {
        if (msg.what == GET_PAYMENT_LIST) {

            DebugLog.i("banklist", (String) msg.obj);
            ArrayList<PaymentMethod> paymentList = JSONUtils.fromJson((String) msg.obj, new TypeToken<ArrayList<PaymentMethod>>() {
            });
            String bMoneyShow = "余额：¥ " + wallet.balance;
            mAdapter = new BankListAdapter(paymentList, getActivity(), bMoneyShow);
            mBankList.setAdapter(mAdapter);

            //选择支付方式
            mBankList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    paymentMethod = (PaymentMethod) parent.getAdapter().getItem(position);
                    if (!isCanBalancePay && PayConstant.BALANCE.equals(paymentID)) {
                        ToastHelper.showRedToast(getActivity(), "余额不足");
                    } else {
                        menuDrawer.closeMenu();
                        mGetPayMethod.setText(paymentMethod.title);
                    }
                }
            });

        } else if (msg.what == GET_DEPOSIT_AMOUNT) {

            amountModel = JSONUtils.fromJson((String) msg.obj, DepositAmountModel.class);
            mDepositAomunt.setText(OtherUtil.stringToMoney(amountModel.amount));

//            获取余额
            getBalance();

        } else if (msg.what == GET_BALANCE) {

            wallet = JSONUtils.fromJson((String) msg.obj, new TypeToken<Wallet>() {
            });

            if (wallet != null) {
                double depositMoney = Double.parseDouble(amountModel.amount);
                double balanceMoney = Double.parseDouble(wallet.balance);

                if (balanceMoney >= depositMoney) {
                    isCanBalancePay = true;
                }

                getPaymentList();
            }

        } else if (msg.what == TO_PAY) {
            DebugLog.i("pay_online", msg.obj + "::" + paymentMethod.paymentID);

            PayOnlineResponse payOnlineResponse = JSONUtils.fromJson((String) msg.obj, PayOnlineResponse.class);
            if (payOnlineResponse == null) {
                ToastHelper.showRedToast(getActivity(), "请求支付失败");
            } else {
                recordTradeID(payOnlineResponse);

                DebugLog.i("Tag", "string  " + getActivity() + "  \n" +
                        amountModel.amount + "\n" + payOnlineResponse.toString());

                PayMethodManager payMethodManager = new PayMethodManager(
                        getActivity(),
                        amountModel.amount,
                        payOnlineResponse
                );

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

                DebugLog.i("Tag", "payMethodManager  " + payMethodManager);

                    try {
                        payMethodManager.paySwitch();
                    } catch (Exception e) {
                        ToastHelper.showRedToast(getActivity(), "支付校验失败");
                        e.printStackTrace();
                    }
            }


        } else if (msg.what == GET_ORDER_DETAIL) {
            OrderModel orderModel = JSONUtils.fromJson((String) msg.obj, new TypeToken<OrderModel>() {
            });
            try {

                /**
                 * 订单时间，订单号提示
                 */

                DebugLog.i("test_deposit", "millis:" + TimeUtil.getLeftTime(orderModel.serverTime, orderModel.expiredTime));
                //倒计时
                countDownTimer = new CountDownTimer(TimeUtil.getLeftTime(orderModel.serverTime, orderModel.expiredTime), 1000) {
                    public void onTick(long millisUntilFinished) {
                        try {
                            SpannableStringBuilder str_time;
                            StringBuilder sb = new StringBuilder();
                            String head = "请在提交订单后";
                            String time = TimeUtil.getDTime(millisUntilFinished);
                            DebugLog.i("test_deposit", "millisUntilFinished:" + millisUntilFinished);
                            String end = "内完成支付，否则订单会自动取消，订金支付成功后，为您锁定车辆！";
                            int time_length = time.length();
                            String all_str = sb.append(head).append(time).append(end).toString();
                            str_time = OtherUtil.toCallText(all_str, 7, 7 + time_length, "#FE4648");
                            orderLeftTime.setText(str_time);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    public void onFinish() {
                        backDialog("由于您没有在规定时间内付款，该订单已被系统取消，请重新下单。如有付款，请联系客服退款，谢谢！");
                    }
                }.start();

                /**
                 * 服务提示  以及电话
                 */
                SpannableStringBuilder str = null;
                //分期
                if (orderModel.paymentType.equals("2")) {
                    str = OtherUtil.toCallText(bottom_text_install, 44, bottom_text_install.length(), "#2196f3");
                }//线下
                else if (orderModel.paymentType.equals("1")) {
                    str = OtherUtil.toCallText(bottom_text_offline, 43, bottom_text_offline.length(), "#2196f3");
                }
                mDepositNotifyView.setText(str);

                mDepositNotifyView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OtherUtil.callHXService(getActivity());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        baseSharedPreferencesHelper = new BaseSharedPreferencesHelper(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mCtx = activity;
        DebugLog.i("test_bug", "onAttach context");
        DebugLog.i("test_bug_ctx", "context: " + mCtx);
        initListener(activity);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCtx = activity;
        DebugLog.i("test_bug", "onAttach Activity");
        DebugLog.i("test_bug_ctx", "Activity: " + mCtx);
        initListener(activity);
    }

    private void initListener(Context activity) {
        try {
            mListener = (OnFragmentChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "--os2df");
        }

        bottom_text_offline =
                "24小时内会有客服人员与您联系，请保持手机畅通。如果有任何疑问，请拨打我们的客服电话："
                        + getResources().getString(R.string.tv_service_phone);

        bottom_text_install =
                "24小时内由专人为您办理分期手续，请保持手机畅通。如果有任何疑问，请拨打我们的客服电话："
                        + getResources().getString(R.string.tv_service_phone);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (apiClient != null)
            apiClient = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_deposit_drawer, container, false);
        view.setOnTouchListener(this);
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiClient = new PayApiClient();
        pay_type = (int) getArguments().get(PayConstant.PAY_STATUS_FLAG);
        order_id = (String) getArguments().get(ConstantValue.PAY_MAIN_ORDER_ID);

        getOrderSuccess = (RelativeLayout) view.findViewById(R.id.rl_orders_success);
        showOrderID = (TextView) view.findViewById(R.id.tv_show_order_id);
        orderLeftTime = (TextView) view.findViewById(R.id.tv_get_order_expired_time);
        mDepositNotifyView = (TextView) view.findViewById(R.id.tv_deposit_notify);
        mBankList = (ListView) view.findViewById(R.id.lv_bank);
        menuDrawer = (MenuDrawer) view.findViewById(R.id.drawer);
        menuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
        mPayDeposit = (Button) view.findViewById(R.id.btn_pay_deposit);
        mGetPayMethod = (EditText) view.findViewById(R.id.mcit_get_bank);
        mDepositAomunt = (TextView) view.findViewById(R.id.tv_deposit_amount);
        getOrderSuccess.setVisibility(View.VISIBLE);
        showOrderID.setText("订单号：" + order_id);
        refreshOrderDetail();

        //订金
        apiClient.getDepositAmount(order_id, new BaseMallJsonHttpResponseHandler(getActivity()) {
            @Override
            public void onSuccess(String response) {
                Message msg = Message.obtain();
                msg.what = GET_DEPOSIT_AMOUNT;
                msg.obj = response;
                mHandler.sendMessage(msg);
            }
        });

        //打开支付方式列表
        mGetPayMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuDrawer.openMenu();
            }
        });

        //去支付
        mPayDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickAble) {
                    clickAble = false;
                    goToPay();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            clickAble = true;
                        }
                    }, 500);
                }
            }
        });
    }

    private void getPaymentList() {
        //支付列表
        apiClient.listPayment(new BaseMallJsonHttpResponseHandler(getActivity()) {
            @Override
            public void onSuccess(String response) {
                Message msg = Message.obtain();
                msg.what = GET_PAYMENT_LIST;
                msg.obj = response;
                mHandler.sendMessage(msg);
            }
        });
    }

    private void refreshOrderDetail() {
        //订单详情
        apiClient.orderDetail(order_id, new BaseMallJsonHttpResponseHandler(getActivity()) {
            @Override
            public void onSuccess(String response) {
                Message msg = Message.obtain();
                msg.what = GET_ORDER_DETAIL;
                msg.obj = response;
                mHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 获取余额
     */
    private void getBalance() {
        new UserApiClient().getWalletData(new LoadingAnimResponseHandler(getActivity(), false) {
            @Override
            public void onSuccess(String response) {
                Message msg = Message.obtain();
                msg.what = GET_BALANCE;
                msg.obj = response;
                mHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 执行余额支付
     */
    private void toPayByBalance() {

        if (!UserInfoHelper.getInstance().hasPayPassword(getActivity())) {
            new NormalDialog(getActivity(), "提示：", "为保证账户安全，请先设置支付密码", "去设置") {
                @Override
                protected void doNext() {
                    ActivitySwitchBase.toRealNameAuthentication(getContext());
                }
            }.show();
        } else {
            new PayPwdViewManager(getActivity()).showBalanceNumberKeyboard(mPayDeposit, amountModel.amount, new PayPwdViewManager.OnPwdFinishListener() {

                @Override
                public void pwdText(String pwd) {
                    goToPayBalance(pwd);
                }

                @Override
                public void inputCancel() {
                    ToastHelper.showRedToast(getActivity(), "交易取消");
                }

            });
        }

    }

    void goToPayBalance(String pwd) {
        /**
         * todo  余额支付
         */
        ToastHelper.showGreenToast(getActivity(), "余额支付：" + order_id + " " + PayConstant.BALANCE + " " + pwd);
    }

    /**
     * 去支付
     */
    private void goToPay() {
        if (paymentMethod == null) {

            ToastHelper.showYellowToast(getActivity(), "请选择支付方式");

        } else {
            String pay_method = paymentMethod.paymentID;

            if (amountModel == null)
                return;

            String pay_amount = amountModel.amount;

            if (TextUtils.isEmpty(pay_amount)) {
                pay_amount = "0";
            }

            if (PayConstant.BALANCE.equals(paymentMethod.paymentID)) {
                if (isCanBalancePay) {
                    toPayByBalance();
                } else {
                    ToastHelper.showRedToast(getActivity(), "余额不足");
                }
                return;
            }

            if (pay_method.equals(PayConstant.WEIXIN)) {

                try {
                    IWXAPI msgApi = WXAPIFactory.createWXAPI(getActivity(), PayKeyConstants.getWxAppId());

                    if (!(msgApi.isWXAppInstalled() && msgApi.isWXAppSupportAPI())) {
                        ToastHelper.showRedToast(getActivity(), "亲，使用微信支付需要先安装微信客户端哦。");
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            apiClient.payOnline(order_id, pay_method, pay_amount, new LoadingAnimResponseHandler(getActivity()) {
                @Override
                public void onSuccess(String response) {
                    Message msg = Message.obtain();
                    msg.what = TO_PAY;
                    msg.obj = response;
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (statusCode == 403) {
                        showCallDialog(true);
                    }
                }
            });
        }
    }


    //接收返回结果显示
    @Subscribe
    public void onEventMainThread(EventGetSuccessModel event) {

        if (event.getPay_status() == PayCallBackTag.PAY_SUCCESS) {
            if (!isTimeout)
                alertConfirmDialog();
        } else if (event.getPay_status() == PayCallBackTag.PAY_FAIL) {
            ToastHelper.showRedToast(getActivity(), "支付失败");
        } else if (event.getPay_status() == PayCallBackTag.PAY_CANCEL) {
            ToastHelper.showRedToast(getActivity(), "交易取消");
        } else if (event.getPay_status() == PayCallBackTag.PAY_EXCEPTION) {
            ToastHelper.showRedToast(getActivity(), "数据异常");
        } else if (event.getPay_status() == PayCallBackTag.PAY_PROGRESSING) {
            if (!isTimeout) {
                ToastHelper.showYellowToast(getActivity(), "支付结果确认中");
                alertConfirmDialog();
            }
        }

    }

    String tradeID = "+";
    String paymentID = "+";

    /**
     * 记录流水号
     */
    private void recordTradeID(PayOnlineResponse payOnlineResponse) {
        paymentID = payOnlineResponse.paymentID;
        tradeID = payOnlineResponse.tradeID;
    }

    final int REQUEST_PAY_STATUS = 1;
    final int GET_PAY_STATUS = 2;
    final int SUCCESS_SWITCH = 3;

    final int success_status = 20;
    final int fail_status = -1;
    SubmitDialog mProgressDialog;
    Timer timer_check_pay_status;

    Handler mHandler_payCallBack = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == REQUEST_PAY_STATUS) {
                requestPaidStatus();
            } else if (msg.what == GET_PAY_STATUS) {

                PayStatusResponse payStatusResponse = JSONUtils.fromJson((String) msg.obj, new TypeToken<PayStatusResponse>() {
                });

                if (payStatusResponse != null) {
                    if (!TextUtils.isEmpty(payStatusResponse.tradeStatus))
                        checkStatus(Integer.parseInt(payStatusResponse.tradeStatus));
                }

            } else if (msg.what == SUCCESS_SWITCH) {
                dismissProgressDialog();
                paySuccess();
            }
            return false;
        }
    });

    /**
     * 支付之后请求服务器是否收到钱弹框
     */
    private void alertConfirmDialog() {
        showProgressDialog();
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (mProgressDialog == null)
            mProgressDialog = new SubmitDialog(getActivity());
        mProgressDialog.setText("付款正在处理中，请销候...");

        /**
         * 每隔3秒请求一次 已支付接口
         * 判断后台是否接收到支付金额
         */
        timer_check_pay_status = new Timer();
        timer_check_pay_status.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what = REQUEST_PAY_STATUS;
                mHandler_payCallBack.sendMessage(message);
            }
        }, 1000, 3000);

        mProgressDialog.show();
    }

    private void showCallDialog(final boolean is_cant_pay) {

        String text_content;
        if (is_cant_pay) {
            text_content = "对不起，您来晚了，该车辆刚刚售罄";
        } else {
            text_content = "支付失败，请重新支付。如有疑问请点击下方电话号码联系客服。";
        }


        PayStatusDialog dialog = new PayStatusDialog(getActivity(), "支付失败", text_content) {

            @Override
            protected void doCall() {
                if (is_cant_pay) {
                    baseSharedPreferencesHelper.setOrderChange(true);
                    getActivity().finish();
                }
            }

            @Override
            protected void doAgain() {
                if (is_cant_pay) {
                    baseSharedPreferencesHelper.setOrderChange(true);
                    getActivity().finish();
                }
            }
        };

        if (is_cant_pay) {
            dialog.mCancelView.setVisibility(View.INVISIBLE);
            dialog.mOkView.setText("关闭");
        }

        dialog.show();
    }

    /**
     * 根据状态判断支付操作
     */
    private void checkStatus(int status) {
        if (status == success_status) {
            mProgressDialog.setText("付款完成，请完善信息！");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Message message = Message.obtain();
                    message.what = SUCCESS_SWITCH;
                    mHandler_payCallBack.sendMessage(message);
                }
            }, 2000);

        } else if (status == fail_status) {
            dismissProgressDialog();
            showCallDialog(false);
        }
    }

    /**
     * 关闭进度框 提示
     */
    private void dismissProgressDialog() {

        if (timer_check_pay_status != null) {
            timer_check_pay_status.cancel();
        }

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 请求已支付金额
     */
    private void requestPaidStatus() {

        apiClient.getPayStatus(paymentID, order_id, tradeID, new LoadingAnimResponseHandler(getActivity()) {
            @Override
            public void onSuccess(String response) {
                Message message = Message.obtain();
                message.what = GET_PAY_STATUS;
                message.obj = response;
                mHandler_payCallBack.sendMessage(message);
            }
        });
    }

    /**
     * 支付成功跳转页面
     */
    private void paySuccess() {
        depositSwitch();
        baseSharedPreferencesHelper.setOrderChange(true);
    }

    private void depositSwitch() {
        if (pay_type == PayConstant.JUST_COMPLETE_INFO || pay_type == PayConstant.PAY_FLOW_DEPOSIT_ONLINE || pay_type == PayConstant.NORMAL_HAD_CONTRACT_UNDERLINE) {
            OrderStep3CompleteInformationFragment fragment = new OrderStep3CompleteInformationFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ConstantValue.PAY_MAIN_ORDER_ID, order_id);
            fragment.setArguments(bundle);
            mListener.OnFragmentChange(ConstantValue.OS3_COMPLETE_INFO_FRAG, fragment);
        } else if (pay_type == PayConstant.PAY_ONLY_DEPOSIT_ONLINE || pay_type == PayConstant.PAY_ONLY_DEPOSIT_UNDERLINE) {
            Switchhelper.toContinueSale(pay_type, order_id, mCtx);
            getActivity().finish();
        }
    }

    @Override
    public String fragmentDescription() {
        return getResources().getString(R.string.fragment_description_deposit);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (menuDrawer.isShown()) {
            if (event.getY() < (v.getHeight() - 700)) {
                menuDrawer.closeMenu();
            }
        }
        return true;
    }
}
