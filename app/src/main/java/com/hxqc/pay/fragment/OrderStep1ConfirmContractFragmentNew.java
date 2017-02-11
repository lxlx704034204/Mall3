package com.hxqc.pay.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.fragment.FunctionAppCompatFragment;
import com.hxqc.mall.core.model.OrderPayRequest;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.pay.api.PayApiClient;
import com.hxqc.pay.inter.OnFragmentChangeListener;
import com.hxqc.pay.model.OrderIDResponse;
import com.hxqc.pay.util.ConstantValue;
import com.hxqc.util.JSONUtils;

import hxqc.mall.R;


/**
 * Author: wanghao
 * Date: 2015-10-14
 * FIXME   新的生成合同页面
 * Todo
 */
public class OrderStep1ConfirmContractFragmentNew extends FunctionAppCompatFragment implements View.OnTouchListener {

    OnFragmentChangeListener mListener;
    WebView mGetFormalContract;
    PayApiClient apiClient;
    OrderPayRequest requestOrder;
    TextView mConfirmView;

    //正常线上付款流程
    final int NOMARL_ONLINE_CREATE_ORDER = 0;
    //正常线下分期付款流程
    final int NOMARL_UNDERLINE_DEPOSIT_ORDER = 1;
    //支付流程类型
    int pay_type = PayConstant.PAY_FLOW_NORMAL_ONLINE;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        initListener(activity);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initListener(activity);
    }

    private void initListener(Context activity) {
        try {
            mListener = (OnFragmentChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "--os1ccf");
        }
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

        View view = inflater.inflate(R.layout.fragment_confirm_contract, container, false);
        view.setOnTouchListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGetFormalContract = (WebView) view.findViewById(R.id.wv_get_contract_confirm_data);
        mConfirmView = (TextView) view.findViewById(R.id.tv_confirmcontract);
        apiClient = new PayApiClient();

//获取上级数据-----------------------------------------------------------------------------------------------------

        pay_type = (int) getArguments().get(PayConstant.PAY_STATUS_FLAG);
        requestOrder = (OrderPayRequest) getArguments().get(ConstantValue.ORDER_PAY_REQUEST);
        //显示合同---------------------------------------------------------------------------------
        initContract();
        showContract();

        if (requestOrder != null) {
            if (requestOrder.isSeckill.equals("0")) {
                mConfirmView.setText("同意上述条款，去付款");
            }
        }
//确认合同-------------------------------------------------------------------------------------------------------
        //noinspection ConstantConditions
        mConfirmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                正常线上全款下单流程
                if (pay_type == PayConstant.PAY_FLOW_NORMAL_ONLINE) {
                    normalOrderRequest(NOMARL_ONLINE_CREATE_ORDER);
                }//正常线下  分期订金流程
                else if (pay_type == PayConstant.PAY_FLOW_DEPOSIT_ONLINE) {
                    normalOrderRequest(NOMARL_UNDERLINE_DEPOSIT_ORDER);
                }//特卖线上,线下
                else if (pay_type == PayConstant.PAY_ONLY_DEPOSIT_ONLINE || pay_type == PayConstant.PAY_ONLY_DEPOSIT_UNDERLINE) {
                    toSkillCommit();
                }
            }
        });
    }

    //提交特卖
    private void toSkillCommit() {
        apiClient.updateSeckillOrder(requestOrder, new LoadingAnimResponseHandler(getActivity()) {
            @Override
            public void onSuccess(String response) {
                Bundle bundle = new Bundle();
                bundle.putInt(PayConstant.PAY_STATUS_FLAG, pay_type);
                bundle.putString(ConstantValue.PAY_MAIN_ORDER_ID, requestOrder.orderID);
                if (pay_type == PayConstant.PAY_ONLY_DEPOSIT_ONLINE) {
                    OrderStep2PayOnlineFragment fragment = new OrderStep2PayOnlineFragment();
                    fragment.setArguments(bundle);
                    mListener.OnFragmentChange(ConstantValue.OS2_PAY_ONLINE_FRAG, fragment);
                } else if (pay_type == PayConstant.PAY_ONLY_DEPOSIT_UNDERLINE) {
                    FinishDepositFragment fragment = new FinishDepositFragment();
                    fragment.setArguments(bundle);
                    mListener.OnFragmentChange(ConstantValue.OS2_PAY_ONLINE_FRAG, fragment);
                }
            }
        });
    }

    /**
     * 初始化合同
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initContract() {

        mGetFormalContract.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        mGetFormalContract.getSettings().setJavaScriptEnabled(true);
        mGetFormalContract.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    /**
     * 显示正式合同
     */
    private void showContract() {
        apiClient.getFormalContract(requestOrder, new LoadingAnimResponseHandler(getActivity()) {
            @Override
            public void onSuccess(String response) {
//                    mGetFormalContract.loadData(response, "text/html", "UTF-8");
                if (!TextUtils.isEmpty(response))
                    mGetFormalContract.loadDataWithBaseURL("", response, "text/html", "UTF-8", "");
            }
        });
    }

    /**
     * 普通订单下单并跳转
     *
     * @param flag
     *         订单付款类型
     */
    private void normalOrderRequest(final int flag) {
        apiClient.createOrderNormal(requestOrder, new LoadingAnimResponseHandler(getActivity()) {
            @Override
            public void onSuccess(String response) {
                Message msg = Message.obtain();
                msg.what = flag;
                msg.obj = response;
                createApiData(msg);
            }
        });
    }

    /**
     * @param msg
     */
    private void createApiData(Message msg) {
        Bundle bundle = new Bundle();
        String orderIDR = (String) msg.obj;
        OrderIDResponse orderIDResponse = JSONUtils.fromJson(orderIDR, new TypeToken< OrderIDResponse >() {
        });
        String order_id = orderIDResponse.orderID;
        bundle.putString(ConstantValue.PAY_MAIN_ORDER_ID, order_id);
        if (msg.what == NOMARL_ONLINE_CREATE_ORDER) {
            OrderStep2PayOnlineFragment fragment = new OrderStep2PayOnlineFragment();
            bundle.putInt(PayConstant.PAY_STATUS_FLAG, pay_type);
            fragment.setArguments(bundle);
            mListener.OnFragmentChange(ConstantValue.OS2_PAY_ONLINE_FRAG, fragment);
        } else if (msg.what == NOMARL_UNDERLINE_DEPOSIT_ORDER) {
            OrderStep2DepositFragment fragment = new OrderStep2DepositFragment();
            bundle.putInt(PayConstant.PAY_STATUS_FLAG, pay_type);
            fragment.setArguments(bundle);
            mListener.OnFragmentChange(ConstantValue.OS2_DEPOSIT_FRAG, fragment);
        }
    }


    @Override
    public String fragmentDescription() {
        return getResources().getString(R.string.fragment_description_complete_info);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
