package com.hxqc.pay.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.fragment.CityChooseFragment;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.model.OrderPayRequest;
import com.hxqc.mall.core.model.User;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.FormatCheck;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.dialog.PayStatusDialog;
import com.hxqc.mall.paymethodlibrary.util.PayConstant;
import com.hxqc.pay.api.PayApiClient;
import com.hxqc.pay.inter.OnFragmentChangeListener;
import com.hxqc.pay.model.ContractSample;
import com.hxqc.pay.util.ConstantValue;
import com.hxqc.pay.util.MyTextUtils;
import com.hxqc.util.DebugLog;
import com.hxqc.util.JSONUtils;

import net.simonvt.menudrawer.MenuDrawer;

import cz.msebera.android.httpclient.Header;

import java.util.regex.Pattern;

import hxqc.mall.R;


/**
 * A simple {@link Fragment} subclass.
 * <p>
 * 编辑合同完善信息
 */
public class OrderStep1EditContractFragment extends FunctionFragment implements CityChooseFragment.OnAreaChooseInteractionListener {

    OnFragmentChangeListener mListener;

    EditText mUsernameView;
    EditText mPhoneView;
    EditText mIdView;
    EditText mChooseCityView;
    EditText mDAddressView;
    Button mCreateContBtn;
    TextView mPreviewContView;

    MenuDrawer menuDrawer;

    CityChooseFragment mFragment;
    PayApiClient apiClient;
    OrderPayRequest requestOrder;

    //支付流程类型
    int pay_type = PayConstant.PAY_FLOW_NORMAL_ONLINE;

    final String regexStr = "(^\\d{17}([0-9]|X|x))$|(^[A-Za-z0-9]{8}-[A-Za-z0-9]{1})$";
    final String regexStr_phone = "^0?(13[0-9]|15[012356789]|18[0-9]|14[57]|17[0-9])[0-9]{8}$";
    boolean result_phone = false;
    boolean result_id = false;

    public OrderStep1EditContractFragment() {
    }

//    @Override
//    public void onAttach(Context activity) {
//        super.onAttach(activity);
//        initListener(activity);
//}

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
                    + "--os1ecf");
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
        return inflater.inflate(R.layout.fragment_edit_contract_drawer, container, false);
    }

    boolean clickAble = true;
    BaseSharedPreferencesHelper baseSharedPreferencesHelper;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiClient = new PayApiClient();
        baseSharedPreferencesHelper = new BaseSharedPreferencesHelper(getActivity());

        //拿到订单请求的部分数据
        pay_type = (int) getArguments().get(PayConstant.PAY_STATUS_FLAG);

        initView(view);
        requestOrder = (OrderPayRequest) getArguments().get(ConstantValue.ORDER_PAY_REQUEST);
//---------------------------------------------------------------------------------------------------------
        mPreviewContView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickAble) {
                    clickAble = false;
                    apiGetContractUrl();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            clickAble = true;
                        }
                    }, 500);
                }
            }
        });

        setInfo();

