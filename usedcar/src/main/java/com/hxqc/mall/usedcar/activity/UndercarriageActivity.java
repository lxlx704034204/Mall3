package com.hxqc.mall.usedcar.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.BaseSharedPreferencesHelper;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.adapter.UndercarriageReasonAdapter;
import com.hxqc.mall.usedcar.api.UsedCarApiClient;
import com.hxqc.mall.usedcar.model.OffsaleReason;
import com.hxqc.util.JSONUtils;
import com.hxqc.widget.GridViewNoSlide;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : 钟学东
 * @Since : 2015-09-09
 * FIXME
 * Todo
 */
public class UndercarriageActivity extends BackActivity {
    UsedCarApiClient usedCarApiClient;
    GridViewNoSlide gridViewNoSlideView;
    EditText mOtherReasonView;
    Button mSubmitView;
    TextView mPhoneView;
    List<OffsaleReason> temporarily;
    List<OffsaleReason> offsaleReasons = new ArrayList<>();

    UndercarriageReasonAdapter mAdapter;
    Context context;

    String content = "";
    String reasonId = "";
    String reason = "";
    String car_source_no;
    String from;
    String number;
    BaseSharedPreferencesHelper baseSharedPreferencesHelper;
    int SUCCESS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_undercarriage);
        usedCarApiClient = new UsedCarApiClient();
        context = UndercarriageActivity.this;
        baseSharedPreferencesHelper = new BaseSharedPreferencesHelper(this);
        number = UserInfoHelper.getInstance().getPhoneNumber(this);
        car_source_no = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString("car_source_no");
        from = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA).getString("from");
        initView();
        initDate();
        initEvent();
    }

    private void initEvent() {
        gridViewNoSlideView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < offsaleReasons.size(); i++) {
                    offsaleReasons.get(i).setIsSelect(false);
                }
                mAdapter.getItem(position).setIsSelect(true);
                mAdapter.notifyDataSetChanged();
                content = mAdapter.getItem(position).content;
                if (content.equals("其他原因")) {
                    reasonId = "";
                    mOtherReasonView.setFocusableInTouchMode(true);
                    mOtherReasonView.setFocusable(true);
                } else {
                    reason = "";
                    mOtherReasonView.setText("");
                    reasonId = mAdapter.getItem(position).getId();
                    mOtherReasonView.setFocusable(false);
                    mOtherReasonView.setFocusableInTouchMode(false);
                }

            }
        });

        mPhoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4001868555"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        mSubmitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (content.equals("其他原因")) {
                    reason = mOtherReasonView.getText().toString().trim();
                    if (reason.equals("")) {
                        ToastHelper.showYellowToast(context, "请输入其他原因");
                        return;
                    }
                }

                if (reasonId.equals("") && reason.equals("")) {
                    ToastHelper.showYellowToast(context, "请选择下架原因");
                    return;
                }


                if (from.equals("person")) {
                    usedCarApiClient.offSale(number, car_source_no, reasonId, reason, new LoadingAnimResponseHandler(context) {
                        @Override
                        public void onSuccess(String response) {
                            Intent intent = getIntent();
                            UndercarriageActivity.this.setResult(SUCCESS, intent);
                            UndercarriageActivity.this.finish();
                        }
                    });
                } else if (from.equals("platform")) {
                    usedCarApiClient.applyOffSale(number, car_source_no, reasonId, reason, new LoadingAnimResponseHandler(context) {
                        @Override
                        public void onSuccess(String response) {
                            Intent intent = getIntent();
                            UndercarriageActivity.this.setResult(SUCCESS, intent);
                            UndercarriageActivity.this.finish();
                        }
                    });
                }
            }
        });
    }

    private void initDate() {
        usedCarApiClient.getOffsaleReason(new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                temporarily = JSONUtils.fromJson(response, new TypeToken<List<OffsaleReason>>() {
                });
                offsaleReasons.addAll(temporarily);
                OffsaleReason reason = new OffsaleReason();
                reason.content = "其他原因";
                offsaleReasons.add(reason);
                mAdapter = new UndercarriageReasonAdapter(context, offsaleReasons);
                gridViewNoSlideView.setAdapter(mAdapter);
            }
        });
    }

    private void initView() {
        gridViewNoSlideView = (GridViewNoSlide) findViewById(R.id.gridview);
        mOtherReasonView = (EditText) findViewById(R.id.et_reason);
        mOtherReasonView.setFocusable(false);
        mSubmitView = (Button) findViewById(R.id.bt_submit);
        mPhoneView = (TextView) findViewById(R.id.tv_call_number);
    }
}
