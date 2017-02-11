package com.hxqc.mall.payment.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.core.R;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.dialog.NoCancelDialog;
import com.hxqc.mall.core.views.dialog.NormalDialog;
import com.hxqc.mall.payment.model.PaymentMethod;
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
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.ListViewNoSlide;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cz.msebera.android.httpclient.Header;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * 说明:支付方式的选择列表，调用界面需要对EventBus进行注册和销毁
 *
 * @author: 吕飞
 * @since: 2016-03-14
 * Copyright:恒信汽车电子商务有限公司
 */
public class PaymentTypeChoice extends LinearLayout implements AdapterView.OnItemClickListener, View.OnClickListener {
    ListViewNoSlide mPayListView;
    TextView mAmountView;
    Button mConfirmView;
    TextView mPayHintView;
    PaymentListener mPaymentListener;
    ArrayList<PaymentMethod> mPaymentMethods;//支付方式数据
    QuickAdapter<PaymentMethod> mPaymentAdapter;//支付方式列表适配器
    String mPaymentID;//支付方式id

    String mBalance = "";//可用余额
    int mBalancePosition;
    String mAmount;

    public PaymentTypeChoice(Context context) {
        super(context);
    }

    public PaymentTypeChoice(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public String getPaymentID() {
        return mPaymentID;
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_payment_type_choice, this);
        mPayListView = (ListViewNoSlide) findViewById(R.id.pay_list);
        mAmountView = (TextView) findViewById(R.id.amount);
        mConfirmView = (Button) findViewById(R.id.confirm);
        mPayHintView = (TextView) findViewById(R.id.pay_hint);
        mConfirmView.setOnClickListener(this);
        mPayListView.setOnItemClickListener(this);
    }

    public void setAmountText(String amountText) {
        mAmountView.setText(amountText);
    }

    /**
     * 数据填充
     *
     * @param paymentMethods 支付方法
     * @param amount         支付金额
     */
    public void fillData(ArrayList<PaymentMethod> paymentMethods, String amount) {
        mPaymentMethods = paymentMethods;
        mAmount = amount;
        if (containBalance()) {
            getBalance();
        } else {
            showList();
        }
    }

