package com.hxqc.mall.activity.auto;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hxqc.mall.core.api.BaseMallJsonHttpResponseHandler;
import com.hxqc.mall.core.controler.UserInfoHelper;
import com.hxqc.mall.core.model.auto.AutoDetail;
import com.hxqc.mall.core.model.auto.AutoItem;
import com.hxqc.mall.core.util.activityutil.ActivitySwitcher;
import com.hxqc.mall.views.auto.AutoCommonColorGroup;
import com.hxqc.mall.views.auto.AutoCommonInformationViewGroup;
import com.hxqc.mall.views.auto.AutoDetailSubsidy;
import com.hxqc.mall.views.dialog.ArrivalDialog;

import hxqc.mall.R;

/**
 * Author: HuJunJie
 * Date: 2015-04-23
 * FIXME
 * Todo
 */
public class AutoItemDetailCommonActivity extends AutoItemDetailActivity implements
        AutoCommonColorGroup.OnAutoCommonColorGroupSelectListener {
    AutoCommonColorGroup mColorGroup;
    //车辆基本信息
    AutoCommonInformationViewGroup mAutoBaseInformationView;
    //关注按钮
    TextView mFocusView;
    //关注状态改变
    boolean changeFocus = false;
//    AutoSQLDataHelper mDataBaseHelper;
    //车辆是特卖车辆
    TextView mItemIsPromotionView;
    AutoDetailSubsidy mSubsidyView;//补贴

    @Override
    int getContentView() {
        return R.layout.activity_auto_item_common_detail;
    }


    @Override
    public String getItemType() {
        return AutoItem.AUTO_COMMON;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mDataBaseHelper = new AutoSQLDataHelper(this);
        //颜色
        mColorGroup = (AutoCommonColorGroup) findViewById(R.id.color_view_group);
        mColorGroup.setOnColorSelectListener(this);
        //基本信息
        mAutoBaseInformationView = (AutoCommonInformationViewGroup) findViewById(R.id.base_information_view_group);
        mFocusView = (TextView) findViewById(R.id.focus_view);
        mItemIsPromotionView = (TextView) findViewById(R.id.is_promotion);
        mSubsidyView = (AutoDetailSubsidy) findViewById(R.id.auto_subsidy);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String token = UserInfoHelper.getInstance().getToken(AutoItemDetailCommonActivity.this);
        if (changeFocus && !TextUtils.isEmpty(token)) {
            mAutoItemDataControl.requestServer();
        }
    }

    @Override
    public void onItemDetailSuccess(AutoDetail autoDetail) {

        super.onItemDetailSuccess(autoDetail);
        if (autoDetail == null)
            return;
//        if (mDataBaseHelper != null) {
//            mDataBaseHelper.insertAutoHistory(autoDetail.getAutoBaseInformation());
//        }
        mColorGroup.colorConfig(autoDetail);
        mAutoBaseInformationView.setInformation(autoDetail);
        setFocus(autoDetail.getIsCollect());
        mSubsidyView.setSubsidy(autoDetail);
        setBottomBarState(autoDetail);
    }

    private void setBottomBarState(AutoDetail autoDetail) {
        if (!TextUtils.isEmpty(autoDetail.getPromotionID())) {
            //该车辆是特卖车辆
            mItemIsPromotionView.setVisibility(View.VISIBLE);
            bottomBarToBuyView.setCanBuy(getString(R.string.auto_common_to_promotion));
        } else {
            mItemIsPromotionView.setVisibility(View.GONE);
            switch (autoDetail.transactionStatus()) {

                case UNAVAILABLE:
                    autoAvailable(true);
                    break;
                case NORMAL:
                    autoAvailable(false);
                    bottomBarToBuyView.setCanBuy(getString(R.string.buy_it_now));
                    break;
                case SELLOUT:
                    autoAvailable(false);
                    bottomBarToBuyView.setNotCanBuy(getString(R.string.temporarily_out_of_stock));
                    break;
                default:
                    autoAvailable(false);
                    bottomBarToBuyView.setEnabled(true);
                    break;
            }
        }
    }


    /**
     * 关注
     */
    protected void changeFocus() {
        final boolean isFocus = (boolean) mFocusView.getTag();
        mApiClient.collect(itemID, !isFocus, new BaseMallJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                setFocus(!isFocus);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                changeFocus = false;
            }
        });
    }

    void setFocus(boolean isFocus) {
        mFocusView.setTag(isFocus);
        if (isFocus) {
            mFocusView.setText("已关注");
            mFocusView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable
                    .ic_focus_selected), null, null);
        } else {
            mFocusView.setText("关注");
            mFocusView.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable
                    .ic_focus_normal), null, null);
        }
    }

    //关注
    public void clickOnFocus(View view) {

        UserInfoHelper.getInstance().loginAction(this,new UserInfoHelper.OnLoginListener(){

            @Override
            public void onLoginSuccess() {
                changeFocus();
            }
        });

    }

    /**
     * 刷新车辆
     */
    public void refreshAuto() {
        mAutoItemDataControl.setConfig(AutoItemDetailCommonActivity.this);
        mAutoItemDataControl.requestServer();
    }

    @Override
    public void openBuyVerify(View view) {
        AutoDetail autoDetail = mAutoItemDataControl.getAutoDetail();
        if (autoDetail == null) {
            return;
        }
        if (TextUtils.isEmpty(autoDetail.getPromotionID())) {
            super.openBuyVerify(view);

            switch (autoDetail.transactionStatus()) {
                case SELLOUT:
                    //售完
                    ArrivalDialog dialog = new ArrivalDialog(this, autoDetail.getItemID());
                    dialog.show();
                    break;
            }
        } else {
            //车辆是特卖车辆，跳转至特卖详情

            ActivitySwitcher.toAutoItemDetail(this, AutoItem.AUTO_PROMOTION,
                    autoDetail.getPromotionID(), getIntent().getStringExtra(ActivitySwitcher.TITLE));
            finish();
        }

    }


    @Override
    public void onSelectColorAction(String itemID) {
        //选择颜色刷新
        this.itemID = itemID;
        refreshAuto();
    }
}