//        apiClient.getDefaultAddress(new BaseMallJsonHttpResponseHandler(getActivity()) {
//            @Override
//            public void onSuccess(String response) {
//                ArrayList< DeliveryAddress > addresses = JSONUtils.fromJson(response, new TypeToken< ArrayList< DeliveryAddress > >() {
//                });
//                if (addresses != null) {
//                    if (addresses.size() != 0) {
//                        DeliveryAddress address = addresses.get(0);
//                        DebugLog.d("address", address.toString());
//                        //购车人
//                        boundSimpleData(address.consignee, mUsernameView);
//                        //联系方式
//                        boundSimpleData(address.phone, mPhoneView);
//                        //详细地址
//                        boundSimpleData(address.detailedAddress, mDAddressView);
//                        //省市区
//                        if (!TextUtils.isEmpty(address.province)) {
//                            mChooseCityView.setText(address.province + " " + address.city + " " + address.district);
//                            cProvince = address.province;
//                            cCity = address.city;
//                            cDistrict = address.district;
//                        }
//                    }
//                }
//            }
//        });

        mCreateContBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 完善订单请求数据
                String username = mUsernameView.getText().toString().trim();
                String phone = mPhoneView.getText().toString().trim();
                String id = mIdView.getText().toString().trim();
                String city = mChooseCityView.getText().toString().trim();
                String dAddress = mDAddressView.getText().toString().trim();
                if (clickAble) {
                    clickAble = false;
                    verifyInfo(username, phone, id, city, dAddress);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            clickAble = true;
                        }
                    }, 500);
                }

            }
        });
    }

    /**
     * 获取个人信息
     */
    private void setInfo() {
        UserInfoHelper.getInstance().getUserInfo(getActivity(), new UserInfoHelper.UserInfoAction() {
            @Override
            public void showUserInfo(User meData) {
                //购车人
                boundSimpleData(meData.fullname, mUsernameView);
                //联系方式
                boundSimpleData(meData.phoneNumber, mPhoneView);
                //详细地址
                boundSimpleData(meData.detailedAddress, mDAddressView);
                //省市区
                if (!TextUtils.isEmpty(meData.province)) {
                    mChooseCityView.setText(String.format("%s %s %s", meData.province, meData.city, meData.district));
                    cProvince = meData.province;
                    cCity = meData.city;
                    cDistrict = meData.district;
                }
            }

            @Override
            public void onFinish() {

            }
        },true);
    }

    //校验数据
    private void verifyInfo(String username, String phone, String id, String city, String dAddress) {
        if (TextUtils.isEmpty(username)) {
            ToastHelper.showYellowToast(getActivity(), "购车人不能为空");
        } else {

            if (username.length() < 2) {
                ToastHelper.showYellowToast(getActivity(), "请检查姓名格式：2-20个字,包含大写字母，小写字母，数字和汉字");
                return;
            }
//            String nicknameRegex = "^[A-Za-z0-9\\-\\u4e00-\\u9fa5]{2,20}$";
//            if (!username.matches(nicknameRegex)) {
//                ToastHelper.showYellowToast(getActivity(), "请检查姓名格式：2-20个字,包含大写字母，小写字母，数字和汉字");
//                return;
//            }
            if (FormatCheck.checkPhoneNumber(phone, getActivity()) != FormatCheck.CHECK_SUCCESS) {
                return;
            }

            if (!FormatCheck.checkName(username, getActivity())) {
                return;
            }


            if (TextUtils.isEmpty(id)) {
                ToastHelper.showYellowToast(getActivity(), "证件号码不能为空");
            } else {
                if (TextUtils.isEmpty(city)) {
                    ToastHelper.showYellowToast(getActivity(), "所在城市不能为空");
                } else {
                    if (TextUtils.isEmpty(dAddress)) {
                        ToastHelper.showYellowToast(getActivity(), "详细地址不能为空");
                    } else {

                        if (MyTextUtils.getLength(dAddress) < 4) {
                            ToastHelper.showYellowToast(getActivity(), "详细地址长度为2~30个字");
                            return;
                        }

                        if (MyTextUtils.getLength(dAddress) > 60) {
                            ToastHelper.showYellowToast(getActivity(), "详细地址长度为2~30个字");
                            return;
                        }

                        //判断 省市区  是否选择
                        if (TextUtils.isEmpty(cProvince)) {
                            ToastHelper.showYellowToast(getActivity(), "请选择省份");
                        } else if (TextUtils.isEmpty(cCity)) {
                            ToastHelper.showYellowToast(getActivity(), "请选择城市");
                        } else if (TextUtils.isEmpty(cDistrict)) {
                            ToastHelper.showYellowToast(getActivity(), "请选择区域");
                        } else if (!result_id) {
                            ToastHelper.showYellowToast(getActivity(), "证件号码有误");
                        } else {
//                            result_phone = Pattern.matches(regexStr_phone, phone);
//                            if (result_phone) {
//                                    String username_p = "^[A-Za-z\\u4e00-\\u9fa5]+$";
//                                    if (Pattern.matches(username_p, username)) {
                                fillData(username, phone, id, dAddress);
//                                    } else {
//                                        ToastHelper.showYellowToast(getActivity(), "用户名只能由中英文组成");
//                                    }

//                            } else {
//                                ToastHelper.showYellowToast(getActivity(), "电话号码有误");
//                            }
                        }
                    }
                }

            }
        }
    }

    //校验长度
    private void setVerifyLength() {
        final int max_name_length = 20;
        final int max_address_length = 60;

        //TODO 验证购车人名字长度
        mUsernameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = MyTextUtils.getLength(s.toString());
                String str = MyTextUtils.getStr(s.toString(), max_name_length);
                DebugLog.i("verify_length", length + "***   " + str);

                if (length > max_name_length) {
                    mUsernameView.setText(str);
                    mUsernameView.setSelection(str.length());
                    ToastHelper.showYellowToast(getActivity(), "2-10个字，可由中英文，字母组成");
                }
            }
        });

        //TODO 验证身份证是否合法
        mIdView.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                result_id = Pattern.matches(regexStr, mIdView.getText().toString());
                DebugLog.d("regexStr", "***   " + result_id + "  ----" + mIdView.getText().toString());
            }
        });

        //TODO 验证详细地址长度
        mDAddressView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = MyTextUtils.getLength(s.toString());
                String str = MyTextUtils.getStr(s.toString(), max_address_length);
                DebugLog.i("verify_length", length + "***   " + str);

                if (length > max_address_length) {
                    mDAddressView.setText(str);
                    mDAddressView.setSelection(str.length());
                    ToastHelper.showYellowToast(getActivity(), "详细地址应为5~60个字");
                }
            }
        });
    }

    /**
     * 加入当前页面填入的信息
     */
    private void fillData(String username, String phone, String id, String dAddress) {
        requestOrder.fullname = username;
        requestOrder.cellphone = phone;
        requestOrder.identifier = id;
        requestOrder.province = cProvince;
        requestOrder.city = cCity;
        requestOrder.district = cDistrict;
        requestOrder.address = dAddress;

        apiClient.getFormalContract(requestOrder, new LoadingAnimResponseHandler(getActivity()) {
            @Override
            public void onSuccess(String response) {
                switchToConfirm();
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
     * 跳转到确认信息
     */
    private void switchToConfirm() {
        OrderStep1ConfirmContractFragmentNew fragment = new OrderStep1ConfirmContractFragmentNew();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ConstantValue.ORDER_PAY_REQUEST, requestOrder);
        bundle.putInt(PayConstant.PAY_STATUS_FLAG, pay_type);
        fragment.setArguments(bundle);
        mListener.OnFragmentChange(ConstantValue.OS1_CONFIRM_CONT_FRAG, fragment);
    }

    /**
     * 获取预览合同  url
     */
    private void apiGetContractUrl() {
        apiClient.getContractSample(new LoadingAnimResponseHandler(getActivity()) {
            @Override
            public void onSuccess(String response) {
                ContractSample contractSample = JSONUtils.fromJson(response, new TypeToken< ContractSample >() {
                });
                if (contractSample != null)
                    if (!TextUtils.isEmpty(contractSample.url)) {
                        DebugLog.d("test_contract",contractSample.url);
                        getContractDialog(contractSample.url);
                    }
            }
        });
    }

    //绑定传进来的简单的数据
    private void boundSimpleData(String text, EditText editText) {
        if (!TextUtils.isEmpty(text)) {
            editText.setText(text);
        }
    }

    //初始化 界面与菜单功能
    private void initView(View view) {
        menuDrawer = (MenuDrawer) view.findViewById(R.id.drawer);
        menuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_NONE);
        mFragment = new CityChooseFragment();
        mFragment.setAreaChooseListener(this);

        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fl_menu_content, mFragment).commit();
        //--------------------------------------------------------------------------
        //用户名
        mUsernameView = (EditText) view.findViewById(R.id.et_username);
        //联系方式
        mPhoneView = (EditText) view.findViewById(R.id.et_phone);
        //证件号码
        mIdView = (EditText) view.findViewById(R.id.et_id);
        //选择城市 区县
        mChooseCityView = (EditText) view.findViewById(R.id.et_city);
        mChooseCityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(cProvince)) {
                    mFragment.setDefaultAreaData(cProvince, cCity, cDistrict);
                }
                menuDrawer.openMenu();
            }
        });
        //详细地址
        mDAddressView = (EditText) view.findViewById(R.id.et_address);
        //生成正式合同    预览合同样本
        mCreateContBtn = (Button) view.findViewById(R.id.btn_edit_contract);
        mPreviewContView = (TextView) view.findViewById(R.id.tv_preview_cont);

        setVerifyLength();
    }

    //获取合同预览的对话框
    private void getContractDialog(String url) {
        final android.app.Dialog mContractSampleDialog = new android.app.Dialog(getActivity());
        mContractSampleDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContractSampleDialog.setContentView(R.layout.dialog_contract_sample);
        mContractSampleDialog.findViewById(R.id.ib_close_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContractSampleDialog.cancel();
            }
        });
        WebView webView = (WebView) mContractSampleDialog.findViewById(R.id.wv_contract_sample);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("tel:")) {
//                    Intent dialIntent = new Intent();
//                    dialIntent.setAction(Intent.ACTION_DIAL);
//                    dialIntent.setData(Uri.parse(url));
//                    startActivity(dialIntent);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }
        });
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.loadUrl(url);
        mContractSampleDialog.show();
    }

    String cProvince;
    String cCity;
    String cDistrict;

    @Override
    public void OnAreaChooseInteraction(String provinces, String city, String district, String pID, String cID, String dID) {
        menuDrawer.closeMenu();
        if (!TextUtils.isEmpty(provinces)) {
            cProvince = provinces;
            cCity = city;
            cDistrict = district;
            mChooseCityView.setText(provinces + " " + city + " " + district);
        }

    }

    @Override
    public String fragmentDescription() {
        return getResources().getString(R.string.fragment_description_edit_contract);
    }
}
