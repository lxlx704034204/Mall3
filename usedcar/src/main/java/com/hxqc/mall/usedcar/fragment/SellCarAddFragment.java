package com.hxqc.mall.usedcar.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hxqc.mall.core.fragment.FunctionFragment;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.materialedittext.MaterialEditText;
import com.hxqc.mall.usedcar.R;
import com.hxqc.util.DebugLog;

/**
 * @Author : 钟学东
 * @Since : 2015-08-13
 * FIXME
 * Todo
 */
@SuppressLint("ValidFragment")
public class SellCarAddFragment extends FunctionFragment implements View.OnClickListener {
    private MaterialEditText mBrandBView;
    private MaterialEditText mSeriesView;
    private MaterialEditText mModelView;
    private Button mConfirmView;

    private String addBrand = "";
    private String addSeries = "";
    private String addModel = "";
    private onConfirmListener monConfirmListener;

    public SellCarAddFragment(String addBrand, String addSeries, String addModel) {
        this.addBrand = addBrand;
        this.addSeries = addSeries;
        this.addModel = addModel;
    }

    public void setOnConfirmListener(onConfirmListener monConfirmListener) {
        this.monConfirmListener = monConfirmListener;
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sell_car_add, null, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBrandBView = (MaterialEditText) view.findViewById(R.id.et_brand);
        mSeriesView = (MaterialEditText) view.findViewById(R.id.et_models);
        mModelView = (MaterialEditText) view.findViewById(R.id.et_car);
        mConfirmView = (Button) view.findViewById(R.id.bt_confirm);
        TextView mTitleView = (TextView) view.findViewById(R.id.title);
        mTitleView.setText("添加车型");


    }

    @Override
    public void onResume() {
        super.onResume();
        DebugLog.i("TAG", addBrand);
        mBrandBView.setText(addBrand);
        DebugLog.i("TAG", "mBrandBView:" + mBrandBView.getText().toString().trim());

        DebugLog.i("TAG", addSeries);
        mSeriesView.setText(addSeries);
        DebugLog.i("TAG", "mSeriesView:" + mSeriesView.getText().toString().trim());

        DebugLog.i("TAG", addModel);
        mModelView.setText(addModel);
        DebugLog.i("TAG", "mModelView:" + mModelView.getText().toString().trim());

        mConfirmView.setOnClickListener(this);
    }

    public void setDate(String s1, String s2, String s3) {
        addBrand = s1;
        addSeries = s2;
        addModel = s3;
    }

    @Override
    public String fragmentDescription() {
        return "添加车型";
    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.bt_confirm) {
            String s1 = "";
            String s2 = "";
            String s3 = "";
            if (mBrandBView.getText().toString().trim().equals("") && mSeriesView.getText().toString().trim().equals("")
                    && mModelView.getText().toString().trim().equals("")) {
                ToastHelper.showYellowToast(getActivity(), "至少填一项");
                return;
            }
            if (!mBrandBView.getText().toString().trim().equals("")) {
                s1 = mBrandBView.getText().toString().trim();
            }
            if (!mSeriesView.getText().toString().trim().equals("")) {
                s2 = mSeriesView.getText().toString().trim();
            }
            if (!mModelView.getText().toString().trim().equals("")) {
                s3 = mModelView.getText().toString().trim();
            }
            monConfirmListener.onConfirmComplete(s1, s2, s3);

        }

    }

    public interface onConfirmListener {
        void onConfirmComplete(String s1, String s2, String s3);
    }
}
