package com.hxqc.aroundservice.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.hxqc.mall.activity.NoBackActivity;
import com.hxqc.mall.auto.config.AutoInfoContants;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.SharedPreferencesHelper;
import com.hxqc.util.DebugLog;

import hxqc.mall.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 05 - 03
 * FIXME
 * Todo 提交成功页面
 */
public class IllegalProcessingSuccessActivity extends NoBackActivity {

    private static final String TAG = AutoInfoContants.LOG_J;
    private Button mCommitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_illegal_processing_success);

        mCommitView = (Button) findViewById(R.id.illegal_processing_commit);

        mCommitView.setOnClickListener(clickCommitListener);
    }

    private View.OnClickListener clickCommitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            toMyOrder();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DebugLog.i(TAG, "返回键");
            toMyOrder();
        }
        return false;
    }

    /**
     *
     */
    private void toMyOrder() {
        new SharedPreferencesHelper(IllegalProcessingSuccessActivity.this).setOrderChange(true);
        ActivitySwitchBase.toMain(IllegalProcessingSuccessActivity.this, 2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
