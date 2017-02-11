package com.hxqc.mall.usedcar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hxqc.mall.activity.BackActivity;
import com.hxqc.mall.core.api.LoadingAnimResponseHandler;
import com.hxqc.mall.core.util.ActivitySwitchBase;
import com.hxqc.mall.core.util.ImageUtil;
import com.hxqc.mall.core.util.ToastHelper;
import com.hxqc.mall.core.views.dialog.NormalDialog;
import com.hxqc.mall.usedcar.R;
import com.hxqc.mall.usedcar.api.UsedCarApiClient;
import com.hxqc.mall.usedcar.model.SellCarDetailModel;
import com.hxqc.mall.usedcar.model.SellCarInfo;
import com.hxqc.mall.usedcar.utils.UsedCarActivitySwitcher;
import com.hxqc.util.JSONUtils;


/**
 * @Author : 吕飞
 * @Since : 2017-1-11
 * FIXME
 * <p/>
 * 个人发布车源对应操作
 * 待审核       待上架    修改、删除
 * 通过审核     上  架    下架、已售、修改、删除
 * 下  架    上架、修改、删除
 * 已  售    删除
 * 未通过审核   待上架    删除
 * <p/>
 * 平台发布车源对应操作
 * 待审核       待上架    无
 * 通过审核     上  架    申请下架
 * 申请下架  上架
 * 下  架    无
 * 已  售    无
 * 未通过审核  待上架    无
 */
