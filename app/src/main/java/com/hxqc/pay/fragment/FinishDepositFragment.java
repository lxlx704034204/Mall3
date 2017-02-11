package com.hxqc.pay.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.fragment.FunctionAppCompatFragment;
import com.hxqc.pay.api.PayApiClient;
import com.hxqc.pay.inter.OnFragmentChangeListener;
import com.hxqc.pay.model.PaidAmountModel;
import com.hxqc.pay.util.ConstantValue;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import hxqc.mall.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FinishDepositFragment extends FunctionAppCompatFragment {
    OnFragmentChangeListener mListener;

    TextView showMoney;
    Button toCompleteInfo;
    TextView mShowOrders;

    String order_id = "+";
    PayApiClient apiClient;
    public FinishDepositFragment() {
        // Required empty public constructor
    }

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finish_deposit, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiClient = new PayApiClient();
        order_id = (String) getArguments().get(ConstantValue.PAY_MAIN_ORDER_ID);

        apiClient.getPayHeadBarData(order_id, new LoadingAnimResponseHandler(getActivity()) {
            @Override
            public void onSuccess(String response) {
                PaidAmountModel paidAmountModel = JSONUtils.fromJson(response, new TypeToken< PaidAmountModel >() {
                });
                showMoney.setText(String.format("¥%s", paidAmountModel.paid));
            }
        });

        showMoney = (TextView) view.findViewById(R.id.tv_money_);
        toCompleteInfo = (Button) view.findViewById(R.id.btn_to_finish_info);
        mShowOrders = (TextView) view.findViewById(R.id.tv_show_order);

        toCompleteInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderStep3CompleteInformationFragment fragment = new OrderStep3CompleteInformationFragment();
                Bundle bundle = new Bundle();
                bundle.putString(ConstantValue.PAY_MAIN_ORDER_ID, order_id);
                DebugLog.i("seckill", order_id + "=-=-=-=step2... put");
                fragment.setArguments(bundle);
                mListener.OnFragmentChange(ConstantValue.OS3_COMPLETE_INFO_FRAG, fragment);
            }
        });


        mShowOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //订单列表
            }
        });

    }

    @Override
    public String fragmentDescription() {
        return getResources().getString(R.string.fragment_description_finish_deposit);
    }
}
