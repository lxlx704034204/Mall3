package com.hxqc.pay.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.fragment.FunctionAppCompatFragment;
import com.hxqc.mall.core.model.DeliveryAddress;
import com.hxqc.mall.core.model.auto.PickupPointT;
import com.hxqc.mall.core.model.order.OrderModel;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.pay.api.PayApiClient;
import com.hxqc.pay.inter.OnFragmentChangeListener;
import com.hxqc.pay.model.CompleteInfoModelRequest;
import com.hxqc.pay.model.OrderIDResponse;
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
 * 完善信息
 */
public class OrderStep3CompleteInformationFragment extends FunctionAppCompatFragment implements View.OnClickListener, View.OnTouchListener {

    OnFragmentChangeListener mListener;


    //头显示填写的收货地址
    LinearLayout mChangeAddress;
    TextView consigneeName;
    TextView consigneePhone;
    TextView consigneeArea;
    TextView consigneeAddress;


    TextView invoice_title_view;//发票抬头
    TextView idView;//证件id


    //-------------------
    Button mSave;

    RelativeLayout mps;
    LinearLayout mhd;
    //    CheckBox mHomeDelivery;
    CheckBox mPickSelf;

    TextView mChoosePickAddress;

    String orderID = "";

    PayApiClient apiClient;
    PickupPointT eventPickup;
    CompleteInfoModelRequest completeInfoModelRequest;

    String pid = "";
    String cid = "";
    String province = "";
    String itemID = "";

    private void saveInfo(String msg) {
        new BaseSharedPreferencesHelper(getActivity()).setOrderChange(true);
        OrderIDResponse response = JSONUtils.fromJson(msg, new TypeToken< OrderIDResponse >() {
        });
        OrderStep4CompleteFragment fragment = new OrderStep4CompleteFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ConstantValue.PAY_MAIN_ORDER_ID, response.orderID);

        DebugLog.i("test_pay", response.orderID + "=-=-=-=step3...put" + msg);
        fragment.setArguments(bundle);

        mListener.OnFragmentChange(ConstantValue.OS4_COMPLETE_FRAG, fragment);
    }


    public OrderStep3CompleteInformationFragment() {
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
                    + "--os3cif");
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
        View view = inflater.inflate(R.layout.fragment_complete_information, container, false);
        view.setOnTouchListener(this);
        return view;
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

    String getCheck = "s";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //拿到上级参数
        apiClient = new PayApiClient();
        orderID = (String) getArguments().get(ConstantValue.PAY_MAIN_ORDER_ID);
        DebugLog.i("test_pay", orderID + "=-=-=-=step3...get");
        completeInfoModelRequest = new CompleteInfoModelRequest();
        //-----------------------------------------------------------
        mChangeAddress = (LinearLayout) view.findViewById(R.id.ll_change_address);
        consigneeName = (TextView) view.findViewById(R.id.tv_consignee);
        consigneePhone = (TextView) view.findViewById(R.id.tv_consignee_phone);
        consigneeArea = (TextView) view.findViewById(R.id.tv_consignee_city_address);
        consigneeAddress = (TextView) view.findViewById(R.id.tv_consignee_detail_address);


        invoice_title_view = (TextView) view.findViewById(R.id.tv_title);
        idView = (TextView) view.findViewById(R.id.tv_id);

        //选择收货地址
        mChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("DeliveryAddress");
                intent.putExtra(PayConstant.CHOOSE_ADDRESS_FLAG, PayConstant.CHOOSE_ADDRESS);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivityForResult(intent, 117);
                startActivityForResult(intent, 117);
            }
        });

        mSave = (Button) view.findViewById(R.id.btn_save);
        mChoosePickAddress = (TextView) view.findViewById(R.id.tv_choose_pick_address);
        mhd = (LinearLayout) view.findViewById(R.id.ll_home_delivery);
        mhd.setVisibility(View.GONE);

        mps = (RelativeLayout) view.findViewById(R.id.rl_pickup_point);
//        mHomeDelivery = (CheckBox) view.findViewById(R.id.cb_home_delivery);
        mPickSelf = (CheckBox) view.findViewById(R.id.cb_pick_address);
        mPickSelf.setEnabled(false);
//        mPickSelf.setBackgroundColor(getResources().getColor(R.color.cursor_orange));
//        mHomeDelivery.setBackgroundColor(getResources().getColor(R.color.cursor_orange));
//        mSave.setEnabled(false);
        apiClient.orderDetail(orderID, new LoadingAnimResponseHandler(getActivity()) {
            @Override
            public void onSuccess(String response) {
                initAddressData(response);
//                checkStatus_pickup();
            }
        });

//        mPickSelf.setClickable(false);
//        mHomeDelivery.setSelected(false);
//        mHomeDelivery.setFocusable(false);

//        mPickSelf.setOncheckListener(new CheckBox.OnCheckListener() {
//            @Override
//            public void onCheck(boolean check) {
//
//            }
//        });

