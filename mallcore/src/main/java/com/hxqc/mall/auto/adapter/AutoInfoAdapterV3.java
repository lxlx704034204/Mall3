package com.hxqc.mall.auto.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.util.ActivitySwitchAutoInfo;
import com.hxqc.mall.auto.view.CommonRelativeTextView;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.util.ImageUtil;

import java.util.ArrayList;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 10
 * FIXME
 * Todo 车辆信息列表
 */
public class AutoInfoAdapterV3 extends BaseAdapter {

    private static final String TAG = "AutoInfoAdapterV2";
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int isDefault;//默认条目
    private ArrayList<MyAuto> mAutoGroups;

    public AutoInfoAdapterV3(Context context, ArrayList<MyAuto> autoGroups) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mAutoGroups = autoGroups;
//        DebugLog.i(TAG, autoGroups.get(0).toString());
    }

    public void notifyData(ArrayList<MyAuto> autoGroups) {
        this.mAutoGroups = autoGroups;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAutoGroups != null ? mAutoGroups.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_auto_info, null);
            viewHolder.mAutoInfoBrandThumb = (ImageView) convertView.findViewById(R.id.item_auto_info_log);
            viewHolder.mAutoInfoType = (TextView) convertView.findViewById(R.id.item_auto_info_model);
            viewHolder.mAutoInfoPlateNum = (TextView) convertView.findViewById(R.id.item_auto_info_plate_number);
            viewHolder.mAutoDrivingDistance = (TextView) convertView.findViewById(R.id.item_auto_info_driving_distance);
            viewHolder.mAutoInfoScore = (TextView) convertView.findViewById(R.id.item_auto_info_score);
            viewHolder.mAutoInfoCoupon = (TextView) convertView.findViewById(R.id.item_auto_info_violet);
            viewHolder.mAutoInfoExplan = (TextView) convertView.findViewById(R.id.item_auto_info_explan);
            viewHolder.mAutoInfoComplete = (TextView) convertView.findViewById(R.id.item_auto_info_complete);

            viewHolder.mAutoIsDefaultView = (ImageView) convertView.findViewById(R.id.auto_is_default);

            viewHolder.mAutoInfoLevel = (ImageView) convertView.findViewById(R.id.item_auto_info_level);
            viewHolder.mAutoInfoLogoCoupon = (ImageView) convertView.findViewById(R.id.item_auto_info_logo_coupon);
            viewHolder.mAutoInfoLogoAttestation = (ImageView) convertView.findViewById(R.id.item_auto_info_logo_attestation);

            viewHolder.mAutoInfoTimeParent = (LinearLayout) convertView.findViewById(R.id.item_auto_info_time_parent);
            viewHolder.mAutoInfoExpirationOfPolicy = (CommonRelativeTextView) convertView.findViewById(R.id.item_auto_info_expirationofpolicy);
            viewHolder.mAutoInfoExamineDate = (CommonRelativeTextView) convertView.findViewById(R.id.item_auto_info_examinedate);
            viewHolder.mAutoInfoGuaranteePeriod = (CommonRelativeTextView) convertView.findViewById(R.id.item_auto_info_guaranteeperiod);
            viewHolder.mAutoInfoNextMaintenanceDate = (CommonRelativeTextView) convertView.findViewById(R.id.item_auto_info_nextmaintenancedate);
            viewHolder.mAutoInfoNextMaintenanceDistance = (CommonRelativeTextView) convertView.findViewById(R.id.item_auto_info_next_maintenance_distance);

            viewHolder.mAutoInfoCutOff = convertView.findViewById(R.id.item_auto_cut_off);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mAutoGroups.get(position).isDefault.equals("0")) {
            if (mAutoGroups.get(position).isDefault.equals("20")) {
                isDefault = position;
                viewHolder.mAutoIsDefaultView.setVisibility(View.GONE);
            } else {
                viewHolder.mAutoIsDefaultView.setVisibility(View.GONE);
            }
        }
        /*if (position == mAutoGroups.size() - 1) {
            viewHolder.mAutoInfoCutOff.setVisibility(View.GONE);
        }*/
        setValue(viewHolder, mAutoGroups.get(position));
        return convertView;
    }

    /**
     * @param viewHolder
     * @param myAutoV2
     */
    public void setValue(ViewHolder viewHolder, final MyAuto myAutoV2) {
        if (AutoInfoContants.IS_TEST) {
            if (TextUtils.isEmpty(myAutoV2.brand) && TextUtils.isEmpty(myAutoV2.series) && TextUtils.isEmpty(myAutoV2.autoModel)) {
                viewHolder.mAutoInfoExplan.setVisibility(View.VISIBLE);
                viewHolder.mAutoInfoComplete.setVisibility(View.VISIBLE);
                viewHolder.mAutoInfoType.setVisibility(View.GONE);
            } else {
                viewHolder.mAutoInfoExplan.setVisibility(View.GONE);
                viewHolder.mAutoInfoComplete.setVisibility(View.VISIBLE);
                viewHolder.mAutoInfoType.setVisibility(View.VISIBLE);
                viewHolder.mAutoInfoType.setText(myAutoV2.autoModel);
            }
        } else {
            if (TextUtils.isEmpty(myAutoV2.brand) && TextUtils.isEmpty(myAutoV2.series) && TextUtils.isEmpty(myAutoV2.autoModel)) {
                viewHolder.mAutoInfoExplan.setVisibility(View.VISIBLE);
                viewHolder.mAutoInfoComplete.setVisibility(View.VISIBLE);
                viewHolder.mAutoInfoType.setVisibility(View.GONE);
            } else {
                viewHolder.mAutoInfoExplan.setVisibility(View.GONE);
                viewHolder.mAutoInfoComplete.setVisibility(View.GONE);
                viewHolder.mAutoInfoType.setVisibility(View.VISIBLE);
                viewHolder.mAutoInfoType.setText(myAutoV2.autoModel);
            }
        }

        if (myAutoV2.authenticated == 1) {
            viewHolder.mAutoInfoLogoAttestation.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mAutoInfoLogoAttestation.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(myAutoV2.plateNumber)) {
            viewHolder.mAutoInfoPlateNum.setVisibility(View.GONE);
        } else {
            viewHolder.mAutoInfoPlateNum.setVisibility(View.VISIBLE);
            viewHolder.mAutoInfoPlateNum.setText("车牌号:" + myAutoV2.plateNumber);
        }

        viewHolder.mAutoDrivingDistance.setText("累计行驶里程:" + String.format("%s", myAutoV2.drivingDistance));

        if (TextUtils.isEmpty(myAutoV2.brandThumb)) {
            viewHolder.mAutoInfoBrandThumb.setImageResource(R.drawable.pic_normal_square);
        } else {
            ImageUtil.setImageSquareOfAutoInfo(mContext, viewHolder.mAutoInfoBrandThumb, myAutoV2.brandThumb);
        }

        if (TextUtils.isEmpty(myAutoV2.couponCount) /*|| Integer.parseInt(myAutoV2.couponCount) <= 0*/) {
            viewHolder.mAutoInfoCoupon.setVisibility(View.GONE);
        } else {
            viewHolder.mAutoInfoCoupon.setVisibility(View.VISIBLE);
            viewHolder.mAutoInfoCoupon.setText(myAutoV2.couponCount + "张券");
        }

        if (TextUtils.isEmpty(myAutoV2.score) /*|| Integer.parseInt(myAutoV2.score) <= 0*/) {
            viewHolder.mAutoInfoScore.setVisibility(View.GONE);
        } else {
            viewHolder.mAutoInfoScore.setVisibility(View.VISIBLE);
            viewHolder.mAutoInfoScore.setText(myAutoV2.score + "积分");
        }

        viewHolder.mAutoInfoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitchAutoInfo.toChooseBrandActivity(mContext, myAutoV2);
            }
        });
        /*if (!TextUtils.isEmpty(myAutoV2.couponCount) && Integer.parseInt(myAutoV2.couponCount) > 0) {
            viewHolder.mAutoInfoLogoCoupon.setVisibility(View.VISIBLE);
        }*/
        if (TextUtils.isEmpty(myAutoV2.Level)) {
            viewHolder.mAutoInfoLevel.setVisibility(View.GONE);
        } else {
            viewHolder.mAutoInfoLevel.setVisibility(View.GONE);
//            ImageUtil.setImage(mContext, viewHolder.mAutoInfoLevel, myAutoV2.LevelIcon);
            /*if (myAutoV2.Level.equals("1")) {
                viewHolder.mAutoInfoLevel.setImageResource(R.drawable.ic_ordinary);
            } else if (myAutoV2.Level.equals("2")) {
                viewHolder.mAutoInfoLevel.setImageResource(R.drawable.ic_copper);
            } else if (myAutoV2.Level.equals("3")) {
                viewHolder.mAutoInfoLevel.setImageResource(R.drawable.ic_silver);
            } else if (myAutoV2.Level.equals("4")) {
                viewHolder.mAutoInfoLevel.setImageResource(R.drawable.ic_gold);
            } else {
                viewHolder.mAutoInfoLevel.setVisibility(View.GONE);
            }*/
        }

        if (!TextUtils.isEmpty(myAutoV2.expirationOfPolicy) || !TextUtils.isEmpty(myAutoV2.examineDate) || !TextUtils.isEmpty(myAutoV2.guaranteePeriod) || !TextUtils.isEmpty(myAutoV2.nextMaintenanceDate) || (!TextUtils.isEmpty(myAutoV2.nextMaintenanceDistance + "") && myAutoV2.nextMaintenanceDistance != 0)) {
            viewHolder.mAutoInfoTimeParent.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mAutoInfoTimeParent.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(myAutoV2.expirationOfPolicy) && !myAutoV2.expirationOfPolicy.equals("")) {
            viewHolder.mAutoInfoExpirationOfPolicy.setVisibility(View.VISIBLE);
            viewHolder.mAutoInfoExpirationOfPolicy.setTwoText(myAutoV2.expirationOfPolicy);
        } else {
            viewHolder.mAutoInfoExpirationOfPolicy.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(myAutoV2.examineDate) && !myAutoV2.examineDate.equals("")) {
            viewHolder.mAutoInfoExamineDate.setVisibility(View.VISIBLE);
            String[] split = myAutoV2.examineDate.split("-");
            viewHolder.mAutoInfoExamineDate.setTwoText(split[0] + "-" + split[1]);
        } else {
            viewHolder.mAutoInfoExamineDate.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(myAutoV2.guaranteePeriod) && !myAutoV2.guaranteePeriod.equals("")) {
            viewHolder.mAutoInfoGuaranteePeriod.setVisibility(View.VISIBLE);
            viewHolder.mAutoInfoGuaranteePeriod.setTwoText(myAutoV2.guaranteePeriod);
        } else {
            viewHolder.mAutoInfoGuaranteePeriod.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(myAutoV2.nextMaintenanceDate) && !myAutoV2.nextMaintenanceDate.equals("")) {
            viewHolder.mAutoInfoNextMaintenanceDate.setVisibility(View.VISIBLE);
            String[] split = myAutoV2.nextMaintenanceDate.split("-");
            viewHolder.mAutoInfoNextMaintenanceDate.setTwoText(split[0] + "-" + split[1]);
        } else {
            viewHolder.mAutoInfoNextMaintenanceDate.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(myAutoV2.nextMaintenanceDistance + "") && myAutoV2.nextMaintenanceDistance != 0) {
            viewHolder.mAutoInfoNextMaintenanceDistance.setVisibility(View.VISIBLE);
            viewHolder.mAutoInfoNextMaintenanceDistance.setTwoText(myAutoV2.nextMaintenanceDistance + "km");
        } else {
            viewHolder.mAutoInfoNextMaintenanceDistance.setVisibility(View.GONE);
        }
    }

    public int getIsDefault() {
        return isDefault;
    }

    public ArrayList<MyAuto> getmAutoGroups() {
        return mAutoGroups;
    }

    class ViewHolder {
        private ImageView mAutoIsDefaultView;
        private ImageView mAutoInfoBrandThumb;
        private TextView mAutoInfoType;
        private TextView mAutoInfoPlateNum;
        private TextView mAutoDrivingDistance;
        private TextView mAutoInfoScore;
        private TextView mAutoInfoCoupon;
        private TextView mAutoInfoExplan;
        private TextView mAutoInfoComplete;
        private ImageView mAutoInfoLevel;
        private ImageView mAutoInfoLogoCoupon;
        private ImageView mAutoInfoLogoAttestation;

        private LinearLayout mAutoInfoTimeParent;
        private CommonRelativeTextView mAutoInfoExpirationOfPolicy;
        private CommonRelativeTextView mAutoInfoExamineDate;
        private CommonRelativeTextView mAutoInfoGuaranteePeriod;
        private CommonRelativeTextView mAutoInfoNextMaintenanceDate;
        private CommonRelativeTextView mAutoInfoNextMaintenanceDistance;

        private View mAutoInfoCutOff;

    }


}
