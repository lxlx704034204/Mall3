package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.thirdshop.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 30
 * FIXME
 * Todo
 */
public abstract class CounselorInfoBaseDialog<T> {
    private T mT;
    private LayoutInflater mLayoutInflater;
    private View view;
    private Context mContext;
    private ImageView mLogoView;//图像
    private TextView mNameView;//姓名
    private TextView mStationView;//岗位
    private TextView mExperienceView;//经历
    private TextView mSpecialityView;//特长
    private TextView mMottoView;//格言
    private AlertDialog alertDialog;
    private TextView mPhoneView;

    public CounselorInfoBaseDialog(Context context, T t) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mT = t;
    }

    public void build() {
        view = mLayoutInflater.inflate(R.layout.layout_counselor_info, null);
        initView();
        initData(mLogoView, mNameView, mStationView, mExperienceView, mSpecialityView, mMottoView,mPhoneView);
        initEvent();
        alertDialog = new AlertDialog.Builder(mContext, R.style.MMaterialDialog).setView(view).create();
    }

    abstract void initData(ImageView mLogoView, TextView mNameView, TextView mStationView, TextView mExperienceView, TextView mSpecialityView, TextView mMottoView,TextView mPhoneView);


    private void initEvent() {

    }

    private void initView() {
        mLogoView = (ImageView) view.findViewById(R.id.counselor_info_logo);
        mNameView = (TextView) view.findViewById(R.id.counselor_info_name);
        mStationView = (TextView) view.findViewById(R.id.counselor_info_station);
        mExperienceView = (TextView) view.findViewById(R.id.counselor_info_experience);
        mSpecialityView = (TextView) view.findViewById(R.id.counselor_info_speciality);
        mMottoView = (TextView) view.findViewById(R.id.counselor_info_motto);
        mPhoneView = (TextView) view.findViewById(R.id.counselor_info_phone);
    }

    public void show() {
        alertDialog.show();
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, 1000);
    }
}
