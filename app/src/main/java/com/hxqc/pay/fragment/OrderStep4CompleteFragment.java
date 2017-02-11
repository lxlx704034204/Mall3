package com.hxqc.pay.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.fragment.FunctionAppCompatFragment;
import com.hxqc.mall.core.model.order.OrderModel;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.pay.activity.PayMainActivity;
import com.hxqc.pay.api.PayApiClient;
import com.hxqc.pay.event.EventUpLoadFile;
import com.hxqc.pay.util.ConstantValue;
import com.hxqc.pay.util.Switchhelper;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import hxqc.mall.R;


/**
 * A simple {@link Fragment} subclass.
 * <p/>
 * 完成界面
 */
public class OrderStep4CompleteFragment extends FunctionAppCompatFragment implements View.OnTouchListener {

    TextView mTVCallForHelp;
    TextView mReadOrders;

    Button mContinuePay;
    Button mUploadInfo;
    Button mFinishBtn;
    Button mOfflineFinishBtn;
//    Button mPickCar;

    //完成显示布局
    LinearLayout mViewOnLineAll;
    LinearLayout mViewOnLinePart;
    LinearLayout mViewOffLineAll;
    LinearLayout mViewInstallment;


    TextView mOfflineNotifyView;
    TextView mInstallmentNotifyView;
    //------------------------------

    PayApiClient apiClient;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        ((PayMainActivity) activity).setCompleteFrag(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((PayMainActivity) activity).setCompleteFrag(true);
    }

    private void showView(String msg) {

        String paymentType = "110";
        String paymentStatus = "110";

        final OrderModel orderModel = JSONUtils.fromJson(msg, new TypeToken< OrderModel >() {
        });

        if (orderModel != null) {
            paymentType = orderModel.paymentType;
            paymentStatus = orderModel.paymentStatus;
            DebugLog.i("complete", " - orderModel - " + orderModel.toString());
            //查看订单
            mReadOrders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new BaseSharedPreferencesHelper(getActivity()).setOrderChange(true);
                    ActivitySwitchBase.toOrderDetail(getActivity(), orderModel.orderID);
//                getActivity().finish();
                }
            });
        }

        //线上全款完成
        if (paymentType.equals("0") && paymentStatus.equals("2")) {

            mViewOnLineAll.setVisibility(View.VISIBLE);
            mViewOnLinePart.setVisibility(View.GONE);
            mViewOffLineAll.setVisibility(View.GONE);
            mViewInstallment.setVisibility(View.GONE);

        }//线上全款未完成
        else if ((paymentStatus.equals("1") || paymentStatus.equals("0")) && paymentType.equals("0")) {

            mViewOnLineAll.setVisibility(View.GONE);
            mViewOnLinePart.setVisibility(View.VISIBLE);
            mViewOffLineAll.setVisibility(View.GONE);
            mViewInstallment.setVisibility(View.GONE);

        }//分期
        else if (paymentType.equals("2")) {
            String text;
            if (orderModel == null) {
                text = String.format(getString(R.string.tv_installment_notice), "用户");
            } else {
                text = String.format(getString(R.string.tv_installment_notice), orderModel.userFullname);
            }
            mInstallmentNotifyView.setText(text);
            mViewOnLineAll.setVisibility(View.GONE);
            mViewOnLinePart.setVisibility(View.GONE);
            mViewOffLineAll.setVisibility(View.GONE);
            mViewInstallment.setVisibility(View.VISIBLE);

        }//线下
        else if (paymentType.equals("1")) {
            String text;
            if (orderModel == null) {
                text = String.format(getString(R.string.tv_underline_notice), "用户");
            } else {
                text = String.format(getString(R.string.tv_underline_notice), orderModel.userFullname);
            }
            mOfflineNotifyView.setText(text);
            mViewOnLineAll.setVisibility(View.GONE);
            mViewOnLinePart.setVisibility(View.GONE);
            mViewOffLineAll.setVisibility(View.VISIBLE);
            mViewInstallment.setVisibility(View.GONE);
        } else {
            mViewOnLineAll.setVisibility(View.GONE);
            mViewOnLinePart.setVisibility(View.GONE);
            mViewOffLineAll.setVisibility(View.GONE);
            mViewInstallment.setVisibility(View.GONE);
        }
    }

    public OrderStep4CompleteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_complete, container, false);
        view.setOnTouchListener(this);
        return view;
    }

    String order_id;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//TODO   无数据判断
        order_id = (String) getArguments().get(ConstantValue.PAY_MAIN_ORDER_ID);
        DebugLog.i("test_pay", order_id + "=-=-=-=step4...get");

        apiClient = new PayApiClient();

        mContinuePay = (Button) view.findViewById(R.id.btn_continue_pay);
        mUploadInfo = (Button) view.findViewById(R.id.btn_upload_info);
        mFinishBtn = (Button) view.findViewById(R.id.btn_upload_info_finish);
        mOfflineFinishBtn = (Button) view.findViewById(R.id.btn_offline_finish);
