package com.hxqc.pay.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.fragment.FunctionAppCompatFragment;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.paymethodlibrary.manager.PayMethodManager;
import com.hxqc.mall.paymethodlibrary.model.EventGetSuccessModel;
import com.hxqc.mall.paymethodlibrary.model.PayOnlineResponse;
import com.hxqc.mall.paymethodlibrary.util.PayCallBackTag;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.pay.adapter.BankListAdapter;
import com.hxqc.pay.adapter.PayMethodAdapterHolder;
import com.hxqc.pay.api.PayApiClient;
import com.hxqc.pay.inter.OnFragmentChangeListener;
import com.hxqc.pay.model.PaidAmountModel;
import com.hxqc.pay.model.PayOnlineRequest;
import com.hxqc.pay.model.PayPartBackDataModel;
import com.hxqc.pay.model.PaymentMethod;
import com.hxqc.pay.util.ConstantValue;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import net.simonvt.menudrawer.MenuDrawer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import hxqc.mall.R;


/**
 * A simple {@link Fragment} subclass.
 * 第二步 付款列表
 */
public class OrderStep2PayOnlineFragment extends FunctionAppCompatFragment implements View.OnTouchListener {


    OnFragmentChangeListener mListener;

    //---支付方式列表
    ListView mBankList;
    BankListAdapter mBankAdapter;
    //---支付方式添加删除列表-----------------
    RecyclerView mPayMethodRecyclerView;
    PayMethodAdapterHolder mPayMethodAdapter;
    //---滑出菜单
    MenuDrawer menuDrawer;
    //---支付金额 提示头
    TextView mTotalAmount;
    TextView mAlreadyPaid;
    TextView mStillNeedToPay;

    PayApiClient apiClient;
    PaymentMethod pay_method;
    String bank_id = "+";
    //-----------------------------------------------------------------------------------------------------------------------------
    String unpaid = "+";
    String order_id = "+";

    //记录的本地缓存已支付 data--------------------------------------
    ArrayList< PayPartBackDataModel > cacheListData = new ArrayList<>();

    //-----------------------------------------------------------------------------------------------------------------------------

    //支付流程类型
    int pay_type = PayConstant.PAY_FLOW_NORMAL_ONLINE;
    String bank_title = "+";
    String money_ = "+";

    int pay_count = 0;

    final int GET_PAYMENT_LIST = 0;
    final int GET_PAID_AMOUNT = 1;
    final int GO_TO_PAY = 2;

    BaseSharedPreferencesHelper baseSharedPreferencesHelper;

    private void createApiData(int type, String msg) {
        if (type == GET_PAYMENT_LIST) {

            DebugLog.i("banklist", msg);
            ArrayList< PaymentMethod > paymentList = JSONUtils.fromJson(msg, new TypeToken< ArrayList< PaymentMethod > >() {
            });
            mBankAdapter = new BankListAdapter(paymentList, getActivity(),"");
            mBankList.setAdapter(mBankAdapter);
        } else if (type == GET_PAID_AMOUNT) {
            DebugLog.i("pay_online", msg);
            PaidAmountModel paidAmountModel = JSONUtils.fromJson(msg, new TypeToken< PaidAmountModel >() {
            });
            mTotalAmount.setText(OtherUtil.stringToMoney(paidAmountModel.amount));
            mAlreadyPaid.setText(OtherUtil.stringToMoney(paidAmountModel.paid));
            mStillNeedToPay.setText(OtherUtil.stringToMoney(paidAmountModel.unpaid));
            payList(paidAmountModel);

        } else if (type == GO_TO_PAY) {
            DebugLog.i("pay_t", msg);
            PayOnlineResponse payOnlineResponse = JSONUtils.fromJson(msg, new TypeToken< PayOnlineResponse >() {
            });

            if (payOnlineResponse == null) {
                ToastHelper.showRedToast(getActivity(), "请求支付失败");
            } else {
                try {
                    new PayMethodManager(getActivity(), money_, payOnlineResponse).paySwitch();
                } catch (Exception e) {
                    ToastHelper.showRedToast(getActivity(), "支付校验失败");
                }
            }

        }
    }

    public OrderStep2PayOnlineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        baseSharedPreferencesHelper = new BaseSharedPreferencesHelper(getActivity());
        DebugLog.i("life", "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
                    + "--os2pof");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        baseSharedPreferencesHelper = null;
        if (apiClient != null)
            apiClient = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DebugLog.i("life", "onCreateView");

        View view = inflater.inflate(R.layout.fragment_pay_online_drawer, container, false);
        view.setOnTouchListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//获取传递参数
        DebugLog.i("life", "onViewCreated");
        pay_type = (int) getArguments().get(PayConstant.PAY_STATUS_FLAG);
        order_id = (String) getArguments().get(ConstantValue.PAY_MAIN_ORDER_ID);

