package com.hxqc.mall.thirdshop.maintenance.views;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.thirdshop.R;
import com.hxqc.mall.thirdshop.maintenance.model.Mechanic;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 30
 * FIXME
 * Todo
 */
public class ServiceMechanicDialog extends CounselorInfoBaseDialog<Mechanic> {

    private Context mContext;
    private Mechanic mMechanic;

    public ServiceMechanicDialog(Context context, Mechanic mechanic) {
        super(context, mechanic);
        this.mContext = context;
        this.mMechanic = mechanic;
    }

    @Override
    void initData(ImageView mLogoView, TextView mNameView, TextView mStationView, TextView mExperienceView, TextView mSpecialityView, TextView mMottoView, TextView mPhoneView) {
        mNameView.setText("姓名:" + mMechanic.name);
        mStationView.setText("岗位:" + mMechanic.station);
        mExperienceView.setText("工作经历:" + mMechanic.experience);
        mSpecialityView.setText("工作特长:" + mMechanic.skill);
        mMottoView.setText("工作格言:" + mMechanic.logos);
        mPhoneView.setVisibility(View.GONE);
        ImageUtil.setImage(mContext, mLogoView, mMechanic.avatar, R.drawable.ic_productcomment_list_user);
    }

}
