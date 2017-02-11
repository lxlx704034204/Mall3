package com.hxqc.mall.fragment.auto;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hxqc.mall.activity.order.LoanBankChooseActivity;
import com.hxqc.mall.api.NewAutoClient;
import com.hxqc.mall.control.AutoItemDataControl;
import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.model.OrderRequest;
import com.hxqc.mall.core.model.auto.AutoBaseInformation;
import com.hxqc.mall.core.model.auto.AutoDetail;
import com.hxqc.mall.core.model.auto.PaymentType;
import com.hxqc.mall.core.model.loan.LoanItemFinanceModel;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.core.views.ColorSquare;
import com.hxqc.mall.core.views.autotext.AutofitTextView;
import com.hxqc.mall.views.auto.AutoVerifyLicenseService;
import com.hxqc.mall.views.auto.AutoVerifyPaymentView;
import com.hxqc.mall.views.autopackage.AutoPackageVerify;
import com.hxqc.util.DebugLog;

import hxqc.mall.R;

import static hxqc.mall.R.id.auto_image;
import static hxqc.mall.R.id.auto_name;
import static hxqc.mall.R.id.auto_price;
import static hxqc.mall.R.id.color_square_appearance;
import static hxqc.mall.R.id.color_square_interior;


/**
 * Author: HuJunJie Date: 2015-04-15 FIXME Todo 购买选择确认
 */