public class SellCarDetailInfoActivity extends BackActivity implements View.OnClickListener {
    public static SellCarDetailInfoActivity instance = null;
    ImageView mCarPhotoView;
    ImageView mTagView;
    TextView mCarNameView;
    TextView mStatusView;
    TextView mOnCardTimeView;
    TextView mUploadTimeView;
    TextView mCarNoView;
    TextView mRangeView;
    TextView mReasonView;
    TextView mPriceView;
    Button mButton1View;
    Button mButton2View;
    Button mButton3View;
    Button mButton4View;
    SellCarInfo mCarInfo;
    RelativeLayout mHeadView;
    boolean mDeleted;
    UsedCarApiClient mUsedCarApiClient;
    Button[] mButtons;
    SellCarDetailModel mSellCarDetailModel;
    int PLATFORM_UNDERCARRIAGE = 1;
    int PERSON_UNDERCARRIAGE = 2;
    String mNumber;
    String mCarSourceNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_car_detail);
        instance = this;
        mUsedCarApiClient = new UsedCarApiClient();
        Bundle bundle = getIntent().getBundleExtra(ActivitySwitchBase.KEY_DATA);
        mNumber = bundle.getString("phoneNumber");
        mCarSourceNo = bundle.getString("carSourceNo");
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDate();
    }

    public void getDate() {
        mUsedCarApiClient.getSellCarDetail(mNumber, mCarSourceNo, new LoadingAnimResponseHandler(this) {
            @Override
            public void onSuccess(String response) {
                mCarInfo = JSONUtils.fromJson(response, new TypeToken<SellCarInfo>() {
                });
                if (mCarInfo != null)
                    initDate();
            }
        });
    }

    private void initDate() {
        mButton1View.setOnClickListener(this);
        mButton2View.setOnClickListener(this);
        mButton3View.setOnClickListener(this);
        mButton4View.setOnClickListener(this);
        mCarNameView.setText(mCarInfo.car_name);
        mCarNoView.setText(mCarInfo.getCarNoText());
        mOnCardTimeView.setText(mCarInfo.getOnCardTimeText());
        mRangeView.setText(mCarInfo.getRangeText());
        mPriceView.setText(mCarInfo.getItemPrice());
        mUploadTimeView.setText(mCarInfo.getUploadTimeText());
        ImageUtil.setImage(this, mCarPhotoView, mCarInfo.path);
        if (mCarInfo.publish_from.equals("1")) {
            mTagView.setBackgroundResource(R.mipmap.me_sellcar_platform);
        } else {
            mTagView.setBackgroundResource(R.mipmap.me_sellcar_personal);
            showButton();
        }
        mStatusView.setText(mCarInfo.getProductStatus());
        if (mCarInfo.getProductStatus().equals("车辆状态：未通过审核") || mCarInfo.getProductStatus().equals("车辆状态：下架")) {
            mReasonView.setVisibility(View.VISIBLE);
            mReasonView.setText(mCarInfo.getReasonText());
        } else {
            mReasonView.setVisibility(View.GONE);
        }
    }

    private void showButton() {
        if (mCarInfo.publish_from.equals("1")) {
            mButtons[0].setVisibility(View.GONE);
            mButtons[1].setVisibility(View.GONE);
            mButtons[2].setVisibility(View.GONE);
            mButtons[3].setVisibility(View.GONE);
            switch (mCarInfo.product_status) {
                case "3":
                    mButtons[0].setVisibility(View.VISIBLE);
                    mButtons[0].setText("申请下架");
                    break;
                case "1":
                    mButtons[0].setVisibility(View.VISIBLE);
                    mButtons[0].setText("上架");
                    break;
            }
        } else {
            mButtons[0].setVisibility(View.VISIBLE);
            mButtons[1].setVisibility(View.GONE);
            mButtons[2].setVisibility(View.GONE);
            mButtons[3].setVisibility(View.GONE);
            switch (mCarInfo.product_status) {
                case "0":
                    mButtons[1].setVisibility(View.VISIBLE);
                    mButtons[0].setText("删除");
                    mButtons[1].setText("修改");
                    break;
                case "1":
                case "2":
                    mButtons[1].setVisibility(View.VISIBLE);
                    mButtons[2].setVisibility(View.VISIBLE);
                    mButtons[0].setText("上架");
                    mButtons[1].setText("删除");
                    mButtons[2].setText("修改");
                    break;
                case "3":
                    mButtons[1].setVisibility(View.VISIBLE);
                    mButtons[2].setVisibility(View.VISIBLE);
                    mButtons[3].setVisibility(View.VISIBLE);
                    mButtons[0].setText("已售");
                    mButtons[1].setText("下架");
                    mButtons[2].setText("删除");
                    mButtons[3].setText("修改");
                    break;
                case "4":
                    mButtons[0].setText("删除");
                    break;
            }
        }
    }

    //平台下架
    private void applyOffSale() {
        Intent intent = new Intent();
        intent.putExtra("car_source_no", mCarInfo.car_source_no);
        intent.putExtra("from", "platform");
        intent.setClass(this, UndercarriageActivity.class);
        startActivityForResult(intent, PLATFORM_UNDERCARRIAGE);
    }

    //个人下架
    private void soldOut() {
        Intent intent = new Intent(this, UndercarriageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("car_source_no", mCarInfo.car_source_no);
        bundle.putString("from", "person");
        intent.putExtra(ActivitySwitchBase.KEY_DATA, bundle);
        startActivityForResult(intent, PERSON_UNDERCARRIAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            ToastHelper.showGreenToast(getApplicationContext(), "车源已下架");
            mCarInfo.product_status = "1";
            showButton();
        }
    }

    private void initView() {
        mUploadTimeView = (TextView) findViewById(R.id.upload_time);
        mCarNoView = (TextView) findViewById(R.id.car_no);
        mCarPhotoView = (ImageView) findViewById(R.id.car_photo);
        mTagView = (ImageView) findViewById(R.id.tag);
        mCarNameView = (TextView) findViewById(R.id.car_name);
        mStatusView = (TextView) findViewById(R.id.car_status);
        mOnCardTimeView = (TextView) findViewById(R.id.on_card_time);
        mReasonView = (TextView) findViewById(R.id.reason);
        mRangeView = (TextView) findViewById(R.id.range);
        mPriceView = (TextView) findViewById(R.id.price);
        mButton1View = (Button) findViewById(R.id.button_1);
        mButton2View = (Button) findViewById(R.id.button_2);
        mButton3View = (Button) findViewById(R.id.button_3);
        mButton4View = (Button) findViewById(R.id.button_4);
        mHeadView = (RelativeLayout) findViewById(R.id.head);
        mButtons = new Button[]{mButton1View, mButton2View, mButton3View, mButton4View};
        mHeadView.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    @Override
    public void onClick(final View v) {
        int i = v.getId();
        if (i == R.id.head) {
            if (mCarInfo.publish_from.equals("1")) {
                UsedCarActivitySwitcher.toPlatformCarDetail(SellCarDetailInfoActivity.this, mCarInfo.car_source_no);
            } else if (!mDeleted) {
                UsedCarActivitySwitcher.toPersonalCarDetail(SellCarDetailInfoActivity.this, mCarInfo.car_source_no);
            }
        } else if (((Button) v).getText().equals("修改")) {
            v.setClickable(false);
            mUsedCarApiClient.getSellDetail(mNumber, mCarInfo.car_source_no, new LoadingAnimResponseHandler(this) {
                @Override
                public void onSuccess(String response) {
                    mSellCarDetailModel = JSONUtils.fromJson(response, new TypeToken<SellCarDetailModel>() {
                    });
                    if (mSellCarDetailModel != null) {
                        UsedCarActivitySwitcher.toSellCarFromSellCarDetail(SellCarDetailInfoActivity.this, mSellCarDetailModel);
                    }
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    v.setClickable(true);
                }
            });
        } else if (((Button) v).getText().equals("删除")) {
            v.setClickable(false);
            new NormalDialog(this, "删除不可恢复,您确定要删除么?") {
                @Override
                protected void doNext() {
                    mUsedCarApiClient.delete(mNumber, mCarInfo.car_source_no, new LoadingAnimResponseHandler(SellCarDetailInfoActivity.this) {
                        @Override
                        public void onSuccess(String response) {
                            ToastHelper.showGreenToast(getContext(), "车源已删除");
                            mDeleted = true;
                            mButton1View.setText("已删除");
                            mButton2View.setVisibility(View.GONE);
                            mButton3View.setVisibility(View.GONE);
                            mButton4View.setVisibility(View.GONE);
                            mStatusView.setText("车辆状态：已删除");
                            mReasonView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            v.setClickable(true);
                        }
                    });
                }
            }.show();
        } else if (((Button) v).getText().equals("下架")) {
            soldOut();
        } else if (((Button) v).getText().equals("已售")) {
            v.setClickable(false);
            new NormalDialog(SellCarDetailInfoActivity.this, "您确定此车已售吗?") {
                @Override
                protected void doNext() {
                    mUsedCarApiClient.sold(mNumber, mCarInfo.car_source_no, new LoadingAnimResponseHandler(SellCarDetailInfoActivity.this) {
                        @Override
                        public void onSuccess(String response) {
                            ToastHelper.showGreenToast(getApplicationContext(), "更改成功");
                            mReasonView.setVisibility(View.GONE);
                            mCarInfo.product_status = "4";
                            mStatusView.setText(mCarInfo.getProductStatus());
                            showButton();
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            v.setClickable(true);
                        }
                    });
                }
            }.show();
        } else if (((Button) v).getText().equals("上架")) {
            v.setClickable(false);
            mUsedCarApiClient.onSale(mNumber, mCarInfo.car_source_no, new LoadingAnimResponseHandler(SellCarDetailInfoActivity.this) {
                @Override
                public void onSuccess(String response) {
                    ToastHelper.showGreenToast(getApplicationContext(), "恭喜！您的车已重新上架");
                    mCarInfo.product_status = "3";
                    mReasonView.setVisibility(View.GONE);
                    mStatusView.setText(mCarInfo.getProductStatus());
                    showButton();
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    v.setClickable(true);
                }
            });
        } else if (((Button) v).getText().equals("申请下架")) {
            applyOffSale();
        }
    }
}
