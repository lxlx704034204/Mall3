package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.model.ServiceAdviser;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 30
 * FIXME
 * Todo
 */
public class ServiceAdviserDialog extends CounselorInfoBaseDialog<ServiceAdviser> {

    private Context mContext;
    private ServiceAdviser mServiceAdviser;

    public ServiceAdviserDialog(Context context, ServiceAdviser serviceAdviser) {
        super(context, serviceAdviser);
        this.mContext = context;
        this.mServiceAdviser = serviceAdviser;
    }

    @Override
    void initData(ImageView mLogoView, TextView mNameView, TextView mStationView, TextView mExperienceView, TextView mSpecialityView, TextView mMottoView, TextView mPhoneView) {
        mNameView.setText("姓名:" + mServiceAdviser.name);
        mStationView.setText("岗位:" + mServiceAdviser.station);
        mPhoneView.setText("咨询电话:" + mServiceAdviser.mobile);
        mPhoneView.setVisibility(View.GONE);
        mExperienceView.setText("工作经历:" + mServiceAdviser.experience);
        mSpecialityView.setText("工作特长:" + mServiceAdviser.skill);
        mMottoView.setText("工作格言:" + mServiceAdviser.logos);
        ImageUtil.setImage(mContext, mLogoView, mServiceAdviser.avatar, R.drawable.ic_productcomment_list_user);
    }

}
