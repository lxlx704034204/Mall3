package com.hxqc.mall.auto.activity.automodel;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.auto.activity.AutoInfoActivityV3;
import com.hxqc.mall.auto.activity.CenterEditAutoActivity;
import com.hxqc.mall.auto.activity.MaintainAutoInfoActivity;
import com.hxqc.mall.auto.activity.automodel.fragment.ThirdShopAutoModelFragment;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoHelper;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.controler.AutoTypeControl;
import com.hxqc.mall.auto.event.FilterMaintenanceShopEvent;
import com.hxqc.mall.auto.event.ReserveMaintainAndHeadActivityEvent;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.AutoModelGroup;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.util.ActivitySwitchAutoInfo;
import com.hxqc.mall.auto.util.ActivityUtil;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.util.DebugLog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import static com.hxqc.mall.auto.config.AutoInfoContants.FLAG_ACTIVITY_MAINTAIN_SHOP_QUOTE;

/**
 * Author:胡仲俊
 * Date: 2016 - 04 - 18
 * Des: 选择车型
 * FIXME
 * Todo
 */
public class ChooseAutoModelActivity extends BackActivity implements ExpandableListView.OnChildClickListener {


    private static final String TAG = AutoInfoContants.LOG_J;
    private ThirdShopAutoModelFragment mAutoModelFragment;
    private MyAuto hmMyAuto;
    private int flagActivity = -1;
    private String shopID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_third_shop_auto_model);

        mAutoModelFragment = (ThirdShopAutoModelFragment) getSupportFragmentManager().findFragmentById(R.id.third_shop_auto_model);
        mAutoModelFragment.setChildClickListener(this);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            if (bundle != null) {
                hmMyAuto = bundle.getParcelable("myAuto");
                flagActivity = bundle.getInt("flagActivity");
                shopID = bundle.getString("shopID", "");
//            myAuto = getIntent().getParcelableExtra("myAuto");
                DebugLog.i(TAG, hmMyAuto.toString());
            }
        }
        AutoTypeControl.getInstance().requestAutoModelN(this, shopID, hmMyAuto.brand, hmMyAuto.brandID, hmMyAuto.series, hmMyAuto.seriesID, autoModelCallBack);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        AutoModelGroup autoModelGroup = mAutoModelGroups.get(groupPosition);
        hmMyAuto.autoModel = autoModelGroup.autoModel.get(childPosition).autoModel;
        hmMyAuto.autoModelID = autoModelGroup.autoModel.get(childPosition).autoModelID;
        DebugLog.i(TAG, hmMyAuto.toString());
       /* boolean isAdd = AutoHelper.getInstance().addAutoLocal(this, hmMyAuto);
        if (isAdd) {
            ActivityUtil.getInstance().killActivity();
            finish();
        }*/
        if (flagActivity == AutoInfoContants.HOME_PAGE || flagActivity == FLAG_ACTIVITY_MAINTAIN_SHOP_QUOTE) {
            DebugLog.i(TAG, "HOME_PAGE");
            ActivitySwitchAutoInfo.toAddAuto(ChooseAutoModelActivity.this, hmMyAuto, flagActivity);
        } else if (flagActivity == AutoInfoContants.MEMBER_CENTER) {
            DebugLog.i(TAG, "MEMBER_CENTER");
            ActivitySwitchAutoInfo.toAddOrEditAutoInfo(this, hmMyAuto, "", -1, AutoInfoContants.ADD_PAGE, CenterEditAutoActivity.class);
            ActivityUtil.getInstance().addActivity(ChooseAutoModelActivity.this);
        } else if (flagActivity == AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_HOME) {
            DebugLog.i(TAG, "FLAG_ACTIVITY_APPOINTMENT_HOME");
            ActivitySwitchBase.toBackData(this, hmMyAuto, flagActivity, true);
            if (UserInfoHelper.getInstance().isLogin(this)) {
                AutoHelper.getInstance().addOrEditAutoLocaltoLinkedList(this, hmMyAuto, AutoHelper.SWITCH_AUTO);
            }
            AutoHelper.getInstance().addOrEditAutoLocaltoLinkedList(this, hmMyAuto, AutoHelper.AUTO_LOCAL_INFO);
            ActivityUtil.getInstance().killActivity();
            finish();
        } else if (flagActivity == AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_4S) {
            DebugLog.i(TAG, "FLAG_ACTIVITY_APPOINTMENT_4S");
            ActivitySwitchBase.toBackData(this, hmMyAuto, flagActivity, false);
            if (UserInfoHelper.getInstance().isLogin(this)) {
                AutoHelper.getInstance().addOrEditAutoLocaltoLinkedList(this, hmMyAuto, AutoHelper.SWITCH_AUTO);
            }
            AutoHelper.getInstance().addOrEditAutoLocaltoLinkedList(this, hmMyAuto, AutoHelper.AUTO_LOCAL_INFO);
            ActivityUtil.getInstance().killActivity();
            finish();
        } else if (flagActivity == AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_LIST) {
            DebugLog.i(TAG, "FLAG_ACTIVITY_APPOINTMENT_LIST");
            EventBus.getDefault().post(new FilterMaintenanceShopEvent(hmMyAuto));
//            ActivitySwitchBase.toBackData(this, hmMyAuto, "com.hxqc.mall.thirdshop.maintenance.activity.FilterMaintenanceShopListActivity");
            if (UserInfoHelper.getInstance().isLogin(this)) {
                AutoHelper.getInstance().addOrEditAutoLocaltoLinkedList(this, hmMyAuto, AutoHelper.SWITCH_AUTO);
            }
            AutoHelper.getInstance().addOrEditAutoLocaltoLinkedList(this, hmMyAuto, AutoHelper.AUTO_LOCAL_INFO);
            ActivityUtil.getInstance().killActivity();
            finish();
        } else if (flagActivity == AutoInfoContants.FLAG_ACTIVITY_APPOINTMENT_REPAIR_DETAIL) {
            DebugLog.i(TAG, "FLAG_ACTIVITY_APPOINTMENT_REPAIR_DETAIL");
//            ActivitySwitchBase.toBackData(this, hmMyAuto, "com.hxqc.mall.thirdshop.maintenance.activity.ReserveMaintainAndHeadActivity");
            EventBus.getDefault().post(new ReserveMaintainAndHeadActivityEvent(hmMyAuto));
            if (UserInfoHelper.getInstance().isLogin(this)) {
                AutoHelper.getInstance().addOrEditAutoLocaltoLinkedList(this, hmMyAuto, AutoHelper.SWITCH_AUTO);
            }
            AutoHelper.getInstance().addOrEditAutoLocaltoLinkedList(this, hmMyAuto, AutoHelper.AUTO_LOCAL_INFO);
            ActivityUtil.getInstance().killActivity();
            finish();
        } else {
            AutoInfoControl.getInstance().editAutoInfoSQ(ChooseAutoModelActivity.this, hmMyAuto, new CallBackControl.CallBack<String>() {
                @Override
                public void onSuccess(String response) {
                    ActivityUtil.getInstance().killActivity();
                    finish();
                    if (flagActivity == AutoInfoContants.AUTO_DETAIL) {
                        DebugLog.i(TAG, "AUTO_DETAIL");
                        ActivitySwitchAutoInfo.toShopQuoteActivity(ChooseAutoModelActivity.this, hmMyAuto);
                    } else if (flagActivity == FLAG_ACTIVITY_MAINTAIN_SHOP_QUOTE) {
                        DebugLog.i(TAG, "FLAG_ACTIVITY_MAINTAIN_SHOP_QUOTE");
                        ActivitySwitchBase.toBackAutoData(ChooseAutoModelActivity.this, hmMyAuto, MaintainAutoInfoActivity.class, AutoInfoContants.CHOOSE_SUCCESS);
                        EventBus.getDefault().post(hmMyAuto);
                        AutoTypeControl.getInstance().killInstance();
                    } else {
                        DebugLog.i(TAG, "OTHER");
//                        ActivitySwitchAutoInfo.toCenterAutoInfo(ChooseAutoModelActivity.this, hmMyAuto);
                        ActivitySwitchBase.toBackAutoData(ChooseAutoModelActivity.this, hmMyAuto, AutoInfoActivityV3.class, AutoInfoContants.CHOOSE_SUCCESS);
                        EventBus.getDefault().post(hmMyAuto);
                        AutoTypeControl.getInstance().killInstance();
                    }
                }

                @Override
                public void onFailed(boolean offLine) {

                }
            });
        }
        return false;
    }

    private ArrayList<AutoModelGroup> mAutoModelGroups;
    private CallBackControl.CallBack<ArrayList<AutoModelGroup>> autoModelCallBack = new CallBackControl.CallBack<ArrayList<AutoModelGroup>>() {
        @Override
        public void onSuccess(ArrayList<AutoModelGroup> response) {
            mAutoModelGroups = response;
            mAutoModelFragment.showAutoModel(response, hmMyAuto.series);
        }

        @Override
        public void onFailed(boolean offLine) {
            mAutoModelFragment.showEmptyAutoModel();
        }
    };

    /*@Override
    public void onAutoModelSucceed(ArrayList<AutoModel> myAutoGroups) {
        mAutoModelFragment.showAutoModel(myAutoGroups,myAuto.series);
    }

    @Override
    public void onAutoModelFailed(boolean offLine) {
        mAutoModelFragment.showEmptyAutoModel();
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (flagActivity != AutoInfoContants.HOME_PAGE) {
            ActivityUtil.getInstance().killInstance();
            AutoInfoControl.getInstance().killInstance();
        }*/
    }
}
