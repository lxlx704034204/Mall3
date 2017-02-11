//package com.hxqc.mall.fragment.auto;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//
//
//import com.hxqc.mall.core.views.SpinnerPopWindow;
//import hxqc.mall.R;
//import com.hxqc.mall.core.adapter.InsuranceCompanyAdapter;
//import com.hxqc.mall.core.adapter.InsuranceDetailAdapter;
//import com.hxqc.mall.control.AutoItemDataControl;
//import com.hxqc.mall.core.model.Insurance;
//import com.hxqc.mall.core.model.InsuranceCompany;
//import com.hxqc.mall.core.model.InsuranceKind;
//import com.hxqc.widget.ListViewNoSlide;
//
//import java.util.ArrayList;
//
//import static hxqc.mall.R.id.factor_group;
//import static hxqc.mall.R.id.insurance_company_spinner;
//import static hxqc.mall.R.id.insurance_company_text;
//import static hxqc.mall.R.id.insurance_detail_layout;
//import static hxqc.mall.R.id.verify_insurance_detail;
//
///**
// * Author: HuJunJie
// * Date: 2015-04-17
// * FIXME
// * Todo 保险
// */
//@Deprecated
//public class InsuranceFragment extends Fragment implements RadioGroup.OnCheckedChangeListener,
//        AdapterView.OnItemClickListener, AutoItemDataControl.InsuranceListener {
//    View mInsuranceDetailLayout; //保险明细layout
//    RadioGroup mTypeCheckView;//保险类型选择
//    ListViewNoSlide mInsuranceDetailListView;//保险详情
//    InsuranceDetailAdapter mInsuranceDetailAdapter;
//    SpinnerPopWindow mInsuranceCompanySpinnerView;//保险公司选择
//    InsuranceCompanyAdapter mCompanyAdapter;
//    TextView mCompanyView;//保险公司名称
//
//    AutoItemDataControl mAutoDataControl;
//
//    Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            ((AutoBuyVerifyFragment) getParentFragment()).mScrollView.requestFocus();
//            ((AutoBuyVerifyFragment) getParentFragment()).mScrollView.smoothScrollTo(0, getView().getTop() - 10);
//            return false;
//        }
//    });
//
//
//
//    public InsuranceFragment() {
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_insurance, container, false);
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        mInsuranceDetailLayout = view.findViewById(insurance_detail_layout);
//        mTypeCheckView = (RadioGroup) view.findViewById(factor_group);
//        mTypeCheckView.setOnCheckedChangeListener(this);
//        mInsuranceDetailListView = (ListViewNoSlide) view.findViewById(verify_insurance_detail);
//        mInsuranceCompanySpinnerView = (SpinnerPopWindow) view.findViewById(insurance_company_spinner);
//        mInsuranceCompanySpinnerView.setOnItemClickList(this);
//        mCompanyView = (TextView) view.findViewById(insurance_company_text);
//
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mAutoDataControl=AutoItemDataControl.getInstance();
//        mAutoDataControl.setInsuranceListener(this);
//    }
//
//    @Override
//    public void onCheckedChanged(RadioGroup group, int checkedId) {
//        //保险类型
//        switch (checkedId) {
//            case R.id.sort_1:
//                mAutoDataControl.changeInsuranceKind(0);
//                break;
//            case R.id.sort_2:
//                mAutoDataControl.changeInsuranceKind(1);
//                break;
//        }
//    }
//
//    @Override
//    public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
//        //保险公司
//        InsuranceCompany mInsuranceCompany = (InsuranceCompany) parent.getAdapter().getItem(position);
//        mCompanyView.setText(mInsuranceCompany.firmName);
//        mTypeCheckView.clearCheck();
//        if (mInsuranceCompany.firmID.equals("0")) {
//            mInsuranceDetailLayout.setVisibility(View.GONE);
//            mAutoDataControl.changeInsuranceCompany(null);
//            return;
//        }
//        mAutoDataControl.changeInsuranceCompany(mInsuranceCompany);
//    }
//
//
//    @Override
//    public void insuranceCompanyAction(ArrayList< InsuranceCompany > insuranceCompanies) {
//        mCompanyAdapter = new InsuranceCompanyAdapter(getActivity(), insuranceCompanies);
//        mInsuranceCompanySpinnerView.setAdapter(mCompanyAdapter);
//    }
//
//    @Override
//    public void changeInsuranceCompany(InsuranceKind insuranceKind) {
//        mInsuranceDetailLayout.setVisibility(View.VISIBLE);
//        mInsuranceDetailAdapter = new InsuranceDetailAdapter(getActivity(), insuranceKind.entirely);
//        mInsuranceDetailListView.setAdapter(mInsuranceDetailAdapter);
//        handler.sendEmptyMessage(0);
//        mTypeCheckView.check(R.id.sort_1);
//    }
//
//    @Override
//    public void changeInsurance(ArrayList< Insurance > insuranceDetail) {
//        mInsuranceDetailAdapter.notifyData(insuranceDetail);
//    }
//}
