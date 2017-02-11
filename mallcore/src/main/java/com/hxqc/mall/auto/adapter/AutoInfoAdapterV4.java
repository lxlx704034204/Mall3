package com.hxqc.mall.auto.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.util.ActivitySwitchAutoInfo;
import com.hxqc.mall.auto.view.CommonRelativeTextView;
import com.hxqc.mall.auto.view.swipemenulistview.SwipeMenuListView;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.util.DebugLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author:胡仲俊
 * Date: 2016 - 03 - 10
 * FIXME
 * Todo 车辆信息列表
 */
public class AutoInfoAdapterV4 extends BaseAdapter {

    private static final String TAG = AutoInfoContants.LOG_J;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int isDefault;//默认条目
    private ArrayList<MyAuto> mAutoGroups;
    private SwipeMenuListView mSwipeMenuListView;
    private ViewStateCallBack l;
    private HashMap<Integer, View> viewHashMap;

    public interface ViewStateCallBack {
        void get(HashMap<Integer, View> map);
    }

    public void setOnCallBack(ViewStateCallBack l) {
        this.l = l;
    }


    public AutoInfoAdapterV4(Context context, ArrayList<MyAuto> autoGroups, SwipeMenuListView swipeMenuListView) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mAutoGroups = autoGroups;
//        DebugLog.i(TAG, autoGroups.get(0).toString());
        this.mSwipeMenuListView = swipeMenuListView;
        viewHashMap = new HashMap<>();
    }

    public void notifyData(ArrayList<MyAuto> autoGroups) {
        this.mAutoGroups = autoGroups;
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < mAutoGroups.size(); i++) {
            if (mAutoGroups.get(i).authenticated == 1) {
                integers.add(i);
            }
        }
        mSwipeMenuListView.disallowOpen(integers);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAutoGroups != null ? mAutoGroups.size() : 0;
    }

    @Override
    public MyAuto getItem(int position) {
        return mAutoGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_auto_info_v3, null);
            viewHolder.mAutoInfoParent = (RelativeLayout) convertView.findViewById(R.id.item_auto_info_parent);
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
            viewHolder.mAutoInfoEditBn = (TextView) convertView.findViewById(R.id.item_auto_info_edit_bn);

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
        /*if (mAutoGroups.get(position).isDefault.equals("0")) {
            if (mAutoGroups.get(position).isDefault.equals("20")) {
                isDefault = position;
                viewHolder.mAutoIsDefaultView.setVisibility(View.GONE);
            } else {
                viewHolder.mAutoIsDefaultView.setVisibility(View.GONE);
            }
        }*/
        /*if (position == mAutoGroups.size() - 1) {
            viewHolder.mAutoInfoCutOff.setVisibility(View.GONE);
        }*/
        if (l != null) {
            viewHashMap.put(position, viewHolder.mAutoInfoEditBn);
            l.get(viewHashMap);
        }

        setValue(viewHolder, mAutoGroups.get(position), position);
//        menuStateChange(position,convertView,parent);
        viewHolder.mAutoInfoParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitchAutoInfo.toAutoDetailActivity(mContext, mAutoGroups.get(position));
            }
        });
        editClick(viewHolder, position);
        return convertView;
    }

    private void menuStateChange(final int position, final View convertView, final ViewGroup parent) {
        mSwipeMenuListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
                DebugLog.i(TAG, "onMenuOpen: " + position);
                View view = getView(position, convertView, parent);
                /*if(viewHolder.mAutoInfoEditBn.getVisibility() ==View.VISIBLE ) {
                    viewHolder.mAutoInfoEditBn.setVisibility(View.GONE);
                }*/

//                mHashMap.put(position, true);
//                notifyDataSetChanged();

            }

            @Override
            public void onMenuClose(int position) {
                DebugLog.i(TAG, "onMenuClose: " + position);
//                mHashMap.put(position, false);
                notifyDataSetChanged();
            }
        });
    }

    /*private void updateView(ViewHolder viewHolder,int itemIndex) {
        //得到第一个可显示控件的位置，
        int visiblePosition = mSwipeMenuListView.getFirstVisiblePosition();
        //只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
        if (itemIndex - visiblePosition >= ) {
            //得到要更新的item的view
            View view = mSwipeMenuListView.getChildAt(itemIndex - visiblePosition);
            //调用adapter更新界面
            viewHolder.mAutoInfoEditBn.setVisibility(View.GONE);
        }
    }*/

    private void editClick(final ViewHolder viewHolder, final int position) {
        viewHolder.mAutoInfoEditBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DebugLog.i(TAG, "position: " + position);
                mSwipeMenuListView.smoothOpenMenu(position);
                viewHolder.mAutoInfoEditBn.setVisibility(View.GONE);
            }
        });
    }

    /**
     * @param viewHolder
     * @param myAutoV2
     */
    public void setValue(ViewHolder viewHolder, final MyAuto myAutoV2, int position) {
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
            viewHolder.mAutoInfoEditBn.setVisibility(View.GONE);
        } else {
            viewHolder.mAutoInfoLogoAttestation.setVisibility(View.GONE);
            viewHolder.mAutoInfoEditBn.setVisibility(View.VISIBLE);
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

        if (!TextUtils.isEmpty(myAutoV2.expirationOfPolicy) || !TextUtils.isEmpty(myAutoV2.examineDate) || !TextUtils.isEmpty(myAutoV2.guaranteePeriod) || !TextUtils.isEmpty(myAutoV2.nextMaintenanceDate)) {
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

        if (!TextUtils.isEmpty(myAutoV2.nextMaintenanceDistance + "") && !(myAutoV2.nextMaintenanceDistance + "").equals("0")) {
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
        private RelativeLayout mAutoInfoParent;
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
        private TextView mAutoInfoEditBn;

        private LinearLayout mAutoInfoTimeParent;
        private CommonRelativeTextView mAutoInfoExpirationOfPolicy;
        private CommonRelativeTextView mAutoInfoExamineDate;
        private CommonRelativeTextView mAutoInfoGuaranteePeriod;
        private CommonRelativeTextView mAutoInfoNextMaintenanceDate;
        private CommonRelativeTextView mAutoInfoNextMaintenanceDistance;

        private View mAutoInfoCutOff;

    }


}