//        mPickCar = (Button) view.findViewById(R.id.btn_how_to_pickup_car);
        //------footer----------------
        mTVCallForHelp = (TextView) view.findViewById(R.id.tv_call_number);
        mReadOrders = (TextView) view.findViewById(R.id.tv_get_into_orders);
        //------layout view-------------------------------
        mViewOnLineAll = (LinearLayout) view.findViewById(R.id.ll_online_all);
        mViewOnLinePart = (LinearLayout) view.findViewById(R.id.ll_online_part);
        mViewOffLineAll = (LinearLayout) view.findViewById(R.id.ll_offline_all);
        mViewInstallment = (LinearLayout) view.findViewById(R.id.ll_installment);

        mOfflineNotifyView = (TextView) view.findViewById(R.id.tv_pay_offline);
        mInstallmentNotifyView = (TextView) view.findViewById(R.id.tv_pay_installment);

        //-------------------------
        apiClient.orderDetail(order_id, new BaseMallJsonHttpResponseHandler(getActivity()) {
            @Override
            public void onSuccess(String response) {
                showView(response);
            }
        });

        //继续付款
        mContinuePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switchhelper.toContinuePay(order_id, getActivity());
//                getActivity().finish();
            }
        });

        //上传分期资料
        mUploadInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Switchhelper.toUploadInfo(order_id, getActivity(), ConstantValue.v_from_step_4);
//                getActivity().finish();
            }
        });
//        mUploadInfo.setVisibility(View.GONE);


        //分期付款 完成按钮
        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        //预付定金  完成按钮
        mOfflineFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mTVCallForHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtherUtil.callHXService(getActivity());
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 接收 上传资料之后 的回传 判断是否试点的保存
     * @param event 返回事件
     */
    @Subscribe
    public void onEventMainThread(EventUpLoadFile event) {

        /**
         * todo 如果进入上传 贷款资料 点击保存    按钮变为修改资料     保存按钮显示
         *          如果 点击返回 保存按钮不显示 贷款资料 不变
         */
//            mFinishBtn


        //资料保存
        if (event.operateStatus == EventUpLoadFile.has_saved){
            mFinishBtn.setVisibility(View.VISIBLE);
            //修改上传资料按钮样式
            // <item name="android:textColor">@color/text_color_orange</item>
//            <item name="android:background">@drawable/btn_border</item>
            mUploadInfo.setText("修改贷款资料");
            mUploadInfo.setTextColor(getResources().getColor(R.color.text_color_orange));
            mUploadInfo.setBackgroundResource(R.drawable.btn_border);

        }
        //未做操作
//        else if (event.operateStatus == EventUpLoadFile.do_nothing){
//
//        }

    }

    @Override
    public String fragmentDescription() {
        return getResources().getString(R.string.fragment_description_complete);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
