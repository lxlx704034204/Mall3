package com.hxqc.mall.auto.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.controler.AutoSPControl;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.auto.util.ActivitySwitchAutoInfo;
import com.hxqc.mall.core.R;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;

/**
 * Author:胡仲俊
 * Date: 2016 - 12 - 15
 * FIXME
 * Todo
 */

public class CompleteDialogActivity extends NoBackActivity {

    private ImageView cancelView;
    private Button completeView;
    private int mFlagActivity;
    private MyAuto mMyAuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_auto_info_complete);

        initView();

        initData();

        initEvent();
    }

    private void initData() {
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            mFlagActivity = bundle.getInt("flagActivity", -1);
            mMyAuto = bundle.getParcelable("myAuto");
        }
    }

    private void initEvent() {
        cancelView.setOnClickListener(cancelClickListener);
        completeView.setOnClickListener(completeClickListener);
    }

    private void initView() {
        cancelView = (ImageView) findViewById(R.id.dialog_cancel);
        completeView = (Button) findViewById(R.id.dialog_complete);
    }

    private View.OnClickListener cancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mFlagActivity == AutoInfoContants.FLAG_MODULE_MAINTAIN || mFlagActivity == AutoInfoContants.FLAG_MODULE_APPOINTMENT) {
//                ActivitySwitchAutoInfo.toAddAuto(CompleteDialogActivity.this, null, AutoInfoContants.AUTO_DETAIL);
                if (AutoSPControl.getDialogCount() != 1) {
                    AutoSPControl.saveDialogCount(1);
                }
                finish();
            } else {
                finish();
            }
        }
    };

    private View.OnClickListener completeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (UserInfoHelper.getInstance().isLogin(CompleteDialogActivity.this)) {
//                    ActivitySwitchAutoInfo.toAutoInfo(getActivity());
//                    ActivitySwitchBase.toAutoInfo(getActivity(), "", AutoInfoContants.AUTO_DETAIL);
//                ActivitySwitchAutoInfo.toChooseBrandActivity(CompleteDialogActivity.this, mMyAuto, AutoInfoContants.AUTO_DETAIL, false);
                if(mFlagActivity == AutoInfoContants.FLAG_MODULE_APPOINTMENT) {
                    ActivitySwitchAutoInfo.toChooseBrandActivity(CompleteDialogActivity.this, mMyAuto, AutoInfoContants.FLAG_MODULE_APPOINTMENT, true);
                } else {
                    ActivitySwitchAutoInfo.toCenterAutoInfo(CompleteDialogActivity.this, "");
                }
//                ActivitySwitchAutoInfo.toMaintainAutoInfo(CompleteDialogActivity.this);
                if (AutoSPControl.getDialogCount() != 1) {
                    AutoSPControl.saveDialogCount(1);
                }
                finish();
            }
        }
    };

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//退出对话框
            finish();
        }
        return false;
    }*/
}