public abstract class AutoBuyVerifyFragment extends FunctionFragment implements
        AutoItemDataControl.FactorChangeListener, View.OnClickListener {
    public static final int LOAN_BANK_RESULT = 909;//分期银行
    public ScrollView mScrollView;//滚动条
    AutoDetail autoDetail;
    AutoBaseInformation autoBaseInformation;
    ImageView mThumbView;//车辆图片
    TextView mNameView;//车辆名称
    TextView mPriceView;//车辆名称
    ColorSquare mAppearanceColorView;//车身颜色
    ColorSquare mInteriorColorView;//内饰颜色
    AutofitTextView mAllCostView;//总费用
    //    AutofitTextView mSubscriptionView;//订金
    AutoVerifyPaymentView mPaymentTypeView; //付款方式
    View mInteriorView;//内饰颜色layout
    AutoItemDataControl mAutoDataControl;
    AutoPackageVerify mPackageVerify;
    AutoVerifyLicenseService mLicenseView;//帮办理牌照
    Button mToOrderView;

    public AutoBuyVerifyFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auto_verify, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mThumbView = (ImageView) view.findViewById(auto_image);
        mNameView = (TextView) view.findViewById(auto_name);
        mPriceView = (TextView) view.findViewById(auto_price);
        mAppearanceColorView = (ColorSquare) view.findViewById(color_square_appearance);
        mInteriorColorView = (ColorSquare) view.findViewById(color_square_interior);
//        mSubscriptionView = (AutofitTextView) view.findViewById(R.id.auto_subscription);
        mLicenseView = (AutoVerifyLicenseService) view.findViewById(R.id.license_service);

        mScrollView = (ScrollView) view.findViewById(R.id.verify_scroll);
        mAllCostView = (AutofitTextView) view.findViewById(R.id.auto_cost);
        mInteriorView = view.findViewById(R.id.interior_layout);
        mToOrderView = (Button) view.findViewById(R.id.to_pay_order);
        mToOrderView.setOnClickListener(this);
        mPackageVerify = (AutoPackageVerify) view.findViewById(R.id.package_verify);

        mPaymentTypeView = (AutoVerifyPaymentView) view.findViewById(R.id.payment_type_view);
        mPaymentTypeView.setOnPaymentClickListener(new PaymentTypeListener());

        mAutoDataControl = AutoItemDataControl.getInstance();
        AutoItemDataControl.getInstance().setFactorChangeListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOAN_BANK_RESULT && resultCode == LoanBankChooseActivity.LOAN_RESULT_CODE) {
            /**
             * TODO   获取 选择的分期银行
             */
            if (data != null && data.getParcelableExtra(LoanBankChooseActivity.LOAN_RESULT_TAG) != null) {
                LoanItemFinanceModel lifm = data.getParcelableExtra(LoanBankChooseActivity.LOAN_RESULT_TAG);
                DebugLog.i("TEST_loan", "onActivityResult--: " + lifm.toString());
                mAutoDataControl.setChooseLoanFinance(lifm);
                mPaymentTypeView.setOfflineDes(false);
                mAutoDataControl.setPaymentType(OrderRequest.PAYMENTTYPE_INSTALLMENT);//分期
            } else {
                mAutoDataControl.setChooseLoanFinance(null);
                mPaymentTypeView.setOfflineDes(true);
                mAutoDataControl.setPaymentType(OrderRequest.PAYMENTTYPE_OFF_LINE);//预付订金
            }
        }
    }

    public void refresh(AutoDetail autoDetail) {
        this.autoDetail = autoDetail;
        autoBaseInformation = autoDetail.getAutoBaseInformation();

        mLicenseView.setLicenseServiceListener(new LicenseChangeListener());
        mLicenseView.setAutoDetail(autoDetail);

        ImageUtil.setImage(getActivity(), mThumbView, autoBaseInformation.getItemThumb());
        mNameView.setText(autoBaseInformation.getItemDescription());
        mPriceView.setText(String.format("¥ %s", autoDetail.getItemPriceU()));
        mAppearanceColorView.setColors(autoDetail.getItemColor());
        if (TextUtils.isEmpty(autoDetail.getItemInteriorColor())) {
            mInteriorView.setVisibility(View.GONE);
        } else {
            mInteriorView.setVisibility(View.VISIBLE);
        }
        mInteriorColorView.setColors(autoDetail.getItemInterior());
        mPackageVerify.showPackage(autoDetail.getPackages() != null && autoDetail.getPackages().size() > 0);
        setPackVerify();
        mPaymentTypeView.setPaymentTypes(autoDetail.getPaymentType());
//        mSubscriptionView.setText(String.format("订金:%s", OtherUtil.stringToMoney(autoDetail.getSubscription())));
        mToOrderView.setText(String.format("预付订金%s", OtherUtil.stringToMoney(autoDetail.getSubscription())));
    }

    public void setPackVerify() {

        mPackageVerify.refreshPackageValue(mAutoDataControl.getCheckAutoPackage());
    }


    @Override
    public void factorChange(double cost) {
        mAllCostView.setText(String.format("合计:%s", OtherUtil.stringToMoney(mAutoDataControl.getAllCost())));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.to_pay_order) {
            if (mAutoDataControl.mOrderPayRequest.paymentType.equals("-1")) {
                ToastHelper.showYellowToast(getActivity(), "请选择付款方式");
                return;
            }
//            if (!UserInfoHelper.getInstance().isLogin(getContext())) {
//                ActivitySwitchAuthenticate.toLogin(getActivity(), null, ActivitySwitchBase.ENTRANCE_SHOP);
//                return;
//            }
            UserInfoHelper.getInstance().loginAction(getActivity(), ActivitySwitchBase.ENTRANCE_SHOP,
                    new UserInfoHelper.OnLoginListener() {
                        @Override
                        public void onLoginSuccess() {
                            readyPurchase();
                            toPay();
                        }
                    });

        }
    }

    /**
     * 预购
     */
    private void readyPurchase() {
        NewAutoClient client = new NewAutoClient();
        client.readyPurchase(getActivity(), autoDetail.getItemID(),
                new BaseMallJsonHttpResponseHandler(getActivity()) {
                    @Override
                    public void onSuccess(String response) {
                    }
                });
    }

    public abstract void toPay();


    /**
     * 付款方式
     */
    class PaymentTypeListener implements AutoVerifyPaymentView.OnPaymentClickListener {
        @Override
        public void onPaymentClick(PaymentType payment, View v) {
            switch (String.valueOf(payment.paymentType)) {
                case OrderRequest.PAYMENTTYPE_INSTALLMENT:
                    String financeID = mAutoDataControl.getChooseLoanFinance() == null ? "" :
                            mAutoDataControl.getChooseLoanFinance().financeID;
                    DebugLog.i("TEST_loan", "OnPaymentClick--: " + financeID);
                    ActivitySwitcher.chooseLoanBank(getActivity(), AutoBuyVerifyFragment.this, financeID);
                    break;
                case OrderRequest.PAYMENTTYPE_OFF_LINE:
                    mAutoDataControl.setChooseLoanFinance(null);
                    break;
            }
        }

    }


    /**
     * 牌照办理
     */
    class LicenseChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.transaction_simpleness:
                    mAutoDataControl.setIsAddLicense(false);
                    break;
                case R.id.transaction_license:
                    //办理上牌
                    mAutoDataControl.setIsAddLicense(true);
                    break;
            }
        }
    }

}
