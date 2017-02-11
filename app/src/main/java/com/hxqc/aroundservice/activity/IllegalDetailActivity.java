package com.hxqc.aroundservice.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxqc.aroundservice.control.IllegalQueryControl;
import com.hxqc.aroundservice.model.IllegalQueryResultInfo;
import com.hxqc.aroundservice.util.ActivitySwitchAround;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.core.model.ImageModel;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.util.DebugLog;

import hxqc.mall.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 07 - 20
 * FIXME
 * Todo 单个违章详情
 */
public class IllegalDetailActivity extends BackActivity {


    public static final String TAG = AutoInfoContants.LOG_J;
    private TextView dzjcxlhView;
    private TextView wfsjView;
    private TextView wfddView;
    private TextView wfxwView;
    private TextView zfddView;
    private TextView fkView;
    private TextView kfView;
    private LinearLayout lookPicView;
    private ActionBar supportActionBar;
    private LinearLayout dzjcxlhParentView;
    private boolean mIsHistory = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_illegal_detail);

        initView();

        initData();

        initEvent();
    }

    private void initEvent() {
        lookPicView.setOnClickListener(lookPicClickListener);
    }

    private void initData() {
        if (getIntent() != null) {
            Bundle bundleExtra = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
            IllegalQueryResultInfo illegalQueryResultInfo = bundleExtra.getParcelable("illegalQueryResultInfo");
            String plateNumber = bundleExtra.getString("plateNumber");
            mIsHistory = bundleExtra.getBoolean("isHistory", false);
            DebugLog.i(TAG, plateNumber.substring(0, 2));
            /*if(!plateNumber.substring(0,2).equals("鄂A")) {
                dzjcxlhParentView.setVisibility(View.GONE);
                lookPicView.setVisibility(View.GONE);
            }*/
            if (TextUtils.isEmpty(illegalQueryResultInfo.xh)) {
                dzjcxlhParentView.setVisibility(View.GONE);
                lookPicView.setVisibility(View.GONE);
            } else if (mIsHistory) {
                lookPicView.setVisibility(View.GONE);
            }
            supportActionBar.setTitle(plateNumber);
            if (illegalQueryResultInfo != null) {
                dzjcxlhView.setText(illegalQueryResultInfo.xh);
                wfsjView.setText(illegalQueryResultInfo.date);
                wfddView.setText(illegalQueryResultInfo.area);
                wfxwView.setText(illegalQueryResultInfo.act);
                kfView.setText(illegalQueryResultInfo.wfjfs);
                if (TextUtils.isEmpty(illegalQueryResultInfo.money)) {
                    fkView.setText(OtherUtil.amountFormat("0", true));
                } else {
//                    fkView.setText(illegalQueryResultInfo.money);
                    fkView.setText(OtherUtil.amountFormat(illegalQueryResultInfo.money, true));
                }
            }
        }
    }

    private void initView() {
        supportActionBar = getSupportActionBar();

        dzjcxlhParentView = (LinearLayout) findViewById(R.id.illegal_detail_dzjcxlh_parent);
        dzjcxlhView = (TextView) findViewById(R.id.illegal_detail_dzjcxlh);
        wfsjView = (TextView) findViewById(R.id.illegal_detail_wfsj);
        wfddView = (TextView) findViewById(R.id.illegal_detail_wfdd);
        wfxwView = (TextView) findViewById(R.id.illegal_detail_wfxw);
//        zfddView = (TextView) findViewById(R.id.illegal_detail_zfdd);
        fkView = (TextView) findViewById(R.id.illegal_detail_fk);
        kfView = (TextView) findViewById(R.id.illegal_detail_kf);

        lookPicView = (LinearLayout) findViewById(R.id.illegal_detail_look_pic);
    }

    private View.OnClickListener lookPicClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            IllegalQueryControl.getInstance().requestWeiZhangPhoto(IllegalDetailActivity.this, dzjcxlhView.getText().toString(), new CallBackControl.CallBack<ImageModel>() {

                @Override
                public void onSuccess(ImageModel response) {
                    DebugLog.i(TAG, response.url);
                    /*String substring = response.substring(8, response.length() - 2);*/
                    ActivitySwitchAround.toActivityLargePic(IllegalDetailActivity.this, lookPicView, response.url);
                }

                @Override
                public void onFailed(boolean offLine) {

                }
            });
        }
    };
}