        cacheListData.add(new PayPartBackDataModel(0));
        cacheListData.add(new PayPartBackDataModel(0));
//----------------------------------------------------------------------------------
        apiClient = new PayApiClient();
        //金额提示头
        mTotalAmount = (TextView) view.findViewById(R.id.atv_total_money);
        mAlreadyPaid = (TextView) view.findViewById(R.id.atv_already_pay);
        mStillNeedToPay = (TextView) view.findViewById(R.id.atv_wait_to_pay);
        //滑出菜单
        menuDrawer = (MenuDrawer) view.findViewById(R.id.drawer_online);
        menuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
        //   选择银行列表
        mBankList = (ListView) view.findViewById(R.id.lv_bank);
        mPayMethodRecyclerView = (RecyclerView) view.findViewById(R.id.rv_pay_method_list);
        apiClient.listPayment(new BaseMallJsonHttpResponseHandler(getActivity()) {
            @Override
            public void onSuccess(String response) {
                createApiData(GET_PAYMENT_LIST, response);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //刷新的抬头列表
        apiClient.getPayHeadBarData(order_id, new LoadingAnimResponseHandler(getActivity()) {
            @Override
            public void onSuccess(String response) {
                createApiData(GET_PAID_AMOUNT, response);
            }
        });
        DebugLog.i("life", "onResume");

        mPayMethodRecyclerView.setHasFixedSize(true);
        mPayMethodRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPayMethodRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public String fragmentDescription() {
        return getResources().getString(R.string.fragment_description_pay_online);
    }

    private void payList(PaidAmountModel model) {

        mPayMethodAdapter = new PayMethodAdapterHolder(cacheListData, pay_type, model);
        mPayMethodRecyclerView.setAdapter(mPayMethodAdapter);
        mPayMethodAdapter.setOnItemClickListener(new PayMethodAdapterHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int type, PayOnlineRequest payOnlineRequest) {

                if (type == ConstantValue.PAY_METHOD_ITEM_ADD) {
                    addItem(position);
                } else if (type == ConstantValue.PAY_METHOD_ITEM_DEL) {
                    removeItem(position);
                } else if (type == ConstantValue.COMPLETE_INFO) {
                    if (pay_type == PayConstant.PAY_ONLY_ONLINE_PAID) {

                        new BaseSharedPreferencesHelper(getActivity()).setOrderChange(true);
                        getActivity().finish();

                    } else {
                        if (!mPayMethodAdapter.isNoPaid()) {
                            OrderStep3CompleteInformationFragment fragment = new OrderStep3CompleteInformationFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString(ConstantValue.PAY_MAIN_ORDER_ID, order_id);
                            DebugLog.i("test_pay", order_id + "=-=-=-=step2... put");
                            fragment.setArguments(bundle);
                            mListener.OnFragmentChange(ConstantValue.OS3_COMPLETE_INFO_FRAG, fragment);
                        } else {
                            ToastHelper.showRedToast(getActivity(), "请先支付部分，再完善信息");
                        }
                    }
                } else if (type == ConstantValue.PAY_METHOD_ITEM_SELECT_BANK) {

                    final EditText bankType = (EditText) view;

                    menuDrawer.openMenu();
                    mBankList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
                            pay_method = (PaymentMethod) parent.getAdapter().getItem(position);
                            bank_id = pay_method.paymentID;
                            bank_title = pay_method.title;
                            bankType.setText(pay_method.title);
                            menuDrawer.closeMenu();
                        }
                    });
                } else if (type == ConstantValue.GO_TO_PAY) {

                    money_ = payOnlineRequest.money;

                    DebugLog.i("pay_t", order_id + "----bank_id:" + bank_id);
                    apiClient.payOnline(order_id, bank_id, payOnlineRequest.money, new LoadingAnimResponseHandler(getActivity()) {
                        @Override
                        public void onSuccess(String response) {
                            createApiData(GO_TO_PAY, response);
                        }
                    });
                }
            }
        });
    }

    private void addItem(int position) {
        cacheListData.add(position, new PayPartBackDataModel(0));
        DebugLog.i("list_test", cacheListData.size() + " 添加item-----" + position);
        mPayMethodAdapter.notifyItemInserted(position);
    }

    boolean removeAble = true;

    private void removeItem(int position) {
        if (removeAble) {
            removeAble = false;
            cacheListData.remove(position);
            DebugLog.i("list_test", cacheListData.size() + " 删除item-----" + position);
            mPayMethodAdapter.notifyItemRemoved(position);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    removeAble = true;
                }
            }, 500);
        }
    }

    //接收付款方式显示
    //TODO    支付后返回数据显示 以及操作的     修改 之类巴拉巴拉....................................................................
    @Subscribe
    public void onEventMainThread(EventGetSuccessModel event) {

        if (event.getPay_status() == PayCallBackTag.PAY_SUCCESS) {
            payCallBackSuccess();
        } else if (event.getPay_status() == PayCallBackTag.PAY_FAIL) {
            ToastHelper.showRedToast(getActivity(), "支付失败");
        } else if (event.getPay_status() == PayCallBackTag.PAY_CANCEL) {
            ToastHelper.showRedToast(getActivity(), "交易取消");
        } else if (event.getPay_status() == PayCallBackTag.PAY_EXCEPTION) {
            ToastHelper.showRedToast(getActivity(), "数据异常");
        } else if (event.getPay_status() == PayCallBackTag.PAY_PROGRESSING) {
            ToastHelper.showYellowToast(getActivity(), "支付结果确认中");
        }

    }

    /**
     * 支付成功  操作
     */
    private void payCallBackSuccess() {
        ToastHelper.showGreenToast(getActivity(), "支付成功");
        PayPartBackDataModel payPartBackDataModel = new PayPartBackDataModel(1);
        payPartBackDataModel.amount = money_;
        payPartBackDataModel.bank_title = bank_title;
        cacheListData.add(0, payPartBackDataModel);
        baseSharedPreferencesHelper.setOrderChange(true);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (menuDrawer.isShown()) {

            if (event.getY() < 800.00) {
                menuDrawer.closeMenu();
            }
        }
        return true;
    }
}
