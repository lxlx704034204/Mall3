package com.hxqc.mall.usedcar.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.adapter.CarReportCheckboxAdapter;
import com.hxqc.mall.usedcar.api.UsedCarApiClient;
import com.hxqc.mall.usedcar.utils.UsedCarSPHelper;

import java.util.List;

/**
 * 举报页面
 * Created by huangyi on 15/10/28.
 */
public class ReportActivity extends BackActivity implements View.OnClickListener {

    EditText mDetailView;
    EditText mPhoneNoView;
    Button mSubmitView;
    CarReportCheckboxAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report);

        mDetailView = (EditText) findViewById(R.id.tv_detail);
        mPhoneNoView = (EditText) findViewById(R.id.tv_phone);
        mSubmitView = (Button) findViewById(R.id.bt_submit);

        mAdapter = new CarReportCheckboxAdapter(new UsedCarSPHelper(this).getReportReason().reason, this);
        ((GridView) findViewById(R.id.gv)).setAdapter(mAdapter);
        mSubmitView.setOnClickListener(this);
        findViewById(R.id.ll_call_phone).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bt_submit) {
            String phoneNumberStr = mPhoneNoView.getText().toString().trim();
            //手机号不能为空
            if (phoneNumberStr.length() == 0) {
                ToastHelper.showYellowToast(ReportActivity.this, "请填写手机号码");
                mPhoneNoView.requestFocus();
                return;
            }
            //手机号首位为1 并有11位即为对的
            if (phoneNumberStr.length() != 11 || (!"1".equals("" + phoneNumberStr.subSequence(0, 1)))) {
                ToastHelper.showYellowToast(ReportActivity.this, "手机号码格式不对");
                mPhoneNoView.requestFocus();
                return;
            }
            //获取选中的举报原因
            List<String> selectedDatas = mAdapter.getSelectedDatas();
            StringBuffer sb = new StringBuffer();
            for(String str : selectedDatas) {
                sb.append(str).append(",");
            }
            if (sb.length() != 0)
                sb.deleteCharAt(sb.length() - 1);//删除最后一个逗号

            if (TextUtils.isEmpty(sb) && TextUtils.isEmpty(mDetailView.getText().toString().trim())) {
                ToastHelper.showYellowToast(ReportActivity.this, "请选择或输入举报原因");
                mDetailView.requestFocus();
                return;
            }

            new UsedCarApiClient().sendReport("" + sb, mDetailView.getText().toString().trim(), phoneNumberStr, getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString("carNo"), new LoadingAnimResponseHandler(this, true) {
                @Override
                public void onStart() {
                    super.onStart();
                    mSubmitView.setEnabled(false);
                }

                @Override
                public void onSuccess(String response) {
                    ToastHelper.showGreenToast(ReportActivity.this, " 提交举报成功");
                    ToastHelper.toastFinish(ReportActivity.this);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    mSubmitView.setEnabled(true);
                }
            });

        } else if (i == R.id.ll_call_phone) {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "400-733-6622")));
        }
    }

}