//        mHomeDelivery.setOncheckListener(new CheckBox.OnCheckListener() {
//            @Override
//            public void onCheck(boolean check) {
//                homeDelivery();
//            }
//        });

        completeInfoModelRequest.orderID = orderID;
        mChoosePickAddress.setOnClickListener(this);
        mSave.setOnClickListener(this);
        mhd.setOnClickListener(this);
        mps.setOnClickListener(this);
    }

    //接收activity 选择的数据
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        DebugLog.d("address", requestCode + "-" + resultCode);
        if (resultCode == 118 && requestCode == 117) {
            if (data.getParcelableExtra("ADDRESS") != null) {
                DeliveryAddress dAdress = data.getParcelableExtra("ADDRESS");
                DebugLog.d("address", dAdress.toString());
                consigneeName.setText(dAdress.consignee);
                consigneePhone.setText(dAdress.phone);
                consigneeArea.setText(dAdress.province + " " + dAdress.city + " " + dAdress.district);
                consigneeAddress.setText(dAdress.detailedAddress);

                completeInfoModelRequest.fullname = dAdress.consignee;
                completeInfoModelRequest.cellphone = dAdress.phone;
                completeInfoModelRequest.province = dAdress.province;
                completeInfoModelRequest.city = dAdress.city;
                completeInfoModelRequest.district = dAdress.district;
                completeInfoModelRequest.address = dAdress.detailedAddress;
            }
        }
    }

    private void initAddressData(String response) {
        OrderModel orderModel = JSONUtils.fromJson(response, new TypeToken< OrderModel >() {
        });
        itemID = orderModel.itemID;
        province = orderModel.province;
        pid = orderModel.provinceID;
        cid = orderModel.cityID;
        DebugLog.i("OrderStep3C", orderModel.toString());
        consigneeName.setText(orderModel.userFullname);
        consigneePhone.setText(orderModel.userPhoneNumber);
        consigneeArea.setText(orderModel.province + " " + orderModel.city + " " + orderModel.district);
        consigneeAddress.setText(orderModel.userAddress);

        idView.setText(orderModel.userIdentifier);
        invoice_title_view.setText(orderModel.userFullname);
    }

    boolean clickAble = true;

    @Override
    public void onClick(View v) {
        if (clickAble) {
            clickAble = false;
            clickLogic(v);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    clickAble = true;
                }
            }, 500);
        }
    }

    /**
     * 点击操作逻辑
     *
     * @param v
     *         点击的view
     */
    private void clickLogic(View v) {
        if (v.getId() == R.id.btn_save) {
            if (getCheck.equals("s")) {
                ToastHelper.showYellowToast(getActivity(), " 请选择自提点 ");
            } else {

                if (getCheck.equals("自提点")) {
                    completeInfoModelRequest.expressType = 1 + "";
                    if (eventPickup == null) {
                        ToastHelper.showYellowToast(getActivity(), "请选择自提点");
                    } else {
                        completeInfoModelRequest.pickupPoint = eventPickup.id ;
                        apiClient.fillInformation(completeInfoModelRequest, new LoadingAnimResponseHandler(getActivity()) {
                            @Override
                            public void onSuccess(String response) {
                                saveInfo(response);
                            }
                        });
                    }
                } else {
                    completeInfoModelRequest.expressType = 2 + "";
//                    Toast.makeText(getActivity(), getCheck, Toast.LENGTH_SHORT).show();
                    apiClient.fillInformation(completeInfoModelRequest, new LoadingAnimResponseHandler(getActivity()) {
                        @Override
                        public void onSuccess(String response) {
                            saveInfo(response);
                        }
                    });
                }
            }
        } else if (v.getId() == R.id.tv_choose_pick_address) {
//            Toast.makeText(getActivity(), "c+++:" + pid + "-" + cid + ":++++c", Toast.LENGTH_SHORT).show();
            Switchhelper.toPickupPoint(orderID,itemID,province, pid, cid, getActivity());
        } else if (v.getId() == R.id.ll_home_delivery) {
//            homeDelivery();
        } else if (v.getId() == R.id.rl_pickup_point) {
//            checkStatus_pickup();
            Switchhelper.toPickupPoint(orderID,itemID,province, pid, cid, getActivity());
        }
    }

    //选择送货上门
//    private void homeDelivery() {
//        mHomeDelivery.setChecked(true);
//        mPickSelf.setChecked(false);
//        getCheck = "送货上门";
//        mSave.setEnabled(true);
//        if (!"请选择自提点".equals(mChoosePickAddress.getText().toString().trim()))
//            mChoosePickAddress.setText("请选择自提点");
//    }

    //自提点自提
    private void checkStatus_pickup() {
        mPickSelf.setChecked(true);
//        mHomeDelivery.setChecked(false);
        getCheck = "自提点";
        mSave.setEnabled(true);
    }

    //接收 自提点的店名
    @Subscribe
    public void onEventMainThread(PickupPointT event) {
        eventPickup = event;
        if (event != null) {
            DebugLog.i("test_pick",event.toString());
            mChoosePickAddress.setText(event.name);
            checkStatus_pickup();
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