    //接收返回结果显示
    @Subscribe
    public void onEventMainThread(EventGetSuccessModel event) {
        if (event.getPay_status() == PayCallBackTag.PAY_SUCCESS) {
            finishPay();
        } else if (event.getPay_status() == PayCallBackTag.PAY_FAIL) {
            ToastHelper.showRedToast(getContext(), "支付失败");
        } else if (event.getPay_status() == PayCallBackTag.PAY_CANCEL) {
            ToastHelper.showRedToast(getContext(), "交易取消");
        } else if (event.getPay_status() == PayCallBackTag.PAY_EXCEPTION) {
            ToastHelper.showRedToast(getContext(), "数据异常");
        } else if (event.getPay_status() == PayCallBackTag.PAY_PROGRESSING) {
            ToastHelper.showYellowToast(getContext(), "支付结果确认中");
        }
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(mPaymentID)) {
            ToastHelper.showYellowToast(getContext(), "其选择支付方式");
        } else {
            if (mPaymentID.equals(PayConstant.WEIXIN)) {
                try {
                    IWXAPI msgApi = WXAPIFactory.createWXAPI(getContext(), PayKeyConstants.getWxAppId());
                    if (!(msgApi.isWXAppInstalled() && msgApi.isWXAppSupportAPI())) {
                        ToastHelper.showYellowToast(getContext(), "亲，使用微信支付需要先安装微信客户端哦。");
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            switch (mPaymentID) {
                case PayConstant.BALANCE:
                    if (UserInfoHelper.getInstance().hasPayPassword(getContext())) {
                            toPayPwdViewManager(v);
                    } else {
                        new NormalDialog(getContext(), "提示：", "为保证账户安全，请先设置支付密码", "去设置") {
                            @Override
                            protected void doNext() {
                                ActivitySwitchBase.toRealNameAuthentication(getContext());
                            }
                        }.show();
                    }
                    break;
                case PayConstant.INSHOP:
                    mPaymentListener.offlinePay();
                    break;
                default:
                    mPaymentListener.pay();
                    break;
            }
        }
    }

    /**
     * 余额支付
     * @param v
     */
    private void toPayPwdViewManager(View v) {
        new PayPwdViewManager(getContext()).showBalanceNumberKeyboard(v, mAmount, new PayPwdViewManager.OnPwdFinishListener() {
            @Override
            public void pwdText(String pwd) {
                mPaymentListener.balancePay(pwd);
            }

            @Override
            public void inputCancel() {

            }

        });
    }

    /**
     * 支付
     *
     * @param payResponse 支付接口返回的数据
     */
    public void toPay(String payResponse) {
        PayOnlineResponse payOnlineResponse = JSONUtils.fromJson(payResponse, PayOnlineResponse.class);
        if (payOnlineResponse == null) {
            ToastHelper.showRedToast(getContext(), "请求支付失败");
        } else {
            PayMethodManager payMethodManager = new PayMethodManager((Activity) getContext(), mAmount, payOnlineResponse);
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
                ToastHelper.showRedToast(getContext(), "支付校验失败");
                e.printStackTrace();
            }
        }
    }

    private void showList() {
        for (int i = 0; i < mPaymentMethods.size(); i++) {
            if (mPaymentMethods.get(i).isValid(Float.parseFloat(mAmount), mBalance)) {
                mPaymentMethods.get(i).selected = true;
                mPaymentID = mPaymentMethods.get(i).paymentID;
                break;
            }
        }
        for (int i = 0; i < mPaymentMethods.size(); i++) {
            if (mPaymentMethods.get(i).paymentID.equals(PayConstant.INSHOP)) {
                mPayHintView.setVisibility(VISIBLE);
                break;
            }
        }
        mPaymentAdapter = new QuickAdapter<PaymentMethod>(getContext(), R.layout.item_pay_subscription_pay_type, mPaymentMethods) {
            @Override
            protected void convert(BaseAdapterHelper helper, PaymentMethod item) {
                ImageUtil.setImageSquare(getContext(), (ImageView) helper.getView(R.id.pay_icon), item.thumb);
                helper.setText(R.id.payment_type, item.title);
                if (item.selected) {
                    helper.setImageResource(R.id.select_payment, R.drawable.radiobutton_selected);
                } else {
                    helper.setImageResource(R.id.select_payment, R.drawable.radiobutton_normal);
                }
                helper.setVisible(R.id.balance, item.paymentID.equals(PayConstant.BALANCE));
                if (item.paymentID.equals(PayConstant.BALANCE)) {
                    helper.setText(R.id.balance, "余额：" + mBalance + "元");
                }
                if (!item.isValid(Float.parseFloat(mAmount), mBalance)) {
                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation(0);
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                    ((ImageView) helper.getView(R.id.pay_icon)).setColorFilter(filter);
                    ((TextView) helper.getView(R.id.payment_type)).setTextColor(getResources().getColor(R.color.divider));
                    ((TextView) helper.getView(R.id.balance)).setTextColor(getResources().getColor(R.color.divider));
                    helper.setImageResource(R.id.select_payment, R.drawable.radiobutton_normal_disabled);
                } else {
                    ColorMatrix matrix = new ColorMatrix();
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                    ((ImageView) helper.getView(R.id.pay_icon)).setColorFilter(filter);
                    ((TextView) helper.getView(R.id.payment_type)).setTextColor(getResources().getColor(R.color.text_color_title));
                }
            }
        };
        mPayListView.setAdapter(mPaymentAdapter);
    }

    //获取可用余额
    private void getBalance() {
        new UserApiClient().getWalletData(new LoadingAnimResponseHandler(getContext()) {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showFailDialog();
            }

            @Override
            public void onSuccess(String response) {
                Wallet wallet = JSONUtils.fromJson(response, Wallet.class);
                if (wallet != null && !TextUtils.isEmpty(wallet.balance)) {
                    mBalance = wallet.balance;
                    showList();
                } else {
                    showFailDialog();
                }
            }
        });
    }

    private void showFailDialog() {
        new NoCancelDialog(getContext(), "请求支付方式失败，请重试") {
            @Override
            protected void doNext() {
                getBalance();
            }
        };
    }

    //判断是否可以余额支付
    private boolean containBalance() {
        for (int i = 0; i < mPaymentMethods.size(); i++) {
            if (mPaymentMethods.get(i).paymentID.equals(PayConstant.BALANCE)) {
                mBalancePosition = i;
                return true;
            }
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mPaymentMethods.get(position).isValid(Float.parseFloat(mAmount), mBalance)) {
            mPaymentID = mPaymentMethods.get(position).paymentID;
            for (int i = 0; i < mPaymentMethods.size(); i++) {
                mPaymentMethods.get(i).selected = false;
            }
            mPaymentMethods.get(position).selected = true;
            mPaymentAdapter.notifyDataSetChanged();
        }
    }

    public void setPaymentListener(PaymentListener paymentListener) {
        mPaymentListener = paymentListener;
    }

    public void finishPay() {
        ToastHelper.showGreenToast(getContext(), "支付成功");
        mPaymentListener.toFinishPay();
        ((Activity) getContext()).finish();
    }

    public interface PaymentListener {
        //调余额支付接口
        void balancePay(String pwd);

        //调支付接口
        void pay();

        //去支付完成
        void toFinishPay();

        //线下支付
        void offlinePay();
    }
}
