package com.hxqc.mall.activity.me;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.auto.controler.AutoInfoControl;
import com.hxqc.mall.auto.inter.CallBackControl;
import com.hxqc.mall.auto.model.MyAuto;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.api.UserApiClient;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.OtherUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.payment.model.Wallet;
import com.hxqc.mall.views.MenuPopupWindow;
import com.hxqc.util.JSONUtils;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import hxqc.mall.R;

/**
 * Author:李烽
 * Date:2016-08-09
 * FIXME
 * Todo 我的钱包 (2016.8.9版本)
 */
public class MyWalletActivity extends BackActivity implements MenuPopupWindow.OnStateChangeListener {
    private MenuPopupWindow mPopupWindow;
    private View shadowView;

    private TextView totalNumber;

    private boolean toCharge = false;//记录用户去充值
    private Button queryScoreBtn;
    private Button comsumerDetailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        initView();
        checkAuto();
        mPopupWindow = new MenuPopupWindow(this);
        mPopupWindow.setOnStateChangeListener(this);
        loadData();
    }

    private void checkAuto() {
        AutoInfoControl.getInstance().requestAutoInfo(this, new CallBackControl.CallBack<ArrayList<MyAuto>>() {
            @Override
            public void onSuccess(ArrayList<MyAuto> response) {
                if (response != null) {
                    if (response.size() > 0) {
                        queryScoreBtnShow();
                    } else {
                        queryScoreBtnHide();
                    }
                } else {
                    queryScoreBtnHide();
                }
            }

            @Override
            public void onFailed(boolean offLine) {
                queryScoreBtnHide();
            }
        });
    }

    private void queryScoreBtnShow() {
        queryScoreBtn.setVisibility(View.VISIBLE);
        comsumerDetailBtn.setBackgroundResource(R.drawable.btn_white);
        comsumerDetailBtn.setTextColor(getResources().getColor(R.color.title_and_main_text));
    }

    private void queryScoreBtnHide() {
        queryScoreBtn.setVisibility(View.GONE);
        comsumerDetailBtn.setBackgroundResource(R.drawable.btn_orange);
        comsumerDetailBtn.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (toCharge)
            loadData();

    }

    private void loadData() {
        //加载数据
        new UserApiClient().getWalletData(new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                Wallet wallet = JSONUtils.fromJson(response, new TypeToken<Wallet>() {
                });
                if (wallet != null)
                    refreshUI(wallet);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                super.onFailure(statusCode, headers, responseBody, error);
                ToastHelper.showRedToast(MyWalletActivity.this, "获取数据失败");
            }
        });
    }

    private void refreshUI(Wallet wallet) {

//        String balance =
//                OtherUtil.formatBalance(wallet.balance);
        String balance = OtherUtil.formatPriceString(OtherUtil.reformatPrice(wallet.balance));
        totalNumber.setText(balance);

    }


    private void initView() {
        totalNumber = (TextView) findViewById(R.id.total_balance_number);
        shadowView = findViewById(R.id.shadow_view);
        queryScoreBtn = (Button) findViewById(R.id.query_score_btn);
        comsumerDetailBtn = (Button) findViewById(R.id.comsumer_detail_btn);
    }

    public void toQueryScore(View view) {
        ActivitySwitchBase.toCenterAutoInfo(this, "");
    }

    @Override
    public void onStateChange(boolean isShow) {
        if (isShow) {
            shadowView.setVisibility(View.VISIBLE);
        } else {
            shadowView.setVisibility(View.GONE);
        }
    }

    public void charge(View v) {
        toCharge = true;
        ActivitySwitcher.toRecharge(MyWalletActivity.this);
    }

    public void toConsumerDetail(View v) {
        if (UserInfoHelper.getInstance().isLogin(this))
            ActivitySwitcher.toMyBillList(MyWalletActivity.this, true);
    }

    public void useBalance(View v) {
        mPopupWindow.showAtLocation(findViewById(R.id.root_layout),
                Gravity.BOTTOM, 0, 0);
    }


    @Override
    public void onBackPressed() {
        if (mPopupWindow != null && mPopupWindow.isShowing())
            mPopupWindow.dismiss();
        else finish();
    }

}
